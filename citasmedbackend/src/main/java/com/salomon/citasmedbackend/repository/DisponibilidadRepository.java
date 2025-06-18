package com.salomon.citasmedbackend.repository;

import com.salomon.citasmedbackend.domain.disponibilidad.DiaSemana;
import com.salomon.citasmedbackend.domain.disponibilidad.Disponibilidad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Long> {
    List<Disponibilidad> findDisponibilidadByMedicoIdAndDiaSemana(Long medicoId, DiaSemana diaSemana);

    List<Disponibilidad> findByMedicoIdAndDiaSemana(Long id, DiaSemana diaSemana);

    Page<Disponibilidad> findByMedicoId(Long id, Pageable pageable);
}
