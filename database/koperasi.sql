-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 18, 2024 at 11:54 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `koperasi`
--

-- --------------------------------------------------------

--
-- Table structure for table `dim_admin`
--

CREATE TABLE `dim_admin` (
  `Admin_ID` int(11) NOT NULL,
  `Nama` varchar(100) DEFAULT NULL,
  `Alamat` varchar(255) DEFAULT NULL,
  `Tanggal_Lahir` date DEFAULT NULL,
  `Username` varchar(18) NOT NULL,
  `Password` varchar(18) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `dim_anggota`
--

CREATE TABLE `dim_anggota` (
  `Anggota_ID` int(11) NOT NULL,
  `Nama` varchar(100) DEFAULT NULL,
  `Alamat` varchar(255) DEFAULT NULL,
  `Tanggal_Lahir` date DEFAULT NULL,
  `Username` varchar(18) NOT NULL,
  `Password` varchar(18) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `dim_bayar_pinjaman`
--

CREATE TABLE `dim_bayar_pinjaman` (
  `Bayar_Pinjaman_ID` int(11) NOT NULL,
  `Tipe_Transaksi` varchar(50) NOT NULL,
  `Amount` decimal(15,0) NOT NULL,
  `Tanggal_Transaksi` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `dim_pinjam`
--

CREATE TABLE `dim_pinjam` (
  `Pinjam_ID` int(11) NOT NULL,
  `Pinjaman_Amount` decimal(15,0) NOT NULL,
  `Tanggal_Pinjam_ID` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `dim_simpan`
--

CREATE TABLE `dim_simpan` (
  `Simpan_ID` int(11) NOT NULL,
  `Simpanan_Amount` decimal(15,0) NOT NULL,
  `Tanggal_Simpan_ID` date NOT NULL,
  `Tanggal_Tarik_Simpan_ID` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `dim_status`
--

CREATE TABLE `dim_status` (
  `Status_ID` int(11) NOT NULL,
  `Status` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `fact_simpanan`
--

CREATE TABLE `fact_simpanan` (
  `Fact_Simpanan_ID` int(11) NOT NULL,
  `Simpan_ID` int(11) DEFAULT NULL,
  `Anggota_ID` int(11) DEFAULT NULL,
  `Total_Simpanan` decimal(15,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `fact_transaksi_pinjaman`
--

CREATE TABLE `fact_transaksi_pinjaman` (
  `Fact_Transaksi_ID` int(11) NOT NULL,
  `Admin_ID` int(11) DEFAULT NULL,
  `Bayar_Pinjaman_ID` int(11) DEFAULT NULL,
  `Anggota_ID` int(11) DEFAULT NULL,
  `Pinjam_ID` int(11) DEFAULT NULL,
  `Status_ID` int(11) DEFAULT NULL,
  `Sisa_Pinjaman` decimal(15,2) DEFAULT NULL,
  `Total_Pinjaman` decimal(15,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `dim_admin`
--
ALTER TABLE `dim_admin`
  ADD PRIMARY KEY (`Admin_ID`);

--
-- Indexes for table `dim_anggota`
--
ALTER TABLE `dim_anggota`
  ADD PRIMARY KEY (`Anggota_ID`);

--
-- Indexes for table `dim_bayar_pinjaman`
--
ALTER TABLE `dim_bayar_pinjaman`
  ADD PRIMARY KEY (`Bayar_Pinjaman_ID`);

--
-- Indexes for table `dim_pinjam`
--
ALTER TABLE `dim_pinjam`
  ADD PRIMARY KEY (`Pinjam_ID`);

--
-- Indexes for table `dim_simpan`
--
ALTER TABLE `dim_simpan`
  ADD PRIMARY KEY (`Simpan_ID`);

--
-- Indexes for table `dim_status`
--
ALTER TABLE `dim_status`
  ADD PRIMARY KEY (`Status_ID`);

--
-- Indexes for table `fact_simpanan`
--
ALTER TABLE `fact_simpanan`
  ADD PRIMARY KEY (`Fact_Simpanan_ID`),
  ADD KEY `Simpan_ID` (`Simpan_ID`),
  ADD KEY `Anggota_ID` (`Anggota_ID`);

--
-- Indexes for table `fact_transaksi_pinjaman`
--
ALTER TABLE `fact_transaksi_pinjaman`
  ADD PRIMARY KEY (`Fact_Transaksi_ID`),
  ADD KEY `Admin_ID` (`Admin_ID`),
  ADD KEY `Bayar_Pinjaman_ID` (`Bayar_Pinjaman_ID`),
  ADD KEY `Anggota_ID` (`Anggota_ID`),
  ADD KEY `Pinjam_ID` (`Pinjam_ID`),
  ADD KEY `Status_ID` (`Status_ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `dim_admin`
--
ALTER TABLE `dim_admin`
  MODIFY `Admin_ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `dim_anggota`
--
ALTER TABLE `dim_anggota`
  MODIFY `Anggota_ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `dim_status`
--
ALTER TABLE `dim_status`
  MODIFY `Status_ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `fact_transaksi_pinjaman`
--
ALTER TABLE `fact_transaksi_pinjaman`
  MODIFY `Fact_Transaksi_ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `fact_simpanan`
--
ALTER TABLE `fact_simpanan`
  ADD CONSTRAINT `fact_simpanan_ibfk_1` FOREIGN KEY (`Simpan_ID`) REFERENCES `dim_simpan` (`Simpan_ID`),
  ADD CONSTRAINT `fact_simpanan_ibfk_2` FOREIGN KEY (`Anggota_ID`) REFERENCES `dim_anggota` (`Anggota_ID`);

--
-- Constraints for table `fact_transaksi_pinjaman`
--
ALTER TABLE `fact_transaksi_pinjaman`
  ADD CONSTRAINT `fact_transaksi_pinjaman_ibfk_1` FOREIGN KEY (`Admin_ID`) REFERENCES `dim_admin` (`Admin_ID`),
  ADD CONSTRAINT `fact_transaksi_pinjaman_ibfk_2` FOREIGN KEY (`Bayar_Pinjaman_ID`) REFERENCES `dim_bayar_pinjaman` (`Bayar_Pinjaman_ID`),
  ADD CONSTRAINT `fact_transaksi_pinjaman_ibfk_3` FOREIGN KEY (`Anggota_ID`) REFERENCES `dim_anggota` (`Anggota_ID`),
  ADD CONSTRAINT `fact_transaksi_pinjaman_ibfk_4` FOREIGN KEY (`Pinjam_ID`) REFERENCES `dim_pinjam` (`Pinjam_ID`),
  ADD CONSTRAINT `fact_transaksi_pinjaman_ibfk_5` FOREIGN KEY (`Status_ID`) REFERENCES `dim_status` (`Status_ID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
