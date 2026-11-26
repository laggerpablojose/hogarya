INSERT IGNORE INTO contratos (id, fecha_inicio, fecha_fin, duracion_meses, importe_mensual, dia_vencimiento, activo, eliminado) VALUES
(1, '2026-01-01', NULL,         24,   450000.00, 10, 1, 0),  -- activo, por duracion (fin 2027-12-31)
(2, '2025-03-15', '2026-03-14', NULL, 380000.00, 31, 1, 0),  -- activo, fin explicito, vence el 31 (prueba ultimo dia)
(3, '2026-01-01', NULL,         12,   300000.00,  5, 0, 0),  -- inactivo
(4, '2026-01-01', NULL,         12,   300000.00,  5, 1, 1);  -- eliminado logicamente
