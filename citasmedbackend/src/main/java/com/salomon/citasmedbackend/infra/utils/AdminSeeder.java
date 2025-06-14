package com.salomon.citasmedbackend.infra.utils;

import com.salomon.citasmedbackend.domain.usuario.Rol;
import com.salomon.citasmedbackend.domain.usuario.Usuario;
import com.salomon.citasmedbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@correo.com").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNombreCompleto("Administrador");
            admin.setEmail("admin@correo.com");
            admin.setTelefono("0000");
            admin.setDocumento("0000");
            admin.setContrasena(passwordEncoder.encode("admin123"));
            admin.setRol(Rol.ADMIN);
            admin.setActivo(true);
            userRepository.save(admin);
            System.out.println("✅ Admin creado");
        }
    }
}
