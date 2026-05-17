package com.iudmarket.iudmarket.Controller;

import com.iudmarket.iudmarket.Model.Producto;
import com.iudmarket.iudmarket.Service.Productoservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private Productoservice productoService;

    // Aquí estand los métodos para manejar las solicitudes relacionadas con productos
    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.crearProducto(producto));
    }

    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        // Lógica para listar productos
        return ResponseEntity.ok(productoService.listarProductos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarPorId(@PathVariable Long id) {
        Producto producto = productoService.buscarPorId(id);
        if (producto != null) {
            return ResponseEntity.ok(producto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto producto){
        Producto productoActualizado = productoService.actualizar(id, producto);

        if (productoActualizado != null) {
            return ResponseEntity.ok(productoActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        boolean ok = productoService.eliminar(id);
        return ok ? ResponseEntity.ok("Producto eliminada") : ResponseEntity.notFound().build();
    }
}
