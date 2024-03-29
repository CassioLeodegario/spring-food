package com.leodegario.springfood.api.v1.controller;

import com.leodegario.springfood.api.v1.SpringFoodLinks;
import com.leodegario.springfood.core.security.SpringFoodSecurity;
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

    @Autowired
    private SpringFoodSecurity springFoodSecurity;

    @GetMapping
    public RootEntryPointModel root() {
        var rootEntryPointModel = new RootEntryPointModel();

        if (springFoodSecurity.podeConsultarCozinhas()) {
            rootEntryPointModel.add(springFoodLinks.linkToCozinhas("cozinhas"));
        }

        if (springFoodSecurity.podePesquisarPedidos()) {
            rootEntryPointModel.add(springFoodLinks.linkToPedidos("pedidos"));
        }

        if (springFoodSecurity.podeConsultarRestaurantes()) {
            rootEntryPointModel.add(springFoodLinks.linkToRestaurantes("restaurantes"));
        }

        if (springFoodSecurity.podeConsultarUsuariosGruposPermissoes()) {
            rootEntryPointModel.add(springFoodLinks.linkToGrupos("grupos"));
            rootEntryPointModel.add(springFoodLinks.linkToUsuarios("usuarios"));
            rootEntryPointModel.add(springFoodLinks.linkToPermissoes("permissoes"));
        }

        if (springFoodSecurity.podeConsultarFormasPagamento()) {
            rootEntryPointModel.add(springFoodLinks.linkToFormasPagamento("formas-pagamento"));
        }

        if (springFoodSecurity.podeConsultarEstados()) {
            rootEntryPointModel.add(springFoodLinks.linkToEstados("estados"));
        }

        if (springFoodSecurity.podeConsultarCidades()) {
            rootEntryPointModel.add(springFoodLinks.linkToCidades("cidades"));
        }

        if (springFoodSecurity.podeConsultarEstatisticas()) {
            rootEntryPointModel.add(springFoodLinks.linkToEstatisticas("estatisticas"));
        }

        return rootEntryPointModel;
    }

    private static class RootEntryPointModel extends RepresentationModel<RootEntryPointModel>{

    }
}
