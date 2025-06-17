package com.perfulandia.inventario.service;

import com.perfulandia.inventario.model.Producto;
import com.perfulandia.inventario.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    public boolean eliminar(Long id) {
        if(productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
