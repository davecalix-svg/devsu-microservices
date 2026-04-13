package com.devsu.cuentaservice;

import com.devsu.cuentaservice.dto.CuentaRequestDTO;
import com.devsu.cuentaservice.dto.CuentaResponseDTO;
import com.devsu.cuentaservice.entity.Cuenta;
import com.devsu.cuentaservice.exception.BusinessException;
import com.devsu.cuentaservice.mapper.CuentaMapper;
import com.devsu.cuentaservice.mapper.MovimientoMapper;
import com.devsu.cuentaservice.repository.ClienteRefRepository;
import com.devsu.cuentaservice.repository.CuentaRepository;
import com.devsu.cuentaservice.repository.MovimientoRepository;
import com.devsu.cuentaservice.service.CuentaServiceImpl;
import com.devsu.cuentaservice.service.MovimientoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CuentaServiceImplTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private ClienteRefRepository clienteRefRepository;

    @Mock
    private CuentaMapper cuentaMapper;

    @InjectMocks
    private CuentaServiceImpl cuentaService;

    @Test
    void shouldCreateCuentaSuccessfully() {

        CuentaRequestDTO request = new CuentaRequestDTO();
        request.setNumeroCuenta("999001");
        request.setClienteId(1L);

        Cuenta cuenta = new Cuenta();

        when(clienteRefRepository.existsById(1L)).thenReturn(true);
        when(cuentaRepository.existsByNumeroCuenta("999001")).thenReturn(false);
        when(cuentaMapper.toEntity(request)).thenReturn(cuenta);
        when(cuentaRepository.save(cuenta)).thenReturn(cuenta);
        when(cuentaMapper.toResponseDTO(cuenta)).thenReturn(new CuentaResponseDTO());

        var result = cuentaService.crear(request);

        assertNotNull(result);
        verify(cuentaRepository).save(cuenta);

    }

    @Test
    void shouldThrowExceptionWhenClienteDoesNotExist() {

        CuentaRequestDTO request = new CuentaRequestDTO();
        request.setClienteId(1L);

        when(clienteRefRepository.existsById(1L)).thenReturn(false);

        assertThrows(
                BusinessException.class,
                () -> cuentaService.crear(request)
        );

        verify(cuentaRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenCuentaAlreadyExists() {

        CuentaRequestDTO request = new CuentaRequestDTO();
        request.setNumeroCuenta("999001");
        request.setClienteId(1L);

        when(clienteRefRepository.existsById(1L)).thenReturn(true);
        when(cuentaRepository.existsByNumeroCuenta("999001")).thenReturn(true);

        assertThrows(
                BusinessException.class,
                () -> cuentaService.crear(request)
        );

        verify(cuentaRepository, never()).save(any());

    }

}
