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

    // POST 
    @PostMapping
    public ResponseEntity<Cajera> crear(@RequestBody Cajera cajera) {
        return ResponseEntity.ok(cajeraService.crearCajera(cajera));
    }

    // GET /api/cajeras — listar todas
    @GetMapping
    public ResponseEntity<List<Cajera>> listar() {
        return ResponseEntity.ok(cajeraService.listarCajeras());
    }

    // GET 
    @GetMapping("/{id}")
    public ResponseEntity<Cajera> buscar(@PathVariable Long id) {
        Cajera c = cajeraService.buscarPorId(id);
        if (c == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(c);
    }

    // GET 
    @GetMapping("/disponibles")
    public ResponseEntity<List<Cajera>> disponibles() {
        return ResponseEntity.ok(cajeraService.cajerasDisponibles());
    }

    // PUT 
    @PutMapping("/{id}/estado")
    public ResponseEntity<Cajera> cambiarEstado(@PathVariable Long id, @RequestParam String nuevoEstado) {
        return ResponseEntity.ok(cajeraService.actualizarEstado(id, nuevoEstado));
    }

    // DELETE 
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        boolean ok = cajeraService.eliminar(id);
        return ok ? ResponseEntity.ok("Cajera eliminada")
                  : ResponseEntity.notFound().build();
    }
}