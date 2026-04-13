package com.devsu.cuentaservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ClienteRef {

    @Id
    private Long clienteId;
}
