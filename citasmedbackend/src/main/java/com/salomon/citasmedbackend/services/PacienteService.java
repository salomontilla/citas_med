package com.salomon.citasmedbackend.services;

import com.salomon.citasmedbackend.domain.paciente.Paciente;
import com.salomon.citasmedbackend.domain.paciente.PacienteRegisterDTO;
import com.salomon.citasmedbackend.domain.paciente.PacienteUpdateDTO;
import com.salomon.citasmedbackend.domain.paciente.PacientesResponseDTO;
import com.salomon.citasmedbackend.domain.usuario.Rol;
import com.salomon.citasmedbackend.domain.usuario.Usuario;
import com.salomon.citasmedbackend.repository.PacienteRepository;
import com.salomon.citasmedbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PacienteService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PacienteRepository pacienteRepository;

    public ResponseEntity<List<PacientesResponseDTO>> obtenerPacientes(){
        List<Paciente> pacientes = pacienteRepository.findAllByUsuarioActivoTrue();
        if (pacientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<PacientesResponseDTO> pacientesResponse = pacientes.stream()
                .map(paciente -> new PacientesResponseDTO(
                        paciente.getId(),
                        paciente.getUsuario().getNombreCompleto(),
                        paciente.getUsuario().getDocumento(),
                        paciente.getUsuario().getEmail(),
                        paciente.getUsuario().getTelefono(),
                        paciente.getFechaNacimiento()
                        )).toList();
        return ResponseEntity.ok(pacientesResponse);
    }

    public ResponseEntity<PacientesResponseDTO> obtenerPacientePorId(Long id){

        Optional<Paciente> pacienteOptional = pacienteRepository.findById(id);

        return pacienteOptional.map(paciente -> ResponseEntity.ok(new PacientesResponseDTO(
                paciente.getId(),
                paciente.getUsuario().getNombreCompleto(),
                paciente.getUsuario().getDocumento(),
                paciente.getUsuario().getEmail(),
                paciente.getUsuario().getTelefono(),
                paciente.getFechaNacimiento()
        ))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<?> registrarPaciente(PacienteRegisterDTO pacienteDto) {
        if (userRepository.existsByEmail(pacienteDto.email())) {
            return ResponseEntity.badRequest().body("El correo electrónico ya está en uso");
        }
        if (userRepository.existsByDocumento(pacienteDto.documento())) {
            return ResponseEntity.badRequest().body("El documento ya está registrado");
        }

        String hashedPassword = passwordEncoder.encode(pacienteDto.contrasena());
        Usuario nuevoUsuario = new Usuario(
                pacienteDto.nombreCompleto(),
                pacienteDto.documento(),
                pacienteDto.email(),
                pacienteDto.telefono(),
                hashedPassword,
                Rol.PACIENTE
        );
        Date fechaNacimiento = Date.valueOf(pacienteDto.fechaNacimiento());
        Paciente paciente = new Paciente(
                nuevoUsuario,
                fechaNacimiento
        );

        userRepository.save(nuevoUsuario);
        pacienteRepository.save(paciente);

        PacientesResponseDTO response = new PacientesResponseDTO(
                paciente.getId(),
                paciente.getUsuario().getNombreCompleto(),
                paciente.getUsuario().getDocumento(),
                paciente.getUsuario().getEmail(),
                paciente.getUsuario().getTelefono(),
                paciente.getFechaNacimiento()
        );

        return ResponseEntity.status(201).body(response );
    }

    @Transactional
    public ResponseEntity<?> actualizarPaciente(Long id, PacienteUpdateDTO pacienteDto) {
        Optional<Paciente> pacienteOptional = pacienteRepository.findById(id);
        if (pacienteOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Paciente no encontrado");
        }

        Paciente paciente = pacienteOptional.get();
        Usuario usuario = paciente.getUsuario();

        if (!usuario.getEmail().equals(pacienteDto.email()) && userRepository.existsByEmail(pacienteDto.email())) {
            return ResponseEntity.badRequest().body("El correo electrónico ya está en uso");
        }

        if (!usuario.getDocumento().equals(pacienteDto.documento()) && userRepository.existsByDocumento(pacienteDto.documento())) {
            return ResponseEntity.badRequest().body("El documento ya está registrado");
        }

        if (pacienteDto.contrasena() != null && !pacienteDto.contrasena().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(pacienteDto.contrasena());
            usuario.setContrasena(hashedPassword);
        }

        usuario.updateUsuario(pacienteDto);
        paciente.updatePaciente(pacienteDto);

        userRepository.save(usuario);
        pacienteRepository.save(paciente);

        PacientesResponseDTO response = new PacientesResponseDTO(
                paciente.getId(),
                paciente.getUsuario().getNombreCompleto(),
                paciente.getUsuario().getDocumento(),
                paciente.getUsuario().getEmail(),
                paciente.getUsuario().getTelefono(),
                paciente.getFechaNacimiento()
        );

        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<?> eliminarPaciente(Long id) {
        Optional<Paciente> pacienteOptional = pacienteRepository.findByIdAndUsuarioActivo(id);
        if (pacienteOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Paciente no encontrado");
        }

        Paciente paciente = pacienteOptional.get();

        if (!paciente.getUsuario().isActivo()) {
            return ResponseEntity.badRequest().body("El paciente ya está eliminado");
        }
        paciente.desactivarPaciente();

        pacienteRepository.save(paciente);

        return ResponseEntity.ok("Paciente eliminado exitosamente");
    }

    @Transactional
    public ResponseEntity<?> activarPaciente (Long id){
        Optional<Paciente> pacienteOptional = pacienteRepository.findById(id);
        if (pacienteOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Paciente no encontrado");
        }

        Paciente paciente = pacienteOptional.get();

        if(paciente.getUsuario().isActivo()){
            return ResponseEntity.badRequest().body("El paciente ya está activo");
        }

        paciente.activarPaciente();
        pacienteRepository.save(paciente);

        return ResponseEntity.ok("Paciente activado exitosamente");
    }
}
