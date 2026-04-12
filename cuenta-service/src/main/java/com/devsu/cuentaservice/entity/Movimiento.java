package com.devsu.cuentaservice.entity;

import com.devsu.cuentaservice.enums.TipoMovimiento;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos")
@Data
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fecha;
    @Enumerated(EnumType.STRING)
    private TipoMovimiento tipoMovimiento;
    private BigDecimal valor;
    private BigDecimal saldo;
    @ManyToOne
    @JoinColumn(name = "cuenta_id")
    private Cuenta cuenta;
}
