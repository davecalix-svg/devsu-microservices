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

        if (!clienteRefRepository.existsById(request.getClienteId())) {
            throw new BusinessException("Cliente no existe", HttpStatus.NOT_FOUND);
        }

        // Validar duplicado
        if (cuentaRepository.existsByNumeroCuenta(request.getNumeroCuenta())) {
            throw new BusinessException("Ya existe una cuenta con ese número", HttpStatus.CONFLICT);
        }

        // Mapear DTO → Entity
        Cuenta cuenta = cuentaMapper.toEntity(request);

        // Guardar
        Cuenta saved = cuentaRepository.save(cuenta);

        // Mapear Entity → DTO
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
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cuenta no encontrada", HttpStatus.NOT_FOUND));

        return cuentaMapper.toResponseDTO(cuenta);
    }

    @Override
    public CuentaResponseDTO actualizar(Long id, CuentaRequestDTO request) {

        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cuenta no encontrada", HttpStatus.NOT_FOUND));

        if (!clienteRefRepository.existsById(request.getClienteId())) {
            throw new BusinessException("Cliente no existe", HttpStatus.NOT_FOUND);
        }

        // Validar cambio de número de cuenta
        if (!cuenta.getNumeroCuenta().equals(request.getNumeroCuenta()) &&
                cuentaRepository.existsByNumeroCuenta(request.getNumeroCuenta())) {
            throw new BusinessException("Ya existe una cuenta con ese número", HttpStatus.CONFLICT);
        }

        // Actualizar campos (NO reemplazar entidad completa)
        cuenta.setNumeroCuenta(request.getNumeroCuenta());
        cuenta.setTipoCuenta(request.getTipoCuenta());
        cuenta.setSaldoInicial(request.getSaldoInicial());
        cuenta.setEstado(request.getEstado());
        cuenta.setClienteId(request.getClienteId());

        Cuenta updated = cuentaRepository.save(cuenta);

        return cuentaMapper.toResponseDTO(updated);
    }
}
