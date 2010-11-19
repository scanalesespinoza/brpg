-- phpMyAdmin SQL Dump
-- version 2.10.3
-- http://www.phpmyadmin.net
-- 
-- Servidor: localhost
-- Tiempo de generación: 19-11-2010 a las 16:48:07
-- Versión del servidor: 5.0.51
-- Versión de PHP: 5.2.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

-- 
-- Base de datos: `db_web_tt2`
-- 
CREATE DATABASE `db_web_tt2` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `db_web_tt2`;

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `contrincante_habilidad`
-- 

CREATE TABLE `contrincante_habilidad` (
  `personaje_id` int(11) NOT NULL default '0',
  `habilidad_id` int(11) NOT NULL default '0',
  `nivelhabilidad` tinyint(4) NOT NULL,
  PRIMARY KEY  (`personaje_id`,`habilidad_id`),
  KEY `contrincante_habilidad_habilidad_id_habilidad_id` (`habilidad_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- 
-- Volcar la base de datos para la tabla `contrincante_habilidad`
-- 

INSERT INTO `contrincante_habilidad` VALUES (1, 1, 1);
INSERT INTO `contrincante_habilidad` VALUES (1, 8, 1);
INSERT INTO `contrincante_habilidad` VALUES (1, 10, 3);
INSERT INTO `contrincante_habilidad` VALUES (1, 11, 1);
INSERT INTO `contrincante_habilidad` VALUES (2, 9, 4);
INSERT INTO `contrincante_habilidad` VALUES (40, 1, 5);
INSERT INTO `contrincante_habilidad` VALUES (40, 3, 9);

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `cuenta`
-- 

CREATE TABLE `cuenta` (
  `id` int(11) NOT NULL default '0',
  `estabaneado` tinyint(4) default '0',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- 
-- Volcar la base de datos para la tabla `cuenta`
-- 


-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `encargo`
-- 

CREATE TABLE `encargo` (
  `personaje_id` int(11) NOT NULL default '0',
  `mision_id` int(11) NOT NULL default '0',
  `created_at` datetime NOT NULL default '0000-00-00 00:00:00',
  `rolpersonaje` tinyint(4) NOT NULL,
  `updated_at` datetime default NULL,
  PRIMARY KEY  (`personaje_id`,`mision_id`,`created_at`),
  KEY `encargo_mision_id_mision_id` (`mision_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- 
-- Volcar la base de datos para la tabla `encargo`
-- 

INSERT INTO `encargo` VALUES (2, 1, '2010-10-13 13:24:12', 88, '2010-10-14 17:27:08');
INSERT INTO `encargo` VALUES (2, 2, '2010-10-13 13:11:10', 0, NULL);

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `habilidad`
-- 

CREATE TABLE `habilidad` (
  `id` int(11) NOT NULL auto_increment,
  `nombre` varchar(20) NOT NULL,
  `descripcion` varchar(100) NOT NULL,
  `danobeneficio` smallint(6) NOT NULL,
  `costobasico` smallint(6) NOT NULL,
  `nivelmaximo` smallint(6) NOT NULL,
  `nom_grafico` varchar(20) NOT NULL default 'singrafico',
  `tiempoEspera` smallint(4) NOT NULL default '2',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=13 ;

-- 
-- Volcar la base de datos para la tabla `habilidad`
-- 

INSERT INTO `habilidad` VALUES (1, 'Golpe simple', 'golpe con el puño', 20, 5, 7, 'singrafico', 2);
INSERT INTO `habilidad` VALUES (3, 'Bola de fuego', 'Bola de fuego que quema al oponente', -11, 5, 10, 'singrafico', 2);
INSERT INTO `habilidad` VALUES (4, 'Saeta de hielo', 'Ataque que daña y congela al oponente', -7, 4, 9, 'singrafico', 2);
INSERT INTO `habilidad` VALUES (5, 'Acuchillar', 'Daño físico moderado', -8, 3, 10, 'singrafico', 2);
INSERT INTO `habilidad` VALUES (6, 'Curación', 'Cura daño de forma instantánea', 12, 7, 11, 'singrafico', 2);
INSERT INTO `habilidad` VALUES (7, 'Envenenar', 'Lanza un veneno a tu oponente', -5, 4, 7, 'singrafico', 2);
INSERT INTO `habilidad` VALUES (8, 'Aturdir', 'Daña físicamente e inhabilita al oponente', -5, 6, 4, 'singrafico', 2);
INSERT INTO `habilidad` VALUES (9, 'Vendar', 'Cura daño mínimo', 4, 2, 6, 'singrafico', 2);
INSERT INTO `habilidad` VALUES (10, 'Zancadilla ', 'Golpe a las piernas del oponente', -6, 3, 9, 'singrafico', 2);
INSERT INTO `habilidad` VALUES (11, 'Maldecir', 'Lanza una maldición de daño al oponente', -7, 3, 8, 'singrafico', 2);
INSERT INTO `habilidad` VALUES (12, 'Empujón', 'Aleja al oponente sin daño', 0, 2, 4, 'singrafico', 2);

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `inventario`
-- 

CREATE TABLE `inventario` (
  `personaje_id` int(11) NOT NULL default '0',
  `objeto_id` int(11) NOT NULL default '0',
  `cantidad` tinyint(4) NOT NULL default '1',
  `estaequipado` tinyint(4) NOT NULL default '0',
  PRIMARY KEY  (`personaje_id`,`objeto_id`),
  KEY `inventario_objeto_id_objeto_id` (`objeto_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- 
-- Volcar la base de datos para la tabla `inventario`
-- 

INSERT INTO `inventario` VALUES (1, 2, 5, 0);
INSERT INTO `inventario` VALUES (1, 6, 1, 0);
INSERT INTO `inventario` VALUES (2, 4, 7, 0);
INSERT INTO `inventario` VALUES (22, 1, 1, 0);
INSERT INTO `inventario` VALUES (22, 6, 1, 0);

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `jugador`
-- 

CREATE TABLE `jugador` (
  `personaje_id` int(11) NOT NULL default '0',
  `vitalidad` smallint(6) NOT NULL,
  `destreza` smallint(6) NOT NULL,
  `sabiduria` smallint(6) NOT NULL,
  `fuerza` smallint(6) NOT NULL,
  `totalpuntoshabilidad` smallint(6) NOT NULL,
  `totalpuntosestadistica` smallint(6) NOT NULL,
  `limitesuperiorexperiencia` int(11) NOT NULL,
  `experiencia` int(11) NOT NULL,
  `dinero` int(8) NOT NULL default '0',
  `pesosoportado` int(11) NOT NULL,
  `fechacreacion` date NOT NULL,
  `estabaneado` tinyint(4) default '0',
  `cuenta_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`personaje_id`),
  KEY `cuenta_id_idx` (`cuenta_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- 
-- Volcar la base de datos para la tabla `jugador`
-- 

INSERT INTO `jugador` VALUES (1, 1, 1, 1, 1, 20, 1, 50, 0, 50, 20, '2010-10-27', 0, 4);
INSERT INTO `jugador` VALUES (22, 1, 1, 1, 1, 1, 1, 50, 0, 0, 20, '2010-10-17', 0, 2);
INSERT INTO `jugador` VALUES (26, 1, 1, 1, 1, 1, 1, 50, 0, 0, 20, '2010-10-22', 0, 4);
INSERT INTO `jugador` VALUES (30, 1, 1, 1, 1, 1, 1, 50, 0, 0, 20, '2010-10-26', 0, 3);
INSERT INTO `jugador` VALUES (36, 1, 1, 1, 1, 1, 1, 50, 0, 0, 20, '2010-10-26', 0, 1);
INSERT INTO `jugador` VALUES (37, 1, 1, 1, 1, 1, 1, 50, 0, 0, 20, '2010-10-27', 0, 6);
INSERT INTO `jugador` VALUES (38, 1, 1, 1, 1, 1, 1, 50, 0, 0, 20, '2010-10-27', 0, 7);
INSERT INTO `jugador` VALUES (39, 1, 1, 1, 1, 1, 1, 50, 0, 0, 20, '2010-11-03', 0, 8);

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `mision`
-- 

CREATE TABLE `mision` (
  `id` int(11) NOT NULL auto_increment,
  `nombre` varchar(20) NOT NULL,
  `descripcion` varchar(100) NOT NULL,
  `nivelrequerido` tinyint(4) NOT NULL,
  `recompensaexp` smallint(6) NOT NULL,
  `repetible` tinyint(4) NOT NULL default '0',
  `personaje_id` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `personaje_id_idx` (`personaje_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

-- 
-- Volcar la base de datos para la tabla `mision`
-- 

INSERT INTO `mision` VALUES (1, 'prueba', 'prueba', 2, 200, 1, NULL);
INSERT INTO `mision` VALUES (2, 'Limpiar el pasto', 'Hey, se me hace imposible poder limpiar todo el desorden que hay en mi jardín!, podrías ayudarme?', 10, 150, 1, NULL);
INSERT INTO `mision` VALUES (3, 'Encontrar la cura!', 'He estado muy enfermo, y solo una medicina en el mundo me ayudará, por suerte...está en nuestra ciud', 0, 10, 0, NULL);
INSERT INTO `mision` VALUES (4, 'El Alcalde', '¡Vaya pués! Has conseguido ayudarme, sé de otra persona que necesita ayuda, él es el alcalde, lider ', 2, 0, 0, NULL);
INSERT INTO `mision` VALUES (5, 'El jardín', 'Hola forastero, ¿me ayudarías en mi jardín? si lo haces, te daré algo de dinero', 2, 3, 0, NULL);
INSERT INTO `mision` VALUES (6, 'Boss', 'El señor de las apariencias, Koh, anda suelto, y podría ser cualquiera de nosotros, ¡Captúralo!', 56, 32000, 0, NULL);

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `mob`
-- 

CREATE TABLE `mob` (
  `personaje_id` int(11) NOT NULL default '0',
  `vitalidad` smallint(6) NOT NULL,
  `destreza` smallint(6) NOT NULL,
  `sabiduria` smallint(6) NOT NULL,
  `fuerza` smallint(6) NOT NULL,
  `experiencia` int(11) NOT NULL,
  PRIMARY KEY  (`personaje_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- 
-- Volcar la base de datos para la tabla `mob`
-- 

INSERT INTO `mob` VALUES (40, 1, 1, 1, 1, 0);

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `objeto`
-- 

CREATE TABLE `objeto` (
  `id` int(11) NOT NULL auto_increment,
  `nombre` varchar(20) NOT NULL,
  `descripcion` varchar(100) NOT NULL,
  `tipo` tinyint(4) NOT NULL,
  `peso` smallint(6) NOT NULL,
  `valordinero` smallint(6) NOT NULL,
  `usocombate` tinyint(4) NOT NULL,
  `nom_grafico` varchar(20) NOT NULL default 'singrafico',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

-- 
-- Volcar la base de datos para la tabla `objeto`
-- 

INSERT INTO `objeto` VALUES (1, 'zapato', 'Este item se puede utilizar en el pie\r\n', 1, 15, 2000, 0, 'singrafico');
INSERT INTO `objeto` VALUES (2, 'capa', 'Este item se puede utilizar en la espalda\r\n', 2, 3, 3000, 0, 'singrafico');
INSERT INTO `objeto` VALUES (3, 'Poción curadora', 'Esta poción revitaliza tus puntos de vida', 1, 23, 2, 3, 'singrafico');
INSERT INTO `objeto` VALUES (4, 'Roca pesada', 'Roca pesada para tirarse de un puente', 2, 4900, 5000, 0, 'singrafico');
INSERT INTO `objeto` VALUES (5, 'Pezcado', 'Alimento para curarse paultinamente', 1, 100, 1000, 0, 'singrafico');
INSERT INTO `objeto` VALUES (6, 'Hongo', 'Vegetal que cura enfermedades simples', 0, 1, 5, 1, 'singrafico');

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `objeto_mision`
-- 

CREATE TABLE `objeto_mision` (
  `mision_id` int(11) NOT NULL default '0',
  `objeto_id` int(11) NOT NULL default '0',
  `cantidad` tinyint(4) NOT NULL default '1',
  PRIMARY KEY  (`mision_id`,`objeto_id`),
  KEY `objeto_mision_objeto_id_objeto_id` (`objeto_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- 
-- Volcar la base de datos para la tabla `objeto_mision`
-- 


-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `personaje`
-- 

CREATE TABLE `personaje` (
  `id` int(11) NOT NULL auto_increment,
  `nombre` varchar(20) NOT NULL,
  `nivel` tinyint(4) default '1',
  `posicionx` smallint(6) default '400',
  `posiciony` smallint(6) default '400',
  `tipo` tinyint(4) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=41 ;

-- 
-- Volcar la base de datos para la tabla `personaje`
-- 

INSERT INTO `personaje` VALUES (1, 'SergioLove', 1, 544, 629, 0);
INSERT INTO `personaje` VALUES (2, 'npc_mision', 1, 400, 400, 1);
INSERT INTO `personaje` VALUES (22, 'Sullivan', 1, 1040, 416, 1);
INSERT INTO `personaje` VALUES (26, 'NuevoPj', 1, 400, 400, 0);
INSERT INTO `personaje` VALUES (30, 'prueba', 12, 400, 400, 0);
INSERT INTO `personaje` VALUES (36, 's', 1, 400, 400, 0);
INSERT INTO `personaje` VALUES (37, 'a', 1, 400, 400, 0);
INSERT INTO `personaje` VALUES (38, 'wenanaty', 1, 400, 400, 0);
INSERT INTO `personaje` VALUES (39, 'tulin', 1, 400, 400, 0);
INSERT INTO `personaje` VALUES (40, 'Mario', 1, 400, 400, 3);

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `sf_guard_forgot_password`
-- 

CREATE TABLE `sf_guard_forgot_password` (
  `id` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) NOT NULL,
  `unique_key` varchar(255) default NULL,
  `expires_at` datetime NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `user_id_idx` (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- 
-- Volcar la base de datos para la tabla `sf_guard_forgot_password`
-- 


-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `sf_guard_group`
-- 

CREATE TABLE `sf_guard_group` (
  `id` bigint(20) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `description` text,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- 
-- Volcar la base de datos para la tabla `sf_guard_group`
-- 


-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `sf_guard_group_permission`
-- 

CREATE TABLE `sf_guard_group_permission` (
  `group_id` bigint(20) NOT NULL default '0',
  `permission_id` bigint(20) NOT NULL default '0',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY  (`group_id`,`permission_id`),
  KEY `sf_guard_group_permission_permission_id_sf_guard_permission_id` (`permission_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- 
-- Volcar la base de datos para la tabla `sf_guard_group_permission`
-- 


-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `sf_guard_permission`
-- 

CREATE TABLE `sf_guard_permission` (
  `id` bigint(20) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `description` text,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- 
-- Volcar la base de datos para la tabla `sf_guard_permission`
-- 


-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `sf_guard_remember_key`
-- 

CREATE TABLE `sf_guard_remember_key` (
  `id` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) default NULL,
  `remember_key` varchar(32) default NULL,
  `ip_address` varchar(50) default NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `user_id_idx` (`user_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

-- 
-- Volcar la base de datos para la tabla `sf_guard_remember_key`
-- 

INSERT INTO `sf_guard_remember_key` VALUES (2, 1, '4pcfurfk9im8o4wkw088w0kgok80cww', '200.74.78.64', '2010-10-16 17:18:45', '2010-10-16 17:18:45');

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `sf_guard_user`
-- 

CREATE TABLE `sf_guard_user` (
  `id` bigint(20) NOT NULL auto_increment,
  `first_name` varchar(255) default NULL,
  `last_name` varchar(255) default NULL,
  `email_address` varchar(255) NOT NULL,
  `username` varchar(128) NOT NULL,
  `algorithm` varchar(128) NOT NULL default 'sha1',
  `salt` varchar(128) default NULL,
  `password` varchar(128) default NULL,
  `is_active` tinyint(1) default '1',
  `is_super_admin` tinyint(1) default '0',
  `last_login` datetime default NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `email_address` (`email_address`),
  UNIQUE KEY `username` (`username`),
  KEY `is_active_idx_idx` (`is_active`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;

-- 
-- Volcar la base de datos para la tabla `sf_guard_user`
-- 

INSERT INTO `sf_guard_user` VALUES (1, 'admin', 'admin', 'Gerald@hotmial.com', 'admin', 'sha1', 'a21af90656458ef0afbbdb43b5b990dc', '73d62e18e6d5a3525a5a283448bd96c7a0575d00', 1, 1, '2010-11-16 19:34:14', '2010-10-11 13:25:09', '2010-11-16 19:34:14');
INSERT INTO `sf_guard_user` VALUES (2, 'user', 'user', 'user@user.cl', 'useruser', 'sha1', '877186c1457b6a792633507bf99ab821', '23242c550a43ea76f2051dca95069626669fbc58', 1, 0, '2010-10-17 18:18:40', '2010-10-11 13:29:36', '2010-10-22 09:24:36');
INSERT INTO `sf_guard_user` VALUES (3, 'usuario_de_prueba', 'usuario_de_prueba', 'usuario_de_prueba@prueba.cl', 'usuario_de_prueba', 'sha1', '4dc443fb12d51a669c8f5022a839eacc', '7c840160c143433fc00b615539940f98c03d142c', 1, 0, NULL, '2010-10-17 14:02:18', '2010-10-17 14:02:18');
INSERT INTO `sf_guard_user` VALUES (4, 'prueba', 'prueba', 'prueba@prueba.com', 'prueba', 'sha1', 'b424afeeca705dc3340272dcf2de319f', '8cd0c800a2c7def06f6ffd6afa514526506f5b65', 1, 0, '2010-10-22 09:06:32', '2010-10-20 23:26:36', '2010-10-22 09:06:32');
INSERT INTO `sf_guard_user` VALUES (5, 'pedro', 'eban', 'eban@eban.cl', '1313', 'sha1', NULL, NULL, 1, 0, NULL, '2010-10-26 23:52:35', '2010-10-26 23:52:35');
INSERT INTO `sf_guard_user` VALUES (6, 'a', 'a', 'alonsito@gmail.com', 'a', 'sha1', '9548c908fbff7743260ceab8dbb2770c', 'fa237560156a10d03d51627bfd4c702446fef380', 1, 0, '2010-10-27 00:00:15', '2010-10-27 00:00:05', '2010-10-27 00:00:15');
INSERT INTO `sf_guard_user` VALUES (7, 'pico', 'lalala', 'psychotic_88@hotmail.es', 'psychotic_88', 'sha1', '106daefcc86f4e7cef069fc3049d6de6', 'b3f1a739f005d0a8fca16214d4db872264388be0', 1, 0, '2010-10-27 13:19:44', '2010-10-27 13:17:50', '2010-10-27 13:19:44');
INSERT INTO `sf_guard_user` VALUES (8, 'george', 'jorjato', 'mente.inerte@hotmail.com', 'jorjatopik', 'sha1', '55d6fca8e10dec393485eb272d7c394e', '751bf833a360ea401f1823614aae6251eaab02b8', 1, 0, '2010-11-03 23:54:48', '2010-11-03 23:54:26', '2010-11-03 23:54:48');

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `sf_guard_user_group`
-- 

CREATE TABLE `sf_guard_user_group` (
  `user_id` bigint(20) NOT NULL default '0',
  `group_id` bigint(20) NOT NULL default '0',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY  (`user_id`,`group_id`),
  KEY `sf_guard_user_group_group_id_sf_guard_group_id` (`group_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- 
-- Volcar la base de datos para la tabla `sf_guard_user_group`
-- 


-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `sf_guard_user_permission`
-- 

CREATE TABLE `sf_guard_user_permission` (
  `user_id` bigint(20) NOT NULL default '0',
  `permission_id` bigint(20) NOT NULL default '0',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY  (`user_id`,`permission_id`),
  KEY `sf_guard_user_permission_permission_id_sf_guard_permission_id` (`permission_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- 
-- Volcar la base de datos para la tabla `sf_guard_user_permission`
-- 

