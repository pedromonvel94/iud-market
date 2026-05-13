package com.iudmarket.iudmarket.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="compras")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaCompra;

    private Double totalCompra;

    private Integer tiempoTotalProcesamiento;

    @ManyToOne
    private Cliente cliente;

    @ManyToOne
    private Cajera cajera;
}
