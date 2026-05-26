# Decisiones de Modelado – HogarYA

## Objetivo

Documentar las principales decisiones tomadas durante la construcción del modelo UML de clases.

---

## D01 – Mantener foco en el dominio

Se modelaron únicamente entidades del negocio.

No se incluyeron:

- Pantallas
- Formularios
- Componentes de interfaz
- Base de datos
- Arquitectura técnica
- Métodos o comportamiento

Motivo:
La consigna solicita un modelo conceptual de clases UML.

---

## D02 – Incorporar InteresVisita

Se creó la entidad InteresVisita para representar la relación entre un Interesado y una Propiedad.

Motivo:
La relación contiene información propia (fecha, observaciones) y no puede representarse correctamente mediante una asociación simple.

---

## D03 – Incorporar CambioEstadoIncidente

Se agregó la entidad CambioEstadoIncidente.

Motivo:
El enunciado indica la necesidad de registrar el historial de cambios de estado de cada incidente y sus fechas.

---

## D04 – Estados como atributos

Los estados del sistema no fueron modelados como clases independientes.

Ejemplos:

- estadoContrato
- estadoFactura
- estadoIncidente
- estadoPublicacion

Motivo:
Son catálogos simples y no poseen comportamiento propio dentro del alcance actual.

---

## D05 – Categorías y tipos como atributos

No se modelaron como entidades:

- Tipo de Propiedad
- Categoría de Incidente
- Prioridad
- Medio de Pago
- Ciudad
- Barrio

Motivo:
Aportan valor como atributos descriptivos y no requieren relaciones adicionales.

---

## D06 – Uso de composición

Se utilizó composición en:

- Contrato → Factura
- Incidente → CambioEstadoIncidente
- Incidente → InformeResolucion

Motivo:
Las entidades hijas no tienen sentido de existencia independiente fuera de su entidad principal.

---

## D07 – Cardinalidades flexibles

Las relaciones Propietario–Propiedad y Propietario–Contrato se definieron como:

- Propietario (1)
- Propiedad / Contrato (0..\*)

Motivo:
Puede existir un propietario cargado en el sistema sin propiedades o contratos asociados.

---

## D08 – Exclusión de atributos redundantes

Se eliminó el atributo `duracionMeses` de Contrato.

Motivo:
La duración puede derivarse de las fechas de inicio y fin, evitando duplicación de información.
