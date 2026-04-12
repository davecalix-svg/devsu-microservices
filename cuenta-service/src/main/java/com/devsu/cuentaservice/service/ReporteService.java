package com.devsu.cuentaservice.service;

import com.devsu.cuentaservice.dto.ReporteResponseDTO;

import java.time.LocalDate;

public interface ReporteService {

    ReporteResponseDTO generarReporte(Long clienteId, LocalDate fecha);
}
