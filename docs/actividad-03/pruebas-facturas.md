# Pruebas manuales - Epic Facturas

## Contexto

Este documento registra las pruebas manuales realizadas sobre el Epic Facturas de la Actividad 03 del TP Integrador HogarYA.

Rama de trabajo:

text
feat/epic-facturas

## Arquitectura utilizada:

	Navegador -> Controller -> Service -> Repository -> Entity -> Base de datos

Estructura aplicada:

	tuti.desi.hogarya.accesoDatos
	tuti.desi.hogarya.entidades
	tuti.desi.hogarya.excepciones
	tuti.desi.hogarya.presentacion
	tuti.desi.hogarya.presentacion.formularios
	tuti.desi.hogarya.servicios

La implementación se realizó con Spring MVC, Thymeleaf, Spring Data JPA, Hibernate y MySQL.

## Validación técnica

Comando ejecutado:

	./mvnw clean compile

Resultado:

	BUILD SUCCESS

Ejecución local:

	./mvnw spring-boot:run

URL base utilizada:

	http://localhost:8080

## Datos de prueba utilizados

Para poder probar Facturas se cargaron datos mínimos en la base:

- Una persona propietaria.
- Una persona inquilina.
- Una propiedad asociada a la persona propietaria.
- Un contrato activo asociado a la propiedad y a la persona inquilina.

Esto fue necesario porque una factura debe estar asociada a un contrato activo y no eliminado.

El script utilizado se documenta en:

	docs/actividad-03/datos-iniciales-facturas.sql

## Pruebas realizadas

|Nro.|	Prueba | Resultado esperado | Resultado obtenido |
|1   |	Abrir /facturas |	Se muestra el listado de facturas sin error | OK |
|2   |	Abrir /facturas/nueva | Se muestra el formulario de alta | OK |
|3 	 |	Guardar formulario vacío | Se muestran errores de validación y no se crea la factura | OK | 
|4 	 |	Alta correcta de factura | Se crea la factura y se redirige al listado | OK |
|5 	 |	Fecha de vencimiento anterior a fecha de emisión | Se bloquea la operación y se muestra error de negocio | OK | 
|6 	 |	Registrar pago correcto | La factura pasa a estado pagada y registra datos de pago | OK | 
|7 	 |	Modificar factura pagada | Se bloquea la modificación | OK | 
|8 	 |	Anular factura pagada | Se bloquea la anulación | OK |
|9 	 |	Eliminar factura pagada | Se bloquea la eliminación | OK |

## Detalle de prueba de alta correcta

Datos utilizados:

	Contrato: contrato activo disponible
	Concepto: Alquiler julio 2026
	Fecha emisión: 2026-07-01
	Fecha vencimiento: 2026-07-10
	Importe: 100000
	Estado: PENDIENTE

Resultado:

	La factura fue creada correctamente.

La factura quedó visible en el listado con estado pendiente.

## Detalle de prueba de vencimiento inválido

Datos utilizados:

	Fecha emisión: 2026-07-10
	Fecha vencimiento: 2026-07-01

Resultado:

	La operación fue rechazada por regla de negocio.

La factura no fue creada.

## Detalle de prueba de pago correcto

Datos utilizados:

	Fecha pago: 2026-07-10
	Medio de pago: medio disponible en el formulario
	Importe pagado: 100000
	Interés pagado: 0

Resultado:

	La factura fue marcada como pagada correctamente.

La factura quedó con estado pagada y con los datos de pago visibles en el listado.

## Reglas de negocio verificadas

Se verificaron las siguientes reglas del Epic Facturas:

- La factura debe estar asociada a un contrato activo.
- La fecha de vencimiento debe ser igual o posterior a la fecha de emisión.
- El importe debe ser positivo.
- La factura se crea inicialmente en estado pendiente.
- Una factura pagada no puede modificarse.
- Una factura pagada no puede anularse.
- Una factura pagada no puede eliminarse.
- Para registrar el pago deben informarse los datos requeridos de pago.

## Resultado general

El Epic Facturas quedó funcionalmente validado para el flujo mínimo requerido:

	Listado -> Alta -> Validaciones -> Pago -> Bloqueos por estado

Resultado final:

	Pruebas manuales aprobadas.