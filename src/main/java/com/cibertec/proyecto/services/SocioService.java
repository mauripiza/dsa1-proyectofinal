package com.cibertec.proyecto.services;

import com.cibertec.proyecto.dtos.SocioDTO;
import com.cibertec.proyecto.dtos.SocioResponseDTO;
import com.cibertec.proyecto.entities.Socio;
import com.cibertec.proyecto.exceptions.ConflictException;
import com.cibertec.proyecto.exceptions.ResourceNotFoundException;
import com.cibertec.proyecto.repositories.SocioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class SocioService {

    private final SocioRepository socioRepository;

    @Transactional
    public SocioResponseDTO crearSocio(SocioDTO dto) {
        if (socioRepository.existsByDni(dto.dni())) {
            throw new ConflictException("Ya existe un socio con el DNI: " + dto.dni());
        }

        Socio socio = new Socio();
        socio.setNombre(dto.nombre());
        socio.setApellido(dto.apellido());
        socio.setDni(dto.dni());
        socio.setTelefono(dto.telefono());
        socio.setEmail(dto.email());

        Socio savedSocio = socioRepository.save(socio);
        return toResponseDTO(savedSocio);
    }

    @Transactional
    public void eliminarSocioPorDni(String dni) {
        Socio socio = socioRepository.findByDni(dni)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un socio con el DNI: " + dni));
        socioRepository.delete(socio);
    }

    @Transactional
    public SocioResponseDTO actualizarSocio(String dni, SocioDTO dto) {
        Socio socio = socioRepository.findByDni(dni)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con DNI: " + dni));

        socio.setNombre(dto.nombre());
        socio.setApellido(dto.apellido());
        socio.setTelefono(dto.telefono());
        socio.setEmail(dto.email());
        
        Socio updatedSocio = socioRepository.save(socio);
        return toResponseDTO(updatedSocio);
    }

    public List<SocioResponseDTO> listarSocios() {
        return socioRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public Page<SocioResponseDTO> listarSociosPaginado(Pageable pageable) {
        return socioRepository.findAll(pageable)
                .map(this::toResponseDTO);
    }

    public Page<SocioResponseDTO> buscarSocios(String term, Pageable pageable) {
        return socioRepository.findByNombreContainingIgnoreCaseOrDniContaining(term, term, pageable)
                .map(this::toResponseDTO);
    }

    public SocioResponseDTO obtenerPorDni(String dni) {
        return socioRepository.findByDni(dni)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con DNI: " + dni));
    }

    private SocioResponseDTO toResponseDTO(Socio socio) {
        return new SocioResponseDTO(
            socio.getId(),
            socio.getNombre(),
            socio.getApellido(),
            socio.getDni(),
            socio.getTelefono(),
            socio.getEmail()
        );
    }
}
