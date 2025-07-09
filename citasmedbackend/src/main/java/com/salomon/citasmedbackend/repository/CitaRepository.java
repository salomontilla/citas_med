package com.salomon.citasmedbackend.repository;

import com.salomon.citasmedbackend.domain.cita.Cita;
import com.salomon.citasmedbackend.domain.cita.EstadoCita;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    Page<Cita> findByPacienteId(Long id, Pageable pageable);

    Page<Cita> findByMedicoId(Long id, Pageable pageable);

    @Query("SELECT c FROM Cita c WHERE c.medico.id = :medicoId AND c.fecha = :fecha AND c.estado != 'CANCELADA'")
    List<Cita> findActivasByMedicoAndFecha(@Param("medicoId") Long medicoId, @Param("fecha") LocalDate fecha);

    List<Cita> findByPacienteIdAndFechaBeforeAndEstado(Long pacienteId, Date fecha, EstadoCita estado);


}
