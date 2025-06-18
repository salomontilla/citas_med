package com.salomon.citasmedbackend.repository;

import com.salomon.citasmedbackend.domain.cita.Cita;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    Page<Cita> findByPacienteId(Long id, Pageable pageable);

    Page<Cita> findByMedicoId(Long id, Pageable pageable);

    List<Cita> findByMedicoIdAndFecha(Long id, Date fecha);
}
