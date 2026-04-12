package com.devsu.clienteservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Cliente extends Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clienteId;
    private String contrasena;
    private Boolean estado;
}
