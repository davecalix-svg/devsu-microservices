package com.devsu.cuentaservice.dto;

import lombok.Data;

@Data
public class ClienteResponseDTO {

    private Long id;
    private String nombre;
    private Boolean estado;
}
