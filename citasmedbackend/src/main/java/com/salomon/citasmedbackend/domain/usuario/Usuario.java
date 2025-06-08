package com.salomon.citasmedbackend.domain.usuario;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreCompleto;
    private String email;
    private String telefono;
    private String contrasena;
    private boolean activo;

    @Enumerated(EnumType.STRING)
    private Rol rol;
}
