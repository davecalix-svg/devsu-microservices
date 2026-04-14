package com.devsu.cuentaservice.service;

import com.devsu.cuentaservice.dto.MovimientoRequestDTO;
import com.devsu.cuentaservice.dto.MovimientoResponseDTO;
import com.devsu.cuentaservice.entity.Cuenta;
import com.devsu.cuentaservice.entity.Movimiento;
import com.devsu.cuentaservice.enums.TipoMovimiento;
import com.devsu.cuentaservice.exception.BusinessException;
import com.devsu.cuentaservice.mapper.MovimientoMapper;
import com.devsu.cuentaservice.repository.CuentaRepository;
import com.devsu.cuentaservice.repository.MovimientoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;
    private final MovimientoMapper movimientoMapper;

    @Override
    @Transactional
    public MovimientoResponseDTO registrarMovimiento(MovimientoRequestDTO request) {

        Cuenta cuenta = obtenerCuenta(request.getCuentaId());

        BigDecimal saldoActual = obtenerSaldoActual(cuenta);
        BigDecimal nuevoSaldo = calcularNuevoSaldo(saldoActual, request.getValor());

        validarSaldo(nuevoSaldo);

        Movimiento movimiento = crearMovimiento(request, cuenta, nuevoSaldo);

        Movimiento saved = movimientoRepository.save(movimiento);

        return mapearRespuesta(saved); // 🔥 IMPORTANTE: mantener mapping manual
    }

    private Cuenta obtenerCuenta(Long cuentaId) {
        return cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new BusinessException("Cuenta no encontrada", HttpStatus.NOT_FOUND));
    }

    private BigDecimal calcularNuevoSaldo(BigDecimal saldoActual, BigDecimal valor) {
        return saldoActual.add(valor);
    }

    private void validarSaldo(BigDecimal nuevoSaldo) {
        // Regla de negocio (F3): no permitir saldo negativo
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Saldo no disponible", HttpStatus.CONFLICT);
        }
    }

    private Movimiento crearMovimiento(MovimientoRequestDTO request, Cuenta cuenta, BigDecimal nuevoSaldo) {
        Movimiento movimiento = new Movimiento();
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setValor(request.getValor());
        movimiento.setSaldo(nuevoSaldo);
        movimiento.setCuenta(cuenta);
        movimiento.setTipoMovimiento(TipoMovimiento.fromValor(request.getValor()));
        return movimiento;
    }

    private MovimientoResponseDTO mapearRespuesta(Movimiento saved) {
        MovimientoResponseDTO response = new MovimientoResponseDTO();
        response.setId(saved.getId());
        response.setFecha(saved.getFecha());
        response.setValor(saved.getValor());
        response.setSaldo(saved.getSaldo());
        response.setTipoMovimiento(saved.getTipoMovimiento().name());
        return response;
    }

    private BigDecimal obtenerSaldoActual(Cuenta cuenta) {
        return movimientoRepository
                .findTopByCuentaOrderByFechaDesc(cuenta)
                .map(Movimiento::getSaldo)
                .orElse(Optional.ofNullable(cuenta.getSaldoInicial())
                        .orElse(BigDecimal.ZERO)); // 🔥 NO tocar lógica original
    }

    @Override
    public List<MovimientoResponseDTO> listar() {
        return movimientoRepository.findAll()
                .stream()
                .map(movimientoMapper::toResponseDTO)
                .toList();
    }

    @Override
    public MovimientoResponseDTO obtenerPorId(Long id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Movimiento no encontrado", HttpStatus.NOT_FOUND));

        return movimientoMapper.toResponseDTO(movimiento);
    }
}