package com.salomon.citasmedbackend.services;

import com.salomon.citasmedbackend.domain.medico.Medico;
import com.salomon.citasmedbackend.domain.medico.MedicoResponseDTO;
import com.salomon.citasmedbackend.domain.medico.RegistrarMedicoDTO;
import com.salomon.citasmedbackend.domain.usuario.Rol;
import com.salomon.citasmedbackend.domain.usuario.Usuario;
import com.salomon.citasmedbackend.repository.MedicoRepository;
import com.salomon.citasmedbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicoService {
    private  final MedicoRepository medicoRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<List<MedicoResponseDTO>> obtenerMedicos(){
        List<Medico> medicos = medicoRepository.findAllByUsuarioActivoTrue();
        if (medicos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<MedicoResponseDTO> medicosResponse =  medicos.stream()
                .map(medico -> new MedicoResponseDTO(
                        medico.getId(),
                        medico.getUsuario().getNombreCompleto(),
                        medico.getUsuario().getEmail(),
                        medico.getUsuario().getDocumento(),
                        medico.getUsuario().getTelefono(),
                        medico.getEspecialidad().toString()
                )).toList();

        return ResponseEntity.ok(medicosResponse);
    }

    public ResponseEntity<MedicoResponseDTO> obtenerMedicoPorId(Long id) {
        Optional<Medico> medicoOptional = medicoRepository.findByIdAndUsuarioActivo(id);
        if (medicoOptional.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        Medico medico = medicoOptional.get();
        MedicoResponseDTO medicoResponse = new MedicoResponseDTO(
                medico.getId(),
                medico.getUsuario().getNombreCompleto(),
                medico.getUsuario().getEmail(),
                medico.getUsuario().getDocumento(),
                medico.getUsuario().getTelefono(),
                medico.getEspecialidad().toString()
        );
        return ResponseEntity.ok(medicoResponse);
    }

    @Transactional
    public ResponseEntity<?> registrarMedico (RegistrarMedicoDTO registrarMedicoDTO) {

        if(userRepository.existsByEmail(registrarMedicoDTO.email())) {
            return ResponseEntity.badRequest().body("El email ya está en uso");
        }
        if(userRepository.existsByDocumento(registrarMedicoDTO.documento())) {
            return ResponseEntity.badRequest().body("El documento ya está registrado");
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


        MedicoResponseDTO medicoResponse = new MedicoResponseDTO(
                nuevoMedico.getId(),
                nuevoMedico.getUsuario().getNombreCompleto(),
                nuevoMedico.getUsuario().getEmail(),
                nuevoMedico.getUsuario().getDocumento(),
                nuevoMedico.getUsuario().getTelefono(),
                nuevoMedico.getEspecialidad().toString()
        );

        return ResponseEntity.status(201).body(medicoResponse);
    }

    public ResponseEntity<?> actualizarMedico(Long id, RegistrarMedicoDTO medicoResponseDTO) {
        Optional<Medico> medicoOptional = medicoRepository.findById(id);
        if (medicoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Medico medico = medicoOptional.get();

        if (medicoResponseDTO.email() != null && !medicoResponseDTO.email().equals(medico.getUsuario().getEmail())) {
            if (userRepository.existsByEmail(medicoResponseDTO.email())) {
                return ResponseEntity.badRequest().body("El email ya está en uso");
            }
        }

        if (medicoResponseDTO.documento() != null && !medicoResponseDTO.documento().equals(medico.getUsuario().getDocumento())) {
            if (userRepository.existsByDocumento(medicoResponseDTO.documento())) {
                return ResponseEntity.badRequest().body("El documento ya está registrado");
            }
        }

        medico.actualizarMedico(medicoResponseDTO);
        medicoRepository.save(medico);
        return ResponseEntity.ok(new MedicoResponseDTO(
                medico.getId(),
                medico.getUsuario().getNombreCompleto(),
                medico.getUsuario().getEmail(),
                medico.getUsuario().getDocumento(),
                medico.getUsuario().getTelefono(),
                medico.getEspecialidad().toString()
        ));

    }

    public ResponseEntity<String> eliminarMedico(Long id) {
        Optional<Medico> medicoOptional = medicoRepository.findById(id);
        if (medicoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Medico medico = medicoOptional.get();

        if (!medico.getUsuario().isActivo()) {
            return ResponseEntity.badRequest().body("El médico ya está eliminado.");
        }

        medico.desactivarMedico();
        medicoRepository.save(medico);
        return ResponseEntity.ok("Médico eliminado correctamente.");
    }

    public ResponseEntity<String> activarMedico(Long id){
        Optional<Medico> medicoOptional = medicoRepository.findById(id);
        if (medicoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Medico medico = medicoOptional.get();
        if (medico.getUsuario().isActivo()) {
            return ResponseEntity.badRequest().body("El médico ya está activo.");
        }

        medico.activarMedico();
        medicoRepository.save(medico);
        return ResponseEntity.ok("Médico activado correctamente.");
    }
}
