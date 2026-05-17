package com.iudmarket.iudmarket.Service;
import com.iudmarket.iudmarket.Dao.CajeraDao;
import com.iudmarket.iudmarket.Model.Cajera;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CajeraService {

    @Autowired
    private CajeraDao cajeraDao;

    public Cajera crearCajera(Cajera cajera) {
        cajera.setEstado("ACTIVA");
        return cajeraDao.save(cajera);
    }

    public List<Cajera> listarCajeras() {
        return cajeraDao.findAll();
    }

    public Cajera buscarPorId(Long id) {
        return cajeraDao.findById(id);
    }

    public List<Cajera> cajerasDisponibles() {
        return cajeraDao.findAvailableCashiers("ACTIVA");
    }

    public Cajera actualizarEstado(Long id, String nuevoEstado) {
        Cajera cajera = cajeraDao.findById(id);
        if (cajera == null) throw new RuntimeException("No se ha podido encontrar la cajera con el id: " + id);
        cajera.setEstado(nuevoEstado);
        return cajeraDao.update(cajera);
    }

    public boolean eliminar(Long id) {
        return cajeraDao.deleteById(id);
    }
}