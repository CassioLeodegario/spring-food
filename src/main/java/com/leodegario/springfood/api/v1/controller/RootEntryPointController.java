package com.leodegario.springfood.api.v1.controller;

import com.leodegario.springfood.api.v1.SpringFoodLinks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1",produces = MediaType.APPLICATION_JSON_VALUE)
public class RootEntryPointController {

    @Autowired
    private SpringFoodLinks springFoodLinks;

    @GetMapping
    public RootEntryPointModel root(){
        var rootEntryPointModel = new RootEntryPointModel();

        rootEntryPointModel.add(springFoodLinks.linkToCozinhas("cozinhas"));
        rootEntryPointModel.add(springFoodLinks.linkToPedidos("pedidos"));
        rootEntryPointModel.add(springFoodLinks.linkToRestaurantes("restaurantes"));
        rootEntryPointModel.add(springFoodLinks.linkToGrupos("grupos"));
        rootEntryPointModel.add(springFoodLinks.linkToUsuarios("usuarios"));
        rootEntryPointModel.add(springFoodLinks.linkToPermissoes("permissoes"));
        rootEntryPointModel.add(springFoodLinks.linkToFormasPagamento("formas-pagamento"));
        rootEntryPointModel.add(springFoodLinks.linkToEstados("estados"));
        rootEntryPointModel.add(springFoodLinks.linkToCidades("cidades"));
        rootEntryPointModel.add(springFoodLinks.linkToEstatisticas("estatisticas"));


        return rootEntryPointModel;
    }

    private static class RootEntryPointModel extends RepresentationModel<RootEntryPointModel>{

    }
}
