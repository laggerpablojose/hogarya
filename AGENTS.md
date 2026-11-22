# Reglas de trabajo del integrador DAOS

Estas instrucciones se aplican al trabajo DAOS en `daos/`. Pablo José Lagger desarrolla S03 Incidentes, confirmado el 22/11/2026. La entrega es el 29/11/2026.

## Objetivo de cada sesión

- Escribir código ejecutable del incremento del día y explicarlo para que Pablo comprenda las responsabilidades, contratos, persistencia, transacciones e integración de servicios.
- Leer `daos/docs/PLAN.md` y `daos/docs/DECISIONES.md` antes de cambiar código. Revisar el estado de Git, la rama y las PR abiertas.
- Trabajar un incremento por jornada del 22 al 29/11. Si Pablo pide continuar, avanzar con el siguiente incremento. Pedir su intervención solo cuando falte una decisión que impida continuar.
- Explicar qué resuelve el incremento, por qué pertenece a esa capa, cómo comprobarlo y qué puede fallar. Usar nombres de archivos y pequeños ejemplos cuando aclaren la explicación.
- Dar pasos concretos de las herramientas cuando se usen por primera vez o cambie el procedimiento. Priorizar comprensión de la materia sobre ejercicios de sintaxis.

## Alcance técnico

- La fuente de requisitos es el enunciado DAOS de la Actividad 02. El análisis de la Actividad 01 aporta contexto y el ejemplo del profesor guía el uso de Spring.
- Usar Java 21, Maven Wrapper, Eclipse STS, Spring Initializr cuando haga falta generar una base, Spring Boot, Git, GitHub, MySQL y Spring Data JPA con Hibernate.
- Mantener una aplicación integrada con las capas de presentación REST, servicios y acceso a datos. Los controladores manejan HTTP y DTO, los servicios reglas y transacciones y los repositorios consultas y persistencia.
- El proyecto DAOS está en `daos/`, con paquete base `tuti.daos.hogarya` y nombre Maven `02-BocchieriDiazLaggerDiaz`. Agregar las dependencias en el día en que se usan.
- S03 registra y consulta incidentes, permite las transiciones del enunciado y conserva el historial. S04 implementa la resolución completa, que requiere coordinación con S03.
- Coordinar cambios de entidades compartidas y contratos con los responsables de los otros módulos. Usar datos ficticios y configuración local para credenciales.
- Crear helpers, capas y dependencias cuando el incremento las necesite. Conservar el contrato HTTP, enlaces HATEOAS y estados 200, 201, 400, 404 y 409 acordados.

## Git y comprobación

- Rama de integración `daos/integrador-2026`. Las ramas diarias se llaman `daos/dia-NN-descripcion` y sus PR apuntan a la integración. Crear cada rama a partir de la integración actualizada.
- Antes de publicar, ejecutar desde `daos/` `.\mvnw.cmd clean verify` y las comprobaciones funcionales del incremento. Si una comprobación queda pendiente, registrarla.
- Registrar en cada PR el cambio concreto, concepto a comprender, pasos reproducibles y resultado real. Vincular la jornada del plan y las decisiones que la afectan.
- Publicar la rama y abrir su PR cuando forme parte del avance pedido. La revisión con Pablo precede a la integración. Mantener una PR diaria pequeña y coherente.
- Mantener al menos seis casos reproducibles de S03 y colaborar en el recorrido integrado con S04, incluyendo rechazos sin registros parciales.

## Continuidad y ubicación

- Al terminar cada jornada, actualizar `daos/docs/PLAN.md` y `daos/docs/DECISIONES.md` con estado, decisiones, pruebas, PR y siguiente paso.
- La copia de trabajo elegida está en `C:\Users\lagge\Desktop\(DAOS) Desarrollo de Aplicaciones Orientadas a Servicios\Trabajo Práctico Integrador\Actividad 02\hogarya`.
- Usar sparse checkout para mostrar `daos/`, este archivo y los archivos de Git necesarios. Consultar DESI desde el historial de Git cuando haga falta.
- Los recordatorios convocan a Pablo para la sesión diaria. El código y la PR se preparan cuando retoma el trabajo en la conversación.
