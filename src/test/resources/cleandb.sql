-- Adminer 4.8.1 MySQL 8.0.22 dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;



DROP TABLE IF EXISTS `employees`;
CREATE TABLE `employees` (
                             `id` int NOT NULL AUTO_INCREMENT,
                             `position` varchar(50) DEFAULT NULL,
                             `first_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                             `middle_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                             `last_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                             `salary` decimal(10,2) NOT NULL,
                             `address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                             `city` varchar(50) NOT NULL,
                             `state` char(2) NOT NULL,
                             `zip_address` varchar(14) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                             `with_us` int NOT NULL DEFAULT '1',
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



DROP TABLE IF EXISTS `item_location`;
CREATE TABLE `item_location` (
                                 `id` int NOT NULL AUTO_INCREMENT,
                                 `item_id` int NOT NULL,
                                 `location_id` int NOT NULL,
                                 PRIMARY KEY (`id`),
                                 KEY `location_id` (`location_id`),
                                 KEY `item_id` (`item_id`),
                                 CONSTRAINT `item_location_ibfk_2` FOREIGN KEY (`location_id`) REFERENCES `locations` (`id`),
                                 CONSTRAINT `item_location_ibfk_3` FOREIGN KEY (`item_id`) REFERENCES `items` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `item_location` (`id`, `item_id`, `location_id`) VALUES
(1,	2,	1),
(2,	2,	2);

DROP TABLE IF EXISTS `items`;
CREATE TABLE `items` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `name` varchar(100) NOT NULL,
                         `description` varchar(100) NOT NULL,
                         `date_acquired` date NOT NULL,
                         `is_lost` int NOT NULL,
                         `is_museum_item` int NOT NULL,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;




DROP TABLE IF EXISTS `employee_item`;
CREATE TABLE `employee_item` (
                                 `employee_id` int NOT NULL,
                                 `item_id` int NOT NULL,
                                 `worth_value` decimal(10,2) NOT NULL,
                                 `id` int NOT NULL AUTO_INCREMENT,
                                 PRIMARY KEY (`id`),
                                 KEY `employee_id` (`employee_id`),
                                 KEY `item_id` (`item_id`),
                                 CONSTRAINT `employee_item_ibfk_1` FOREIGN KEY (`item_id`) REFERENCES `items` (`id`),
                                 CONSTRAINT `employee_item_ibfk_2` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `employee_item` (`employee_id`, `item_id`, `worth_value`, `id`) VALUES
(1,	2,	123.12,	1);

DROP TABLE IF EXISTS `locations`;
CREATE TABLE `locations` (
  `id` int NOT NULL AUTO_INCREMENT,
  `storage_type` varchar(100) NOT NULL,
  `description` varchar(255) NOT NULL,
  `date_when_put` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `locations` (`id`, `storage_type`, `description`, `date_when_put`) VALUES (1,	'room 4A',	'right top shelf B7',	'2021-06-18');
INSERT INTO `locations` (`id`, `storage_type`, `description`, `date_when_put`) VALUES (2,	'room 5A',	'left top shelf B7',	'2021-06-18');

DROP TABLE IF EXISTS `lost_items`;
CREATE TABLE `lost_items` (
  `item_id` int NOT NULL,
  `date_lost` date NOT NULL,
  `description` varchar(110) NOT NULL,
  PRIMARY KEY (`item_id`),
  CONSTRAINT `lost_items_ibfk_1` FOREIGN KEY (`item_id`) REFERENCES `items` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `lost_items` (`item_id`, `date_lost`, `description`) VALUES
(1,	'2021-06-18',	'desc');

DROP TABLE IF EXISTS `phone_numbers`;
CREATE TABLE `phone_numbers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `phone_number` int NOT NULL,
  `employee_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `employee_id` (`employee_id`),
  CONSTRAINT `phone_numbers_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



INSERT INTO `items` (`id`, `name`, `description`, `date_acquired`, `is_lost`, `is_museum_item`) VALUES
(1,	'TestName',	'testDesc',	'2021-06-18',	0,	1),
(2,	'Ancient Dioniss Jug',	'Ancient Jug for wine. XVII B.C',	'2021-06-18',	0,	1);

INSERT INTO `employees` ( `position`, `first_name`, `middle_name`, `last_name`, `salary`, `address`, `city`, `state`, `zip_address`, `with_us`) VALUES
('manager',	'Myke',	NULL,	'Turchanov',	333.33,	'address',	'city',	'WI',	'1233',	1);


INSERT INTO `phone_numbers` (`id`, `phone_number`, `employee_id`) VALUES
(1,	123213,	2),
(2,	12321312,	2);

-- 2021-06-24 04:57:25
