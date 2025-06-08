package com.salomon.citasmedbackend.services;

import com.salomon.citasmedbackend.domain.medico.Medico;
import com.salomon.citasmedbackend.domain.medico.MedicoResponseDTO;
import com.salomon.citasmedbackend.domain.medico.RegistrarMedicoDTO;
import com.salomon.citasmedbackend.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicoService {
    private  final MedicoRepository medicoRepository;

    public ResponseEntity<List<MedicoResponseDTO>> obtenerMedicos(){
        List<Medico> medicos = medicoRepository.findAll();
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
        Optional<Medico> medicoOptional = medicoRepository.findById(id);
        if (medicoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
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
    public ResponseEntity<MedicoResponseDTO> registrarMedico (RegistrarMedicoDTO registrarMedicoDTO) {
        Medico nuevoMedico = new Medico(registrarMedicoDTO);
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

}
