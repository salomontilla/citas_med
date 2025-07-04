package com.salomon.citasmedbackend.repository;

import com.salomon.citasmedbackend.domain.usuario.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByDocumento(String documento);
    boolean existsByEmail(String email);
    boolean existsByDocumento(String email);
    Optional<Usuario> findById(Long id);

}
