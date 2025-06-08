package com.salomon.citasmedbackend.repository;

import com.salomon.citasmedbackend.domain.paciente.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

}
