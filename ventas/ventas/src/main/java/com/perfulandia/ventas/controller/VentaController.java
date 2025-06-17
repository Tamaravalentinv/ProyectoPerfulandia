package com.perfulandia.ventas.controller;

import com.perfulandia.ventas.entity.Venta;
import com.perfulandia.ventas.service.VentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping
    public List<Venta> listarVentas() {
        return ventaService.obtenerVentas();
    }

    @PostMapping
    public ResponseEntity<String> registrarVenta(@RequestBody Venta venta) {
        ventaService.guardarVenta(venta);
        String mensaje = "Venta creada. Producto comprado: " + venta.getProducto();
        return ResponseEntity.status(201).body(mensaje);
    }
}
