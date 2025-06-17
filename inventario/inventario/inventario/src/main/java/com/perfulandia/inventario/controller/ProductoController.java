package com.perfulandia.inventario.controller;

import com.perfulandia.inventario.model.Producto;
import com.perfulandia.inventario.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<Producto> listar() {
        return productoService.listar();
    }

    @PostMapping
    public ResponseEntity<String> guardar(@RequestBody Producto producto) {
        productoService.guardar(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Perfume ingresado");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtener(@PathVariable Long id) {
        Optional<Producto> producto = productoService.obtenerPorId(id);
        return producto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        boolean eliminado = productoService.eliminar(id);
        if(eliminado) {
            return ResponseEntity.ok("Perfume eliminado");
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable Long id, @RequestBody Producto productoActualizado) {
        Optional<Producto> productoOptional = productoService.obtenerPorId(id);
        if(productoOptional.isPresent()) {
            Producto productoExistente = productoOptional.get();

            productoExistente.setNombre(productoActualizado.getNombre());
            productoExistente.setDescripcion(productoActualizado.getDescripcion());
            productoExistente.setPrecio(productoActualizado.getPrecio());
            productoExistente.setCantidad(productoActualizado.getCantidad());

            productoService.guardar(productoExistente);
            return ResponseEntity.ok("Perfume actualizado");
        }
        return ResponseEntity.notFound().build();
    }
}
