package com.devsu.clienteservice.event;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClienteCreadoEvent {

    private Long clienteId;
    public ClienteCreadoEvent() {}

    public ClienteCreadoEvent(Long clienteId) {
        this.clienteId = clienteId;
    }

}
