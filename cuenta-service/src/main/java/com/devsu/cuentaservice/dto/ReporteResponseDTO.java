package com.devsu.cuentaservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReporteResponseDTO {

    private Long clienteId;
    private String nombreCliente; // opcional si luego integras cliente-service
    private List<ReporteCuentaDTO> cuentas;
}
