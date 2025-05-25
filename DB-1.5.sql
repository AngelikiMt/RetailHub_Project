CREATE DATABASE IF NOT EXISTS retailhub_db;

USE `retailhub_db` ;

-- -----------------------------------------------------
-- Table `retailhub_db`.`Client`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `retailhub_db`.`Client` ;

CREATE TABLE IF NOT EXISTS `retailhub_db`.`Client` (
  `clientId` BIGINT NOT NULL AUTO_INCREMENT,
  `firstName` VARCHAR(50) NOT NULL,
  `lastName` VARCHAR(50) NOT NULL,
  `birthDate` DATE,
  `phoneNumber` VARCHAR(50) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `gender` ENUM('male', 'female', 'other', 'prefer_not_to_say') NULL DEFAULT 'prefer_not_to_say',
  `activeStatus` TINYINT NOT NULL DEFAULT 1,
  `dateJoined` DATE NULL DEFAULT (CURRENT_DATE),
  `clientSumTotal` DOUBLE NULL DEFAULT '0',
  `lastPurchaseDate` DATE NULL DEFAULT NULL,
  PRIMARY KEY (`clientId`),
  UNIQUE INDEX `phone_number` (`phoneNumber` ASC) VISIBLE,
  UNIQUE INDEX `email` (`email` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `retailhub_db`.`Store`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `retailhub_db`.`Store` ;

CREATE TABLE IF NOT EXISTS `retailhub_db`.`Store` (
  `storeId` INT NOT NULL AUTO_INCREMENT,
  `phone` VARCHAR(50) NOT NULL,
  `address` VARCHAR(100) NOT NULL,
  `country` VARCHAR(50) NOT NULL,
  `storeName` VARCHAR(100) NOT NULL,
  `active` TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (`storeId`),
  UNIQUE INDEX `phone_number` (`phone` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `retailhub_db`.`products`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `retailhub_db`.`products`;

CREATE TABLE `retailhub_db`.`products` (
  `productId` BIGINT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(100) NOT NULL,
  `price` DOUBLE NOT NULL,
  `cost` DOUBLE NOT NULL,
  `category` VARCHAR(50) NOT NULL,
  `active` BOOLEAN DEFAULT TRUE,
  PRIMARY KEY (`productId`)
) ENGINE=InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;



-- -----------------------------------------------------
-- Table `retailhub_db`.`stock`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `retailhub_db`.`stock` ;

CREATE TABLE IF NOT EXISTS `retailhub_db`.`stock` (
  `storeId` INT NOT NULL,
  `productId` BIGINT NOT NULL,
  `stockQuantity` INT NOT NULL,
  `activeFlag` TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (`storeId`, `productId`),
  INDEX `id_product` (`productId` ASC) VISIBLE,
  CONSTRAINT `has_stock_ibfk_1`
    FOREIGN KEY (`storeId`)
    REFERENCES `retailhub_db`.`Store` (`storeId`),
  CONSTRAINT `has_stock_ibfk_2`
    FOREIGN KEY (`productId`)
    REFERENCES `retailhub_db`.`products` (`productId`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `retailhub_db`.`transaction`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `retailhub_db`.`transaction` ;

CREATE TABLE IF NOT EXISTS `retailhub_db`.`transaction` (
  `transactionId` BIGINT NOT NULL AUTO_INCREMENT,
  `clientId` BIGINT NOT NULL,
  `storeId` INT NOT NULL,
  `dateTime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `paymentMethod` VARCHAR(30) NOT NULL,
  `sumTotal` DOUBLE NOT NULL DEFAULT '0',
  `discount` DOUBLE NULL DEFAULT '0',
  PRIMARY KEY (`transactionId`),
  INDEX `id_client` (`clientId` ASC) VISIBLE,
  INDEX `id_store` (`storeId` ASC) VISIBLE,
  CONSTRAINT `transaction_ibfk_1`
    FOREIGN KEY (`clientId`)
    REFERENCES `retailhub_db`.`Client` (`clientId`),
  CONSTRAINT `transaction_ibfk_2`
    FOREIGN KEY (`storeId`)
    REFERENCES `retailhub_db`.`Store` (`storeId`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `retailhub_db`.`includes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `retailhub_db`.`includes` ;

CREATE TABLE IF NOT EXISTS `retailhub_db`.`includes` (
  `transactionId` BIGINT NOT NULL,
  `soldQuantity` INT NOT NULL,
  `productId` BIGINT NOT NULL,
  PRIMARY KEY (`transactionId`, `productId`),
  INDEX `id_product` (`productId` ASC) VISIBLE,
  CONSTRAINT `includes_ibfk_1`
    FOREIGN KEY (`transactionId`)
    REFERENCES `retailhub_db`.`transaction` (`transactionId`),
  CONSTRAINT `includes_ibfk_2`
    FOREIGN KEY (`productId`)
    REFERENCES `retailhub_db`.`products` (`productId`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
