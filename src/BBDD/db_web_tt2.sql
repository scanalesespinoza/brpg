-- phpMyAdmin SQL Dump
-- version 3.3.2deb1
-- http://www.phpmyadmin.net
--
-- Servidor: localhost
-- Tiempo de generación: 18-11-2010 a las 22:44:51
-- Versión del servidor: 5.1.41
-- Versión de PHP: 5.3.2-1ubuntu4.5

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `db_web_tt2`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `contrincante_habilidad`
--

CREATE TABLE IF NOT EXISTS `contrincante_habilidad` (
  `personaje_id` int(11) NOT NULL DEFAULT '0',
  `habilidad_id` int(11) NOT NULL DEFAULT '0',
  `nivelhabilidad` tinyint(4) NOT NULL,
  PRIMARY KEY (`personaje_id`,`habilidad_id`),
  KEY `contrincante_habilidad_habilidad_id_habilidad_id` (`habilidad_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcar la base de datos para la tabla `contrincante_habilidad`
--

INSERT INTO `contrincante_habilidad` (`personaje_id`, `habilidad_id`, `nivelhabilidad`) VALUES
(1, 1, 1),
(1, 8, 1),
(1, 10, 3),
(1, 11, 1),
(2, 9, 4),
(40, 1, 5),
(40, 3, 9);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cuenta`
--

CREATE TABLE IF NOT EXISTS `cuenta` (
  `id` int(11) NOT NULL DEFAULT '0',
  `estabaneado` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcar la base de datos para la tabla `cuenta`
--


-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `encargo`
--

CREATE TABLE IF NOT EXISTS `encargo` (
  `personaje_id` int(11) NOT NULL DEFAULT '0',
  `mision_id` int(11) NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `rolpersonaje` tinyint(4) NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`personaje_id`,`mision_id`,`created_at`),
  KEY `encargo_mision_id_mision_id` (`mision_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcar la base de datos para la tabla `encargo`
--

INSERT INTO `encargo` (`personaje_id`, `mision_id`, `created_at`, `rolpersonaje`, `updated_at`) VALUES
(2, 1, '2010-10-13 13:24:12', 88, '2010-10-14 17:27:08'),
(2, 2, '2010-10-13 13:11:10', 0, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `habilidad`
--

CREATE TABLE IF NOT EXISTS `habilidad` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) NOT NULL,
  `descripcion` varchar(100) NOT NULL,
  `danobeneficio` smallint(6) NOT NULL,
  `costobasico` smallint(6) NOT NULL,
  `nivelmaximo` smallint(6) NOT NULL,
  `nom_grafico` varchar(20) NOT NULL DEFAULT 'singrafico',
  `tiempoEspera` smallint(4) NOT NULL DEFAULT '2',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=13 ;

--
-- Volcar la base de datos para la tabla `habilidad`
--

INSERT INTO `habilidad` (`id`, `nombre`, `descripcion`, `danobeneficio`, `costobasico`, `nivelmaximo`, `nom_grafico`, `tiempoEspera`) VALUES
(1, 'Golpe simple', 'golpe con el puño', 20, 5, 7, 'singrafico', 2),
(3, 'Bola de fuego', 'Bola de fuego que quema al oponente', -11, 5, 10, 'singrafico', 2),
(4, 'Saeta de hielo', 'Ataque que daña y congela al oponente', -7, 4, 9, 'singrafico', 2),
(5, 'Acuchillar', 'Daño físico moderado', -8, 3, 10, 'singrafico', 2),
(6, 'Curación', 'Cura daño de forma instantánea', 12, 7, 11, 'singrafico', 2),
(7, 'Envenenar', 'Lanza un veneno a tu oponente', -5, 4, 7, 'singrafico', 2),
(8, 'Aturdir', 'Daña físicamente e inhabilita al oponente', -5, 6, 4, 'singrafico', 2),
(9, 'Vendar', 'Cura daño mínimo', 4, 2, 6, 'singrafico', 2),
(10, 'Zancadilla ', 'Golpe a las piernas del oponente', -6, 3, 9, 'singrafico', 2),
(11, 'Maldecir', 'Lanza una maldición de daño al oponente', -7, 3, 8, 'singrafico', 2),
(12, 'Empujón', 'Aleja al oponente sin daño', 0, 2, 4, 'singrafico', 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `inventario`
--

CREATE TABLE IF NOT EXISTS `inventario` (
  `personaje_id` int(11) NOT NULL DEFAULT '0',
  `objeto_id` int(11) NOT NULL DEFAULT '0',
  `cantidad` tinyint(4) NOT NULL DEFAULT '1',
  `estaequipado` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`personaje_id`,`objeto_id`),
  KEY `inventario_objeto_id_objeto_id` (`objeto_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcar la base de datos para la tabla `inventario`
--

INSERT INTO `inventario` (`personaje_id`, `objeto_id`, `cantidad`, `estaequipado`) VALUES
(1, 2, 5, 0),
(1, 6, 1, 0),
(2, 4, 7, 0),
(22, 1, 1, 0),
(22, 6, 1, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `jugador`
--

CREATE TABLE IF NOT EXISTS `jugador` (
  `personaje_id` int(11) NOT NULL DEFAULT '0',
  `vitalidad` smallint(6) NOT NULL,
  `destreza` smallint(6) NOT NULL,
  `sabiduria` smallint(6) NOT NULL,
  `fuerza` smallint(6) NOT NULL,
  `totalpuntoshabilidad` smallint(6) NOT NULL,
  `totalpuntosestadistica` smallint(6) NOT NULL,
  `limitesuperiorexperiencia` int(11) NOT NULL,
  `experiencia` int(11) NOT NULL,
  `pesosoportado` int(11) NOT NULL,
  `fechacreacion` date NOT NULL,
  `estabaneado` tinyint(4) DEFAULT '0',
  `cuenta_id` bigint(20) NOT NULL,
  PRIMARY KEY (`personaje_id`),
  KEY `cuenta_id_idx` (`cuenta_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcar la base de datos para la tabla `jugador`
--

INSERT INTO `jugador` (`personaje_id`, `vitalidad`, `destreza`, `sabiduria`, `fuerza`, `totalpuntoshabilidad`, `totalpuntosestadistica`, `limitesuperiorexperiencia`, `experiencia`, `pesosoportado`, `fechacreacion`, `estabaneado`, `cuenta_id`) VALUES
(1, 1, 1, 1, 1, 20, 1, 50, 0, 20, '2010-10-27', 0, 4),
(22, 1, 1, 1, 1, 1, 1, 50, 0, 20, '2010-10-17', 0, 2),
(26, 1, 1, 1, 1, 1, 1, 50, 0, 20, '2010-10-22', 0, 4),
(30, 1, 1, 1, 1, 1, 1, 50, 0, 20, '2010-10-26', 0, 3),
(36, 1, 1, 1, 1, 1, 1, 50, 0, 20, '2010-10-26', 0, 1),
(37, 1, 1, 1, 1, 1, 1, 50, 0, 20, '2010-10-27', 0, 6),
(38, 1, 1, 1, 1, 1, 1, 50, 0, 20, '2010-10-27', 0, 7),
(39, 1, 1, 1, 1, 1, 1, 50, 0, 20, '2010-11-03', 0, 8);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mision`
--

CREATE TABLE IF NOT EXISTS `mision` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) NOT NULL,
  `descripcion` varchar(100) NOT NULL,
  `nivelrequerido` tinyint(4) NOT NULL,
  `recompensaexp` smallint(6) NOT NULL,
  `repetible` tinyint(4) NOT NULL DEFAULT '0',
  `personaje_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `personaje_id_idx` (`personaje_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

--
-- Volcar la base de datos para la tabla `mision`
--

INSERT INTO `mision` (`id`, `nombre`, `descripcion`, `nivelrequerido`, `recompensaexp`, `repetible`, `personaje_id`) VALUES
(1, 'prueba', 'prueba', 2, 200, 1, NULL),
(2, 'Limpiar el pasto', 'Hey, se me hace imposible poder limpiar todo el desorden que hay en mi jardín!, podrías ayudarme?', 10, 150, 1, NULL),
(3, 'Encontrar la cura!', 'He estado muy enfermo, y solo una medicina en el mundo me ayudará, por suerte...está en nuestra ciud', 0, 10, 0, NULL),
(4, 'El Alcalde', '¡Vaya pués! Has conseguido ayudarme, sé de otra persona que necesita ayuda, él es el alcalde, lider ', 2, 0, 0, NULL),
(5, 'El jardín', 'Hola forastero, ¿me ayudarías en mi jardín? si lo haces, te daré algo de dinero', 2, 3, 0, NULL),
(6, 'Boss', 'El señor de las apariencias, Koh, anda suelto, y podría ser cualquiera de nosotros, ¡Captúralo!', 56, 32000, 0, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mob`
--

CREATE TABLE IF NOT EXISTS `mob` (
  `personaje_id` int(11) NOT NULL DEFAULT '0',
  `vitalidad` smallint(6) NOT NULL,
  `destreza` smallint(6) NOT NULL,
  `sabiduria` smallint(6) NOT NULL,
  `fuerza` smallint(6) NOT NULL,
  `experiencia` int(11) NOT NULL,
  PRIMARY KEY (`personaje_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcar la base de datos para la tabla `mob`
--

INSERT INTO `mob` (`personaje_id`, `vitalidad`, `destreza`, `sabiduria`, `fuerza`, `experiencia`) VALUES
(40, 1, 1, 1, 1, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `objeto`
--

CREATE TABLE IF NOT EXISTS `objeto` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) NOT NULL,
  `descripcion` varchar(100) NOT NULL,
  `tipo` tinyint(4) NOT NULL,
  `peso` smallint(6) NOT NULL,
  `valordinero` smallint(6) NOT NULL,
  `usocombate` tinyint(4) NOT NULL,
  `nom_grafico` varchar(20) NOT NULL DEFAULT 'singrafico',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

--
-- Volcar la base de datos para la tabla `objeto`
--

INSERT INTO `objeto` (`id`, `nombre`, `descripcion`, `tipo`, `peso`, `valordinero`, `usocombate`, `nom_grafico`) VALUES
(1, 'zapato', 'Este item se puede utilizar en el pie\r\n', 1, 15, 2000, 0, 'singrafico'),
(2, 'capa', 'Este item se puede utilizar en la espalda\r\n', 2, 3, 3000, 0, 'singrafico'),
(3, 'Poción curadora', 'Esta poción revitaliza tus puntos de vida', 1, 23, 2, 3, 'singrafico'),
(4, 'Roca pesada', 'Roca pesada para tirarse de un puente', 2, 4900, 5000, 0, 'singrafico'),
(5, 'Pezcado', 'Alimento para curarse paultinamente', 1, 100, 1000, 0, 'singrafico'),
(6, 'Hongo', 'Vegetal que cura enfermedades simples', 0, 1, 5, 1, 'singrafico');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `objeto_mision`
--

CREATE TABLE IF NOT EXISTS `objeto_mision` (
  `mision_id` int(11) NOT NULL DEFAULT '0',
  `objeto_id` int(11) NOT NULL DEFAULT '0',
  `cantidad` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`mision_id`,`objeto_id`),
  KEY `objeto_mision_objeto_id_objeto_id` (`objeto_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcar la base de datos para la tabla `objeto_mision`
--


-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `personaje`
--

CREATE TABLE IF NOT EXISTS `personaje` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) NOT NULL,
  `nivel` tinyint(4) DEFAULT '1',
  `posicionx` smallint(6) DEFAULT '400',
  `posiciony` smallint(6) DEFAULT '400',
  `tipo` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=41 ;

--
-- Volcar la base de datos para la tabla `personaje`
--

INSERT INTO `personaje` (`id`, `nombre`, `nivel`, `posicionx`, `posiciony`, `tipo`) VALUES
(1, 'SergioLove', 1, 544, 629, 0),
(2, 'npc_mision', 1, 400, 400, 1),
(22, 'Sullivan', 1, 1040, 416, 0),
(26, 'NuevoPj', 1, 400, 400, 0),
(30, 'prueba', 12, 400, 400, 0),
(36, 's', 1, 400, 400, 0),
(37, 'a', 1, 400, 400, 0),
(38, 'wenanaty', 1, 400, 400, 0),
(39, 'tulin', 1, 400, 400, 0),
(40, 'Mario', 1, 400, 400, 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sf_guard_forgot_password`
--

CREATE TABLE IF NOT EXISTS `sf_guard_forgot_password` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `unique_key` varchar(255) DEFAULT NULL,
  `expires_at` datetime NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id_idx` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Volcar la base de datos para la tabla `sf_guard_forgot_password`
--


-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sf_guard_group`
--

CREATE TABLE IF NOT EXISTS `sf_guard_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` text,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Volcar la base de datos para la tabla `sf_guard_group`
--


-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sf_guard_group_permission`
--

CREATE TABLE IF NOT EXISTS `sf_guard_group_permission` (
  `group_id` bigint(20) NOT NULL DEFAULT '0',
  `permission_id` bigint(20) NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`group_id`,`permission_id`),
  KEY `sf_guard_group_permission_permission_id_sf_guard_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcar la base de datos para la tabla `sf_guard_group_permission`
--


-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sf_guard_permission`
--

CREATE TABLE IF NOT EXISTS `sf_guard_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` text,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Volcar la base de datos para la tabla `sf_guard_permission`
--


-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sf_guard_remember_key`
--

CREATE TABLE IF NOT EXISTS `sf_guard_remember_key` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `remember_key` varchar(32) DEFAULT NULL,
  `ip_address` varchar(50) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id_idx` (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Volcar la base de datos para la tabla `sf_guard_remember_key`
--

INSERT INTO `sf_guard_remember_key` (`id`, `user_id`, `remember_key`, `ip_address`, `created_at`, `updated_at`) VALUES
(2, 1, '4pcfurfk9im8o4wkw088w0kgok80cww', '200.74.78.64', '2010-10-16 17:18:45', '2010-10-16 17:18:45');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sf_guard_user`
--

CREATE TABLE IF NOT EXISTS `sf_guard_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `email_address` varchar(255) NOT NULL,
  `username` varchar(128) NOT NULL,
  `algorithm` varchar(128) NOT NULL DEFAULT 'sha1',
  `salt` varchar(128) DEFAULT NULL,
  `password` varchar(128) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `is_super_admin` tinyint(1) DEFAULT '0',
  `last_login` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_address` (`email_address`),
  UNIQUE KEY `username` (`username`),
  KEY `is_active_idx_idx` (`is_active`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;

--
-- Volcar la base de datos para la tabla `sf_guard_user`
--

INSERT INTO `sf_guard_user` (`id`, `first_name`, `last_name`, `email_address`, `username`, `algorithm`, `salt`, `password`, `is_active`, `is_super_admin`, `last_login`, `created_at`, `updated_at`) VALUES
(1, 'admin', 'admin', 'Gerald@hotmial.com', 'admin', 'sha1', 'a21af90656458ef0afbbdb43b5b990dc', '73d62e18e6d5a3525a5a283448bd96c7a0575d00', 1, 1, '2010-11-16 19:34:14', '2010-10-11 13:25:09', '2010-11-16 19:34:14'),
(2, 'user', 'user', 'user@user.cl', 'useruser', 'sha1', '877186c1457b6a792633507bf99ab821', '23242c550a43ea76f2051dca95069626669fbc58', 1, 0, '2010-10-17 18:18:40', '2010-10-11 13:29:36', '2010-10-22 09:24:36'),
(3, 'usuario_de_prueba', 'usuario_de_prueba', 'usuario_de_prueba@prueba.cl', 'usuario_de_prueba', 'sha1', '4dc443fb12d51a669c8f5022a839eacc', '7c840160c143433fc00b615539940f98c03d142c', 1, 0, NULL, '2010-10-17 14:02:18', '2010-10-17 14:02:18'),
(4, 'prueba', 'prueba', 'prueba@prueba.com', 'prueba', 'sha1', 'b424afeeca705dc3340272dcf2de319f', '8cd0c800a2c7def06f6ffd6afa514526506f5b65', 1, 0, '2010-10-22 09:06:32', '2010-10-20 23:26:36', '2010-10-22 09:06:32'),
(5, 'pedro', 'eban', 'eban@eban.cl', '1313', 'sha1', NULL, NULL, 1, 0, NULL, '2010-10-26 23:52:35', '2010-10-26 23:52:35'),
(6, 'a', 'a', 'alonsito@gmail.com', 'a', 'sha1', '9548c908fbff7743260ceab8dbb2770c', 'fa237560156a10d03d51627bfd4c702446fef380', 1, 0, '2010-10-27 00:00:15', '2010-10-27 00:00:05', '2010-10-27 00:00:15'),
(7, 'pico', 'lalala', 'psychotic_88@hotmail.es', 'psychotic_88', 'sha1', '106daefcc86f4e7cef069fc3049d6de6', 'b3f1a739f005d0a8fca16214d4db872264388be0', 1, 0, '2010-10-27 13:19:44', '2010-10-27 13:17:50', '2010-10-27 13:19:44'),
(8, 'george', 'jorjato', 'mente.inerte@hotmail.com', 'jorjatopik', 'sha1', '55d6fca8e10dec393485eb272d7c394e', '751bf833a360ea401f1823614aae6251eaab02b8', 1, 0, '2010-11-03 23:54:48', '2010-11-03 23:54:26', '2010-11-03 23:54:48');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sf_guard_user_group`
--

CREATE TABLE IF NOT EXISTS `sf_guard_user_group` (
  `user_id` bigint(20) NOT NULL DEFAULT '0',
  `group_id` bigint(20) NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`user_id`,`group_id`),
  KEY `sf_guard_user_group_group_id_sf_guard_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcar la base de datos para la tabla `sf_guard_user_group`
--


-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sf_guard_user_permission`
--

CREATE TABLE IF NOT EXISTS `sf_guard_user_permission` (
  `user_id` bigint(20) NOT NULL DEFAULT '0',
  `permission_id` bigint(20) NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`user_id`,`permission_id`),
  KEY `sf_guard_user_permission_permission_id_sf_guard_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcar la base de datos para la tabla `sf_guard_user_permission`
--


--
-- Filtros para las tablas descargadas (dump)
--

--
-- Filtros para la tabla `contrincante_habilidad`
--
ALTER TABLE `contrincante_habilidad`
  ADD CONSTRAINT `contrincante_habilidad_ibfk_1` FOREIGN KEY (`personaje_id`) REFERENCES `personaje` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `contrincante_habilidad_ibfk_2` FOREIGN KEY (`habilidad_id`) REFERENCES `habilidad` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `encargo`
--
ALTER TABLE `encargo`
  ADD CONSTRAINT `encargo_ibfk_1` FOREIGN KEY (`personaje_id`) REFERENCES `personaje` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `encargo_ibfk_2` FOREIGN KEY (`mision_id`) REFERENCES `mision` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `inventario`
--
ALTER TABLE `inventario`
  ADD CONSTRAINT `inventario_ibfk_1` FOREIGN KEY (`personaje_id`) REFERENCES `personaje` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `inventario_ibfk_2` FOREIGN KEY (`objeto_id`) REFERENCES `objeto` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `jugador`
--
ALTER TABLE `jugador`
  ADD CONSTRAINT `jugador_ibfk_1` FOREIGN KEY (`cuenta_id`) REFERENCES `sf_guard_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `jugador_ibfk_2` FOREIGN KEY (`personaje_id`) REFERENCES `personaje` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `mision`
--
ALTER TABLE `mision`
  ADD CONSTRAINT `mision_ibfk_1` FOREIGN KEY (`personaje_id`) REFERENCES `personaje` (`id`) ON DELETE SET NULL ON UPDATE SET NULL;

--
-- Filtros para la tabla `mob`
--
ALTER TABLE `mob`
  ADD CONSTRAINT `mob_ibfk_1` FOREIGN KEY (`personaje_id`) REFERENCES `personaje` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `objeto_mision`
--
ALTER TABLE `objeto_mision`
  ADD CONSTRAINT `objeto_mision_mision_id_mision_id` FOREIGN KEY (`mision_id`) REFERENCES `mision` (`id`),
  ADD CONSTRAINT `objeto_mision_objeto_id_objeto_id` FOREIGN KEY (`objeto_id`) REFERENCES `objeto` (`id`);

--
-- Filtros para la tabla `sf_guard_forgot_password`
--
ALTER TABLE `sf_guard_forgot_password`
  ADD CONSTRAINT `sf_guard_forgot_password_user_id_sf_guard_user_id` FOREIGN KEY (`user_id`) REFERENCES `sf_guard_user` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `sf_guard_group_permission`
--
ALTER TABLE `sf_guard_group_permission`
  ADD CONSTRAINT `sf_guard_group_permission_group_id_sf_guard_group_id` FOREIGN KEY (`group_id`) REFERENCES `sf_guard_group` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `sf_guard_group_permission_permission_id_sf_guard_permission_id` FOREIGN KEY (`permission_id`) REFERENCES `sf_guard_permission` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `sf_guard_remember_key`
--
ALTER TABLE `sf_guard_remember_key`
  ADD CONSTRAINT `sf_guard_remember_key_user_id_sf_guard_user_id` FOREIGN KEY (`user_id`) REFERENCES `sf_guard_user` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `sf_guard_user_group`
--
ALTER TABLE `sf_guard_user_group`
  ADD CONSTRAINT `sf_guard_user_group_group_id_sf_guard_group_id` FOREIGN KEY (`group_id`) REFERENCES `sf_guard_group` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `sf_guard_user_group_user_id_sf_guard_user_id` FOREIGN KEY (`user_id`) REFERENCES `sf_guard_user` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `sf_guard_user_permission`
--
ALTER TABLE `sf_guard_user_permission`
  ADD CONSTRAINT `sf_guard_user_permission_permission_id_sf_guard_permission_id` FOREIGN KEY (`permission_id`) REFERENCES `sf_guard_permission` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `sf_guard_user_permission_user_id_sf_guard_user_id` FOREIGN KEY (`user_id`) REFERENCES `sf_guard_user` (`id`) ON DELETE CASCADE;
