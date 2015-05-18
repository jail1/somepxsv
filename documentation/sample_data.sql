-- phpMyAdmin SQL Dump
-- version 4.2.11
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: May 07, 2015 at 04:41 PM
-- Server version: 5.6.21
-- PHP Version: 5.6.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `photox`
--

--
-- Dumping data for table `photox_products`
--

INSERT INTO `photox_products` (`ID`, `parent_id`, `is_listable`, `is_complex`, `name`, `description`, `extra_data`, `ratio`, `lost_ratio`, `min_quantity`) VALUES
(1, NULL, 1, 0, 'Fotografia', '810x305mm', '{"width":305,"height":810}', 0.24705, 0, 1),
(2, 4, 0, 0, 'Fotografia pt bloc album', '810x305mm', '{"width":305,"height":810}', 0.24705, 0, 10),
(3, 4, 0, 0, 'Componenta bloc album', '810x305mm', '{"width":310,"height":410}', 0.1271, 0, 11),
(4, NULL, 0, 1, 'Bloc album', '305x810 raster', '{"width":0,"height":0}', 0, 0, 1),
(5, 4, 0, 0, 'Manopera bloc', NULL, '{"width":305,"height":810}', 0.24705, 0, 10);

--
-- Dumping data for table `photox_products_prices`
--

INSERT INTO `photox_products_prices` (`ID`, `product_id`, `rawmaterial_id`, `selling_price`) VALUES
(1, 1, 3, 4.47),
(2, 1, 4, 1.905),
(3, 2, 3, 4.47),
(4, 2, 4, 1.905),
(6, 5, 13, 0.0000001);

--
-- Dumping data for table `photox_products_selectors`
--

INSERT INTO `photox_products_selectors` (`id`, `rawmaterialgroup_id`, `product_id`) VALUES
(1, 3, 1),
(2, 4, 1),
(3, 3, 2),
(4, 4, 2),
(5, 3, 2),
(6, 4, 2),
(8, 5, 3),
(9, 2, 5),
(10, 8, 5);

--
-- Dumping data for table `photox_rawmaterials`
--

INSERT INTO `photox_rawmaterials` (`ID`, `parent_group_id`, `name`, `description`, `aquisition_price`, `aquisition_units`, `price_per_production_unit`, `units_per_production_unit`) VALUES
(1, 1, 'Manopera foto', '', 2.5, 1, 2.5, 1),
(2, 2, 'Manopera bloc album', '', 2.5, 1, 2.5, 1),
(3, 3, 'Hartie foto Metal 305mm', '', 178.64, 30.8, 5.8, 1),
(4, 3, 'Hartie foto Premier Y 305mm', '', 72.072, 30.8, 2.34, 1),
(5, 4, 'Revelator (CD) RA4', '', 88, 50000, 0.1408, 80),
(6, 4, 'Fixator (BF) RA4', '', 15, 20000, 0.06, 80),
(7, 4, 'Stabilizator (STB) RA4', '', 22, 240000, 0.0275, 300),
(8, 5, 'Carton biadeziv', '', 2.2, 1, 2.2, 1),
(9, 6, 'Faceoff alb', '', 3, 1, 3, 1),
(10, 6, 'Faceoff negru', '', 3.1, 1, 3.1, 1),
(11, 7, 'Material divers', '', 0.3, 1, 0.3, 1),
(13, 8, 'v2', '', 0, 0, 0, 0),
(14, 8, 'v3', '', 0, 0, 0, 0);

--
-- Dumping data for table `photox_rawmaterial_groups`
--

INSERT INTO `photox_rawmaterial_groups` (`id`, `name`, `required_in_product`, `is_selector`) VALUES
(1, 'Manopera', 0, 0),
(2, 'Manopera bloc album', 0, 0),
(3, 'Hartia', 0, 1),
(4, 'Chimicale', 0, 0),
(5, 'Biadeziv', 0, 0),
(6, 'Faceoff', 0, 1),
(7, 'Materiale diverse', 0, 0),
(8, 'Fake', 0, 1);

--
-- Dumping data for table `photox_settings`
--

INSERT INTO `photox_settings` (`ID`, `setting_key`, `setting_value`, `typecast`) VALUES
(1, 'applicationMinAddedPercent', '10', 'Integer'),
(2, 'applicationDefaultDiscount', '10', 'Integer'),
(3, 'applicationEuro', '4.5', 'Double'),
(4, 'applicationDiskQuota', '6442450944', 'Double'),
(5, 'applicationDefaultDecimals', '4', 'Integer');

--
-- Dumping data for table `photox_users`
--

INSERT INTO `photox_users` (`ID`, `parent_id`, `account_enabled`, `activation_string`, `created_at`, `first_name`, `last_seen`, `last_name`, `phone`, `modified_at`, `pwd`, `retrieval_code`, `username`, `login_name`, `prefered_language`) VALUES
(1, NULL, 1, 'f3c71a609eadfff8ad713dc6dde1309b', '2015-05-07', 'ActiveMall', '2015-05-07', 'SRL', '+4000000', '2015-05-07', '$2a$10$2tLQnvnutQllyLrMZmcdJur5UZ3GGI74t9vZ3vpw1UkkF1tqexBje', NULL, 'office@activemall.ro', 'admin', 'ro_RO');

--
-- Dumping data for table `photox_users_logs`
--

INSERT INTO `photox_users_logs` (`id`, `type`, `event_date`, `user_id`, `extra_info`) VALUES
(1, 0, '2015-05-07', 1, '2015-05-07T15:42:17.737+03:00'),
(2, 0, '2015-05-07', 1, '2015-05-07T16:05:35.274+03:00'),
(3, 0, '2015-05-07', 1, '2015-05-07T16:26:00.163+03:00'),
(4, 0, '2015-05-07', 1, '2015-05-07T16:59:47.754+03:00');

--
-- Dumping data for table `photox_user_roles`
--

INSERT INTO `photox_user_roles` (`id`, `role`, `user_id`) VALUES
(1, 1, 1);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
