-- phpMyAdmin SQL Dump
-- version 3.3.2deb1
-- http://www.phpmyadmin.net
--
-- Servidor: localhost
-- Tiempo de generación: 01-01-2011 a las 21:09:46
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
(18, 1, 2),
(18, 4, 8),
(18, 5, 10),
(18, 6, 11),
(18, 7, 7),
(18, 8, 4),
(19, 1, 4),
(19, 3, 3),
(19, 4, 3),
(19, 5, 2),
(19, 6, 7),
(28, 1, 3),
(28, 3, 7),
(28, 6, 3),
(28, 7, 2),
(31, 1, 2),
(31, 3, 1),
(31, 4, 3),
(31, 6, 1),
(31, 7, 3),
(31, 8, 4),
(31, 10, 4),
(31, 11, 1),
(32, 1, 1);

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
(1, 5, NULL, 4),
(2, 9, NULL, 8),
(3, 15, NULL, 14),
(1, 2, 3, NULL),
(1, 3, 4, 2),
(1, 4, 5, 3),
(2, 6, 7, NULL),
(2, 7, 8, 6),
(2, 8, 9, 8),
(3, 12, 13, NULL),
(3, 13, 14, 12),
(3, 14, 15, 13);

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
(12, 24, NULL),
(3, 11, 1),
(3, 1, 10),
(5, 31, 10),
(3, 10, 11),
(13, 22, 16),
(14, 23, 17),
(14, 17, 18),
(13, 16, 19),
(14, 18, 20),
(13, 19, 21),
(13, 21, 22),
(14, 20, 23),
(5, 10, 25),
(5, 25, 26),
(15, 28, 27),
(15, 27, 28),
(17, 30, 29),
(17, 29, 30),
(5, 26, 31),
(16, 34, 32),
(16, 32, 33),
(16, 33, 34);

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
(3, 2, '2010-11-30 18:08:31', 0, NULL),
(4, 3, '2010-11-30 18:08:37', 0, NULL),
(18, 1, '2010-11-15 23:17:30', 1, NULL),
(18, 3, '2010-11-15 23:16:55', 1, '2010-11-17 00:55:18'),
(18, 3, '2010-11-17 00:55:30', 1, '2010-11-17 00:55:32'),
(18, 3, '2010-11-17 02:43:57', 1, '2010-11-17 02:44:12'),
(19, 1, '2010-11-17 11:40:53', 1, NULL),
(19, 3, '2010-11-17 11:38:59', 1, '2010-11-17 11:39:03'),
(19, 3, '2010-11-17 11:39:09', 1, '2010-11-17 11:39:58'),
(19, 3, '2010-11-17 11:43:00', 1, NULL),
(28, 1, '2010-11-24 21:07:41', 1, NULL),
(28, 3, '2010-11-24 21:11:34', 1, '2010-11-24 21:12:20'),
(28, 3, '2010-11-24 21:16:00', 1, '2010-11-24 21:16:04'),
(28, 3, '2010-11-24 21:16:13', 1, '2010-11-24 21:16:16'),
(28, 3, '2010-11-24 21:16:27', 1, '2010-11-24 21:17:16'),
(28, 3, '2010-11-24 21:17:23', 1, '2010-11-24 21:17:31'),
(28, 3, '2010-11-24 21:17:36', 1, '2010-11-24 21:17:40'),
(28, 3, '2010-11-24 21:17:48', 1, NULL),
(31, 1, '2010-11-17 14:22:51', 1, '2010-11-17 14:36:12'),
(31, 3, '2010-11-17 14:31:46', 1, '2010-11-17 14:32:35'),
(31, 3, '2010-11-17 14:33:16', 1, '2010-11-17 14:33:27'),
(31, 3, '2010-11-17 14:34:34', 1, '2010-11-17 14:34:38');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `equipo`
--

CREATE TABLE IF NOT EXISTS `equipo` (
  `id_objeto` int(6) NOT NULL,
  `vitalidad` int(3) NOT NULL,
  `destreza` int(3) NOT NULL,
  `sabiduria` int(3) NOT NULL,
  `fuerza` int(3) NOT NULL,
  `equipa_en` int(1) NOT NULL,
  PRIMARY KEY (`id_objeto`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcar la base de datos para la tabla `equipo`
--

INSERT INTO `equipo` (`id_objeto`, `vitalidad`, `destreza`, `sabiduria`, `fuerza`, `equipa_en`) VALUES
(1, 1, 1, 1, 1, 5),
(2, 1, 1, 1, 1, 2),
(8, 1, 1, 1, 1, 3),
(9, 12, 0, 0, 0, 4),
(11, 1, 1, 1, 1, 1);

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
(1, 'Golpe simple', 'Golpe con el puño', -5, 0, 7, 'golpesimple', 2),
(3, 'Bola de fuego', 'Bola de fuego que quema al oponente', -11, 3, 10, 'boladefuego', 5),
(4, 'Saeta de hielo', 'Ataque que daña y congela al oponente', -7, 4, 9, 'saetadehielo', 3),
(5, 'Acuchillar', 'Daño físico moderado', -8, 1, 10, 'acuchillar', 2),
(6, 'Curación', 'Cura daño de forma instantánea', 12, 2, 11, 'curacion', 4),
(7, 'Envenenar', 'Lanza un veneno a tu oponente', -5, 4, 7, 'envenenar', 4),
(8, 'Aturdir', 'Daña físicamente e inhabilita al oponente', -10, 10, 4, 'aturdir', 8),
(9, 'Vendar', 'Cura daño mínimo', 8, 2, 6, 'vendar', 10),
(10, 'Zancadilla ', 'Golpe a las piernas del oponente', -6, 3, 9, 'zancadilla', 4),
(11, 'Maldecir', 'Lanza una maldición de daño al oponente', -7, 3, 8, 'maldecir', 2),
(12, 'Empujón', 'Aleja al oponente sin daño', 1, 2, 4, 'empujon', 3);

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
(1, 1, 1, 0),
(1, 6, 1, 0),
(2, 3, 1, 0),
(2, 5, 1, 0),
(2, 8, 1, 0),
(2, 9, 1, 0),
(2, 10, 1, 0),
(2, 11, 1, 0),
(3, 8, 1, 0),
(4, 7, 1, 0),
(6, 1, 1, 0),
(6, 5, 1, 0),
(7, 2, 1, 0),
(7, 4, 1, 0),
(8, 4, 1, 0),
(9, 6, 1, 0),
(10, 6, 1, 0),
(11, 4, 1, 0),
(11, 6, 1, 0),
(12, 7, 1, 0),
(14, 4, 1, 0),
(18, 1, 2, 1),
(18, 2, 5, 1),
(18, 4, 4, 0),
(18, 6, 3, 0),
(18, 7, 4, 0),
(18, 8, 1, 1),
(18, 9, 1, 1),
(18, 11, 1, 1),
(19, 1, 2, 1),
(19, 2, 1, 1),
(19, 4, 1, 0),
(19, 5, 2, 0),
(19, 6, 2, 0),
(19, 8, 2, 1),
(19, 9, 1, 1),
(19, 10, 2, 0),
(19, 11, 1, 1),
(28, 1, 2, 1),
(28, 2, 1, 0),
(28, 3, 2, 0),
(28, 4, 2, 0),
(28, 5, 2, 0),
(28, 7, 5, 0),
(31, 1, 2, 1),
(31, 3, 13, 0),
(31, 4, 1, 0),
(31, 5, 2, 0),
(31, 6, 14, 0),
(31, 8, 1, 1),
(31, 9, 1, 1),
(31, 11, 1, 1);

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
(18, 231, 166, 192, 166, 0, 0, 13477, 9993, 4800, 72549, '2010-12-15', 0, 1),
(19, 83, 52, 52, 52, 0, 0, 367, 262, 3650, 2269, '2010-12-15', 0, 1),
(28, 130, 109, 111, 111, 11, 5, 1096, 176, 4000, 7678, '2010-12-17', 0, 1),
(31, 117, 83, 91, 88, 3, 3, 586, 215, 3800, 8292, '2010-12-17', 0, 4),
(32, 1, 1, 1, 1, 5, 10, 50, 0, 3000, 800, '2010-12-26', 0, 4);

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
  `recompensadinero` int(8) NOT NULL,
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

INSERT INTO `mision` (`id`, `nombre`, `descripcion`, `nivelrequerido`, `recompensaexp`, `recompensadinero`, `repetible`, `personaje_id`, `mision_siguiente_id`) VALUES
(1, '¡De compras!', 'Compra 2 cinceles, 1 martillo , 2 pegamentos y 1 casco de seguridad', 1, 60, 5000, 0, 3, 2),
(2, 'Destruye a Koh', 'Encuentra a Koh en lo mas profundo del pantano, enfréntalo y trae su amuleto', 28, 1000, 20000, 1, 3, NULL),
(3, 'Provisiones', 'Recolecta 3 hongos y 2 pociones, los puedes comprar o tomar de los monstruos', 2, 1000, 1000, 1, 4, NULL);

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
  `beneficio` int(11) NOT NULL DEFAULT '0',
  `nom_grafico` varchar(20) NOT NULL DEFAULT 'singrafico',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=12 ;

--
-- Volcar la base de datos para la tabla `objeto`
--

INSERT INTO `objeto` (`id`, `nombre`, `descripcion`, `tipo`, `peso`, `valordinero`, `usocombate`, `beneficio`, `nom_grafico`) VALUES
(1, 'zapato', 'Este item se puede utilizar en el pie\r\n', 1, 100, 1000, 0, 0, 'zapatos'),
(2, 'capa', 'Este item se puede utilizar en la espalda\r\n', 1, 100, 1500, 0, 0, 'capa'),
(3, 'Poción curadora', 'Esta poción revitaliza tus puntos de vida', 0, 20, 10, 3, 2000, 'pocion'),
(4, 'Roca pesada', 'Roca pesada para tirarse de un puente', 2, 1000, 500, 0, 0, 'roca'),
(5, 'Pescado', 'Alimento para curarse paultinamente', 0, 20, 15, 0, 20, 'pescado'),
(6, 'Hongo', 'Vegetal que cura enfermedades simples', 0, 10, 5, 1, 15, 'hongo'),
(7, 'Amuleto de Koh', 'Muestra fehaciente de tu victoria ante Koh', 2, 15, 3000, 0, 0, 'amuleto'),
(8, 'Cincel', 'Artefacto para tallar', 1, 100, 1200, 1, 50, 'cincel'),
(9, 'Martillo', 'Artefacto para golpear otras piezas', 1, 100, 1500, 1, 50, 'martillo'),
(10, 'Pegamento rápido', 'Une dos piezas por medio de un lazo químico', 2, 25, 100, 0, 0, 'pegamento'),
(11, 'Casco de seguridad', 'Cuida tu cabeza frente a impactos', 1, 80, 1300, 0, 0, 'casco');

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

INSERT INTO `objeto_mision` (`mision_id`, `objeto_id`, `cantidad`) VALUES
(1, 8, 2),
(1, 9, 1),
(1, 10, 2),
(1, 11, 1),
(2, 7, 1),
(3, 3, 2),
(3, 6, 3);

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=33 ;

--
-- Volcar la base de datos para la tabla `personaje`
--

INSERT INTO `personaje` (`id`, `nombre`, `nivel`, `posicionx`, `posiciony`, `tipo`) VALUES
(1, 'Abastos', 1, 1046, 416, 1),
(2, 'Picsa', 10, 1146, 416, 1),
(3, 'Fido', 1, 1008, 832, 2),
(4, 'Miguel', 50, 896, 160, 2),
(5, 'Liz', 6, 496, 320, 2),
(6, 'Esbirro', 1, 1920, 1392, 3),
(7, 'Secuaz', 3, 1728, 1552, 3),
(8, 'Sicario', 8, 2128, 1568, 3),
(9, 'Golpiarela', 15, 2032, 1648, 3),
(10, 'Verdugo', 20, 2192, 1664, 3),
(11, 'Sayón', 25, 2180, 1640, 3),
(12, 'Koh', 30, 2284, 1612, 3),
(13, 'Astán', 40, 1280, 160, 2),
(14, 'kain', 40, 1280, 336, 2),
(15, 'Hipólito', 5, 208, 144, 2),
(16, 'Cotmally', 12, 144, 832, 2),
(17, 'Mika', 20, 160, 1760, 2),
(18, 'Gerald', 37, 2181, 1568, 0),
(19, 'Sullivan', 14, 2372, 1548, 0),
(28, 'Baal', 21, 2341, 1109, 0),
(31, 'Sergio', 17, 208, 208, 0),
(32, 'asd', 1, 1312, 336, 0);

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Volcar la base de datos para la tabla `sf_guard_remember_key`
--


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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Volcar la base de datos para la tabla `sf_guard_user`
--

INSERT INTO `sf_guard_user` (`id`, `first_name`, `last_name`, `email_address`, `username`, `algorithm`, `salt`, `password`, `is_active`, `is_super_admin`, `last_login`, `created_at`, `updated_at`) VALUES
(1, 'admin', 'admin', 'Gerald@hotmial.com', 'admin', 'sha1', 'a21af90656458ef0afbbdb43b5b990dc', '73d62e18e6d5a3525a5a283448bd96c7a0575d00', 1, 1, '2010-12-26 16:52:16', '2010-10-11 13:25:09', '2010-12-26 16:52:16'),
(4, 'Sergio', 'Sergio', 'sergio.canales.e@gmail.com', 'Sergio', 'sha1', '10b6d4c26014442bb51078aa9bf2fbdb', '1d4d4b909c6690e4dd6e67b019a4fad81f7c10b6', 1, 0, '2010-12-26 16:45:33', '2010-12-17 14:10:33', '2010-12-26 16:45:33');

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=35 ;

--
-- Volcar la base de datos para la tabla `texto`
--

INSERT INTO `texto` (`id`, `texto`) VALUES
(1, 'Estoy ocupado, no puedo hablar'),
(2, 'Buen dia, estoy algo ocupado acá, necesito que compres algo ¿Me ayudas?'),
(3, 'Vaya, las diligencias en este pueblo son muchas, ¿Cumpliste mi encargo?'),
(4, 'Mmmmm... veo que no...¡Anímate! no es muy difícil'),
(5, 'Bien, ahora podré seguir con mis asuntos..¡Gracias!'),
(6, 'Necesito un favor mas, anda un ente extraño que aterra a la ciudad, su nombre es Koh ...¡Destrúyelo!'),
(7, 'Tu valentía es admirable ¿Cómo te fue?'),
(8, 'Vaya, no te desanimes...Koh es difícil de vencer, ha estado tiempo aquí y se ha fortalecido mucho, es conveniente que tu lo hagas también'),
(9, '¡Que honra tenerte en mi pueblo!..Muchas gracias por destruir a Koh y terminar esta presentación de Trabajo de Título'),
(10, 'Recuérda combatir con monstruos para fortalecer tus capacidades'),
(11, 'Hola, ¿Cómo estás?'),
(12, 'Hola, debo juntar hongos y pociones, mientras más ...¡Mejor! ...¿Te apuntas?'),
(13, 'Has vuelto, ¿Cómo te fue?'),
(14, 'Uff ...No son suficientes, trae más'),
(15, 'Justo... ¡Muchas Gracias!'),
(16, 'Si tienes puntos de habilidad, deberías utilizarlos con sabiduría. Puedes hacerlo en el menú de habilidades presionando la letra "H"'),
(17, 'Si tienes puntos de estadísticas, deberías utilizarlos con sabiduría. Puedes hacerlo en el menú de estadísticas presionando la letra "E"'),
(18, 'Al subir de nivel, obtienes automáticamente puntos de estadísticas y habilidades para distribuirlos según te parezca'),
(19, 'Para comenzar una batalla solo debes acercarte a algún monstruo'),
(20, 'Mantente con vida durante un combate, ya sea con ítems o habilidades de sanación'),
(21, 'Si eres vencido en combate, te llevarán a la ciudad para que estés a salvo'),
(22, 'Si eres el vencedor de un combate, no olvides hacerte del botín'),
(23, 'Usa tus habilidades con inteligencia , ya que tienen un costo de ejecución y un tiempo de reutilización asociados que pueden variar el resultado de un combate'),
(24, 'Puedes activar y desactivar el sonido en el menú opciones apretando la letra "O"'),
(25, 'Puedes gestionar las misiones que te han encomendado en el menú de misiones presionando la tecla "M"'),
(26, 'Puedes eliminar items de tu inventario pulsando la tecla "B" y presionando un icono del inventario'),
(27, 'Puedes equipar items desde tu inventario haciendo click en un icono de la pestaña equipo'),
(28, 'También puedes desequiparlos presionando el botón "Vestir" ubicado en el panel lateral, y luego presionando los iconos de la ventana "Vestimenta"'),
(29, 'Agradecimientos : Jorge Castro (Música) ,Joshua Semann(Edición de gráficos) , Iván Araya (Gráficos)'),
(30, 'Creadores: Sergio Canales Espinoza , Gerald Schmidt Padilla'),
(31, 'Para utilizar un ítem, puedes hacer click en él y , dependiendo de si se puede usar en un determinado estado, actualizará tu situación'),
(32, 'Para comprar items , debes interactuar con un vendedor, él te mostrará una ventana con su mercancía, si tienes suficiente dinero y soportas suficiente peso, podrás comprar'),
(33, 'Para vender items, debes interactuar con un vendedor, cuando él te ofrezca su mercancía, puedes hacer click en tus objetos y estos serán vendidos , en consecuencia liberarás peso y ganarás dinero'),
(34, 'Después de ganar una batalla o cumplir con una misión , puedes obtener un una cantidad de objetos asociados al personaje con el que interactuaste');

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
-- Filtros para la tabla `equipo`
--
ALTER TABLE `equipo`
  ADD CONSTRAINT `equipo_ibfk_1` FOREIGN KEY (`id_objeto`) REFERENCES `objeto` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

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
