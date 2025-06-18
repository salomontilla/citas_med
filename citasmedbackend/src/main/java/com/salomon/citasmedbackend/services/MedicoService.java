package com.salomon.citasmedbackend.services;

import com.salomon.citasmedbackend.domain.medico.Medico;
import com.salomon.citasmedbackend.domain.medico.RegistrarMedicoDTO;
import com.salomon.citasmedbackend.domain.usuario.Rol;
import com.salomon.citasmedbackend.domain.usuario.Usuario;
import com.salomon.citasmedbackend.repository.MedicoRepository;
import com.salomon.citasmedbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicoService {
    private  final MedicoRepository medicoRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public Medico registrarMedico (RegistrarMedicoDTO registrarMedicoDTO) {

        if (userRepository.existsByEmail(registrarMedicoDTO.email())) {
            throw new RuntimeException("El email ya está en uso");
        }
        if (userRepository.existsByDocumento(registrarMedicoDTO.documento())) {
            throw new RuntimeException("El documento ya está registrado");
        }

        String hashedPassword = passwordEncoder.encode(registrarMedicoDTO.contrasena());
        Usuario nuevoUsuario = new Usuario(
                registrarMedicoDTO.nombreCompleto(),
                registrarMedicoDTO.documento(),
                registrarMedicoDTO.email(),
                registrarMedicoDTO.telefono(),
                hashedPassword,
                Rol.MEDICO
        );

        Medico nuevoMedico = new Medico(
                nuevoUsuario,
                registrarMedicoDTO.especialidad()
        );
        userRepository.save(nuevoUsuario);
        medicoRepository.save(nuevoMedico);

        return nuevoMedico;
    }

    public Page<Medico> obtenerMedicos(Pageable pageable) {
        Page<Medico> medicos = medicoRepository.findAllByUsuarioActivoTrue(pageable);
        if (medicos.isEmpty()) {
            return Page.empty();
        }
        return medicos;
    }

    public Medico obtenerMedicoPorId(Long id) {
        return medicoRepository.findByIdAndUsuarioActivo(id).orElseThrow(
                () -> new RuntimeException("Médico no encontrado o inactivo")
        );
    }

    public Medico obtenerMedicoPorEmail(String email) {
        return medicoRepository.findByUsuarioEmailAndUsuarioActivo(email)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado o inactivo"));
    }

    public Medico actualizarMedico(Long id, RegistrarMedicoDTO medicoResponseDTO) {

        Medico medico = medicoRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Médico no encontrado")
        );

        if (!medicoResponseDTO.email().equals(medico.getUsuario().getEmail()) && userRepository.existsByEmail(medicoResponseDTO.email())) {
            throw new RuntimeException("El email ya está en uso");
        }

        if (!medicoResponseDTO.documento().equals(medico.getUsuario().getDocumento())
                && userRepository.existsByDocumento(medicoResponseDTO.documento())) {
            throw new RuntimeException("El documento ya está registrado");
        }

        medico.actualizarMedico(medicoResponseDTO);
        medicoRepository.save(medico);

        return medico;
    }

    public String eliminarMedico(Long id) {

        Medico medico = medicoRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Médico no encontrado")
        );

        if (!medico.getUsuario().isActivo()) {
            return "El médico ya está inactivo.";
        }

        medico.desactivarMedico();
        medicoRepository.save(medico);
        return "Médico eliminado correctamente.";
    }

    public String activarMedico(Long id){
        Medico medico = medicoRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Médico no encontrado")
        );
        if (medico.getUsuario().isActivo()) {
            return "El médico ya está activo.";
        }

        medico.activarMedico();
        medicoRepository.save(medico);
        return "Médico activado correctamente.";
    }
}
