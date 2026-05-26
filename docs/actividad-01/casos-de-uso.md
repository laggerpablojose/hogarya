# Casos de Uso

## Ciclo de alquiler

| Código | Caso de uso                |
| ------ | -------------------------- |
| CU01   | Registrar propiedad        |
| CU02   | Consultar propiedades      |
| CU03   | Crear publicación          |
| CU04   | Gestionar publicación      |
| CU05   | Registrar interesado       |
| CU06   | Registrar interés o visita |
| CU07   | Generar contrato           |
| CU08   | Gestionar contrato         |
| CU09   | Generar factura            |
| CU10   | Consultar facturas         |
| CU11   | Registrar pago             |
| CU12   | Consultar pagos            |

## Gestión de incidentes

| Código | Caso de uso                     |
| ------ | ------------------------------- |
| CU13   | Registrar incidente             |
| CU14   | Consultar incidentes            |
| CU15   | Actualizar estado de incidente  |
| CU16   | Registrar informe de resolución |
| CU17   | Consultar informe de resolución |

## Reglas de negocio relevantes

- Una propiedad no puede tener contratos activos superpuestos.
- Una factura pagada no puede volver a pagarse.
- Los incidentes sólo pueden registrarse sobre contratos vigentes.
- Un informe de resolución sólo puede generarse para incidentes resueltos.
