package com.perfulandia.pedidos.controller;

import com.perfulandia.pedidos.model.Pedido;
import com.perfulandia.pedidos.service.PedidoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping(produces = "application/hal+json")
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> listarPedidos() {
        List<Pedido> pedidos = pedidoService.getPedidos();

        List<EntityModel<Pedido>> pedidosModel = pedidos.stream()
                .map(pedido -> EntityModel.of(pedido,
                        linkTo(methodOn(PedidoController.class).obtenerPedidoPorId(pedido.getId())).withSelfRel(),
                        linkTo(methodOn(PedidoController.class).listarPedidos()).withRel("pedidos")))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Pedido>> collectionModel = CollectionModel.of(pedidosModel,
                linkTo(methodOn(PedidoController.class).listarPedidos()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @PostMapping
    public ResponseEntity<String> agregarPedido(@RequestBody Pedido pedido) {
        pedidoService.savePedido(pedido);
        return new ResponseEntity<>("Pedido creado", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Pedido>> obtenerPedidoPorId(@PathVariable Integer id) {
        Pedido pedido = pedidoService.getPedidoId(id);
        if (pedido == null) {
            return ResponseEntity.notFound().build();
        }
        EntityModel<Pedido> pedidoModel = EntityModel.of(pedido,
                linkTo(methodOn(PedidoController.class).obtenerPedidoPorId(id)).withSelfRel(),
                linkTo(methodOn(PedidoController.class).listarPedidos()).withRel("pedidos"));

        return ResponseEntity.ok(pedidoModel);
    }

    @PutMapping("/{id}")
public ResponseEntity<EntityModel<Pedido>> actualizarPedido(@PathVariable Integer id, @RequestBody Pedido pedidoActualizado) {
    // Obtener el pedido existente por ID
    Pedido pedidoExistente = pedidoService.getPedidoId(id);
    if (pedidoExistente == null) {
        // Si no existe, devolver 404
        return ResponseEntity.notFound().build();
    }

    // Actualizar los campos del pedido existente con los nuevos datos
    // Aquí puedes actualizar solo los campos que quieres permitir modificar
    pedidoExistente.setNombre(pedidoActualizado.getNombre());
    pedidoExistente.setValor(pedidoActualizado.getValor());

    // Guardar el pedido actualizado
    Pedido pedidoGuardado = pedidoService.updatePedido(pedidoExistente);

    // Crear el EntityModel con los enlaces HATEOAS
    EntityModel<Pedido> pedidoModel = EntityModel.of(pedidoGuardado,
            linkTo(methodOn(PedidoController.class).obtenerPedidoPorId(id)).withSelfRel(),
            linkTo(methodOn(PedidoController.class).listarPedidos()).withRel("pedidos"));

    // Devolver respuesta 200 con el pedido actualizado
    return ResponseEntity.ok(pedidoModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPedido(@PathVariable Integer id) {
        Pedido pedidoExistente = pedidoService.getPedidoId(id);
        if (pedidoExistente == null) {
            return ResponseEntity.notFound().build();
        }
        pedidoService.deletePedido(id);
        return ResponseEntity.ok("Pedido eliminado");
    }
}
