package com.salomon.citasmedbackend.domain.usuario;

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
}
