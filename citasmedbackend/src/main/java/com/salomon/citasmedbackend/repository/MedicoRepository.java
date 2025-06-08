package com.salomon.citasmedbackend.repository;

import com.salomon.citasmedbackend.domain.medico.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

}
