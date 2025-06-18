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
        MockitoAnnotations.openMocks(this); // Inicializa los mocks
    }

    @Test
    void registrarVenta_deberiaRetornarMensajeCorrecto() {
        // Preparar
        Venta venta = new Venta();
        venta.setProducto("Perfume XYZ");

        // Ejecutar
        ResponseEntity<String> response = ventaController.registrarVenta(venta);

        // Verificar estado y contenido
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo("Venta creada. Producto comprado: Perfume XYZ");

        // Verificar llamada al servicio
        verify(ventaService).guardarVenta(venta);
    }
}
