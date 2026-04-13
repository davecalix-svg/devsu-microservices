package com.devsu.clienteservice.service;

import com.devsu.clienteservice.config.RabbitConfig;
import com.devsu.clienteservice.dto.ClienteRequestDTO;
import com.devsu.clienteservice.dto.ClienteResponseDTO;
import com.devsu.clienteservice.entity.Cliente;
import com.devsu.clienteservice.event.ClienteCreadoEvent;
import com.devsu.clienteservice.exception.BusinessException;
import com.devsu.clienteservice.mapper.ClienteMapper;
import com.devsu.clienteservice.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repository;
    private final ClienteMapper mapper;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public ClienteResponseDTO crear(ClienteRequestDTO clienteRequestDTO) {

        if (repository.existsByIdentificacion(clienteRequestDTO.getIdentificacion())) {
            throw new BusinessException(
                    "Ya existe un cliente con esa identificación",
                    HttpStatus.CONFLICT
            );
        }

        Cliente cliente = mapper.toEntity(clienteRequestDTO);
        Cliente guardado = repository.save(cliente);

        ClienteCreadoEvent event = new ClienteCreadoEvent(guardado.getClienteId());

        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_KEY,
                event
        );

        return mapper.toDTO(guardado);
    }

    @Override
    public ClienteResponseDTO obtenerPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new BusinessException("Cliente no encontrado", HttpStatus.NOT_FOUND));
    }

    @Override
    public List<ClienteResponseDTO> listar() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public ClienteResponseDTO actualizar(Long id, ClienteRequestDTO dto) {
        Cliente existente = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente no encontrado", HttpStatus.NOT_FOUND));

        if (!existente.getEstado()) {
            throw new BusinessException("Cliente inactivo", HttpStatus.BAD_REQUEST);
        }

        if (!existente.getIdentificacion().equals(dto.getIdentificacion()) &&
                repository.existsByIdentificacion(dto.getIdentificacion())) {

            throw new BusinessException(
                    "Ya existe un cliente con esa identificación",
                    HttpStatus.CONFLICT
            );
        }

        mapper.updateFromDto(dto, existente);

        return mapper.toDTO(repository.save(existente));
    }

    public ClienteResponseDTO actualizarParcial(Long id, ClienteRequestDTO dto) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente no encontrado", HttpStatus.NOT_FOUND));
        mapper.updateFromDto(dto, cliente);
        Cliente actualizado = repository.save(cliente);
        return mapper.toDTO(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente no encontrado", HttpStatus.NOT_FOUND));
        repository.delete(cliente);
    }
}
