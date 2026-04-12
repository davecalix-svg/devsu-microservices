package com.devsu.cuentaservice.service;

import com.devsu.cuentaservice.dto.CuentaRequestDTO;
import com.devsu.cuentaservice.dto.CuentaResponseDTO;

import java.util.List;

public interface CuentaService {

    CuentaResponseDTO crear(CuentaRequestDTO request);
    List<CuentaResponseDTO> listar();
    CuentaResponseDTO obtenerPorId(Long id);
    CuentaResponseDTO actualizar(Long id, CuentaRequestDTO request);
}
