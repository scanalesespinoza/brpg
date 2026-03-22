-- phpMyAdmin SQL Dump
-- version 3.3.2deb1
-- http://www.phpmyadmin.net
--
-- Servidor: localhost
-- Tiempo de generación: 03-12-2010 a las 18:43:19
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
CREATE DATABASE `db_web_tt2` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `db_web_tt2`;

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
(6, 1, 2),
(7, 4, 4),
(8, 1, 5),
(9, 4, 4),
(9, 6, 1),
(10, 1, 5),
(10, 3, 7),
(11, 1, 5),
(11, 3, 3),
(12, 1, 7),
(12, 3, 10),
(12, 4, 9),
(12, 6, 11),
(13, 1, 1);

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
-- Estructura de tabla para la tabla `dialogo_mision`
--

CREATE TABLE IF NOT EXISTS `dialogo_mision` (
  `mision_id` int(11) NOT NULL,
  `texto_id` int(11) NOT NULL,
  `texto_siguiente_id` int(11) DEFAULT NULL,
  `texto_anterior_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`mision_id`,`texto_id`),
  KEY `personaje_siguiente_id` (`texto_siguiente_id`,`texto_anterior_id`),
  KEY `texto_anterior_id` (`texto_anterior_id`),
  KEY `texto_siguiente_id` (`texto_siguiente_id`),
  KEY `texto_id` (`texto_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcar la base de datos para la tabla `dialogo_mision`
--

INSERT INTO `dialogo_mision` (`mision_id`, `texto_id`, `texto_siguiente_id`, `texto_anterior_id`) VALUES
(1, 4, NULL, 3),
(2, 9, NULL, 8),
(1, 1, 2, NULL),
(1, 2, 3, 1),
(1, 3, 4, 2),
(2, 6, 7, NULL),
(2, 7, 8, 6),
(2, 8, 9, 8);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `dialogo_personaje`
--

CREATE TABLE IF NOT EXISTS `dialogo_personaje` (
  `personaje_id` int(11) NOT NULL,
  `texto_id` int(11) NOT NULL,
  `texto_siguiente_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`personaje_id`,`texto_id`),
  KEY `texto_id` (`texto_id`),
  KEY `texto_siguiente_id` (`texto_siguiente_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcar la base de datos para la tabla `dialogo_personaje`
--

INSERT INTO `dialogo_personaje` (`personaje_id`, `texto_id`, `texto_siguiente_id`) VALUES
(4, 11, NULL),
(5, 10, NULL),
(3, 11, 1),
(3, 1, 10),
(3, 10, 11);

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
(3, 1, '2010-11-30 18:07:18', 0, NULL),
(4, 2, '2010-11-30 18:08:31', 0, NULL),
(4, 3, '2010-11-30 18:08:37', 0, NULL);

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
(1, 'Golpe simple', 'golpe con el puño', -10, 5, 7, 'golpesimple', 2),
(3, 'Bola de fuego', 'Bola de fuego que quema al oponente', -11, 5, 10, 'boladefuego', 2),
(4, 'Saeta de hielo', 'Ataque que daña y congela al oponente', -7, 4, 9, 'saetadehielo', 2),
(5, 'Acuchillar', 'Daño físico moderado', -8, 3, 10, 'singrafico', 2),
(6, 'Curación', 'Cura daño de forma instantánea', 12, 7, 11, 'curacion', 2),
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
(1, 1, 4, 0),
(1, 6, 15, 0),
(2, 5, 1, 0),
(6, 1, 1, 0),
(6, 5, 1, 0),
(13, 4, 2, 0);

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
  `dinero` int(8) NOT NULL DEFAULT '0',
  `fechacreacion` date NOT NULL,
  `estabaneado` tinyint(4) DEFAULT '0',
  `cuenta_id` bigint(20) NOT NULL,
  PRIMARY KEY (`personaje_id`),
  KEY `cuenta_id_idx` (`cuenta_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcar la base de datos para la tabla `jugador`
--

INSERT INTO `jugador` (`personaje_id`, `vitalidad`, `destreza`, `sabiduria`, `fuerza`, `totalpuntoshabilidad`, `totalpuntosestadistica`, `limitesuperiorexperiencia`, `experiencia`, `pesosoportado`, `dinero`, `fechacreacion`, `estabaneado`, `cuenta_id`) VALUES
(13, 1, 1, 1, 1, 10, 4, 50, 0, 20, 1000, '2010-11-30', 0, 1);

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
  `mision_siguiente_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `personaje_id_idx` (`personaje_id`),
  KEY `mision_siguiente_id` (`mision_siguiente_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Volcar la base de datos para la tabla `mision`
--

INSERT INTO `mision` (`id`, `nombre`, `descripcion`, `nivelrequerido`, `recompensaexp`, `repetible`, `personaje_id`, `mision_siguiente_id`) VALUES
(1, '¡De compras!', 'Compra un par de zapatos y una capa', 1, 60, 0, 3, 2),
(2, 'Criaturas extrañas', 'Destruye la amenaza que aterra al pueblo', 28, 1000, 1, 3, NULL),
(3, 'Cosechar', 'Busca hongos en la humedad de las rocas', 2, 1000, 1, 4, NULL);

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
  `dinero` int(8) NOT NULL DEFAULT '5',
  PRIMARY KEY (`personaje_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcar la base de datos para la tabla `mob`
--

INSERT INTO `mob` (`personaje_id`, `vitalidad`, `destreza`, `sabiduria`, `fuerza`, `experiencia`, `dinero`) VALUES
(6, 1, 1, 1, 1, 50, 5),
(7, 3, 2, 6, 4, 30, 30),
(8, 6, 7, 4, 5, 250, 600),
(9, 13, 5, 8, 1, 500, 1000),
(10, 5, 13, 12, 16, 800, 1250),
(11, 15, 13, 20, 18, 1000, 1345),
(12, 20, 20, 20, 20, 4000, 5000);

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
(1, 'zapato', 'Este item se puede utilizar en el pie\r\n', 1, 15, 1000, 0, 'zapatos'),
(2, 'capa', 'Este item se puede utilizar en la espalda\r\n', 2, 3, 30, 0, 'singrafico'),
(3, 'Poción curadora', 'Esta poción revitaliza tus puntos de vida', 1, 23, 2, 3, 'pocion'),
(4, 'Roca pesada', 'Roca pesada para tirarse de un puente', 2, 4900, 5000, 0, 'singrafico'),
(5, 'Pescado', 'Alimento para curarse paultinamente', 1, 100, 1000, 0, 'pescado'),
(6, 'Hongo', 'Vegetal que cura enfermedades simples', 0, 1, 5, 1, 'hongo');

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=14 ;

--
-- Volcar la base de datos para la tabla `personaje`
--

INSERT INTO `personaje` (`id`, `nombre`, `nivel`, `posicionx`, `posiciony`, `tipo`) VALUES
(1, 'Abastos', 1, 1046, 416, 1),
(2, 'Picsa', 10, 1146, 416, 1),
(3, 'Fido', 1, 200, 272, 2),
(4, 'Miguel', 50, 400, 272, 2),
(5, 'Liz', 6, 500, 276, 2),
(6, 'Esbirro', 1, 1750, 200, 3),
(7, 'Secuaz', 3, 1446, 200, 3),
(8, 'Sicario', 8, 1546, 200, 3),
(9, 'Golpiarela', 15, 1746, 200, 3),
(10, 'Verdugo', 20, 1850, 200, 3),
(11, 'Sayón', 25, 1950, 200, 3),
(12, 'Koh', 30, 2000, 200, 3),
(13, 'Gerald', 1, 544, 629, 0);

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
(1, 'admin', 'admin', 'Gerald@hotmial.com', 'admin', 'sha1', 'a21af90656458ef0afbbdb43b5b990dc', '73d62e18e6d5a3525a5a283448bd96c7a0575d00', 1, 1, '2010-12-03 11:54:14', '2010-10-11 13:25:09', '2010-12-03 11:54:14'),
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


-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `texto`
--

CREATE TABLE IF NOT EXISTS `texto` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `texto` varchar(200) NOT NULL DEFAULT 'estoy ocupado, no puedo hablar',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=12 ;

--
-- Volcar la base de datos para la tabla `texto`
--

INSERT INTO `texto` (`id`, `texto`) VALUES
(1, 'Estoy ocupado, no puedo hablar'),
(2, 'Buen dia, estoy algo ocupado acá, necesito que compres algo ¿Me ayudas?'),
(3, 'Vaya, las diligencias en este pueblo son muchas, ¿Cumpliste mi encargo?'),
(4, 'Mmmmm... veo que no...¡Anímate! no es muy difícil'),
(5, 'Bien, ahora podré seguir con mis asuntos..¡Gracias!'),
(6, 'Necesito un favor mas, anda un ente extraño que aterra a la ciudad ...¡Destrúyelo!'),
(7, 'Tu valentía es admirable ¿Cómo te fue?'),
(8, 'Vaya, no te desanimes...Koh es difícil de vencer, ha estado mucho tiempo aquí y se ha fortalecido mucho, es conveniente que tu lo hagas también'),
(9, '¡Que honra tenerte en mi pueblo!..Muchas gracias por destruir a Koh y terminar esta presentación de Trabajo de Título'),
(10, 'Recuérda combatir con monstruos para fortalecer tus capacidades'),
(11, 'Hola, ¿Cómo estás?');

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
-- Filtros para la tabla `dialogo_mision`
--
ALTER TABLE `dialogo_mision`
  ADD CONSTRAINT `dialogo_mision_ibfk_1` FOREIGN KEY (`mision_id`) REFERENCES `mision` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `dialogo_mision_ibfk_2` FOREIGN KEY (`texto_id`) REFERENCES `texto` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `dialogo_mision_ibfk_4` FOREIGN KEY (`texto_siguiente_id`) REFERENCES `dialogo_mision` (`texto_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `dialogo_mision_ibfk_6` FOREIGN KEY (`texto_anterior_id`) REFERENCES `dialogo_mision` (`texto_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `dialogo_personaje`
--
ALTER TABLE `dialogo_personaje`
  ADD CONSTRAINT `dialogo_personaje_ibfk_1` FOREIGN KEY (`personaje_id`) REFERENCES `personaje` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `dialogo_personaje_ibfk_2` FOREIGN KEY (`texto_id`) REFERENCES `texto` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `dialogo_personaje_ibfk_4` FOREIGN KEY (`texto_siguiente_id`) REFERENCES `dialogo_personaje` (`texto_id`) ON DELETE CASCADE ON UPDATE CASCADE;

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
  ADD CONSTRAINT `mision_ibfk_1` FOREIGN KEY (`personaje_id`) REFERENCES `personaje` (`id`) ON DELETE SET NULL ON UPDATE SET NULL,
  ADD CONSTRAINT `mision_ibfk_2` FOREIGN KEY (`mision_siguiente_id`) REFERENCES `mision` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

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
