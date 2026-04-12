package com.devsu.cuentaservice.controller;

import com.devsu.cuentaservice.dto.CuentaRequestDTO;
import com.devsu.cuentaservice.dto.CuentaResponseDTO;
import com.devsu.cuentaservice.service.CuentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cuentas")
@RequiredArgsConstructor
@Tag(name = "Cuentas", description = "Operaciones sobre cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    @Operation(summary = "Crear una cuenta")
    @PostMapping
    public ResponseEntity<CuentaResponseDTO> crearCuenta(
            @Valid @RequestBody CuentaRequestDTO request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cuentaService.crear(request));
    }

    @Operation(summary = "Listar cuentas")
    @GetMapping
    public ResponseEntity<List<CuentaResponseDTO>> listarCuentas() {
        return ResponseEntity.ok(cuentaService.listar());
    }

    @Operation(summary = "Obtener cuenta por ID")
    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponseDTO> obtenerCuenta(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.obtenerPorId(id));
    }

    @Operation(summary = "Actualizar cuenta")
    @PutMapping("/{id}")
    public ResponseEntity<CuentaResponseDTO> actualizarCuenta(
            @PathVariable Long id,
            @Valid @RequestBody CuentaRequestDTO request) {

        return ResponseEntity.ok(cuentaService.actualizar(id, request));
    }

    @GetMapping("/ping")
    public String ping() {
        return "Cuenta-service activo";
    }
}