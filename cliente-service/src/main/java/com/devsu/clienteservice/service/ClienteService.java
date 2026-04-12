package com.devsu.clienteservice.service;

import com.devsu.clienteservice.dto.ClienteRequestDTO;
import com.devsu.clienteservice.dto.ClienteResponseDTO;

import java.util.List;

public interface ClienteService {
    ClienteResponseDTO crear(ClienteRequestDTO clienteRequestDTO);
    ClienteResponseDTO obtenerPorId(Long id);
    List<ClienteResponseDTO> listar();
    ClienteResponseDTO actualizar(Long id, ClienteRequestDTO dto);
    ClienteResponseDTO actualizarParcial(Long id, ClienteRequestDTO dto);
    void eliminar(Long id);
}
