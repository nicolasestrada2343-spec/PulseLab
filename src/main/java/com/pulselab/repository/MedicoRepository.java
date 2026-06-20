package com.pulselab.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pulselab.model.Medico;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

    Optional<Medico> findByMatricula(String matricula);
}
