-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 02 Jul 2025 pada 11.40
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_arion`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `data_pembayaran_per_siswa`
--

CREATE TABLE `data_pembayaran_per_siswa` (
  `id_siswa` int(11) NOT NULL,
  `nama_siswa` varchar(100) DEFAULT NULL,
  `jumlah_bulan_dibayar` int(11) DEFAULT NULL,
  `jumlah_bulan_tunggakan` int(11) DEFAULT 0,
  `status_pembayaran_buku` enum('lunas','belum_lunas') DEFAULT 'belum_lunas',
  `status_pembayaran_seragam` enum('lunas','belum_lunas') DEFAULT 'belum_lunas',
  `status_pembayaran_spp` enum('lunas','belum_lunas') DEFAULT 'belum_lunas'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `data_siswa`
--

CREATE TABLE `data_siswa` (
  `id_siswa` int(11) NOT NULL,
  `nis` varchar(20) NOT NULL,
  `nama_siswa` varchar(100) DEFAULT NULL,
  `kelas` varchar(10) NOT NULL,
  `jenis_kelamin` enum('Laki-Laki','Perempuan') DEFAULT NULL,
  `alamat` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `data_siswa`
--

INSERT INTO `data_siswa` (`id_siswa`, `nis`, `nama_siswa`, `kelas`, `jenis_kelamin`, `alamat`) VALUES
(10001, '109087', 'Kia', '07', 'Perempuan', 'Pamulang'),
(10002, '232012', 'Jarwo', '07', 'Laki-Laki', 'surabaya'),
(10003, '2425263', 'susanti', '07', 'Perempuan', 'jakarta'),
(10004, '30232', 'Ibrahim', '07', 'Laki-Laki', 'Tanah abang'),
(10005, '256172', 'Origin', '06', 'Laki-Laki', 'Depok'),
(10006, '152633', 'Adrian', '05', 'Laki-Laki', 'ciledug'),
(10007, '192933', 'Nawita', '05', 'Perempuan', 'Tanggerang'),
(10008, '231032039', 'Jumyarti', '06', 'Perempuan', 'Ciputat'),
(10009, '1092125', 'Saleh', '09', 'Laki-Laki', 'Depok');

-- --------------------------------------------------------

--
-- Struktur dari tabel `data_staff_administrasi`
--

CREATE TABLE `data_staff_administrasi` (
  `id_staff` int(11) NOT NULL,
  `nama_staff` varchar(30) DEFAULT NULL,
  `telepon` varchar(20) DEFAULT NULL,
  `alamat` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `data_staff_administrasi`
--

INSERT INTO `data_staff_administrasi` (`id_staff`, `nama_staff`, `telepon`, `alamat`) VALUES
(20001, 'sandika', '085793930723', 'ciledug'),
(20002, 'Daffa', '08965434261', 'Cisauk'),
(20003, 'Susi', '08337782', 'Bekasi'),
(20004, 'Jumyarto', '08975362712', 'Depok'),
(20005, 'Budi', '0853423638444', 'Tanggerang'),
(20006, 'Jaya', '086735453643', 'Bogor');

-- --------------------------------------------------------

--
-- Struktur dari tabel `transaksi_pembayaran`
--

CREATE TABLE `transaksi_pembayaran` (
  `id_pembayaran` int(11) NOT NULL,
  `id_siswa` int(11) DEFAULT NULL,
  `nama_siswa` varchar(100) DEFAULT NULL,
  `id_staff` int(11) DEFAULT NULL,
  `jenis_pembayaran` varchar(100) DEFAULT NULL,
  `metode_pembayaran` varchar(30) DEFAULT NULL,
  `biaya_spp_perbulan` decimal(10,2) DEFAULT NULL,
  `bulan` enum('Januari','Februari','Maret','April','Mei','Juni','Juli','Agustus','September','Oktober','November','Desember') DEFAULT NULL,
  `biaya_seragam` decimal(10,2) DEFAULT 0.00,
  `biaya_buku` decimal(10,2) DEFAULT 0.00,
  `total_pembayaran` decimal(10,2) DEFAULT NULL,
  `tanggal` date DEFAULT NULL,
  `status_pembayaran` enum('lunas','belum_lunas') DEFAULT 'belum_lunas'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `transaksi_pembayaran`
--

INSERT INTO `transaksi_pembayaran` (`id_pembayaran`, `id_siswa`, `nama_siswa`, `id_staff`, `jenis_pembayaran`, `metode_pembayaran`, `biaya_spp_perbulan`, `bulan`, `biaya_seragam`, `biaya_buku`, `total_pembayaran`, `tanggal`, `status_pembayaran`) VALUES
(101108, 10007, 'Nawita', 20001, 'SERAGAM', 'TRANSFER', 0.00, NULL, 250000.00, 0.00, 250000.00, '2025-06-15', 'lunas'),
(101109, 10007, 'Nawita', 20001, 'SPP', 'TRANSFER', 650000.00, 'Januari', 0.00, 0.00, 650000.00, '2025-06-15', 'lunas'),
(101110, 10007, 'Nawita', 20001, 'SPP', 'CASH', 650000.00, 'Februari', 0.00, 0.00, 650000.00, '2025-06-15', 'lunas'),
(101111, 10006, 'Adrian', 20001, 'SPP', 'TRANSFER', 650000.00, 'Januari', 0.00, 0.00, 650000.00, '2025-06-15', 'lunas'),
(101112, 10007, 'Nawita', 20001, 'SPP', 'CASH', 650000.00, 'Maret', 0.00, 0.00, 650000.00, '2025-06-15', 'lunas'),
(101113, 10005, 'Origin', 20002, 'SERAGAM', 'CASH', 0.00, NULL, 250000.00, 0.00, 250000.00, '2025-06-21', 'lunas'),
(101114, 10005, 'Origin', 20001, 'SPP', 'TRANSFER', 650000.00, 'Januari', 0.00, 0.00, 650000.00, '2025-06-21', 'lunas'),
(101115, 10008, 'Jumyarti', 20002, 'BUKU', 'TRANSFER', 0.00, NULL, 0.00, 250000.00, 250000.00, '2025-06-21', 'lunas'),
(101116, 10009, 'Saleh', 20002, 'SERAGAM', 'TRANSFER', 0.00, NULL, 250000.00, 0.00, 250000.00, '2025-06-21', 'lunas'),
(101117, 10006, 'Adrian', 20001, 'SERAGAM', 'TRANSFER', 0.00, NULL, 250000.00, 0.00, 250000.00, '2025-06-29', 'lunas');

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `akses` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `akses`) VALUES
(10001, 'Kia', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'siswa'),
(10002, 'Jarwo', '8bb0cf6eb9b17d0f7d22b456f121257dc1254e1f01665370476383ea776df414', 'siswa'),
(10003, 'susanti', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'siswa'),
(10004, 'Ibrahim', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'siswa'),
(10005, 'Origin', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'siswa'),
(10006, 'Adrian', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'siswa'),
(10007, 'Nawita', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'siswa'),
(10008, 'Jumyarti', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'siswa'),
(10009, 'Saleh', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'siswa'),
(20001, 'sandika', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'staff'),
(20002, 'Daffa', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'staff'),
(20003, 'Susi', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'staff'),
(20004, 'Jumyarto', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'staff'),
(20005, 'Budi', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'staff'),
(20006, 'Jaya', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'staff');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `data_pembayaran_per_siswa`
--
ALTER TABLE `data_pembayaran_per_siswa`
  ADD PRIMARY KEY (`id_siswa`);

--
-- Indeks untuk tabel `data_siswa`
--
ALTER TABLE `data_siswa`
  ADD PRIMARY KEY (`id_siswa`);

--
-- Indeks untuk tabel `data_staff_administrasi`
--
ALTER TABLE `data_staff_administrasi`
  ADD PRIMARY KEY (`id_staff`),
  ADD UNIQUE KEY `telepon` (`telepon`);

--
-- Indeks untuk tabel `transaksi_pembayaran`
--
ALTER TABLE `transaksi_pembayaran`
  ADD PRIMARY KEY (`id_pembayaran`),
  ADD KEY `fk_dsa_staff` (`id_staff`),
  ADD KEY `fk_transaksi_siswa` (`id_siswa`);

--
-- Indeks untuk tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `data_siswa`
--
ALTER TABLE `data_siswa`
  MODIFY `id_siswa` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10017;

--
-- AUTO_INCREMENT untuk tabel `data_staff_administrasi`
--
ALTER TABLE `data_staff_administrasi`
  MODIFY `id_staff` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20007;

--
-- AUTO_INCREMENT untuk tabel `transaksi_pembayaran`
--
ALTER TABLE `transaksi_pembayaran`
  MODIFY `id_pembayaran` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=101118;

--
-- AUTO_INCREMENT untuk tabel `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20010;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `data_pembayaran_per_siswa`
--
ALTER TABLE `data_pembayaran_per_siswa`
  ADD CONSTRAINT `fk_dpp_siswa` FOREIGN KEY (`id_siswa`) REFERENCES `data_siswa` (`id_siswa`);

--
-- Ketidakleluasaan untuk tabel `transaksi_pembayaran`
--
ALTER TABLE `transaksi_pembayaran`
  ADD CONSTRAINT `fk_dsa_staff` FOREIGN KEY (`id_staff`) REFERENCES `data_staff_administrasi` (`id_staff`),
  ADD CONSTRAINT `fk_id_staff` FOREIGN KEY (`id_staff`) REFERENCES `data_staff_administrasi` (`id_staff`),
  ADD CONSTRAINT `fk_transaksi_siswa` FOREIGN KEY (`id_siswa`) REFERENCES `data_siswa` (`id_siswa`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
