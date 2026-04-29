package com.cibertec.proyecto.dtos;

public record SocioResponseDTO(
    Integer id,
    String nombre,
    String apellido,
    String dni,
    String telefono,
    String email
) {}
