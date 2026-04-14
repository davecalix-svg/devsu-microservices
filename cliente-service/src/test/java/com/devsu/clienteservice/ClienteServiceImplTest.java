package com.devsu.clienteservice;

import com.devsu.clienteservice.dto.ClienteRequestDTO;
import com.devsu.clienteservice.dto.ClienteResponseDTO;
import com.devsu.clienteservice.entity.Cliente;
import com.devsu.clienteservice.event.ClienteCreadoEvent;
import com.devsu.clienteservice.exception.BusinessException;
import com.devsu.clienteservice.mapper.ClienteMapper;
import com.devsu.clienteservice.repository.ClienteRepository;
import com.devsu.clienteservice.service.ClienteServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
@ActiveProfiles("test")
public class ClienteServiceImplTest {

    @Mock
    private ClienteRepository repository;

    @Mock
    private ClienteMapper mapper;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private ClienteServiceImpl service;

    @Test
    void shouldCreateClienteSuccessfully() {

        ClienteRequestDTO dto = new ClienteRequestDTO();
        dto.setIdentificacion("123");

        Cliente entity = new Cliente();
        entity.setClienteId(1L);

        when(repository.existsByIdentificacion("123")).thenReturn(false);
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);

        when(mapper.toDTO(entity)).thenReturn(new ClienteResponseDTO());

        var result = service.crear(dto);

        assertNotNull(result);
        verify(repository).save(entity);
        verify(rabbitTemplate).convertAndSend(
                eq("cliente.exchange"),
                eq("cliente.creado"),
                any(ClienteCreadoEvent.class)
        );

    }

    @Test
    void shouldThrowExceptionWhenDuplicatedIdentificacion() {

        ClienteRequestDTO dto = new ClienteRequestDTO();
        dto.setIdentificacion("123");

        when(repository.existsByIdentificacion("123")).thenReturn(true);

        assertThrows(
                BusinessException.class,
                () -> service.crear(dto)
        );
    }
}
