package com.iudmarket.iudmarket.Daoimpl;

import com.iudmarket.iudmarket.Dao.DetalleCompraDao;
import com.iudmarket.iudmarket.Model.DetalleCompra;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class Detallecompradaoimpl implements DetalleCompraDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public DetalleCompra findById(Long id) {
        return em.find(DetalleCompra.class, id);
    }

    @Override
    public List<DetalleCompra> findByCompraId(Long compraId) {
        TypedQuery<DetalleCompra> query = em.createQuery(
            "SELECT d FROM DetalleCompra d WHERE d.compra.id = :compraId",
            DetalleCompra.class);
        query.setParameter("compraId", compraId);
        return query.getResultList();
    }

    @Override
    public DetalleCompra save(DetalleCompra detalleCompra) {
        em.persist(detalleCompra);
        return detalleCompra;
    }

    @Override
    public boolean deleteById(Long id) {
        DetalleCompra detalle = findById(id);
        if (detalle != null) {
            em.remove(detalle);
            return true;
        }
        return false;
    }
}