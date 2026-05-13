package com.iudmarket.iudmarket.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrearCompraRequestDTO {
    private Long clienteId;
    private Long cajeraId;

}
