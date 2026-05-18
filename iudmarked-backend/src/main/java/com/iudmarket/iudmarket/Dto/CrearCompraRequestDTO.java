package com.iudmarket.iudmarket.Dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "El id del cliente es obligatorio")
    private Long clienteId;

    @NotNull(message = "El id de la cajera es obligatorio")
    private Long cajeraId;

    @NotEmpty(message = "Debe incluir al menos un producto")
    @Valid
    private List<ItemCompraDTO> productos;
}
