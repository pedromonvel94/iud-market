package com.iudmarket.iudmarket.Daoimpl;

import com.iudmarket.iudmarket.Dao.CajeraDao;
import com.iudmarket.iudmarket.Model.Cajera;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class Cajeradaoimpl implements CajeraDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Cajera findById(Long id) {
        return em.find(Cajera.class, id);
    }

    @Override
    public List<Cajera> findAll() {
        return em.createQuery("SELECT c FROM Cajera c", Cajera.class)
                 .getResultList();
    }

    @Override
    public Cajera save(Cajera cajera) {
        em.persist(cajera);
        return cajera;
    }

    @Override
    public Cajera update(Cajera cajera) {
        return em.merge(cajera);
    }

    @Override
    public boolean deleteById(Long id) {
        Cajera cajera = findById(id);
        if (cajera != null) {
            em.remove(cajera);
            return true;
        }
        return false;
    }

    @Override
    public List<Cajera> findAvailableCashiers(String estado) {
        TypedQuery<Cajera> query = em.createQuery(
            "SELECT c FROM Cajera c WHERE c.estado = :estado", Cajera.class);
        query.setParameter("estado", estado);
        return query.getResultList();
    }
}