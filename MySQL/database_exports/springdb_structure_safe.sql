-- MySQL dump 10.13  Distrib 8.0.39, for macos14 (arm64)
-- Host: 127.0.0.1    Database: springdb
-- Server version	8.0.39

-- 데이터베이스 생성 (없다면)
CREATE DATABASE IF NOT EXISTS `springdb`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE `springdb`;

-- 기존 테이블 삭제 (외래키 제약조건 무시)
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `tbl_reply`;
DROP TABLE IF EXISTS `tbl_board`;
DROP TABLE IF EXISTS `tbl_member`;
DROP TABLE IF EXISTS `simple_todo`;

SET FOREIGN_KEY_CHECKS = 1;

-- Table structure for table `simple_todo`
CREATE TABLE `simple_todo` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `done` tinyint(1) DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table structure for table `tbl_board`
CREATE TABLE `tbl_board` (
  `bno` int NOT NULL AUTO_INCREMENT,
  `title` varchar(500) NOT NULL,
  `content` varchar(2000) NOT NULL,
  `writer` varchar(50) NOT NULL,
  `regdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `delflag` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`bno`)
) ENGINE=InnoDB AUTO_INCREMENT=5635997 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table structure for table `tbl_member`
CREATE TABLE `tbl_member` (
  `mno` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `email` varchar(200) NOT NULL,
  `password` varchar(200) NOT NULL,
  `regdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`mno`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table structure for table `tbl_reply`
CREATE TABLE `tbl_reply` (
  `rno` int NOT NULL AUTO_INCREMENT,
  `replyText` varchar(500) NOT NULL,
  `replyer` varchar(50) NOT NULL,
  `replydate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `deflag` tinyint(1) DEFAULT '0',
  `bno` int NOT NULL,
  PRIMARY KEY (`rno`),
  KEY `fk_reply_board` (`bno`),
  KEY `idx_reply_board` (`bno` DESC,`rno`),
  CONSTRAINT `fk_reply_board` FOREIGN KEY (`bno`) REFERENCES `tbl_board` (`bno`)
) ENGINE=InnoDB AUTO_INCREMENT=603 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



