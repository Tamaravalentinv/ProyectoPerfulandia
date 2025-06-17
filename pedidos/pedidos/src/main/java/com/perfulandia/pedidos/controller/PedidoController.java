package com.perfulandia.pedidos.controller;

import com.perfulandia.pedidos.model.Pedido;
import com.perfulandia.pedidos.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<Pedido>> listarPedidos() {
        List<Pedido> pedidos = pedidoService.getPedidos();
        return ResponseEntity.ok(pedidos);
    }

    @PostMapping
    public ResponseEntity<String> agregarPedido(@RequestBody Pedido pedido) {
        pedidoService.savePedido(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body("Pedido creado");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPedidoPorId(@PathVariable int id) {
        Pedido pedido = pedidoService.getPedidoId(id);
        if (pedido == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pedido);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> actualizarPedido(@PathVariable int id, @RequestBody Pedido pedido) {
        Pedido existente = pedidoService.getPedidoId(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        pedido.setId(id);
        Pedido actualizado = pedidoService.updatePedido(pedido);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPedido(@PathVariable int id) {
        Pedido existente = pedidoService.getPedidoId(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        pedidoService.deletePedido(id);
        return ResponseEntity.ok("Producto eliminado");
    }
}
