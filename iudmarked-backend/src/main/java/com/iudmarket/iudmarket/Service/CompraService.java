package com.iudmarket.iudmarket.Service;

import com.iudmarket.iudmarket.Dao.CajeraDao;
import com.iudmarket.iudmarket.Dao.ClienteDao;
import com.iudmarket.iudmarket.Dao.CompraDao;
import com.iudmarket.iudmarket.Dao.DetalleCompraDao;
import com.iudmarket.iudmarket.Dto.CrearCompraRequestDTO;
import com.iudmarket.iudmarket.Dto.DetalleCompraResponseDTO;
import com.iudmarket.iudmarket.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Servicio principal de compras.
 *
 * en teoria deberia funcionar asi:
 *  - Cada producto tiene un tiempoProcesamiento (supongamos en segundos)
 *  - Al procesar la compra, se suma el tiempo de todos los productos
 *  - Con @Async  segun lo que vi el cobro de cada compra corre en un hilo separado del pool "cajeraExecutor"
 *  - Esto simula que varias cajeras atienden clientes al mismo tiempo
 */
@Service
public class CompraService {

    @Autowired
    private CompraDao compraDao;

    @Autowired
    private ClienteDao clienteDao;

    @Autowired
    private CajeraDao cajeraDao;

    @Autowired
    private DetalleCompraDao detalleCompraDao;

    // ─── Listamos todas las compras ──────────────────────────────────────
    public List<Compra> listarCompras() {
        return compraDao.findAll();
    }

    // ─── Compras de un cliente específico ─────────────────────────────
    public List<Compra> comprasPorCliente(Long clienteId) {
        return compraDao.findComprasByCliente(clienteId);
    }

    // ─── Detalle de una compra ────────────────────────────────────────
    public List<DetalleCompraResponseDTO> detalleCompra(Long compraId) {
        List<DetalleCompra> detalles = detalleCompraDao.findByCompraId(compraId);
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

    // ─── asi se procesan las compras de forma ASÍNCRONA ───────────────────────────
    /**
     * @Async en nuesto caso iudmarketapp.... hace que este método corra en un hilo separado del pool "cajeraExecutor".
     * Esto simula que la cajera está procesando al cliente mientras otras cajeras
     * atienden a otros clientes al mismo tiempo (concurrencia real).
     *
     * CompletableFuture permite esperar el resultado cuando sea necesario.
     */
    @Async("cajeraExecutor")
    @Transactional
    public CompletableFuture<Compra> procesarCompraAsync(CrearCompraRequestDTO dto) {

        String hilo = Thread.currentThread().getName();
        System.out.println("🧵 [" + hilo + "] Iniciando cobro para cliente ID: " + dto.getClienteId());

        // 1. Validacion de cliente y cajera
        Cliente cliente = clienteDao.findById(dto.getClienteId());
        if (cliente == null)
            throw new RuntimeException("Cliente no encontrado: " + dto.getClienteId());

        Cajera cajera = cajeraDao.findById(dto.getCajeraId());
        if (cajera == null)
            throw new RuntimeException("Cajera no encontrada: " + dto.getCajeraId());

        // 2. estado de cajera a OCUPADA (simula que la cajera está atendiendo al cliente)
        cajera.setEstado("OCUPADA");
        cajeraDao.update(cajera);
        System.out.println(" [" + hilo + "] Cajera " + cajera.getNombre() + " ocupada");

        // 3. Crear cabecera de la compra
        Compra compra = new Compra();
        compra.setCliente(cliente);
        compra.setCajera(cajera);
        compra.setFechaCompra(LocalDateTime.now());
        compra.setTotalCompra(0.0);
        compra.setTiempoTotalProcesamiento(0);
        compraDao.save(compra);

        // 4. Procesar cada producto y simula el cobro uno a uno
        double totalCompra         = 0.0;
        int    tiempoTotalSegundos = 0;

        for (var item : dto.getProductos()) {

            // Buscar producto
            Producto producto = buscarProducto(item.getIdProducto());
            if (producto == null) {
                System.out.println("  Producto no encontrado: " + item.getIdProducto() + " — omitido");
                continue;
            }

            // Calcular valores
            double subtotal       = producto.getPrecio() * item.getCantidad();
            int    tiempoProducto = producto.getTiempoProcesamiento() * item.getCantidad();

            // Simular tiempo real de escaneo (1 segundo por cada unidad de tiempo)
            simularTiempo(tiempoProducto);

            // Guardar detalle
            DetalleCompra detalle = new DetalleCompra();
            detalle.setCompra(compra);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(subtotal);
            detalle.setTiempoProducto(tiempoProducto);
            detalleCompraDao.save(detalle);

            totalCompra         += subtotal;
            tiempoTotalSegundos += tiempoProducto;

            System.out.printf("    [%s] Producto: %-25s | Cant: %d | Subtotal: $%.2f | Tiempo: %ds%n",
                hilo, producto.getNombre(), item.getCantidad(), subtotal, tiempoProducto);
        }

        // 5. Actualiza el total de la compra
        compra.setTotalCompra(totalCompra);
        compra.setTiempoTotalProcesamiento(tiempoTotalSegundos);
        compraDao.save(compra);

        // 6. Liberar cajera o cambia de estado a DISPONIBLE
        cajera.setEstado("DISPONIBLE");
        cajeraDao.update(cajera);

        System.out.printf("🏁 [%s] Compra #%d finalizada | Total: $%.2f | Tiempo: %ds%n",
            hilo, compra.getId(), totalCompra, tiempoTotalSegundos);

        return CompletableFuture.completedFuture(compra);
    }

    // ─── Utilidades privadas ──────────────────────────────────────────

    /**
     * Simula el tiempo de procesamiento de un producto.
     * En un sistema real esto sería el tiempo real de escaneo.
     * Aquí dormimos el hilo para hacer la simulación visible.
     */
    private void simularTiempo(int segundos) {
        try {
            // Cada unidad = 500ms para que la demo no tarde demasiado, podemos ajustar esto según lo que queramos mostrar
            Thread.sleep(segundos * 500L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Busca un producto por ID usando EntityManager vía JPA.
     * Se puede reemplazar por un ProductoDao si lo agregamos.
     */
    private Producto buscarProducto(Long id) {
        try {
            // Usamos el contexto de JPA directamente ya que no hay ProductoDao en las interfaces actuales.(hay que revisar)
           // esto es simulado, la verdad me lo sugiero la ia de github
            return detalleCompraDao.findByCompraId(-1L)  // solo para obtener el em — ver nota abajo
                   == null ? null : null;
        } catch (Exception e) {
            return null;
        }
    }
}