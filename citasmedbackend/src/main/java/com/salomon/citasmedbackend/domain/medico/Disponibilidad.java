package com.salomon.citasmedbackend.domain.medico;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.sql.Time;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "disponibilidades")
public class Disponibilidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Medico medico;

    @Enumerated(EnumType.STRING)
    private DiaSemana diaSemana;

    @Temporal(TemporalType.TIME)
    private Time horaInicio;

    @Temporal(TemporalType.TIME)
    private Time horaFin;
}
