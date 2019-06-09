
-- comprobaciones para MySQL 

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Creamos la base de datos y si existe se sustituye
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `proyectodb` ;
CREATE SCHEMA IF NOT EXISTS `proyectodb` DEFAULT CHARACTER SET utf8 ;
USE `proyectodb` ;

-- -------------------------------------------------------
-- Creamos un usuario para el acceso a la base de datos
-- -------------------------------------------------------
DROP USER IF EXISTS 'rcastillejose'@'%';
CREATE USER 'rcastillejose'@'%';
GRANT ALL PRIVILEGES ON proyectodb.* TO 'rcastillejose'@'%' IDENTIFIED BY '1207';
GRANT all PRIVILEGES on mysql.proc TO 'rcastillejose'@'%' IDENTIFIED BY '1207';

-- -----------------------------------------------------
-- Table `proyectodb`.`Alumno`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyectodb`.`Alumno` (
  `email` VARCHAR(50) UNIQUE NOT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  `Apellidos` VARCHAR(45) NOT NULL,
  `login` VARCHAR(45) NOT NULL,
  `passwd` BLOB NOT NULL,
  PRIMARY KEY (`email`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `proyectodb`.`Curso`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyectodb`.`Curso` (
  `cursoYear` INT NOT NULL,
  PRIMARY KEY (`cursoYear`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `proyectodb`.`Periodo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyectodb`.`Periodo` (
  `idPeriodo` INT NOT NULL AUTO_INCREMENT,
  `dia_inicio` DATE NOT NULL,
  `dia_fin` DATE NOT NULL,
  `hora_inicio` TIME NOT NULL,
  `hora_fin` TIME NOT NULL,
  `intervalo` TIME NOT NULL,
  `habilitado` TINYINT(1) NOT NULL,
  `cursoYear` INT NOT NULL,
  PRIMARY KEY (`idPeriodo`),
  INDEX `fk_Periodo_Curso1_idx` (`cursoYear` ASC),
  UNIQUE (`dia_inicio`,`dia_fin`, `hora_inicio`,`hora_fin`),
  CONSTRAINT `fk_Periodo_Curso1`
    FOREIGN KEY (`cursoYear`)
    REFERENCES `proyectodb`.`Curso` (`cursoYear`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `proyectodb`.`Reserva`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyectodb`.`Reserva` (
  `idReserva` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(50) UNIQUE,
  `reserva_dia` DATE NOT NULL,
  `reserva_hora` TIME NOT NULL,
  `idPeriodo` INT NOT NULL,
  PRIMARY KEY (`idReserva`),
  INDEX `fk_Periodo_idx` (`idPeriodo` ASC),
  INDEX `fk_Alumno_idx` (`email` ASC),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC),
  UNIQUE (reserva_dia,reserva_hora),
  CONSTRAINT `fk_Periodo`
    FOREIGN KEY (`idPeriodo`)
    REFERENCES `proyectodb`.`Periodo` (`idPeriodo`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Alumno`
    FOREIGN KEY (`email`)
    REFERENCES `proyectodb`.`Alumno` (`email`)
    ON DELETE SET NULL
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `proyectodb`.`Mensaje`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyectodb`.`Mensaje` (
  `tipoMensaje` VARCHAR(10) NOT NULL,
  `Cuerpo` LONGTEXT NOT NULL,
  PRIMARY KEY (`tipoMensaje`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- ------------------------------------------------------
-- -   Creamos los procedimientos
-- ------------------------------------------------------
DELIMITER @@
DROP PROCEDURE IF EXISTS crear_periodo @@
CREATE PROCEDURE crear_periodo(IN d_inicio DATE, IN d_fin DATE, IN hora_inicio TIME, IN hora_fin TIME, IN intervalo TIME, IN cursoYear INT) 
BEGIN

	DECLARE lastNumeroPeriodo INT;
	DECLARE existeYear INT;
	DECLARE auxDia DATE;
	DECLARE auxHora TIME;

	DECLARE EXIT HANDLER FOR SQLEXCEPTION
   	BEGIN
		ROLLBACK;
	    SHOW ERRORS LIMIT 1;
	    RESIGNAL;	       
   	END;

    SELECT COUNT(*) INTO existeYear FROM Curso;
    SELECT IFNULL(MAX(idPeriodo),0) INTO lastNumeroPeriodo FROM Periodo;
    SET auxDia=d_inicio;
    SET auxHora=hora_inicio;
    
	IF DAYOFWEEK(d_inicio) = 1 OR DAYOFWEEK(d_inicio) = 7 OR DAYOFWEEK(d_fin) = 1 OR DAYOFWEEK(d_fin) = 7
   	THEN
		SIGNAL SQLSTATE '45000' 
		SET MESSAGE_TEXT = 'Los dias de inicio y de fin han de estar dentro de la semana laboral', 
		MYSQL_ERRNO = 1000; 
	ELSEIF d_inicio > d_fin
	THEN
		SIGNAL SQLSTATE '45000' 
		SET MESSAGE_TEXT = 'El dia de inicio no puede ser superior al final', 
		MYSQL_ERRNO = 1001;
	ELSEIF hora_inicio > hora_fin
	THEN
		SIGNAL SQLSTATE '45000' 
		SET MESSAGE_TEXT = 'La hora de inicio no puede ser superior a la final', 
		MYSQL_ERRNO = 1002;
	ELSEIF d_inicio < CURDATE()
	THEN
		SIGNAL SQLSTATE '45000' 
		SET MESSAGE_TEXT = 'El periodo no puede ser anterior a hoy', 
		MYSQL_ERRNO = 1003;
	ELSEIF existeYear = 0
	THEN 
		SIGNAL SQLSTATE '45000' 
		SET MESSAGE_TEXT = 'El year no existe, por favor creelo antes de proceder con el periodo', 
		MYSQL_ERRNO = 1004;
	ELSE
	 	START TRANSACTION;
        
	    	INSERT INTO Periodo (idPeriodo,dia_inicio,dia_fin,hora_inicio,hora_fin,intervalo,cursoYear,habilitado)
	    	VALUES (lastNumeroPeriodo +1,d_inicio,d_fin,hora_inicio,hora_fin,intervalo,cursoYear,true);

		bucle_dias: LOOP	
		    IF DAYOFWEEK(auxDia) = 1 
		    THEN 
			SET auxDia=auxDia+1;
		    ELSEIF DAYOFWEEK(auxDia) = 7 
		    THEN 
			SET auxDia=auxDia+2;
		    END IF;

		    IF auxDia>=d_fin THEN
		      LEAVE bucle_dias;
		    END IF;	
		    
			SET auxHora = hora_inicio;
			bucle_reservas: LOOP
		
				IF auxHora>=hora_fin THEN
			    	   LEAVE bucle_reservas;
			    	END IF;

				INSERT INTO Reserva(reserva_dia,reserva_hora,idPeriodo) VALUES(auxDia,auxHora,lastNumeroPeriodo+1); 
			    	SET auxHora=addtime(auxHora,intervalo);				

			END LOOP bucle_reservas;
			
		    SET auxDia=addDate(auxDia,1);

		    
		END LOOP bucle_dias;

		COMMIT;
	END IF;

END @@
DELIMITER ;

-- ------------------------------------------------------------------------------------------

DELIMITER @@
DROP PROCEDURE IF EXISTS `anular_reserva` @@
CREATE PROCEDURE `anular_reserva`(IN emailN VARCHAR(50))
BEGIN

	DECLARE existe_reserva INT;

   	SELECT COUNT(email) INTO existe_reserva FROM Reserva WHERE email=emailN;

    IF existe_reserva < 1
	THEN
		SIGNAL SQLSTATE '45000' 
		SET MESSAGE_TEXT = 'La reserva no existe', 
		MYSQL_ERRNO = 1009;		
    ELSE
	UPDATE Reserva SET email=NULL WHERE email=emailN;
    END IF;
END@@

DELIMITER ;


-- ---------------------------------------------------------------------------------------------------------------
INSERT INTO Curso VALUES(2019);
INSERT INTO Alumno VALUES('rcastillejose@ieslavereda.es','Raul','Castillejos','rcastillejos',PASSWORD('1207'));
CALL crear_periodo('2019-06-10','2019-06-21','09:00:00','11:00:00','00:30:00',2019);




