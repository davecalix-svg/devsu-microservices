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

@Tag(name = "Clientes", description = "Operaciones sobre clientes")
@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @Operation(summary = "Crear cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ClienteResponseDTO crear(@Valid @RequestBody ClienteRequestDTO clienteRequestDTO) {
        return clienteService.crear(clienteRequestDTO);
    }

    @Operation(summary = "Listar todos los clientes")
    @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida correctamente")
    @GetMapping
    public List<ClienteResponseDTO> listar() {
        return clienteService.listar();
    }

    @Operation(summary = "Obtener cliente por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping("/{id}")
    public ClienteResponseDTO obtener(
            @Parameter(description = "ID del cliente", example = "1")
            @PathVariable Long id
    ) {
        return clienteService.obtenerPorId(id);
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
            @Valid @RequestBody ClienteRequestDTO clienteRequestDTO
    ) {
        return ResponseEntity.ok(clienteService.actualizar(id, clienteRequestDTO));
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
            @RequestBody ClienteRequestDTO clienteRequestDTO
    ) {
        return ResponseEntity.ok(clienteService.actualizarParcial(id, clienteRequestDTO));
    }

    @Operation(summary = "Eliminar cliente por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del cliente", example = "1")
            @PathVariable Long id
    ) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
