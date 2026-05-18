package com.iudmarket.iudmarket.Service;

import com.iudmarket.iudmarket.Dao.Productodao;
import com.iudmarket.iudmarket.Model.Producto;
import com.iudmarket.iudmarket.exception.RecursoNoEncontradoException;
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
        Producto producto = productoDao.findById(id);
        if (producto == null) {
            throw new RecursoNoEncontradoException("Producto no encontrado con id: " + id);
        }
        return producto;
    }

    public Producto actualizar(Long id, Producto datos) {
        Producto producto = buscarPorId(id);
        producto.setNombre(datos.getNombre());
        producto.setPrecio(datos.getPrecio());
        producto.setTiempoProcesamiento(datos.getTiempoProcesamiento());
        return productoDao.update(producto);
    }

    public boolean eliminar(Long id) {
        buscarPorId(id);
        return productoDao.deleteById(id);
    }
}
