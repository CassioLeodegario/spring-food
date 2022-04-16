package com.leodegario.springfood.domain.service;

import com.leodegario.springfood.domain.exception.PedidoNaoEncontradoException;
import com.leodegario.springfood.domain.model.Pedido;
import com.leodegario.springfood.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmissaoPedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    
    public Pedido buscarOuFalhar(Long pedidoId) {
        return pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new PedidoNaoEncontradoException(pedidoId));
    }            
}       