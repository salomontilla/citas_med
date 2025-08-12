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

    public void actualizarMedico(ActualizarMedicoDTO medico) {
        if (medico.nombreCompleto() != null) {
            this.usuario.setNombreCompleto(medico.nombreCompleto());
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
    }

    public void desactivarMedico() {
        this.usuario.setActivo(false);
    }

    public void activarMedico() {
        this.usuario.setActivo(true);
    }

    public void actualizarMedicoAdmin(ActualizarMedicoAdminDTO medicoResponseDTO)
{
        if (medicoResponseDTO.nombre() != null) {
            this.usuario.setNombreCompleto(medicoResponseDTO.nombre());
        }
        if (medicoResponseDTO.email() != null) {
            this.usuario.setEmail(medicoResponseDTO.email());
        }
        if (medicoResponseDTO.contrasena() != null) {
            this.usuario.setContrasena(medicoResponseDTO.contrasena());
        }
        if (medicoResponseDTO.documento() != null) {
            this.usuario.setDocumento(medicoResponseDTO.documento());
        }
        if (medicoResponseDTO.telefono() != null) {
            this.usuario.setTelefono(medicoResponseDTO.telefono());
        }
        if (medicoResponseDTO.especialidad() != null) {
            this.especialidad = Especialidad.valueOf(medicoResponseDTO.especialidad());
        }
    }
}
