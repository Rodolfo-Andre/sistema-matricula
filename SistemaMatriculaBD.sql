
CREATE DATABASE sistema_matricula;
GO

USE sistema_matricula;
GO

-- =========================================================
-- TABLAS
-- =========================================================

CREATE TABLE roles (
    id_rol INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);
GO

CREATE TABLE carreras (
    id_carrera INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);
GO

CREATE TABLE profesores (
    id_profesor INT IDENTITY(1,1) PRIMARY KEY,
    dni VARCHAR(15) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    especialidad VARCHAR(100) NOT NULL
);
GO

CREATE TABLE estudiantes (
    id_estudiante INT IDENTITY(1,1) PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    dni VARCHAR(15) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    id_carrera INT NOT NULL,
    ciclo INT NOT NULL DEFAULT 1,
    CONSTRAINT fk_estudiante_carrera 
        FOREIGN KEY (id_carrera) REFERENCES carreras(id_carrera)
);
GO

CREATE TABLE usuarios (
    id_usuario INT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    id_rol INT NOT NULL,
    id_estudiante INT NULL,
    id_profesor INT NULL,
    estado BIT DEFAULT 1,
    CONSTRAINT fk_usuario_rol 
        FOREIGN KEY (id_rol) REFERENCES roles(id_rol),
    CONSTRAINT fk_usuario_estudiante 
        FOREIGN KEY (id_estudiante) REFERENCES estudiantes(id_estudiante) 
        ON DELETE SET NULL,
    CONSTRAINT fk_usuario_profesor 
        FOREIGN KEY (id_profesor) REFERENCES profesores(id_profesor) 
        ON DELETE SET NULL
);
GO

CREATE TABLE cursos (
    id_curso INT IDENTITY(1,1) PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    creditos INT NOT NULL
);
GO

CREATE TABLE horarios (
    id_horario INT IDENTITY(1,1) PRIMARY KEY,
    dia VARCHAR(20) NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    aula VARCHAR(20) NOT NULL
);
GO

CREATE TABLE curso_profesor (
    id_curso_profesor INT IDENTITY(1,1) PRIMARY KEY,
    id_curso INT NOT NULL,
    id_profesor INT NOT NULL,
    id_horario INT NOT NULL,
    periodo VARCHAR(10) NOT NULL,
    CONSTRAINT fk_cp_curso 
        FOREIGN KEY (id_curso) REFERENCES cursos(id_curso),
    CONSTRAINT fk_cp_profesor 
        FOREIGN KEY (id_profesor) REFERENCES profesores(id_profesor),
    CONSTRAINT fk_cp_horario 
        FOREIGN KEY (id_horario) REFERENCES horarios(id_horario)
);
GO

CREATE TABLE matriculas (
    id_matricula INT IDENTITY(1,1) PRIMARY KEY,
    id_estudiante INT NOT NULL,
    fecha_matricula DATE NOT NULL,
    periodo VARCHAR(10) NOT NULL,
    CONSTRAINT fk_matricula_estudiante 
        FOREIGN KEY (id_estudiante) REFERENCES estudiantes(id_estudiante)
);
GO

CREATE TABLE detalle_matricula (
    id_detalle INT IDENTITY(1,1) PRIMARY KEY,
    id_matricula INT NOT NULL,
    id_curso_profesor INT NOT NULL,
    CONSTRAINT fk_dm_matricula 
        FOREIGN KEY (id_matricula) REFERENCES matriculas(id_matricula) 
        ON DELETE CASCADE,
    CONSTRAINT fk_dm_cursoprofesor 
        FOREIGN KEY (id_curso_profesor) REFERENCES curso_profesor(id_curso_profesor)
);
GO

-- =========================================================
--  DATOS INICIALES (PRUEBA)
-- =========================================================

INSERT INTO roles (nombre) VALUES ('ADMINISTRADOR'), ('DOCENTE'), ('ESTUDIANTE');

INSERT INTO carreras (nombre) VALUES 
('Ingeniería de Sistemas'), ('Ingeniería de Software'), ('Administración de Empresas'),
('Contabilidad'), ('Marketing Digital'), ('Diseño Gráfico'),
('Redes y Comunicaciones'), ('Ciberseguridad'), ('Inteligencia de Negocios'), ('Gestión Logística');

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

INSERT INTO estudiantes (codigo, dni, nombres, apellidos, id_carrera, ciclo) VALUES 
('E001', '71234567', 'Juan', 'Pérez', 1, 1),
('E002', '72345678', 'María', 'Gómez', 2, 1),
('E003', '73456789', 'Pedro', 'Rojas', 1, 2),
('E004', '74567890', 'Laura', 'Sánchez', 3, 2),
('E005', '75678901', 'Diego', 'Díaz', 4, 3),
('E006', '76789012', 'Carmen', 'Fernández', 5, 3),
('E007', '77890123', 'Gabriel', 'Álvarez', 6, 4),
('E008', '78901234', 'Valeria', 'Espinoza', 7, 4),
('E009', '79012345', 'Mateo', 'Romero', 8, 5),
('E010', '70123456', 'Camila', 'Navarro', 2, 5);

INSERT INTO usuarios (username, password, id_rol, id_estudiante, id_profesor) VALUES 
('admin', 'admin123', 1, NULL, NULL),
('E001', 'estudiante123', 3, 1, NULL),
('E002', 'estudiante123', 3, 2, NULL),
('prof_carlos', 'docente123', 2, NULL, 1);

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
GO

SELECT * FROM estudiantes;
GO

-- =========================================================
-- PROCEDIMIENTOS ALMACENADOS 
-- =========================================================

-- LOGIN
CREATE PROCEDURE sp_ValidarUsuario
    @username VARCHAR(50),
    @password VARCHAR(255)
AS
BEGIN
    SELECT u.id_usuario, u.username, u.id_rol, r.nombre AS rol, u.id_estudiante, u.id_profesor
    FROM usuarios u
    INNER JOIN roles r ON u.id_rol = r.id_rol
    WHERE u.username = @username 
      AND u.password = @password 
      AND u.estado = 1;
END;
GO

-- CRUD ESTUDIANTES
CREATE PROCEDURE sp_ListarEstudiantes
AS
BEGIN
    SELECT e.id_estudiante, 
           e.codigo, 
           e.dni, 
           e.nombres, 
           e.apellidos, 
           c.nombre AS carrera, 
           e.ciclo, 
           e.id_carrera
    FROM estudiantes e
    INNER JOIN carreras c ON e.id_carrera = c.id_carrera
    ORDER BY e.id_estudiante DESC;
END;
GO

CREATE PROCEDURE sp_InsertarEstudiante
    @codigo VARCHAR(20),
    @dni VARCHAR(15),
    @nombres VARCHAR(100),
    @apellidos VARCHAR(100),
    @id_carrera INT,
    @ciclo INT
AS
BEGIN
    INSERT INTO estudiantes (codigo, dni, nombres, apellidos, id_carrera, ciclo)
    VALUES (@codigo, @dni, @nombres, @apellidos, @id_carrera, @ciclo);
END;
GO

CREATE PROCEDURE sp_ActualizarEstudiante
    @id_estudiante INT,
    @codigo VARCHAR(20),
    @dni VARCHAR(15),
    @nombres VARCHAR(100),
    @apellidos VARCHAR(100),
    @id_carrera INT,
    @ciclo INT
AS
BEGIN
    UPDATE estudiantes
    SET codigo = @codigo,
        dni = @dni,
        nombres = @nombres,
        apellidos = @apellidos,
        id_carrera = @id_carrera,
        ciclo = @ciclo
    WHERE id_estudiante = @id_estudiante;
END;
GO

CREATE PROCEDURE sp_EliminarEstudiante
    @id_estudiante INT
AS
BEGIN
    DELETE FROM estudiantes WHERE id_estudiante = @id_estudiante;
END;
GO

CREATE PROCEDURE sp_BuscarEstudiantePorCodigo
    @codigo VARCHAR(20)
AS
BEGIN
    SELECT e.id_estudiante, e.codigo, e.dni, e.nombres, e.apellidos, 
           c.nombre AS carrera, e.ciclo, e.id_carrera
    FROM estudiantes e
    INNER JOIN carreras c ON e.id_carrera = c.id_carrera
    WHERE e.codigo = @codigo;
END;
GO

CREATE PROCEDURE sp_BuscarEstudiantesPorFiltro
    @filtro VARCHAR(100)
AS
BEGIN
    SELECT e.id_estudiante, e.codigo, e.dni, e.nombres, e.apellidos, 
           c.nombre AS carrera, e.ciclo, e.id_carrera
    FROM estudiantes e
    INNER JOIN carreras c ON e.id_carrera = c.id_carrera
    WHERE e.codigo LIKE '%' + @filtro + '%'
       OR e.dni LIKE '%' + @filtro + '%'
       OR e.nombres LIKE '%' + @filtro + '%'
       OR e.apellidos LIKE '%' + @filtro + '%'
    ORDER BY e.apellidos;
END;
GO

-- CARRERAS Y CURSOS
CREATE PROCEDURE sp_ListarCarreras
AS
BEGIN
    SELECT id_carrera, nombre FROM carreras ORDER BY nombre;
END;
GO

CREATE PROCEDURE sp_ListarCursos
AS
BEGIN
    SELECT id_curso, codigo, nombre, creditos FROM cursos ORDER BY nombre;
END;
GO

CREATE PROCEDURE sp_InsertarCurso
    @codigo VARCHAR(20),
    @nombre VARCHAR(100),
    @creditos INT
AS
BEGIN
    INSERT INTO cursos (codigo, nombre, creditos)
    VALUES (@codigo, @nombre, @creditos);
END;
GO

CREATE PROCEDURE sp_ActualizarCurso
    @id_curso INT,
    @codigo VARCHAR(20),
    @nombre VARCHAR(100),
    @creditos INT
AS
BEGIN
    UPDATE cursos
    SET codigo = @codigo,
        nombre = @nombre,
        creditos = @creditos
    WHERE id_curso = @id_curso;
END;
GO

CREATE PROCEDURE sp_EliminarCurso
    @id_curso INT
AS
BEGIN
    DELETE FROM cursos WHERE id_curso = @id_curso;
END;
GO

-- MATRÍCULA
CREATE PROCEDURE sp_ListarSeccionesDisponibles
    @periodo VARCHAR(10)
AS
BEGIN
    SELECT cp.id_curso_profesor, 
           c.codigo AS codigo_curso, 
           c.nombre AS curso, 
           c.creditos,
           CONCAT(p.nombres, ' ', p.apellidos) AS docente,
           h.dia, h.hora_inicio, h.hora_fin, h.aula,
           cp.periodo
    FROM curso_profesor cp
    INNER JOIN cursos c ON cp.id_curso = c.id_curso
    INNER JOIN profesores p ON cp.id_profesor = p.id_profesor
    INNER JOIN horarios h ON cp.id_horario = h.id_horario
    WHERE cp.periodo = @periodo;
END;
GO

CREATE PROCEDURE sp_RegistrarMatricula
    @id_estudiante INT,
    @periodo VARCHAR(10),
    @id_matricula_generado INT OUTPUT
AS
BEGIN
    INSERT INTO matriculas (id_estudiante, fecha_matricula, periodo)
    VALUES (@id_estudiante, GETDATE(), @periodo);
    
    SET @id_matricula_generado = SCOPE_IDENTITY();
END;
GO

CREATE PROCEDURE sp_AgregarDetalleMatricula
    @id_matricula INT,
    @id_curso_profesor INT
AS
BEGIN
    INSERT INTO detalle_matricula (id_matricula, id_curso_profesor)
    VALUES (@id_matricula, @id_curso_profesor);
END;
GO

-- REPORTES
CREATE PROCEDURE sp_ReporteMatriculasPorEstudiante
    @id_estudiante INT
AS
BEGIN
    SELECT m.id_matricula, m.fecha_matricula, m.periodo, 
           c.codigo AS codigo_curso, c.nombre AS curso, c.creditos, 
           CONCAT(p.nombres, ' ', p.apellidos) AS docente, 
           h.dia, h.hora_inicio, h.hora_fin, h.aula
    FROM matriculas m
    INNER JOIN detalle_matricula dm ON m.id_matricula = dm.id_matricula
    INNER JOIN curso_profesor cp ON dm.id_curso_profesor = cp.id_curso_profesor
    INNER JOIN cursos c ON cp.id_curso = c.id_curso
    INNER JOIN profesores p ON cp.id_profesor = p.id_profesor
    INNER JOIN horarios h ON cp.id_horario = h.id_horario
    WHERE m.id_estudiante = @id_estudiante
    ORDER BY m.fecha_matricula DESC;
END;
GO

CREATE PROCEDURE sp_ReporteAlumnosPorCurso
    @id_curso INT,
    @periodo VARCHAR(10)
AS
BEGIN
    SELECT c.nombre AS curso,
           e.codigo AS codigo_estudiante,
           CONCAT(e.apellidos, ', ', e.nombres) AS estudiante,
           car.nombre AS carrera,
           m.fecha_matricula
    FROM detalle_matricula dm
    INNER JOIN matriculas m ON dm.id_matricula = m.id_matricula
    INNER JOIN estudiantes e ON m.id_estudiante = e.id_estudiante
    INNER JOIN carreras car ON e.id_carrera = car.id_carrera
    INNER JOIN curso_profesor cp ON dm.id_curso_profesor = cp.id_curso_profesor
    INNER JOIN cursos c ON cp.id_curso = c.id_curso
    WHERE cp.id_curso = @id_curso AND cp.periodo = @periodo
    ORDER BY e.apellidos;
END;
GO

CREATE PROCEDURE sp_ReporteConsolidadoMatriculas
    @periodo VARCHAR(10)
AS
BEGIN
    SELECT m.id_matricula,
           e.codigo AS codigo_estudiante,
           CONCAT(e.apellidos, ' ', e.nombres) AS estudiante,
           car.nombre AS carrera,
           m.fecha_matricula,
           COUNT(dm.id_curso_profesor) AS total_cursos,
           SUM(c.creditos) AS total_creditos
    FROM matriculas m
    INNER JOIN estudiantes e ON m.id_estudiante = e.id_estudiante
    INNER JOIN carreras car ON e.id_carrera = car.id_carrera
    INNER JOIN detalle_matricula dm ON m.id_matricula = dm.id_matricula
    INNER JOIN curso_profesor cp ON dm.id_curso_profesor = cp.id_curso_profesor
    INNER JOIN cursos c ON cp.id_curso = c.id_curso
    WHERE m.periodo = @periodo
    GROUP BY m.id_matricula, e.codigo, e.apellidos, e.nombres, car.nombre, m.fecha_matricula
    ORDER BY m.fecha_matricula DESC;
END;
GO

PRINT 'Base de datos y tablas creadas correctamente.';
SELECT 'Base de datos y tablas creadas correctamente' AS Mensaje;
GO