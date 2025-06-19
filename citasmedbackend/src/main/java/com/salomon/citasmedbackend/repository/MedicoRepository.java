package com.salomon.citasmedbackend.repository;

import com.salomon.citasmedbackend.domain.medico.Medico;
import com.salomon.citasmedbackend.domain.paciente.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

    @Query("SELECT m FROM Medico m WHERE m.id = :id AND m.usuario.activo = true")
    Optional<Medico> findByIdAndUsuarioActivo(@Param("id") Long id);

    @Query("SELECT m FROM Medico m WHERE m.usuario.activo = true")
    Page<Medico> findAllByUsuarioActivoTrue(Pageable pageable);

    @Query("SELECT m FROM Medico m WHERE m.usuario.email = :email AND m.usuario.activo = true")
    Optional<Medico> findByUsuarioEmailAndUsuarioActivo(@Param("email") String email);

    @Query("SELECT DISTINCT c.paciente FROM Cita c WHERE c.medico.id = :medicoId")
    Page<Paciente> findPacientesByMedicoId(@Param("medicoId") Long medicoId, Pageable pageable);

}
