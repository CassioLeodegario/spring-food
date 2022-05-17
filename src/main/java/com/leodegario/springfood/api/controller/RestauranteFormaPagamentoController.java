package com.leodegario.springfood.api.controller;

import com.leodegario.springfood.api.SpringFoodLinks;
import com.leodegario.springfood.api.assembler.FormaPagamentoModelAssembler;
import com.leodegario.springfood.api.model.FormaPagamentoModel;
import com.leodegario.springfood.api.openapi.controller.RestauranteFormaPagamentoControllerOpenApi;
import com.leodegario.springfood.domain.model.Restaurante;
import com.leodegario.springfood.domain.service.CadastroRestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/restaurantes/{restauranteId}/formas-pagamento")
public class RestauranteFormaPagamentoController implements RestauranteFormaPagamentoControllerOpenApi {

    @Autowired
    private CadastroRestauranteService cadastroRestaurante;

    @Autowired
    private FormaPagamentoModelAssembler formaPagamentoModelAssembler;

    @Autowired
    private SpringFoodLinks springFoodLinks;

    @Override
    @GetMapping
    public CollectionModel<FormaPagamentoModel> listar(@PathVariable Long restauranteId) {
        Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);

        return formaPagamentoModelAssembler.toCollectionModel(restaurante.getFormasPagamento())
                .removeLinks()
                .add(springFoodLinks.linkToRestauranteFormasPagamento(restauranteId));
    }

    @DeleteMapping("/{formaPagamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desassociar(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId){
        cadastroRestaurante.desassociarFormaPagamento(restauranteId, formaPagamentoId);
    }


    @PutMapping("/{formaPagamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void associar(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId){
        cadastroRestaurante.associarFormaPagamento(restauranteId, formaPagamentoId);
    }


}
