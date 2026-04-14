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
    public ResponseEntity<CuentaResponseDTO> crear(
            @Valid @RequestBody CuentaRequestDTO request) {

        CuentaResponseDTO response = cuentaService.crear(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Listar cuentas")
    @GetMapping
    public ResponseEntity<List<CuentaResponseDTO>> listar() {

        List<CuentaResponseDTO> response = cuentaService.listar();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener cuenta por ID")
    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponseDTO> obtener(@PathVariable Long id) {

        CuentaResponseDTO response = cuentaService.obtenerPorId(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Actualizar cuenta")
    @PutMapping("/{id}")
    public ResponseEntity<CuentaResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CuentaRequestDTO request) {

        CuentaResponseDTO response = cuentaService.actualizar(id, request);
        return ResponseEntity.ok(response);
    }
}