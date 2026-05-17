package com.iudmarket.iudmarket.Dao;

import com.iudmarket.iudmarket.Model.Producto;
import java.util.List;

public interface Productodao {
    Producto findById(Long id);
    List<Producto> findAll();
    Producto save(Producto producto);
    Producto update(Producto producto);
    boolean deleteById(Long id);
}