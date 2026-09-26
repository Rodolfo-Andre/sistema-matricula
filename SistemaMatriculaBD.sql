DROP DATABASE IF EXISTS sistema_matricula;
 
CREATE DATABASE sistema_matricula
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_spanish_ci;
 
USE sistema_matricula;
 
CREATE TABLE roles (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;
 
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
    ciclo INT NOT NULL DEFAULT 1,
    CONSTRAINT fk_estudiante_carrera 
        FOREIGN KEY (id_carrera) REFERENCES carreras(id_carrera) 
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;
 
CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    id_rol INT NOT NULL,
    id_estudiante INT NULL,
    id_profesor INT NULL,
    estado TINYINT DEFAULT 1,
    CONSTRAINT fk_usuario_rol 
        FOREIGN KEY (id_rol) REFERENCES roles(id_rol) 
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_usuario_estudiante 
        FOREIGN KEY (id_estudiante) REFERENCES estudiantes(id_estudiante) 
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_usuario_profesor 
        FOREIGN KEY (id_profesor) REFERENCES profesores(id_profesor) 
        ON DELETE SET NULL ON UPDATE CASCADE
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
 
-- =========================================================
-- DATOS PRUEBA
-- =========================================================
 
-- Roles para autenticación
INSERT INTO roles (nombre) VALUES 
('ADMINISTRADOR'),
('DOCENTE'),
('ESTUDIANTE');
 
-- Carreras
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
 
-- Profesores
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
 
-- Estudiantes 
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
 
-- Cuentas de Usuario para Login
INSERT INTO usuarios (username, password, id_rol, id_estudiante, id_profesor) VALUES 
('admin', 'admin123', 1, NULL, NULL),
('E001', 'estudiante123', 3, 1, NULL),
('E002', 'estudiante123', 3, 2, NULL),
('prof_carlos', 'docente123', 2, NULL, 1);
 
-- Cursos
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
 
-- Horarios
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
 
-- Asignación Curso - Profesor
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
 
-- Matrículas
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
 
-- Detalle de Matrícula
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
 

-- Insertar 
INSERT INTO usuarios (username, password, id_rol, id_estudiante, id_profesor) VALUES 
('admin', '$2a$12$HfXezO9Q1POJb8Ag3S0eaeqlqScVq97oPLexGTNx0e0xuYZjq6BjW', 1, NULL, NULL),
('E001', '$2a$12$bEkqErdmLWszYbg9voFWf.Pb0U2RT3vzdoKNx6dQ0DyTPCcj2AHjO', 3, 1, NULL),
('E002', '$2a$12$bEkqErdmLWszYbg9voFWf.Pb0U2RT3vzdoKNx6dQ0DyTPCcj2AHjO', 3, 2, NULL),
('prof_carlos', '$2a$12$OhjzeGeEsC7QQjAZfDA.geoAihcTQD37pKXfe/L3cilhYO0L/.NN6', 2, NULL, 1);
 select *from  usuarios;
-- =========================================================
-- PROCEDIMIENTOS ALMACENADOS 
-- =========================================================
 
DELIMITER //
-- LISTAR PROFESORES
DROP PROCEDURE IF EXISTS sp_ListarProfesores //

CREATE PROCEDURE sp_ListarProfesores()
BEGIN
    SELECT id_profesor, dni, nombres, apellidos, especialidad
    FROM profesores
    ORDER BY id_profesor DESC;
END //

DELIMITER ;

-- INSERTAR PROFESOR
DROP PROCEDURE IF EXISTS sp_InsertarProfesor //
CREATE PROCEDURE sp_InsertarProfesor(
    IN p_dni VARCHAR(15),
    IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_especialidad VARCHAR(100)
)
BEGIN
    INSERT INTO profesores (dni, nombres, apellidos, especialidad)
    VALUES (p_dni, p_nombres, p_apellidos, p_especialidad);
END //

-- ACTUALIZAR PROFESOR
DROP PROCEDURE IF EXISTS sp_ActualizarProfesor //
CREATE PROCEDURE sp_ActualizarProfesor(
    IN p_id_profesor INT,
    IN p_dni VARCHAR(15),
    IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_especialidad VARCHAR(100)
)
BEGIN
    UPDATE profesores
    SET dni = p_dni,
        nombres = p_nombres,
        apellidos = p_apellidos,
        especialidad = p_especialidad
    WHERE id_profesor = p_id_profesor;
END //

-- ELIMINAR PROFESOR
DROP PROCEDURE IF EXISTS sp_EliminarProfesor //
CREATE PROCEDURE sp_EliminarProfesor(
    IN p_id_profesor INT
)
BEGIN
    DELETE FROM profesores WHERE id_profesor = p_id_profesor;
END //

 
-- LOGIN

DROP PROCEDURE IF EXISTS sp_ValidarUsuario //

CREATE PROCEDURE sp_ValidarUsuario(
    IN p_username VARCHAR(50)
)
BEGIN
    SELECT u.id_usuario, u.username, u.password, u.id_rol, r.nombre AS rol, 
           u.id_estudiante, u.id_profesor,
           -- Agregamos la búsqueda del nombre real:
           CASE 
               WHEN u.id_estudiante IS NOT NULL THEN (SELECT CONCAT(nombres, ' ', apellidos) FROM estudiantes WHERE id_estudiante = u.id_estudiante)
               WHEN u.id_profesor IS NOT NULL THEN (SELECT CONCAT(nombres, ' ', apellidos) FROM profesores WHERE id_profesor = u.id_profesor)
               ELSE 'Administrador del Sistema'
           END AS nombre_real
    FROM usuarios u
    INNER JOIN roles r ON u.id_rol = r.id_rol
    WHERE u.username = p_username 
      AND u.estado = 1;
END //


-- CRUD Usuarios
-- LISTAR USUARIOS 

DROP PROCEDURE IF EXISTS sp_ListarUsuarios //

CREATE PROCEDURE sp_ListarUsuarios()
BEGIN
    SELECT u.id_usuario, u.username, u.password, u.id_rol, r.nombre AS rol, 
           u.id_estudiante, u.id_profesor, u.estado,
           -- Agregamos la búsqueda del nombre real igual que en el login:
           CASE 
               WHEN u.id_estudiante IS NOT NULL THEN (SELECT CONCAT(nombres, ' ', apellidos) FROM estudiantes WHERE id_estudiante = u.id_estudiante)
               WHEN u.id_profesor IS NOT NULL THEN (SELECT CONCAT(nombres, ' ', apellidos) FROM profesores WHERE id_profesor = u.id_profesor)
               ELSE 'Administrador del Sistema'
           END AS nombre_real
    FROM usuarios u
    INNER JOIN roles r ON u.id_rol = r.id_rol;
END //

-- INSERTAR USUARIO 
CREATE PROCEDURE sp_InsertarUsuario(
    IN p_username VARCHAR(50),
    IN p_password VARCHAR(255),
    IN p_id_rol INT,
    IN p_id_estudiante INT,
    IN p_id_profesor INT
)
BEGIN
    INSERT INTO usuarios (username, password, id_rol, id_estudiante, id_profesor, estado)
    VALUES (p_username, p_password, p_id_rol, p_id_estudiante, p_id_profesor, 1);
END //

--  ACTUALIZAR USUARIO  
CREATE PROCEDURE sp_ActualizarUsuario(
    IN p_id_usuario INT,
    IN p_username VARCHAR(50),
    IN p_password VARCHAR(255),
    IN p_id_rol INT,
    IN p_id_estudiante INT,
    IN p_id_profesor INT,
    IN p_estado TINYINT
)
BEGIN
    UPDATE usuarios
    SET username = p_username,
        password = p_password,
        id_rol = p_id_rol,
        id_estudiante = p_id_estudiante,
        id_profesor = p_id_profesor,
        estado = p_estado
    WHERE id_usuario = p_id_usuario;
END //

-- CAMBIAR ESTADO 
CREATE PROCEDURE sp_CambiarEstadoUsuario(
    IN p_id_usuario INT,
    IN p_estado TINYINT
)
BEGIN
    UPDATE usuarios
    SET estado = p_estado
    WHERE id_usuario = p_id_usuario;
END //

-- ELIMINAR USUARIO 
DROP PROCEDURE IF EXISTS sp_EliminarUsuario //

CREATE PROCEDURE sp_EliminarUsuario(
    IN p_id_usuario INT
)
BEGIN
    DELETE FROM usuarios WHERE id_usuario = p_id_usuario;
END //

-- CRUD ESTUDIANTES
CREATE PROCEDURE sp_ListarEstudiantes()
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
END //
 
CREATE PROCEDURE sp_InsertarEstudiante(
    IN p_codigo VARCHAR(20),
    IN p_dni VARCHAR(15),
    IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_id_carrera INT,
    IN p_ciclo INT
)
BEGIN
    INSERT INTO estudiantes (codigo, dni, nombres, apellidos, id_carrera, ciclo)
    VALUES (p_codigo, p_dni, p_nombres, p_apellidos, p_id_carrera, p_ciclo);
END //
 
CREATE PROCEDURE sp_ActualizarEstudiante(
    IN p_id_estudiante INT,
    IN p_codigo VARCHAR(20),
    IN p_dni VARCHAR(15),
    IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_id_carrera INT,
    IN p_ciclo INT
)
BEGIN
    UPDATE estudiantes
    SET codigo = p_codigo,
        dni = p_dni,
        nombres = p_nombres,
        apellidos = p_apellidos,
        id_carrera = p_id_carrera,
        ciclo = p_ciclo
    WHERE id_estudiante = p_id_estudiante;
END //
 
CREATE PROCEDURE sp_EliminarEstudiante(
    IN p_id_estudiante INT
)
BEGIN
    DELETE FROM estudiantes WHERE id_estudiante = p_id_estudiante;
END //
 
CREATE PROCEDURE sp_BuscarEstudiantePorCodigo(
    IN p_codigo VARCHAR(20)
)
BEGIN
    SELECT e.id_estudiante, e.codigo, e.dni, e.nombres, e.apellidos, 
           c.nombre AS carrera, e.ciclo, e.id_carrera
    FROM estudiantes e
    INNER JOIN carreras c ON e.id_carrera = c.id_carrera
    WHERE e.codigo = p_codigo;
END //
 
CREATE PROCEDURE sp_BuscarEstudiantesPorFiltro(
    IN p_filtro VARCHAR(100)
)
BEGIN
    SELECT e.id_estudiante, e.codigo, e.dni, e.nombres, e.apellidos, 
           c.nombre AS carrera, e.ciclo, e.id_carrera
    FROM estudiantes e
    INNER JOIN carreras c ON e.id_carrera = c.id_carrera
    WHERE e.codigo LIKE CONCAT('%', p_filtro, '%')
       OR e.dni LIKE CONCAT('%', p_filtro, '%')
       OR e.nombres LIKE CONCAT('%', p_filtro, '%')
       OR e.apellidos LIKE CONCAT('%', p_filtro, '%')
    ORDER BY e.apellidos;
END //
 
-- CARRERAS Y CURSOS
CREATE PROCEDURE sp_ListarCarreras()
BEGIN
    SELECT id_carrera, nombre FROM carreras ORDER BY nombre;
END //
 
CREATE PROCEDURE sp_ListarCursos()
BEGIN
    SELECT id_curso, codigo, nombre, creditos FROM cursos ORDER BY nombre;
END //
 
CREATE PROCEDURE sp_InsertarCurso(
    IN p_codigo VARCHAR(20),
    IN p_nombre VARCHAR(100),
    IN p_creditos INT
)
BEGIN
    INSERT INTO cursos (codigo, nombre, creditos)
    VALUES (p_codigo, p_nombre, p_creditos);
END //
 
CREATE PROCEDURE sp_ActualizarCurso(
    IN p_id_curso INT,
    IN p_codigo VARCHAR(20),
    IN p_nombre VARCHAR(100),
    IN p_creditos INT
)
BEGIN
    UPDATE cursos
    SET codigo = p_codigo,
        nombre = p_nombre,
        creditos = p_creditos
    WHERE id_curso = p_id_curso;
END //
 
CREATE PROCEDURE sp_EliminarCurso(
    IN p_id_curso INT
)
BEGIN
    DELETE FROM cursos WHERE id_curso = p_id_curso;
END //
 
-- REPORTES
CREATE PROCEDURE sp_ReporteMatriculasPorEstudiante(
    IN p_id_estudiante INT
)
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
    WHERE m.id_estudiante = p_id_estudiante
    ORDER BY m.fecha_matricula DESC;
END //
 
CREATE PROCEDURE sp_ReporteAlumnosPorCurso(
    IN p_id_curso INT,
    IN p_periodo VARCHAR(10)
)
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
    WHERE cp.id_curso = p_id_curso AND cp.periodo = p_periodo
    ORDER BY e.apellidos;
END //
 
CREATE PROCEDURE sp_ReporteConsolidadoMatriculas(
    IN p_periodo VARCHAR(10)
)
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
    WHERE m.periodo = p_periodo
    GROUP BY m.id_matricula, e.codigo, e.apellidos, e.nombres, car.nombre, m.fecha_matricula
    ORDER BY m.fecha_matricula DESC;
END //
 
-- MATRÍCULA
CREATE PROCEDURE sp_ListarSeccionesDisponibles(
    IN p_periodo VARCHAR(10)
)
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
    WHERE cp.periodo = p_periodo;
END //

CREATE PROCEDURE sp_RegistrarMatricula(
    IN p_id_estudiante INT,
    IN p_periodo VARCHAR(10),
    OUT p_id_matricula_generado INT
)
BEGIN
    INSERT INTO matriculas (id_estudiante, fecha_matricula, periodo, estado)
    VALUES (p_id_estudiante, CURDATE(), p_periodo, 'ACTIVA');
    SET p_id_matricula_generado = LAST_INSERT_ID();
END //

CREATE PROCEDURE sp_AgregarDetalleMatricula(
    IN p_id_matricula INT,
    IN p_id_curso_profesor INT
)
BEGIN
    INSERT INTO detalle_matricula (id_matricula, id_curso_profesor, estado)
    VALUES (p_id_matricula, p_id_curso_profesor, 'ACTIVO');
END //

CREATE PROCEDURE sp_ListarMatriculas(
    IN p_solo_activas TINYINT
)
BEGIN
    SELECT m.id_matricula,
           CONCAT('MAT-', m.id_matricula) AS codigo_matricula,
           e.codigo AS codigo_estudiante,
           CONCAT(e.nombres, ' ', e.apellidos) AS nombre_estudiante,
           COALESCE(c.codigo, 'SIN_CURSO') AS codigo_curso,
           COALESCE(c.nombre, 'Sin curso asignado') AS nombre_curso,
           CONCAT(p.nombres, ' ', p.apellidos) AS nombre_profesor,
           CONCAT(h.dia, ' ', SUBSTRING(h.hora_inicio, 1, 5), '-', SUBSTRING(h.hora_fin, 1, 5), ' Aula:', h.aula) AS horario,
           car.nombre AS nombre_carrera,
           m.fecha_matricula,
           m.periodo,
           m.estado,
           COALESCE(dm.estado, 'ACTIVO') AS estado_detalle
    FROM matriculas m
    JOIN estudiantes e ON m.id_estudiante = e.id_estudiante
    LEFT JOIN carreras car ON e.id_carrera = car.id_carrera
    LEFT JOIN detalle_matricula dm ON m.id_matricula = dm.id_matricula
    LEFT JOIN curso_profesor cp ON dm.id_curso_profesor = cp.id_curso_profesor
    LEFT JOIN cursos c ON cp.id_curso = c.id_curso
    LEFT JOIN profesores p ON cp.id_profesor = p.id_profesor
    LEFT JOIN horarios h ON cp.id_horario = h.id_horario
    WHERE (p_solo_activas = 0 OR (m.estado = 'ACTIVA' AND COALESCE(dm.estado, 'ACTIVO') = 'ACTIVO'))
    ORDER BY m.id_matricula;
END //

CREATE PROCEDURE sp_BuscarCabecera(
    IN p_codigo_estudiante VARCHAR(20),
    IN p_periodo VARCHAR(10)
)
BEGIN
    SELECT m.id_matricula, m.estado, m.fecha_matricula
    FROM matriculas m
    JOIN estudiantes e ON m.id_estudiante = e.id_estudiante
    WHERE e.codigo = p_codigo_estudiante AND m.periodo = p_periodo
    LIMIT 1;
END //

CREATE PROCEDURE sp_CrearCabecera(
    IN p_codigo_estudiante VARCHAR(20),
    IN p_fecha DATE,
    IN p_periodo VARCHAR(10),
    OUT p_id_matricula INT
)
BEGIN
    INSERT INTO matriculas (id_estudiante, fecha_matricula, periodo, estado)
    SELECT e.id_estudiante, p_fecha, p_periodo, 'ACTIVA'
    FROM estudiantes e WHERE e.codigo = p_codigo_estudiante;
    SET p_id_matricula = LAST_INSERT_ID();
END //

CREATE PROCEDURE sp_ReactivarCabecera(
    IN p_id_matricula INT,
    IN p_fecha DATE
)
BEGIN
    UPDATE matriculas SET estado = 'ACTIVA', fecha_matricula = p_fecha
    WHERE id_matricula = p_id_matricula AND estado = 'ANULADA';
END //

CREATE PROCEDURE sp_VerDetalle(
    IN p_id_matricula INT,
    IN p_id_curso_profesor INT
)
BEGIN
    SELECT estado FROM detalle_matricula
    WHERE id_matricula = p_id_matricula AND id_curso_profesor = p_id_curso_profesor
    LIMIT 1;
END //

CREATE PROCEDURE sp_ReactivarDetalle(
    IN p_id_matricula INT,
    IN p_id_curso_profesor INT
)
BEGIN
    UPDATE detalle_matricula SET estado = 'ACTIVO'
    WHERE id_matricula = p_id_matricula AND id_curso_profesor = p_id_curso_profesor;
END //

CREATE PROCEDURE sp_ExisteMatricula(
    IN p_codigo_estudiante VARCHAR(20),
    IN p_codigo_curso VARCHAR(20),
    IN p_periodo VARCHAR(10)
)
BEGIN
    SELECT COUNT(*) AS existe
    FROM matriculas m
    JOIN estudiantes e ON m.id_estudiante = e.id_estudiante
    JOIN detalle_matricula dm ON m.id_matricula = dm.id_matricula
    JOIN curso_profesor cp ON dm.id_curso_profesor = cp.id_curso_profesor
    JOIN cursos c ON cp.id_curso = c.id_curso
    WHERE e.codigo = p_codigo_estudiante AND c.codigo = p_codigo_curso
      AND m.periodo = p_periodo AND m.estado = 'ACTIVA' AND dm.estado = 'ACTIVO';
END //

CREATE PROCEDURE sp_ExisteCursoProfesor(
    IN p_codigo_curso VARCHAR(20),
    IN p_periodo VARCHAR(10)
)
BEGIN
    SELECT COUNT(*) AS existe
    FROM curso_profesor cp
    JOIN cursos c ON cp.id_curso = c.id_curso
    WHERE c.codigo = p_codigo_curso AND cp.periodo = p_periodo;
END //

CREATE PROCEDURE sp_GetIdCursoProfesor(
    IN p_codigo_curso VARCHAR(20),
    IN p_periodo VARCHAR(10)
)
BEGIN
    SELECT cp.id_curso_profesor
    FROM curso_profesor cp
    JOIN cursos c ON cp.id_curso = c.id_curso
    WHERE c.codigo = p_codigo_curso AND cp.periodo = p_periodo
    LIMIT 1;
END //

CREATE PROCEDURE sp_ExisteTraslapeHorario(
    IN p_codigo_estudiante VARCHAR(20),
    IN p_periodo VARCHAR(10),
    IN p_id_curso_profesor INT
)
BEGIN
    SELECT COUNT(*) AS existe
    FROM matriculas m
    JOIN estudiantes e ON m.id_estudiante = e.id_estudiante
    JOIN detalle_matricula dm ON m.id_matricula = dm.id_matricula
    JOIN curso_profesor cp ON dm.id_curso_profesor = cp.id_curso_profesor
    JOIN horarios h ON cp.id_horario = h.id_horario
    JOIN curso_profesor cpN ON cpN.id_curso_profesor = p_id_curso_profesor
    JOIN horarios hN ON cpN.id_horario = hN.id_horario
    WHERE e.codigo = p_codigo_estudiante AND m.periodo = p_periodo
      AND m.estado = 'ACTIVA' AND dm.estado = 'ACTIVO'
      AND h.dia = hN.dia
      AND h.hora_inicio < hN.hora_fin AND h.hora_fin > hN.hora_inicio;
END //

CREATE PROCEDURE sp_AnularPeriodo(
    IN p_id_matricula INT
)
BEGIN
    UPDATE matriculas SET estado = 'ANULADA' WHERE id_matricula = p_id_matricula AND estado = 'ACTIVA';
    UPDATE detalle_matricula SET estado = 'INACTIVO' WHERE id_matricula = p_id_matricula AND estado = 'ACTIVO';
END //

CREATE PROCEDURE sp_QuitarCurso(
    IN p_id_matricula INT,
    IN p_codigo_curso VARCHAR(20)
)
BEGIN
    UPDATE detalle_matricula dm
    JOIN matriculas m ON dm.id_matricula = m.id_matricula
    JOIN curso_profesor cp ON dm.id_curso_profesor = cp.id_curso_profesor
    JOIN cursos c ON cp.id_curso = c.id_curso
    SET dm.estado = 'INACTIVO'
    WHERE dm.id_matricula = p_id_matricula AND c.codigo = p_codigo_curso
      AND m.estado = 'ACTIVA' AND dm.estado = 'ACTIVO';
END //

CREATE PROCEDURE sp_ReactivarCurso(
    IN p_id_matricula INT,
    IN p_codigo_curso VARCHAR(20)
)
BEGIN
    UPDATE matriculas SET estado = 'ACTIVA' WHERE id_matricula = p_id_matricula AND estado = 'ANULADA';
    UPDATE detalle_matricula dm
    JOIN matriculas m ON dm.id_matricula = m.id_matricula
    JOIN curso_profesor cp ON dm.id_curso_profesor = cp.id_curso_profesor
    JOIN cursos c ON cp.id_curso = c.id_curso
    SET dm.estado = 'ACTIVO'
    WHERE dm.id_matricula = p_id_matricula AND c.codigo = p_codigo_curso
      AND m.estado = 'ACTIVA' AND dm.estado = 'INACTIVO';
END //

CREATE PROCEDURE sp_TotalCabecerasActivas()
BEGIN
    SELECT COUNT(*) AS total FROM matriculas WHERE estado = 'ACTIVA';
END //

CREATE PROCEDURE sp_GetIdMatriculaByCodigo(
    IN p_codigo_matricula VARCHAR(20)
)
BEGIN
    SELECT id_matricula, estado FROM matriculas
    WHERE CONCAT('MAT-', id_matricula) = p_codigo_matricula LIMIT 1;
END //
 
DELIMITER ;