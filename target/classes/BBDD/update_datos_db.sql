-- phpMyAdmin SQL Dump
-- version 2.10.3
-- http://www.phpmyadmin.net
-- 
-- Servidor: localhost
-- Tiempo de generación: 24-11-2010 a las 14:48:57
-- Versión del servidor: 5.0.51
-- Versión de PHP: 5.2.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

-- 
-- Base de datos: `db_web_tt2`
-- 

-- 
-- Volcar la base de datos para la tabla `contrincante_habilidad`
-- 

UPDATE `contrincante_habilidad` SET `personaje_id` = 1, `habilidad_id` = 1, `nivelhabilidad` = 1 WHERE  `contrincante_habilidad`.`personaje_id` = 1 AND `contrincante_habilidad`.`habilidad_id` = 1;
UPDATE `contrincante_habilidad` SET `personaje_id` = 1, `habilidad_id` = 8, `nivelhabilidad` = 1 WHERE  `contrincante_habilidad`.`personaje_id` = 1 AND `contrincante_habilidad`.`habilidad_id` = 8;
UPDATE `contrincante_habilidad` SET `personaje_id` = 1, `habilidad_id` = 10, `nivelhabilidad` = 3 WHERE  `contrincante_habilidad`.`personaje_id` = 1 AND `contrincante_habilidad`.`habilidad_id` = 10;
UPDATE `contrincante_habilidad` SET `personaje_id` = 1, `habilidad_id` = 11, `nivelhabilidad` = 1 WHERE  `contrincante_habilidad`.`personaje_id` = 1 AND `contrincante_habilidad`.`habilidad_id` = 11;
UPDATE `contrincante_habilidad` SET `personaje_id` = 2, `habilidad_id` = 9, `nivelhabilidad` = 4 WHERE  `contrincante_habilidad`.`personaje_id` = 2 AND `contrincante_habilidad`.`habilidad_id` = 9;
UPDATE `contrincante_habilidad` SET `personaje_id` = 40, `habilidad_id` = 1, `nivelhabilidad` = 5 WHERE  `contrincante_habilidad`.`personaje_id` = 40 AND `contrincante_habilidad`.`habilidad_id` = 1;
UPDATE `contrincante_habilidad` SET `personaje_id` = 40, `habilidad_id` = 3, `nivelhabilidad` = 9 WHERE  `contrincante_habilidad`.`personaje_id` = 40 AND `contrincante_habilidad`.`habilidad_id` = 3;

-- 
-- Volcar la base de datos para la tabla `cuenta`
-- 


-- 
-- Volcar la base de datos para la tabla `encargo`
-- 

UPDATE `encargo` SET `personaje_id` = 2, `mision_id` = 1, `created_at` = '2010-10-13 13:24:12', `rolpersonaje` = 88, `updated_at` = '2010-10-14 17:27:08' WHERE  `encargo`.`personaje_id` = 2 AND `encargo`.`mision_id` = 1 AND `encargo`.`created_at` = '2010-10-13 13:24:12';
UPDATE `encargo` SET `personaje_id` = 2, `mision_id` = 2, `created_at` = '2010-10-13 13:11:10', `rolpersonaje` = 0, `updated_at` = NULL WHERE  `encargo`.`personaje_id` = 2 AND `encargo`.`mision_id` = 2 AND `encargo`.`created_at` = '2010-10-13 13:11:10';

-- 
-- Volcar la base de datos para la tabla `habilidad`
-- 

UPDATE `habilidad` SET `id` = 1, `nombre` = 'Golpe simple', `descripcion` = 'golpe con el puño', `danobeneficio` = 20, `costobasico` = 5, `nivelmaximo` = 7, `nom_grafico` = 'golpesimple', `tiempoEspera` = 2 WHERE  `habilidad`.`id` = 1;
UPDATE `habilidad` SET `id` = 3, `nombre` = 'Bola de fuego', `descripcion` = 'Bola de fuego que quema al oponente', `danobeneficio` = -11, `costobasico` = 5, `nivelmaximo` = 10, `nom_grafico` = 'boladefuego', `tiempoEspera` = 2 WHERE  `habilidad`.`id` = 3;
UPDATE `habilidad` SET `id` = 4, `nombre` = 'Saeta de hielo', `descripcion` = 'Ataque que daña y congela al oponente', `danobeneficio` = -7, `costobasico` = 4, `nivelmaximo` = 9, `nom_grafico` = 'saetadehielo', `tiempoEspera` = 2 WHERE  `habilidad`.`id` = 4;
UPDATE `habilidad` SET `id` = 5, `nombre` = 'Acuchillar', `descripcion` = 'Daño físico moderado', `danobeneficio` = -8, `costobasico` = 3, `nivelmaximo` = 10, `nom_grafico` = 'singrafico', `tiempoEspera` = 2 WHERE  `habilidad`.`id` = 5;
UPDATE `habilidad` SET `id` = 6, `nombre` = 'Curación', `descripcion` = 'Cura daño de forma instantánea', `danobeneficio` = 12, `costobasico` = 7, `nivelmaximo` = 11, `nom_grafico` = 'curacion', `tiempoEspera` = 2 WHERE  `habilidad`.`id` = 6;
UPDATE `habilidad` SET `id` = 7, `nombre` = 'Envenenar', `descripcion` = 'Lanza un veneno a tu oponente', `danobeneficio` = -5, `costobasico` = 4, `nivelmaximo` = 7, `nom_grafico` = 'singrafico', `tiempoEspera` = 2 WHERE  `habilidad`.`id` = 7;
UPDATE `habilidad` SET `id` = 8, `nombre` = 'Aturdir', `descripcion` = 'Daña físicamente e inhabilita al oponente', `danobeneficio` = -5, `costobasico` = 6, `nivelmaximo` = 4, `nom_grafico` = 'singrafico', `tiempoEspera` = 2 WHERE  `habilidad`.`id` = 8;
UPDATE `habilidad` SET `id` = 9, `nombre` = 'Vendar', `descripcion` = 'Cura daño mínimo', `danobeneficio` = 4, `costobasico` = 2, `nivelmaximo` = 6, `nom_grafico` = 'singrafico', `tiempoEspera` = 2 WHERE  `habilidad`.`id` = 9;
UPDATE `habilidad` SET `id` = 10, `nombre` = 'Zancadilla ', `descripcion` = 'Golpe a las piernas del oponente', `danobeneficio` = -6, `costobasico` = 3, `nivelmaximo` = 9, `nom_grafico` = 'singrafico', `tiempoEspera` = 2 WHERE  `habilidad`.`id` = 10;
UPDATE `habilidad` SET `id` = 11, `nombre` = 'Maldecir', `descripcion` = 'Lanza una maldición de daño al oponente', `danobeneficio` = -7, `costobasico` = 3, `nivelmaximo` = 8, `nom_grafico` = 'singrafico', `tiempoEspera` = 2 WHERE  `habilidad`.`id` = 11;
UPDATE `habilidad` SET `id` = 12, `nombre` = 'Empujón', `descripcion` = 'Aleja al oponente sin daño', `danobeneficio` = 0, `costobasico` = 2, `nivelmaximo` = 4, `nom_grafico` = 'singrafico', `tiempoEspera` = 2 WHERE  `habilidad`.`id` = 12;

-- 
-- Volcar la base de datos para la tabla `inventario`
-- 

UPDATE `inventario` SET `personaje_id` = 1, `objeto_id` = 3, `cantidad` = 5, `estaequipado` = 0 WHERE  `inventario`.`personaje_id` = 1 AND `inventario`.`objeto_id` = 3;
UPDATE `inventario` SET `personaje_id` = 1, `objeto_id` = 6, `cantidad` = 1, `estaequipado` = 0 WHERE  `inventario`.`personaje_id` = 1 AND `inventario`.`objeto_id` = 6;
UPDATE `inventario` SET `personaje_id` = 2, `objeto_id` = 4, `cantidad` = 7, `estaequipado` = 0 WHERE  `inventario`.`personaje_id` = 2 AND `inventario`.`objeto_id` = 4;
UPDATE `inventario` SET `personaje_id` = 22, `objeto_id` = 1, `cantidad` = 1, `estaequipado` = 0 WHERE  `inventario`.`personaje_id` = 22 AND `inventario`.`objeto_id` = 1;
UPDATE `inventario` SET `personaje_id` = 22, `objeto_id` = 6, `cantidad` = 1, `estaequipado` = 0 WHERE  `inventario`.`personaje_id` = 22 AND `inventario`.`objeto_id` = 6;

-- 
-- Volcar la base de datos para la tabla `jugador`
-- 

UPDATE `jugador` SET `personaje_id` = 1, `vitalidad` = 1, `destreza` = 1, `sabiduria` = 1, `fuerza` = 1, `totalpuntoshabilidad` = 20, `totalpuntosestadistica` = 1, `limitesuperiorexperiencia` = 50, `experiencia` = 0, `pesosoportado` = 20, `dinero` = 1000, `fechacreacion` = '2010-10-27', `estabaneado` = 0, `cuenta_id` = 4 WHERE  `jugador`.`personaje_id` = 1;
UPDATE `jugador` SET `personaje_id` = 22, `vitalidad` = 1, `destreza` = 1, `sabiduria` = 1, `fuerza` = 1, `totalpuntoshabilidad` = 1, `totalpuntosestadistica` = 1, `limitesuperiorexperiencia` = 50, `experiencia` = 0, `pesosoportado` = 20, `dinero` = 0, `fechacreacion` = '2010-10-17', `estabaneado` = 0, `cuenta_id` = 2 WHERE  `jugador`.`personaje_id` = 22;
UPDATE `jugador` SET `personaje_id` = 26, `vitalidad` = 1, `destreza` = 1, `sabiduria` = 1, `fuerza` = 1, `totalpuntoshabilidad` = 1, `totalpuntosestadistica` = 1, `limitesuperiorexperiencia` = 50, `experiencia` = 0, `pesosoportado` = 20, `dinero` = 0, `fechacreacion` = '2010-10-22', `estabaneado` = 0, `cuenta_id` = 4 WHERE  `jugador`.`personaje_id` = 26;
UPDATE `jugador` SET `personaje_id` = 30, `vitalidad` = 1, `destreza` = 1, `sabiduria` = 1, `fuerza` = 1, `totalpuntoshabilidad` = 1, `totalpuntosestadistica` = 1, `limitesuperiorexperiencia` = 50, `experiencia` = 0, `pesosoportado` = 20, `dinero` = 0, `fechacreacion` = '2010-10-26', `estabaneado` = 0, `cuenta_id` = 3 WHERE  `jugador`.`personaje_id` = 30;
UPDATE `jugador` SET `personaje_id` = 36, `vitalidad` = 1, `destreza` = 1, `sabiduria` = 1, `fuerza` = 1, `totalpuntoshabilidad` = 1, `totalpuntosestadistica` = 1, `limitesuperiorexperiencia` = 50, `experiencia` = 0, `pesosoportado` = 20, `dinero` = 0, `fechacreacion` = '2010-10-26', `estabaneado` = 0, `cuenta_id` = 1 WHERE  `jugador`.`personaje_id` = 36;
UPDATE `jugador` SET `personaje_id` = 37, `vitalidad` = 1, `destreza` = 1, `sabiduria` = 1, `fuerza` = 1, `totalpuntoshabilidad` = 1, `totalpuntosestadistica` = 1, `limitesuperiorexperiencia` = 50, `experiencia` = 0, `pesosoportado` = 20, `dinero` = 0, `fechacreacion` = '2010-10-27', `estabaneado` = 0, `cuenta_id` = 6 WHERE  `jugador`.`personaje_id` = 37;
UPDATE `jugador` SET `personaje_id` = 38, `vitalidad` = 1, `destreza` = 1, `sabiduria` = 1, `fuerza` = 1, `totalpuntoshabilidad` = 1, `totalpuntosestadistica` = 1, `limitesuperiorexperiencia` = 50, `experiencia` = 0, `pesosoportado` = 20, `dinero` = 0, `fechacreacion` = '2010-10-27', `estabaneado` = 0, `cuenta_id` = 7 WHERE  `jugador`.`personaje_id` = 38;
UPDATE `jugador` SET `personaje_id` = 39, `vitalidad` = 1, `destreza` = 1, `sabiduria` = 1, `fuerza` = 1, `totalpuntoshabilidad` = 1, `totalpuntosestadistica` = 1, `limitesuperiorexperiencia` = 50, `experiencia` = 0, `pesosoportado` = 20, `dinero` = 0, `fechacreacion` = '2010-11-03', `estabaneado` = 0, `cuenta_id` = 8 WHERE  `jugador`.`personaje_id` = 39;

-- 
-- Volcar la base de datos para la tabla `mision`
-- 

UPDATE `mision` SET `id` = 1, `nombre` = 'prueba', `descripcion` = 'prueba', `nivelrequerido` = 2, `recompensaexp` = 200, `repetible` = 1, `personaje_id` = NULL WHERE  `mision`.`id` = 1;
UPDATE `mision` SET `id` = 2, `nombre` = 'Limpiar el pasto', `descripcion` = 'Hey, se me hace imposible poder limpiar todo el desorden que hay en mi jardín!, podrías ayudarme?', `nivelrequerido` = 10, `recompensaexp` = 150, `repetible` = 1, `personaje_id` = NULL WHERE  `mision`.`id` = 2;
UPDATE `mision` SET `id` = 3, `nombre` = 'Encontrar la cura!', `descripcion` = 'He estado muy enfermo, y solo una medicina en el mundo me ayudará, por suerte...está en nuestra ciud', `nivelrequerido` = 0, `recompensaexp` = 10, `repetible` = 0, `personaje_id` = NULL WHERE  `mision`.`id` = 3;
UPDATE `mision` SET `id` = 4, `nombre` = 'El Alcalde', `descripcion` = '¡Vaya pués! Has conseguido ayudarme, sé de otra persona que necesita ayuda, él es el alcalde, lider ', `nivelrequerido` = 2, `recompensaexp` = 0, `repetible` = 0, `personaje_id` = NULL WHERE  `mision`.`id` = 4;
UPDATE `mision` SET `id` = 5, `nombre` = 'El jardín', `descripcion` = 'Hola forastero, ¿me ayudarías en mi jardín? si lo haces, te daré algo de dinero', `nivelrequerido` = 2, `recompensaexp` = 3, `repetible` = 0, `personaje_id` = NULL WHERE  `mision`.`id` = 5;
UPDATE `mision` SET `id` = 6, `nombre` = 'Boss', `descripcion` = 'El señor de las apariencias, Koh, anda suelto, y podría ser cualquiera de nosotros, ¡Captúralo!', `nivelrequerido` = 56, `recompensaexp` = 32000, `repetible` = 0, `personaje_id` = NULL WHERE  `mision`.`id` = 6;

-- 
-- Volcar la base de datos para la tabla `mob`
-- 

UPDATE `mob` SET `personaje_id` = 40, `vitalidad` = 1, `destreza` = 1, `sabiduria` = 1, `fuerza` = 1, `experiencia` = 25, `dinero` = 5 WHERE  `mob`.`personaje_id` = 40;

-- 
-- Volcar la base de datos para la tabla `objeto`
-- 

UPDATE `objeto` SET `id` = 1, `nombre` = 'zapato', `descripcion` = 'Este item se puede utilizar en el pie\r\n', `tipo` = 1, `peso` = 15, `valordinero` = 1000, `usocombate` = 0, `nom_grafico` = 'zapatos' WHERE  `objeto`.`id` = 1;
UPDATE `objeto` SET `id` = 2, `nombre` = 'capa', `descripcion` = 'Este item se puede utilizar en la espalda\r\n', `tipo` = 2, `peso` = 3, `valordinero` = 3000, `usocombate` = 0, `nom_grafico` = 'singrafico' WHERE  `objeto`.`id` = 2;
UPDATE `objeto` SET `id` = 3, `nombre` = 'Poción curadora', `descripcion` = 'Esta poción revitaliza tus puntos de vida', `tipo` = 1, `peso` = 23, `valordinero` = 2, `usocombate` = 3, `nom_grafico` = 'pocion' WHERE  `objeto`.`id` = 3;
UPDATE `objeto` SET `id` = 4, `nombre` = 'Roca pesada', `descripcion` = 'Roca pesada para tirarse de un puente', `tipo` = 2, `peso` = 4900, `valordinero` = 5000, `usocombate` = 0, `nom_grafico` = 'singrafico' WHERE  `objeto`.`id` = 4;
UPDATE `objeto` SET `id` = 5, `nombre` = 'Pescado', `descripcion` = 'Alimento para curarse paultinamente', `tipo` = 1, `peso` = 100, `valordinero` = 1000, `usocombate` = 0, `nom_grafico` = 'pescado' WHERE  `objeto`.`id` = 5;
UPDATE `objeto` SET `id` = 6, `nombre` = 'Hongo', `descripcion` = 'Vegetal que cura enfermedades simples', `tipo` = 0, `peso` = 1, `valordinero` = 5, `usocombate` = 1, `nom_grafico` = 'hongo' WHERE  `objeto`.`id` = 6;

-- 
-- Volcar la base de datos para la tabla `objeto_mision`
-- 


-- 
-- Volcar la base de datos para la tabla `personaje`
-- 

UPDATE `personaje` SET `id` = 1, `nombre` = 'SergioLove', `nivel` = 1, `posicionx` = 544, `posiciony` = 629, `tipo` = 0 WHERE  `personaje`.`id` = 1;
UPDATE `personaje` SET `id` = 2, `nombre` = 'npc_mision', `nivel` = 1, `posicionx` = 400, `posiciony` = 400, `tipo` = 1 WHERE  `personaje`.`id` = 2;
UPDATE `personaje` SET `id` = 22, `nombre` = 'Sullivan', `nivel` = 1, `posicionx` = 1040, `posiciony` = 416, `tipo` = 1 WHERE  `personaje`.`id` = 22;
UPDATE `personaje` SET `id` = 26, `nombre` = 'NuevoPj', `nivel` = 1, `posicionx` = 400, `posiciony` = 400, `tipo` = 0 WHERE  `personaje`.`id` = 26;
UPDATE `personaje` SET `id` = 30, `nombre` = 'prueba', `nivel` = 12, `posicionx` = 400, `posiciony` = 400, `tipo` = 0 WHERE  `personaje`.`id` = 30;
UPDATE `personaje` SET `id` = 36, `nombre` = 's', `nivel` = 1, `posicionx` = 400, `posiciony` = 400, `tipo` = 0 WHERE  `personaje`.`id` = 36;
UPDATE `personaje` SET `id` = 37, `nombre` = 'a', `nivel` = 1, `posicionx` = 400, `posiciony` = 400, `tipo` = 0 WHERE  `personaje`.`id` = 37;
UPDATE `personaje` SET `id` = 38, `nombre` = 'wenanaty', `nivel` = 1, `posicionx` = 400, `posiciony` = 400, `tipo` = 0 WHERE  `personaje`.`id` = 38;
UPDATE `personaje` SET `id` = 39, `nombre` = 'tulin', `nivel` = 1, `posicionx` = 400, `posiciony` = 400, `tipo` = 0 WHERE  `personaje`.`id` = 39;
UPDATE `personaje` SET `id` = 40, `nombre` = 'Mario', `nivel` = 1, `posicionx` = 400, `posiciony` = 400, `tipo` = 3 WHERE  `personaje`.`id` = 40;

-- 
-- Volcar la base de datos para la tabla `sf_guard_forgot_password`
-- 


-- 
-- Volcar la base de datos para la tabla `sf_guard_group`
-- 


-- 
-- Volcar la base de datos para la tabla `sf_guard_group_permission`
-- 


-- 
-- Volcar la base de datos para la tabla `sf_guard_permission`
-- 


-- 
-- Volcar la base de datos para la tabla `sf_guard_remember_key`
-- 

UPDATE `sf_guard_remember_key` SET `id` = 2, `user_id` = 1, `remember_key` = '4pcfurfk9im8o4wkw088w0kgok80cww', `ip_address` = '200.74.78.64', `created_at` = '2010-10-16 17:18:45', `updated_at` = '2010-10-16 17:18:45' WHERE  `sf_guard_remember_key`.`id` = 2;

-- 
-- Volcar la base de datos para la tabla `sf_guard_user`
-- 

UPDATE `sf_guard_user` SET `id` = 1, `first_name` = 'admin', `last_name` = 'admin', `email_address` = 'Gerald@hotmial.com', `username` = 'admin', `algorithm` = 'sha1', `salt` = 'a21af90656458ef0afbbdb43b5b990dc', `password` = '73d62e18e6d5a3525a5a283448bd96c7a0575d00', `is_active` = 1, `is_super_admin` = 1, `last_login` = '2010-11-16 19:34:14', `created_at` = '2010-10-11 13:25:09', `updated_at` = '2010-11-16 19:34:14' WHERE  `sf_guard_user`.`id` = 1;
UPDATE `sf_guard_user` SET `id` = 2, `first_name` = 'user', `last_name` = 'user', `email_address` = 'user@user.cl', `username` = 'useruser', `algorithm` = 'sha1', `salt` = '877186c1457b6a792633507bf99ab821', `password` = '23242c550a43ea76f2051dca95069626669fbc58', `is_active` = 1, `is_super_admin` = 0, `last_login` = '2010-10-17 18:18:40', `created_at` = '2010-10-11 13:29:36', `updated_at` = '2010-10-22 09:24:36' WHERE  `sf_guard_user`.`id` = 2;
UPDATE `sf_guard_user` SET `id` = 3, `first_name` = 'usuario_de_prueba', `last_name` = 'usuario_de_prueba', `email_address` = 'usuario_de_prueba@prueba.cl', `username` = 'usuario_de_prueba', `algorithm` = 'sha1', `salt` = '4dc443fb12d51a669c8f5022a839eacc', `password` = '7c840160c143433fc00b615539940f98c03d142c', `is_active` = 1, `is_super_admin` = 0, `last_login` = NULL, `created_at` = '2010-10-17 14:02:18', `updated_at` = '2010-10-17 14:02:18' WHERE  `sf_guard_user`.`id` = 3;
UPDATE `sf_guard_user` SET `id` = 4, `first_name` = 'prueba', `last_name` = 'prueba', `email_address` = 'prueba@prueba.com', `username` = 'prueba', `algorithm` = 'sha1', `salt` = 'b424afeeca705dc3340272dcf2de319f', `password` = '8cd0c800a2c7def06f6ffd6afa514526506f5b65', `is_active` = 1, `is_super_admin` = 0, `last_login` = '2010-10-22 09:06:32', `created_at` = '2010-10-20 23:26:36', `updated_at` = '2010-10-22 09:06:32' WHERE  `sf_guard_user`.`id` = 4;
UPDATE `sf_guard_user` SET `id` = 5, `first_name` = 'pedro', `last_name` = 'eban', `email_address` = 'eban@eban.cl', `username` = '1313', `algorithm` = 'sha1', `salt` = NULL, `password` = NULL, `is_active` = 1, `is_super_admin` = 0, `last_login` = NULL, `created_at` = '2010-10-26 23:52:35', `updated_at` = '2010-10-26 23:52:35' WHERE  `sf_guard_user`.`id` = 5;
UPDATE `sf_guard_user` SET `id` = 6, `first_name` = 'a', `last_name` = 'a', `email_address` = 'alonsito@gmail.com', `username` = 'a', `algorithm` = 'sha1', `salt` = '9548c908fbff7743260ceab8dbb2770c', `password` = 'fa237560156a10d03d51627bfd4c702446fef380', `is_active` = 1, `is_super_admin` = 0, `last_login` = '2010-10-27 00:00:15', `created_at` = '2010-10-27 00:00:05', `updated_at` = '2010-10-27 00:00:15' WHERE  `sf_guard_user`.`id` = 6;
UPDATE `sf_guard_user` SET `id` = 7, `first_name` = 'pico', `last_name` = 'lalala', `email_address` = 'psychotic_88@hotmail.es', `username` = 'psychotic_88', `algorithm` = 'sha1', `salt` = '106daefcc86f4e7cef069fc3049d6de6', `password` = 'b3f1a739f005d0a8fca16214d4db872264388be0', `is_active` = 1, `is_super_admin` = 0, `last_login` = '2010-10-27 13:19:44', `created_at` = '2010-10-27 13:17:50', `updated_at` = '2010-10-27 13:19:44' WHERE  `sf_guard_user`.`id` = 7;
UPDATE `sf_guard_user` SET `id` = 8, `first_name` = 'george', `last_name` = 'jorjato', `email_address` = 'mente.inerte@hotmail.com', `username` = 'jorjatopik', `algorithm` = 'sha1', `salt` = '55d6fca8e10dec393485eb272d7c394e', `password` = '751bf833a360ea401f1823614aae6251eaab02b8', `is_active` = 1, `is_super_admin` = 0, `last_login` = '2010-11-03 23:54:48', `created_at` = '2010-11-03 23:54:26', `updated_at` = '2010-11-03 23:54:48' WHERE  `sf_guard_user`.`id` = 8;

-- 
-- Volcar la base de datos para la tabla `sf_guard_user_group`
-- 


-- 
-- Volcar la base de datos para la tabla `sf_guard_user_permission`
-- 

