package com.leodegario.springfood.api.assembler;

import com.leodegario.springfood.api.model.FotoProdutoModel;
import com.leodegario.springfood.api.model.PedidoModel;
import com.leodegario.springfood.domain.model.FotoProduto;
import com.leodegario.springfood.domain.model.Pedido;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FotoProdutoModelAssembler {

    @Autowired
    private ModelMapper modelMapper;
    
    public FotoProdutoModel toModel(FotoProduto fotoProduto) {
        return modelMapper.map(fotoProduto, FotoProdutoModel.class);
    }

}