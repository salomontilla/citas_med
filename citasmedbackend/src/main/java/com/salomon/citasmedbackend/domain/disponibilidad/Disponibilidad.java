package com.salomon.citasmedbackend.domain.disponibilidad;

import com.salomon.citasmedbackend.domain.medico.Medico;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "disponibilidades")
public class Disponibilidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_medico", nullable = false)
    private Medico medico;

    @Enumerated(EnumType.STRING)
    private DiaSemana diaSemana;

    @Temporal(TemporalType.TIME)
    private Time horaInicio;

    @Temporal(TemporalType.TIME)
    private Time horaFin;
}
