package com.pulselab.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pulselab.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Optional<Paciente> findByNombre(String nombre);
}
