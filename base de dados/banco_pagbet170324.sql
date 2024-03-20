-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: pagbet_db
-- ------------------------------------------------------
-- Server version	8.0.36

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
-- Table structure for table `produto`
--

DROP TABLE IF EXISTS `produto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `produto` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ativo` bit(1) NOT NULL,
  `avaliacao` bigint NOT NULL,
  `codigo` bigint NOT NULL,
  `descricao` varchar(255) DEFAULT NULL,
  `nome_produto` varchar(255) DEFAULT NULL,
  `preco` decimal(10,2) DEFAULT NULL,
  `quantidade` bigint NOT NULL,
  `qtd_avaliacoes` bigint NOT NULL,
  `imagem` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produto`
--

LOCK TABLES `produto` WRITE;
/*!40000 ALTER TABLE `produto` DISABLE KEYS */;
INSERT INTO `produto` VALUES (1,_binary '',0,3554,'Descrição pendente','Cartela de Bingo sem número',1.99,1000,0,'..\\img\\bingo_cartela2.webp'),(2,_binary '',0,4766,'Descrição pendente','Cartela de Bingo colorida',2.99,5,0,'..\\img\\bingo_cartela3.webp'),(3,_binary '',0,4058,'Descrição pendente','Cartela de Bingo simples 2',1.99,1,0,'..\\img\\cartela_bingo.png'),(4,_binary '',0,1048,'Descrição pendente','Cartela de Bingo simples 3',1.99,1,0,'..\\img\\bingo_cartela4.webp'),(5,_binary '',0,1013,'Descrição pendente','Dado BlackJack',1.99,1,0,'..\\index\\img\\imagem_indisponivel.png'),(6,_binary '',0,5731,'Descrição pendente','Baralho Comum',1.99,1,0,'..\\index\\img\\imagem_indisponivel.png'),(7,_binary '',0,8851,'Descrição pendente','Tele Sena - Abril',1.99,1,0,'..\\index\\img\\imagem_indisponivel.png'),(8,_binary '',0,8442,'Descrição pendente','Tele Sena - Fevereiro',1.99,1,0,'..\\index\\img\\imagem_indisponivel.png'),(9,_binary '',0,5227,'Descrição pendente','Carnê do Baú',1.99,1,0,'..\\index\\img\\imagem_indisponivel.png'),(10,_binary '',0,2850,'Descrição pendente','Kit Poker',1.99,1,0,'..\\index\\img\\imagem_indisponivel.png'),(11,_binary '',0,2438,'Descrição pendente','Roleta',1.99,1,0,'..\\index\\img\\imagem_indisponivel.png');
/*!40000 ALTER TABLE `produto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `nome` varchar(255) DEFAULT NULL,
  `senha` varchar(255) DEFAULT NULL,
  `ativo` bit(1) DEFAULT NULL,
  `funcao` varchar(255) DEFAULT NULL,
  `cpf` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (10,'angelo@outlook.com','Angelo','$2a$10$zNdzE08D/GFWJ76XFX.TdeipVR85CNSym.CGdJRqgjVnMNKU/lpke',_binary '\0','administrador',123456789101),(11,'George@gmail','George','$2a$10$g.NqopdrIn1LjijIl0DPkOFDkP3fgSotdEvZlHkATyBOZDZ/6QAIi',_binary '','admin',123456789),(16,'ferreira.angelo98@gmail.com','Angelo Baracho','$2a$10$XRF0PAzXYe8kWOi0OFvQ1OgcLXkEtBKY29sIZW9Smzxlmwu99wCgG',_binary '','estoquista',123456789101),(19,'','George','$2a$10$I.Txz7fPi.KoW7XgSqLmjOx8j9.k2s4Kfiwm3vcVqaOBzoGzfafHG',_binary '','admin',123456789);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-03-19 22:57:16
