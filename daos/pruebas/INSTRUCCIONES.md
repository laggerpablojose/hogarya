# Casos reproducibles de HogarYA

HogarYA.postman_collection.json contiene seis casos por módulo y los dos recorridos integrados. casos-curl.txt ofrece las mismas solicitudes en texto, con datos previos y resultados esperados.

## Preparación

1. Crear los esquemas con scripts/crear-esquemas.sql.
2. Definir DB_USER y DB_PASSWORD como se explica en el README.
3. Ejecutar una vez .\mvnw.cmd clean verify para crear las tablas del esquema de pruebas y comprobar la aplicación.
4. En Workbench, ejecutar scripts/reiniciar-pruebas.sql para descartar los registros de prueba que dejaron los tests. Los cuatro contratos de ejemplo permanecen.
5. Configurar la aplicación para usar el esquema de pruebas y arrancarla.

```powershell
$env:DB_URL = 'jdbc:mysql://localhost:3306/02-BocchieriDiazLaggerDiaz_pruebas?serverTimezone=America/Argentina/Buenos_Aires'
.\mvnw.cmd spring-boot:run
```

La aplicación utiliza el puerto 8081. Las fechas de los datos de ejemplo están pensadas para la entrega de noviembre de 2026. Para probar fuera de la vigencia del contrato 1, actualizar sus datos de prueba.

## Postman

Importar HogarYA.postman_collection.json y abrir su Collection Runner. Mantener baseUrl en http://localhost:8081 y ejecutar todas las carpetas en el orden guardado. Los POST exitosos capturan sus identificadores en variables de colección para las solicitudes siguientes. Cada solicitud comprueba el código HTTP.

| Módulo | Casos individuales |
| --- | --- |
| S01 | Emisión, consulta, filtro, período inválido, contrato inexistente y mensual duplicada |
| S02 | Pago, consulta, filtro, importe inválido, factura inexistente y segundo pago |
| S03 | Alta, consulta, filtro, título vacío, incidente inexistente y transición prohibida |
| S04 | Resolución, consulta, filtro, datos inválidos, informe inexistente y resolución duplicada |

S04 tiene una solicitud adicional de preparación para iniciar la atención. Los recorridos integrados reutilizan los datos previos de la colección. El recorrido de pago emite otra factura, la paga, consulta los recursos y rechaza un segundo pago. El de mantenimiento reabre el incidente ya resuelto, retoma atención y agrega un segundo informe. Al terminar deben existir dos informes y seis entradas de historial para ese incidente.

## cURL

Ejecutar las solicitudes de casos-curl.txt en PowerShell 7, en el orden indicado. Reemplazar los identificadores entre dobles llaves por los IDs devueltos en los POST. La colección Postman realiza esa captura automáticamente.

## Comprobaciones de consistencia

Además de los códigos HTTP, consultar los recursos después de cada rechazo. Una mensual duplicada deja una sola factura para contrato y período. El segundo pago deja exactamente un pago y la factura pagada. La transición prohibida conserva estado e historial. Una resolución duplicada conserva los informes y las entradas de historial existentes.

Las pruebas Java automatizan también la comprobación directa en MySQL. Inducen un fallo después de insertar un pago o un informe y verifican que la transacción revierta sus escrituras. Otros casos sincronizan dos solicitudes para comprobar emisión, pago y resolución concurrentes.
