package com.iudmarket.iudmarket.Service;

import com.iudmarket.iudmarket.Dao.CajeraDao;
import com.iudmarket.iudmarket.Model.Cajera;
import com.iudmarket.iudmarket.exception.RecursoNoEncontradoException;
import com.iudmarket.iudmarket.exception.SolicitudInvalidaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CajeraService {

    private static final String ESTADO_ACTIVA = "ACTIVA";
    private static final Set<String> ESTADOS_PERMITIDOS = Set.of("ACTIVA", "INACTIVA");

    @Autowired
    private CajeraDao cajeraDao;

    public Cajera crearCajera(Cajera cajera) {
        cajera.setEstado(ESTADO_ACTIVA);
        return cajeraDao.save(cajera);
    }

    public List<Cajera> listarCajeras() {
        return cajeraDao.findAll();
    }

    public Cajera buscarPorId(Long id) {
        Cajera cajera = cajeraDao.findById(id);
        if (cajera == null) {
            throw new RecursoNoEncontradoException("Cajera no encontrada con id: " + id);
        }
        return cajera;
    }

    public List<Cajera> cajerasDisponibles() {
        return cajeraDao.findAvailableCashiers(ESTADO_ACTIVA);
    }

    public Cajera actualizarEstado(Long id, String nuevoEstado) {
        Cajera cajera = buscarPorId(id);
        if (nuevoEstado == null || !ESTADOS_PERMITIDOS.contains(nuevoEstado.toUpperCase())) {
            throw new SolicitudInvalidaException(
                    "Estado inválido. Valores permitidos: ACTIVA, INACTIVA");
        }
        cajera.setEstado(nuevoEstado.toUpperCase());
        return cajeraDao.update(cajera);
    }

    public boolean eliminar(Long id) {
        buscarPorId(id);
        return cajeraDao.deleteById(id);
    }
}
