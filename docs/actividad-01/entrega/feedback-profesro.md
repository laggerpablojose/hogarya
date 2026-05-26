# Observaciones del Docente – Actividad 01

## Observaciones del docente

### 1. Ambigüedad en criterios de aceptación

Se observó que algunos criterios de aceptación presentan expresiones ambiguas, por ejemplo:

> "Debe permitir cambios coherentes de estado"

Se recomienda definir explícitamente qué transiciones o condiciones son válidas para evitar interpretaciones múltiples.

---

### 2. HU14 debería integrarse en HU13

La historia:

- HU14 – Impedir pagos sobre facturas anuladas o ya pagadas

No representa una funcionalidad independiente para el usuario, sino una validación propia del proceso de registro de pagos.

Se recomienda incorporarla como criterio de aceptación de la HU13.

---

### 3. HU16 debería integrarse en HU15

La historia:

- HU16 – Impedir la creación de incidentes sobre contratos no vigentes

Corresponde a una regla de negocio asociada a la creación de incidentes.

Se recomienda incorporarla como criterio de aceptación de la HU15.

---

### 4. HU18 debería integrarse en HU17

La historia:

- HU18 – Registrar la fecha de cada cambio de estado de un incidente

Representa una consecuencia de la actualización de estado y no una funcionalidad independiente.

Se recomienda incorporarla como criterio de aceptación de la HU17.

---

### 5. HU23 demasiado amplia

La historia:

- HU23 – Consultar el estado general de alquileres, pagos e incidentes

Resulta demasiado genérica.

Se recomienda expresar con mayor precisión qué información requiere visualizar el gerente.

---

### 6. HU24 demasiado amplia

La historia:

- HU24 – Centralizar la información de gestiones

Representa un objetivo general del sistema más que una historia de usuario específica.

Se recomienda dividirla en funcionalidades más concretas, simples y verificables.

---

# Acciones realizadas

## 1. Refinamiento de HU10

Se reemplazó el criterio ambiguo:

> "Debe permitir cambios coherentes de estado"

por criterios específicos que describen los estados permitidos y las condiciones necesarias para activar un contrato.

---

## 2. Eliminación de HU14

La HU14 fue eliminada como historia independiente.

Sus validaciones fueron incorporadas como criterios de aceptación de la HU13:

- No permitir pagos sobre facturas anuladas.
- No permitir registrar más de un pago sobre una factura ya pagada.

---

## 3. Eliminación de HU16

La HU16 fue eliminada como historia independiente.

Sus validaciones fueron incorporadas como criterios de aceptación de la HU15:

- Solo permitir incidentes asociados a contratos activos.
- Informar al usuario cuando el contrato no se encuentre vigente.

---

## 4. Eliminación de HU18

La HU18 fue eliminada como historia independiente.

Sus requisitos de trazabilidad fueron incorporados como criterios de aceptación de la HU17:

- Registrar la fecha de cada cambio de estado.
- Permitir consultar el historial de cambios.

---

## 5. Reformulación de HU23

La HU23 fue redefinida como una funcionalidad concreta orientada a un tablero de control gerencial.

La nueva versión especifica claramente la información requerida:

- Contratos activos.
- Facturas pendientes.
- Facturas vencidas.
- Incidentes agrupados por estado.

---

## 6. Reformulación de HU24

La HU24 fue redefinida para enfocarse en una funcionalidad verificable:

> Consulta del historial completo de una gestión.

La historia ahora permite continuar el trabajo realizado por otro empleado mediante la consulta de información relacionada con propiedades, contratos, pagos e incidentes.

---

## 7. Revisión de la conclusión

Se reemplazó la conclusión original por una versión orientada al análisis de requerimientos.

La nueva redacción enfatiza:

- Identificación de actores.
- Obtención de historias de usuario.
- Definición de criterios de aceptación.
- Base para las siguientes etapas del trabajo práctico.

Esto mejora la alineación con los objetivos de la Actividad 01.
