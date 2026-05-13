package com.iudmarket.iudmarket.Dao;

import com.iudmarket.iudmarket.Dto.CrearClienteRequestDTO;
import com.iudmarket.iudmarket.Model.Cliente;

import java.util.List;

public interface ClienteDao {
    Cliente findById(Long id);
    List<Cliente> findAll();
    Cliente save (Cliente cliente);
    Cliente update(Cliente cliente);
    boolean deleteById(Long id);
}
