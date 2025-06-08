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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    private Especialidad especialidad;

    public Medico(Usuario nuevoUsuario, String especialidad) {
        this.usuario = nuevoUsuario;
        this.especialidad = Especialidad.valueOf(especialidad);
    }

    public void actualizarMedico(RegistrarMedicoDTO medico) {
        if (medico.nombre() != null) {
            this.usuario.setNombreCompleto(medico.nombre());
        }
        if(medico.email() != null) {
            this.usuario.setEmail(medico.email());
        }
        if (medico.contrasena() != null) {
            this.usuario.setContrasena(medico.contrasena());
        }
        if (medico.documento() != null) {
            this.usuario.setDocumento(medico.documento());
        }
        if (medico.telefono() != null) {
            this.usuario.setTelefono(medico.telefono());
        }
        if (medico.especialidad() != null) {
            this.especialidad = Especialidad.valueOf(medico.especialidad());
        }
    }
}
