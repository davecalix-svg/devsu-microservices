package com.devsu.cuentaservice;

import com.devsu.cuentaservice.entity.ClienteRef;
import com.devsu.cuentaservice.repository.ClienteRefRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CuentaIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ClienteRefRepository clienteRefRepository;

    @LocalServerPort
    private int port;

    @Test
    void shouldReturn404WhenClienteNotExists() {

        String url = "http://localhost:" + port + "/api/v1/cuentas";

        Map<String, Object> request = Map.of(
                "numeroCuenta", "75454654",
                "tipoCuenta", "AHORROS",
                "saldoInicial", 1000,
                "estado", true,
                "clienteId", 999
        );

        ResponseEntity<String> response =
                restTemplate.postForEntity(url, request, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldCreateCuentaSuccessfully() {

        // GIVEN → cliente existe
        ClienteRef ref = new ClienteRef();
        ref.setClienteId(1L);
        clienteRefRepository.save(ref);

        String url = "http://localhost:" + port + "/api/v1/cuentas";

        Map<String, Object> request = Map.of(
                "numeroCuenta", "75454654",
                "tipoCuenta", "AHORROS",
                "saldoInicial", 1000,
                "estado", true,
                "clienteId", 1
        );

        // WHEN
        ResponseEntity<String> response =
                restTemplate.postForEntity(url, request, String.class);

        // THEN
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void shouldCreateMovimiento() {

        // GIVEN
        ClienteRef ref = new ClienteRef();
        ref.setClienteId(1L);
        clienteRefRepository.save(ref);

        // crear cuenta primero
        String cuentaUrl = "http://localhost:" + port + "/api/v1/cuentas";

        var cuentaRequest = Map.of(
                "numeroCuenta", "999003",
                "tipoCuenta", "AHORROS",
                "saldoInicial", 1000,
                "estado", true,
                "clienteId", 1
        );

        restTemplate.postForEntity(cuentaUrl, cuentaRequest, String.class);

        // WHEN → crear movimiento
        String movimientoUrl = "http://localhost:" + port + "/api/v1/movimientos";

        var movimientoRequest = Map.of(
                "cuentaId", 1,
                "valor", 500
        );

        ResponseEntity<String> response =
                restTemplate.postForEntity(movimientoUrl, movimientoRequest, String.class);

        // THEN
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void shouldFailWhenSaldoInsuficiente() {

        String url = "http://localhost:" + port + "/api/v1/movimientos";

        var request = Map.of(
                "cuentaId", 1,
                "valor", -5000
        );

        ResponseEntity<String> response =
                restTemplate.postForEntity(url, request, String.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
}
