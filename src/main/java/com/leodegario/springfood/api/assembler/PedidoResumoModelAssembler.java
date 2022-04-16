package com.leodegario.springfood.api.assembler;

import com.leodegario.springfood.api.model.PedidoModel;
import com.leodegario.springfood.api.model.PedidoResumoModel;
import com.leodegario.springfood.domain.model.Pedido;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PedidoResumoModelAssembler {

    @Autowired
    private ModelMapper modelMapper;
    
    public PedidoResumoModel toModel(Pedido pedido) {
        return modelMapper.map(pedido, PedidoResumoModel.class);
    }
    
    public List<PedidoResumoModel> toCollectionModel(List<Pedido> pedidos) {
        return pedidos.stream()
                .map(pedido -> toModel(pedido))
                .collect(Collectors.toList());
    }
    
}