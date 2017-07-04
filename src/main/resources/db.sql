
-- Create database

DROP DATABASE IF EXISTS `research_hub`;
CREATE DATABASE  IF NOT EXISTS `research_hub`;
USE `research_hub`;


-- Table structure for categories

CREATE TABLE `content_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

CREATE TABLE `research_phase` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

CREATE TABLE `role_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;


-- Table structure for table `content`

CREATE TABLE `content` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `summary` varchar(255) DEFAULT NULL,
  `description` TEXT DEFAULT NULL,
  `actionable_info` TEXT DEFAULT NULL,
  `additional_info` TEXT DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `created` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated` DATETIME ON UPDATE CURRENT_TIMESTAMP,
  `audited` DATETIME DEFAULT NULL,
  `content_type_id` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_content_contenttypeid_idx` (`content_type_id`),
  CONSTRAINT `fk_content_contenttypeid` FOREIGN KEY (`content_type_id`) REFERENCES `content_type` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;


-- Table structure for table `content`

CREATE TABLE `person` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `first_name` TEXT DEFAULT NULL,
  `last_name` TEXT DEFAULT NULL,
  `email` TEXT DEFAULT NULL,
  `directory_url` TEXT DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

CREATE TABLE `content_role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `content_id` int(11) unsigned NOT NULL,
  `person_id` int(11) unsigned NOT NULL,
  `role_type_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY (`content_id`),
  KEY (`person_id`),
  KEY (`role_type_id`),
  UNIQUE KEY (`content_id`,`person_id`),
  CONSTRAINT FOREIGN KEY (`content_id`) REFERENCES `content` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FOREIGN KEY (`person_id`) REFERENCES `person` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FOREIGN KEY (`role_type_id`) REFERENCES `role_type` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- Table structure for many to one relationships

CREATE TABLE `external_url` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `content_id` int(11) unsigned NOT NULL,
  `name` TEXT DEFAULT NULL,
  `url` TEXT NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_external_url_contentid_idx` (`content_id`),
  CONSTRAINT `fk_external_url_contentid` FOREIGN KEY (`content_id`) REFERENCES `content` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

-- Table structure for many to many relationships

CREATE TABLE `content_research_phase` (
  `content_id` int(11) unsigned NOT NULL,
  `research_phase_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`content_id`,`research_phase_id`),
  KEY `fk_contentresearch_phase_content_idx` (`content_id`),
  KEY `fk_contentresearch_phase_research_phase_idx` (`research_phase_id`),
  CONSTRAINT `fk_contentresearch_phase_content` FOREIGN KEY (`content_id`) REFERENCES `content` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_contentresearch_phase_research_phase` FOREIGN KEY (`research_phase_id`) REFERENCES `research_phase` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;