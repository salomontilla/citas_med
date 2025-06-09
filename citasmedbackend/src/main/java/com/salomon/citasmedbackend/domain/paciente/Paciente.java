package com.salomon.citasmedbackend.domain.paciente;


import com.salomon.citasmedbackend.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "pacientes")
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    public Paciente(Usuario nuevoUsuario, Date fechaNacimiento) {
        this.usuario = nuevoUsuario;
        this.fechaNacimiento = fechaNacimiento;
    }

    public void updatePaciente(PacienteUpdateDTO pacientesResponseDTO) {
        if (pacientesResponseDTO.fechaNacimiento() != null) {
            this.fechaNacimiento = Date.valueOf(pacientesResponseDTO.fechaNacimiento());
        }
    }

    public void desactivarPaciente() {
        this.usuario.setActivo(false);
    }

    public void activarPaciente() {
        this.usuario.setActivo(true);
    }
}
