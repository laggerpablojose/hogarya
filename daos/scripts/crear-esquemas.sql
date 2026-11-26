-- Ejecutar con una conexión administradora en MySQL Workbench.
CREATE DATABASE IF NOT EXISTS `02-BocchieriDiazLaggerDiaz`
    CHARACTER SET utf8mb4;
CREATE DATABASE IF NOT EXISTS `02-BocchieriDiazLaggerDiaz_pruebas`
    CHARACTER SET utf8mb4;

-- Ejemplo para la cuenta local ya utilizada por el equipo.
-- La cuenta debe existir antes de ejecutar estos permisos.
GRANT ALL PRIVILEGES ON `02-BocchieriDiazLaggerDiaz`.* TO 'hogarya'@'localhost';
GRANT ALL PRIVILEGES ON `02-BocchieriDiazLaggerDiaz_pruebas`.* TO 'hogarya'@'localhost';
