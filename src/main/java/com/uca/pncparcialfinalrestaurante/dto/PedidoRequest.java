package com.uca.pncparcialfinalrestaurante.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoRequest {
    // id del cliente que realiza el pedido (en la parte 1 pasamos id, luego será por usuario autenticado)
    @NotNull
    private Long clienteId;

    @NotNull
    private Long mesaId;

    @NotNull
    private List<PedidoItemRequest> items;
}