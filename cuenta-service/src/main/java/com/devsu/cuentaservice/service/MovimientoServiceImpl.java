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

        // 1. Obtener cuenta
        Cuenta cuenta = cuentaRepository.findById(request.getCuentaId())
                .orElseThrow(() -> new BusinessException("Cuenta no encontrada"));

        // 2. Obtener saldo actual
        BigDecimal saldoActual = obtenerSaldoActual(cuenta);

        // 3. Calcular nuevo saldo
        BigDecimal nuevoSaldo = saldoActual.add(request.getValor());

        // 4. Validar saldo
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Saldo no disponible");
        }

        // 5. Crear movimiento
        Movimiento movimiento = new Movimiento();
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setValor(request.getValor());
        movimiento.setSaldo(nuevoSaldo);
        movimiento.setCuenta(cuenta);
        movimiento.setTipoMovimiento(TipoMovimiento.fromValor(request.getValor()));

        // 6. Guardar
        Movimiento saved = movimientoRepository.save(movimiento);

        // 7. Mapear respuesta (manual simple)
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
                        .orElse(BigDecimal.ZERO));
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
                .orElseThrow(() -> new BusinessException("Movimiento no encontrado"));

        return movimientoMapper.toResponseDTO(movimiento);
    }
}
