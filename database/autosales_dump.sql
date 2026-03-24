-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: autosales
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `audit_log`
--

DROP TABLE IF EXISTS `audit_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audit_log` (
  `log_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `action` varchar(50) NOT NULL,
  `table_name` varchar(50) NOT NULL,
  `record_id` int NOT NULL,
  `old_values` json DEFAULT NULL,
  `new_values` json DEFAULT NULL,
  `ip_address` varchar(45) DEFAULT NULL,
  `timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `audit_log_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_log`
--

LOCK TABLES `audit_log` WRITE;
/*!40000 ALTER TABLE `audit_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `audit_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `brands`
--

DROP TABLE IF EXISTS `brands`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `brands` (
  `brand_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `country` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`brand_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `brands`
--

LOCK TABLES `brands` WRITE;
/*!40000 ALTER TABLE `brands` DISABLE KEYS */;
INSERT INTO `brands` VALUES (1,'BMW','Германия'),(2,'LADA','Россия'),(3,'Toyota','Япония'),(4,'Lexus','Япония');
/*!40000 ALTER TABLE `brands` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `car_images`
--

DROP TABLE IF EXISTS `car_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `car_images` (
  `id` int NOT NULL AUTO_INCREMENT,
  `car_id` int NOT NULL,
  `image_path` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `car_id` (`car_id`),
  CONSTRAINT `car_images_ibfk_1` FOREIGN KEY (`car_id`) REFERENCES `cars` (`car_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=231 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `car_images`
--

LOCK TABLES `car_images` WRITE;
/*!40000 ALTER TABLE `car_images` DISABLE KEYS */;
INSERT INTO `car_images` VALUES (103,30,'/images/4ec27cae-3968-417c-9b2f-edad665f3920.jpg','2026-03-16 19:01:54'),(104,30,'/images/65caf231-f24a-4bcf-98e6-a73fc8f31846.jpg','2026-03-16 19:01:54'),(105,30,'/images/a185724a-6454-49ec-8042-55a9133d5164.jpg','2026-03-16 19:01:54'),(106,30,'/images/59e5ba08-a89d-4f8f-ae25-07c3abc1c20b.jpg','2026-03-16 19:01:54'),(107,30,'/images/9132e44a-a43d-4d42-92ea-f9d8e48595bb.jpg','2026-03-16 19:01:54'),(108,30,'/images/e535457a-2235-4551-999a-44c275d6bdc3.jpg','2026-03-16 19:01:54'),(109,30,'/images/6654d63c-87bc-4e3d-b2e9-414d774b13b5.jpg','2026-03-16 19:01:54'),(110,30,'/images/c4bc39e7-209a-47ca-8aa2-2648f1ce3f25.jpg','2026-03-16 19:01:54'),(111,30,'/images/f29a406a-e768-4321-8ee2-bb081e52ff59.jpg','2026-03-16 19:01:54'),(112,30,'/images/2684637e-223f-47d4-a156-219f415a54fc.jpg','2026-03-16 19:01:54'),(113,75,'/images/8fe9b8f0-c305-482d-a838-5a18eb429f35.pYOPJp7zwMWJhW1paW8ebapbbmUBCSLpynpM8NeJBTM','2026-03-16 19:55:11'),(114,76,'/images/b32cac94-763c-415e-bce6-785351c29fa0.9ZxES6QDvZWjQGGXwGNHWYEj5595EZZsj_tUI6RSzlc','2026-03-17 07:59:04'),(115,76,'/images/005f3be1-905d-48f2-bbc0-8cd45f65db78.EJ-k6gYHMIg7HExNcofq2oqLXcsQ9OI-YVT1PZFIxmY','2026-03-17 07:59:04'),(116,78,'/images/e863d1dc-1dce-4d59-98c0-2d00862d428f.html','2026-03-17 08:06:51'),(117,78,'/images/12816ccd-4f64-4fe5-a20e-2aea96f8db31.qE6fC2AbMfG0dXauoFJRc8hVMik1OZWefERGpzDyp60','2026-03-17 08:06:51'),(118,78,'/images/b0b88724-69f3-4c1e-aab3-fd660ab1db4a.webp','2026-03-17 08:06:51'),(119,78,'/images/7c7f4c1e-4c21-4a60-9315-39f679dda897.JRxfv82a6Jpsd85VkfZ09EcgtDByGv1wWpZc5cjjhUY','2026-03-17 08:06:51'),(120,78,'/images/623c637f-9d6d-40f7-ace5-87b4001a2b8e.gWJdcS1QlOs4vkKogKuVV838XqNBfpOcNgK0eCCNCDs','2026-03-17 08:06:51'),(121,77,'/images/eec45ecd-e6c7-4fa9-ab17-4508c816fca6.Mj8DHGzt_G8NHjmN515l6Tw_Q4imVOBO0zUwb5c__78','2026-03-17 08:08:38'),(122,77,'/images/01d9c02e-348f-4698-abc7-a37da8d35137.jpg','2026-03-17 08:08:38'),(123,77,'/images/13ea8f22-8424-4478-87ff-461eefae4be2.WVwL6hfNyDFoAJzZAjdK6seVBZ2XHB-8Gt1Q-nSk3Ro','2026-03-17 08:08:38'),(124,77,'/images/9d0bfeba-b1a5-42a0-8b58-bf2e1ed808d6.jpg','2026-03-17 08:08:38'),(125,79,'/images/13091875-3c7b-4840-a2eb-8e20afbde6e1.Qj0bssvWg_pfyn7bnDLfxTdSP9jICoL6P0gMHm0r9dg','2026-03-17 08:12:16'),(126,79,'/images/196d0254-2ea2-4cc8-bfaf-945b97c89239.rpdIyEtP41WwzqNAzP0eepvtgh1IiaLm2wPjzm2ezeo','2026-03-17 08:12:16'),(127,79,'/images/8ca3ef0f-c8f1-49a4-87a0-a800b4414e88.cvb3XrIlbcJZxI0zzpJGz6F4jpsIBSAVOPTQXqnAzXQ','2026-03-17 08:12:16'),(128,79,'/images/d18351fc-a653-475d-9a29-a9e102e90d51.webp','2026-03-17 08:12:16'),(129,79,'/images/5e38d10a-510c-4731-80c9-d33cb517e280.jpg','2026-03-17 08:12:16'),(130,80,'/images/ebbf85ee-7a4d-442b-9803-5db52d659a50.k-JJPtFldIO8-QzmyBit4RaQhYSjahhnHRpZQw_3EJ4','2026-03-17 08:18:36'),(131,80,'/images/d6f22c41-c966-47c1-8ae0-496388deaf9a.UDZMcSbpNZHsKHqK27CkOkKn0mjBzhTY4V-8Rovc-qY','2026-03-17 08:18:36'),(132,80,'/images/f65cc83f-7c1f-449f-b469-eb8314e35a47.wiotXfhSmPHfdg-26D_lw5yNXZOG6XKQ-RvSPiNAwI8','2026-03-17 08:18:36'),(133,80,'/images/ce376699-d56c-4ba9-aead-ea73b80ea848.OJ7WcB1z__ZsBht5qAHYlwxcmBgweeHTq36cBfAyV18','2026-03-17 08:18:36'),(134,81,'/images/74903e5d-6f6e-469f-b99c-c016ce0535c3.webp','2026-03-17 08:22:39'),(135,81,'/images/5e3e72ae-78c6-49f1-b385-92184a83eca0.webp','2026-03-17 08:22:39'),(136,81,'/images/cf8db626-8417-43e6-b84f-b01daef1d9b3.webp','2026-03-17 08:22:39'),(137,81,'/images/f04eaf6b-64f5-4fef-865c-b2a025155766.webp','2026-03-17 08:22:39'),(138,82,'/images/17614775-5b3a-4371-95aa-1dbb8e5bd35a.YsZfiUp62I8NiaSKt_LjCXEqA9XDVPGUz1noDgzJgM4','2026-03-17 08:26:52'),(139,82,'/images/565ed18c-554b-4872-a82f-1a95d38ec539.imaresqg6SOcXglYOaomugl1sBBqv2s3glpT5WRK1AI','2026-03-17 08:26:52'),(140,82,'/images/372a4d3a-603d-487f-a2b6-57df0ecc2017.98xMrTpjuBhWB02Jgpb9t5RjYgGmpwKLwaZO1XTXUIA','2026-03-17 08:26:52'),(141,82,'/images/cc1e204f-6165-4ea3-abb6-ed4657e51865.V9CoZS7a8RkP9BZFc8ZPmM4cRPkPIgu9_WmYi19i5dM','2026-03-17 08:26:52'),(142,82,'/images/ec9b1b9b-76e9-4fad-a5fb-d742f70820bf.dAjwNgrYrBDP9_haqvMGoqRTvrjkD5hqtWjy2o2SbJE','2026-03-17 08:26:52'),(143,84,'/images/222fb8d4-6963-487a-a502-9753fe69f782.RNoA16FhjO5wL6JsTXmIdtOyKp1RFcjLnILqTOSQ_T8','2026-03-17 08:33:05'),(144,84,'/images/001f8dd1-9ba5-4ae5-af8a-0d8491f7ddf1.O8mKcgmUs_l38UoIMqvkiUQXGA_7eWpxXS0qSryoQes','2026-03-17 08:33:05'),(145,84,'/images/defb7784-bd99-4719-bb20-3987476f0875.webp','2026-03-17 08:33:05'),(146,84,'/images/590fe0a5-1c17-43c4-a169-221817b91648.webp','2026-03-17 08:33:05'),(147,84,'/images/b2a4c5ec-846e-4469-85f1-09c9620ade8a.webp','2026-03-17 08:33:05'),(148,84,'/images/2203d5d5-2946-49ab-94a8-83a770a3d99c.webp','2026-03-17 08:33:05'),(149,85,'/images/c109c3e7-7e25-4c56-bc2a-279f344aeeb0.webp','2026-03-17 08:36:40'),(150,85,'/images/135aa36a-5466-4b8e-b457-aedb842af682.95r0tYRFGfdf_7lpYrDltSa6A0VJO3B_gi_VMQ0Qzto','2026-03-17 08:36:40'),(151,85,'/images/67b4f7fc-e89f-4946-9760-3e7bcdf3e37b.WHa6-QyXW0cKWTeSQ7wElHYOAUXGSm2HOvMBOWVvyfE','2026-03-17 08:36:40'),(152,85,'/images/3e32b0dc-9e4b-4bda-9596-af2f521d2592.webp','2026-03-17 08:36:40'),(153,86,'/images/4ee0921d-b9bc-46c6-982e-cf728cd1b0a0.k0djL3oLLD7AzE-YFhtj-yRBOXNqtgTVsnkNcxZMmb8','2026-03-17 08:39:36'),(154,86,'/images/bbb666d8-2460-4282-ab95-2cfbf8669944.webp','2026-03-17 08:39:36'),(155,86,'/images/fa5507b6-82a2-4cce-a2da-1e3a058e5ea2.Qlm8sEawtcvbW3x3rl4xKMG7g4cixsc3cg4pOgdebnk','2026-03-17 08:39:36'),(156,86,'/images/e1b17c0e-767d-498d-a995-9b12da0aa56d.webp','2026-03-17 08:39:36'),(157,87,'/images/28050aa4-8b57-47ec-a304-0c503ef2d9f6.pMCi0ul8QLtK6htDQCcF_K0Q_EQnl_--A-E9TOT6FT4','2026-03-17 08:42:33'),(158,87,'/images/cb62145e-01a2-4453-b7b8-0da59ffd9d7b.webp','2026-03-17 08:42:33'),(159,87,'/images/4db57abe-6ce5-4d12-a76d-b632c019cca6.F6xXPTEY77V2u_IMH8ORYvtHpv9QKxeL92_QhfsUqHY','2026-03-17 08:42:33'),(160,87,'/images/2fec8263-0827-45df-abdf-23018a220ea4.webp','2026-03-17 08:42:33'),(161,88,'/images/2ce4d58d-e854-4100-8cfe-d7850a3732d7.VYANoYvonSSkuKRX-F3W4lf9Pj2Vblzu9yxp4QiQ6wc','2026-03-17 08:46:50'),(162,88,'/images/4b479388-0fa5-433b-8b2c-4b00b41cc903._VeK35lYc2xF1riRvIEYndmYVgjgWPVO70bLeCWqwC8','2026-03-17 08:46:50'),(163,89,'/images/35159852-618d-46a4-a89e-fca527352ee6.3GTL1bFhLfr7-dCx0-DDUrAI5Q9rJV2Kw5e-tP5_Was','2026-03-17 08:50:25'),(164,89,'/images/bd9ea7bb-4055-4528-b6df-3e08d9c047cd.webp','2026-03-17 08:50:25'),(165,89,'/images/775f5440-4439-4ac4-a634-353e76fa6cf3.S4J8l4Cs35HramC0r-X_tpXNKm1OKIxPafqHrrtiuSk','2026-03-17 08:50:25'),(166,89,'/images/d6cb44dc-38f7-4ffa-a607-45c7398ea63e.QTUpUz2ucIkNTbWcR0cLOxlQrGGhTzGt5Sq7AcfAmIg','2026-03-17 08:50:25'),(167,90,'/images/9f6d1781-4c98-4d25-881a-dcc1f5f6a2bb.tqJCpklZKUlQDPIEoTFcStPUHpqMHQxuUERn8mDGTcY','2026-03-17 08:52:54'),(168,90,'/images/59d5e6f6-cbfb-4dfb-9e75-426a326e4778.xPk8X3Evfchgn5G7cGFUcgocj4UxL5FrJA4yBw0HuIY','2026-03-17 08:52:54'),(169,90,'/images/a2e428e0-6227-4342-a9c0-ded2c7d989df.webp','2026-03-17 08:52:54'),(170,92,'/images/ba2d4b56-73f8-454e-bf42-5bc36ae32fd2.PWNGHZu4v5rYgiruyuER52dsMcgBFzpP_aZbVs7mucA','2026-03-17 08:55:47'),(171,92,'/images/c115b6c5-3f35-46b2-951f-a975fecbf659.7ZCBZYtyLB1b3PTekPcD8JwYQd2Fb2yhZwIIqAWqIbs','2026-03-17 08:55:47'),(172,93,'/images/0fc941ce-eae7-4b22-b97a-d2142a71fddd.cSXD7uhj9a1Im5LSnYkrqJrbQzkqXclajQeHrnX2XcU','2026-03-17 08:58:16'),(173,93,'/images/5fa10077-7f43-412b-b4a7-237a61d72544.O109L9va4r73WuRZ35cjQ_vePqA44-2ZKZn_7nYGIh8','2026-03-17 08:58:16'),(174,93,'/images/d9e88267-7209-4b08-8d3f-ea6affa1bd30.4O6Gt1qc1nEBHpTNUd1WEfDe7LJfURE-WAltpC0ZsXk','2026-03-17 08:58:16'),(175,93,'/images/c16f0fc7-51e9-4ef0-873e-a486835e4325.webp','2026-03-17 08:58:16'),(176,93,'/images/d7643ae0-a1e8-46be-aa58-c2b0081e50ac.5wx3_YBxyzxzBoOLHFUBvEHFw2oXatOpw2Q-xe-eom8','2026-03-17 08:58:16'),(177,94,'/images/a2189f66-41f7-4630-8bc5-a89688aa2688.webp','2026-03-17 09:01:15'),(178,94,'/images/842bf2f5-bd93-4441-a5e8-40f03d2a418b.webp','2026-03-17 09:01:15'),(179,94,'/images/75689568-109f-471e-95eb-55613037e6fc.webp','2026-03-17 09:01:15'),(180,94,'/images/a1dbbb87-82ae-45ae-b796-7f1fe4c9e1a0.webp','2026-03-17 09:01:15'),(181,94,'/images/8b3c0b3b-ccb3-4958-b8a1-4bf89e586b05.ir1j40licWIxeBqolFJfx1tADq-i2vsMpi3nPuQoixI','2026-03-17 09:01:15'),(182,95,'/images/e85129f5-694d-4755-a3a3-4e1fb87fec68.webp','2026-03-17 09:04:43'),(183,95,'/images/468bd580-3704-45b4-8765-ef49a00d8afc.--jzNr6akT05UFr6LLT-hwsbYNV92L8jvPtC2cQkhto','2026-03-17 09:04:43'),(184,95,'/images/7619fc0a-9e75-44be-a29e-9b46c8f49206.asKyfYxdlhgOP4WwsrsqMaMY5CtpOwMZDF-M4zjXW9s','2026-03-17 09:04:43'),(185,95,'/images/aa755f17-526f-44aa-8a20-c105d410ba30.cv2SwkWr--DLMdHQt2MoAqX0GkyU8VAWGumlalX5hig','2026-03-17 09:04:43'),(186,96,'/images/29021e96-af7a-48a3-a31d-cfcac1f7bab6.nupl3I3x7p-eKzX0I1hpDvcbwsGYP2nHiruZytMR0QY','2026-03-17 09:37:59'),(187,96,'/images/4a60729c-1181-4916-ae48-43525b017f88.ERKlk6pOHm8tFKZV-xzFhkjB0uo2ALaUVvBAbUxQeck','2026-03-17 09:37:59'),(188,96,'/images/122c2268-1878-4035-a02c-3a4941d5ae8f.FNzRvgMLAB1N2WQbEcD7ckDed5ZXRp_55GpfUAkIKWA','2026-03-17 09:37:59'),(189,96,'/images/0db6128c-f31d-4e5a-a04f-85a119353147.webp','2026-03-17 09:37:59'),(190,96,'/images/16adda4e-4d79-4b04-8c15-af58817edc9f.j9n6avgXUaVLH5VV0UyfvZQTND-BGeRdvVpgwtUVdb4','2026-03-17 09:37:59'),(191,97,'/images/3c7342cc-6df3-4d3d-9556-ec74b88d1c16.4aAFnjCzgpPxvZ2nZ-I6mqGlfDwz-TDi7ajUYA5yEZw','2026-03-17 09:40:44'),(192,97,'/images/1560d342-1c16-425b-97a0-447a34570fc0.webp','2026-03-17 09:40:44'),(193,97,'/images/4d1521e5-deb3-409b-8e7b-13a8e569b69e.webp','2026-03-17 09:40:44'),(194,97,'/images/d5e8ad47-3fd9-4ca2-a35c-e70d7a332440.webp','2026-03-17 09:40:44'),(195,98,'/images/cec1c1f7-0c91-441b-8f3f-2aa8df56c6fb.CkGLg6T6P3ECpcc8Wk7bEEgYSuKlTA3XAvxh6u6BiWg','2026-03-17 09:43:24'),(196,98,'/images/2cddffe4-c3cc-4282-b676-99c898c74d53.webp','2026-03-17 09:43:24'),(197,98,'/images/08383d29-38c0-4167-a7a6-e849b5f1d0ab.Vg0TQGeJdwK3X9qjkTtxnFtj5v_qRkIsCXRk0sEMz-Y','2026-03-17 09:43:24'),(198,98,'/images/238835c6-dc53-4b7f-87ac-2f5568d79c41.webp','2026-03-17 09:43:24'),(199,99,'/images/92d0ce9e-840a-40b0-bc27-9cce567df6fa.webp','2026-03-17 09:46:17'),(200,99,'/images/2260ca6e-abd1-4f6e-8aed-89cee47177b7.7mddxqE-AUe0Iut9SO0_N6FqcW4pLiiqod1MLjzIuPU','2026-03-17 09:46:17'),(201,99,'/images/c9abb62a-fb1c-4030-a6a6-ddecbaff820e.webp','2026-03-17 09:46:17'),(202,99,'/images/175f73a6-e532-4dfe-856f-3a71d00d7263.webp','2026-03-17 09:46:17'),(203,99,'/images/171a4b0a-0eab-47d5-9f1c-59bbbc7b17d2.pGtj7KW_dnHPwlfAqRb4YeSeUOlHWd5hJ_k4xgAraJA','2026-03-17 09:46:17'),(204,101,'/images/fd6323ef-1dee-47b4-bd6a-0cb7cdd5319b.I8B8QI5JqwJ7n_twjZWc5AfR6Sccon3rgcRJUUluybM','2026-03-17 09:52:01'),(205,101,'/images/780f4b01-7fdf-4c57-955f-2d60d621fcca.webp','2026-03-17 09:52:01'),(206,101,'/images/7eaf0693-2cce-401d-92af-5aba1bcf2377.Z_xkcsBnB4u3vgNdJHd6EZlNWmoZqBkS96HYNRhbccg','2026-03-17 09:52:01'),(207,101,'/images/134144c9-2eab-4ebe-bea4-b88b2e38a5af.webp','2026-03-17 09:52:01'),(208,102,'/images/220882ba-9a88-4b5d-ac4e-c4547806d1c3.webp','2026-03-17 09:54:19'),(209,102,'/images/3686fc9f-f3c1-455b-9dbb-aea0c56bd2b0.webp','2026-03-17 09:54:19'),(210,107,'/images/b057cf72-593e-49ff-9b18-49fc3fe9cc2d.webp','2026-03-17 09:57:49'),(211,107,'/images/a12a4f69-94ad-41f6-ade9-add7b9863af7.webp','2026-03-17 09:57:49'),(212,107,'/images/62b4661d-36f7-487e-aac0-9d909bd0e69c.webp','2026-03-17 09:57:50'),(213,108,'/images/b3bde78a-c41b-40ab-acaf-d56a59e13075.jpg','2026-03-17 09:59:42'),(214,108,'/images/c1add7f0-9b7c-4580-aaaa-fc0cc5506b27.jpg','2026-03-17 09:59:42'),(215,108,'/images/78e62bca-0f6a-4fca-877b-0f59ac19473a.d5Utb72ZQvr-cYZJiEUtGmH6LIxDaZ28BlZtbmWyYp8','2026-03-17 09:59:42'),(216,116,'/images/211db93f-7c83-4ddb-a8fd-93064eff4bb6.webp','2026-03-17 10:04:05'),(217,116,'/images/7fe7f92c-2153-45a8-b4fd-c14d625e9294.tiglrV8dmW7_I2KTpyPcgCq7CwPWh9rPhSn_Ec6omdU','2026-03-17 10:04:05'),(218,116,'/images/71af61f7-b7d4-4f3c-8745-12cb050e5142.iX8ZKtChGABe1sygBjVmpHyFN6fF9S0R1T9B6nVkaaI','2026-03-17 10:04:05'),(219,115,'/images/f1d7c2fd-b94f-459a-ab5c-8a19fdf98be2.MHsF5vgF0rLx5JauD26ZVUKympNDO3q3d4Q_vrF_E2s','2026-03-17 10:08:08'),(220,115,'/images/35a3bdee-8b06-46bd-aa71-252536549b35.webp','2026-03-17 10:08:08'),(221,115,'/images/eac02783-da6b-46eb-a595-93bf6d9c6b5a.c8CGWC3P_46-r1MwrCtWWax2KnxwEfz318RjsT2UfEc','2026-03-17 10:08:08'),(222,117,'/images/46d45ba4-1678-4490-8340-79c43afbac93.odVIed_l4-5ww5BZEJFKLpumelTaCf3rY_4TE1-c_vc','2026-03-17 10:15:20'),(223,117,'/images/eef1cb5a-a413-4900-a53c-0627c19807e2.Q-aMt71riZgP3iTRdfQvz_hEXXsxOJE6NVfyaNWFkYs','2026-03-17 10:15:20'),(224,117,'/images/0baab775-6dea-483e-9865-0052dd95d80b.a3iHkOR7ci1EKeS6F-fQrsR5U8rIF4WvxJZFxyQrwGk','2026-03-17 10:15:20'),(227,118,'/images/55ad34de-6787-4989-8ca1-77fb0b3d159d.webp','2026-03-17 10:28:35'),(228,118,'/images/77e67d06-c041-479f-a861-b5192b45acf8.webp','2026-03-17 10:28:35'),(230,118,'/images/7f94fd21-c9b1-4955-81f5-48cd5eb0271a.ZgwEiUPw6w0oYXbUgg2-tZfpl3wWclmjr2C8CZnSmDk','2026-03-17 10:29:37');
/*!40000 ALTER TABLE `car_images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `car_specifications`
--

DROP TABLE IF EXISTS `car_specifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `car_specifications` (
  `car_id` int NOT NULL,
  `engine_layout` varchar(50) DEFAULT NULL,
  `engine_volume` decimal(3,1) DEFAULT NULL,
  `horsepower` int DEFAULT NULL,
  `color` varchar(50) DEFAULT NULL,
  `air_conditioning` tinyint(1) DEFAULT NULL,
  `year` int DEFAULT NULL,
  `mileage` int DEFAULT NULL,
  PRIMARY KEY (`car_id`),
  CONSTRAINT `car_specifications_ibfk_1` FOREIGN KEY (`car_id`) REFERENCES `cars` (`car_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `car_specifications`
--

LOCK TABLES `car_specifications` WRITE;
/*!40000 ALTER TABLE `car_specifications` DISABLE KEYS */;
INSERT INTO `car_specifications` VALUES (30,NULL,1.6,90,'булат',NULL,2022,30000),(75,NULL,2.0,178,'Белый',1,2024,5),(76,NULL,2.0,184,'Чёрный',1,2025,3),(77,NULL,2.0,184,'Серый',1,2024,5700),(78,NULL,3.0,387,'синий',1,2021,45000),(79,NULL,2.0,258,'Чёрный',1,2025,15000),(80,NULL,3.0,313,'коричневый',1,2012,152000),(81,NULL,4.4,407,'Чёрный',1,2011,220000),(82,NULL,3.0,320,'Синий металлик',1,2019,97000),(83,NULL,2.0,204,'черный',1,2025,7),(84,NULL,2.0,178,'серый',1,2025,5),(85,NULL,2.0,245,'Синий',1,2018,83000),(86,NULL,2.0,245,'Серый',1,2022,21000),(87,NULL,3.0,381,'Серый',1,2020,83000),(88,NULL,3.0,381,'Белый',1,2021,67000),(89,NULL,3.0,381,'Белый',1,2025,3),(90,NULL,2.0,197,'Черный',1,2024,13000),(92,NULL,1.5,374,'Серебристый',1,2017,56000),(93,NULL,3.0,460,'Голубой',1,2017,105000),(94,NULL,3.0,510,'Синий',1,2021,34000),(95,NULL,3.0,510,'Белый',1,2023,13000),(96,NULL,4.4,625,'Серый',1,2021,65000),(97,NULL,4.4,600,'Белый',1,2013,110000),(98,NULL,4.4,625,'Серый',1,2019,73000),(99,NULL,4.4,625,'Зеленый',1,2025,3),(101,NULL,1.6,98,'Чёрный',1,2017,120000),(102,NULL,1.6,106,'Белый',1,2025,5),(107,NULL,1.6,106,'Белый',1,2026,8),(108,NULL,1.6,106,'Белый',1,2026,5),(115,NULL,2.5,181,'Белый',1,2017,173000),(116,NULL,1.7,122,'белый',1,2025,4),(117,NULL,2.5,208,'красный',1,2010,200000),(118,NULL,4.6,381,'черный',1,2008,195000);
/*!40000 ALTER TABLE `car_specifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cars`
--

DROP TABLE IF EXISTS `cars`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cars` (
  `car_id` int NOT NULL AUTO_INCREMENT,
  `model_id` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `stock_quantity` int NOT NULL DEFAULT '0',
  `expected_date` date DEFAULT NULL,
  `description` text,
  `image` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`car_id`),
  KEY `idx_cars_model` (`model_id`),
  KEY `idx_cars_price` (`price`),
  CONSTRAINT `cars_ibfk_1` FOREIGN KEY (`model_id`) REFERENCES `models` (`model_id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cars`
--

LOCK TABLES `cars` WRITE;
/*!40000 ALTER TABLE `cars` DISABLE KEYS */;
INSERT INTO `cars` VALUES (30,123,850000.00,0,NULL,'Автомобиль в идеальном состоянии. ','/images/14186aca-82fc-4945-9fe6-321dd6d79015.jpg','2026-03-16 18:58:10','2026-03-19 15:52:37'),(75,97,1200000.00,4,NULL,'BMW 120i хэтчбек, 2.0L 178 л.с., автомат, задний привод. Расход 6.5 л, разгон 7.1 с. Комплектация Sport Line, двухзонный климат, подогрев сидений, светодиодные фары, парктроники, 17\" диски.','/images/8fe9b8f0-c305-482d-a838-5a18eb429f35.pYOPJp7zwMWJhW1paW8ebapbbmUBCSLpynpM8NeJBTM','2026-03-16 19:37:35','2026-03-16 19:55:13'),(76,98,3500000.00,1,NULL,'BMW 220i купе, 2.0L 184 л.с., автомат, задний привод. Спортивный дизайн, кожаный салон, M-спорт пакет, адаптивная подвеска, панорамная крыша, Harman Kardon.','/images/4ff99f70-204b-4baa-854c-fb5a5b4c0795.Rr5fxyU5fbT3husmmyRfj8347uBmMASQAYfhwCRLznU','2026-03-16 19:37:35','2026-03-17 07:59:07'),(77,99,4200000.00,1,NULL,'BMW 320d седан, 2.0L 184 л.с., автомат, задний привод. Business Class, цифровая панель, навигация, подогрев передних сидений, камера заднего вида, круиз-контроль.','/images/72d05029-57b5-4474-89f7-21f06ef7d0a8.webp','2026-03-16 19:37:35','2026-03-17 08:08:44'),(78,100,5100000.00,1,NULL,'BMW M440i купе, автомат, полный привод xDrive. M-пакет, лазерные фары, спортивные сиденья, панорамная крыша, аудиосистема Harman Kardon.','/images/c6819ed6-05c5-427d-a84c-72bae18131cd.4C4UzgKVYslIqFkfg4XOqHg2WGzyzqOmul1hktNvJM8','2026-03-16 19:37:35','2026-03-17 08:06:32'),(79,101,6700000.00,1,NULL,'BMW 530i седан, 2.0L 258 л.с., автомат, задний привод. Executive пакет, кожа Nappa, проекционный дисплей, адаптивный круиз, вентиляция сидений, подогрев всех сидений, мягкое закрытие дверей.','/images/ceb5fe89-e462-4152-b7a5-89bb127e28e0.jpg','2026-03-16 19:37:35','2026-03-17 08:12:22'),(80,102,2790000.00,1,NULL,'BMW 6 , 3.0L 313 л.с., автомат, полный привод. Воздушная подвеска, панорамная крыша, отделка деревом, массаж сидений, аудиосистема Bowers & Wilkins.','/images/134cb90a-7d4a-4f61-947e-fbeee71dc1c1.webp','2026-03-16 19:37:35','2026-03-17 08:18:38'),(81,103,1250000.00,1,NULL,'BMW 750i седан, 4.4L 407 л.с., автомат, полный привод. Представительский класс, задние сиденья с массажем, холодильник, отделка кожей Merino, Sky Lounge панорама, 20\" диски.','/images/8a3cc3af-9756-4cac-885b-45cf6553b6c2.webp','2026-03-16 19:37:35','2026-03-17 08:22:44'),(82,104,6950000.00,1,NULL,'BMW 840d 3.0 xDrive Steptronic (320 л.с.), автомат, полный привод xDrive. M-пакет, карбон-керамические тормоза, спортивная выхлопная система, вентиляция сидений, подогрев руля, лазерные фары.','/images/97127c8f-bc50-4fa6-a0cb-f97d2cf33169.webp','2026-03-16 19:37:35','2026-03-17 08:26:55'),(83,105,5600000.00,0,'2026-05-05','BMW X1 xDrive25i, 2.0L 204 л.с., автомат, полный привод. Компактный кроссовер, панорамная крыша, кожаная обивка, электропривод двери багажника, навигация, подогрев сидений.','/images/5dbdd5b0-f8c1-4014-8d29-c83b9079ea12.kN8iz5CA37uppymqoeJDilCOMSkylYAuif5bYW_AyRs','2026-03-16 19:37:35','2026-03-17 08:29:50'),(84,106,6300000.00,1,NULL,'BMW X2 sDrive18i, 2.0L 178 л.с., автомат, передний привод. Спортивный кузов, M-спорт руль, двухзонный климат, подогрев сидений, 18\" диски.','/images/deb14667-12f2-4adf-8955-261a44950b6b.V3M_mbCYDCIhJ5CY_g7EsNWjddY1fyVeLXZoNYid4v4','2026-03-16 19:37:35','2026-03-17 08:33:07'),(85,107,4900000.00,1,NULL,'BMW X3 xDrive30i, 2.0L 245 л.с., автомат, полный привод. M-спорт пакет, цифровая панель, адаптивная подвеска, панорамная крыша, Harman Kardon.','/images/5af4c98e-6e07-484b-a9a8-e73e6344e068.B7x8u79InyB3nS8DuLy5ywr1w2y32XnXMZL6OMGwLt4','2026-03-16 19:37:35','2026-03-17 08:36:42'),(86,108,5400000.00,1,NULL,'BMW X4 xDrive30i, 2.0L 245 л.с., автомат, полный привод. Спортивное купе-кроссовер, M-спорт сиденья, лазерные фары, парковка с камерами 360°.','/images/35addec7-1f43-4bc0-bf0a-82458aa223c6.webp','2026-03-16 19:37:35','2026-03-17 08:39:38'),(87,109,6000000.00,1,NULL,'BMW X5 xDrive40i, 3.0L 381 л.с., автомат, полный привод. Пневмоподвеска, кожа Merino, подогрев и вентиляция всех сидений, проекционный дисплей, аудиосистема Harman Kardon.','/images/fe0c2d85-0f47-46de-9e14-2bed51d14447.k2n7WyMd1FM14TTO8ziIFddJOD_pw2wG2A4BfHkN4uQ','2026-03-16 19:37:35','2026-03-17 08:42:36'),(88,110,9000000.00,1,NULL,'BMW X6 xDrive40i, 3.0L 381 л.с., автомат, полный привод. Купе-кроссовер, M-пакет, панорамная крыша, спортивный глушитель, 21\" диски.','/images/4a99f970-e1ec-43d7-9af3-dc5017c1654d.A1HM8bcXtLaaMBNcPO3v5bQGSd23LL_nbWHv_kebNb0','2026-03-16 19:37:35','2026-03-17 08:46:52'),(89,111,15000000.00,0,'2026-05-10','BMW X7 xDrive40i, 3.0L 381 л.с., автомат, полный привод. 7-местный салон, кожа Merino, задние развлекательные экраны, холодильник, пневмоподвеска, массаж сидений.','/images/15d8a3d0-b5d5-49ec-b688-250634e31837.Z8hK_YVd9j5pHQb-D0CHc5EA849k9lpCtjt2vpp--7w','2026-03-16 19:37:35','2026-03-17 08:50:28'),(90,112,4800000.00,1,NULL,'BMW Z4 sDrive20i, 2.0L 197 л.с., автомат, задний привод. Родстер с мягкой крышей, M-спорт пакет, спортивные сиденья, подогрев шеи, проекционный дисплей, аудиосистема Harman Kardon.','/images/236db1b0-aa99-4dc4-b283-96cffaed1672.webp','2026-03-16 19:37:35','2026-03-17 08:52:56'),(92,114,8500000.00,1,NULL,'BMW i8 гибрид, 1.5L + электромотор 374 л.с., автомат, полный привод. Двери-крылья, карбоновый монокок, разгон до 100 км/ч за 4.4 с, расход 2.5 л/100 км.','/images/2b7a75b6-499c-4ad1-aaef-f8f64e8aa407.Lpj9es7_gAwdaVR-qzTSfTksDhh8F5GyM4G6wWJq1Jk','2026-03-16 19:37:35','2026-03-17 08:55:48'),(93,115,7400000.00,1,NULL,'BMW M2 Competition, 3.0L 460 л.с., робот, задний привод. M-спорт дифференциал, адаптивная подвеска, карбон-керамические тормоза, спортивные сиденья, M-руль.','/images/a369ab46-779f-433f-ad27-39b0133222ef.jJRJHA_g4CiaqBp8HM8o0K2NcsDyzTjaZYR7OHewftU','2026-03-16 19:37:35','2026-03-17 09:02:47'),(94,116,9800000.00,1,NULL,'BMW M3 Competition, 3.0L 510 л.с., автомат, задний привод. M-пакет, карбоновый крышей, спортивная выхлопная, M-сиденья, отделка карбоном.','/images/7b83b21c-e530-4a1e-83a0-269965ed6a83.HLzuGRu3g72DwXZTRWDb9QMdyKnYThuy-Z2ggVUVHA4','2026-03-16 19:37:35','2026-03-17 09:02:37'),(95,117,10200000.00,1,NULL,'BMW M4 Competition купе, 3.0L 510 л.с., автомат, задний привод. Карбоновый кузов, керамические тормоза, M-спорт выхлоп, адаптивная подвеска.','/images/7e0cfc56-1853-4773-9f0e-e93d46f6e9ea.webp','2026-03-16 19:37:35','2026-03-17 09:04:48'),(96,118,13500000.00,1,NULL,'BMW M5 Competition, 4.4L 625 л.с., автомат, полный привод M xDrive. Спортивный седан, разгон 3.1 с, M-спорт выхлоп, карбон-керамика, отделка алькантарой.','/images/320d1cad-43e9-4090-a955-4838e537fb6b.webp','2026-03-16 19:37:35','2026-03-17 09:38:06'),(97,119,5000000.00,1,NULL,'BMW M6 Gran Coupe, 4.4L 600 л.с., автомат, задний привод. M-пакет, карбоновые элементы, спортивные сиденья, отделка кожей Merino.','/images/50c51395-9a56-4187-8a24-68d1c70358fa.webp','2026-03-16 19:37:35','2026-03-17 09:40:46'),(98,120,10850000.00,1,NULL,'BMW M8 Competition купе, 4.4L 625 л.с., автомат, полный привод. M-пакет, карбоновые детали, M-спорт выхлоп, керамические тормоза, 20\" диски.','/images/842e70e3-c44d-4f01-8226-3b9951280d03.G0v8hMyhaNCccop7a6-PP52_cTdYStrSxwD8RDqcVJ4','2026-03-16 19:37:35','2026-03-17 09:43:26'),(99,121,24000000.00,0,'2026-05-14','BMW X5M Competition, 4.4L 625 л.с., автомат, полный привод. Спортивный внедорожник, M-подвеска, карбоновые детали, M-спорт выхлоп, 21\" диски.','/images/402b33ae-d165-49c2-9c7f-e8184014162b.APM6DbB4SqDiXcBQp65mUk9dlUJHMeqjYn-LbiY-v1g','2026-03-16 19:37:35','2026-03-17 09:46:19'),(101,124,1050000.00,1,NULL,'Машина дeлалаcь для себя, денег не жaлели oт слoва cовсeм, вложений бoлee 1?бeз цeны\r\nмашины.✅\r\n• Оснoвное:\r\nТурбинa ТDО4L, выxлoп 63 мм, мoзги SP Tronic (прoшивка под турбo).✅\r\nФoрcунки Вosch 630сc (37000т.p)\r\nПoдрамник, трeугoльные рычaги, задняя незaвиcимая пoдвeска.\r\nCтойки SS20 газо-мaслo c зaнижeниeм (делaлись в Cтаврополе у спортсменов).\r\nКПП капиталена (пробег после капремонта✅\r\n~ 3-4 тыс. км, вложено 23 000 руб).\r\nДвигатель в отличном состоянии, масло не ест (на последнюю сборку мотора вышло в районе 380тыс ₽). Масло в моторе и в коробке недавно было заменено ✅Новые губки сидений, новые чехлы.✅\r\nНовые накидки ✅\r\nПланшет андроид , подключён к рулю и родной мультимедиа.✅\r\nКамера заднего вида ✅\r\nПолная шумоизоляция в 4 слоя со снятием панели и тд. Стоят двойные стёкла.✅\r\nОткидные рамки ✅\r\nСмарт ? ключ ✅\r\nСтарт ? стоп✅\r\nБез ключевой доступ ✅','/images/b7cd53e7-1806-4c0d-9138-8422f6c3dce1.0I6I-6pzOHr_0icO8A2Zr4r8ZHVP70pYs0JpIR7H3V0','2026-03-16 19:37:35','2026-03-17 09:52:04'),(102,125,1350000.00,3,NULL,'LADA Vesta седан, 1.6L 106 л.с., механика. Комплектация Comfort: кондиционер, подогрев передних сидений, электропривод зеркал, подогрев зеркал, аудиосистема с 7\" экраном.','/images/2f57e274-44e0-4d83-b81b-4d5ced13630c.CHGuJn9Y4n0ZWXtmh9y-ocUMhlLLJVowHHN2cA9Cxjg','2026-03-16 19:37:35','2026-03-17 09:54:21'),(107,130,1600000.00,2,NULL,'LADA Largus универсал, 1.6L 106 л.с., механика. 7-местный салон, кондиционер, подогрев передних сидений, электропривод зеркал, аудиосистема.','/images/a3d46e61-2533-4540-9891-2b7df933e963.webp','2026-03-16 19:37:35','2026-03-17 09:57:51'),(108,131,1800000.00,3,NULL,'LADA Largus Фургон, 1.6L 106 л.с., механика. Грузовой отсек 2540 л, грузоподъемность 750 кг, две сдвижные двери, подогрев сидений.','/images/cb71c384-4598-47a0-9570-95ebc3210832.3LgfQNtF_rdZYWZfVmB3XbRoZtewi4M3SWxmh3T2Dt0','2026-03-16 19:37:35','2026-03-17 09:59:44'),(115,138,1900000.00,1,NULL,'Toyota Camry Стандарт, 2.5L 181 л.с., передний привод. Комплектация: двухзонный климат, подогрев передних сидений, круиз-контроль, 7\" экран, камера заднего вида, 16\" диски.','/images/fac49764-c5b1-49de-a0ec-e92b9cb3429d.webp','2026-03-16 19:37:35','2026-03-17 10:09:01'),(116,132,1700000.00,1,NULL,'Lada Niva Sport — это модернизированная версия внедорожника Lada Niva Legend, доработанная подразделением Lada Sport. Она оснащается 16-клапанным двигателем 1.6 л (122 л.с.), задними дисковыми тормозами, усиленной подвеской, расширителями арок и 17-дюймовыми дисками. Основной упор сделан на повышение мощности, управляемости и комфорта.','/images/fd17690c-9186-43eb-9469-601827f46f0a.-QXjJz7qHv3W716yR0-zIfHYmPT1dekTIzlAZaT-7gY','2026-03-17 10:03:07','2026-03-17 10:04:07'),(117,139,1350000.00,1,NULL,'Машина в отличном состоянии\r\nВложений не требует\r\nДоедет куда угодно','/images/2ec61444-84e8-4582-b182-73e6f9850c2f.uwJL9kb7EloSV_wITyrd1Oz3G6SRO1rPzaoqR4KIMbA','2026-03-17 10:14:36','2026-03-17 10:15:24'),(118,140,1500000.00,0,NULL,'Лучший в своeм клаcсе! Koмфоpтный, надeжный и богaтo ocнaщенный автомoбиль.\r\n\r\nЮридически чистый aвто с пoнятнoй иcтopиeй и пpозpачной докумeнтaцией.\r\n\r\nTеxничeскиx нapеканий нет. Bнeшний вид отличный. Сдeлана пoлиpовкa, нaнесeна кepамика. Пo кузову ecть втoричные окpaсы. Приличная Автотека (редкость для данной позиции) одно желтое ДТП, расчет на 90₽.\r\n\r\nБогатая комплектация: салон кожа; потолок алькантара; эл. привод руля; память и вентиляция всех кресел; массаж задних кресел; потолочный монитор; эл. шторки; раздельный климат; люк; эл. привод багажника; двойные стекла; доводчики дверей, акустика МаrkLеvinsоn и т.д.','/images/39349247-80c6-45cd-a7c2-7df4fe3ed818.gzZ-V1LsZyxziDK4lmUynmHX1loEW52bqbT8AXHFecA','2026-03-17 10:24:50','2026-03-22 08:40:17');
/*!40000 ALTER TABLE `cars` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `favorites`
--

DROP TABLE IF EXISTS `favorites`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favorites` (
  `user_id` int NOT NULL,
  `car_id` int NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`,`car_id`),
  KEY `car_id` (`car_id`),
  KEY `idx_favorites_user` (`user_id`),
  CONSTRAINT `favorites_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `favorites_ibfk_2` FOREIGN KEY (`car_id`) REFERENCES `cars` (`car_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `favorites`
--

LOCK TABLES `favorites` WRITE;
/*!40000 ALTER TABLE `favorites` DISABLE KEYS */;
INSERT INTO `favorites` VALUES (7,117,'2026-03-21 15:56:12'),(7,118,'2026-03-22 08:38:33'),(8,101,'2026-03-23 21:13:43'),(8,117,'2026-03-18 18:22:17');
/*!40000 ALTER TABLE `favorites` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `models`
--

DROP TABLE IF EXISTS `models`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `models` (
  `model_id` int NOT NULL AUTO_INCREMENT,
  `brand_id` int NOT NULL,
  `name` varchar(100) NOT NULL,
  `steering_type` varchar(50) DEFAULT NULL,
  `transmission` varchar(50) DEFAULT NULL,
  `engine_type` varchar(50) DEFAULT NULL,
  `steering_wheel_side` varchar(10) DEFAULT NULL,
  `body_type` varchar(50) DEFAULT NULL,
  `doors_count` int DEFAULT NULL,
  `seats_count` int DEFAULT NULL,
  PRIMARY KEY (`model_id`),
  UNIQUE KEY `unique_brand_model` (`brand_id`,`name`),
  CONSTRAINT `models_ibfk_1` FOREIGN KEY (`brand_id`) REFERENCES `brands` (`brand_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=141 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `models`
--

LOCK TABLES `models` WRITE;
/*!40000 ALTER TABLE `models` DISABLE KEYS */;
INSERT INTO `models` VALUES (97,1,'1 Series','electric','automatic','petrol','left','хэтчбек',5,5),(98,1,'2 Series','electric','automatic','petrol','left','купе',2,4),(99,1,'3 Series','electric','automatic','petrol','left','седан',4,5),(100,1,'4 Series','electric','automatic','petrol','left','купе',2,4),(101,1,'5 Series','electric','automatic','petrol','left','седан',4,5),(102,1,'6 Series','electric','automatic','petrol','left','купе',2,4),(103,1,'7 Series','electric','automatic','petrol','left','седан',4,5),(104,1,'8 Series','electric','automatic','petrol','left','купе',2,4),(105,1,'X1','electric','automatic','petrol','left','кроссовер',5,5),(106,1,'X2','electric','automatic','petrol','left','кроссовер',5,5),(107,1,'X3','electric','automatic','petrol','left','кроссовер',5,5),(108,1,'X4','electric','automatic','petrol','left','кроссовер',5,5),(109,1,'X5','electric','automatic','petrol','left','кроссовер',5,5),(110,1,'X6','electric','automatic','petrol','left','кроссовер',5,5),(111,1,'X7','electric','automatic','petrol','left','кроссовер',5,7),(112,1,'Z4','electric','automatic','petrol','left','кабриолет',2,2),(113,1,'i3','electric','automatic','electric','left','хэтчбек',4,4),(114,1,'i8','electric','automatic','hybrid','left','купе',2,4),(115,1,'M2','electric','automatic','petrol','left','купе',2,4),(116,1,'M3','electric','automatic','petrol','left','седан',4,5),(117,1,'M4','electric','automatic','petrol','left','купе',2,4),(118,1,'M5','electric','automatic','petrol','left','седан',4,5),(119,1,'M6','electric','automatic','petrol','left','купе',2,4),(120,1,'M8','electric','automatic','petrol','left','купе',2,4),(121,1,'X5M','electric','automatic','petrol','left','кроссовер',5,5),(122,1,'X6M','electric','automatic','petrol','left','кроссовер',5,5),(123,2,'Granta','electric','manual','petrol','left','седан',4,5),(124,2,'Priora','electric','manual','petrol','left','седан',4,5),(125,2,'Vesta','electric','manual','petrol','left','седан',4,5),(126,2,'Vesta SW','electric','manual','petrol','left','универсал',5,5),(127,2,'Vesta SW Cross','electric','manual','petrol','left','универсал',5,5),(128,2,'XRAY','electric','manual','petrol','left','кроссовер',5,5),(129,2,'XRAY Cross','electric','manual','petrol','left','кроссовер',5,5),(130,2,'Largus','electric','manual','petrol','left','универсал',5,5),(131,2,'Largus Фургон','electric','manual','petrol','left','фургон',5,2),(132,2,'Niva Legend 3-дв.','hydraulic','manual','petrol','left','внедорожник',3,5),(133,2,'Niva Legend 5-дв.','hydraulic','manual','petrol','left','внедорожник',5,5),(134,2,'Niva Travel','electric','manual','petrol','left','внедорожник',5,5),(135,2,'Kalina','hydraulic','manual','petrol','left','хэтчбек',5,5),(136,2,'Kalina Универсал','hydraulic','manual','petrol','left','универсал',5,5),(137,2,'Kalina Cross','hydraulic','manual','petrol','left','универсал',5,5),(138,3,'Camry','hydraulic','automatic','petrol','left','седан',5,5),(139,4,'Is250','hydraulic','automatic','petrol','left','седан',5,5),(140,4,'LS460','hydraulic','automatic','petrol','left','седан',5,5);
/*!40000 ALTER TABLE `models` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (2,'ROLE_ADMIN'),(1,'ROLE_CUSTOMER');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sale_details`
--

DROP TABLE IF EXISTS `sale_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sale_details` (
  `detail_id` int NOT NULL AUTO_INCREMENT,
  `sale_id` int NOT NULL,
  `car_id` int NOT NULL,
  `quantity` int NOT NULL DEFAULT '1',
  `price_at_sale` decimal(10,2) NOT NULL,
  PRIMARY KEY (`detail_id`),
  KEY `sale_id` (`sale_id`),
  KEY `idx_sale_details_car` (`car_id`),
  CONSTRAINT `sale_details_ibfk_1` FOREIGN KEY (`sale_id`) REFERENCES `sales` (`sale_id`) ON DELETE CASCADE,
  CONSTRAINT `sale_details_ibfk_2` FOREIGN KEY (`car_id`) REFERENCES `cars` (`car_id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sale_details`
--

LOCK TABLES `sale_details` WRITE;
/*!40000 ALTER TABLE `sale_details` DISABLE KEYS */;
INSERT INTO `sale_details` VALUES (3,3,30,1,850000.00),(4,4,118,1,1500000.00);
/*!40000 ALTER TABLE `sale_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales`
--

DROP TABLE IF EXISTS `sales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales` (
  `sale_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `sale_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `total_amount` decimal(10,2) NOT NULL,
  `delivery_required` tinyint(1) DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`sale_id`),
  KEY `idx_sales_user` (`user_id`),
  KEY `idx_sales_date` (`sale_date`),
  CONSTRAINT `sales_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales`
--

LOCK TABLES `sales` WRITE;
/*!40000 ALTER TABLE `sales` DISABLE KEYS */;
INSERT INTO `sales` VALUES (3,7,'2026-03-19 11:52:37',850000.00,0,'2026-03-19 15:52:37'),(4,7,'2026-03-22 04:40:18',1500000.00,0,'2026-03-22 08:40:17');
/*!40000 ALTER TABLE `sales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `patronymic` varchar(50) DEFAULT NULL,
  `passport_series` varchar(255) DEFAULT NULL,
  `passport_number` varchar(255) DEFAULT NULL,
  `address` text,
  `phone` varchar(20) DEFAULT NULL,
  `role_id` int NOT NULL,
  `enabled` tinyint(1) DEFAULT '1',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `balance` decimal(10,2) DEFAULT '100000.00',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email` (`email`),
  KEY `role_id` (`role_id`),
  KEY `idx_users_email` (`email`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (7,'demavarlamov@gmail.com','$2a$10$E4GJTyi1wl1vZZoWZ3aSVufxkq9FhFmHKGX30mZyBIa8Az74Yhkna','QA','AutoSales','','1234','123456','Test address','+7 999 123 45 67',1,1,'2026-03-16 18:44:40','2026-03-22 19:01:59',7650000.00),(8,'admin@autosales.com','$2a$10$1V1g9JkBDju8alMvMhL4POa6uHK.n/USBf24l1HLbyAL11Fw2hlwS','Adminov','Admin','',NULL,NULL,NULL,NULL,2,1,'2026-03-16 18:53:39','2026-03-18 19:34:02',100000.00),(9,'edik076mvs@gmail.com','$2a$10$IMJWzbrF1P9juOD2KNWwd.TO4Z3h0VyJ4ePxYqmHer1FtnqrwQXoi','Эдуард','Иванов','Эдуардович',NULL,NULL,NULL,NULL,1,1,'2026-03-17 15:13:28','2026-03-18 19:26:30',100000.00),(10,'qa_1774100962477@mail.test','$2a$10$5WAulrIY2mqblRRIpcMzWO4V9cYi.jMOOJgbjl1g6JmR7ng4TBM86','Иван','Иванов','Иванович',NULL,NULL,NULL,NULL,1,1,'2026-03-21 13:49:24','2026-03-22 13:46:16',100000.00),(11,'qa_1774101821148@mail.test','$2a$10$0Y5cpQTW2f5sWFlw8YYcQO4at8bBNLpHAQlNwA33dejOpqfikEgIK','Иван','Иванов','Иванович',NULL,NULL,NULL,NULL,1,0,'2026-03-21 14:03:43','2026-03-21 14:03:43',100000.00),(12,'qa_1774102405266@mail.test','$2a$10$QKfhkeJyoCNTzMAeiyon0OvHFYYktnOXguQ7vCQwnoB.xAunEYyIG','Иван','Иванов','Иванович',NULL,NULL,NULL,NULL,1,0,'2026-03-21 14:13:27','2026-03-21 14:13:27',100000.00);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `verification_tokens`
--

DROP TABLE IF EXISTS `verification_tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `verification_tokens` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `token` varchar(255) NOT NULL,
  `user_id` int NOT NULL,
  `expiry_date` timestamp NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `verification_tokens_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `verification_tokens`
--

LOCK TABLES `verification_tokens` WRITE;
/*!40000 ALTER TABLE `verification_tokens` DISABLE KEYS */;
INSERT INTO `verification_tokens` VALUES (4,'794a8e57-3045-4849-8750-d73a3d441967',8,'2026-03-17 14:53:40'),(5,'37a37231-129d-4ea3-9c76-f91a8ee7e57b',9,'2026-03-18 11:13:28'),(6,'d05c7703-dee1-4bb5-a96d-861174ed5f0f',10,'2026-03-22 09:49:25'),(7,'6da47c83-bb9f-420d-acd2-8e5c3c83863c',11,'2026-03-22 10:03:43'),(8,'c83025a3-e8a6-429a-a2b2-3a9f8d96cc98',12,'2026-03-22 10:13:27');
/*!40000 ALTER TABLE `verification_tokens` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-24 15:35:34
