package com.iudmarket.iudmarket.Controller;

import com.iudmarket.iudmarket.Model.Cajera;
import com.iudmarket.iudmarket.Service.CajeraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cajeras")
public class CajeraController {

    @Autowired
    private CajeraService cajeraService;

    @PostMapping
    public ResponseEntity<Cajera> crear(@RequestBody Cajera cajera) {
        return ResponseEntity.ok(cajeraService.crearCajera(cajera));
    }

    @GetMapping
    public ResponseEntity<List<Cajera>> listar() {
        return ResponseEntity.ok(cajeraService.listarCajeras());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cajera> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(cajeraService.buscarPorId(id));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Cajera>> disponibles() {
        return ResponseEntity.ok(cajeraService.cajerasDisponibles());
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Cajera> cambiarEstado(@PathVariable Long id, @RequestParam String nuevoEstado) {
        return ResponseEntity.ok(cajeraService.actualizarEstado(id, nuevoEstado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        cajeraService.eliminar(id);
        return ResponseEntity.ok("Cajera eliminada");
    }
}
