package com.devsu.cuentaservice;

import com.devsu.cuentaservice.dto.ReporteResponseDTO;
import com.devsu.cuentaservice.entity.Cuenta;
import com.devsu.cuentaservice.entity.Movimiento;
import com.devsu.cuentaservice.enums.TipoMovimiento;
import com.devsu.cuentaservice.external.ClienteClient;
import com.devsu.cuentaservice.repository.CuentaRepository;
import com.devsu.cuentaservice.repository.MovimientoRepository;
import com.devsu.cuentaservice.service.ReporteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class ReporteServiceImplTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private ClienteClient clienteClient;

    @InjectMocks
    private ReporteServiceImpl reporteService;

    private Long clienteId;
    private LocalDate fecha;

    @BeforeEach
    void setUp() {
        clienteId = 1L;
        fecha = LocalDate.of(2024, 1, 1);
    }

    @Test
    void shouldReturnLastMovementBalance_whenReportHasMovements() {

        // Arrange
        when(clienteClient.obtenerNombreCliente(clienteId)).thenReturn("Juan Perez");

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta("123");
        cuenta.setTipoCuenta("AHORROS");
        cuenta.setSaldoInicial(BigDecimal.valueOf(100.0));

        when(cuentaRepository.findByClienteId(clienteId))
                .thenReturn(List.of(cuenta));

        Movimiento m1 = new Movimiento();
        m1.setFecha(LocalDateTime.now());
        m1.setValor(new BigDecimal("50"));
        m1.setSaldo(BigDecimal.valueOf(150.0));
        m1.setTipoMovimiento(TipoMovimiento.CREDITO);

        Movimiento m2 = new Movimiento();
        m2.setFecha(LocalDateTime.now());
        m2.setValor(new BigDecimal("-20"));
        m2.setSaldo(BigDecimal.valueOf(130.0));
        m2.setTipoMovimiento(TipoMovimiento.DEBITO);

        when(movimientoRepository.findByCuentaAndFechaBetween(any(), any(), any()))
                .thenReturn(List.of(m1, m2));

        // Act
        ReporteResponseDTO response = reporteService.generarReporte(clienteId, fecha);

        // Assert
        assertNotNull(response);
        assertEquals("Juan Perez", response.getNombreCliente());
        assertEquals(1, response.getCuentas().size());

        var cuentaDTO = response.getCuentas().get(0);

        assertEquals("123", cuentaDTO.getNumeroCuenta());
        assertEquals(new BigDecimal("130.0"), cuentaDTO.getSaldo());
        assertEquals(2, cuentaDTO.getMovimientos().size());
    }

    @Test
    void shouldUseInitialBalance_whenReportHasNoMovements(){

        // Arrange
        when(clienteClient.obtenerNombreCliente(clienteId)).thenReturn("Maria");

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta("456");
        cuenta.setTipoCuenta("CORRIENTE");
        cuenta.setSaldoInicial(BigDecimal.valueOf(200.0));

        when(cuentaRepository.findByClienteId(clienteId))
                .thenReturn(List.of(cuenta));

        when(movimientoRepository.findByCuentaAndFechaBetween(any(), any(), any()))
                .thenReturn(List.of());

        // Act
        ReporteResponseDTO response = reporteService.generarReporte(clienteId, fecha);

        // Assert
        var cuentaDTO = response.getCuentas().get(0);

        assertEquals(new BigDecimal("200.0"), cuentaDTO.getSaldo());
        assertTrue(cuentaDTO.getMovimientos().isEmpty());
    }

    @Test
    void shouldReturnEmptyList_whenGeneratingReportWithoutAccounts() {

        // Arrange
        when(clienteClient.obtenerNombreCliente(clienteId)).thenReturn("Carlos");

        when(cuentaRepository.findByClienteId(clienteId))
                .thenReturn(List.of());

        // Act
        ReporteResponseDTO response = reporteService.generarReporte(clienteId, fecha);

        // Assert
        assertNotNull(response);
        assertTrue(response.getCuentas().isEmpty());
    }
}
