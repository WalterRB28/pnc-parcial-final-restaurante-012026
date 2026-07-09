package com.uca.pncparcialfinalrestaurante.controller;

import com.uca.pncparcialfinalrestaurante.dto.PedidoRequest;
import com.uca.pncparcialfinalrestaurante.entity.Pedido;
import com.uca.pncparcialfinalrestaurante.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('CLIENTE', 'ENCARGADO', 'ADMIN')")
    public ResponseEntity<Pedido> crear(@Valid @RequestBody PedidoRequest request) {
        Pedido p = service.crearPedido(request);
        return ResponseEntity.ok(p);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ENCARGADO', 'ADMIN')")
    public ResponseEntity<Pedido> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Pedido> list() {
        return service.obtenerTodos();
    }

    @GetMapping("/sucursal/{sucursalId}")
    @PreAuthorize("hasRole('ENCARGADO') or hasRole('ADMIN')")
    public List<Pedido> bySucursal(@PathVariable Long sucursalId) {
        return service.obtenerPorSucursal(sucursalId);
    }

    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasRole('CLIENTE') or hasRole('ADMIN')")
    public List<Pedido> byCliente(@PathVariable Long clienteId) {
        return service.obtenerPorCliente(clienteId);
    }

    @PostMapping("{id}/cancelar")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ENCARGADO', 'ADMIN')")
    public ResponseEntity<Pedido> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancelarPedido(id));
    }

    @PutMapping("{id}/confirmar")
    @PreAuthorize("hasRole('ENCARGADO') or hasRole('ADMIN')")
    public ResponseEntity<Pedido> confirmar(@PathVariable Long id) {
        return ResponseEntity.ok(service.confirmarPedido(id));
    }
}