package com.salomon.citasmedbackend.repository;

import com.salomon.citasmedbackend.domain.cita.Cita;
import com.salomon.citasmedbackend.domain.medico.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CitasRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByPacienteId(Long id);

    List<Cita> findByMedicoId(Long id);
}
