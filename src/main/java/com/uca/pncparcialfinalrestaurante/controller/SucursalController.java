package com.uca.pncparcialfinalrestaurante.controller;

import com.uca.pncparcialfinalrestaurante.entity.Sucursal;
import com.uca.pncparcialfinalrestaurante.repository.SucursalRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
public class SucursalController {

    private final SucursalRepository repo;

    public SucursalController(SucursalRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Sucursal> list() {
        return repo.findAll();
    }

    @PostMapping
    public ResponseEntity<Sucursal> create(@Valid @RequestBody Sucursal s) {
        Sucursal saved = repo.save(s);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("{id}")
    public ResponseEntity<Sucursal> get(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}