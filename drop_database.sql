DROP DATABASE db;

SET FOREIGN_KEY_CHECKS = 0;

SELECT CONCAT('DROP TABLE IF EXISTS `', table_name, '`;')
FROM information_schema.tables
WHERE table_schema = 'db'; -- reemplaza esto con el nombre de tu base de datos

SET FOREIGN_KEY_CHECKS = 1;

CREATE DATABASE db;