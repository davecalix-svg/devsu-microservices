package com.devsu.cuentaservice.event;

import com.devsu.cuentaservice.config.RabbitConfig;
import com.devsu.cuentaservice.entity.ClienteRef;
import com.devsu.cuentaservice.repository.ClienteRefRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ClienteEventListener {

    private final ClienteRefRepository repository;

    public ClienteEventListener(ClienteRefRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void handleClienteCreado(ClienteCreadoEvent event){

        ClienteRef ref = new ClienteRef();
        ref.setClienteId(event.getClienteId());
        repository.save(ref);

        System.out.println("Cliente recibido: " + event.getClienteId());
    }
}
