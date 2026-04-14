package com.devsu.cuentaservice.service;

import com.devsu.cuentaservice.dto.CuentaRequestDTO;
import com.devsu.cuentaservice.dto.CuentaResponseDTO;
import com.devsu.cuentaservice.entity.Cuenta;
import com.devsu.cuentaservice.exception.BusinessException;
import com.devsu.cuentaservice.mapper.CuentaMapper;
import com.devsu.cuentaservice.repository.ClienteRefRepository;
import com.devsu.cuentaservice.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteRefRepository clienteRefRepository;
    private final CuentaMapper cuentaMapper;

    @Override
    public CuentaResponseDTO crear(CuentaRequestDTO request) {

        validarClienteExiste(request.getClienteId());
        validarNumeroCuentaUnico(request.getNumeroCuenta());

        Cuenta cuenta = cuentaMapper.toEntity(request);
        Cuenta saved = cuentaRepository.save(cuenta);

        return cuentaMapper.toResponseDTO(saved);
    }

    @Override
    public List<CuentaResponseDTO> listar() {
        return cuentaRepository.findAll()
                .stream()
                .map(cuentaMapper::toResponseDTO)
                .toList();
    }

    @Override
    public CuentaResponseDTO obtenerPorId(Long id) {
        Cuenta cuenta = obtenerCuentaPorId(id);
        return cuentaMapper.toResponseDTO(cuenta);
    }

    @Override
    public CuentaResponseDTO actualizar(Long id, CuentaRequestDTO request) {

        Cuenta cuenta = obtenerCuentaPorId(id);

        validarClienteExiste(request.getClienteId());
        validarNumeroCuentaUnicoEnActualizacion(cuenta, request.getNumeroCuenta());

        actualizarDatosCuenta(cuenta, request);

        Cuenta updated = cuentaRepository.save(cuenta);

        return cuentaMapper.toResponseDTO(updated);
    }

    private Cuenta obtenerCuentaPorId(Long id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cuenta no encontrada", HttpStatus.NOT_FOUND));
    }

    private void validarClienteExiste(Long clienteId) {
        if (!clienteRefRepository.existsById(clienteId)) {
            throw new BusinessException("Cliente no existe", HttpStatus.NOT_FOUND);
        }
    }

    private void validarNumeroCuentaUnico(String numeroCuenta) {
        if (cuentaRepository.existsByNumeroCuenta(numeroCuenta)) {
            throw new BusinessException("Ya existe una cuenta con ese número", HttpStatus.CONFLICT);
        }
    }

    private void validarNumeroCuentaUnicoEnActualizacion(Cuenta cuenta, String numeroCuenta) {
        boolean cambiaNumero = !cuenta.getNumeroCuenta().equals(numeroCuenta);

        if (cambiaNumero && cuentaRepository.existsByNumeroCuenta(numeroCuenta)) {
            throw new BusinessException("Ya existe una cuenta con ese número", HttpStatus.CONFLICT);
        }
    }

    private void actualizarDatosCuenta(Cuenta cuenta, CuentaRequestDTO request) {
        cuenta.setNumeroCuenta(request.getNumeroCuenta());
        cuenta.setTipoCuenta(request.getTipoCuenta());
        cuenta.setSaldoInicial(request.getSaldoInicial());
        cuenta.setEstado(request.getEstado());
        cuenta.setClienteId(request.getClienteId());
    }
}