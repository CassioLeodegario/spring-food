package com.leodegario.springfood.api.assembler;

import com.leodegario.springfood.api.SpringFoodLinks;
import com.leodegario.springfood.api.controller.RestauranteController;
import com.leodegario.springfood.api.model.RestauranteModel;
import com.leodegario.springfood.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class RestauranteModelAssembler
        extends RepresentationModelAssemblerSupport<Restaurante, RestauranteModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SpringFoodLinks springFoodLinks;

    public RestauranteModelAssembler() {
        super(RestauranteController.class, RestauranteModel.class);
    }

    @Override
    public RestauranteModel toModel(Restaurante restaurante) {
        RestauranteModel restauranteModel = createModelWithId(restaurante.getId(), restaurante);
        modelMapper.map(restaurante, restauranteModel);

        restauranteModel.add(springFoodLinks.linkToRestaurantes("restaurantes"));

        restauranteModel.getCozinha().add(
                springFoodLinks.linkToCozinha(restaurante.getCozinha().getId()));

        restauranteModel.getEndereco().getCidade().add(
                springFoodLinks.linkToCidade(restaurante.getEndereco().getCidade().getId()));

        restauranteModel.add(springFoodLinks.linkToRestauranteFormasPagamento(restaurante.getId(),
                "formas-pagamento"));

        restauranteModel.add(springFoodLinks.linkToRestauranteResponsaveis(restaurante.getId(),
                "responsaveis"));

        return restauranteModel;
    }

    @Override
    public CollectionModel<RestauranteModel> toCollectionModel(Iterable<? extends Restaurante> entities) {
        return super.toCollectionModel(entities)
                .add(springFoodLinks.linkToRestaurantes());
    }
}
