package com.perfulandia.ventas.controller;

import com.perfulandia.ventas.entity.Venta;
import com.perfulandia.ventas.service.VentaService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping
    public CollectionModel<Venta> listarVentas() {
        List<Venta> ventas = ventaService.obtenerVentas();

        List<Venta> ventasConLinks = ventas.stream()
            .map(venta -> {
                venta.add(WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(VentaController.class).obtenerVenta(venta.getId())
                ).withSelfRel());
                venta.add(WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(VentaController.class).listarVentas()
                ).withRel("ventas"));
                return venta;
            })
            .collect(Collectors.toList());

        return CollectionModel.of(ventasConLinks,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VentaController.class)
                .listarVentas()).withSelfRel());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> obtenerVenta(@PathVariable Integer id) {
        return ventaService.obtenerVentaPorId(id)
            .map(venta -> {
                venta.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VentaController.class).obtenerVenta(id)).withSelfRel());
                venta.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VentaController.class).listarVentas()).withRel("ventas"));
                return new ResponseEntity<>(venta, HttpStatus.OK);
            })
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<String> registrarVenta(@RequestBody Venta venta) {
        ventaService.guardarVenta(venta);
        String mensaje = "Venta creada. Producto comprado: " + venta.getProducto();
        return new ResponseEntity<>(mensaje, HttpStatus.CREATED);
    }
}
