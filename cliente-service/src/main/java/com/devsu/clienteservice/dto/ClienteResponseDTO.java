package com.devsu.clienteservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteResponseDTO {
    private Long clienteId;
    private String nombre;
    private String identificacion;
    private Boolean estado;
}
