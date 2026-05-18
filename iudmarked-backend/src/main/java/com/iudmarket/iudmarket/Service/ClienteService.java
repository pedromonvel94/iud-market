package com.iudmarket.iudmarket.Service;

import com.iudmarket.iudmarket.Dao.ClienteDao;
import com.iudmarket.iudmarket.Dto.CrearClienteRequestDTO;
import com.iudmarket.iudmarket.Model.Cliente;
import com.iudmarket.iudmarket.exception.RecursoNoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteDao clienteDao;

    public Cliente crearCliente(CrearClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setDocumento(dto.getDocumento());
        return clienteDao.save(cliente);
    }

    public List<Cliente> listarClientes() {
        return clienteDao.findAll();
    }

    public Cliente buscarPorId(Long id) {
        Cliente cliente = clienteDao.findById(id);
        if (cliente == null) {
            throw new RecursoNoEncontradoException("Cliente no encontrado con id: " + id);
        }
        return cliente;
    }

    public Cliente actualizar(Long id, CrearClienteRequestDTO dto) {
        Cliente cliente = buscarPorId(id);
        cliente.setNombre(dto.getNombre());
        cliente.setDocumento(dto.getDocumento());
        return clienteDao.update(cliente);
    }

    public boolean eliminar(Long id) {
        buscarPorId(id);
        return clienteDao.deleteById(id);
    }
}
