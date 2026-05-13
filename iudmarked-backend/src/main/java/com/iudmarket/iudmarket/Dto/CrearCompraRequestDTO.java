package com.iudmarket.iudmarket.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrearCompraRequestDTO {
    private Long clienteId;
    private Long cajeraId;
    private List<ItemCompraDTO> productos;
}
