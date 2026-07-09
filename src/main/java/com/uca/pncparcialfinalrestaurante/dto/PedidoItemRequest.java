package com.uca.pncparcialfinalrestaurante.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoItemRequest {
    @NotNull
    private Long productoId;

    @Min(1)
    private Integer cantidad;
}