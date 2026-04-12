package com.devsu.cuentaservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_cuenta", unique = true, nullable = false)
    private String numeroCuenta;
    private String tipoCuenta; // AHORROS / CORRIENTE
    private BigDecimal saldoInicial;
    private Boolean estado;
    private Long clienteId; // referencia al otro microservicio
}
