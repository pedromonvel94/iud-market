package com.iudmarket.iudmarket.Dao;

import com.iudmarket.iudmarket.Model.Cliente;
import com.iudmarket.iudmarket.Model.Compra;

import java.util.List;

public interface CompraDao {
    Compra findById(Long id);
    List<Compra> findAll ();
    Compra save(Compra compra);
    boolean deleteById(Long id);
    List<Compra> findComprasByCliente(Long clienteId);
}
