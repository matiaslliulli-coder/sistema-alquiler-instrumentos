# Semana 1 – Diseño hexagonal del módulo de Alquileres

El **dominio** (Alquiler, Cliente, Devolucion, Penalidad y la regla `CalculadoraPenalidades`) no conoce
Spring ni la base de datos. Los casos de uso (servicios) lo orquestan y los adaptadores (REST y JPA) lo conectan con el exterior.

```mermaid
flowchart LR
    subgraph ENTRADA["Adaptadores de entrada (web.controller)"]
        A1[AlquilerController]
        A2[DevolucionController]
        A3[InstrumentoController]
        A4[ClienteController]
    end
    subgraph APP["Aplicación (application.service)"]
        S1[AlquilerService]
        S2[DevolucionService]
        S3[HistorialService]
        S4[ClienteService]
    end
    subgraph DOM["Dominio (domain)"]
        D1[Alquiler / Cliente / InstrumentoRef]
        D2[Devolucion / Penalidad / TarifaPenalidad]
        D3[CalculadoraPenalidades]
    end
    subgraph SALIDA["Adaptadores de salida (infrastructure.persistence)"]
        R1[(Repositorios JPA)]
        DB[(PostgreSQL alquileres_db)]
    end
    A1 --> S1
    A2 --> S2
    A3 --> S3
    A4 --> S4
    S1 --> D1
    S2 --> D2
    S2 --> D3
    S3 --> D1
    S1 --> R1
    S2 --> R1
    S3 --> R1
    S4 --> R1
    R1 --> DB
```

## Paquetes
```
com.unifranz.sistemaalquileresinstrumentos
├── domain                      entidades JPA, enums y CalculadoraPenalidades (regla pura)
├── infrastructure.persistence  repositorios Spring Data
├── application
│   ├── dto                     requests / responses
│   └── service                 casos de uso
├── web
│   ├── controller              API REST
│   └── exception               errores en JSON
└── config                      CORS para el frontend
```

## Integración futura
`InstrumentoRef` es una copia mínima del inventario. En la semana 10, al integrar con el módulo de Matías,
se reemplaza por una consulta al inventario real (la tabla `instrumentos`) sin tocar las reglas del dominio.
