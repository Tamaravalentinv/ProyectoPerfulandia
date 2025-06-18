package com.perfulandia.pedidos.controller;

import com.perfulandia.pedidos.model.Pedido;
import com.perfulandia.pedidos.service.PedidoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class PedidoControllerTest {

    @Mock
    private PedidoService pedidoService;

    @InjectMocks
    private PedidoController pedidoController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarPedidos_deberiaRetornarListaDePedidos() {
        List<Pedido> listaPedidos = Arrays.asList(new Pedido(), new Pedido());
        when(pedidoService.getPedidos()).thenReturn(listaPedidos);

        ResponseEntity<CollectionModel<EntityModel<Pedido>>> response = pedidoController.listarPedidos();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        CollectionModel<EntityModel<Pedido>> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getContent()).hasSize(2);

        verify(pedidoService).getPedidos();
    }

    @Test
    void agregarPedido_deberiaRetornarRespuestaCreada() {
        Pedido pedido = new Pedido();
        when(pedidoService.savePedido(any(Pedido.class))).thenReturn(pedido);

        ResponseEntity<String> response = pedidoController.agregarPedido(pedido);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo("Pedido creado");
        verify(pedidoService).savePedido(pedido);
    }

    @Test
    void obtenerPedidoPorId_deberiaRetornarPedidoSiExiste() {
    Pedido pedido = new Pedido();
    pedido.setId(1);
    pedido.setNombre("Prueba");
    pedido.setValor(100);

    when(pedidoService.getPedidoId(1)).thenReturn(pedido);

    ResponseEntity<EntityModel<Pedido>> response = pedidoController.obtenerPedidoPorId(1);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getContent()).isEqualTo(pedido);

    verify(pedidoService).getPedidoId(1);
    }

    @Test
    void obtenerPedidoPorId_deberiaRetornarNotFoundSiNoExiste() {
        when(pedidoService.getPedidoId(999)).thenReturn(null);

        ResponseEntity<EntityModel<Pedido>> response = pedidoController.obtenerPedidoPorId(999);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(pedidoService).getPedidoId(999);
    }

    @Test
    void actualizarPedido_deberiaRetornarPedidoActualizadoSiExiste() {
        Pedido pedidoExistente = new Pedido();
        Pedido pedidoActualizado = new Pedido();
        when(pedidoService.getPedidoId(1)).thenReturn(pedidoExistente);
        when(pedidoService.updatePedido(any(Pedido.class))).thenReturn(pedidoActualizado);

        ResponseEntity<EntityModel<Pedido>> response = pedidoController.actualizarPedido(1, pedidoActualizado);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).isEqualTo(pedidoActualizado);

        verify(pedidoService).getPedidoId(1);
        verify(pedidoService).updatePedido(any(Pedido.class));
    }

    @Test
    void actualizarPedido_deberiaRetornarNotFoundSiNoExiste() {
        when(pedidoService.getPedidoId(2)).thenReturn(null);

        ResponseEntity<EntityModel<Pedido>> response = pedidoController.actualizarPedido(2, new Pedido());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(pedidoService).getPedidoId(2);
        verify(pedidoService, never()).updatePedido(any(Pedido.class));
    }

    @Test
    void eliminarPedido_deberiaRetornarOkSiExiste() {
        Pedido pedidoExistente = new Pedido();
        when(pedidoService.getPedidoId(1)).thenReturn(pedidoExistente);
        doNothing().when(pedidoService).deletePedido(1);

        ResponseEntity<String> response = pedidoController.eliminarPedido(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Producto eliminado");
        verify(pedidoService).getPedidoId(1);
        verify(pedidoService).deletePedido(1);
    }

    @Test
    void eliminarPedido_deberiaRetornarNotFoundSiNoExiste() {
        when(pedidoService.getPedidoId(999)).thenReturn(null);

        ResponseEntity<String> response = pedidoController.eliminarPedido(999);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(pedidoService).getPedidoId(999);
        verify(pedidoService, never()).deletePedido(anyInt());
    }
}
