package com.leodegario.springfood.api.v1.assembler;

import com.leodegario.springfood.api.v1.SpringFoodLinks;
import com.leodegario.springfood.api.v1.controller.RestauranteController;
import com.leodegario.springfood.api.v1.model.RestauranteBasicoModel;
import com.leodegario.springfood.core.security.SpringFoodSecurity;
import com.leodegario.springfood.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class RestauranteBasicoModelAssembler 
        extends RepresentationModelAssemblerSupport<Restaurante, RestauranteBasicoModel> {

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private SpringFoodLinks springFoodLinks;

    @Autowired
    private SpringFoodSecurity springFoodSecurity;
    
    public RestauranteBasicoModelAssembler() {
        super(RestauranteController.class, RestauranteBasicoModel.class);
    }

    @Override
    public RestauranteBasicoModel toModel(Restaurante restaurante) {
        RestauranteBasicoModel restauranteModel = createModelWithId(
                restaurante.getId(), restaurante);

        modelMapper.map(restaurante, restauranteModel);

        if (springFoodSecurity.podeConsultarRestaurantes()) {
            restauranteModel.add(springFoodLinks.linkToRestaurantes("restaurantes"));
        }

        if (springFoodSecurity.podeConsultarCozinhas()) {
            restauranteModel.getCozinha().add(
                    springFoodLinks.linkToCozinha(restaurante.getCozinha().getId()));
        }

        return restauranteModel;
    }

    @Override
    public CollectionModel<RestauranteBasicoModel> toCollectionModel(Iterable<? extends Restaurante> entities) {
        CollectionModel<RestauranteBasicoModel> collectionModel = super.toCollectionModel(entities);

        if (springFoodSecurity.podeConsultarRestaurantes()) {
            collectionModel.add(springFoodLinks.linkToRestaurantes());
        }

        return collectionModel;
    }
}        