package com.devsu.cuentaservice.controller;

import com.devsu.cuentaservice.dto.MovimientoRequestDTO;
import com.devsu.cuentaservice.dto.MovimientoResponseDTO;
import com.devsu.cuentaservice.service.MovimientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/movimientos")
@RequiredArgsConstructor
@Tag(name = "Movimientos", description = "Operaciones sobre movimientos")
public class MovimientoController {

    private final MovimientoService movimientoService;

    @Operation(summary = "Registrar movimiento (débito/crédito)")
    @PostMapping
    public ResponseEntity<MovimientoResponseDTO> crear(
            @Valid @RequestBody MovimientoRequestDTO request) {

        MovimientoResponseDTO response = movimientoService.registrarMovimiento(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Listar movimientos")
    @GetMapping
    public ResponseEntity<List<MovimientoResponseDTO>> listar() {

        List<MovimientoResponseDTO> response = movimientoService.listar();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener movimiento por ID")
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoResponseDTO> obtener(@PathVariable Long id) {

        MovimientoResponseDTO response = movimientoService.obtenerPorId(id);
        return ResponseEntity.ok(response);
    }
}