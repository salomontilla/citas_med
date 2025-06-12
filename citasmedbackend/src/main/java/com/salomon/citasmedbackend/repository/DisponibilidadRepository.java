package com.salomon.citasmedbackend.repository;

import com.salomon.citasmedbackend.domain.disponibilidad.DiaSemana;
import com.salomon.citasmedbackend.domain.disponibilidad.Disponibilidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Long> {
    Optional<Disponibilidad> findDisponibilidadByMedicoIdAndDiaSemana(Long medicoId, DiaSemana diaSemana);

    List<Disponibilidad> findByMedicoIdAndDiaSemana(Long id, DiaSemana diaSemana);

    List<Disponibilidad> findByMedicoId(Long id);
}
