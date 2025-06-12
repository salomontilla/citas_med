package com.salomon.citasmedbackend.repository;

import com.salomon.citasmedbackend.domain.cita.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByPacienteId(Long id);

    List<Cita> findByMedicoId(Long id);

    List<Cita> findByMedicoIdAndFecha(Long id, Date fecha);
}
