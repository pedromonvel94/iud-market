package com.iudmarket.iudmarket.Dao;

import com.iudmarket.iudmarket.Model.Cajera;

import java.util.List;

public interface CajeraDao {
    Cajera findById(Long id);
    List<Cajera> findAll();
    Cajera save(Cajera cajera);
    Cajera update(Cajera cajera);
    boolean deleteById(Long id);
    List<Cajera> findAvailableCashiers(String estado);
}
