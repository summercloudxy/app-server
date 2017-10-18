-- MySQL dump 10.13  Distrib 5.6.37, for Linux (x86_64)
--
-- Host: 192.168.5.34    Database: smartfactory2
-- ------------------------------------------------------
-- Server version	5.6.35

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
-- Dumping data for table `tb_filterpress_config`
--

LOCK TABLES `tb_filterpress_config` WRITE;
/*!40000 ALTER TABLE `tb_filterpress_config` DISABLE KEYS */;
INSERT INTO `tb_filterpress_config` VALUES (26,'2492','feedIntelligent',1);
INSERT INTO `tb_filterpress_config` VALUES (27,'2493','feedIntelligent',1);
INSERT INTO `tb_filterpress_config` VALUES (28,'2494','feedIntelligent',1);
INSERT INTO `tb_filterpress_config` VALUES (29,'2495','feedIntelligent',1);
INSERT INTO `tb_filterpress_config` VALUES (30,'2496','feedIntelligent',1);
INSERT INTO `tb_filterpress_config` VALUES (31,'2496A','feedIntelligent',1);
INSERT INTO `tb_filterpress_config` VALUES (32,'2492','unloadIntelligent',0);
INSERT INTO `tb_filterpress_config` VALUES (33,'2493','unloadIntelligent',0);
INSERT INTO `tb_filterpress_config` VALUES (34,'2494','unloadIntelligent',1);
INSERT INTO `tb_filterpress_config` VALUES (35,'2495','unloadIntelligent',0);
INSERT INTO `tb_filterpress_config` VALUES (36,'2496','unloadIntelligent',0);
INSERT INTO `tb_filterpress_config` VALUES (37,'2496A','unloadIntelligent',0);
INSERT INTO `tb_filterpress_config` VALUES (38,'2492','feedConfirmNeed',1);
INSERT INTO `tb_filterpress_config` VALUES (39,'2493','feedConfirmNeed',1);
INSERT INTO `tb_filterpress_config` VALUES (40,'2494','feedConfirmNeed',1);
INSERT INTO `tb_filterpress_config` VALUES (41,'2495','feedConfirmNeed',1);
INSERT INTO `tb_filterpress_config` VALUES (42,'2496','feedConfirmNeed',1);
INSERT INTO `tb_filterpress_config` VALUES (43,'2496A','feedConfirmNeed',1);
INSERT INTO `tb_filterpress_config` VALUES (44,'2492','unloadConfirmNeed',1);
INSERT INTO `tb_filterpress_config` VALUES (45,'2493','unloadConfirmNeed',1);
INSERT INTO `tb_filterpress_config` VALUES (46,'2494','unloadConfirmNeed',1);
INSERT INTO `tb_filterpress_config` VALUES (47,'2495','unloadConfirmNeed',1);
INSERT INTO `tb_filterpress_config` VALUES (48,'2496','unloadConfirmNeed',1);
INSERT INTO `tb_filterpress_config` VALUES (49,'2496A','unloadConfirmNeed',1);
INSERT INTO `tb_filterpress_config` VALUES (50,'sys','maxUnloadParallel',1);
/*!40000 ALTER TABLE `tb_filterpress_config` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-10-13 16:17:38
