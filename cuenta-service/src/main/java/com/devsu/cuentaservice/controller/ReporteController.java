package com.devsu.cuentaservice.controller;

import com.devsu.cuentaservice.dto.ReporteResponseDTO;
import com.devsu.cuentaservice.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping
    public ResponseEntity<ReporteResponseDTO> generarReporte(
            @RequestParam Long cliente,
            @RequestParam String fecha
    ) {
        LocalDate fechaParsed = LocalDate.parse(fecha);
        return ResponseEntity.ok(
                reporteService.generarReporte(cliente, fechaParsed)
        );
    }
}
