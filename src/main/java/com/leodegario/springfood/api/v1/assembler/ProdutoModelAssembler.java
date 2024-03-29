package com.leodegario.springfood.api.v1.assembler;

import com.leodegario.springfood.api.v1.SpringFoodLinks;
import com.leodegario.springfood.api.v1.controller.RestauranteProdutoController;
import com.leodegario.springfood.api.v1.model.ProdutoModel;
import com.leodegario.springfood.core.security.SpringFoodSecurity;
import com.leodegario.springfood.domain.model.Produto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class ProdutoModelAssembler
        extends RepresentationModelAssemblerSupport<Produto, ProdutoModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SpringFoodLinks springFoodLinks;

    @Autowired
    private SpringFoodSecurity springFoodSecurity;

    public ProdutoModelAssembler() {
        super(RestauranteProdutoController.class, ProdutoModel.class);
    }

    @Override
    public ProdutoModel toModel(Produto produto) {
        ProdutoModel produtoModel = createModelWithId(
                produto.getId(), produto, produto.getRestaurante().getId());

        modelMapper.map(produto, produtoModel);

        if (springFoodSecurity.podeConsultarRestaurantes()) {
            produtoModel.add(springFoodLinks.linkToProdutos(produto.getRestaurante().getId(), "produtos"));

            produtoModel.add(springFoodLinks.linkToFotoProduto(
                    produto.getRestaurante().getId(), produto.getId(), "foto"));
        }

        return produtoModel;
    }
}