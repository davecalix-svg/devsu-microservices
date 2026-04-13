package com.devsu.cuentaservice.event;

public class ClienteCreadoEvent {
    private Long clienteId;

    public ClienteCreadoEvent() {}

    public ClienteCreadoEvent(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
}

