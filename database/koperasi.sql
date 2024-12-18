-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 18 Des 2024 pada 07.09
-- Versi server: 10.4.28-MariaDB
-- Versi PHP: 8.0.28

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
-- Struktur dari tabel `dim_admin`
--

CREATE TABLE `dim_admin` (
  `Admin_ID` int(11) NOT NULL,
  `Nama` varchar(100) DEFAULT NULL,
  `Alamat` varchar(255) DEFAULT NULL,
  `Tanggal_Lahir` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `dim_anggota`
--

CREATE TABLE `dim_anggota` (
  `Anggota_ID` int(11) NOT NULL,
  `Nama` varchar(100) DEFAULT NULL,
  `Alamat` varchar(255) DEFAULT NULL,
  `Tanggal_Lahir` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `dim_bayar_pinjaman`
--

CREATE TABLE `dim_bayar_pinjaman` (
  `Bayar_Pinjaman_ID` int(11) NOT NULL,
  `Tipe_Transaksi` varchar(50) NOT NULL,
  `Amount` decimal(15,0) NOT NULL,
  `Tanggal_Transaksi` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `dim_pinjam`
--

CREATE TABLE `dim_pinjam` (
  `Pinjam_ID` int(11) NOT NULL,
  `Pinjaman_Amount` decimal(15,0) NOT NULL,
  `Tanggal_Pinjam_ID` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `dim_simpan`
--

CREATE TABLE `dim_simpan` (
  `Simpan_ID` int(11) NOT NULL,
  `Simpanan_Amount` decimal(15,0) NOT NULL,
  `Tanggal_Simpan_ID` date NOT NULL,
  `Tanggal_Tarik_Simpan_ID` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `dim_status`
--

CREATE TABLE `dim_status` (
  `Status_ID` int(11) NOT NULL,
  `Status` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `dim_admin`
--
ALTER TABLE `dim_admin`
  ADD PRIMARY KEY (`Admin_ID`);

--
-- Indeks untuk tabel `dim_anggota`
--
ALTER TABLE `dim_anggota`
  ADD PRIMARY KEY (`Anggota_ID`);

--
-- Indeks untuk tabel `dim_bayar_pinjaman`
--
ALTER TABLE `dim_bayar_pinjaman`
  ADD PRIMARY KEY (`Bayar_Pinjaman_ID`);

--
-- Indeks untuk tabel `dim_pinjam`
--
ALTER TABLE `dim_pinjam`
  ADD PRIMARY KEY (`Pinjam_ID`);

--
-- Indeks untuk tabel `dim_simpan`
--
ALTER TABLE `dim_simpan`
  ADD PRIMARY KEY (`Simpan_ID`);

--
-- Indeks untuk tabel `dim_status`
--
ALTER TABLE `dim_status`
  ADD PRIMARY KEY (`Status_ID`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `dim_admin`
--
ALTER TABLE `dim_admin`
  MODIFY `Admin_ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `dim_anggota`
--
ALTER TABLE `dim_anggota`
  MODIFY `Anggota_ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `dim_status`
--
ALTER TABLE `dim_status`
  MODIFY `Status_ID` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
