
-- Create database

DROP DATABASE IF EXISTS `research_hub`;
CREATE DATABASE  IF NOT EXISTS `research_hub`;
USE `research_hub`;


-- Table structure for categories

DROP TABLE IF EXISTS `product_type`;
CREATE TABLE `product_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `provider`;
CREATE TABLE `provider` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `life_cycle`;
CREATE TABLE `life_cycle` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `eligibility`;
CREATE TABLE `eligibility` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `service_type`;
CREATE TABLE `service_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `programme`;
CREATE TABLE `programme` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `study_level`;
CREATE TABLE `study_level` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `cost`;
CREATE TABLE `cost` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

-- Table structure for table `product`

DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `product_type_id` int(11) unsigned DEFAULT NULL,
  `provider_id` int(11) unsigned DEFAULT NULL,
  `summary` TEXT DEFAULT NULL,
  `image_uri` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_product_productcategoryid_idx` (`product_type_id`),
  KEY `fk_product_providercategoryid_idx` (`provider_id`),
  CONSTRAINT `fk_product_productcategoryid` FOREIGN KEY (`product_type_id`) REFERENCES `product_type` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_product_providercategoryid` FOREIGN KEY (`provider_id`) REFERENCES `provider` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

-- Table structure for many to one relationships

DROP TABLE IF EXISTS `requirement`;
CREATE TABLE `requirement` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `product_id` int(11) unsigned NOT NULL,
  `name` TEXT DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_requirement_productid_idx` (`product_id`),
  CONSTRAINT `fk_requirement_productid` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `consideration`;
CREATE TABLE `consideration` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `product_id` int(11) unsigned NOT NULL,
  `name` TEXT DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_consideration_productid_idx` (`product_id`),
  CONSTRAINT `fk_consideration_productid` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `feature`;
CREATE TABLE `feature` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `product_id` int(11) unsigned NOT NULL,
  `name` TEXT DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_feature_productid_idx` (`product_id`),
  CONSTRAINT `fk_feature_productid` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `limitation`;
CREATE TABLE `limitation` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `product_id` int(11) unsigned NOT NULL,
  `name` TEXT DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_limitation_productid_idx` (`product_id`),
  CONSTRAINT `fk_limitation_productid` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `support`;
CREATE TABLE `support` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `product_id` int(11) unsigned NOT NULL,
  `name` TEXT DEFAULT NULL,
  `url` TEXT DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_support_productid_idx` (`product_id`),
  CONSTRAINT `fk_support_productid` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `reference`;
CREATE TABLE `reference` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `product_id` int(11) unsigned NOT NULL,
  `name` TEXT DEFAULT NULL,
  `url` TEXT DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_reference_productid_idx` (`product_id`),
  CONSTRAINT `fk_reference_productid` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `contact`;
CREATE TABLE `contact` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `product_id` int(11) unsigned NOT NULL,
  `name` TEXT DEFAULT NULL,
  `url` TEXT DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_contact_productid_idx` (`product_id`),
  CONSTRAINT `fk_contact_productid` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;


-- Table structure for many to many relationships

DROP TABLE IF EXISTS `product_life_cycle`;
CREATE TABLE `product_life_cycle` (
  `product_id` int(11) unsigned NOT NULL,
  `life_cycle_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`product_id`,`life_cycle_id`),
  KEY `fk_productlifecycle_product_idx` (`product_id`),
  KEY `fk_productlifecycle_lifecycle_idx` (`life_cycle_id`),
  CONSTRAINT `fk_productlifecycle_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_productlifecycle_lifecycle` FOREIGN KEY (`life_cycle_id`) REFERENCES `life_cycle` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `product_eligibility`;
CREATE TABLE `product_eligibility` (
  `product_id` int(11) unsigned NOT NULL,
  `eligibility_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`product_id`,`eligibility_id`),
  KEY `fk_producteligibility_product_idx` (`product_id`),
  KEY `fk_producteligibility_eligibility_idx` (`eligibility_id`),
  CONSTRAINT `fk_producteligibility_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_producteligibility_eligibility` FOREIGN KEY (`eligibility_id`) REFERENCES `eligibility` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `product_service_type`;
CREATE TABLE `product_service_type` (
  `product_id` int(11) unsigned NOT NULL,
  `service_type_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`product_id`,`service_type_id`),
  KEY `fk_productservicetype_product_idx` (`product_id`),
  KEY `fk_productservicetype_servicetype_idx` (`service_type_id`),
  CONSTRAINT `fk_productservicetype_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_productservicetype_servicetype` FOREIGN KEY (`service_type_id`) REFERENCES `service_type` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `product_programme`;
CREATE TABLE `product_programme` (
  `product_id` int(11) unsigned NOT NULL,
  `programme_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`product_id`,`programme_id`),
  KEY `fk_productprogramme_product_idx` (`product_id`),
  KEY `fk_productprogramme_programme_idx` (`programme_id`),
  CONSTRAINT `fk_productprogramme_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_productprogramme_programme` FOREIGN KEY (`programme_id`) REFERENCES `programme` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `product_study_level`;
CREATE TABLE `product_study_level` (
  `product_id` int(11) unsigned NOT NULL,
  `study_level_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`product_id`,`study_level_id`),
  KEY `fk_productstudylevel_product_idx` (`product_id`),
  KEY `fk_productstudylevel_studylevel_idx` (`study_level_id`),
  CONSTRAINT `fk_productstudylevel_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_productstudylevel_studylevel` FOREIGN KEY (`study_level_id`) REFERENCES `study_level` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `product_cost`;
CREATE TABLE `product_cost` (
  `product_id` int(11) unsigned NOT NULL,
  `cost_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`product_id`,`cost_id`),
  KEY `fk_productcost_product_idx` (`product_id`),
  KEY `fk_productcost_cost_idx` (`cost_id`),
  CONSTRAINT `fk_productcost_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_productcost_cost` FOREIGN KEY (`cost_id`) REFERENCES `cost` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;