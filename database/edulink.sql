-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 18-07-2026 a las 01:33:34
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `edulink`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `administrador`
--

CREATE TABLE `administrador` (
  `idAdministrador` int(11) NOT NULL,
  `nombre` char(50) DEFAULT NULL,
  `password` char(50) DEFAULT NULL,
  `correo` char(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `administrador`
--

INSERT INTO `administrador` (`idAdministrador`, `nombre`, `password`, `correo`) VALUES
(1, 'admin12345', 'admin12345', 'admin12345@gmail.com');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `alumno`
--

CREATE TABLE `alumno` (
  `idAlumno` int(11) NOT NULL,
  `nombre` char(50) DEFAULT NULL,
  `apellido` char(50) DEFAULT NULL,
  `correo` char(50) DEFAULT NULL,
  `matricula` int(8) DEFAULT NULL,
  `password` char(16) DEFAULT NULL,
  `domicilio` char(100) DEFAULT NULL,
  `localidad` char(50) DEFAULT NULL,
  `telefono` char(12) DEFAULT NULL,
  `edad` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `alumno`
--

INSERT INTO `alumno` (`idAlumno`, `nombre`, `apellido`, `correo`, `matricula`, `password`, `domicilio`, `localidad`, `telefono`, `edad`) VALUES
(1, 'Dario', 'Fernandez Rubio', 'dariofernandezrubio@gmail.com', 25325005, 'dario123', 'privada de todo', 'Lomas de Santa cruz', '5646675683', 39),
(3, 'Ana', 'López', 'ana.lopez@email.com', 20260001, 'pass01', 'Calle 1 #10', 'CDMX', '5511111111', 20),
(4, 'Luis', 'Martínez', 'luis.martinez@email.com', 20260002, 'pass02', 'Calle 2 #20', 'CDMX', '5522222222', 22),
(5, 'María', 'García', 'maria.garcia@email.com', 20260003, 'pass03', 'Calle 3 #30', 'Guadalajara', '3333333333', 19),
(6, 'José', 'Rodríguez', 'jose.rodriguez@email.com', 20260004, 'pass04', 'Calle 4 #40', 'Guadalajara', '3344444444', 21),
(7, 'Laura', 'Hernández', 'laura.hernandez@email.com', 20260005, 'pass05', 'Calle 5 #50', 'Monterrey', '8155555555', 23),
(8, 'Carlos', 'Gómez', 'carlos.gomez@email.com', 20260006, 'pass06', 'Calle 6 #60', 'Monterrey', '8166666666', 20),
(9, 'Marta', 'Sánchez', 'marta.sanchez@email.com', 20260007, 'pass07', 'Calle 7 #70', 'Puebla', '2277777777', 24),
(10, 'Javier', 'Pérez', 'javier.perez@email.com', 20260008, 'pass08', 'Calle 8 #80', 'Puebla', '2288888888', 22),
(11, 'Elena', 'Ramírez', 'elena.ramirez@email.com', 20260009, 'pass09', 'Calle 9 #90', 'Toluca', '7299999999', 21),
(12, 'David', 'Flores', 'david.flores@email.com', 20260010, 'pass10', 'Calle 10 #100', 'Toluca', '7210101010', 20),
(13, 'Sofía', 'Morales', 'sofia.morales@email.com', 20260011, 'pass11', 'Calle 11 #110', 'Querétaro', '4421212121', 19),
(14, 'Daniel', 'Ortiz', 'daniel.ortiz@email.com', 20260012, 'pass12', 'Calle 12 #120', 'Querétaro', '4432323232', 25),
(15, 'Paula', 'Castro', 'paula.castro@email.com', 20260013, 'pass13', 'Calle 13 #130', 'León', '4743434343', 22),
(16, 'Alejandro', 'Romero', 'alejandro.romero@email.com', 20260014, 'pass14', 'Calle 14 #140', 'León', '4754545454', 23),
(17, 'Natalia', 'Mendoza', 'natalia.mendoza@email.com', 20260015, 'pass15', 'Calle 15 #150', 'Mexicali', '6865656565', 21),
(18, 'Pedro', 'Vega', 'pedro.vega@email.com', 20260016, 'pass16', 'Calle 16 #160', 'Mexicali', '6876767676', 20),
(19, 'Carmen', 'Reyes', 'carmen.reyes@email.com', 20260017, 'pass17', 'Calle 17 #170', 'Tijuana', '6647878787', 24),
(20, 'Fernando', 'Guzmán', 'fernando.guzman@email.com', 20260018, 'pass18', 'Calle 18 #180', 'Tijuana', '6658989898', 22),
(21, 'Lucía', 'Jiménez', 'lucia.jimenez@email.com', 20260019, 'pass19', 'Calle 19 #190', 'Cancún', '9989090909', 21),
(22, 'Roberto', 'Márquez', 'roberto.marquez@email.com', 20260020, 'pass20', 'Calle 20 #200', 'Cancún', '9990101010', 23);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `curso`
--

CREATE TABLE `curso` (
  `idCurso` int(11) NOT NULL,
  `nombre` char(100) DEFAULT NULL,
  `nivel` char(10) DEFAULT NULL,
  `descripcion` text DEFAULT NULL,
  `idMaestro` int(11) DEFAULT NULL,
  `fDeInicio` date NOT NULL,
  `fDeFinalizacion` date NOT NULL,
  `imgCurso` longblob DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `curso`
--

INSERT INTO `curso` (`idCurso`, `nombre`, `nivel`, `descripcion`, `idMaestro`, `fDeInicio`, `fDeFinalizacion`, `imgCurso`) VALUES
(1, 'Inglés Básico 1', 'A1', 'Curso para principiantes sin conocimiento previo. Vocabulario básico y gramática elemental.', 1, '2026-01-15', '2026-04-15', NULL),
(2, 'Inglés Básico 2', 'A2', 'Continuación del nivel A1. Se refuerzan estructuras y se amplía vocabulario.', 1, '2026-05-01', '2026-08-01', NULL),
(3, 'Inglés Intermedio', 'B1', 'Curso para estudiantes con base sólida. Se trabaja en fluidez y comprensión de textos.', 2, '2026-02-10', '2026-06-10', NULL),
(4, 'Inglés Avanzado', 'B2', 'Nivel alto, preparación para exámenes internacionales y conversación avanzada.', 3, '2026-03-01', '2026-07-01', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalleCA`
--

CREATE TABLE `detalleCA` (
  `idCurso` int(11) DEFAULT NULL,
  `idAlumno` int(11) DEFAULT NULL,
  `calificacion` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalleTareas`
--

CREATE TABLE `detalleTareas` (
  `idDetalle` int(11) NOT NULL,
  `idTarea` int(11) NOT NULL,
  `idAlumno` int(11) NOT NULL,
  `fDeEntrega` datetime NOT NULL,
  `archivoEntrega` longblob DEFAULT NULL,
  `comentarios` text DEFAULT NULL,
  `calificacion` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `maestro`
--

CREATE TABLE `maestro` (
  `idMaestro` int(11) NOT NULL,
  `nombre` char(50) DEFAULT NULL,
  `apellido` char(50) DEFAULT NULL,
  `correo` char(50) DEFAULT NULL,
  `password` char(16) DEFAULT NULL,
  `domicilio` char(100) DEFAULT NULL,
  `localidad` char(50) DEFAULT NULL,
  `telefono` char(12) DEFAULT NULL,
  `edad` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `maestro`
--

INSERT INTO `maestro` (`idMaestro`, `nombre`, `apellido`, `correo`, `password`, `domicilio`, `localidad`, `telefono`, `edad`) VALUES
(1, 'Laura', 'González', 'laura.gonzalez@edulink.com', '123456', 'Calle Ficticia 123', 'Ciudad de México', '5512345678', 35),
(2, 'Carlos', 'Mendoza', 'carlos.mendoza@edulink.com', 'abcdef', 'Av. Siempre Viva 742', 'Guadalajara', '3334567890', 42),
(3, 'María', 'Fernández', 'maria.fernandez@edulink.com', 'qwerty', 'Calle Luna 45', 'Monterrey', '8112345678', 28);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `material`
--

CREATE TABLE `material` (
  `idCurso` int(11) NOT NULL,
  `idMaestro` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `archivo` longblob NOT NULL,
  `curso` varchar(50) NOT NULL,
  `descripcion` text NOT NULL,
  `leccion` int(11) NOT NULL,
  `idMaterial` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `material`
--

INSERT INTO `material` (`idCurso`, `idMaestro`, `nombre`, `archivo`, `curso`, `descripcion`, `leccion`, `idMaterial`) VALUES
(1, 1, 'Lección 1 - Introducción', 0x255044462d312e332025c3a2, 'Inglés Básico 1', 'Presentación del curso y saludos básicos', 1, 1),
(1, 1, 'Lección 2 - Verbos ser/estar', 0x255044462d312e332025c3a2, 'Inglés Básico 1', 'Uso de to be, ejercicios', 2, 2),
(2, 1, 'Lección 1 - Repaso A1', 0x255044462d312e332025c3a2, 'Inglés Básico 2', 'Repaso de vocabulario y tiempos simples', 1, 3),
(3, 2, 'Lección 3 - Pasado simple', 0x255044462d312e332025c3a2, 'Inglés Intermedio', 'Formación y uso del pasado simple', 3, 4),
(4, 3, 'Lección 2 - Condicionales', 0x255044462d312e332025c3a2, 'Inglés Avanzado', 'Condicionales tipo 0, 1, 2 y 3', 2, 5);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tarea`
--

CREATE TABLE `tarea` (
  `idTarea` int(11) NOT NULL,
  `nombre` char(50) NOT NULL,
  `descripcion` text NOT NULL,
  `f_habil` date NOT NULL,
  `f_limit` date NOT NULL,
  `idCurso` int(11) NOT NULL,
  `idMaestro` int(11) NOT NULL,
  `archivoEnunciado` longblob DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tarea`
--

INSERT INTO `tarea` (`idTarea`, `nombre`, `descripcion`, `f_habil`, `f_limit`, `idCurso`, `idMaestro`, `archivoEnunciado`) VALUES
(1, 'Tarea 1: Saludos', 'Escribe un diálogo usando los saludos básicos vistos en clase.', '2026-01-20', '2026-01-30', 1, 1, NULL),
(2, 'Tarea 2: Mi familia', 'Describe a tu familia usando el verbo to be y vocabulario de parentesco.', '2026-05-05', '2026-05-15', 2, 1, NULL),
(3, 'Tarea 3: Un viaje', 'Redacta un relato sobre un viaje que hayas hecho usando pasado simple.', '2026-02-15', '2026-02-28', 3, 2, NULL),
(4, 'Tarea 4: Debate', 'Prepara un argumento a favor o en contra de una posición usando condicionales.', '2026-03-10', '2026-03-25', 4, 3, NULL),
(5, 'Tarea 5: Vocabulario extra', 'Haz una lista de 20 palabras nuevas con su definición y una oración.', '2026-02-01', '2026-02-10', 1, 1, NULL);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `administrador`
--
ALTER TABLE `administrador`
  ADD PRIMARY KEY (`idAdministrador`),
  ADD UNIQUE KEY `correo` (`correo`);

--
-- Indices de la tabla `alumno`
--
ALTER TABLE `alumno`
  ADD PRIMARY KEY (`idAlumno`),
  ADD UNIQUE KEY `matricula` (`matricula`,`correo`);

--
-- Indices de la tabla `curso`
--
ALTER TABLE `curso`
  ADD PRIMARY KEY (`idCurso`),
  ADD KEY `idMaestro` (`idMaestro`);

--
-- Indices de la tabla `detalleCA`
--
ALTER TABLE `detalleCA`
  ADD KEY `idCurso` (`idCurso`),
  ADD KEY `idAlumno` (`idAlumno`);

--
-- Indices de la tabla `detalleTareas`
--
ALTER TABLE `detalleTareas`
  ADD PRIMARY KEY (`idDetalle`);

--
-- Indices de la tabla `maestro`
--
ALTER TABLE `maestro`
  ADD PRIMARY KEY (`idMaestro`),
  ADD UNIQUE KEY `correo` (`correo`);

--
-- Indices de la tabla `material`
--
ALTER TABLE `material`
  ADD PRIMARY KEY (`idMaterial`);

--
-- Indices de la tabla `tarea`
--
ALTER TABLE `tarea`
  ADD PRIMARY KEY (`idTarea`),
  ADD KEY `idCurso` (`idCurso`),
  ADD KEY `idMaestro` (`idMaestro`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `administrador`
--
ALTER TABLE `administrador`
  MODIFY `idAdministrador` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `alumno`
--
ALTER TABLE `alumno`
  MODIFY `idAlumno` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT de la tabla `curso`
--
ALTER TABLE `curso`
  MODIFY `idCurso` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `maestro`
--
ALTER TABLE `maestro`
  MODIFY `idMaestro` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `material`
--
ALTER TABLE `material`
  MODIFY `idMaterial` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `tarea`
--
ALTER TABLE `tarea`
  MODIFY `idTarea` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `curso`
--
ALTER TABLE `curso`
  ADD CONSTRAINT `curso_ibfk_1` FOREIGN KEY (`idMaestro`) REFERENCES `maestro` (`idMaestro`);

--
-- Filtros para la tabla `detalleCA`
--
ALTER TABLE `detalleCA`
  ADD CONSTRAINT `detalleCA_ibfk_1` FOREIGN KEY (`idCurso`) REFERENCES `curso` (`idCurso`),
  ADD CONSTRAINT `detalleCA_ibfk_2` FOREIGN KEY (`idAlumno`) REFERENCES `alumno` (`idAlumno`);

--
-- Filtros para la tabla `detalleTareas`
--
ALTER TABLE `detalleTareas`
  ADD CONSTRAINT `detalleTareas_ibfk_1` FOREIGN KEY (`idTarea`) REFERENCES `tarea` (`idTarea`),
  ADD CONSTRAINT `detalleTareas_ibfk_2` FOREIGN KEY (`idAlumno`) REFERENCES `alumno` (`idAlumno`);

--
-- Filtros para la tabla `tarea`
--
ALTER TABLE `tarea`
  ADD CONSTRAINT `tarea_ibfk_1` FOREIGN KEY (`idCurso`) REFERENCES `curso` (`idCurso`),
  ADD CONSTRAINT `tarea_ibfk_2` FOREIGN KEY (`idMaestro`) REFERENCES `maestro` (`idMaestro`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
