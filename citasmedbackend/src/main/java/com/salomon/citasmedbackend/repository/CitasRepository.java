package com.salomon.citasmedbackend.repository;

import com.salomon.citasmedbackend.domain.cita.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CitasRepository extends JpaRepository<Cita, Long> {

}
