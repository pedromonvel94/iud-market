package com.iudmarket.iudmarket.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="cajeras")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cajera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private Integer numeroCaja;

    private String estado;
}
