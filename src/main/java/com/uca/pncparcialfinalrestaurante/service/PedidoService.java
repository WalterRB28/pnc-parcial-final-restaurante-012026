package com.uca.pncparcialfinalrestaurante.service;

import com.uca.pncparcialfinalrestaurante.dto.PedidoRequest;
import com.uca.pncparcialfinalrestaurante.entity.Pedido;

import java.util.List;

public interface PedidoService {
    Pedido crearPedido(PedidoRequest request);
    Pedido obtenerPorId(Long id);
    List<Pedido> obtenerTodos();
    List<Pedido> obtenerPorSucursal(Long sucursalId);
    List<Pedido> obtenerPorCliente(Long clienteId);
    Pedido cancelarPedido(Long id);
}