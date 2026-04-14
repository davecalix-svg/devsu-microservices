# Devsu Challenge - Arquitectura de Microservicios

## Descripción

Sistema financiero basado en microservicios para gestión de clientes,
cuentas, movimientos y reportes.

------------------------------------------------------------------------

## Arquitectura del Sistema

flowchart LR
    User[👤 Cliente]
    ClienteService[🟦 cliente-service]
    CuentaService[🟩 cuenta-service]

    DB[(🗄️ MySQL)]
    RabbitMQ[📨 RabbitMQ]

    ClienteRef[(📄 cliente_ref)]

    User -->|HTTP| ClienteService
    User -->|HTTP| CuentaService

    ClienteService -->|JPA| DB
    ClienteService -->|Evento| RabbitMQ

    RabbitMQ -->|Evento| CuentaService

    CuentaService --> ClienteRef
    CuentaService --> DB

    CuentaService -->|WebFlux| ClienteService

    %% estilos
    style RabbitMQ fill:#ffe4b5,stroke:#ff8c00,stroke-width:2px
    style DB fill:#e0f7fa,stroke:#00796b,stroke-width:2px

------------------------------------------------------------------------

## Swagger / OpenAPI

Puedes acceder a la documentación de APIs en:

-   **cliente-service**

        http://localhost:8081/swagger-ui.html

-   **cuenta-service**

        http://localhost:8082/swagger-ui.html

> Nota: Los puertos dependen de tu configuración en `docker-compose`

------------------------------------------------------------------------

## Flujos

### Creación de Cliente

1.  cliente-service crea cliente
2.  Publica evento en RabbitMQ
3.  cuenta-service consume evento
4.  Guarda cliente_id en cliente_ref

### Creación de Cuenta

1.  cuenta-service valida cliente en cliente_ref
2.  Crea cuenta

### Reportes

1.  cuenta-service obtiene cuentas/movimientos
2.  Consulta cliente-service vía WebFlux
3.  Genera reporte

------------------------------------------------------------------------

## Base de Datos

Script incluido: BaseDatos.sql

Tablas: - clientes - cliente_ref - cuenta - movimientos

------------------------------------------------------------------------

## Ejecución

``` bash
docker-compose up -d
```

------------------------------------------------------------------------

## Postman

Se incluyen: - collection.json - environment.json

------------------------------------------------------------------------

## Endpoints

### Clientes

-   POST /clientes
-   GET /clientes
-   GET /clientes/{id}
-   PUT /clientes/{id}
-   PATCH /clientes/{id}
-   DELETE /clientes/{id}

### Cuentas

-   POST /cuentas
-   GET /cuentas
-   GET /cuentas/{id}
-   PUT /cuentas/{id}

### Movimientos

-   POST /movimientos
-   GET /movimientos

### Reportes

GET /reportes?cliente={id}&fecha={yyyy-MM-dd}

------------------------------------------------------------------------

## Pruebas

-   Unitarias
-   Integración

------------------------------------------------------------------------

## Docker

Incluye: - docker-compose - servicios levantados automáticamente

------------------------------------------------------------------------

## Notas Técnicas

-   RabbitMQ → comunicación asíncrona
-   WebFlux → comunicación reactiva
-   cliente_ref → consistencia eventual

------------------------------------------------------------------------

## Autor

Prueba técnica backend - David Guzman - https://www.linkedin.com/in/davecalix/

