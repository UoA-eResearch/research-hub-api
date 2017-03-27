
-- Create database

DROP DATABASE IF EXISTS `research_hub`;
CREATE DATABASE  IF NOT EXISTS `research_hub`;
USE `research_hub`;


-- Table structure for categories

DROP TABLE IF EXISTS `product_category`;
CREATE TABLE `product_category` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `provider_category`;
CREATE TABLE `provider_category` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `life_cycle_category`;
CREATE TABLE `life_cycle_category` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;


-- Table structure for table `product`

DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `product_category_id` int(11) unsigned DEFAULT NULL,
  `provider_category_id` int(11) unsigned DEFAULT NULL,
  `summary` TEXT DEFAULT NULL,
  `image_uri` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_product_productcategoryid_idx` (`product_category_id`),
  KEY `fk_product_providercategoryid_idx` (`provider_category_id`),
  CONSTRAINT `fk_product_productcategoryid` FOREIGN KEY (`product_category_id`) REFERENCES `product_category` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_product_providercategoryid` FOREIGN KEY (`provider_category_id`) REFERENCES `provider_category` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;


-- Table structure for many to many relationships

DROP TABLE IF EXISTS `product_life_cycle`;
CREATE TABLE `product_life_cycle` (
  `product_id` int(11) unsigned NOT NULL,
  `life_cycle_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`product_id`,`life_cycle_id`),
  KEY `fk_bookpublisher_book_idx` (`product_id`),
  KEY `fk_bookpublisher_lifecycle_idx` (`life_cycle_id`),
  CONSTRAINT `fk_productlifecycle_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_productlifecycle_lifecycle` FOREIGN KEY (`life_cycle_id`) REFERENCES `life_cycle_category` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;