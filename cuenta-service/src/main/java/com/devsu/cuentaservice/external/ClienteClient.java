package com.devsu.cuentaservice.external;

import com.devsu.cuentaservice.dto.ClienteResponseDTO;
import com.devsu.cuentaservice.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@Slf4j
public class ClienteClient {

    private final WebClient webClient;

    public ClienteClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public String obtenerNombreCliente(Long clienteId) {

        ClienteResponseDTO response = webClient.get()
                .uri("/api/v1/clientes/{id}", clienteId)
                .retrieve()

                // 4xx → cliente no existe
                .onStatus(HttpStatusCode::is4xxClientError, res -> {
                    log.warn("Cliente no encontrado. clienteId={}", clienteId);
                    return Mono.error(() ->
                            new BusinessException("Cliente no encontrado", HttpStatus.NOT_FOUND)
                    );
                })

                // 5xx → error en cliente-service
                .onStatus(HttpStatusCode::is5xxServerError, res -> {
                    log.error("Error 5xx en cliente-service. clienteId={}", clienteId);
                    return Mono.error(() ->
                            new RuntimeException("Servicio de clientes no disponible")
                    );
                })

                .bodyToMono(ClienteResponseDTO.class)

                // timeout controlado
                .timeout(Duration.ofSeconds(3))

                // errores de conexión (service caído, DNS, etc.)
                .onErrorMap(WebClientRequestException.class, ex -> {
                    log.error("Error de conexión con cliente-service. clienteId={}", clienteId, ex);
                    return new RuntimeException("No se pudo conectar con cliente-service");
                })

                // errores HTTP (por si se escapa algo del onStatus)
                .onErrorMap(WebClientResponseException.class, ex -> {
                    log.error("Error HTTP llamando cliente-service. status={}, clienteId={}",
                            ex.getStatusCode(), clienteId);
                    return new RuntimeException("Error consultando cliente-service");
                })

                .block();

        // defensa extra (no debería pasar)
        if (response == null || response.getNombre() == null) {
            log.error("Respuesta inválida de cliente-service. clienteId={}", clienteId);
            throw new RuntimeException("Respuesta inválida de cliente-service");
        }

        return response.getNombre();
    }
}