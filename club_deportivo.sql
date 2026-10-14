-- Script de creacion de la base de datos "club_deportivo"
-- Gestion de Reservas: una sola entidad principal (reservas), CRUD completo

DROP DATABASE IF EXISTS club_deportivo;
CREATE DATABASE club_deportivo;
USE club_deportivo;

CREATE TABLE reservas (
    idreserva INT NOT NULL AUTO_INCREMENT,
    cancha    VARCHAR(30) NOT NULL,
    cliente   VARCHAR(30) NOT NULL,
    fecha     VARCHAR(10) NOT NULL,  -- formato dd/mm/aaaa
    hora      VARCHAR(5)  NOT NULL,  -- formato HH:mm
    PRIMARY KEY (idreserva)
);

INSERT INTO reservas (cancha, cliente, fecha, hora) VALUES
('Futbol 5',  'Raul Gomez',      '15/10/2026', '18:00'),
('Tenis',     'Natalia Lopez',   '16/10/2026', '09:00'),
('Padel',     'Martin Diaz',     '16/10/2026', '20:00'),
('Rugby',     'Sofia Fernandez', '18/10/2026', '15:00');
