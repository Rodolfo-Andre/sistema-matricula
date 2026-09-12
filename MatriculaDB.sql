
DROP DATABASE IF EXISTS sistema_matricula;

CREATE DATABASE sistema_matricula
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_spanish_ci;

USE sistema_matricula;

CREATE TABLE carreras (
    id_carrera INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE profesores (
    id_profesor INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(15) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    especialidad VARCHAR(100) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE estudiantes (
    id_estudiante INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    dni VARCHAR(15) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    id_carrera INT NOT NULL,
    CONSTRAINT fk_estudiante_carrera 
        FOREIGN KEY (id_carrera) REFERENCES carreras(id_carrera) 
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE cursos (
    id_curso INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    creditos INT NOT NULL
) ENGINE=InnoDB;

CREATE TABLE horarios (
    id_horario INT AUTO_INCREMENT PRIMARY KEY,
    dia VARCHAR(20) NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    aula VARCHAR(20) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE curso_profesor (
    id_curso_profesor INT AUTO_INCREMENT PRIMARY KEY,
    id_curso INT NOT NULL,
    id_profesor INT NOT NULL,
    id_horario INT NOT NULL,
    periodo VARCHAR(10) NOT NULL,
    CONSTRAINT fk_cp_curso 
        FOREIGN KEY (id_curso) REFERENCES cursos(id_curso) 
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_cp_profesor 
        FOREIGN KEY (id_profesor) REFERENCES profesores(id_profesor) 
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_cp_horario 
        FOREIGN KEY (id_horario) REFERENCES horarios(id_horario) 
        ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_cp_periodo (periodo)
) ENGINE=InnoDB;

CREATE TABLE matriculas (
    id_matricula INT AUTO_INCREMENT PRIMARY KEY,
    id_estudiante INT NOT NULL,
    fecha_matricula DATE NOT NULL,
    periodo VARCHAR(10) NOT NULL,
    estado ENUM('ACTIVA','ANULADA') NOT NULL DEFAULT 'ACTIVA',
    CONSTRAINT fk_matricula_estudiante 
        FOREIGN KEY (id_estudiante) REFERENCES estudiantes(id_estudiante) 
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT uq_matricula_est_periodo UNIQUE (id_estudiante, periodo),
    INDEX idx_matriculas_periodo (periodo),
    INDEX idx_matriculas_est_periodo (id_estudiante, periodo)
) ENGINE=InnoDB;


CREATE TABLE detalle_matricula (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_matricula INT NOT NULL,
    id_curso_profesor INT NOT NULL,
    estado ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
    CONSTRAINT fk_dm_matricula 
        FOREIGN KEY (id_matricula) REFERENCES matriculas(id_matricula) 
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_dm_cursoprofesor 
        FOREIGN KEY (id_curso_profesor) REFERENCES curso_profesor(id_curso_profesor) 
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT uq_detalle_mat_cursoprof UNIQUE (id_matricula, id_curso_profesor),
    INDEX idx_detalle_idmat (id_matricula),
    INDEX idx_detalle_mat_estado (id_matricula, estado)
) ENGINE=InnoDB;


-- ================================
-- DATOS DE PRUEBA 
-- ================================

INSERT INTO carreras (nombre) VALUES 
('Ingeniería de Sistemas'),
('Ingeniería de Software'),
('Administración de Empresas'),
('Contabilidad'),
('Marketing Digital'),
('Diseño Gráfico'),
('Redes y Comunicaciones'),
('Ciberseguridad'),
('Inteligencia de Negocios'),
('Gestión Logística');

INSERT INTO profesores (dni, nombres, apellidos, especialidad) VALUES 
('10111213', 'Carlos', 'Mendoza', 'Base de Datos'),
('14151617', 'Ana', 'Torres', 'Programación Java'),
('18192021', 'Luis', 'Ramírez', 'Algoritmos'),
('22232425', 'Sofía', 'Castillo', 'Estructura de Datos'),
('26272829', 'Miguel', 'Flores', 'Redes'),
('30313233', 'Elena', 'Vargas', 'Ingeniería de Requerimientos'),
('34353637', 'Jorge', 'Pérez', 'Sistemas Operativos'),
('38394041', 'Patricia', 'Morales', 'Estadística'),
('42434445', 'Roberto', 'Gómez', 'Diseño Web'),
('46474849', 'Lucía', 'Herrera', 'Ciberseguridad');

INSERT INTO estudiantes (codigo, dni, nombres, apellidos, id_carrera) VALUES 
('E001', '71234567', 'Juan', 'Pérez', 1),
('E002', '72345678', 'María', 'Gómez', 2),
('E003', '73456789', 'Pedro', 'Rojas', 1),
('E004', '74567890', 'Laura', 'Sánchez', 3),
('E005', '75678901', 'Diego', 'Díaz', 4),
('E006', '76789012', 'Carmen', 'Fernández', 5),
('E007', '77890123', 'Gabriel', 'Álvarez', 6),
('E008', '78901234', 'Valeria', 'Espinoza', 7),
('E009', '79012345', 'Mateo', 'Romero', 8),
('E010', '70123456', 'Camila', 'Navarro', 2);

INSERT INTO cursos (codigo, nombre, creditos) VALUES 
('CUR101', 'Programación Orientada a Objetos', 4),
('CUR102', 'Bases de Datos', 3),
('CUR103', 'Estructura de Datos', 4),
('CUR104', 'Desarrollo Web Integrado', 3),
('CUR105', 'Ingeniería de Requerimientos', 4),
('CUR106', 'Redes y Comunicaciones', 3),
('CUR107', 'Sistemas Operativos', 4),
('CUR108', 'Análisis de Algoritmos', 3),
('CUR109', 'Fundamentos de Ciberseguridad', 3),
('CUR110', 'Arquitectura de Software', 4);

INSERT INTO horarios (dia, hora_inicio, hora_fin, aula) VALUES 
('Lunes', '08:00:00', '10:00:00', 'Lab-101'),
('Lunes', '10:00:00', '12:00:00', 'Lab-102'),
('Martes', '08:00:00', '10:00:00', 'Aula-201'),
('Martes', '10:00:00', '12:00:00', 'Aula-202'),
('Miércoles', '08:00:00', '10:00:00', 'Lab-103'),
('Miércoles', '10:00:00', '12:00:00', 'Aula-203'),
('Jueves', '08:00:00', '10:00:00', 'Lab-104'),
('Jueves', '10:00:00', '12:00:00', 'Aula-204'),
('Viernes', '08:00:00', '10:00:00', 'Lab-105'),
('Viernes', '10:00:00', '12:00:00', 'Aula-205');

INSERT INTO curso_profesor (id_curso, id_profesor, id_horario, periodo) VALUES 
(1, 2, 1, '2026-1'),
(2, 1, 2, '2026-1'),
(3, 4, 3, '2026-1'),
(4, 9, 4, '2026-1'),
(5, 6, 5, '2026-1'),
(6, 5, 6, '2026-1'),
(7, 7, 7, '2026-1'),
(8, 3, 8, '2026-1'),
(9, 10, 9, '2026-1'),
(10, 2, 10, '2026-1');

INSERT INTO matriculas (id_estudiante, fecha_matricula, periodo) VALUES 
(1, '2026-03-01', '2026-1'),
(2, '2026-03-01', '2026-1'),
(3, '2026-03-02', '2026-1'),
(4, '2026-03-02', '2026-1'),
(5, '2026-03-03', '2026-1'),
(6, '2026-03-03', '2026-1'),
(7, '2026-03-04', '2026-1'),
(8, '2026-03-04', '2026-1'),
(9, '2026-03-05', '2026-1'),
(10, '2026-03-05', '2026-1');

INSERT INTO detalle_matricula (id_matricula, id_curso_profesor) VALUES 
(1, 1),
(1, 2),
(2, 1),
(3, 3),
(4, 4),
(5, 5),
(6, 6),
(7, 7),
(8, 8),
(9, 9);