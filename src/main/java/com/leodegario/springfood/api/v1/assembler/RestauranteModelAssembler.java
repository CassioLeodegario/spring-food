package com.leodegario.springfood.api.v1.assembler;

import com.leodegario.springfood.api.v1.SpringFoodLinks;
import com.leodegario.springfood.api.v1.controller.RestauranteController;
import com.leodegario.springfood.api.v1.model.RestauranteModel;
import com.leodegario.springfood.core.security.SpringFoodSecurity;
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

    @Autowired
    private SpringFoodSecurity springFoodSecurity;

    public RestauranteModelAssembler() {
        super(RestauranteController.class, RestauranteModel.class);
    }

    @Override
    public RestauranteModel toModel(Restaurante restaurante) {
        RestauranteModel restauranteModel = createModelWithId(restaurante.getId(), restaurante);
        modelMapper.map(restaurante, restauranteModel);

        if (springFoodSecurity.podeConsultarRestaurantes()) {
            restauranteModel.add(springFoodLinks.linkToRestaurantes("restaurantes"));
        }

        if (springFoodSecurity.podeGerenciarCadastroRestaurantes()) {
            if (restaurante.ativacaoPermitida()) {
                restauranteModel.add(
                        springFoodLinks.linkToRestauranteAtivacao(restaurante.getId(), "ativar"));
            }

            if (restaurante.inativacaoPermitida()) {
                restauranteModel.add(
                        springFoodLinks.linkToRestauranteInativacao(restaurante.getId(), "inativar"));
            }
        }

        if (springFoodSecurity.podeGerenciarFuncionamentoRestaurantes(restaurante.getId())) {
            if (restaurante.aberturaPermitida()) {
                restauranteModel.add(
                        springFoodLinks.linkToRestauranteAbertura(restaurante.getId(), "abrir"));
            }

            if (restaurante.fechamentoPermitido()) {
                restauranteModel.add(
                        springFoodLinks.linkToRestauranteFechamento(restaurante.getId(), "fechar"));
            }
        }

        if (springFoodSecurity.podeConsultarRestaurantes()) {
            restauranteModel.add(springFoodLinks.linkToProdutos(restaurante.getId(), "produtos"));
        }

        if (springFoodSecurity.podeConsultarCozinhas()) {
            restauranteModel.getCozinha().add(
                    springFoodLinks.linkToCozinha(restaurante.getCozinha().getId()));
        }

        if (springFoodSecurity.podeConsultarCidades()) {
            if (restauranteModel.getEndereco() != null
                    && restauranteModel.getEndereco().getCidade() != null) {
                restauranteModel.getEndereco().getCidade().add(
                        springFoodLinks.linkToCidade(restaurante.getEndereco().getCidade().getId()));
            }
        }

        if (springFoodSecurity.podeConsultarRestaurantes()) {
            restauranteModel.add(springFoodLinks.linkToRestauranteFormasPagamento(restaurante.getId(),
                    "formas-pagamento"));
        }

        if (springFoodSecurity.podeGerenciarCadastroRestaurantes()) {
            restauranteModel.add(springFoodLinks.linkToRestauranteResponsaveis(restaurante.getId(),
                    "responsaveis"));
        }

        return restauranteModel;
    }

    @Override
    public CollectionModel<RestauranteModel> toCollectionModel(Iterable<? extends Restaurante> entities) {
        CollectionModel<RestauranteModel> collectionModel = super.toCollectionModel(entities);

        if (springFoodSecurity.podeConsultarRestaurantes()) {
            collectionModel.add(springFoodLinks.linkToRestaurantes());
        }

        return collectionModel;
    }
}
