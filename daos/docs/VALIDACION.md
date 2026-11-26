# Validación de la solución integrada

La revisión del 25/11/2026 utiliza MySQL Community Server 8.0.46, JDK 21.0.11 y Maven 3.10.0. La base de partida es la versión S04 de DAOS.zip.

## Pruebas Java

El comando .\mvnw.cmd clean verify ejecuta 38 pruebas contra el esquema exclusivo 02-BocchieriDiazLaggerDiaz_pruebas. El resultado comprobado es 38 ejecutadas, cero fallos, cero errores y cero omitidas.

Incluyen los seis casos mínimos por módulo, los dos recorridos integrados, fallos inducidos con reversión de transacciones, tres competencias concurrentes y casos de vencimiento, reemisión, copia del importe, vigencia y continuidad del seguimiento.

Las pruebas de concurrencia sincronizan lecturas para que dos solicitudes operen sobre el mismo recurso. Se espera una creación 201 y un rechazo 409, con una sola factura mensual, un solo pago o un solo informe según el caso. La base se consulta después para comprobar que no quedaron escrituras parciales.

## Ejecución del JAR

Se inició el JAR con conexión al esquema de pruebas y se ejecutaron las solicitudes de HogarYA.postman_collection.json mediante un cliente HTTP, sustituyendo sus variables y verificando los códigos guardados en la colección. El registro se encuentra en pruebas/resultado-ejecucion.txt.

Se comprobaron la raíz, OpenAPI y Swagger UI. Los conteos finales del recorrido externo fueron dos facturas, dos pagos, un incidente, seis entradas de historial y dos informes.

La importación en la aplicación Postman y el recorrido visual de STS quedan para revisión del equipo. Las solicitudes de la colección se verificaron mediante HTTP y la compilación mediante Maven Wrapper.

La verificación final del 26/11/2026 se repitió desde la copia definitiva del Escritorio con el mismo resultado de 38 pruebas aprobadas.
