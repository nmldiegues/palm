-- MySQL dump 10.13  Distrib 5.1.54, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: palm
-- ------------------------------------------------------
-- Server version	5.1.54-1ubuntu4

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ArticleRecord`
--

DROP TABLE IF EXISTS `ArticleRecord`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ArticleRecord` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `creationDate` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `year` int(11) NOT NULL,
  `submitter_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK401744C7D5D1A100` (`submitter_id`),
  CONSTRAINT `FK401744C7D5D1A100` FOREIGN KEY (`submitter_id`) REFERENCES `User` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ArticleRecord`
--

LOCK TABLES `ArticleRecord` WRITE;
/*!40000 ALTER TABLE `ArticleRecord` DISABLE KEYS */;
/*!40000 ALTER TABLE `ArticleRecord` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ArticleRecord_Author`
--

DROP TABLE IF EXISTS `ArticleRecord_Author`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ArticleRecord_Author` (
  `ArticleRecord_id` bigint(20) NOT NULL,
  `authors_id` bigint(20) NOT NULL,
  KEY `FK6F002823A9FEF0A5` (`authors_id`),
  KEY `FK6F002823DCC854D2` (`ArticleRecord_id`),
  CONSTRAINT `FK6F002823A9FEF0A5` FOREIGN KEY (`authors_id`) REFERENCES `Author` (`id`),
  CONSTRAINT `FK6F002823DCC854D2` FOREIGN KEY (`ArticleRecord_id`) REFERENCES `ArticleRecord` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ArticleRecord_Author`
--

LOCK TABLES `ArticleRecord_Author` WRITE;
/*!40000 ALTER TABLE `ArticleRecord_Author` DISABLE KEYS */;
/*!40000 ALTER TABLE `ArticleRecord_Author` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ArticleRecord_Tag`
--

DROP TABLE IF EXISTS `ArticleRecord_Tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ArticleRecord_Tag` (
  `ArticleRecord_id` bigint(20) NOT NULL,
  `tags_id` bigint(20) NOT NULL,
  PRIMARY KEY (`ArticleRecord_id`,`tags_id`),
  KEY `FK25F60002F8B068F3` (`tags_id`),
  KEY `FK25F60002DCC854D2` (`ArticleRecord_id`),
  CONSTRAINT `FK25F60002DCC854D2` FOREIGN KEY (`ArticleRecord_id`) REFERENCES `ArticleRecord` (`id`),
  CONSTRAINT `FK25F60002F8B068F3` FOREIGN KEY (`tags_id`) REFERENCES `Tag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ArticleRecord_Tag`
--

LOCK TABLES `ArticleRecord_Tag` WRITE;
/*!40000 ALTER TABLE `ArticleRecord_Tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `ArticleRecord_Tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Author`
--

DROP TABLE IF EXISTS `Author`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Author` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Author`
--

LOCK TABLES `Author` WRITE;
/*!40000 ALTER TABLE `Author` DISABLE KEYS */;
/*!40000 ALTER TABLE `Author` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Author_ArticleRecord`
--

DROP TABLE IF EXISTS `Author_ArticleRecord`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Author_ArticleRecord` (
  `Author_id` bigint(20) NOT NULL,
  `articles_id` bigint(20) NOT NULL,
  KEY `FK896C5D335B61B482` (`Author_id`),
  KEY `FK896C5D33FF03AF9C` (`articles_id`),
  CONSTRAINT `FK896C5D335B61B482` FOREIGN KEY (`Author_id`) REFERENCES `Author` (`id`),
  CONSTRAINT `FK896C5D33FF03AF9C` FOREIGN KEY (`articles_id`) REFERENCES `ArticleRecord` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Author_ArticleRecord`
--

LOCK TABLES `Author_ArticleRecord` WRITE;
/*!40000 ALTER TABLE `Author_ArticleRecord` DISABLE KEYS */;
/*!40000 ALTER TABLE `Author_ArticleRecord` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Citation`
--

DROP TABLE IF EXISTS `Citation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Citation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bibNum` int(11) NOT NULL,
  `citation` longtext,
  `reference` longtext,
  `recordThatReferences_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKADDC6F078C8FE8F9` (`recordThatReferences_id`),
  CONSTRAINT `FKADDC6F078C8FE8F9` FOREIGN KEY (`recordThatReferences_id`) REFERENCES `ArticleRecord` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Citation`
--

LOCK TABLES `Citation` WRITE;
/*!40000 ALTER TABLE `Citation` DISABLE KEYS */;
/*!40000 ALTER TABLE `Citation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Document`
--

DROP TABLE IF EXISTS `Document`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Document` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` longblob,
  `creationDate` datetime DEFAULT NULL,
  `fileName` varchar(255) DEFAULT NULL,
  `identification` varchar(255) DEFAULT NULL,
  `mimeType` varchar(255) DEFAULT NULL,
  `record_id` bigint(20) DEFAULT NULL,
  `type_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3737353B28E1C3DD` (`type_id`),
  KEY `FK3737353B968ED08` (`record_id`),
  CONSTRAINT `FK3737353B28E1C3DD` FOREIGN KEY (`type_id`) REFERENCES `DocumentType` (`id`),
  CONSTRAINT `FK3737353B968ED08` FOREIGN KEY (`record_id`) REFERENCES `ArticleRecord` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Document`
--

LOCK TABLES `Document` WRITE;
/*!40000 ALTER TABLE `Document` DISABLE KEYS */;
/*!40000 ALTER TABLE `Document` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DocumentType`
--

DROP TABLE IF EXISTS `DocumentType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DocumentType` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DocumentType`
--

LOCK TABLES `DocumentType` WRITE;
/*!40000 ALTER TABLE `DocumentType` DISABLE KEYS */;
/*!40000 ALTER TABLE `DocumentType` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Role`
--

DROP TABLE IF EXISTS `Role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Role` (
  `DTYPE` varchar(31) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Role`
--

LOCK TABLES `Role` WRITE;
/*!40000 ALTER TABLE `Role` DISABLE KEYS */;
INSERT INTO `Role` VALUES ('Manager',1),('Submitter',2);
/*!40000 ALTER TABLE `Role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Tag`
--

DROP TABLE IF EXISTS `Tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Tag`
--

LOCK TABLES `Tag` WRITE;
/*!40000 ALTER TABLE `Tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `Tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `firstName` varchar(255) DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (1,'nmld@ist.utl.pt','Nuno','Diegues','Qdgjq5YO9JlzzmKDd9e/XQ==');
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User_Role`
--

DROP TABLE IF EXISTS `User_Role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User_Role` (
  `User_id` bigint(20) NOT NULL,
  `roles_id` bigint(20) NOT NULL,
  PRIMARY KEY (`User_id`,`roles_id`),
  KEY `FK8B9F886A47140EFE` (`User_id`),
  KEY `FK8B9F886A4001C377` (`roles_id`),
  CONSTRAINT `FK8B9F886A4001C377` FOREIGN KEY (`roles_id`) REFERENCES `Role` (`id`),
  CONSTRAINT `FK8B9F886A47140EFE` FOREIGN KEY (`User_id`) REFERENCES `User` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User_Role`
--

LOCK TABLES `User_Role` WRITE;
/*!40000 ALTER TABLE `User_Role` DISABLE KEYS */;
INSERT INTO `User_Role` VALUES (1,1),(1,2);
/*!40000 ALTER TABLE `User_Role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-07-16 10:31:40
