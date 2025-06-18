package com.perfulandia.inventario.controller;

import com.perfulandia.inventario.model.Producto;
import com.perfulandia.inventario.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController productoController;

    public ProductoControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
void listar_deberiaRetornarListaDeProductos() {
    List<Producto> productos = Arrays.asList(new Producto(), new Producto());
    when(productoService.listar()).thenReturn(productos);

    ResponseEntity<CollectionModel<EntityModel<Producto>>> respuesta = productoController.listar();

    assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
    assertThat(respuesta.getBody()).isNotNull();
    assertThat(respuesta.getBody().getContent()).hasSize(2);
}


    @Test
    void guardar_deberiaRetornarRespuestaCreado() {
        Producto producto = new Producto();
        ResponseEntity<String> respuesta = productoController.guardar(producto);

        assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
        assertThat(respuesta.getBody()).isEqualTo("Perfume ingresado");
    }

    @Test
void obtener_deberiaRetornarProductoSiExiste() {
    Producto producto = new Producto();
    when(productoService.obtenerPorId(1L)).thenReturn(Optional.of(producto));

    ResponseEntity<EntityModel<Producto>> respuesta = productoController.obtener(1L);

    assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
    assertThat(respuesta.getBody()).isNotNull();
    assertThat(respuesta.getBody().getContent()).isEqualTo(producto);
}


    @Test
    void eliminar_deberiaRetornarOkSiEliminado() {
        when(productoService.eliminar(1L)).thenReturn(true);

        ResponseEntity<String> respuesta = productoController.eliminar(1L);

        assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
        assertThat(respuesta.getBody()).isEqualTo("Perfume eliminado");
    }
}
