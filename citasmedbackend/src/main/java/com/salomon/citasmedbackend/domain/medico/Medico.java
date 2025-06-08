package com.salomon.citasmedbackend.domain.medico;

import com.salomon.citasmedbackend.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "medicos")
public class Medico {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    private Especialidad especialidad;
}
