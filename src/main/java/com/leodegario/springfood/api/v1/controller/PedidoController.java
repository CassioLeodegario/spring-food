package com.leodegario.springfood.api.v1.controller;

import com.leodegario.springfood.api.v1.assembler.PedidoInputDisassembler;
import com.leodegario.springfood.api.v1.assembler.PedidoModelAssembler;
import com.leodegario.springfood.api.v1.assembler.PedidoResumoModelAssembler;
import com.leodegario.springfood.api.v1.model.PedidoModel;
import com.leodegario.springfood.api.v1.model.PedidoResumoModel;
import com.leodegario.springfood.api.v1.model.input.PedidoInput;
import com.leodegario.springfood.api.v1.openapi.controller.PedidoControllerOpenApi;
import com.leodegario.springfood.core.data.PageWrapper;
import com.leodegario.springfood.core.data.PageableTranslator;
import com.leodegario.springfood.core.security.CheckSecurity;
import com.leodegario.springfood.core.security.SpringFoodSecurity;
import com.leodegario.springfood.domain.exception.EntidadeNaoEncontradaException;
import com.leodegario.springfood.domain.exception.NegocioException;
import com.leodegario.springfood.domain.filter.PedidoFilter;
import com.leodegario.springfood.domain.model.Pedido;
import com.leodegario.springfood.domain.model.Usuario;
import com.leodegario.springfood.domain.service.EmissaoPedidoService;
import com.leodegario.springfood.infrastructure.repository.spec.PedidoSpecs;
import com.leodegario.springfood.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(value = "/v1/pedidos")
public class PedidoController implements PedidoControllerOpenApi {

    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private EmissaoPedidoService emissaoPedido;
    
    @Autowired
    private PedidoModelAssembler pedidoModelAssembler;

    @Autowired
    private PedidoResumoModelAssembler pedidoResumoModelAssembler;

    @Autowired
    private PedidoInputDisassembler pedidoInputDisassembler;

    @Autowired
    private PagedResourcesAssembler<Pedido> pagedResourcesAssembler;

    @Autowired
    private SpringFoodSecurity springFoodSecurity;

    @CheckSecurity.Pedidos.PodePesquisar
    @Override
    @GetMapping
    public PagedModel<PedidoResumoModel> pesquisar(PedidoFilter filtro,
                                                   @PageableDefault(size = 10) Pageable pageable) {
        Pageable pageableTraduzido = traduzirPageable(pageable);

        Page<Pedido> pedidosPage = pedidoRepository.findAll(
                PedidoSpecs.usandoFiltro(filtro), pageableTraduzido);

        pedidosPage = new PageWrapper<>(pedidosPage, pageable);

        return pagedResourcesAssembler.toModel(pedidosPage, pedidoResumoModelAssembler);
    }


    @CheckSecurity.Pedidos.PodeBuscar
    @GetMapping("/{codigoPedido}")
    public PedidoModel buscar(@PathVariable String codigoPedido) {
        Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);
        
        return pedidoModelAssembler.toModel(pedido);
    }

    @CheckSecurity.Pedidos.PodeCriar
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoModel adicionar(@Valid @RequestBody PedidoInput pedidoInput) {
        try {
            Pedido novoPedido = pedidoInputDisassembler.toDomainObject(pedidoInput);

            novoPedido.setCliente(new Usuario());
            novoPedido.getCliente().setId(springFoodSecurity.getUsuarioId());

            novoPedido = emissaoPedido.emitir(novoPedido);

            return pedidoModelAssembler.toModel(novoPedido);
        } catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }


    private Pageable traduzirPageable(Pageable apiPageable) {
        var mapeamento = Map.of(
                "codigo", "codigo",
                "subtotal", "subtotal",
                "taxaFrete", "taxaFrete",
                "valorTotal", "valorTotal",
                "dataCriacao", "dataCriacao",
                "nomerestaurante", "restaurante.nome",
                "restaurante.id", "restaurante.id",
                "cliente.id", "cliente.id",
                "cliente.nome", "cliente.nome"
        );

        return PageableTranslator.translate(apiPageable, mapeamento);
    }
}           