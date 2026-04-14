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

    private final ClienteRepository clienteRepository;
    private final ClienteMapper mapper;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public ClienteResponseDTO crear(ClienteRequestDTO dto) {

        validarIdentificacionUnica(dto.getIdentificacion());

        Cliente cliente = mapper.toEntity(dto);
        Cliente guardado = clienteRepository.save(cliente);

        publicarEventoClienteCreado(guardado);

        return mapper.toDTO(guardado);
    }

    @Override
    public ClienteResponseDTO obtenerPorId(Long id) {
        Cliente cliente = obtenerCliente(id);
        return mapper.toDTO(cliente);
    }

    @Override
    public List<ClienteResponseDTO> listar() {
        return clienteRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public ClienteResponseDTO actualizar(Long id, ClienteRequestDTO dto) {

        Cliente cliente = obtenerCliente(id);

        validarClienteActivo(cliente);
        validarIdentificacionUnicaEnActualizacion(cliente, dto.getIdentificacion());

        mapper.updateFromDto(dto, cliente);

        Cliente actualizado = clienteRepository.save(cliente);

        return mapper.toDTO(actualizado);
    }

    @Override
    public ClienteResponseDTO actualizarParcial(Long id, ClienteRequestDTO dto) {

        Cliente cliente = obtenerCliente(id);

        mapper.updateFromDto(dto, cliente);

        Cliente actualizado = clienteRepository.save(cliente);

        return mapper.toDTO(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        Cliente cliente = obtenerCliente(id);
        clienteRepository.delete(cliente);
    }

    // =========================
    // Métodos privados
    // =========================

    private Cliente obtenerCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente no encontrado", HttpStatus.NOT_FOUND));
    }

    private void validarClienteActivo(Cliente cliente) {
        if (!cliente.getEstado()) {
            throw new BusinessException("Cliente inactivo", HttpStatus.BAD_REQUEST);
        }
    }

    private void validarIdentificacionUnica(String identificacion) {
        if (clienteRepository.existsByIdentificacion(identificacion)) {
            throw new BusinessException(
                    "Ya existe un cliente con esa identificación",
                    HttpStatus.CONFLICT
            );
        }
    }

    private void validarIdentificacionUnicaEnActualizacion(Cliente cliente, String identificacion) {
        boolean cambiaIdentificacion = !cliente.getIdentificacion().equals(identificacion);

        if (cambiaIdentificacion && clienteRepository.existsByIdentificacion(identificacion)) {
            throw new BusinessException(
                    "Ya existe un cliente con esa identificación",
                    HttpStatus.CONFLICT
            );
        }
    }

    private void publicarEventoClienteCreado(Cliente cliente) {
        ClienteCreadoEvent event = new ClienteCreadoEvent(cliente.getClienteId());

        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_KEY,
                event
        );
    }
}
