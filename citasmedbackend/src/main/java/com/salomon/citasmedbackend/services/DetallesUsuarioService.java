package com.salomon.citasmedbackend.services;

import com.salomon.citasmedbackend.domain.usuario.DetallesUsuario;
import com.salomon.citasmedbackend.domain.usuario.Usuario;
import com.salomon.citasmedbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DetallesUsuarioService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public DetallesUsuario loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Usuario> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return new DetallesUsuario(user.get());
        } else {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
    }
}
