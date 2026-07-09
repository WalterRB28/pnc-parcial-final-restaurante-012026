package com.uca.pncparcialfinalrestaurante.controller;

import com.uca.pncparcialfinalrestaurante.dto.PedidoRequest;
import com.uca.pncparcialfinalrestaurante.entity.Pedido;
import com.uca.pncparcialfinalrestaurante.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Pedido> crear(@Valid @RequestBody PedidoRequest request) {
        Pedido p = service.crearPedido(request);
        return ResponseEntity.ok(p);
    }

    @GetMapping("{id}")
    public ResponseEntity<Pedido> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping
    public List<Pedido> list() {
        return service.obtenerTodos();
    }

    @GetMapping("/sucursal/{sucursalId}")
    public List<Pedido> bySucursal(@PathVariable Long sucursalId) {
        return service.obtenerPorSucursal(sucursalId);
    }

    @GetMapping("/cliente/{clienteId}")
    public List<Pedido> byCliente(@PathVariable Long clienteId) {
        return service.obtenerPorCliente(clienteId);
    }

    @PostMapping("{id}/cancelar")
    public ResponseEntity<Pedido> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancelarPedido(id));
    }
}