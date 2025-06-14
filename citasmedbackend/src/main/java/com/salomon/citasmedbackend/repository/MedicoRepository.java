package com.salomon.citasmedbackend.repository;

import com.salomon.citasmedbackend.domain.medico.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

    @Query("SELECT m FROM Medico m WHERE m.id = :id AND m.usuario.activo = true")
    Optional<Medico> findByIdAndUsuarioActivo(@Param("id") Long id);

    @Query("SELECT m FROM Medico m WHERE m.usuario.activo = true")
    List<Medico> findAllByUsuarioActivoTrue();
}
