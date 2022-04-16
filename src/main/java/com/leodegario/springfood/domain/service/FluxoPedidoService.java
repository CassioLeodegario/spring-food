package com.leodegario.springfood.domain.service;

import com.leodegario.springfood.domain.exception.NegocioException;
import com.leodegario.springfood.domain.model.Pedido;
import com.leodegario.springfood.domain.model.StatusPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;

@Service
public class FluxoPedidoService {

    @Autowired
    private EmissaoPedidoService emissaoPedidoService;

    public void confirmar(Long pedidoId){
        Pedido pedido = emissaoPedidoService.buscarOuFalhar(pedidoId);
        if(!pedido.getStatus().equals(StatusPedido.CRIADO)){
            throw new NegocioException(
                    String.format("Status do Pedido %d não pode ser alterado de %s para %s",
                            pedidoId, pedido.getStatus(), StatusPedido.CONFIRMADO));
        }
        pedido.setStatus(StatusPedido.CONFIRMADO);
        pedido.setDataConfirmacao(OffsetDateTime.now());
    }

    @Transactional
    public void cancelar(Long pedidoId) {
        Pedido pedido = emissaoPedidoService.buscarOuFalhar(pedidoId);

        if (!pedido.getStatus().equals(StatusPedido.CRIADO)) {
            throw new NegocioException(
                    String.format("Status do pedido %d não pode ser alterado de %s para %s",
                            pedido.getId(), pedido.getStatus().getDescricao(),
                            StatusPedido.CANCELADO.getDescricao()));
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        pedido.setDataCancelamento(OffsetDateTime.now());
    }

    @Transactional
    public void entregar(Long pedidoId) {
        Pedido pedido = emissaoPedidoService.buscarOuFalhar(pedidoId);

        if (!pedido.getStatus().equals(StatusPedido.CONFIRMADO)) {
            throw new NegocioException(
                    String.format("Status do pedido %d não pode ser alterado de %s para %s",
                            pedido.getId(), pedido.getStatus().getDescricao(),
                            StatusPedido.ENTREGUE.getDescricao()));
        }

        pedido.setStatus(StatusPedido.ENTREGUE);
        pedido.setDataEntrega(OffsetDateTime.now());
    }
}
