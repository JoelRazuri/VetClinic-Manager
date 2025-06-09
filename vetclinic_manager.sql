-- Creación de la BDD --
CREATE DATABASE vetclinic_manager;
SHOW DATABASES;

USE vetclinic_manager;

-- CREACIÓN DE TABLAS --
-- Tabla 'client', hace referencia a la tabla de Clientes --
CREATE TABLE IF NOT EXISTS `client` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `phone` VARCHAR(32) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email`)
);

-- Tabla 'pet', hace referencia a la tabla de Mascotas -- 
CREATE TABLE IF NOT EXISTS `pet` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `species` VARCHAR(32) NOT NULL,
  `race` VARCHAR(45) NOT NULL,
  `gender` VARCHAR(45) NOT NULL,
  `age` INT NOT NULL,
  `id_client` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `id_client_idx` (`id_client`),
  CONSTRAINT `id_client`
    FOREIGN KEY (`id_client`)
    REFERENCES `client` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
);

-- Tabla 'medical_shift', hace referencia a la tabla de Turnos --
CREATE TABLE IF NOT EXISTS `medical_shift` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `reason` VARCHAR(45) NOT NULL,
  `date` DATE NOT NULL,
  `price` DECIMAL(10,2) NOT NULL,
  `status` VARCHAR(20) NOT NULL,
  `id_pet` INT NOT NULL,
  `id_client` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `id_pet_idx` (`id_pet`),
  INDEX `id_client_idx` (`id_client`),
  CONSTRAINT `id_pet`
    FOREIGN KEY (`id_pet`)
    REFERENCES `pet` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `id_client_medical_shift`
    FOREIGN KEY (`id_client`)
    REFERENCES `client` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

-- Tabla 'medical_history', hace referencia a la tabla de Historial Médico --
CREATE TABLE IF NOT EXISTS `medical_history` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `date` DATE NOT NULL,
  `description` VARCHAR(45) NOT NULL,
  `treatment` VARCHAR(45) NOT NULL,
  `vaccines` VARCHAR(45) NULL,
  `id_pet` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `id_pet_idx` (`id_pet`),
  CONSTRAINT `id_pet_history`
    FOREIGN KEY (`id_pet`)
    REFERENCES `pet` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

-- INSERCIÓN DE DATOS DE PRUEBA --
-- Inserción de datos en la tabla 'client' --
INSERT INTO `client` (`name`, `phone`, `email`) 
VALUES 
	('Juan Pérez', '1234567890', 'juan.perez@email.com'),
	('María López', '0987654321', 'maria.lopez@email.com');

-- Inserción de datos en la tabla 'pet' --
INSERT INTO `pet` (`name`, `species`, `race`, `gender`, `age`, `id_client`) 
VALUES 
	('Bobby', 'Perro', 'Labrador', 'Macho', 3, 1),
	('Mia', 'Gato', 'Siames', 'Hembra', 2, 2);

-- Inserción de datos en la tabla 'medical_shift' --
INSERT INTO `medical_shift` (`reason`, `date`, `price`, `status`,`id_pet`, `id_client`) 
VALUES 
	('Vacunación', '2024-11-18', 200.50, 'Confirmado', 1, 1),
	('Chequeo general', '2024-11-19', 150.00, 'Confirmado', 2, 2);


-- CONSULTA DE DATOS DE PRUEBA --
SELECT 
    ms.`id` AS 'ID Turno', 
    c.`name` AS 'Nombre Cliente',
    p.`name` AS 'Nombre Mascota',
    ms.`reason` AS 'Motivo Turno', 
    ms.`date` AS 'Fecha Turno', 
    ms.`price` AS 'Precio Turno',
    ms.`status` AS 'Estado'
FROM 
    `medical_shift` ms
JOIN 
    `pet` p ON ms.`id_pet` = p.`id`
JOIN 
    `client` c ON p.`id_client` = c.`id`;


-- BORRADO DE DATOS DE PRUEBA --
-- Desactivar el "safe update mode" --
SET SQL_SAFE_UPDATES = 0;

DELETE FROM `medical_shift`;
DELETE FROM `pet`;  
DELETE FROM `client`;

-- Reactivar el "safe update mode" --
SET SQL_SAFE_UPDATES = 1;

SELECT * FROM `client`;
SELECT * FROM `pet`;
SELECT * FROM `medical_shift`;





    
    
