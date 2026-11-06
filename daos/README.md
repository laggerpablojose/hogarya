# HogarYA · Integrador DAOS 2026

Guía de trabajo para la Actividad 02. El equipo implementará **una API REST en Java con los cuatro módulos integrados y un esquema MySQL compartido**. El objetivo del grupo es tener lista la entrega el **viernes 20/11/2026**. Moodle publica el cierre el **domingo 29/11/2026 a las 23:59** y da prioridad de corrección y coloquio a las entregas anticipadas. [Enunciado de la Actividad 02](https://tuti.sied.utn.edu.ar/mod/resource/view.php?id=7160) · [Entrega en Moodle](https://tuti.sied.utn.edu.ar/mod/assign/view.php?id=7163)

## Fuentes y alcance

- **La Actividad 02 define lo que se implementa y evalúa.** Usar el enunciado general y el informe y BPMN de la Actividad 01 de DAOS para entender los procesos. Por ejemplo, el BPMN describe la reapertura y el reinicio del tratamiento como proceso. La API debe registrar dos transiciones explícitas, de resuelto a reabierto y de reabierto a en proceso. [Material del integrador](https://tuti.sied.utn.edu.ar/course/section.php?id=1097)
- **HogarYA de DESI aporta el dominio y la persistencia de partida.** Revisar las entidades, repositorios, validaciones y datos de ejemplo. Los controladores existentes sirven vistas Thymeleaf y algunos servicios reciben formularios de presentación. En DAOS usaremos DTO para el contrato HTTP y mantendremos las reglas en servicios. [HogarYA DESI, revisión 3c04ea6](https://github.com/laggerpablojose/hogarya/tree/3c04ea626b549aab79c84fd6fc2012eea6de4beb)
- **El proyecto del profesor aporta ejemplos técnicos.** Revisar `ContratoRestController` y `ContratoResponseDTO` para rutas, códigos HTTP y enlaces, y `ExceptionHandler` para el tratamiento centralizado. Los datos y recursos a exponer salen del enunciado de HogarYA. [Código de clase, revisión bdb0edd](https://github.com/dardokuttel2/daos2026/tree/bdb0edd7d5a636ee799806d5f1d366588d1fc7b9) · [Demostración del profesor](https://youtu.be/Er9UREp_TNY)

## Organización del repositorio

HogarYA ya está registrado como repositorio del grupo en Moodle. La rama local de integración es `daos/integrador-2026`. La carpeta `daos/` es la ubicación propuesta para un único proyecto Maven importable en STS, con su `pom.xml`, código, Wrapper, scripts y pruebas reproducibles. Hoy contiene la guía. La raíz del repositorio sigue siendo la referencia DESI. Cada integrante trabajará en una rama `daos/s01-…` a `daos/s04-…` a partir de la base común y abrirá un PR hacia la rama de integración. [Registro del repositorio](https://tuti.sied.utn.edu.ar/mod/assign/view.php?id=7505)

Para el esquema MySQL, el proyecto y el ZIP se propone la base de nombre `02-BocchieriDiazLaggerDiaz`, siguiendo el ZIP de la Actividad 01. Usar un esquema DAOS independiente del de DESI para poder ajustar el modelo y reproducir pruebas desde cero. Confirmar el nombre con el equipo antes de crear la configuración compartida. Pasar la URL y las credenciales por configuración local, con valores de ejemplo en las instrucciones.

## Herramientas

| Herramienta | Uso y comprobación del 06/11 |
| --- | --- |
| Java | JDK **21.0.11** disponible. `java -version` y `javac -version` coinciden con Java 21 de los dos POM. |
| Maven | Funcionó `.\mvnw.cmd -v` en ambos proyectos. El Wrapper de DESI usa **3.9.16** y el docente **3.10.0**. El comando global `mvn` no está disponible y `MAVEN_HOME` apunta a una ruta sin ejecutable. Usar el Wrapper desde la carpeta del proyecto. [Apache Maven Wrapper](https://maven.apache.org/tools/wrapper/) |
| Eclipse STS | Ejecutable de **Spring Tools for Eclipse 5.1.1** localizado. Queda por comprobar la importación del futuro proyecto `daos/` con **Existing Maven Projects** y JDK 21. [Spring Tools](https://spring.io/tools/) |
| Spring Initializr | El sitio y su servicio de metadatos están disponibles. Se usa para generar la base cuando haga falta. Para trabajar con un proyecto Maven existente se importa su `pom.xml`. [Spring Initializr](https://start.spring.io/) |
| Spring Boot | DESI **4.1.0** y base docente **4.1.2** empaquetaron con `.\mvnw.cmd -B -DskipTests package`. Para DAOS se propone **4.1.2** y el Wrapper del profesor, con Spring Web, Validation, Spring Data JPA, MySQL Connector/J, HATEOAS y springdoc. Queda pendiente el arranque integrado contra el esquema DAOS. [Spring Boot](https://docs.spring.io/spring-boot/reference/) |
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

Los nombres corresponden al grupo de DAOS de la Actividad 01. Cada persona se hace cargo de un módulo completo, desde la ruta HTTP hasta la persistencia y sus casos de prueba. El reparto se confirma entre los cuatro.

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

## Plan hasta el 20 de noviembre

| Fecha | Resultado verificable |
| --- | --- |
| **06–08/11** | Confirmar responsables y habilitar acceso al repositorio. Crear la base Maven de DAOS y comprobar importación en STS, arranque y conexión al esquema nuevo. Acordar DTO, filtros, errores, enlaces, modelo y responsabilidades cruzadas. |
| **09–13/11** | Implementar los cuatro módulos en paralelo. Entregar PR revisable con una ruta principal funcional, datos de prueba y borrador de los seis casos por persona. Probar temprano los contratos S01–S02 y S03–S04. |
| **14–17/11** | Integrar operaciones y completar filtros, validaciones, restricciones y enlaces. Revisar OpenAPI después del encuentro de documentación del 16/11. Ejecutar los 24 casos individuales. |
| **18–19/11** | Probar desde un clon y esquema limpios. Ejecutar los dos recorridos integrados y comprobar ausencia de registros parciales ante rechazos. Corregir problemas de instalación, datos y contrato, y verificar el ZIP con STS. |
| **20/11** | Cierre del grupo. Identificar la revisión de entrega en GitHub y entregar URL y ZIP en Moodle. |

## Pruebas y entrega

- **Seis casos reproducibles por persona como mínimo**. Éxito de la operación principal, consulta por ID, listado filtrado, datos inválidos, recurso inexistente y conflicto de negocio. Para cada caso incluir solicitud, datos previos, código HTTP y resultado esperado. Usar colección Postman exportada o comandos cURL en texto.
- **Recorrido de facturación y pago**. Emitir factura, pagar, consultar la factura pagada, seguir su enlace al pago y comprobar que el segundo pago se rechaza sin duplicados.
- **Recorrido de mantenimiento**. Registrar, iniciar atención, resolver, reabrir, retomar y resolver de nuevo. Verificar ambos informes y el historial completo. Añadir comprobaciones de que una operación fallida deja consistentes factura, pago, incidente, historial e informes según corresponda.
- **Paquete**. URL de GitHub y ZIP con proyecto completo importable en Eclipse STS, `pom.xml`, Wrapper, scripts de inicialización, casos reproducibles y archivo con nombres completos y correos de quienes participaron en esta etapa. Preparar el ZIP sin `target/`. Usar datos ficticios que permitan repetir los recorridos. [Enunciado general](https://tuti.sied.utn.edu.ar/mod/resource/view.php?id=7157) · [Formato y pruebas de la Actividad 02](https://tuti.sied.utn.edu.ar/mod/resource/view.php?id=7160)

## Contexto para pasarle a un agente

> Trabajá en el módulo y rama que te asignaron. Leé el enunciado DAOS Actividad 02, esta guía y el contrato común antes de editar. Usá Java 21, Maven, Spring Boot, Spring Data JPA y MySQL. Implementá en `daos/` las rutas, DTO, reglas, estados HTTP y enlaces acordados. Tomá DESI como referencia para el dominio y el proyecto docente como referencia técnica. Ubicá las reglas y transacciones en servicios, y usá repositorios para la persistencia. Coordiná cambios que afecten otro módulo antes de incorporarlos. Entregá el PR con los seis casos reproducibles, datos necesarios, resultado de las pruebas y los puntos que queden abiertos. Si dos fuentes difieren, seguí la Actividad 02 y dejá la decisión registrada.
