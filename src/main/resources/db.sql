
-- Create database

DROP DATABASE IF EXISTS `research_hub`;
CREATE DATABASE  IF NOT EXISTS `research_hub`;
USE `research_hub`;


-- Table structures for categories

CREATE TABLE `content_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

CREATE TABLE `content_subtype` (
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


-- Table structures for objects

CREATE TABLE `content` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `summary` varchar(255) DEFAULT NULL,
  `description` TEXT DEFAULT NULL,
  `actionable_info` TEXT DEFAULT NULL,
  `additional_info` TEXT DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `call_to_action` TEXT DEFAULT NULL,
  `created` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated` DATETIME ON UPDATE CURRENT_TIMESTAMP,
  `audited` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

CREATE TABLE `org_unit` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `url` TEXT DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

CREATE TABLE `person` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `first_name` TEXT DEFAULT NULL,
  `last_name` TEXT DEFAULT NULL,
  `email` TEXT DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `job_title` TEXT DEFAULT NULL,
  `directory_url` TEXT DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;


-- Table structure for many to one relationships

CREATE TABLE `content_webpage` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `content_id` int(11) unsigned NOT NULL,
  `title` TEXT DEFAULT NULL,
  `url` TEXT NOT NULL,
  PRIMARY KEY (`id`),
  KEY (`content_id`),
  CONSTRAINT FOREIGN KEY (`content_id`) REFERENCES `content` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

CREATE TABLE `content_keyword` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `content_id` int(11) unsigned NOT NULL,
  `keyword` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY (`content_id`),
  CONSTRAINT FOREIGN KEY (`content_id`) REFERENCES `content` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

-- Table structure for many to many relationships

CREATE TABLE `content_content_type` (
  `content_id` int(11) unsigned NOT NULL,
  `content_type_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`content_id`,`content_type_id`),
  KEY (`content_id`),
  KEY (`content_type_id`),
  CONSTRAINT FOREIGN KEY (`content_id`) REFERENCES `content` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FOREIGN KEY (`content_type_id`) REFERENCES `content_type` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `content_content_subtype` (
  `content_id` int(11) unsigned NOT NULL,
  `content_subtype_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`content_id`,`content_subtype_id`),
  KEY (`content_id`),
  KEY (`content_subtype_id`),
  CONSTRAINT FOREIGN KEY (`content_id`) REFERENCES `content` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FOREIGN KEY (`content_subtype_id`) REFERENCES `content_subtype` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `content_org_unit` (
  `content_id` int(11) unsigned NOT NULL,
  `org_unit_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`content_id`,`org_unit_id`),
  KEY (`content_id`),
  KEY (`org_unit_id`),
  CONSTRAINT FOREIGN KEY (`content_id`) REFERENCES `content` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FOREIGN KEY (`org_unit_id`) REFERENCES `org_unit` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `content_research_phase` (
  `content_id` int(11) unsigned NOT NULL,
  `research_phase_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`content_id`,`research_phase_id`),
  KEY (`content_id`),
  KEY (`research_phase_id`),
  CONSTRAINT FOREIGN KEY (`content_id`) REFERENCES `content` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FOREIGN KEY (`research_phase_id`) REFERENCES `research_phase` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `person_org_unit` (
  `person_id` int(11) unsigned NOT NULL,
  `org_unit_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`person_id`,`org_unit_id`),
  KEY (`person_id`),
  KEY (`org_unit_id`),
  CONSTRAINT FOREIGN KEY (`person_id`) REFERENCES `person` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FOREIGN KEY (`org_unit_id`) REFERENCES `org_unit` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `person_content_role` (
  `person_id` int(11) unsigned NOT NULL,
  `content_id` int(11) unsigned NOT NULL,
  `role_type_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`person_id`, `content_id`, `role_type_id`),
  CONSTRAINT FOREIGN KEY (`person_id`) REFERENCES `person` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FOREIGN KEY (`content_id`) REFERENCES `content` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FOREIGN KEY (`role_type_id`) REFERENCES `role_type` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
