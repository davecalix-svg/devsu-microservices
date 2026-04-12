package com.devsu.clienteservice.service;

import com.devsu.clienteservice.dto.ClienteRequestDTO;
import com.devsu.clienteservice.dto.ClienteResponseDTO;
import com.devsu.clienteservice.entity.Cliente;
import com.devsu.clienteservice.exception.ResourceNotFoundException;
import com.devsu.clienteservice.mapper.ClienteMapper;
import com.devsu.clienteservice.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repository;
    private final ClienteMapper mapper;

    @Override
    public ClienteResponseDTO crear(ClienteRequestDTO clienteRequestDTO) {
        Cliente cliente = mapper.toEntity(clienteRequestDTO);
        return mapper.toDTO(repository.save(cliente));
    }

    @Override
    public ClienteResponseDTO obtenerPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
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
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
        mapper.updateFromDto(dto, existente);
        return mapper.toDTO(repository.save(existente));
    }

    public ClienteResponseDTO actualizarParcial(Long id, ClienteRequestDTO dto) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        mapper.updateFromDto(dto, cliente);
        Cliente actualizado = repository.save(cliente);
        return mapper.toDTO(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
        repository.delete(cliente);
    }
}
