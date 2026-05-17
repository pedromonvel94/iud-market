package com.iudmarket.iudmarket.Daoimpl;

import com.iudmarket.iudmarket.Dao.CompraDao;
import com.iudmarket.iudmarket.Model.Compra;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class Compradaoimpl implements CompraDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Compra findById(Long id) {
        return em.find(Compra.class, id);
    }

    @Override
    public List<Compra> findAll() {
        return em.createQuery("SELECT c FROM Compra c", Compra.class)
                 .getResultList();
    }

    @Override
    public Compra save(Compra compra) {
        em.persist(compra);
        return compra;
    }

    @Override
    public boolean deleteById(Long id) {
        Compra compra = findById(id);
        if (compra != null) {
            em.remove(compra);
            return true;
        }
        return false;
    }

    @Override
    public List<Compra> findComprasByCliente(Long clienteId) {
        TypedQuery<Compra> query = em.createQuery(
            "SELECT c FROM Compra c WHERE c.cliente.id = :clienteId", Compra.class);
        query.setParameter("clienteId", clienteId);
        return query.getResultList();
    }
}