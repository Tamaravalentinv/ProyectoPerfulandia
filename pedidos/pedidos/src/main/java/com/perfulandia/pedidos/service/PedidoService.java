package com.perfulandia.pedidos.service;

import com.perfulandia.pedidos.model.Pedido;
import com.perfulandia.pedidos.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    public List<Pedido> getPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido getPedidoId(Integer id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    public Pedido savePedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public Pedido updatePedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public void deletePedido(Integer id) {
        pedidoRepository.deleteById(id);
    }
}
