# Plan diario de S03 Incidentes

Pablo desarrollará S03 entre el 22 y el 29/11/2026. Cada jornada termina con código comprobado, una PR hacia `daos/integrador-2026`, explicación de las responsabilidades y actualización del registro de decisiones. La entrega grupal vence el 29/11 a las 23:59.

| Día y recordatorio en Argentina | Concepto para comprender | Incremento y comprobación | Rama diaria |
| --- | --- | --- | --- |
| **1 · domingo 22/11 · sesión inicial** | Proyecto, dependencias, arranque y representación de un recurso. | Crear la base Maven, `GET /` con enlaces, OpenAPI y pruebas del punto de entrada y su documentación. | `daos/dia-01-base` |
| **2 · lunes 23/11 · 18:00** | Entidad, relación, repositorio y diferencia entre JPA, Hibernate y MySQL. | Agregar persistencia y configurar el esquema del TP. Modelar Incidente y CambioEstadoIncidente con fecha y hora. Acordar con el equipo los datos mínimos de Contrato y la relación con los informes de S04. Comprobar mapeos y datos iniciales en MySQL. | `daos/dia-02-persistencia` |
| **3 · martes 24/11 · 18:00** | Contrato de entrada, validación y transacción del caso de uso. | Implementar `POST /incidentes`, validar contrato existente, activo, sin baja y vigente. Generar abierto e historial inicial juntos. Responder 201 con Location. Probar alta y rechazos 400, 404 y 409. | `daos/dia-03-alta-incidentes` |
| **4 · miércoles 25/11 · 08:00** | Separación de entidades y DTO, consulta e historial. | Implementar `GET /incidentes/{id}` y `GET /incidentes/{id}/historial`. Orden cronológico y ausencia de edición del historial. Documentar y probar las representaciones y sus enlaces disponibles. | `daos/dia-04-consultas` |
| **5 · jueves 26/11 · 08:00** | Búsqueda sobre recursos y contrato de filtros. | Implementar `GET /incidentes` con filtros acordados por contrato, estado, categoría y prioridad. Probar combinaciones y lista vacía. Coordinar los enlaces contrato e informes con las consultas del equipo. | `daos/dia-05-filtros` |
| **6 · viernes 27/11 · 08:00** | Máquina de estados y atomicidad. | Implementar `PATCH /incidentes/{id}/estado`, validar las cinco transiciones de S03 y guardar cambio e historial juntos. Acordar con S04 el mecanismo de bloqueo para cambios concurrentes. Probar transiciones rechazadas sin escrituras parciales. | `daos/dia-06-estados` |
| **7 · sábado 28/11 · 15:00** | Colaboración entre servicios y consistencia del proceso. | Integrar con el trabajo de S04. Ejecutar alta → atención → resolución → reapertura → atención → segunda resolución. Verificar dos informes, historial y conflictos concurrentes. Completar los seis casos reproducibles de S03 y revisar OpenAPI. | `daos/dia-07-integracion` |
| **8 · domingo 29/11 · 15:00** | Reproducibilidad, contrato entregable y defensa de decisiones. | Ejecutar desde esquema limpio. Revisar datos, casos cURL o Postman, proyecto STS, integrantes, ZIP y URL. Cerrar ajustes de S03 y revisar el paquete con el equipo. | `daos/dia-08-entrega` |

## Criterio de avance

La rama del día siguiente sale de la integración una vez revisada la PR anterior. Si sigue abierta, primero se revisa y se decide su integración. Cada sesión comienza con una explicación breve del recorrido que se va a implementar y termina siguiendo una solicitud real por sus capas.

Antes de cerrar una jornada, Pablo debería poder explicar qué recibe el controlador, qué decide el servicio, qué guarda el repositorio y por qué una operación puede fallar. Se usan ejemplos de la jornada para comprobar la comprensión y registrar lo que convenga retomar.

El 28/11 depende de la disponibilidad del módulo S04 y de las consultas auxiliares del grupo. Esa coordinación empieza el 23/11. Si hay cambios de alcance o retrasos, actualizar este plan con evidencia y conservar la revisión final del 29.

## Estado comprobado

- Jornada 1 implementada y verificada. `.\mvnw.cmd clean verify` terminó con dos pruebas exitosas. El JAR respondió HTTP 200 en la raíz y en sus enlaces OpenAPI y Swagger. Falta la revisión de la PR con Pablo para incorporar la base a integración.
- Jornadas 2 a 8 pendientes.
- Recordatorios configurados en esta conversación para los horarios indicados. Convocan a la sesión diaria.
