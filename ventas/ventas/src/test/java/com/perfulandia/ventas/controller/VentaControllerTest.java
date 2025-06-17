package com.perfulandia.ventas.controller;

import com.perfulandia.ventas.entity.Venta;
import com.perfulandia.ventas.service.VentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

public class VentaControllerTest {

    @Mock
    private VentaService ventaService;

    @InjectMocks
    private VentaController ventaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registrarVenta_deberiaRetornarMensajeCorrecto() {
        Venta venta = new Venta();
        venta.setProducto("Perfume XYZ");

        // No mocks necesarios para método void guardarVenta

        // Ejecutar el método
        ResponseEntity<String> response = ventaController.registrarVenta(venta);

        // Validar la respuesta
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo("Venta creada. Producto comprado: Perfume XYZ");

        // Verificar que guardarVenta fue llamado con la venta
        verify(ventaService).guardarVenta(venta);
    }
}
