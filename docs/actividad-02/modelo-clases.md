# Modelo de Clases – HogarYA

## Objetivo

Representar la estructura conceptual del dominio del sistema HogarYA mediante un modelo UML de clases, identificando entidades, atributos y relaciones relevantes para los procesos de alquileres e incidentes de mantenimiento.

## Alcance

El modelo contempla los dos procesos principales definidos en el enunciado:

- Gestión del ciclo de alquiler:
  - Propiedad
  - Publicación
  - Interés / Visita
  - Contrato
  - Facturación
  - Pagos

- Gestión de incidentes:
  - Reclamo
  - Seguimiento
  - Resolución

## Entidades principales

### Gestión de alquileres

- Propietario
- Propiedad
- Publicacion
- Interesado
- InteresVisita
- Inquilino
- Contrato
- Factura
- PagoFactura

### Gestión de incidentes

- Incidente
- CambioEstadoIncidente
- InformeResolucion

## Relaciones destacadas

- Un Propietario puede poseer múltiples Propiedades.
- Una Propiedad puede tener una Publicacion.
- Un Interesado puede registrar múltiples intereses o visitas.
- Un Contrato vincula una Propiedad, un Propietario y un Inquilino.
- Un Contrato genera Facturas mensuales.
- Una Factura puede tener un único PagoFactura.
- Un Contrato puede tener múltiples Incidentes.
- Un Incidente registra múltiples cambios de estado.
- Un Incidente puede generar un InformeResolucion.

## Restricciones de negocio relevantes

- Una propiedad no puede tener contratos activos superpuestos.
- Un contrato solo puede activarse sobre propiedades disponibles.
- No se puede registrar un pago sobre una factura anulada o ya pagada.
- Una factura pagada debe poseer exactamente un pago asociado.
- Un incidente solo puede registrarse sobre contratos vigentes.
- Un informe de resolución solo puede generarse para incidentes resueltos.

## Artefactos asociados

- `hogarya-clases.drawio`
- `hogarya-clases.png`
- `hogarya-clases.pdf`
