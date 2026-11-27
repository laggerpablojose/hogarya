-- Descarta exclusivamente los registros del esquema de pruebas.
-- Ejecutar después de que la aplicación o los tests hayan creado sus tablas.
USE `02-BocchieriDiazLaggerDiaz_pruebas`;
DELETE FROM informes_resolucion;
DELETE FROM historial_estados_incidente;
DELETE FROM incidentes;
DELETE FROM pagos;
DELETE FROM facturas;
