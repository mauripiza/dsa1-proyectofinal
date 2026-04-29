package com.cibertec.proyecto.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PagoResponseDTO {
    private Integer id;
    private Double monto;
    private LocalDateTime fecha;
    private String metodoPago;
    private Integer deudaId;
    private String conceptoDeuda;
}
