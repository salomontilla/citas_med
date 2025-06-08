package com.salomon.citasmedbackend.repository;

import com.salomon.citasmedbackend.domain.paciente.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findById(Long id);
}
