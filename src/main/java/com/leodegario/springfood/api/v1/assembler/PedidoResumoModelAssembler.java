package com.leodegario.springfood.api.v1.assembler;

import com.leodegario.springfood.api.v1.SpringFoodLinks;
import com.leodegario.springfood.api.v1.controller.PedidoController;
import com.leodegario.springfood.api.v1.model.PedidoResumoModel;
import com.leodegario.springfood.core.security.SpringFoodSecurity;
import com.leodegario.springfood.domain.model.Pedido;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class PedidoResumoModelAssembler
        extends RepresentationModelAssemblerSupport<Pedido, PedidoResumoModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SpringFoodLinks springFoodLinks;

    @Autowired
    private SpringFoodSecurity springFoodSecurity;

    public PedidoResumoModelAssembler() {
        super(PedidoController.class, PedidoResumoModel.class);
    }

    @Override
    public PedidoResumoModel toModel(Pedido pedido) {
        PedidoResumoModel pedidoModel = createModelWithId(pedido.getCodigo(), pedido);
        modelMapper.map(pedido, pedidoModel);

        if (springFoodSecurity.podePesquisarPedidos()) {
            pedidoModel.add(springFoodLinks.linkToPedidos("pedidos"));
        }

        if (springFoodSecurity.podeConsultarRestaurantes()) {
            pedidoModel.getRestaurante().add(
                    springFoodLinks.linkToRestaurante(pedido.getRestaurante().getId()));
        }

        if (springFoodSecurity.podeConsultarUsuariosGruposPermissoes()) {
            pedidoModel.getCliente().add(springFoodLinks.linkToUsuario(pedido.getCliente().getId()));
        }

        return pedidoModel;
    }
}