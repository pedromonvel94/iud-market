package com.iudmarket.iudmarket.Dao;

import com.iudmarket.iudmarket.Model.Compra;
import com.iudmarket.iudmarket.Model.DetalleCompra;

import java.util.List;

public interface DetalleCompraDao {
    DetalleCompra findById(Long id);
    List<DetalleCompra> findByCompraId(Long compraId);
    DetalleCompra save(DetalleCompra detalleCompra);
    boolean deleteById(Long id);
}
