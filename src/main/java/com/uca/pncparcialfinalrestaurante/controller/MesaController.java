package com.uca.pncparcialfinalrestaurante.controller;

import com.uca.pncparcialfinalrestaurante.entity.Mesa;
import com.uca.pncparcialfinalrestaurante.repository.MesaRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mesas")
public class MesaController {

    private final MesaRepository repo;

    public MesaController(MesaRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Mesa> list() {
        return repo.findAll();
    }

    @GetMapping("/sucursal/{sucursalId}")
    public List<Mesa> bySucursal(@PathVariable Long sucursalId) {
        return repo.findBySucursalId(sucursalId);
    }

    @PostMapping
    public ResponseEntity<Mesa> create(@Valid @RequestBody Mesa m) {
        Mesa saved = repo.save(m);
        return ResponseEntity.ok(saved);
    }
}