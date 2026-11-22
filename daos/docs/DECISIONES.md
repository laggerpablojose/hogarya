# Decisiones y avance del integrador

Este registro conserva los acuerdos y los resultados de cada jornada. Se actualiza al cerrar la sesión y sirve como punto de partida del día siguiente.

## Acuerdos del 22 de noviembre de 2026

| Decisión | Motivo y alcance |
| --- | --- |
| Pablo desarrolla S03 Incidentes. | Confirmado por Pablo en esta conversación. Los responsables de los otros módulos coordinan sus propios avances. |
| Trabajar por jornadas del 22 al 29, con una PR por incremento. | Pablo quiere comprender responsabilidades, contratos, transacciones y persistencia al revisar código ya implementado. |
| Usar una rama de integración y ramas diarias. | `daos/integrador-2026` reúne el TP. La primera PR parte de `daos/dia-01-base`. Las siguientes se crean al comenzar cada jornada desde la integración revisada. |
| Proyecto DAOS independiente en `daos/`. | Permite importar su propio POM en STS y entregar la etapa 2 con las herramientas de la materia. |
| Copia de trabajo en el Escritorio con sparse checkout. | La ubicación elegida es `Trabajo Práctico Integrador\Actividad 02\hogarya`. Se muestran los archivos de DAOS y las reglas. Git conserva el historial completo. |
| Nombre del proyecto `02-BocchieriDiazLaggerDiaz`. | Sigue el nombre usado en la Actividad 01 y el criterio de la entrega. Se aplicará al esquema MySQL y al ZIP. |
| Java 21, Boot 4.1.2, Wrapper 3.10.0 y springdoc 3.2.0. | Se toma la referencia docente ya revisada. El Wrapper fija Maven y el POM fija dependencias. |
| Puerto 8081 para la API. | Permite reconocer la ejecución DAOS al seguir los ejemplos locales. |
| Incorporar JPA y MySQL en la jornada 2. | Hoy se verifica el arranque y el contrato HTTP. La persistencia se agrega junto a entidades y repositorios, con su explicación y comprobación. |
| `GET /` devuelve el nombre de la API y enlaces. | El punto de entrada permite comprobar JSON, HATEOAS, OpenAPI y Swagger. Sus datos son metadatos de la aplicación. |
| Los servicios de negocio se crean con los casos de uso. | El alta de incidentes necesitará validación del contrato y una transacción que incluya el historial inicial. |
| La resolución pertenece a S04. | S03 comparte el incidente y el historial con el responsable de S04 para conservar una única transacción de resolución. |
| Documentar decisiones y procedimientos dentro del repositorio. | `AGENTS.md` guía la continuidad. Este archivo conserva acuerdos y resultados, y el plan identifica el siguiente incremento. |
| Recordatorios a medida hasta el 29. | Lunes y martes 18:00, miércoles a viernes 08:00, sábado y domingo 15:00, hora de Argentina. El domingo fue confirmado expresamente por Pablo. |

## Jornada 1

Se incorporan el POM, Wrapper, aplicación Spring Boot, un controlador del punto de entrada y un DTO de respuesta con enlaces HATEOAS. Las pruebas comprueban la representación JSON y que los enlaces OpenAPI y Swagger se puedan consultar.

La guía del día explica la importación en STS, la ejecución, Maven, las consultas HTTP y la revisión de la PR. Los modelos y las operaciones de incidentes corresponden a las jornadas siguientes del plan.

La comprobación inicial de Maven requirió permiso para acceder a la carpeta de bibliotecas y descargar dependencias. `.\mvnw.cmd clean verify` terminó con `BUILD SUCCESS`, dos pruebas ejecutadas y cero fallos. Se inició además el JAR y se comprobó con cURL la raíz HAL y sus tres enlaces, todos con HTTP 200. La ejecución de comprobación quedó detenida.

La importación manual en STS queda como primer recorrido de Pablo con la guía del día. La comprobación ejecutada usó el mismo POM mediante Maven Wrapper.

El siguiente incremento es la jornada 2 del 23/11. Se incorporarán entidades, repositorios y conexión MySQL antes de implementar el alta.

Las ramas `daos/integrador-2026` y `daos/dia-01-base` se publicaron en GitHub. La [PR 12](https://github.com/laggerpablojose/hogarya/pull/12) apunta a integración y contiene la base de la primera jornada.
