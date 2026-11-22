# Día 1 de la base REST de HogarYA

Hoy vamos a seguir una solicitud desde HTTP hasta su respuesta JSON. La aplicación arranca con Spring Boot y publica su documentación. Al revisarla, interesa comprender quién recibe la solicitud, qué datos se exponen y cómo el cliente descubre otras direcciones.

## Abrir el proyecto en Eclipse STS

1. Abrir `C:\Workspace\eclipse\sts-5.1.1.RELEASE\SpringToolsForEclipse.exe`.
2. Elegir el workspace habitual de STS. El workspace conserva la configuración del IDE y referencia el proyecto por su ubicación.
3. Ir a **File → Import → Maven → Existing Maven Projects**.
4. En **Root Directory**, elegir `C:\Users\lagge\Desktop\(DAOS) Desarrollo de Aplicaciones Orientadas a Servicios\Trabajo Práctico Integrador\Actividad 02\hogarya\daos`.
5. Seleccionar el POM del proyecto `02-BocchieriDiazLaggerDiaz` y pulsar **Finish**.
6. En **Project → Properties → Java Build Path**, comprobar **JRE System Library JavaSE-21**. En **Window → Preferences → Java → Installed JREs** se puede seleccionar `C:\Program Files\Java\jdk-21.0.11`.
7. Si faltan dependencias al importar, usar clic derecho sobre el proyecto → **Maven → Update Project**.

Se reutiliza la base del profesor y se adapta el POM. Spring Initializr es el generador que puede crear esa estructura y seleccionar dependencias. En esta jornada la base se entrega en el repositorio lista para importar.

## Ejecutar y consultar

Abrir `HogarYaApplication.java` y usar clic derecho → **Run As → Spring Boot App**. La consola debería mostrar el arranque de Tomcat en el puerto 8081.

Abrir PowerShell en la carpeta `daos` y ejecutar una consulta.

```powershell
curl.exe -i http://localhost:8081/
```

La respuesta debería tener estado `200 OK`, tipo `application/hal+json` y el cuerpo siguiente.

```json
{
  "nombre": "HogarYA",
  "_links": {
    "self": { "href": "http://localhost:8081/" },
    "documentacion": { "href": "http://localhost:8081/v3/api-docs" },
    "swagger": { "href": "http://localhost:8081/swagger-ui/index.html" }
  }
}
```

Abrir el enlace `swagger` en el navegador. Expandir **GET /**, usar **Try it out → Execute** y revisar código, encabezados y cuerpo. El enlace `documentacion` expone el contrato OpenAPI que Swagger UI utiliza para mostrar las operaciones.

También se puede ejecutar la aplicación desde PowerShell.

```powershell
.\mvnw.cmd spring-boot:run
```

Detener esa ejecución con **Ctrl+C**, o con el botón rojo de la consola de STS si se inició desde el IDE.

## Comprobar el incremento

Desde la carpeta `daos`, ejecutar el Wrapper.

```powershell
.\mvnw.cmd -v
.\mvnw.cmd clean verify
```

El primer comando informa Maven y Java. El segundo recompila, ejecuta las pruebas y genera el JAR en `target`. El resultado esperado es `BUILD SUCCESS`. El Wrapper fija la versión de Maven del proyecto.

## Seguir las responsabilidades

| Archivo | Responsabilidad |
| --- | --- |
| `pom.xml` | Declara Java, Spring Boot y las bibliotecas para HTTP, validación, enlaces, documentación y pruebas. |
| `HogarYaApplication.java` | Inicia Spring y define el paquete desde el que detecta los componentes. Declara los datos generales de OpenAPI. |
| `InicioRestController.java` | Atiende `GET /` y construye la representación del punto de entrada con sus enlaces. |
| `InicioResponse.java` | Define el dato `nombre` y hereda el soporte de enlaces que se serializan en `_links`. |
| `application.properties` | Configura el nombre de la aplicación y el puerto. |
| `InicioRestControllerTests.java` | Comprueba el contrato de la raíz y que los destinos de documentación respondan. |

La solicitud llega a Spring MVC, se encuentra el método asociado a `GET /`, el controlador devuelve el DTO y el convertidor produce JSON para el cuerpo HTTP. El `self` identifica esta representación y los otros enlaces permiten descubrir la documentación.

En la siguiente jornada aparece el almacenamiento. Cuando implementemos el alta, el controlador delegará las validaciones de negocio al servicio y este utilizará repositorios para guardar el incidente y su historial. Ese recorrido permitirá analizar el límite de la transacción.

Para revisar la comprensión de hoy, seguir el enlace a OpenAPI desde la respuesta e identificar dónde están definidos el puerto, los datos JSON y la ruta HTTP. Explicar qué habría que conservar si un cliente externo empezara a consumir esta respuesta.

## Revisar el avance en GitHub

Desde la carpeta `hogarya`, comprobar la rama y el estado.

```powershell
git status
git branch --show-current
git diff daos/integrador-2026...HEAD -- daos AGENTS.md
```

La rama de hoy es `daos/dia-01-base`. En la PR hacia `daos/integrador-2026`, revisar **Files changed** y comparar cada archivo con su responsabilidad. Las comprobaciones anteriores permiten seguir los pasos de la descripción.

Cuando se revisa una PR, se decide incorporar ese incremento a la integración. La próxima rama diaria se crea a partir de esa integración actualizada. En el plan queda registrado qué se implementa cada día.
