package com.devsu.cuentaservice.service;

import com.devsu.cuentaservice.dto.ReporteCuentaDTO;
import com.devsu.cuentaservice.dto.ReporteMovimientoDTO;
import com.devsu.cuentaservice.dto.ReporteResponseDTO;
import com.devsu.cuentaservice.entity.Cuenta;
import com.devsu.cuentaservice.entity.Movimiento;
import com.devsu.cuentaservice.external.ClienteClient;
import com.devsu.cuentaservice.repository.CuentaRepository;
import com.devsu.cuentaservice.repository.MovimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;
    private final ClienteClient clienteClient;

    @Override
    public ReporteResponseDTO generarReporte(Long clienteId, LocalDate fecha) {

        String nombreCliente = clienteClient.obtenerNombreCliente(clienteId);

        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);

        List<ReporteCuentaDTO> cuentasDTO = cuentas.stream().map(cuenta -> {

            // rango del día
            LocalDateTime inicio = fecha.atStartOfDay();
            LocalDateTime fin = fecha.atTime(23, 59, 59);

            List<Movimiento> movimientos = movimientoRepository
                    .findByCuentaAndFechaBetween(cuenta, inicio, fin);

            List<ReporteMovimientoDTO> movimientosDTO = movimientos.stream().map(m -> {

                ReporteMovimientoDTO dto = new ReporteMovimientoDTO();
                dto.setFecha(m.getFecha());
                dto.setTipoMovimiento(m.getTipoMovimiento().name());
                dto.setValor(m.getValor());
                dto.setSaldo(m.getSaldo());
                return dto;
            }).toList();

            ReporteCuentaDTO cuentaDTO = new ReporteCuentaDTO();
            cuentaDTO.setNumeroCuenta(cuenta.getNumeroCuenta());
            cuentaDTO.setTipoCuenta(cuenta.getTipoCuenta());
            cuentaDTO.setSaldo(
                    movimientos.isEmpty()
                            ? cuenta.getSaldoInicial()
                            : movimientos.get(movimientos.size() - 1).getSaldo()
            );
            cuentaDTO.setMovimientos(movimientosDTO);

            return cuentaDTO;

        }).toList();

        ReporteResponseDTO response = new ReporteResponseDTO();
        response.setNombreCliente(nombreCliente);
        response.setCuentas(cuentasDTO);

        return response;
    }
}
