package com.iudmarket.iudmarket.Daoimpl;

import com.iudmarket.iudmarket.Dao.ClienteDao;
import com.iudmarket.iudmarket.Model.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class Clientedaoimpl implements ClienteDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Cliente findById(Long id) {
        return em.find(Cliente.class, id);
    }

    @Override
    public List<Cliente> findAll() {
        return em.createQuery("SELECT c FROM Cliente c", Cliente.class)
                 .getResultList();
    }

    @Override
    public Cliente save(Cliente cliente) {
        em.persist(cliente);
        return cliente;
    }

    @Override
    public Cliente update(Cliente cliente) {
        return em.merge(cliente);
    }

    @Override
    public boolean deleteById(Long id) {
        Cliente cliente = findById(id);
        if (cliente != null) {
            em.remove(cliente);
            return true;
        }
        return false;
    }
}