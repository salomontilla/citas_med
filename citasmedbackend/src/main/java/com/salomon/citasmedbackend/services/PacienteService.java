package com.salomon.citasmedbackend.services;

import com.salomon.citasmedbackend.domain.paciente.Paciente;
import com.salomon.citasmedbackend.domain.paciente.PacienteRegisterDTO;
import com.salomon.citasmedbackend.domain.paciente.PacienteUpdateDTO;
import com.salomon.citasmedbackend.domain.usuario.Rol;
import com.salomon.citasmedbackend.domain.usuario.Usuario;
import com.salomon.citasmedbackend.repository.PacienteRepository;
import com.salomon.citasmedbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;

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

    public Page<Paciente> obtenerPacientes(Pageable paginacion) {
        Page<Paciente> pacientes = pacienteRepository.findAll(paginacion);
        if (pacientes.isEmpty()) {
            return Page.empty();
        }
        return pacientes;
    }

    public Paciente obtenerPacientePorId(Long id){

        return pacienteRepository.findPacienteById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado o inactivo"));
    }

    public Paciente actualizarPaciente(Long id, PacienteUpdateDTO pacienteDto) {
        Paciente paciente = pacienteRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Paciente no encontrado")
        );

        Usuario usuario = paciente.getUsuario();

        // Validación de email
        if (pacienteDto.email() != null &&
                !usuario.getEmail().equals(pacienteDto.email()) &&
                userRepository.existsByEmail(pacienteDto.email())) {
            throw new RuntimeException("El correo electrónico ya está en uso");
        }

        // Validación de documento
        if (pacienteDto.documento() != null &&
                !usuario.getDocumento().equals(pacienteDto.documento()) &&
                userRepository.existsByDocumento(pacienteDto.documento())) {
            throw new RuntimeException("El documento ya está registrado");
        }



        usuario.updateUsuario(pacienteDto);
        if (pacienteDto.contrasena() != null && !pacienteDto.contrasena().isBlank()) {
            String hashedPassword = passwordEncoder.encode(pacienteDto.contrasena());
            usuario.setContrasena(hashedPassword);
        }
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
