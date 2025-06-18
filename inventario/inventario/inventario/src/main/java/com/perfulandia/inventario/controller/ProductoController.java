package com.perfulandia.inventario.controller;

import com.perfulandia.inventario.model.Producto;
import com.perfulandia.inventario.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> listar() {
        List<Producto> productos = productoService.listar();

        List<EntityModel<Producto>> productosModel = productos.stream()
                .map(producto -> EntityModel.of(producto,
                        linkTo(methodOn(ProductoController.class).obtener(producto.getId())).withSelfRel(),
                        linkTo(methodOn(ProductoController.class).listar()).withRel("productos")))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Producto>> collectionModel = CollectionModel.of(productosModel,
                linkTo(methodOn(ProductoController.class).listar()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    
    @PostMapping
    public ResponseEntity<String> guardar(@RequestBody Producto producto) {
        productoService.guardar(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Perfume ingresado");
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> obtener(@PathVariable Long id) {
        Optional<Producto> producto = productoService.obtenerPorId(id);
        return producto.map(p -> {
            EntityModel<Producto> productoModel = EntityModel.of(p,
                    linkTo(methodOn(ProductoController.class).obtener(id)).withSelfRel(),
                    linkTo(methodOn(ProductoController.class).listar()).withRel("productos"));
            return ResponseEntity.ok(productoModel);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        boolean eliminado = productoService.eliminar(id);
        if (eliminado) {
            return ResponseEntity.ok("Perfume eliminado");
        }
        return ResponseEntity.notFound().build();
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> actualizar(@PathVariable Long id, @RequestBody Producto productoActualizado) {
        Optional<Producto> productoOptional = productoService.obtenerPorId(id);
        if (productoOptional.isPresent()) {
            Producto productoExistente = productoOptional.get();

            productoExistente.setNombre(productoActualizado.getNombre());
            productoExistente.setDescripcion(productoActualizado.getDescripcion());
            productoExistente.setPrecio(productoActualizado.getPrecio());
            productoExistente.setCantidad(productoActualizado.getCantidad());

            productoService.guardar(productoExistente);

            EntityModel<Producto> productoModel = EntityModel.of(productoExistente,
                    linkTo(methodOn(ProductoController.class).obtener(id)).withSelfRel(),
                    linkTo(methodOn(ProductoController.class).listar()).withRel("productos"));

            return ResponseEntity.ok(productoModel);
        }
        return ResponseEntity.notFound().build();
    }
}
