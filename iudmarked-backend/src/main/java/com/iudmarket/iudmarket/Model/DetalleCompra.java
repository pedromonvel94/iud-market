package com.iudmarket.iudmarket.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="detalle_compra")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer cantidad;

    private Double precioUnitario;

    private Double subtotal;

    private Integer tiempoProducto;

    @ManyToOne
    private Compra compra;

    @ManyToOne
    private Producto producto;
}
