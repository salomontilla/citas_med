CREATE TABLE usuarios (
                          id_usuario INT AUTO_INCREMENT PRIMARY KEY,
                          nombre_completo VARCHAR(100) NOT NULL,
                          email VARCHAR(100) NOT NULL UNIQUE,
                          contraseña VARCHAR(255) NOT NULL,
                          telefono VARCHAR(20),
                          activo tinyint,
                          rol ENUM('PACIENTE', 'MEDICO', 'ADMIN') NOT NULL
);
CREATE TABLE medicos (
                         id_medico INT AUTO_INCREMENT PRIMARY KEY,
                         id_usuario INT NOT NULL UNIQUE,
                         especialidad ENUM('GENERAL', 'PEDIATRIA', 'CARDIOLOGIA') NOT NULL,
                         FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);
CREATE TABLE pacientes (
                           id_paciente INT AUTO_INCREMENT PRIMARY KEY,
                           id_usuario INT NOT NULL UNIQUE,
                           fecha_nacimiento DATE NOT NULL,
                           direccion VARCHAR(255),
                           FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);
CREATE TABLE disponibilidades (
                                id_disponibilidad INT AUTO_INCREMENT PRIMARY KEY,
                                id_medico INT NOT NULL,
                                dia_semana ENUM('LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES', 'SABADO', 'DOMINGO') NOT NULL,
                                hora_inicio TIME NOT NULL,
                                hora_fin TIME NOT NULL,
                                FOREIGN KEY (id_medico) REFERENCES medicos(id_medico)
);
;
CREATE TABLE citas (
                       id_cita INT AUTO_INCREMENT PRIMARY KEY,
                       id_paciente INT NOT NULL,
                       id_medico INT NOT NULL,
                       fecha DATE NOT NULL,
                       hora TIME NOT NULL,
                       estado ENUM('PENDIENTE', 'ATENDIDA', 'CANCELADA') DEFAULT 'PENDIENTE',
                       FOREIGN KEY (id_paciente) REFERENCES pacientes(id_paciente),
                       FOREIGN KEY (id_medico) REFERENCES medicos(id_medico)
);