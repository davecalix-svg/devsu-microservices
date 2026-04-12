package com.devsu.cuentaservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CuentaResponseDTO {
    private Long id;
    private String numeroCuenta;
    private String tipoCuenta;
    private BigDecimal saldoInicial;
    private Boolean estado;
    private Long clienteId;
}