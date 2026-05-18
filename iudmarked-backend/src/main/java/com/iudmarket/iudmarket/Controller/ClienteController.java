package com.iudmarket.iudmarket.Controller;

import com.iudmarket.iudmarket.Dto.CrearClienteRequestDTO;
import com.iudmarket.iudmarket.Model.Cliente;
import com.iudmarket.iudmarket.Service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<Cliente> crear(@Valid @RequestBody CrearClienteRequestDTO newCliente) {
        return ResponseEntity.ok(clienteService.crearCliente(newCliente));
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listar() {
        return ResponseEntity.ok(clienteService.listarClientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CrearClienteRequestDTO newCliente) {
        return ResponseEntity.ok(clienteService.actualizar(id, newCliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.ok("Cliente eliminado");
    }
}
