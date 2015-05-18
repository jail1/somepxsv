-- phpMyAdmin SQL Dump
-- version 4.2.11
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: May 07, 2015 at 02:39 PM
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

-- --------------------------------------------------------

--
-- Table structure for table `photox_companies`
--

CREATE TABLE IF NOT EXISTS `photox_companies` (
`ID` bigint(20) NOT NULL,
  `registered_at` datetime NOT NULL,
  `owner_id` bigint(20) NOT NULL,
  `is_default` tinyint(1) DEFAULT '1',
  `vat_payer` tinyint(1) DEFAULT '1',
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `cif` varchar(25) COLLATE utf8_bin DEFAULT NULL,
  `rc` varchar(25) COLLATE utf8_bin DEFAULT NULL,
  `address` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `phone` varchar(10) COLLATE utf8_bin DEFAULT NULL,
  `iban` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `bank` varchar(30) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Table structure for table `photox_invoices`
--

CREATE TABLE IF NOT EXISTS `photox_invoices` (
`ID` bigint(20) NOT NULL,
  `created_at` datetime NOT NULL,
  `serie` varchar(254) COLLATE utf8_bin NOT NULL,
  `due_to` datetime NOT NULL,
  `owner_id` bigint(20) NOT NULL,
  `status` int(11) DEFAULT NULL,
  `company_id` bigint(20) NOT NULL,
  `target_company_id` bigint(20) NOT NULL,
  `discount` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Table structure for table `photox_invoice_lines`
--

CREATE TABLE IF NOT EXISTS `photox_invoice_lines` (
`ID` bigint(20) NOT NULL,
  `invoice_id` bigint(20) NOT NULL,
  `sequence` int(11) DEFAULT NULL,
  `name` varchar(254) COLLATE utf8_bin NOT NULL,
  `um` varchar(254) COLLATE utf8_bin DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `price` double NOT NULL,
  `vat` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Table structure for table `photox_products`
--

CREATE TABLE IF NOT EXISTS `photox_products` (
`ID` bigint(20) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `is_listable` tinyint(1) NOT NULL DEFAULT '0',
  `is_complex` tinyint(1) NOT NULL DEFAULT '0',
  `name` varchar(254) COLLATE utf8_bin NOT NULL,
  `description` varchar(254) COLLATE utf8_bin DEFAULT NULL,
  `extra_data` varchar(254) COLLATE utf8_bin DEFAULT NULL,
  `ratio` double NOT NULL,
  `lost_ratio` double DEFAULT NULL,
  `min_quantity` int(11) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Table structure for table `photox_products_prices`
--

CREATE TABLE IF NOT EXISTS `photox_products_prices` (
`ID` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `rawmaterial_id` bigint(20) NOT NULL,
  `selling_price` double NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Table structure for table `photox_products_selectors`
--

CREATE TABLE IF NOT EXISTS `photox_products_selectors` (
`id` bigint(20) NOT NULL,
  `rawmaterialgroup_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Table structure for table `photox_rawmaterials`
--

CREATE TABLE IF NOT EXISTS `photox_rawmaterials` (
`ID` bigint(20) NOT NULL,
  `parent_group_id` bigint(20) NOT NULL,
  `name` varchar(254) COLLATE utf8_bin NOT NULL,
  `description` varchar(254) COLLATE utf8_bin DEFAULT NULL,
  `aquisition_price` double NOT NULL,
  `aquisition_units` double NOT NULL,
  `price_per_production_unit` double NOT NULL,
  `units_per_production_unit` double NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Table structure for table `photox_rawmaterial_groups`
--

CREATE TABLE IF NOT EXISTS `photox_rawmaterial_groups` (
`id` bigint(20) NOT NULL,
  `name` varchar(254) COLLATE utf8_bin NOT NULL,
  `required_in_product` tinyint(1) DEFAULT '0',
  `is_selector` tinyint(1) DEFAULT '1'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Table structure for table `photox_resources`
--

CREATE TABLE IF NOT EXISTS `photox_resources` (
`id` bigint(20) NOT NULL,
  `created_at` datetime NOT NULL,
  `owner_id` bigint(20) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `title` text COLLATE utf8_bin NOT NULL,
  `description` text COLLATE utf8_bin,
  `extra` text COLLATE utf8_bin,
  `content_type` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `sequence` int(11) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Table structure for table `photox_resources_tags`
--

CREATE TABLE IF NOT EXISTS `photox_resources_tags` (
`id` bigint(20) NOT NULL,
  `resource_id` bigint(20) NOT NULL,
  `tag_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Table structure for table `photox_settings`
--

CREATE TABLE IF NOT EXISTS `photox_settings` (
`ID` bigint(20) NOT NULL,
  `setting_key` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `setting_value` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `typecast` varchar(255) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Table structure for table `photox_tag_names`
--

CREATE TABLE IF NOT EXISTS `photox_tag_names` (
`id` bigint(20) NOT NULL,
  `name` varchar(254) COLLATE utf8_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Table structure for table `photox_templates`
--

CREATE TABLE IF NOT EXISTS `photox_templates` (
`id` bigint(20) NOT NULL,
  `type` int(11) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `owner_id` bigint(20) NOT NULL,
  `title` text COLLATE utf8_bin NOT NULL,
  `content` text COLLATE utf8_bin
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Table structure for table `photox_users`
--

CREATE TABLE IF NOT EXISTS `photox_users` (
`ID` bigint(20) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `account_enabled` tinyint(1) DEFAULT '1',
  `activation_string` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `created_at` date DEFAULT NULL,
  `first_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `last_seen` date DEFAULT NULL,
  `last_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `phone` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `modified_at` date DEFAULT NULL,
  `pwd` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `retrieval_code` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `username` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
  `login_name` varchar(60) COLLATE utf8_bin NOT NULL DEFAULT '',
  `prefered_language` varchar(5) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Table structure for table `photox_users_extra`
--

CREATE TABLE IF NOT EXISTS `photox_users_extra` (
  `ID` bigint(20) NOT NULL,
  `owner_id` bigint(20) NOT NULL,
  `logo` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `facebook_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `twitter_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `googleplus_id` varchar(255) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Table structure for table `photox_users_logs`
--

CREATE TABLE IF NOT EXISTS `photox_users_logs` (
`id` bigint(20) NOT NULL,
  `type` int(11) NOT NULL,
  `event_date` date DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  `extra_info` text COLLATE utf8_bin NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Table structure for table `photox_user_roles`
--

CREATE TABLE IF NOT EXISTS `photox_user_roles` (
`id` bigint(20) NOT NULL,
  `role` int(11) DEFAULT '3',
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `photox_companies`
--
ALTER TABLE `photox_companies`
 ADD PRIMARY KEY (`ID`), ADD KEY `FK_USER_ID5` (`owner_id`);

--
-- Indexes for table `photox_invoices`
--
ALTER TABLE `photox_invoices`
 ADD PRIMARY KEY (`ID`), ADD UNIQUE KEY `serie` (`serie`), ADD KEY `FK_USER_ID6` (`owner_id`), ADD KEY `FK_COMPANY_ID1` (`company_id`), ADD KEY `FK_COMPANY_ID2` (`target_company_id`);

--
-- Indexes for table `photox_invoice_lines`
--
ALTER TABLE `photox_invoice_lines`
 ADD PRIMARY KEY (`ID`), ADD KEY `FK_INVOICE_ID1` (`invoice_id`);

--
-- Indexes for table `photox_products`
--
ALTER TABLE `photox_products`
 ADD PRIMARY KEY (`ID`), ADD KEY `FK_PRODUCT_ID2` (`parent_id`);

--
-- Indexes for table `photox_products_prices`
--
ALTER TABLE `photox_products_prices`
 ADD PRIMARY KEY (`ID`), ADD KEY `FK_RAWMATERIAL_ID1` (`rawmaterial_id`), ADD KEY `FK_PRODUCT_ID3` (`product_id`);

--
-- Indexes for table `photox_products_selectors`
--
ALTER TABLE `photox_products_selectors`
 ADD PRIMARY KEY (`id`), ADD KEY `FK_RAWGROUP_ID2` (`rawmaterialgroup_id`), ADD KEY `FK_PRODUCT_ID1` (`product_id`);

--
-- Indexes for table `photox_rawmaterials`
--
ALTER TABLE `photox_rawmaterials`
 ADD PRIMARY KEY (`ID`), ADD KEY `FK_GROUP_ID1` (`parent_group_id`);

--
-- Indexes for table `photox_rawmaterial_groups`
--
ALTER TABLE `photox_rawmaterial_groups`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `photox_resources`
--
ALTER TABLE `photox_resources`
 ADD PRIMARY KEY (`id`), ADD KEY `FK_RESOURCE_ID1` (`parent_id`), ADD KEY `FK_USER_ID3` (`owner_id`);

--
-- Indexes for table `photox_resources_tags`
--
ALTER TABLE `photox_resources_tags`
 ADD PRIMARY KEY (`id`), ADD KEY `FK_RESOURCE_ID2` (`resource_id`), ADD KEY `FK_TAG_ID1` (`tag_id`);

--
-- Indexes for table `photox_settings`
--
ALTER TABLE `photox_settings`
 ADD PRIMARY KEY (`ID`), ADD UNIQUE KEY `setting_key` (`setting_key`);

--
-- Indexes for table `photox_tag_names`
--
ALTER TABLE `photox_tag_names`
 ADD PRIMARY KEY (`id`), ADD UNIQUE KEY `UNIQUE_TAG_NAME` (`name`);

--
-- Indexes for table `photox_templates`
--
ALTER TABLE `photox_templates`
 ADD PRIMARY KEY (`id`), ADD KEY `FK_USER_ID4` (`owner_id`);

--
-- Indexes for table `photox_users`
--
ALTER TABLE `photox_users`
 ADD PRIMARY KEY (`ID`), ADD UNIQUE KEY `uniqueUsername` (`username`), ADD UNIQUE KEY `uniqueLoginName` (`login_name`);

--
-- Indexes for table `photox_users_extra`
--
ALTER TABLE `photox_users_extra`
 ADD PRIMARY KEY (`ID`), ADD KEY `FK_USER_ID6` (`owner_id`);

--
-- Indexes for table `photox_users_logs`
--
ALTER TABLE `photox_users_logs`
 ADD PRIMARY KEY (`id`), ADD KEY `FK_USER_ID2` (`user_id`);

--
-- Indexes for table `photox_user_roles`
--
ALTER TABLE `photox_user_roles`
 ADD PRIMARY KEY (`id`), ADD KEY `FK_USER_ID1` (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `photox_companies`
--
ALTER TABLE `photox_companies`
MODIFY `ID` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `photox_invoices`
--
ALTER TABLE `photox_invoices`
MODIFY `ID` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `photox_invoice_lines`
--
ALTER TABLE `photox_invoice_lines`
MODIFY `ID` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `photox_products`
--
ALTER TABLE `photox_products`
MODIFY `ID` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `photox_products_prices`
--
ALTER TABLE `photox_products_prices`
MODIFY `ID` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `photox_products_selectors`
--
ALTER TABLE `photox_products_selectors`
MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `photox_rawmaterials`
--
ALTER TABLE `photox_rawmaterials`
MODIFY `ID` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `photox_rawmaterial_groups`
--
ALTER TABLE `photox_rawmaterial_groups`
MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `photox_resources`
--
ALTER TABLE `photox_resources`
MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `photox_resources_tags`
--
ALTER TABLE `photox_resources_tags`
MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `photox_settings`
--
ALTER TABLE `photox_settings`
MODIFY `ID` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `photox_tag_names`
--
ALTER TABLE `photox_tag_names`
MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `photox_templates`
--
ALTER TABLE `photox_templates`
MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `photox_users`
--
ALTER TABLE `photox_users`
MODIFY `ID` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `photox_users_logs`
--
ALTER TABLE `photox_users_logs`
MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `photox_user_roles`
--
ALTER TABLE `photox_user_roles`
MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `photox_companies`
--
ALTER TABLE `photox_companies`
ADD CONSTRAINT `FK_USER_ID5` FOREIGN KEY (`owner_id`) REFERENCES `photox_users` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `photox_invoices`
--
ALTER TABLE `photox_invoices`
ADD CONSTRAINT `FK_COMPANY_ID1` FOREIGN KEY (`company_id`) REFERENCES `photox_companies` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `FK_COMPANY_ID2` FOREIGN KEY (`target_company_id`) REFERENCES `photox_companies` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `FK_USER_ID6` FOREIGN KEY (`owner_id`) REFERENCES `photox_users` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `photox_invoice_lines`
--
ALTER TABLE `photox_invoice_lines`
ADD CONSTRAINT `FK_INVOICE_ID1` FOREIGN KEY (`invoice_id`) REFERENCES `photox_invoices` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `photox_products`
--
ALTER TABLE `photox_products`
ADD CONSTRAINT `FK_PRODUCT_ID2` FOREIGN KEY (`parent_id`) REFERENCES `photox_products` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `photox_products_prices`
--
ALTER TABLE `photox_products_prices`
ADD CONSTRAINT `FK_PRODUCT_ID3` FOREIGN KEY (`product_id`) REFERENCES `photox_products` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `FK_RAWMATERIAL_ID1` FOREIGN KEY (`rawmaterial_id`) REFERENCES `photox_rawmaterials` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `photox_products_selectors`
--
ALTER TABLE `photox_products_selectors`
ADD CONSTRAINT `FK_PRODUCT_ID1` FOREIGN KEY (`product_id`) REFERENCES `photox_products` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `FK_RAWGROUP_ID2` FOREIGN KEY (`rawmaterialgroup_id`) REFERENCES `photox_rawmaterial_groups` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `photox_rawmaterials`
--
ALTER TABLE `photox_rawmaterials`
ADD CONSTRAINT `FK_GROUP_ID1` FOREIGN KEY (`parent_group_id`) REFERENCES `photox_rawmaterial_groups` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `photox_resources`
--
ALTER TABLE `photox_resources`
ADD CONSTRAINT `FK_RESOURCE_ID1` FOREIGN KEY (`parent_id`) REFERENCES `photox_resources` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `FK_USER_ID3` FOREIGN KEY (`owner_id`) REFERENCES `photox_users` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `photox_resources_tags`
--
ALTER TABLE `photox_resources_tags`
ADD CONSTRAINT `FK_RESOURCE_ID2` FOREIGN KEY (`resource_id`) REFERENCES `photox_resources` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `FK_TAG_ID1` FOREIGN KEY (`tag_id`) REFERENCES `photox_tag_names` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `photox_templates`
--
ALTER TABLE `photox_templates`
ADD CONSTRAINT `FK_USER_ID4` FOREIGN KEY (`owner_id`) REFERENCES `photox_users` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `photox_users_extra`
--
ALTER TABLE `photox_users_extra`
ADD CONSTRAINT `FK_USER_ID7` FOREIGN KEY (`owner_id`) REFERENCES `photox_users` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `photox_users_logs`
--
ALTER TABLE `photox_users_logs`
ADD CONSTRAINT `FK_USER_ID2` FOREIGN KEY (`user_id`) REFERENCES `photox_users` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `photox_user_roles`
--
ALTER TABLE `photox_user_roles`
ADD CONSTRAINT `FK_USER_ID1` FOREIGN KEY (`user_id`) REFERENCES `photox_users` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
