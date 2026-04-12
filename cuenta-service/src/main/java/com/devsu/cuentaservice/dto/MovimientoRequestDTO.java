package com.devsu.cuentaservice.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MovimientoRequestDTO {

    @NotNull(message = "El id de la cuenta es obligatorio")
    private Long cuentaId;

    @NotNull(message = "El valor es obligatorio")
    @Digits(integer = 10, fraction = 2, message = "Formato de valor inválido")
    private BigDecimal valor;
}