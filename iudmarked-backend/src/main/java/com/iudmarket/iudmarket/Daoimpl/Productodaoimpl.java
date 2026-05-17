package com.iudmarket.iudmarket.Daoimpl;

import com.iudmarket.iudmarket.Dao.Productodao;
import com.iudmarket.iudmarket.Model.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class Productodaoimpl implements Productodao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Producto findById(Long id) {
        return em.find(Producto.class, id);
    }

    @Override
    public List<Producto> findAll() {
        return em.createQuery("SELECT p FROM Producto p", Producto.class)
                 .getResultList();
    }

    @Override
    public Producto save(Producto producto) {
        em.persist(producto);
        return producto;
    }

    @Override
    public Producto update(Producto producto) {
        return em.merge(producto);
    }

    @Override
    public boolean deleteById(Long id) {
        Producto p = findById(id);
        if (p != null) { em.remove(p); return true; }
        return false;
    }
}