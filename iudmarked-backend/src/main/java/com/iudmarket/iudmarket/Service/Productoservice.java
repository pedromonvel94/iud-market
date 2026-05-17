package com.iudmarket.iudmarket.Service;

import com.iudmarket.iudmarket.Dao.Productodao;
import com.iudmarket.iudmarket.Model.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Productoservice {

    @Autowired
    private Productodao productoDao;

    public Producto crearProducto(Producto producto) {
        return productoDao.save(producto);
    }

    public List<Producto> listarProductos() {
        return productoDao.findAll();
    }

    public Producto buscarPorId(Long id) {
        Producto p = productoDao.findById(id);
        if (p == null) throw new RuntimeException("Producto no encontrado: " + id);
        return p;
    }

    public Producto actualizar(Long id, Producto datos) {
        Producto p = buscarPorId(id);
        p.setNombre(datos.getNombre());
        p.setPrecio(datos.getPrecio());
        p.setTiempoProcesamiento(datos.getTiempoProcesamiento());
        return productoDao.update(p);
    }

    public boolean eliminar(Long id) {
        return productoDao.deleteById(id);
    }
}