package com.salomon.citasmedbackend.controller;

import com.salomon.citasmedbackend.domain.cita.Cita;
import com.salomon.citasmedbackend.domain.cita.CitaActualizarDTO;
import com.salomon.citasmedbackend.domain.cita.CitaResponseDTO;
import com.salomon.citasmedbackend.domain.disponibilidad.Disponibilidad;
import com.salomon.citasmedbackend.domain.disponibilidad.DisponibilidadDTO;
import com.salomon.citasmedbackend.domain.disponibilidad.DisponibilidadResponseDTO;
import com.salomon.citasmedbackend.domain.disponibilidad.UpdateDisponibilidadDTO;
import com.salomon.citasmedbackend.domain.medico.*;
import com.salomon.citasmedbackend.domain.paciente.Paciente;
import com.salomon.citasmedbackend.domain.paciente.PacienteUpdateDTO;
import com.salomon.citasmedbackend.domain.paciente.PacientesResponseAdminDTO;
import com.salomon.citasmedbackend.domain.paciente.PacientesResponseDTO;
import com.salomon.citasmedbackend.domain.usuario.DetallesUsuario;
import com.salomon.citasmedbackend.domain.usuario.UserResponseDTO;
import com.salomon.citasmedbackend.domain.usuario.Usuario;
import com.salomon.citasmedbackend.repository.UserRepository;
import com.salomon.citasmedbackend.services.CitaService;
import com.salomon.citasmedbackend.services.DisponibilidadService;
import com.salomon.citasmedbackend.services.MedicoService;
import com.salomon.citasmedbackend.services.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/citasmed/admin")
public class AdminController {

    private final PacienteService pacienteService;
    private final MedicoService medicoService;
    private final DisponibilidadService disponibilidadService;
    private final CitaService citaService;
    private final UserRepository userRepository;

    // operations for Usuarios
    @GetMapping("/users")
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(
            @PageableDefault(size = 5) Pageable paginacion
    ) {
        Page<Usuario> usuarios = userRepository.findAll(paginacion);
        return ResponseEntity.ok( usuarios
                .map(usuario -> new UserResponseDTO(
                        usuario.getId(),
                        usuario.getNombreCompleto(),
                        usuario.getEmail(),
                        usuario.getTelefono(),
                        usuario.getDocumento(),
                        usuario.isActivo(),
                        usuario.getRol().name()
                ))
        );
    }

    // CRUD operations for Paciente
    @GetMapping("/pacientes")
    public ResponseEntity<Page<PacientesResponseAdminDTO>> getAllPacientes(
            @PageableDefault(size = 8) Pageable paginacion,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String estado
            ) {
        Page<Paciente> pacientes = pacienteService.obtenerPacientes(paginacion, search, estado);
        return ResponseEntity.ok().body(pacientes
                .map(paciente -> new PacientesResponseAdminDTO(
                        paciente.getId(),
                        paciente.getUsuario().getNombreCompleto(),
                        paciente.getUsuario().getDocumento(),
                        paciente.getUsuario().getEmail(),
                        paciente.getUsuario().getTelefono(),
                        paciente.getUsuario().isActivo(),
                        paciente.getFechaNacimiento()
                )));
    }

    @GetMapping("/pacientes/{id}")
    public ResponseEntity<PacientesResponseDTO> getPacienteById(@PathVariable Long id) {
        Paciente paciente = pacienteService.obtenerPacientePorId(id);
        return ResponseEntity.ok(new PacientesResponseDTO(
                paciente.getId(),
                paciente.getUsuario().getNombreCompleto(),
                paciente.getUsuario().getDocumento(),
                paciente.getUsuario().getEmail(),
                paciente.getUsuario().getTelefono(),
                paciente.getFechaNacimiento(),
                paciente.getUsuario().isActivo()
        ));
    }

    @PatchMapping("/pacientes/editar/{id}")
    @Transactional
    public ResponseEntity<?> updatePaciente(@PathVariable Long id, @RequestBody PacienteUpdateDTO usuarioDto) {
        Paciente pacienteActualizado = pacienteService.actualizarPaciente(id, usuarioDto);
        return ResponseEntity.ok(new PacientesResponseDTO(
                pacienteActualizado.getId(),
                pacienteActualizado.getUsuario().getNombreCompleto(),
                pacienteActualizado.getUsuario().getDocumento(),
                pacienteActualizado.getUsuario().getEmail(),
                pacienteActualizado.getUsuario().getTelefono(),
                pacienteActualizado.getFechaNacimiento(),
                pacienteActualizado.getUsuario().isActivo()
        ));
    }

    @PatchMapping("/pacientes/activar/{id}")
    @Transactional
    public ResponseEntity<?> activarPaciente (@PathVariable Long id){
        return ResponseEntity.ok().body(pacienteService.activarPaciente(id));
    }

    @DeleteMapping("/pacientes/{id}")
    @Transactional
    public ResponseEntity<?> deletePaciente(@PathVariable Long id) {
        return ResponseEntity.ok().body(pacienteService.eliminarPaciente(id));
    }

    // CRUD operations for Medico
    //CREATE
    @PostMapping("/medicos/register")
    @Transactional
    public ResponseEntity<?> createMedico(@RequestBody @Valid RegistrarMedicoDTO medicoResponseDTO,
                                          UriComponentsBuilder uriBuilder) {
        Medico nuevoMedico = medicoService.registrarMedico(medicoResponseDTO);
        MedicoResponseDTO medicoResponse = new MedicoResponseDTO(
                nuevoMedico.getId(),
                nuevoMedico.getUsuario().getNombreCompleto(),
                nuevoMedico.getUsuario().getEmail(),
                nuevoMedico.getUsuario().getDocumento(),
                nuevoMedico.getUsuario().getTelefono(),
                nuevoMedico.getEspecialidad().toString()
        );
        URI location = uriBuilder
                .path("/api/citasmed/medicos/{id}")
                .buildAndExpand(nuevoMedico.getId())
                .toUri();
        return ResponseEntity.created(location).body(medicoResponse);
    }

    @GetMapping("/medicos")
    public ResponseEntity<Page<MedicoResponseAdminDTO>> getAllMedicos (@PageableDefault(size = 8) Pageable paginacion,
                                                                  @RequestParam(required = false) String search,
                                                                  @RequestParam(required = false) String estado) {

        return ResponseEntity.ok(medicoService.obtenerMedicos(paginacion, search, estado).map(
                medico -> new MedicoResponseAdminDTO(
                        medico.getId(),
                        medico.getUsuario().getNombreCompleto(),
                        medico.getUsuario().getEmail(),
                        medico.getUsuario().getDocumento(),
                        medico.getUsuario().getTelefono(),
                        medico.getUsuario().isActivo(),
                        medico.getEspecialidad().toString()
                )
        ));
    }

    @GetMapping("/medicos/{id}")
    public ResponseEntity<MedicoResponseAdminDTO> getMedicoById(@PathVariable Long id) {
        Medico medico = medicoService.obtenerMedicoPorId(id);
        return ResponseEntity.ok(new MedicoResponseAdminDTO(
                medico.getId(),
                medico.getUsuario().getNombreCompleto(),
                medico.getUsuario().getEmail(),
                medico.getUsuario().getDocumento(),
                medico.getUsuario().getTelefono(),
                medico.getUsuario().isActivo(),
                medico.getEspecialidad().toString()
        ));
    }

    //UPDATE
    @PatchMapping("/medicos/editar/{id}")
    @Transactional
    public ResponseEntity<?> updateMedico(@PathVariable Long id,
                                          @RequestBody ActualizarMedicoAdminDTO medicoResponseDTO) {
        Medico medico = medicoService.actualizarMedicoAdmin(id, medicoResponseDTO);

        return ResponseEntity.ok(new MedicoResponseDTO(
                medico.getId(),
                medico.getUsuario().getNombreCompleto(),
                medico.getUsuario().getEmail(),
                medico.getUsuario().getDocumento(),
                medico.getUsuario().getTelefono(),
                medico.getEspecialidad().toString()
        ));
    }

    @PatchMapping("/medicos/activar/{id}")
    @Transactional
    public ResponseEntity<String> activarMedico(@PathVariable Long id) {
        return ResponseEntity.ok().body( medicoService.activarMedico(id));
    }

    //DELETE
    @DeleteMapping("/medicos/{id}")
    @Transactional
    public ResponseEntity<String> deleteMedico(@PathVariable Long id) {
        return ResponseEntity.ok().body(medicoService.eliminarMedico(id));
    }

    //CRUD OPERATIONS FOR DISPONIBILIDAD
    @PostMapping("/disponibilidades/registrar/{medicoId}")
    public ResponseEntity<?> registrarDisponibilidad(@RequestBody @Valid DisponibilidadDTO dto,
                                                     @PathVariable Long medicoId) {
        Disponibilidad nueva = disponibilidadService.agregarDisponibilidad(dto, medicoId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DisponibilidadResponseDTO(
                nueva.getId(),
                nueva.getMedico().getId(),
                nueva.getDiaSemana().toString(),
                nueva.getHoraInicio().toLocalTime().toString(),
                nueva.getHoraFin().toLocalTime().toString()
        ));
    }

    @GetMapping("/disponibilidades/{medicoId}")
    public ResponseEntity<?> obtenerDisponibilidadesPorMedico(@PathVariable Long medicoId, @PageableDefault(size = 8) Pageable paginacion) {
        return ResponseEntity.ok(disponibilidadService.obtenerDisponibilidadesPorMedico(medicoId, paginacion)
                .map(disponibilidad -> new DisponibilidadResponseDTO(
                        disponibilidad.getId(),
                        disponibilidad.getMedico().getId(),
                        disponibilidad.getDiaSemana().toString(),
                        disponibilidad.getHoraInicio().toLocalTime().toString(),
                        disponibilidad.getHoraFin().toLocalTime().toString()
                )));
    }


    @PatchMapping("/disponibilidades/modificar/{id}")
    public ResponseEntity<?> modificarDisponibilidad(@PathVariable Long id, @RequestBody UpdateDisponibilidadDTO dto) {
        Disponibilidad actualizada = disponibilidadService.modificarDisponibilidad(dto, id);
        return ResponseEntity.ok(new DisponibilidadResponseDTO(
                actualizada.getId(),
                actualizada.getMedico().getId(),
                actualizada.getDiaSemana().toString(),
                actualizada.getHoraInicio().toLocalTime().toString(),
                actualizada.getHoraFin().toLocalTime().toString()
        ));
    }

    @DeleteMapping("/disponibilidades/eliminar/{id}")
    public ResponseEntity<?> eliminarDisponibilidad(@PathVariable Long id) {
        return ResponseEntity.ok(disponibilidadService.eliminarDisponibilidad(id));
    }

    //CRUD OPS FOR CITAS
    @GetMapping("/citas")
    public ResponseEntity<?> getCitas(@PageableDefault (size = 8) Pageable paginacion) {
        Page<Cita> citas = citaService.obtenerCitas(paginacion);
        Page<CitaResponseDTO> citasResponse = citas
                .map(cita -> new CitaResponseDTO(
                        cita.getId(),
                        cita.getPaciente().getId(),
                        cita.getMedico().getUsuario().getNombreCompleto(),
                        cita .getMedico().getEspecialidad(),
                        cita.getFecha().toString(),
                        cita.getHora().toString(),
                        cita.getEstado().toString()
                ));
        return ResponseEntity.ok(citasResponse);
    }

    @GetMapping("/citas/{id}")
    public ResponseEntity<?> getCitaById(@PathVariable Long id){
        Cita cita = citaService.obtenerCitaById(id);
        CitaResponseDTO response = new CitaResponseDTO(
                cita.getId(),
                cita.getPaciente().getId(),
                cita.getMedico().getUsuario().getNombreCompleto(),
                cita.getMedico().getEspecialidad(),
                cita.getFecha().toString(),
                cita.getHora().toString(),
                cita.getEstado().toString()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/citas/pacientes/{pacienteId}")
    public ResponseEntity<?> getCitasByPacienteId(@PathVariable Long pacienteId, @PageableDefault Pageable paginacion) {
        Page<Cita> citas = citaService.obtenerCitasPorPacienteId(pacienteId, paginacion);

        Page<CitaResponseDTO> citasResponse = citas
                .map(cita -> new CitaResponseDTO(
                        cita.getId(),
                        cita.getPaciente().getId(),
                        cita.getMedico().getUsuario().getNombreCompleto(),
                        cita.getMedico().getEspecialidad(),
                        cita.getFecha().toString(),
                        cita.getHora().toString(),
                        cita.getEstado().toString()
                ));

        return ResponseEntity.ok(citasResponse);
    }

    @GetMapping("/citas/medicos/{medicoId}")
    public ResponseEntity<?> getCitasByMedicoId(@PathVariable Long medicoId, @PageableDefault Pageable paginacion) {
        Page<Cita> citas = citaService.obtenerCitasPorMedicoId(medicoId, paginacion);
        Page<CitaResponseDTO> citasResponse = citas
                .map(cita -> new CitaResponseDTO(
                        cita.getId(),
                        cita.getPaciente().getId(),
                        cita.getMedico().getUsuario().getNombreCompleto(),
                        cita.getMedico().getEspecialidad(),
                        cita.getFecha().toString(),
                        cita.getHora().toString(),
                        cita.getEstado().toString()
                ));

        return ResponseEntity.ok(citasResponse);

    }

    @PatchMapping("/citas/editar/{id}")
    @Transactional
    public ResponseEntity<?> updateCita(@PathVariable Long id, @RequestBody CitaActualizarDTO citaDto) {

        Cita updatedCita = citaService.actualizarCitaAdmin(id, citaDto);
        CitaResponseDTO response = new CitaResponseDTO(
                updatedCita.getId(),
                updatedCita.getPaciente().getId(),
                updatedCita.getMedico().getUsuario().getNombreCompleto(),
                updatedCita.getMedico().getEspecialidad(),
                updatedCita.getFecha().toString(),
                updatedCita.getHora().toString(),
                updatedCita.getEstado().toString()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/citas/eliminar/{id}")
    @Transactional
    public ResponseEntity<?> deleteCita(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.cancelarCitaAdmin(id));
    }
}
