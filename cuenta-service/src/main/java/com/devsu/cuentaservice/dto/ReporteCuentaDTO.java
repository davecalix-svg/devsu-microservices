package com.devsu.cuentaservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ReporteCuentaDTO {

    private String numeroCuenta;
    private String tipoCuenta;
    private BigDecimal saldo;
    private List<ReporteMovimientoDTO> movimientos;
}
