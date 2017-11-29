
-- Create database

DROP DATABASE IF EXISTS `research_hub`;
CREATE DATABASE `research_hub`;

USE `research_hub`;

-- Table structures for categories

CREATE TABLE `action_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

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


-- Table structures for objects

CREATE TABLE `content` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `summary` varchar(255) DEFAULT NULL,
  `description` TEXT DEFAULT NULL,
  `actionable_info` TEXT DEFAULT NULL,
  `additional_info` TEXT DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `action` TEXT DEFAULT NULL,
  `action_type_id` int(11) unsigned DEFAULT NULL,
  `keywords` TEXT DEFAULT NULL,
  `created` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated` DATETIME ON UPDATE CURRENT_TIMESTAMP,
  `audited` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY (`action_type_id`),
  CONSTRAINT FOREIGN KEY (`action_type_id`) REFERENCES `action_type` (`id`),
  FULLTEXT KEY (`name`,`summary`,`description`,`actionable_info`,`additional_info`,`keywords`)
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
  PRIMARY KEY (`id`),
  FULLTEXT KEY (`title`,`first_name`,`last_name`,`job_title`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

CREATE TABLE `policy` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` TEXT DEFAULT NULL,
  `description` TEXT DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `url` TEXT DEFAULT NULL,
  PRIMARY KEY (`id`),
  FULLTEXT KEY (`name`,`description`)
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


CREATE TABLE `guide_category` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` TEXT DEFAULT NULL,
  `content_id` int(11) unsigned NOT NULL,
  `display_order` int(11) unsigned NOT NULL,
  `summary` TEXT DEFAULT NULL,
  `description` TEXT DEFAULT NULL,
  `additional_info` TEXT DEFAULT NULL,
  `icon` TEXT DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY (`content_id`),
  CONSTRAINT FOREIGN KEY (`content_id`) REFERENCES `content` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

-- Table structure for many to many relationships

CREATE TABLE `guide_category_content` (
  `guide_category_id` int(11) unsigned NOT NULL,
  `content_id` int(11) unsigned NOT NULL,
  `display_order` int(11) unsigned NOT NULL,
  PRIMARY KEY (`guide_category_id`,`content_id`),
  KEY (`guide_category_id`),
  KEY (`content_id`),
  CONSTRAINT FOREIGN KEY (`guide_category_id`) REFERENCES `guide_category` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FOREIGN KEY (`content_id`) REFERENCES `content` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `content_content_type` (
  `content_id` int(11) unsigned NOT NULL,
  `content_type_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`content_id`,`content_type_id`),
  KEY (`content_id`),
  KEY (`content_type_id`),
  CONSTRAINT FOREIGN KEY (`content_id`) REFERENCES `content` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FOREIGN KEY (`content_type_id`) REFERENCES `content_type` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `content_to_content` (
  `content_id` int(11) unsigned NOT NULL,
  `similar_content_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`content_id`,`similar_content_id`),
  KEY (`content_id`),
  KEY (`similar_content_id`),
  CONSTRAINT FOREIGN KEY (`content_id`) REFERENCES `content` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FOREIGN KEY (`similar_content_id`) REFERENCES `content` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
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

CREATE TABLE `content_policy` (
  `content_id` int(11) unsigned NOT NULL,
  `policy_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`content_id`,`policy_id`),
  KEY (`content_id`),
  KEY (`policy_id`),
  CONSTRAINT FOREIGN KEY (`content_id`) REFERENCES `content` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FOREIGN KEY (`policy_id`) REFERENCES `policy` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
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