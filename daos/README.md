# HogarYA · Integrador DAOS 2026

El integrador se entrega el **domingo 29/11/2026 a las 23:59**. El equipo implementará una API REST integrada con cuatro módulos y un esquema MySQL compartido. **Pablo desarrolla S03 Incidentes**, confirmado el 22/11. El plan actual distribuye su trabajo en ocho jornadas del 22 al 29. [Enunciado de la Actividad 02](https://tuti.sied.utn.edu.ar/mod/resource/view.php?id=7160) · [Entrega en Moodle](https://tuti.sied.utn.edu.ar/mod/assign/view.php?id=7163)

La jornada 1 incorpora una aplicación Spring Boot ejecutable, su punto de entrada JSON con HATEOAS y la documentación OpenAPI. Para comenzar, seguir [la guía del día 1](docs/GUIA-DIA-01.md). Las jornadas siguientes están en [el plan diario](docs/PLAN.md), y los acuerdos y resultados se conservan en [el registro de decisiones](docs/DECISIONES.md). [AGENTS.md](../AGENTS.md) contiene las reglas para continuar con agentes.

## Fuentes y alcance

- **La Actividad 02 define lo que se implementa y evalúa.** Usar el enunciado general y el informe y BPMN de la Actividad 01 de DAOS para entender los procesos. Por ejemplo, el BPMN describe la reapertura y el reinicio del tratamiento como proceso. La API debe registrar dos transiciones explícitas, de resuelto a reabierto y de reabierto a en proceso. [Material del integrador](https://tuti.sied.utn.edu.ar/course/section.php?id=1097)
- **HogarYA de DESI aporta el dominio y la persistencia de partida.** Revisar las entidades, repositorios, validaciones y datos de ejemplo. Los controladores existentes sirven vistas Thymeleaf y algunos servicios reciben formularios de presentación. En DAOS usaremos DTO para el contrato HTTP y mantendremos las reglas en servicios. [HogarYA DESI, revisión 3c04ea6](https://github.com/laggerpablojose/hogarya/tree/3c04ea626b549aab79c84fd6fc2012eea6de4beb)
- **El proyecto del profesor aporta ejemplos técnicos.** Revisar `ContratoRestController` y `ContratoResponseDTO` para rutas, códigos HTTP y enlaces, y `ExceptionHandler` para el tratamiento centralizado. Los datos y recursos a exponer salen del enunciado de HogarYA. [Código de clase, revisión bdb0edd](https://github.com/dardokuttel2/daos2026/tree/bdb0edd7d5a636ee799806d5f1d366588d1fc7b9) · [Demostración del profesor](https://youtu.be/Er9UREp_TNY)

## Organización del repositorio

HogarYA ya está registrado como repositorio del grupo en Moodle. La rama de integración es `daos/integrador-2026`. El proyecto Maven de esta etapa está en `daos/`. Las PR diarias de Pablo apuntan a la rama de integración y usan ramas `daos/dia-NN-descripcion`. La copia local del Escritorio usa sparse checkout para mostrar `daos/`, `AGENTS.md` y los archivos de Git necesarios. Git conserva el historial para consultar DESI. [Registro del repositorio](https://tuti.sied.utn.edu.ar/mod/assign/view.php?id=7505)

El proyecto se llama `02-BocchieriDiazLaggerDiaz`, siguiendo el ZIP de la Actividad 01. Se usará el mismo nombre para el esquema MySQL y el ZIP. La persistencia y su configuración local se incorporan en la jornada 2. El punto de entrada de la jornada 1 funciona en el puerto `8081`.

## Herramientas

| Herramienta | Uso y comprobación del 06/11 |
| --- | --- |
| Java | JDK **21.0.11** disponible. `java -version` y `javac -version` coinciden con Java 21 de los dos POM. |
| Maven | Funcionó `.\mvnw.cmd -v` en ambos proyectos. El Wrapper de DESI usa **3.9.16** y el docente **3.10.0**. El comando global `mvn` no está disponible y `MAVEN_HOME` apunta a una ruta sin ejecutable. Usar el Wrapper desde la carpeta del proyecto. [Apache Maven Wrapper](https://maven.apache.org/tools/wrapper/) |
| Eclipse STS | Ejecutable de **Spring Tools for Eclipse 5.1.1** localizado. Queda por comprobar la importación del futuro proyecto `daos/` con **Existing Maven Projects** y JDK 21. [Spring Tools](https://spring.io/tools/) |
| Spring Initializr | El sitio y su servicio de metadatos están disponibles. Se usa para generar la base cuando haga falta. Para trabajar con un proyecto Maven existente se importa su `pom.xml`. [Spring Initializr](https://start.spring.io/) |
| Spring Boot | La base DAOS usa **4.1.2**, Maven Wrapper **3.10.0**, Spring Web MVC, Validation, HATEOAS y springdoc **3.2.0**. Spring Data JPA y MySQL Connector/J se agregan con la persistencia en la jornada 2. [Spring Boot](https://docs.spring.io/spring-boot/reference/) |
| MySQL | Servidor **8.0.46** activo en el puerto **3306**. Falta validar la conexión de la aplicación al esquema DAOS. [Repaso de persistencia y MySQL](https://tuti.sied.utn.edu.ar/course/section.php?id=1092) |
| Git y GitHub | Git **2.54.0** disponible. Se verificó la descarga del repositorio HogarYA y del ejemplo docente. [HogarYA](https://github.com/laggerpablojose/hogarya) |

El apunte de herramientas de DESI muestra JDK 11 en la instalación de ejemplo. Para este práctico la versión de referencia es Java 21, declarada por los POM y verificada al compilar. [Herramientas de la cátedra](https://tuti.sied.utn.edu.ar/course/section.php?id=1090) · [POM docente](https://github.com/dardokuttel2/daos2026/blob/bdb0edd7d5a636ee799806d5f1d366588d1fc7b9/pom.xml)

## Arquitectura y contrato HTTP

`Cliente → presentación REST → servicios → repositorios Spring Data JPA → MySQL`

1. **Presentación**. `@RestController` define rutas, lee y valida DTO de entrada, delega al servicio y devuelve DTO de respuesta, enlaces y códigos HTTP. Spring MVC serializa el resultado como representación JSON en el cuerpo HTTP. Centralizar la traducción de errores con `@RestControllerAdvice`. [Capa de presentación](https://tuti.sied.utn.edu.ar/course/section.php?id=1093) · [Spring MVC y @ResponseBody](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods/responsebody.html) · [Tratamiento de errores](https://tuti.sied.utn.edu.ar/course/section.php?id=1094)
2. **Servicios**. Validan reglas, fechas y transiciones, coordinan dependencias y definen los límites de cada transacción. Pactar interfaces internas entre Facturas y Pagos, y entre Incidentes e Informes, dentro del mismo proceso Java. [Repaso de arquitectura en capas](https://tuti.sied.utn.edu.ar/course/section.php?id=1091)
3. **Acceso a datos**. Los repositorios Spring Data JPA consultan y guardan entidades mapeadas por JPA, con Hibernate como implementación y MySQL como motor. Las restricciones de base de datos complementan las reglas de servicio. [Spring Data JPA](https://docs.spring.io/spring-data/jpa/reference/)

En REST cada recurso tiene una URI, se consulta o modifica mediante representaciones, y sus mensajes explicitan el significado con métodos, encabezados y estados HTTP. Los enlaces HATEOAS permiten seguir relaciones desde la respuesta. JSON es el formato elegido para la representación de esta API. Un cliente que respeta el contrato HTTP puede seguir usándolo si cambia la implementación interna. [Fielding, capítulo 5](https://ics.uci.edu/~fielding/pubs/dissertation/rest_arch_style.htm) · [Spring HATEOAS](https://docs.spring.io/spring-hateoas/docs/current/reference/html/)

Pactar un formato común de DTO, errores, filtros y enlaces antes de abrir ramas. Usar las rutas del enunciado. Para las creaciones, devolver `201`, encabezado `Location` y representación con `self`. Usar `200` en consultas y cambios de estado, `400` para datos inválidos, `404` para recursos ausentes y `409` para conflictos de negocio. Incluir `self` en las representaciones exitosas, incluso en las colecciones, y comprobar que cada enlace se pueda seguir. Documentar rutas, cuerpos, filtros, errores y relaciones con OpenAPI y Swagger UI. [Enunciado de Actividad 02](https://tuti.sied.utn.edu.ar/mod/resource/view.php?id=7160) · [Unidad 6](https://tuti.sied.utn.edu.ar/course/section.php?id=1095) · [springdoc](https://springdoc.org/)

## Reparto propuesto y criterios de aceptación

Los nombres corresponden al grupo de DAOS de la Actividad 01. Pablo confirmó S03 el 22/11. El reparto de los demás módulos conserva la propuesta inicial y requiere acuerdo del grupo. Cada persona se hace cargo de un módulo completo, desde la ruta HTTP hasta la persistencia y sus casos de prueba.

| Módulo y responsable propuesto | Operaciones obligatorias | Trabajo compartido |
| --- | --- | --- |
| **S01 Facturas · Matías Bocchieri** | `POST /facturas`, `GET /facturas/{id}`, `GET /facturas` con filtros y `PATCH /facturas/{id}/estado` para anular. | Consulta auxiliar `GET /contratos/{id}` y reglas de vigencia compartidas con S03. |
| **S02 Pagos · Aron Díaz** | `POST /pagos`, `GET /pagos/{id}` y `GET /pagos` con filtro por factura. | Datos de prueba de facturación e integración S01 + S02. |
| **S03 Incidentes · Pablo José Lagger** | `POST /incidentes`, `GET /incidentes/{id}`, `GET /incidentes` con filtros, `PATCH /incidentes/{id}/estado` y `GET /incidentes/{id}/historial`. | Base del proyecto, convenciones REST y formato común de errores. |
| **S04 Informes de resolución · Neron Díaz** | `POST /informes-resolucion`, `GET /informes-resolucion/{id}` y `GET /informes-resolucion` con filtros. | Datos de mantenimiento, integración S03 + S04 y preparación del ZIP junto al equipo. |

- **S01**. Agregar período `AAAA-MM` y distinguir mensual de adicional según si se calculó el importe. Validar contrato activo, sin baja lógica y con al menos un día de vigencia en el mes facturado. Si corresponde, calcular fin de contrato como inicio más duración menos un día y ajustar el vencimiento al último día del mes. Copiar el importe mensual al emitir y calcular el estado vencida en consultas, filtros y operaciones. Permitir una sola mensual no anulada por contrato y período, y reemitir después de anular conservando la anterior. La anulación aplica a pendientes y vencidas. En detalle, enlaces `self`, `contrato` y `pago` si existe.
- **S02**. DESI almacena los datos del pago dentro de `Factura`. DAOS necesita un pago identificable y consultable. Usar restricción única por factura y guardar pago y estado pagada en una transacción. Aceptar pago total de facturas pendientes o vencidas, con total igual a importe de factura más interés, fecha entre emisión y hoy y los medios del enunciado. En detalle, enlaces `self` y `factura`.
- **S03**. El modelo DESI registra fechas sin hora y un solo estado por fila de historial. Agregar fecha y hora de alta y de cada transición, estado anterior y nuevo. Validar vigencia del contrato en el alta y registrar el primer estado abierto junto con el incidente. Permitir abierto → en proceso o cancelado, en proceso → cancelado, resuelto → reabierto y reabierto → en proceso. Guardar cambio e historial en la misma transacción. El seguimiento puede continuar tras finalizar el contrato. En detalle, enlaces `self`, `contrato` e `informes`.
- **S04**. DESI tiene una relación uno a uno entre incidente e informe. DAOS requiere conservar un informe por cada resolución. Resolver solo desde en proceso, guardar informe, estado resuelto e historial en una transacción, con la misma fecha y hora. Impedir dos informes de la misma transición y conservar los anteriores tras una reapertura. En detalle, enlaces `self` e `incidente`.

Revisar en conjunto los cambios simultáneos sobre una misma factura o incidente. Acordar cómo serializar emisión mensual, pago frente a anulación y resolución frente a cancelación o a otra resolución. Los enlaces de contrato necesitan una consulta auxiliar real, y el de informes debe llevar a la colección filtrada por el incidente. El alcance de desarrollo cubre los cuatro módulos y las consultas auxiliares que requieren sus flujos. Propiedades, publicaciones y la administración completa de contratos quedan como contexto de DESI.

## Plan diario del 22 al 29 de noviembre

El [plan vigente](docs/PLAN.md) vincula cada jornada con un concepto de DAOS, código verificable y una PR. Comienza por la base REST y continúa con persistencia, alta, consultas, transiciones, integración con S04 y entrega.

## Pruebas y entrega

- **Seis casos reproducibles por persona como mínimo**. Éxito de la operación principal, consulta por ID, listado filtrado, datos inválidos, recurso inexistente y conflicto de negocio. Para cada caso incluir solicitud, datos previos, código HTTP y resultado esperado. Usar colección Postman exportada o comandos cURL en texto.
- **Recorrido de facturación y pago**. Emitir factura, pagar, consultar la factura pagada, seguir su enlace al pago y comprobar que el segundo pago se rechaza sin duplicados.
- **Recorrido de mantenimiento**. Registrar, iniciar atención, resolver, reabrir, retomar y resolver de nuevo. Verificar ambos informes y el historial completo. Añadir comprobaciones de que una operación fallida deja consistentes factura, pago, incidente, historial e informes según corresponda.
- **Paquete**. URL de GitHub y ZIP con proyecto completo importable en Eclipse STS, `pom.xml`, Wrapper, scripts de inicialización, casos reproducibles y archivo con nombres completos y correos de quienes participaron en esta etapa. Preparar el ZIP sin `target/`. Usar datos ficticios que permitan repetir los recorridos. [Enunciado general](https://tuti.sied.utn.edu.ar/mod/resource/view.php?id=7157) · [Formato y pruebas de la Actividad 02](https://tuti.sied.utn.edu.ar/mod/resource/view.php?id=7160)

## Contexto para pasarle a un agente

> Trabajá en el módulo y rama que te asignaron. Leé el enunciado DAOS Actividad 02, esta guía y el contrato común antes de editar. Usá Java 21, Maven, Spring Boot, Spring Data JPA y MySQL. Implementá en `daos/` las rutas, DTO, reglas, estados HTTP y enlaces acordados. Tomá DESI como referencia para el dominio y el proyecto docente como referencia técnica. Ubicá las reglas y transacciones en servicios, y usá repositorios para la persistencia. Coordiná cambios que afecten otro módulo antes de incorporarlos. Entregá el PR con los seis casos reproducibles, datos necesarios, resultado de las pruebas y los puntos que queden abiertos. Si dos fuentes difieren, seguí la Actividad 02 y dejá la decisión registrada.
