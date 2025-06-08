CREATE TABLE usuarios (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          nombre_completo VARCHAR(100) NOT NULL,
                          documento VARCHAR(100) NOT NULL UNIQUE,
                          email VARCHAR(100) NOT NULL UNIQUE,
                          contrasena VARCHAR(255) NOT NULL,
                          telefono VARCHAR(20),
                          activo tinyint,
                          rol ENUM('PACIENTE', 'MEDICO', 'ADMIN') NOT NULL
);
CREATE TABLE medicos (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         id_usuario BIGINT NOT NULL UNIQUE,
                         especialidad ENUM('GENERAL', 'PEDIATRIA', 'CARDIOLOGIA') NOT NULL,
                         FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);
CREATE TABLE pacientes (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           id_usuario BIGINT NOT NULL UNIQUE,
                           fecha_nacimiento DATE NOT NULL,

                           FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);
CREATE TABLE disponibilidades (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                id_medico BIGINT NOT NULL,
                                dia_semana ENUM('LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES', 'SABADO', 'DOMINGO') NOT NULL,
                                hora_inicio TIME NOT NULL,
                                hora_fin TIME NOT NULL,
                                FOREIGN KEY (id_medico) REFERENCES medicos(id)
);
;
CREATE TABLE citas (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       id_paciente BIGINT NOT NULL,
                       id_medico BIGINT NOT NULL,
                       fecha DATE NOT NULL,
                       hora TIME NOT NULL,
                       estado ENUM('PENDIENTE', 'ATENDIDA', 'CANCELADA') DEFAULT 'PENDIENTE',
                       FOREIGN KEY (id_paciente) REFERENCES pacientes(id),
                       FOREIGN KEY (id_medico) REFERENCES medicos(id)
);