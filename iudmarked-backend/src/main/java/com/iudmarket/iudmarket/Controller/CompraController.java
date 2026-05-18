package com.iudmarket.iudmarket.Controller;

import com.iudmarket.iudmarket.Dto.CrearCompraRequestDTO;
import com.iudmarket.iudmarket.Dto.DetalleCompraResponseDTO;
import com.iudmarket.iudmarket.Model.Compra;
import com.iudmarket.iudmarket.Service.CompraService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return ResponseEntity.ok(compraService.detalleCompra(id));
    }

    @PostMapping("/procesar")
    public CompletableFuture<ResponseEntity<Compra>> procesar(@Valid @RequestBody CrearCompraRequestDTO dto) {
        return compraService.procesarCompraAsync(dto)
                .thenApply(compra -> ResponseEntity.status(HttpStatus.CREATED).body(compra));
    }

}
