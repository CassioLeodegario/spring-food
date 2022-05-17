package com.leodegario.springfood.api.assembler;

import com.leodegario.springfood.api.SpringFoodLinks;
import com.leodegario.springfood.api.controller.PedidoController;
import com.leodegario.springfood.api.controller.RestauranteController;
import com.leodegario.springfood.api.controller.UsuarioController;
import com.leodegario.springfood.api.model.PedidoModel;
import com.leodegario.springfood.api.model.PedidoResumoModel;
import com.leodegario.springfood.domain.model.Pedido;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PedidoResumoModelAssembler
        extends RepresentationModelAssemblerSupport<Pedido, PedidoResumoModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SpringFoodLinks springFoodLinks;

    public PedidoResumoModelAssembler() {
        super(PedidoController.class, PedidoResumoModel.class);
    }

    @Override
    public PedidoResumoModel toModel(Pedido pedido) {
        PedidoResumoModel pedidoModel = createModelWithId(pedido.getCodigo(), pedido);
        modelMapper.map(pedido, pedidoModel);

        pedidoModel.add(springFoodLinks.linkToPedidos());

        pedidoModel.getRestaurante().add(
                springFoodLinks.linkToRestaurante(pedido.getRestaurante().getId()));

        pedidoModel.getCliente().add(springFoodLinks.linkToUsuario(pedido.getCliente().getId()));

        return pedidoModel;
    }
}