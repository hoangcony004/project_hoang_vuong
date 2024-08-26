-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 20, 2024 at 11:27 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.1.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `doan_web`
--

-- --------------------------------------------------------

--
-- Table structure for table `anh_san_pham`
--

CREATE TABLE `anh_san_pham` (
  `id` int(11) NOT NULL,
  `hinh_anh` varchar(255) DEFAULT NULL,
  `masp` int(11) NOT NULL,
  `ngay_sua` date DEFAULT NULL,
  `ngay_tao` date DEFAULT NULL,
  `thu_tu` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `anh_san_pham`
--

INSERT INTO `anh_san_pham` (`id`, `hinh_anh`, `masp`, `ngay_sua`, `ngay_tao`, `thu_tu`) VALUES
(6, 'https://i.postimg.cc/jSPpgC2g/IPhone-14-Pro-Max.jpg', 7, NULL, '2024-08-19', 1),
(7, 'https://i.postimg.cc/jSPpgC2g/IPhone-14-Pro-Max.jpg', 7, NULL, '2024-08-19', 2);

-- --------------------------------------------------------

--
-- Table structure for table `cai_dat`
--

CREATE TABLE `cai_dat` (
  `id` int(11) NOT NULL,
  `gia_tri` varchar(255) DEFAULT NULL,
  `khoa` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `khach_hang`
--

CREATE TABLE `khach_hang` (
  `id` int(11) NOT NULL,
  `anh_dai_dien` varchar(255) DEFAULT NULL,
  `dia_chi` varchar(255) DEFAULT NULL,
  `dien_thoai` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `mat_khau` varchar(255) DEFAULT NULL,
  `ngay_het_han` date DEFAULT NULL,
  `ngaytao` date DEFAULT NULL,
  `ten_dang_nhap` varchar(255) DEFAULT NULL,
  `ten_day_du` varchar(255) DEFAULT NULL,
  `thu_tu` int(11) NOT NULL,
  `trang_thai` bit(1) DEFAULT NULL,
  `ngay_tao` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `khach_hang`
--

INSERT INTO `khach_hang` (`id`, `anh_dai_dien`, `dia_chi`, `dien_thoai`, `email`, `mat_khau`, `ngay_het_han`, `ngaytao`, `ten_dang_nhap`, `ten_day_du`, `thu_tu`, `trang_thai`, `ngay_tao`) VALUES
(1, 'https://i.postimg.cc/Tw5svn4Z/Screenshot-25.png', 'hà nội', '0987654321', 'hoang@gmail.com', '$2a$12$X0fTxirCAyCgzWHaGQOkounQOlC1gTv2ZtqGsQkmPmu3fyniek94a', '2024-08-30', NULL, 'hoang001', 'hoang', 0, b'1', '2024-08-16'),
(2, 'https://i.postimg.cc/Tw5svn4Z/Screenshot-25.png', 'hà nội', '09984785', 'abc123@gmail.com', '$2a$12$s1xmwIxe1nTjUZY7q6Rg5.BbpCxbK217uXwGSu0tBaXUGg1FWvghy', '2024-08-30', NULL, 'abc', 'adc', 0, b'0', '2024-08-16');

-- --------------------------------------------------------

--
-- Table structure for table `nhan_vien`
--

CREATE TABLE `nhan_vien` (
  `id` int(11) NOT NULL,
  `anh_dai_dien` varchar(255) DEFAULT NULL,
  `dien_thoai` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `mat_khau` varchar(255) DEFAULT NULL,
  `mo_ta` varchar(255) DEFAULT NULL,
  `ngay_het_han` date DEFAULT NULL,
  `ngay_sua` date DEFAULT NULL,
  `ngay_tao` date DEFAULT NULL,
  `ten_dang_nhap` varchar(255) DEFAULT NULL,
  `ten_day_du` varchar(255) DEFAULT NULL,
  `thu_tu` int(11) NOT NULL,
  `trang_thai` bit(1) DEFAULT NULL,
  `xac_nhan_mat_khau` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `nhan_vien`
--

INSERT INTO `nhan_vien` (`id`, `anh_dai_dien`, `dien_thoai`, `email`, `mat_khau`, `mo_ta`, `ngay_het_han`, `ngay_sua`, `ngay_tao`, `ten_dang_nhap`, `ten_day_du`, `thu_tu`, `trang_thai`, `xac_nhan_mat_khau`) VALUES
(2, 'https://i.postimg.cc/Tw5svn4Z/Screenshot-25.png', '123456789', 'admin@gmail.com', '$2a$12$s/SzpL9xO0wBva9D4tYikuGZ0sNX1NwEqN2BPeN/V695c.8Jesh9a', 'admin', '2024-11-30', '2024-06-27', '2024-06-27', 'admin', 'Nguyễn Đức Hoàng', 1, b'1', '12345678'),
(3, 'https://i.postimg.cc/Tw5svn4Z/Screenshot-25.png', '045745', 'user123@gmail.com', '$2a$12$LkTw4SPR8bjFDvZ0z0UAI.xHZ1aNZtywddcF9HSNrbdH80RXtLvVa', 'dfbsdfbsdf', '2024-06-29', '2024-06-27', '2024-06-27', 'admin225456', 'Nguyễn Đức Hoàng', 2, b'1', ''),
(4, 'https://i.postimg.cc/Tw5svn4Z/Screenshot-25.png', '045745', 'user123@gmail.com', '$2a$12$pNHEsfAakVadSjjNf0XLWOsuRKCxEle0i66gCuRZT3fwCdOqmYY3y', 'svdasvdddd', '2024-08-17', NULL, '2024-08-13', 'admin2254567', 'Nguyễn Đức Hoàng', 0, b'0', NULL),
(5, 'https://i.postimg.cc/Tw5svn4Z/Screenshot-25.png', '045745', 'user123@gmail.com', '$2a$12$jHLNM7wJn8wUx7lOcjyUr.JvOHE8w1xfAyRfhj.RAPCrko4Io2n56', 'dfbzd', '2024-08-30', '2024-08-13', '2024-08-13', 'admin2254567', 'Nguyễn Đức Hoàng', 0, b'1', ''),
(6, 'https://i.postimg.cc/Tw5svn4Z/Screenshot-25.png', '045745', 'user123@gmail.com', '$2a$12$PM.Bf72xp5sCmR2chLemmu/fgAnOgF12TWt/rLOw.vgz97PJlEYV2', 'sáv', '2024-08-29', '2024-08-13', '2024-08-13', 'admin2254567', 'Nguyễn Đức Hoàng', 0, b'1', ''),
(9, 'https://i.postimg.cc/Tw5svn4Z/Screenshot-25.png', '0255555663', 'nguyenduchoang522@gmail.com', '$2a$12$kPqhrFSMjcKWR7fzORqz2OvDt5k.790aDIAG7Y36LCDX7644CdUOy', 'sdvsdv', '2024-08-15', '2024-08-14', '2024-08-14', 'hoang', 'a', 0, b'1', ''),
(10, 'https://i.postimg.cc/Tw5svn4Z/Screenshot-25.png', '0255555663', 'nguyenduchoang522@gmail.com', '$2a$12$fkg84Y1HdROQAV5Fhe889.hO0kFZZgUOcY/aYStl33mYs3ID/ZZ7i', 'dvsdv', '2024-08-16', '2024-08-14', '2024-08-14', 'hoang', 'b', 0, b'0', ''),
(11, 'https://i.postimg.cc/Tw5svn4Z/Screenshot-25.png', '23344566', 'admin666@gmail.com', '$2a$12$kwcrYU/6VRE.rXN5Ea.OuuVvqd/KLp1E74OkRcwGmkli1OxqyaDpG', 'test', '2024-08-31', '2024-08-18', '2024-08-16', 'kkllk', 'uuuuu', 0, b'0', NULL),
(12, 'https://i.postimg.cc/Tw5svn4Z/Screenshot-25.png', '745675688', 'hhhhhh@gmail.com', '$2a$12$K4UpEBXRskhJiIejAKqZ.urindMVxFDbJr1VrjfPjBEYxzJg1bBWi', 'gndgndfgn', '2024-08-24', '2024-08-16', '2024-08-16', 'gg66', 'gggg', 0, b'1', NULL),
(14, '', '09766363666', 'rrrrr@gmail.com', '$2a$12$LT4GZKN7a9aqjchvwoTz.uN6pWLkwQMuKSDZCfZZhGxw7TidO8dKi', 'sdfgsdfbdsf', '2024-08-28', '2024-08-16', '2024-08-16', 'levanC', 'lê văn c', 0, b'1', '12345678'),
(15, '', '34646555', 'abc@gmail.com', '$2a$12$jzuEwqER6QzWI50apH9Ad.RANO90qQSgPdQ/xk4MfIZU6/zvPhsQu', 'sdavsadv', '2024-08-29', '2024-08-16', '2024-08-16', 'abcd', 'abc', 0, b'1', '$2a$12$jzuEwqER6QzWI50apH9Ad.RANO90qQSgPdQ/xk4MfIZU6/zvPhsQu'),
(16, '', '5634563456', 'zzz@gmail.com', '', 'dfdfb', '2024-08-23', '2024-08-16', '2024-08-16', 'zzz', 'zzz', 0, b'1', '123456'),
(17, '', '5634563456', 'zzz@gmail.com', '$2a$12$20SQXTSil6cRr/THBgeyWOeYUpOfRW16zVM.7vatDTk8mMvJN3FNy', 'ávdasv', '2024-08-30', '2024-08-16', '2024-08-16', 'zzz1', 'zzz1', 0, b'1', '123456');

-- --------------------------------------------------------

--
-- Table structure for table `nha_san_xuat`
--

CREATE TABLE `nha_san_xuat` (
  `id` int(11) NOT NULL,
  `anh` varchar(255) DEFAULT NULL,
  `link` varchar(255) DEFAULT NULL,
  `noi_bat` int(11) NOT NULL,
  `ten` varchar(255) DEFAULT NULL,
  `thu_tu` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `nha_san_xuat`
--

INSERT INTO `nha_san_xuat` (`id`, `anh`, `link`, `noi_bat`, `ten`, `thu_tu`) VALUES
(1, 'https://i.postimg.cc/Tw5svn4Z/Screenshot-25.png', 'https://www.apple.com/', 0, 'Apple', 1),
(2, 'https://i.postimg.cc/Tw5svn4Z/Screenshot-25.png', 'https://www.samsung.com/', 0, 'SamSung', 1),
(3, 'https://i.postimg.cc/pX2H6890/Yelan-660416da8f1b3f627f8690f8.png', 'https://www.mi.com/vn/', 1, 'Xiaomi', 2),
(7, 'https://i.postimg.cc/pX2H6890/Yelan-660416da8f1b3f627f8690f8.png', 'https://www.realme.com/', 0, 'Realme', 0),
(8, 'https://i.postimg.cc/pX2H6890/Yelan-660416da8f1b3f627f8690f8.png', 'https://www.vivo.com/', 0, 'Vivo', 0),
(9, 'https://i.postimg.cc/pX2H6890/Yelan-660416da8f1b3f627f8690f8.png', 'https://www.oppo.com/', 0, 'Oppo', 0);

-- --------------------------------------------------------

--
-- Table structure for table `san_pham`
--

CREATE TABLE `san_pham` (
  `id` int(11) NOT NULL,
  `anh_dai_dien` varchar(255) DEFAULT NULL,
  `ban_chay` bit(1) DEFAULT NULL,
  `don_gia` float DEFAULT NULL,
  `mansx` int(11) NOT NULL,
  `mo_ta` varchar(255) DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `ngay_het_han` date DEFAULT NULL,
  `ngay_sua` date DEFAULT NULL,
  `ngay_tao` date DEFAULT NULL,
  `noi_bat` bit(1) DEFAULT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `thu_tu` int(11) NOT NULL,
  `trang_thai` bit(1) DEFAULT NULL,
  `tensp` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `san_pham`
--

INSERT INTO `san_pham` (`id`, `anh_dai_dien`, `ban_chay`, `don_gia`, `mansx`, `mo_ta`, `model`, `ngay_het_han`, `ngay_sua`, `ngay_tao`, `noi_bat`, `tag`, `thu_tu`, `trang_thai`, `tensp`) VALUES
(7, 'https://i.postimg.cc/jSPpgC2g/IPhone-14-Pro-Max.jpg', b'1', 20600000, 1, 'IPhone 14 tuyệt đẹp', 'IPhone', '2024-08-30', '2024-08-19', '2024-08-19', b'1', 'IPhone, 14, Điện thoại', 1, b'1', 'IPhone 14 Pro Max'),
(8, 'https://i.postimg.cc/BbRzNG0f/IPhone-11.png', b'1', 9700000, 1, 'IPhone 11 đẹp keng', 'IPhone', '2024-08-30', NULL, '2024-08-19', b'1', 'IPhone, 11, Điện thoại', 2, b'1', 'IPhone 11'),
(9, 'https://i.postimg.cc/fWP1MY0q/IPhone-11-Pro-Max.jpg', b'1', 9700000, 1, 'IPhome 11 Pro Max siêu lướt', 'IPhone', '2024-08-30', NULL, '2024-08-19', b'1', 'IPhone, 11, Pro Max, Điện thoại', 3, b'1', 'IPhone 11 Pro '),
(10, 'https://i.postimg.cc/rpgY34kJ/IPhone13.webp', b'1', 12800000, 1, 'IPhone 12 đẹp', 'IPhone', '2024-08-30', NULL, '2024-08-19', b'1', 'IPhone, 12, Điện thoại', 4, b'1', 'IPhone 13'),
(11, 'https://i.postimg.cc/266XD5jf/IPhone-13-Pro.jpg', b'1', 13500000, 1, 'IPhone 13', 'IPhone', '2024-08-30', NULL, '2024-08-19', b'1', 'IPhone, 13, Pro Max, Điện thoại', 5, b'1', 'IPhone 13 Pro Max'),
(12, 'https://i.postimg.cc/QVL61kQX/IPhone-14-Plus.jpg', b'1', 12800000, 1, 'IPhone 14 Plus new', 'IPhone', '2024-08-30', NULL, '2024-08-19', b'1', 'IPhone, 14, Điện thoại, Plus', 6, b'1', 'IPhone 14 Plus'),
(13, 'https://i.postimg.cc/zftYW6V8/Sam-Sung-S22.jpg', b'1', 3700000, 2, 'SamSung S22 Cũ', 'SamSung', '2024-08-30', NULL, '2024-08-19', b'1', 'SamSung, S22, Điện Thoại', 7, b'1', 'SamSung S22'),
(14, 'https://i.postimg.cc/4djC2c60/Sam-Sung-S23-Utra.jpg', b'1', 18766700, 2, 'SamSung S23 Utra mới', 'SamSung', '2024-08-30', NULL, '2024-08-19', b'1', 'SamSung, S23, Điện Thoại, Utra', 8, b'1', 'SamSung S23 Utra'),
(15, 'https://i.postimg.cc/Njsn4bhM/Xiaomi-10.jpg', b'1', 5400000, 3, 'Xiaomi 10 Bóc hộp', 'Xiaomi', '2024-08-30', NULL, '2024-08-19', b'1', 'Xiaomi, Điện thoại, ', 9, b'1', 'Xiaomi 10'),
(16, 'https://i.postimg.cc/SxdBF3LC/Sam-Sung-Galaxy-Z-Fold-4.png', b'1', 18444400, 2, 'SamSung Z Fold 4 siêu chất', 'SamSung', '2024-08-30', NULL, '2024-08-19', b'1', 'SamSung, Fold, Điện Thoại', 10, b'1', 'SamSung Z Fold 4');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `anh_san_pham`
--
ALTER TABLE `anh_san_pham`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKlk1my5j5shviqurasjc5n5k1o` (`masp`);

--
-- Indexes for table `cai_dat`
--
ALTER TABLE `cai_dat`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `khach_hang`
--
ALTER TABLE `khach_hang`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `nhan_vien`
--
ALTER TABLE `nhan_vien`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `nha_san_xuat`
--
ALTER TABLE `nha_san_xuat`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `san_pham`
--
ALTER TABLE `san_pham`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK54dkh41652f0skwnl5tbp1ceq` (`mansx`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `anh_san_pham`
--
ALTER TABLE `anh_san_pham`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `cai_dat`
--
ALTER TABLE `cai_dat`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `khach_hang`
--
ALTER TABLE `khach_hang`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `nhan_vien`
--
ALTER TABLE `nhan_vien`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `nha_san_xuat`
--
ALTER TABLE `nha_san_xuat`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `san_pham`
--
ALTER TABLE `san_pham`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `anh_san_pham`
--
ALTER TABLE `anh_san_pham`
  ADD CONSTRAINT `FKlk1my5j5shviqurasjc5n5k1o` FOREIGN KEY (`masp`) REFERENCES `san_pham` (`id`);

--
-- Constraints for table `san_pham`
--
ALTER TABLE `san_pham`
  ADD CONSTRAINT `FK54dkh41652f0skwnl5tbp1ceq` FOREIGN KEY (`mansx`) REFERENCES `nha_san_xuat` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
