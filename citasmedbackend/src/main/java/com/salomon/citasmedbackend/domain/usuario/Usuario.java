package com.salomon.citasmedbackend.domain.usuario;

import com.salomon.citasmedbackend.domain.paciente.PacienteUpdateDTO;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "usuarios")
@Getter
@Setter
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreCompleto;
    private String documento;
    private String email;
    private String telefono;
    private String contrasena;
    private boolean activo;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    public Usuario(
            String nombreCompleto,
            String documento,
            String email,
            String telefono,
            String contrasena,
            Rol rol
    ) {
        this.nombreCompleto = nombreCompleto;
        this.documento = documento;
        this.email = email;
        this.telefono = telefono;
        this.contrasena = contrasena;
        this.rol = rol;
        this.activo = true;
    }

    public void updateUsuario (PacienteUpdateDTO usuarioUpdateDTO) {
        if (usuarioUpdateDTO.nombreCompleto() != null) {
            this.nombreCompleto = usuarioUpdateDTO.nombreCompleto();
        }
        if (usuarioUpdateDTO.documento() != null) {
            this.documento = usuarioUpdateDTO.documento();
        }
        if (usuarioUpdateDTO.email() != null) {
            this.email = usuarioUpdateDTO.email();
        }
        if (usuarioUpdateDTO.telefono() != null) {
            this.telefono = usuarioUpdateDTO.telefono();
        }
        if (usuarioUpdateDTO.contrasena() != null) {
            this.contrasena = usuarioUpdateDTO.contrasena();
        }
    }
}
