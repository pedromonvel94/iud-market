package com.iudmarket.iudmarket.Service;



import com.iudmarket.iudmarket.Dao.CajeraDao;

import com.iudmarket.iudmarket.Dao.ClienteDao;

import com.iudmarket.iudmarket.Dao.CompraDao;

import com.iudmarket.iudmarket.Dao.DetalleCompraDao;

import com.iudmarket.iudmarket.Dao.Productodao;

import com.iudmarket.iudmarket.Dto.CrearCompraRequestDTO;

import com.iudmarket.iudmarket.Dto.DetalleCompraResponseDTO;

import com.iudmarket.iudmarket.Model.*;

import com.iudmarket.iudmarket.exception.CajeraNoDisponibleException;

import com.iudmarket.iudmarket.exception.RecursoNoEncontradoException;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Async;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;



import java.time.LocalDateTime;

import java.util.ArrayList;

import java.util.List;

import java.util.concurrent.CompletableFuture;


@Service

public class CompraService {

    private static final String ESTADO_ACTIVA = "ACTIVA";
    private static final String ESTADO_INACTIVA = "INACTIVA";

    @Autowired
    private CompraDao compraDao;

    @Autowired
    private ClienteDao clienteDao;

    @Autowired
    private CajeraDao cajeraDao;

    @Autowired
    private DetalleCompraDao detalleCompraDao;

    @Autowired
    private Productodao productoDao;

    public List<Compra> listarCompras() {
        return compraDao.findAll();
    }

    public List<Compra> comprasPorCliente(Long clienteId) {
        return compraDao.findComprasByCliente(clienteId);
    }

    public List<DetalleCompraResponseDTO> detalleCompra(Long compraId) {
        Compra compra = compraDao.findById(compraId);
        if (compra == null) {
            throw new RecursoNoEncontradoException("Compra no encontrada con id: " + compraId);
        }

        List<DetalleCompra> detalles = detalleCompraDao.findByCompraId(compraId);

        if (detalles.isEmpty()) {
            throw new RecursoNoEncontradoException("No hay detalle para la compra id: " + compraId);
        }

        List<DetalleCompraResponseDTO> respuesta = new ArrayList<>();

        for (DetalleCompra d : detalles) {
            DetalleCompraResponseDTO dto = new DetalleCompraResponseDTO();
            dto.setNombreProducto(d.getProducto().getNombre());
            dto.setCantidad(d.getCantidad());
            dto.setPrecioUnitario(d.getPrecioUnitario());
            dto.setSubtotal(d.getSubtotal());
            dto.setTiempoProcesamiento(d.getTiempoProducto());
            respuesta.add(dto);
        }
        return respuesta;
    }

    @Async("cajeraExecutor")
    @Transactional
    public CompletableFuture<Compra> procesarCompraAsync(CrearCompraRequestDTO dto) {

        String hilo = Thread.currentThread().getName();
        System.out.println("🧵 [" + hilo + "] Iniciando cobro para cliente ID: " + dto.getClienteId());

        Cliente cliente = clienteDao.findById(dto.getClienteId());

        if (cliente == null) {
            throw new RecursoNoEncontradoException("Cliente no encontrado con id: " + dto.getClienteId());
        }

        Cajera cajera = cajeraDao.findById(dto.getCajeraId());

        if (cajera == null) {
            throw new RecursoNoEncontradoException("Cajera no encontrada con id: " + dto.getCajeraId());
        }

        if (!ESTADO_ACTIVA.equals(cajera.getEstado())) {
            throw new CajeraNoDisponibleException(cajera.getNombre(), cajera.getId());
        }

        // Cajera pasa a INACTIVA mientras atiende (cobrando)
        cajera.setEstado(ESTADO_INACTIVA);
        cajeraDao.update(cajera);
        System.out.println(" [" + hilo + "] Cajera " + cajera.getNombre() + " ocupada");

        Compra compra = new Compra();
        compra.setCliente(cliente);
        compra.setCajera(cajera);
        compra.setFechaCompra(LocalDateTime.now());
        compra.setTotalCompra(0.0);
        compra.setTiempoTotalProcesamiento(0);
        compraDao.save(compra);

        double totalCompra = 0.0;
        int tiempoTotalSegundos = 0;

        for (var item : dto.getProductos()) {

            Producto producto = buscarProducto(item.getIdProducto());

            if (producto == null) {
                System.out.println("  Producto no encontrado: " + item.getIdProducto() + " — omitido");
                continue;
            }

            double subtotal = producto.getPrecio() * item.getCantidad();
            int tiempoProducto = producto.getTiempoProcesamiento() * item.getCantidad();
            simularTiempo(tiempoProducto);

            DetalleCompra detalle = new DetalleCompra();

            detalle.setCompra(compra);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario((double) producto.getPrecio());
            detalle.setSubtotal(subtotal);
            detalle.setTiempoProducto(tiempoProducto);
            detalleCompraDao.save(detalle);

            totalCompra += subtotal;
            tiempoTotalSegundos += tiempoProducto;

            System.out.printf("    [%s] Producto: %-25s | Cant: %d | Subtotal: $%.2f | Tiempo: %ds%n",
                    hilo, producto.getNombre(), item.getCantidad(), subtotal, tiempoProducto);

        }

        compra.setTotalCompra(totalCompra);
        compra.setTiempoTotalProcesamiento(tiempoTotalSegundos);
        compraDao.save(compra);

        // Cajera vuelve a ACTIVA (libre)
        cajera.setEstado(ESTADO_ACTIVA);
        cajeraDao.update(cajera);

        System.out.printf("[%s] Compra #%d finalizada | Total: $%.2f | Tiempo: %ds%n",
                hilo, compra.getId(), totalCompra, tiempoTotalSegundos);

        return CompletableFuture.completedFuture(compra);

    }

    private void simularTiempo(int segundos) {
        try {
            Thread.sleep(segundos * 500L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private Producto buscarProducto(Long id) {
        return productoDao.findById(id);
    }

}


