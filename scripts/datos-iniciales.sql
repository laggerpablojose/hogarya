-- Datos iniciales mínimos para probar la Actividad 03 del TP Integrador HogarYA.
-- Base de datos: LezcanoBocchieriRossiLagger
-- Ejecutar sobre una base limpia, luego de iniciar la aplicación para que Hibernate cree las tablas.
--
-- Este script carga:
-- - Personas de prueba.
-- - Una propiedad disponible para probar Publicaciones.
-- - Una publicación activa.
-- - Una propiedad alquilada para probar Facturas.
-- - Un contrato activo requerido por Facturas.
--
-- No inserta facturas intencionalmente: las facturas se crean desde la interfaz web.

USE LezcanoBocchieriRossiLagger;

-- Personas
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

-- Propiedad disponible para probar Publicaciones
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
    'Departamento disponible para publicación de prueba',
    'San Martin 1234',
    0,
    'DISPONIBLE',
    75.00,
    'DEPARTAMENTO',
    @propietario_id
);

SET @propiedad_publicacion_id = LAST_INSERT_ID();

-- Publicación activa de prueba
INSERT INTO publicaciones (
    condiciones,
    descripcion,
    eliminado,
    estado_publicacion,
    fecha_publicacion,
    precio_mensual_alquiler,
    propiedad_id
) VALUES (
    'Contrato mínimo de 12 meses. Actualización según acuerdo entre partes.',
    'Publicación de prueba para propiedad disponible.',
    0,
    'ACTIVA',
    '2026-07-01',
    120000.00,
    @propiedad_publicacion_id
);

-- Propiedad alquilada para probar Contrato activo y Facturas
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
    2,
    'Santa Fe',
    'Propiedad alquilada para contrato activo de prueba',
    'Bv. Pellegrini 2500',
    0,
    'ALQUILADA',
    55.00,
    'DEPARTAMENTO',
    @propietario_id
);

SET @propiedad_contrato_id = LAST_INSERT_ID();

-- Contrato activo requerido para probar Facturas
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
    @propiedad_contrato_id
);

-- Verificación rápida
SELECT * FROM personas;
SELECT * FROM propiedades;
SELECT * FROM publicaciones;
SELECT * FROM contratos;
