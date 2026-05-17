package com.iudmarket.iudmarket.Controller;

import com.iudmarket.iudmarket.Service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iudmarket.iudmarket.Dto.CrearCompraRequestDTO;
import com.iudmarket.iudmarket.Dto.DetalleCompraResponseDTO;
import com.iudmarket.iudmarket.Model.Compra;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/compras")
public class CompraController {
    @Autowired
    private CompraService compraService;

    @GetMapping
    public ResponseEntity<List<Compra>> listar() {
        return ResponseEntity.ok(compraService.listarCompras());
    }
    
    @GetMapping("/{id}/detalle")
    public ResponseEntity<List<DetalleCompraResponseDTO>> detalle(@PathVariable Long id) {
        List<DetalleCompraResponseDTO> detalle = compraService.detalleCompra(id);
        if (detalle.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detalle);
    }

    @PostMapping("/procesar")
    //Esto de CompletableFuture es para procesar la compra de forma asíncrona, es decir, que no se bloquee el hilo principal mientras se procesa la compra
    public CompletableFuture<ResponseEntity<Compra>> procesar(@RequestBody CrearCompraRequestDTO dto) {
        return compraService.procesarCompraAsync(dto)
                .thenApply(compra -> ResponseEntity.status(HttpStatus.CREATED).body(compra));
    }

    
}
