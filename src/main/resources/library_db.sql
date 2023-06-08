-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: localhost    Database: library
-- ------------------------------------------------------
-- Server version	8.0.30

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- DataBase library
-- 创建数据库
CREATE DATABASE IF NOT EXISTS library ;

USE library;
--
-- Table structure for table `tb_book`
--

DROP TABLE IF EXISTS `tb_book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_book` (
  `id` int NOT NULL AUTO_INCREMENT,
  `author` varchar(12) DEFAULT NULL,
  `name` varchar(36) NOT NULL,
  `description` varchar(128) DEFAULT NULL,
  `type` varchar(10) DEFAULT NULL,
  `status` int DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_book`
--

LOCK TABLES `tb_book` WRITE;
/*!40000 ALTER TABLE `tb_book` DISABLE KEYS */;
INSERT INTO `tb_book` VALUES (3,'天蚕土豆','斗破苍穹','斗破苍穹666','玄幻',0),(4,'辰东','完美世界','完美世界','玄幻',0),(5,'唐家三少','神印王座','神印王座','玄幻',0),(6,'天蚕土豆','大主宰','大主宰','玄幻',0),(7,'天蚕土豆','武动乾坤','武动乾坤','玄幻',0),(8,'辰东','遮天','遮天','玄幻',0),(9,'我吃西红柿','盘龙','盘龙','玄幻',0),(10,'我吃西红柿','星辰变','星辰变','玄幻',0),(11,'墨香铜臭','魔道祖师','魔道祖师','仙侠',0),(12,'橘公司','约会大作战','约会大作战','后宫',0),(13,'佐伯さん','邻家天使','邻家天使','爱情',1),(14,'伏濑','史莱姆','史莱姆','冒险',1),(15,'长月达平','Re0','蕾姆','冒险',1),(16,'月夜涙','回复术士的重启人生','回复术士无法一个人战斗。少年凯亚尔正因为是如此无力的存在，\n才会一直遭到勇者与魔术师利用、剥夺。\n然而有一天，他察觉到将回复（Heal）锻炼到极致后还有更进一步的能力，\n决定借此重建整个世界，回到四年前从头来过。','冒险',1),(17,'丸山くがね','不死者之王','一款席卷游戏界的网路游戏「YGGDRASIL」，有一天突然毫无预警地停止一切服务原本应该是如此。但是不知为何它却成了一款即使过了结束时间，玩家角色依然不会登出的游戏。NPC开始拥有自己的思想。','冒险',1),(18,'白米良','平凡职业造就世界最强','『被霸凌的孩子』南云始，与同班同学一起被召唤至异世界。虽然同学们接连显现战斗取向的特殊能力，但始却只拥有炼成师这种平凡的能力。而且在异世界仍为最弱的他，竟被某位同学恶意推落了迷宫深谷──！？','冒险',1);
/*!40000 ALTER TABLE `tb_book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user`
--

DROP TABLE IF EXISTS `tb_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `password` varchar(64) NOT NULL,
  `permission` int DEFAULT '0',
  `email` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=281640971 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user`
--

LOCK TABLES `tb_user` WRITE;
/*!40000 ALTER TABLE `tb_user` DISABLE KEYS */;
INSERT INTO `tb_user` VALUES (1,'test','123456',0,'3288316494@qq.com'),(2,'admin','password',1,'13450144906@163.com');
/*!40000 ALTER TABLE `tb_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user_collect`
--

DROP TABLE IF EXISTS `tb_user_collect`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_user_collect` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `book_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `tb_user_collect_pk` (`user_id`,`book_id`),
  KEY `book_id` (`book_id`),
  CONSTRAINT `tb_user_collect_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`),
  CONSTRAINT `tb_user_collect_ibfk_2` FOREIGN KEY (`book_id`) REFERENCES `tb_book` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user_collect`
--

LOCK TABLES `tb_user_collect` WRITE;
/*!40000 ALTER TABLE `tb_user_collect` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_user_collect` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-06-08 13:39:42
