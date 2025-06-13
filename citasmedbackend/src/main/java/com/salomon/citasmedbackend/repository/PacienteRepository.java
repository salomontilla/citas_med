package com.salomon.citasmedbackend.repository;

import com.salomon.citasmedbackend.domain.paciente.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    @Query("SELECT p FROM Paciente p WHERE p.id = :id AND p.usuario.activo = true")
    Optional<Paciente> findByIdAndUsuarioActivo(@Param("id") Long id);

    @Query("SELECT p FROM Paciente p WHERE p.usuario.activo = true")
    List<Paciente> findAllByUsuarioActivoTrue();

    @Query("SELECT p FROM Paciente p WHERE p.usuario.email = :email AND p.usuario.activo = true")
    Optional<Paciente> findByUsuarioEmailAndUsuarioActivo(@Param("email") String email);
}
