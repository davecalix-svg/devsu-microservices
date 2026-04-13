package com.devsu.clienteservice.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteRequestDTO {

    private Long clienteId;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El género es obligatorio")
    private String genero;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 0, message = "La edad debe ser mayor o igual a 0")
    private Integer edad;

    @NotBlank(message = "La identificación es obligatoria")
    private String identificacion;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;
}