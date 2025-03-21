-- MySQL dump 10.13  Distrib 9.1.0, for macos14 (arm64)
--
-- Host: localhost    Database: ebookshop
-- ------------------------------------------------------
-- Server version	9.1.0

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

--
-- Current Database: `ebookshop`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `ebookshop` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `ebookshop`;

--
-- Table structure for table `authors`
--

DROP TABLE IF EXISTS `authors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `authors` (
  `author_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `birth_date` date DEFAULT NULL,
  `nationality` varchar(100) DEFAULT NULL,
  `bio` text,
  `profile_picture` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`author_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authors`
--

LOCK TABLES `authors` WRITE;
/*!40000 ALTER TABLE `authors` DISABLE KEYS */;
INSERT INTO `authors` VALUES (1,'J.K. Rowling','1965-07-31','British','J.K. Rowling is the British author best known for the Harry Potter series.','rowling.jpg'),(2,'J.R.R. Tolkien','1892-01-03','British','J.R.R. Tolkien was an English writer, professor, and philologist, best known for The Hobbit and The Lord of the Rings series.','tolkien.jpg'),(3,'George Orwell','1903-06-25','British','George Orwell was an English novelist, essayist, journalist, and critic, known for works like 1984 and Animal Farm.','orwell.jpg'),(4,'Jane Austen','1775-12-16','British','Jane Austen was an English novelist known for her six major novels, including Pride and Prejudice and Sense and Sensibility.','austen.jpg'),(5,'J.D. Salinger','1919-01-01','American','J.D. Salinger was an American writer best known for his novel, The Catcher in the Rye.','salinger.jpg'),(6,'Harper Lee','1926-04-28','American','Harper Lee was an American author, best known for her Pulitzer Prize-winning novel, To Kill a Mockingbird.','lee.jpg'),(7,'F. Scott Fitzgerald','1896-09-24','American','F. Scott Fitzgerald was an American novelist known for his novel The Great Gatsby, which explored themes of wealth and social class.','fitzgerald.jpg'),(8,'Herman Melville','1819-08-01','American','Herman Melville was an American novelist best known for Moby-Dick, an epic story of obsession and revenge.','melville.jpg'),(9,'asdas',NULL,NULL,NULL,NULL),(10,'abcdefgh',NULL,NULL,NULL,NULL),(11,'J.R.R. Tolkie',NULL,NULL,NULL,NULL),(12,'asdasd',NULL,NULL,NULL,NULL),(13,'mew',NULL,NULL,NULL,NULL),(14,'melo olin mfh',NULL,NULL,NULL,NULL),(15,'new',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `authors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books` (
  `book_id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `author_id` int DEFAULT NULL,
  `genre` varchar(100) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `qty` int DEFAULT NULL,
  `cover_image` varchar(255) DEFAULT NULL,
  `summary` text,
  PRIMARY KEY (`book_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books`
--

LOCK TABLES `books` WRITE;
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` VALUES (1,'Harry Potter and the Sorcerer\'s Stone',1,'Fantasy',39.99,150,'cover1.jpg','The first book in the Harry Potter series, where Harry discovers he is a wizard and attends Hogwarts School of Witchcraft and Wizardry.'),(2,'Harry Potter and the Chamber of Secrets',1,'Fantasy',42.99,140,'cover2.jpg','The second book in the Harry Potter series, where Harry returns to Hogwarts and faces a new dark mystery.'),(3,'The Hobbit',2,'Fantasy',24.99,120,'cover3.jpg','A fantasy adventure about Bilbo Baggins, who embarks on a journey with dwarves to reclaim their treasure.'),(4,'The Lord of the Rings: The Fellowship of the Ring',2,'Fantasy',29.99,100,'cover4.jpg','The first book in the Lord of the Rings trilogy, where Frodo Baggins begins his journey to destroy the One Ring.'),(5,'1984',3,'Dystopian',19.99,200,'cover5.jpg','A dystopian novel by George Orwell, depicting a totalitarian regime where surveillance and thought control are omnipresent.'),(6,'Animal Farm',3,'Political Satire',14.99,180,'cover6.jpg','A political allegory by George Orwell, where farm animals rebel against their human master only to fall into a new form of oppression.'),(7,'Pride and Prejudice',4,'Romance',18.99,250,'cover7.jpg','A classic romance novel by Jane Austen, exploring the complex relationship between Elizabeth Bennet and Mr. Darcy.'),(8,'Sense and Sensibility',4,'Romance',16.50,220,'cover8.jpg','A novel by Jane Austen, focusing on the Dashwood sisters and their contrasting approaches to love and life.'),(9,'The Catcher in the Rye',5,'Literary Fiction',22.99,170,'cover9.jpg','A coming-of-age story by J.D. Salinger, following the disillusioned teenager Holden Caulfield through his rebellious journey in New York City.'),(10,'To Kill a Mockingbird',6,'Historical Fiction',21.50,300,'cover10.jpg','Harper Lee\'s Pulitzer Prize-winning novel, exploring themes of racial injustice and moral growth through the eyes of a young girl in the American South.'),(11,'The Great Gatsby',7,'Fiction',23.00,150,'cover11.jpg','F. Scott Fitzgerald\'s classic novel about the American Dream and the tragic life of Jay Gatsby during the Roaring Twenties.'),(12,'Moby-Dick',8,'Adventure',28.99,130,'cover12.jpg','Herman Melville\'s novel about the obsessive quest of Captain Ahab to hunt down the elusive white whale, Moby Dick.'),(15,'asdasd',9,NULL,1.00,1,'cover2.jpg',NULL),(16,'qqq',10,NULL,123.00,12,'cover3.jpg',NULL),(17,'bscdba',11,'fantasy',1210.00,15,'cover3.jpg','aifnqpfw'),(22,'new book (edited)',15,'fantasy',100.00,100,'cover1.jpg','newbooknewbook');
/*!40000 ALTER TABLE `books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_records`
--

DROP TABLE IF EXISTS `order_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_records` (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `book_id` int NOT NULL,
  `customer_id` int NOT NULL,
  `qty_ordered` int NOT NULL,
  `total_price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_records`
--

LOCK TABLES `order_records` WRITE;
/*!40000 ALTER TABLE `order_records` DISABLE KEYS */;
INSERT INTO `order_records` VALUES (1,4,1,1,29.99),(2,1,1,1,39.99),(3,2,1,1,42.99),(4,3,5,2,49.98),(5,3,4,1,24.99),(6,1,5,5,199.95),(7,3,5,1,24.99),(8,1,5,8,319.92),(9,3,4,1,24.99),(10,4,4,1,29.99),(11,2,4,1,42.99),(12,1,4,5,199.95);
/*!40000 ALTER TABLE `order_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('customer','seller') NOT NULL,
  `name` varchar(100) NOT NULL,
  `phone_number` varchar(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `shipping_address` text NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'janice','sellerpass','seller','Janice','12345678','janice@gmail.com','NTU Tamarind Blk 24'),(2,'klarissa','password1','customer','Klarissa','0987654','klarissa@gmail.com','NTU Tamarind Blk 23'),(4,'prof_wesley','password1','customer','Prof Wesley','0987654','prof_wesley@gmail.com','IEM Workshop'),(5,'janicelagi','password1','customer','Janice','12345678','janice@gmail.com','NTU');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-14 10:17:54
