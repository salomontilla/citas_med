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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static com.salomon.citasmedbackend.infra.utils.FechaUtils.convertirFecha;

@Service
@RequiredArgsConstructor
public class PacienteService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PacienteRepository pacienteRepository;

    public Paciente registrarPaciente(PacienteRegisterDTO pacienteDto) {
        if (userRepository.existsByEmail(pacienteDto.email())) {
            throw new RuntimeException("El email ya está en uso");
        }
        if (userRepository.existsByDocumento(pacienteDto.documento())) {
            throw new RuntimeException("El documento ya está registrado");
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
        Date fechaNacimiento = convertirFecha(pacienteDto.fechaNacimiento());
        Paciente paciente = new Paciente(
                nuevoUsuario,
                fechaNacimiento
        );

        userRepository.save(nuevoUsuario);
        pacienteRepository.save(paciente);

        return paciente;
    }

    public List<Paciente> obtenerPacientes(){
        List<Paciente> pacientes = pacienteRepository.findAllByUsuarioActivoTrue();
        if (pacientes.isEmpty()) {
            return List.of();
        }
        return pacientes;
    }

    public Paciente obtenerPacientePorId(Long id){

        return pacienteRepository.findByIdAndUsuarioActivo(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado o inactivo"));
    }

    public Paciente actualizarPaciente(Long id, PacienteUpdateDTO pacienteDto) {
        Paciente paciente = pacienteRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Paciente no encontrado")
        );

        Usuario usuario = paciente.getUsuario();

        if (!usuario.getEmail().equals(pacienteDto.email()) && userRepository.existsByEmail(pacienteDto.email())) {
            throw new RuntimeException("El correo electrónico ya está en uso");
        }

        if (!usuario.getDocumento().equals(pacienteDto.documento()) && userRepository.existsByDocumento(pacienteDto.documento())) {
            throw new RuntimeException("El documento ya está registrado");
        }

        if (pacienteDto.contrasena() != null && !pacienteDto.contrasena().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(pacienteDto.contrasena());
            usuario.setContrasena(hashedPassword);
        }

        usuario.updateUsuario(pacienteDto);
        paciente.updatePaciente(pacienteDto);

        userRepository.save(usuario);
        pacienteRepository.save(paciente);

        return paciente;
    }

    public String eliminarPaciente(Long id) {

        Paciente paciente = pacienteRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Paciente no encontrado")
        );

        if (!paciente.getUsuario().isActivo()) {
            return ("El paciente ya está eliminado");
        }
        paciente.desactivarPaciente();
        pacienteRepository.save(paciente);

        return ("Paciente eliminado exitosamente");
    }

    public String activarPaciente (Long id){

        Paciente paciente = pacienteRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Paciente no encontrado")
        );

        if(paciente.getUsuario().isActivo()){
            return ("El paciente ya está activo");
        }

        paciente.activarPaciente();
        pacienteRepository.save(paciente);

        return ("Paciente activado exitosamente");
    }

    public Paciente obtenerPacientePorEmail(String email) {
        return pacienteRepository.findByUsuarioEmailAndUsuarioActivo(email)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado o inactivo"));
    }
}
