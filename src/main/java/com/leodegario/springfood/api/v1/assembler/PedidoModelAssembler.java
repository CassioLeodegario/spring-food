package com.leodegario.springfood.api.v1.assembler;

import com.leodegario.springfood.api.v1.SpringFoodLinks;
import com.leodegario.springfood.api.v1.controller.*;
import com.leodegario.springfood.api.v1.model.PedidoModel;
import com.leodegario.springfood.core.security.SpringFoodSecurity;
import com.leodegario.springfood.domain.model.Pedido;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class PedidoModelAssembler
        extends RepresentationModelAssemblerSupport<Pedido, PedidoModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SpringFoodLinks springFoodLinks;

    @Autowired
    private SpringFoodSecurity springFoodSecurity;

    public PedidoModelAssembler() {
        super(PedidoController.class, PedidoModel.class);
    }

    @Override
    public PedidoModel toModel(Pedido pedido) {
        PedidoModel pedidoModel = createModelWithId(pedido.getCodigo(), pedido);
        modelMapper.map(pedido, pedidoModel);

        if (springFoodSecurity.podePesquisarPedidos()) {
            pedidoModel.add(springFoodLinks.linkToPedidos("pedidos"));
        }

        if (springFoodSecurity.podeGerenciarPedidos(pedido.getCodigo())) {
            if (pedido.podeSerConfirmado()) {
                pedidoModel.add(springFoodLinks.linkToConfirmacaoPedido(pedido.getCodigo(), "confirmar"));
            }

            if (pedido.podeSerCancelado()) {
                pedidoModel.add(springFoodLinks.linkToCancelamentoPedido(pedido.getCodigo(), "cancelar"));
            }

            if (pedido.podeSerEntregue()) {
                pedidoModel.add(springFoodLinks.linkToEntregaPedido(pedido.getCodigo(), "entregar"));
            }
        }

        if (springFoodSecurity.podeConsultarRestaurantes()) {
            pedidoModel.getRestaurante().add(
                    springFoodLinks.linkToRestaurante(pedido.getRestaurante().getId()));
        }

        if (springFoodSecurity.podeConsultarUsuariosGruposPermissoes()) {
            pedidoModel.getCliente().add(
                    springFoodLinks.linkToUsuario(pedido.getCliente().getId()));
        }

        if (springFoodSecurity.podeConsultarFormasPagamento()) {
            pedidoModel.getFormaPagamento().add(
                    springFoodLinks.linkToFormaPagamento(pedido.getFormaPagamento().getId()));
        }

        if (springFoodSecurity.podeConsultarCidades()) {
            pedidoModel.getEnderecoEntrega().getCidade().add(
                    springFoodLinks.linkToCidade(pedido.getEnderecoEntrega().getCidade().getId()));
        }

        if (springFoodSecurity.podeConsultarRestaurantes()) {
            pedidoModel.getItens().forEach(item -> {
                item.add(springFoodLinks.linkToProduto(
                        pedidoModel.getRestaurante().getId(), item.getProdutoId(), "produto"));
            });
        }

        return pedidoModel;
    }
}