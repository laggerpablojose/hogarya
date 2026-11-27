# HogarYA API del integrador DAOS

Aplicación integrada de facturas, pagos, incidentes e informes de resolución. La implementación parte de la versión S04 recibida en DAOS.zip. Las comprobaciones realizadas se describen en docs/VALIDACION.md.

El proyecto Maven se llama 02-BocchieriDiazLaggerDiaz. Usa Java 21, Spring Boot 3.3.5, Spring Data JPA con Hibernate, MySQL 8, HATEOAS y springdoc 2.6.0. La versión de Boot se conserva de la implementación compartida. Maven Wrapper fija Maven 3.10.0.

## Preparar MySQL

En MySQL Workbench, abrir una conexión administradora y ejecutar scripts/crear-esquemas.sql. El ejemplo de permisos usa la cuenta local hogarya, que debe existir previamente. Si se usa otra cuenta, ajustar ese nombre en las sentencias GRANT.

Hibernate crea las tablas. src/main/resources/data.sql carga cuatro contratos ficticios con fechas de 2026 y 2027. El contrato 1 está activo y vigente para la entrega. El 2 permite comprobar vencimientos al último día del mes, el 3 está inactivo y el 4 tiene baja lógica.

La aplicación usa el esquema 02-BocchieriDiazLaggerDiaz. Las pruebas utilizan 02-BocchieriDiazLaggerDiaz_pruebas y vacían sus registros de facturas, pagos, incidentes, historial e informes antes de cada caso. En Windows, MySQL puede mostrar los nombres en minúsculas.

## Importar en Eclipse STS

1. Abrir Spring Tools for Eclipse y elegir el workspace habitual.
2. Usar File → Import → Maven → Existing Maven Projects.
3. Seleccionar la carpeta daos del repositorio y su pom.xml.
4. Comprobar JavaSE-21 en Project → Properties → Java Build Path. Si hace falta, registrar el JDK 21 en Window → Preferences → Java → Installed JREs.
5. Ejecutar Maven → Update Project para descargar las dependencias.
6. En Run → Run Configurations → Spring Boot App, seleccionar com.hogarya.HogarYaApplication.
7. En la pestaña Environment, agregar DB_USER y DB_PASSWORD con la cuenta local. Se pueden definir desde un gestor de contraseñas o la configuración de ejecución del equipo.
8. Iniciar con Run y abrir http://localhost:8081/swagger-ui/index.html.

El código original utiliza Lombok. Si STS marca como inexistentes getters o constructores y Maven compila correctamente, comprobar la instalación de Lombok para ese STS siguiendo https://projectlombok.org/setup/eclipse. La compilación por Maven procesa las anotaciones con las dependencias del POM.

## Ejecutar desde PowerShell

Desde daos, definir las variables de la conexión local. El siguiente ejemplo solicita la contraseña sin mostrarla.

```powershell
$env:DB_USER = 'hogarya'
$credential = Get-Credential -UserName $env:DB_USER -Message 'Conexión MySQL de HogarYA'
$env:DB_PASSWORD = $credential.GetNetworkCredential().Password
.\mvnw.cmd spring-boot:run
```

La consola informa el arranque en el puerto 8081. Ctrl+C detiene el proceso. DB_URL permite elegir otra conexión JDBC. El Wrapper puede descargar Maven y las bibliotecas durante la primera ejecución.

## Consultar el contrato y los recursos

La raíz http://localhost:8081/ devuelve el nombre y enlaces a documentación. OpenAPI se publica en http://localhost:8081/v3/api-docs y Swagger UI permite ejecutar las solicitudes con Try it out.

| Módulo | Operaciones |
| --- | --- |
| S01 Facturas | POST y GET /facturas, GET /facturas/{id}, PATCH /facturas/{id}/estado para anular |
| S02 Pagos | POST y GET /pagos, GET /pagos/{id} |
| S03 Incidentes | POST y GET /incidentes, GET /incidentes/{id}, PATCH /incidentes/{id}/estado, GET /incidentes/{id}/historial |
| S04 Informes | POST y GET /informes-resolucion, GET /informes-resolucion/{id} |
| Consulta auxiliar | GET /contratos/{id} |

Las creaciones responden 201 con Location. Las consultas y cambios exitosos responden 200. Los errores usan ProblemDetail con 400 para datos inválidos, 404 para recursos ausentes y 409 para conflictos de negocio o concurrencia.

## Ejecutar las pruebas

Con DB_USER y DB_PASSWORD definidos, ejecutar desde daos.

```powershell
.\mvnw.cmd clean verify
```

Las pruebas usan MySQL real y el esquema de pruebas fijo. El reloj de las pruebas se fija al 25/11/2026 para que las verificaciones de vigencia y vencimiento se puedan repetir. El reloj de la aplicación utiliza la fecha actual de Argentina.

Para repetir las solicitudes externas, consultar pruebas/INSTRUCCIONES.md. Allí se explica cómo usar la colección Postman y los casos cURL.

Postman actúa como cliente HTTP para ejecutar solicitudes contra la API. La aplicación conserva sus datos en MySQL. La colección guarda solicitudes y verificaciones de sus respuestas.
