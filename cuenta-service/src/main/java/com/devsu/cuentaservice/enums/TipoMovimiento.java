package com.devsu.cuentaservice.enums;

import java.math.BigDecimal;

public enum TipoMovimiento {
    CREDITO,
    DEBITO;

    public static TipoMovimiento fromValor(BigDecimal valor) {
        return valor.compareTo(BigDecimal.ZERO) > 0
                ? CREDITO
                : DEBITO;
    }
}
