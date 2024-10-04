-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: doan_web
-- ------------------------------------------------------
-- Server version	5.5.5-10.4.32-MariaDB

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
-- Table structure for table `anh_san_pham`
--

DROP TABLE IF EXISTS `anh_san_pham`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `anh_san_pham` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `hinh_anh` varchar(255) DEFAULT NULL,
  `masp` int(11) DEFAULT NULL,
  `ngay_sua` date DEFAULT NULL,
  `ngay_tao` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKlk1my5j5shviqurasjc5n5k1o` (`masp`),
  CONSTRAINT `FKlk1my5j5shviqurasjc5n5k1o` FOREIGN KEY (`masp`) REFERENCES `san_pham` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `anh_san_pham`
--

/*!40000 ALTER TABLE `anh_san_pham` DISABLE KEYS */;
INSERT INTO `anh_san_pham` VALUES (2,'https://i.postimg.cc/jSPpgC2g/IPhone-14-Pro-Max.jpg',9,NULL,'2024-09-20');
/*!40000 ALTER TABLE `anh_san_pham` ENABLE KEYS */;

--
-- Table structure for table `cai_dat`
--

DROP TABLE IF EXISTS `cai_dat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cai_dat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gia_tri` varchar(255) DEFAULT NULL,
  `khoa` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cai_dat`
--

/*!40000 ALTER TABLE `cai_dat` DISABLE KEYS */;
INSERT INTO `cai_dat` VALUES (1,'Hoàng','name'),(2,'nguyenduchoang522@gmail.com','email'),(3,'0388937608','phone');
/*!40000 ALTER TABLE `cai_dat` ENABLE KEYS */;

--
-- Table structure for table `chi_tiet_don_hang`
--

DROP TABLE IF EXISTS `chi_tiet_don_hang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chi_tiet_don_hang` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `don_gia` float DEFAULT NULL,
  `masp` int(11) DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `ngay_sua` date DEFAULT NULL,
  `ngay_tao` date DEFAULT NULL,
  `so_luong` int(11) NOT NULL,
  `tong_tien` float DEFAULT NULL,
  `don_hang_id` int(11) NOT NULL,
  `ten` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKt57maavf6s28hxyar724mdr1b` (`don_hang_id`),
  KEY `FK98u87ds1aj1339w293i4er4fx` (`masp`),
  CONSTRAINT `FK98u87ds1aj1339w293i4er4fx` FOREIGN KEY (`masp`) REFERENCES `san_pham` (`id`),
  CONSTRAINT `FKt57maavf6s28hxyar724mdr1b` FOREIGN KEY (`don_hang_id`) REFERENCES `don_hang` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chi_tiet_don_hang`
--

/*!40000 ALTER TABLE `chi_tiet_don_hang` DISABLE KEYS */;
INSERT INTO `chi_tiet_don_hang` VALUES (33,5794440,13,'512GB, Vàng Cứt',NULL,'2024-09-23',1,5794440,59,'Realme 3 Pro'),(34,25400000,10,NULL,NULL,'2024-09-23',1,25400000,60,NULL),(35,11005000,15,'128GB, Xám, Pin 97%',NULL,'2024-09-23',1,11005000,61,'Xiaomi 11'),(36,11005000,15,'128GB, Xám, Pin 97%',NULL,'2024-09-24',1,11005000,63,'Xiaomi 11'),(37,19444400,10,'128GB, Xanh Đen',NULL,'2024-09-24',1,19444400,64,'SamSung S23 UTra'),(38,27600000,8,'512GB, Xám, Pin 97%',NULL,'2024-09-24',1,27600000,65,'IPhone 15 Pro Max'),(39,19444400,10,'128GB, Xanh Đen',NULL,'2024-09-24',2,38888800,66,'SamSung S23 UTra'),(40,19444400,10,'128GB, Xanh Đen',NULL,'2024-09-24',1,19444400,67,'SamSung S23 UTra'),(41,5794440,13,'512GB, Vàng Cứt','2024-10-04','2024-10-04',1,5794440,68,'Realme 3 Pro'),(44,19444400,10,'128GB, Xanh Đen',NULL,'2024-10-04',1,19444400,71,'SamSung S23 UTra');
/*!40000 ALTER TABLE `chi_tiet_don_hang` ENABLE KEYS */;

--
-- Table structure for table `don_hang`
--

DROP TABLE IF EXISTS `don_hang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `don_hang` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dien_thoai` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `ghi_chu` varchar(255) DEFAULT NULL,
  `makh` int(11) NOT NULL,
  `ngay_tao` date DEFAULT NULL,
  `ten_day_du` varchar(255) DEFAULT NULL,
  `tong_tien` float NOT NULL,
  `trang_thai` int(11) NOT NULL,
  `dia_chi` varchar(255) DEFAULT NULL,
  `quan_huyen` varchar(255) DEFAULT NULL,
  `tinh_thanh` varchar(255) DEFAULT NULL,
  `xa_phuong` varchar(255) DEFAULT NULL,
  `madh` varchar(255) DEFAULT NULL,
  `thanhtoan` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKn067siyskj21r17c860rg1eik` (`madh`),
  KEY `FKiy9wbkgc3iv3ome6new025n9o` (`makh`),
  CONSTRAINT `FKiy9wbkgc3iv3ome6new025n9o` FOREIGN KEY (`makh`) REFERENCES `khach_hang` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `don_hang`
--

/*!40000 ALTER TABLE `don_hang` DISABLE KEYS */;
INSERT INTO `don_hang` VALUES (59,'0388937608','nguyenduchoang522@gmail.com','sadfas',0,'2023-08-23','Nguyễn Đức Hoàng',5794440,0,'Ngõ 1,  (Phường Ngọc Khánh Quận Ba Đình Thành phố Hà Nội)',NULL,NULL,NULL,'DH165440','monkey'),(60,'0987654377','nguyenduchoang522@gmail.com','ádfsad',0,'2024-09-23','Nguyễn Đức Hoàng',25400000,0,'100','Quận Nam Từ Liêm','Thành phố Hà Nội','Phường Đại Mỗ','DH358810',NULL),(61,'0388937608','test123@gmail.com','sgasg',0,'2024-09-23','Nguyễn Đức Hoàng',11005000,0,'Ngõ 1,  (Phường Ngọc Khánh Quận Ba Đình Thành phố Hà Nội)',NULL,NULL,NULL,'DH572062','monkey'),(63,'0388937608','test123@gmail.com','fghdf',0,'2024-09-24','Nguyễn Đức Hoàng',11005000,0,'Ngõ 1,  (Phường Chương Dương Quận Hoàn Kiếm Thành phố Hà Nội)',NULL,NULL,NULL,'DH941153','monkey'),(64,'0388937608','test123@gmail.com','sadvasdv',0,'2024-09-24','Nguyễn Đức Hoàng',19444400,0,'Ngõ 1,  (Phường Thanh Xuân Nam Quận Thanh Xuân Thành phố Hà Nội)',NULL,NULL,NULL,'DH949911','monkey'),(65,'0388937608','test123@gmail.com','ggg',0,'2024-09-24','Nguyễn Đức Hoàng',27600000,0,'Ngõ 1,  (Phường Nhật Tân Quận Tây Hồ Thành phố Hà Nội)',NULL,NULL,NULL,'DH188551','monkey'),(66,'0388937608','test123@gmail.com','dfdf',0,'2024-09-24','Nguyễn Đức Hoàng',38888800,0,'Ngõ 1,  (Phường Thạch Bàn Quận Long Biên Thành phố Hà Nội)',NULL,NULL,NULL,'DH524564','monkey'),(67,'0388937608','hoang@gmail.com','dfgsd',0,'2024-10-24','Nguyễn Đức Hoàng',19444400,1,'Ngõ 1,  (Phường Cửa Nam Quận Hoàn Kiếm Thành phố Hà Nội)',NULL,NULL,NULL,'DH886004','monkey'),(68,'0388937608','hoang@gmail.com','dfgddfgdf',0,'2024-10-04','Nguyễn Đức Hoàng',5794440,1,'Ngõ 1,  (Phường Kim Mã Quận Ba Đình Thành phố Hà Nội)',NULL,NULL,NULL,'DH224270','banking'),(71,'0974653212','hoang999@gmail.com','',12,'2024-10-04','Nguyễn Đức Hoàng',19444400,1,'Ngõ 1,  (Xã Thái Học Huyện Bảo Lâm Tỉnh Cao Bằng)',NULL,NULL,NULL,'DH221392','monkey');
/*!40000 ALTER TABLE `don_hang` ENABLE KEYS */;

--
-- Table structure for table `khach_hang`
--

DROP TABLE IF EXISTS `khach_hang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `khach_hang` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `anh_dai_dien` varchar(255) DEFAULT NULL,
  `dia_chi` varchar(255) DEFAULT NULL,
  `dien_thoai` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `mat_khau` varchar(255) DEFAULT NULL,
  `ngay_sua` date DEFAULT NULL,
  `ngay_tao` date DEFAULT NULL,
  `ten_dang_nhap` varchar(255) DEFAULT NULL,
  `ten_day_du` varchar(255) DEFAULT NULL,
  `trang_thai` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKnlomnykdsca5gj0oakbh2cpu` (`dien_thoai`),
  UNIQUE KEY `UKj3lhg8opnqln2wcb41cp14xn9` (`email`),
  UNIQUE KEY `UKjbvtsjsqvol6fe5y1jwdu8oui` (`ten_dang_nhap`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `khach_hang`
--

/*!40000 ALTER TABLE `khach_hang` DISABLE KEYS */;
INSERT INTO `khach_hang` VALUES (0,'https://th.bing.com/th/id/OIP.v0Cw6Gydr5bjYPAQrKRljwHaHa?rs=1&pid=ImgDetMain','Thành Phố Hà Nội','0987654321','admin999@gmail.com','$2a$12$LAB2U5DtJF7mFzdoBlTSAe7TKZMdwd5DATege1BcjNpWQ.co4c49W','2024-09-13','2024-08-13','admin','Khách Vãn Lai',0x00),(3,'https://th.bing.com/th/id/OIP.v0Cw6Gydr5bjYPAQrKRljwHaHa?rs=1&pid=ImgDetMain','hà nội','8975453455','levanc55@gmail.com','$2a$12$6NlaUOyra1z57BKL9uxVBuYGh/g5fTQzrkrjDM5PuZHM.NHpi8/0u',NULL,'2024-09-19','levanC1','nguyen duc hoang',0x01),(4,NULL,NULL,'hoang00','hoang00090@gmail.com','$2a$12$GwadLZR4xCWUNIq1IqoYpeaVUwjPZ/f62LndOC9j59BCTq9kzHxU2','2024-09-20','2024-09-20',NULL,NULL,NULL),(7,'https://th.bing.com/th/id/OIP.v0Cw6Gydr5bjYPAQrKRljwHaHa?rs=1&pid=ImgDetMain','hà nội','8975453478','levanc4445@gmail.com','$2a$12$U90nWWapc3r49.Jq4M0wl.d7b2i/neW0ra3TxPczXTppbW7PWkKVG',NULL,'2024-09-22','hoang000','nguyen duc hoang',0x01),(10,NULL,NULL,'12345678967','hoang111@gmail.com','$2a$12$dF9sEnWNYqf4lrGKSTvFvusDM889c6XDCM1OleS/Ex8k4JK.2u3DG','2024-09-24','2024-09-24',NULL,NULL,NULL),(12,NULL,NULL,'0974653212','hoang999@gmail.com','$2a$12$0mFSzpsGt1.BGo0EE5hS0OKmfUEFVXaBaQPcyZrwu.guMRDpuj2xW','2024-10-04','2024-10-04',NULL,NULL,NULL);
/*!40000 ALTER TABLE `khach_hang` ENABLE KEYS */;

--
-- Table structure for table `lien_he`
--

DROP TABLE IF EXISTS `lien_he`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lien_he` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dia_chi` varchar(255) DEFAULT NULL,
  `dien_thoai` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `ngay_sua` date DEFAULT NULL,
  `ngay_tao` date DEFAULT NULL,
  `ten` varchar(255) DEFAULT NULL,
  `tieu_de` varchar(255) DEFAULT NULL,
  `tin_nhan` varchar(255) DEFAULT NULL,
  `web_site` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK7xu9rx6jlynpdiup0vmhjnxlj` (`dien_thoai`),
  UNIQUE KEY `UKcm39x5jg7vhd81c723syt2hpg` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lien_he`
--

/*!40000 ALTER TABLE `lien_he` DISABLE KEYS */;
INSERT INTO `lien_he` VALUES (1,'Thanh Xuân, Hà Nội','0388937608','nguyenduchoang522@gmail.com',NULL,'2024-09-24','Nguyễn Đức Hoàng','Shop Của Hoàng','Test','http://java-app:6868/admin/lien-he');
/*!40000 ALTER TABLE `lien_he` ENABLE KEYS */;

--
-- Table structure for table `nha_san_xuat`
--

DROP TABLE IF EXISTS `nha_san_xuat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nha_san_xuat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `anh` varchar(255) DEFAULT NULL,
  `link` varchar(255) DEFAULT NULL,
  `ngay_sua` date DEFAULT NULL,
  `ngay_tao` date DEFAULT NULL,
  `ten` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nha_san_xuat`
--

/*!40000 ALTER TABLE `nha_san_xuat` DISABLE KEYS */;
INSERT INTO `nha_san_xuat` VALUES (6,'https://i.postimg.cc/Yq6Wt2mZ/apple-logo.jpg','https://www.apple.com/','2024-09-17','2024-09-17','Apple'),(7,'https://i.postimg.cc/ZqM9wC1B/samsung-logo.png','https://www.samsung.com/vn','2024-09-17','2024-09-17','SamSung'),(8,'https://i.postimg.cc/g2G6j9VJ/oppo-logo.png','https://www.oppo.com/',NULL,'2024-09-17','Oppo'),(9,'https://i.postimg.cc/Vk40b7gF/realme-logo.jpg','https://www.mi.com/vn/',NULL,'2024-09-17','Realme'),(10,'https://i.postimg.cc/x8rXSBj9/vivo-logo.png','https://www.vivo.com/vn/',NULL,'2024-09-17','Vivo'),(11,'https://i.postimg.cc/c1YKc1TC/xiaomi-logo.png','https://www.mi.com/vn/','2024-09-17','2024-09-17','Xiaomi'),(12,'https://i.postimg.cc/qvBz0sr6/LG-logo.jpg','https://www.lg.com/vn/',NULL,'2024-09-17','LG');
/*!40000 ALTER TABLE `nha_san_xuat` ENABLE KEYS */;

--
-- Table structure for table `nhan_vien`
--

DROP TABLE IF EXISTS `nhan_vien`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nhan_vien` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `anh_dai_dien` varchar(255) DEFAULT NULL,
  `dien_thoai` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `mat_khau` varchar(255) DEFAULT NULL,
  `mat_khau_moi` varchar(255) DEFAULT NULL,
  `mo_ta` longtext DEFAULT NULL,
  `ngay_sua` date DEFAULT NULL,
  `ngay_tao` date DEFAULT NULL,
  `nhap_lai_mat_khau_moi` varchar(255) DEFAULT NULL,
  `ten_dang_nhap` varchar(255) DEFAULT NULL,
  `ten_day_du` varchar(255) DEFAULT NULL,
  `trang_thai` bit(1) DEFAULT NULL,
  `xac_nhan_mat_khau` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6ild79ti9ya6c9d9fb9j8xxs` (`dien_thoai`),
  UNIQUE KEY `UK12waxxsiniyddv2lt9ianfh8a` (`email`),
  UNIQUE KEY `UK9fb7c721iy35sk0jf6eckb9l8` (`ten_dang_nhap`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nhan_vien`
--

/*!40000 ALTER TABLE `nhan_vien` DISABLE KEYS */;
INSERT INTO `nhan_vien` VALUES (1,'https://th.bing.com/th/id/OIP.XKdZgJT9MaVBqYDg-5JlvgAAAA?rs=1&pid=ImgDetMain','0388937608','nam682800@gmail.com','$2a$12$zRtwn8cbY0b1AqPlSh.ar.ESwDW5CYlje5DB8qNJbrgkPgst0wLk6',NULL,'admin','2024-09-05','2024-06-27',NULL,'admin','Nguyễn Đức Hoàng',0x01,'12345678'),(2,'https://th.bing.com/th/id/OIP.XKdZgJT9MaVBqYDg-5JlvgAAAA?rs=1&pid=ImgDetMain','0987654377','hoang323v9@gmail.com','$2a$12$qRcEUvN49vWvCwtiyxwP9OZy0jJYsDJ2BAcZ5elzTe.HNOZ94qYO6',NULL,'ádvasdvadhmfhkmtghmhjmghmg','2024-09-14','2024-09-13',NULL,'levanC','Nguyen duc Hoang',0x01,'11111111');
/*!40000 ALTER TABLE `nhan_vien` ENABLE KEYS */;

--
-- Table structure for table `quang_cao`
--

DROP TABLE IF EXISTS `quang_cao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quang_cao` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `anh` varchar(255) DEFAULT NULL,
  `gia_tien` float DEFAULT NULL,
  `link` varchar(255) DEFAULT NULL,
  `mo_ta` varchar(255) DEFAULT NULL,
  `ngay_het_han` date DEFAULT NULL,
  `ngay_sua` date DEFAULT NULL,
  `ngay_tao` date DEFAULT NULL,
  `trang_thai` bit(1) DEFAULT NULL,
  `tua_de` varchar(255) DEFAULT NULL,
  `tua_de_phu` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quang_cao`
--

/*!40000 ALTER TABLE `quang_cao` DISABLE KEYS */;
INSERT INTO `quang_cao` VALUES (1,'https://i.postimg.cc/v8WBVqH6/banner-iphone.jpg',35000000,'https://www.apple.com/','IPhone 16 Siêu Chất','2024-10-30','2024-09-17',NULL,0x01,'IPhone 16 Đã Ra Mắt','Cùng Đón Chờ Nhé'),(4,'https://i.postimg.cc/3rC8RknX/banner-Xiaomi.jpg',500000,'https://www.samsung.com/vn','Hàng Giảm Mạnh Tay','2024-09-30','2024-09-24',NULL,0x00,'Xiaomi Lướt','Theo Dõi Ngay Nào');
/*!40000 ALTER TABLE `quang_cao` ENABLE KEYS */;

--
-- Table structure for table `san_pham`
--

DROP TABLE IF EXISTS `san_pham`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `san_pham` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `anh_dai_dien` varchar(255) DEFAULT NULL,
  `ban_chay` bit(1) DEFAULT NULL,
  `don_gia` float DEFAULT NULL,
  `mansx` int(11) DEFAULT NULL,
  `mo_ta` longtext DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `ngay_sua` date DEFAULT NULL,
  `ngay_tao` date DEFAULT NULL,
  `noi_bat` bit(1) DEFAULT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `tensp` varchar(255) DEFAULT NULL,
  `trang_thai` bit(1) DEFAULT NULL,
  `ngay_het_han` date DEFAULT NULL,
  `so_luong` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK54dkh41652f0skwnl5tbp1ceq` (`mansx`),
  CONSTRAINT `FK54dkh41652f0skwnl5tbp1ceq` FOREIGN KEY (`mansx`) REFERENCES `nha_san_xuat` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `san_pham`
--

/*!40000 ALTER TABLE `san_pham` DISABLE KEYS */;
INSERT INTO `san_pham` VALUES (8,'https://i.postimg.cc/dQzv4bn0/IPhone-15-Pro-Max.jpg',0x01,27600000,6,'Hàng đẹp như mới','512GB, Xám, Pin 97%',NULL,'2024-09-17',0x01,'IPhone, 15, Pro Max, Điện thoại','IPhone 15 Pro Max',0x01,NULL,50),(9,'https://i.postimg.cc/jSPpgC2g/IPhone-14-Pro-Max.jpg',0x00,20600000,6,'Hàng Có Hạn','256GB, Đỏ, Pin 100%, Xước Nhẹ',NULL,'2024-09-17',0x01,'IPhone, 14, Pro Max, Điện thoại','IPhone 14 Pro Max',0x01,NULL,10),(10,'https://i.postimg.cc/4djC2c60/Sam-Sung-S23-Utra.jpg',0x01,19444400,7,'Hàng Chất Lượng ','128GB, Xanh Đen','2024-09-17','2024-09-17',0x01,'SamSung, S23, Điện Thoại','SamSung S23 UTra',0x01,NULL,8),(11,'https://i.postimg.cc/02GPJFzj/LG-dien-thoai.jpg',0x00,6444440,12,'LG cũ siêu chất','128GB, Xanh Đen',NULL,'2024-09-18',0x00,'Điện Thoại, LG','LG Utra',0x01,NULL,5),(12,'https://i.postimg.cc/Njsn4bhM/Xiaomi-10.jpg',0x01,7444440,11,'chất chơi quá','128GB, Xanh Đen','2024-09-19','2024-09-18',0x00,'Điện Thoại, Xiaomi','Xiaomi 10',0x00,NULL,8),(13,'https://i.postimg.cc/TYdHC4kp/realme-3-pro.jpg',0x01,5794440,9,'mua đê','512GB, Vàng Cứt',NULL,'2024-09-18',0x01,'Điện Thoại, Realme','Realme 3 Pro',0x01,NULL,10),(14,'https://i.postimg.cc/zftYW6V8/Sam-Sung-S22.jpg',0x01,9005000,7,'hàng chất lượng','128GB, Xám, Pin 97%',NULL,'2024-09-22',0x01,'Điện Thoại, SamSung','SamSung S22',0x01,NULL,10),(15,'https://i.postimg.cc/43nSCdxR/Xiaomi-11.png',0x01,11005000,11,'mua đê','128GB, Xám, Pin 97%',NULL,'2024-09-22',0x01,'Điện Thoại, Xiaomi','Xiaomi 11',0x01,NULL,10),(16,'https://i.postimg.cc/xd7F92XJ/Xiaomi-12.jpg',0x00,15005000,11,'dfsdf','128GB, Xám, Pin 97%',NULL,'2024-09-22',0x00,'Điện Thoại, Xiaomi','Xiaomi 12',0x01,NULL,10),(17,'https://i.postimg.cc/QVL61kQX/IPhone-14-Plus.jpg',0x00,18005000,6,'sadvasdv','256GB, Xám, Pin 97%',NULL,'2024-09-22',0x00,'Điện Thoại, Iphone','IPhone 14 Plus',0x01,NULL,10),(18,'https://i.postimg.cc/kMtP6j9P/Sam-Sung-A54.jpg',0x00,6005000,7,'sdvasdf','256GB, Xám, Pin 97%',NULL,'2024-09-22',0x00,'Điện Thoại, samsung','SamSung ',0x01,NULL,10);
/*!40000 ALTER TABLE `san_pham` ENABLE KEYS */;

--
-- Dumping routines for database 'doan_web'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-10-04 22:20:13
