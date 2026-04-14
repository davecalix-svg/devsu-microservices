package com.devsu.clienteservice.controller;

import com.devsu.clienteservice.dto.ClienteRequestDTO;
import com.devsu.clienteservice.dto.ClienteResponseDTO;
import com.devsu.clienteservice.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Operaciones sobre clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Operation(summary = "Crear cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> crear(
            @Valid @RequestBody ClienteRequestDTO request) {

        ClienteResponseDTO response = clienteService.crear(request);

        return ResponseEntity
                .status(201)
                .body(response);
    }

    @Operation(summary = "Listar todos los clientes")
    @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listar() {

        List<ClienteResponseDTO> response = clienteService.listar();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener cliente por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> obtener(
            @Parameter(description = "ID del cliente", example = "1")
            @PathVariable Long id) {

        ClienteResponseDTO response = clienteService.obtenerPorId(id);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Actualizar cliente completamente (PUT)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> actualizar(
            @Parameter(description = "ID del cliente", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO request) {

        ClienteResponseDTO response = clienteService.actualizar(id, request);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Actualizar parcialmente un cliente (PATCH)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado parcialmente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> actualizarParcial(
            @Parameter(description = "ID del cliente", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO request) {

        ClienteResponseDTO response = clienteService.actualizarParcial(id, request);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar cliente por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del cliente", example = "1")
            @PathVariable Long id) {

        clienteService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}