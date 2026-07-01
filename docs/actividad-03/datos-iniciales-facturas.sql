-- Datos iniciales mínimos para probar el Epic Facturas.
-- Proyecto: HogarYA - Actividad 03
-- Base de datos: LezcanoBocchieriRossiLagger
--
-- Este script carga:
-- 1 persona propietaria
-- 1 persona inquilina
-- 1 propiedad alquilada
-- 1 contrato activo
--
-- No inserta facturas intencionalmente.
-- Las facturas se crean desde la interfaz web para probar el flujo del sistema.

USE LezcanoBocchieriRossiLagger;

INSERT INTO personas (
    apellido,
    dni,
    eliminado,
    email,
    nombre,
    telefono
) VALUES (
    'Gomez',
    '30111222',
    0,
    'propietario@hogarya.com',
    'Carlos',
    '3425000001'
);

SET @propietario_id = LAST_INSERT_ID();

INSERT INTO personas (
    apellido,
    dni,
    eliminado,
    email,
    nombre,
    telefono
) VALUES (
    'Perez',
    '40222333',
    0,
    'inquilino@hogarya.com',
    'Lucia',
    '3425000002'
);

SET @inquilino_id = LAST_INSERT_ID();

INSERT INTO propiedades (
    ambientes,
    ciudad,
    descripcion,
    direccion,
    eliminado,
    estado,
    metros_cuadrados,
    tipo,
    persona_propietario_id
) VALUES (
    3,
    'Santa Fe',
    'Propiedad de prueba para contrato activo',
    'San Martin 1234',
    0,
    'ALQUILADA',
    75.00,
    'DEPARTAMENTO',
    @propietario_id
);

SET @propiedad_id = LAST_INSERT_ID();

INSERT INTO contratos (
    descripcion,
    dia_vencimiento,
    duracion_meses,
    eliminado,
    estado,
    fecha_inicio,
    importe_mensual,
    persona_inquilino_id,
    propiedad_id
) VALUES (
    'Contrato activo de prueba para facturación',
    10,
    12,
    0,
    'ACTIVO',
    '2026-07-01',
    100000.00,
    @inquilino_id,
    @propiedad_id
);

SELECT * FROM personas;
SELECT * FROM propiedades;
SELECT * FROM contratos;