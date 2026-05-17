package com.iudmarket.iudmarket.Controller;

import com.iudmarket.iudmarket.Dto.CrearClienteRequestDTO;
import com.iudmarket.iudmarket.Model.Cliente;
import com.iudmarket.iudmarket.Service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    // POST
    @PostMapping
    public ResponseEntity<Cliente> crear(@RequestBody CrearClienteRequestDTO newCliente) {
        Cliente clienteCreado = clienteService.crearCliente(newCliente);
        return ResponseEntity.ok(clienteCreado);
    }

    // GET /api/clientes — listar todos
    @GetMapping
    public ResponseEntity<List<Cliente>> listar() {
        return ResponseEntity.ok(clienteService.listarClientes());
    }

    // GET
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscar(@PathVariable Long id) {
        Cliente c = clienteService.buscarPorId(id);
        if (c == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(c);
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(@PathVariable Long id, @RequestBody CrearClienteRequestDTO newCliente) {
        Cliente clienteActualizado = clienteService.actualizar(id, newCliente);
        return ResponseEntity.ok(clienteActualizado);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        boolean ok = clienteService.eliminar(id);
        return ok ? ResponseEntity.ok("Cliente eliminado")
                : ResponseEntity.notFound().build();
    }
}
