package com.devsu.cuentaservice;

import com.devsu.cuentaservice.dto.MovimientoRequestDTO;
import com.devsu.cuentaservice.entity.Cuenta;
import com.devsu.cuentaservice.entity.Movimiento;
import com.devsu.cuentaservice.exception.BusinessException;
import com.devsu.cuentaservice.repository.CuentaRepository;
import com.devsu.cuentaservice.repository.MovimientoRepository;
import com.devsu.cuentaservice.service.MovimientoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovimientoServiceImplTest {

    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private MovimientoServiceImpl movimientoService;

    @Test
    void shouldCreateMovimientoCredito() {

        MovimientoRequestDTO request = new MovimientoRequestDTO();
        request.setCuentaId(1L);
        request.setValor(new BigDecimal("500"));

        Cuenta cuenta = new Cuenta();
        cuenta.setSaldoInicial(new BigDecimal("1000"));

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.findTopByCuentaOrderByFechaDesc(cuenta))
                .thenReturn(Optional.empty());

        when(movimientoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = movimientoService.registrarMovimiento(request);

        assertNotNull(result);
        assertEquals(new BigDecimal("1500"), result.getSaldo());
        assertEquals("CREDITO", result.getTipoMovimiento());

    }

    @Test
    void shouldCreateMovimientoDebitoUsingLastMovimiento() {

        MovimientoRequestDTO request = new MovimientoRequestDTO();
        request.setCuentaId(1L);
        request.setValor(new BigDecimal("-200"));

        Cuenta cuenta = new Cuenta();

        Movimiento ultimoMovimiento = new Movimiento();
        ultimoMovimiento.setSaldo(new BigDecimal("1000"));

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.findTopByCuentaOrderByFechaDesc(cuenta))
                .thenReturn(Optional.of(ultimoMovimiento));

        when(movimientoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = movimientoService.registrarMovimiento(request);

        assertEquals(new BigDecimal("800"), result.getSaldo());
        assertEquals("DEBITO", result.getTipoMovimiento());
    }

    @Test
    void shouldThrowExceptionWhenSaldoInsuficiente() {

        MovimientoRequestDTO request = new MovimientoRequestDTO();
        request.setCuentaId(1L);
        request.setValor(new BigDecimal("-1500"));

        Cuenta cuenta = new Cuenta();

        Movimiento ultimoMovimiento = new Movimiento();
        ultimoMovimiento.setSaldo(new BigDecimal("1000"));

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.findTopByCuentaOrderByFechaDesc(cuenta))
                .thenReturn(Optional.of(ultimoMovimiento));

        assertThrows(
                BusinessException.class,
                () -> movimientoService.registrarMovimiento(request)
        );

        verify(movimientoRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenCuentaNotFound() {

        MovimientoRequestDTO request = new MovimientoRequestDTO();
        request.setCuentaId(1L);

        when(cuentaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                BusinessException.class,
                () -> movimientoService.registrarMovimiento(request)
        );
    }

    @Test
    void shouldUseZeroWhenNoSaldoInicialAndNoMovimientos() {

        MovimientoRequestDTO request = new MovimientoRequestDTO();
        request.setCuentaId(1L);
        request.setValor(new BigDecimal("100"));

        Cuenta cuenta = new Cuenta();
        cuenta.setSaldoInicial(null);

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.findTopByCuentaOrderByFechaDesc(cuenta))
                .thenReturn(Optional.empty());

        when(movimientoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = movimientoService.registrarMovimiento(request);

        assertEquals(new BigDecimal("100"), result.getSaldo());
    }
}
