-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generaciรณn: 18-02-2021 a las 03:46:44
-- Versiรณn del servidor: 10.4.16-MariaDB
-- Versiรณn de PHP: 7.4.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `hibernaterevision`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente`
--

CREATE TABLE `Cliente` (
  `DNI` varchar(255) NOT NULL,
  `Nombre` varchar(255) DEFAULT NULL,
  `Direccion` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `cliente`
--

INSERT INTO `Cliente` (`DNI`, `Nombre`, `Direccion`) VALUES
('29658978Y', 'Manuel', 'Calle conde'),
('30202020K', 'Juan', 'Juan de dios'),
('30286630L', 'Juan', 'Cristo de la sed'),
('30286635M', 'Jose', 'Pez 92');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `coche`
--

CREATE TABLE `Coche` (
  `Matricula` varchar(255) NOT NULL,
  `Marca` varchar(255) DEFAULT NULL,
  `Modelo` varchar(255) DEFAULT NULL,
  `Tipo_Combustible` varchar(255) DEFAULT NULL,
  `Cliente_Pertenece` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `coche`
--

INSERT INTO `Coche` (`Matricula`, `Marca`, `Modelo`, `Tipo_Combustible`, `Cliente_Pertenece`) VALUES
('1258MPK', 'Seat', 'Leon', 'ELECTRICO', '29658978Y'),
('2121ZXC', 'Seat', 'Arona', 'GASOLINA', '30202020K'),
('3032ASD', 'Seat', 'Ibiza FR', 'DIESEL', '30286635M'),
('3322LKJ', 'Seat', 'Ibiza', 'DIESEL', '30286630L'),
('3333LLL', 'SEAT', 'TOLEDO', 'GASOLINA', '30286635M');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `revision`
--

CREATE TABLE `Revision` (
  `idRevision` int(11) NOT NULL,
  `FechaRevision` tinyblob DEFAULT NULL,
  `Descripcion` varchar(255) DEFAULT NULL,
  `Matricula_Revision` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `revision`
--

INSERT INTO `Revision` (`idRevision`, `FechaRevision`, `Descripcion`, `Matricula_Revision`) VALUES
(3, 0xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e5011e78, 'Cambio de bateria', '1258MPK'),
(4, 0xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e5021078, 'Presion de los neumaticos', '1258MPK'),
(5, 0xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e5020e78, 'CAMBIO DE ACEITE', '3333LLL');

--
-- indices para tablas volcadas
--

--
-- Indices de la tabla `cliente`
--
ALTER TABLE `Cliente`
  ADD PRIMARY KEY (`DNI`);

--
-- Indices de la tabla `coche`
--
ALTER TABLE `Coche`
  ADD PRIMARY KEY (`Matricula`),
  ADD KEY `FK_CP` (`Cliente_Pertenece`);

--
-- Indices de la tabla `revision`
--
ALTER TABLE `Revision`
  ADD PRIMARY KEY (`idRevision`),
  ADD KEY `FK_MR` (`Matricula_Revision`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `revision`
--
ALTER TABLE `Revision`
  MODIFY `idRevision` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `coche`
--
ALTER TABLE `Coche`
  ADD CONSTRAINT `FK_CP` FOREIGN KEY (`Cliente_Pertenece`) REFERENCES `Cliente` (`DNI`);

--
-- Filtros para la tabla `revision`
--
ALTER TABLE `Revision`
  ADD CONSTRAINT `FK_MR` FOREIGN KEY (`Matricula_Revision`) REFERENCES `Coche` (`Matricula`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
