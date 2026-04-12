package com.devsu.cuentaservice.service;

import com.devsu.cuentaservice.dto.MovimientoRequestDTO;
import com.devsu.cuentaservice.dto.MovimientoResponseDTO;

import java.util.List;

public interface MovimientoService {

    MovimientoResponseDTO registrarMovimiento(MovimientoRequestDTO request);
    List<MovimientoResponseDTO> listar();
    MovimientoResponseDTO obtenerPorId(Long id);
}
