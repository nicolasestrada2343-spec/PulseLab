-- =============================================================
-- PulseLab — Database Schema (SQL Script)
-- =============================================================
-- Engine: H2 Database (SQL standard, compatible with MySQL/PostgreSQL
-- with minor syntax adjustments).
--
-- This script is provided as a deliverable for documentation purposes.
-- In the running application, Spring Data JPA / Hibernate creates and
-- updates these same tables automatically (spring.jpa.hibernate.ddl-auto
-- =update) based on the entity classes Medico.java and Paciente.java.
-- =============================================================

DROP TABLE IF EXISTS pacientes;
DROP TABLE IF EXISTS medicos;

-- =============================================================
-- Table: medicos
-- Maps to: com.pulselab.model.Medico
-- =============================================================
CREATE TABLE medicos (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL,
    apellido        VARCHAR(100) NOT NULL,
    matricula       VARCHAR(50)  NOT NULL UNIQUE,
    especialidad    VARCHAR(100)
);

-- =============================================================
-- Table: pacientes
-- Maps to: com.pulselab.model.Paciente
-- The columns frecuencia_cardiaca, saturacion_oxigeno, presion_sistolica,
-- presion_diastolica, frecuencia_respiratoria and temperatura come from
-- the embedded SignosVitales (@Embeddable) object.
-- =============================================================
CREATE TABLE pacientes (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre                  VARCHAR(150) NOT NULL,
    edad                    INT,
    genero                  VARCHAR(10),
    peso                    DOUBLE,
    altura                  DOUBLE,
    historial               VARCHAR(2000),

    -- Embedded: SignosVitales
    frecuencia_cardiaca     INT,
    saturacion_oxigeno      INT,
    presion_sistolica       INT,
    presion_diastolica      INT,
    frecuencia_respiratoria INT,
    temperatura             DOUBLE,

    -- Diagnosis / exam mode state
    ritmo_asignado          VARCHAR(50),
    modo_examen             BOOLEAN DEFAULT FALSE,
    diagnosticado           BOOLEAN DEFAULT FALSE,
    diagnostico_correcto    BOOLEAN DEFAULT FALSE,
    respuesta_estudiante    VARCHAR(100),

    -- Foreign key: Paciente N..1 Medico
    medico_id               BIGINT,
    CONSTRAINT fk_paciente_medico
        FOREIGN KEY (medico_id) REFERENCES medicos(id)
        ON DELETE SET NULL
);

-- =============================================================
-- Sample data (matches the seed data created by Simulador.java
-- on first application startup)
-- =============================================================

INSERT INTO medicos (nombre, apellido, matricula, especialidad)
VALUES ('Roberto', 'García', 'CR-4521', 'Cardiología');

INSERT INTO pacientes (
    nombre, edad, genero, peso, altura, historial,
    frecuencia_cardiaca, saturacion_oxigeno, presion_sistolica,
    presion_diastolica, frecuencia_respiratoria, temperatura,
    ritmo_asignado, modo_examen, diagnosticado, diagnostico_correcto,
    medico_id
) VALUES (
    'Juan Carlos Pérez', 54, 'M', 78, 175, '',
    72, 98, 120, 80, 16, 36.7,
    'NORMAL', FALSE, FALSE, FALSE,
    (SELECT id FROM medicos WHERE matricula = 'CR-4521')
);

-- =============================================================
-- Example queries (CRUD demonstration)
-- =============================================================

-- SELECT: list every patient with their assigned doctor
-- SELECT p.nombre AS paciente, p.ritmo_asignado, m.nombre AS medico
-- FROM pacientes p
-- LEFT JOIN medicos m ON p.medico_id = m.id;

-- UPDATE: register a student's diagnosis result
-- UPDATE pacientes
-- SET diagnosticado = TRUE, diagnostico_correcto = TRUE, respuesta_estudiante = 'TAQUICARDIA'
-- WHERE nombre = 'Juan Carlos Pérez';

-- DELETE: remove a patient
-- DELETE FROM pacientes WHERE nombre = 'Juan Carlos Pérez';
