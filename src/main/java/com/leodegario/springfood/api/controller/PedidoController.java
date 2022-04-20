package com.leodegario.springfood.api.controller;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.leodegario.springfood.api.assembler.PedidoInputDisassembler;
import com.leodegario.springfood.api.assembler.PedidoModelAssembler;
import com.leodegario.springfood.api.assembler.PedidoResumoModelAssembler;
import com.leodegario.springfood.api.model.PedidoModel;
import com.leodegario.springfood.api.model.PedidoResumoModel;
import com.leodegario.springfood.api.model.input.PedidoInput;
import com.leodegario.springfood.domain.exception.EntidadeNaoEncontradaException;
import com.leodegario.springfood.domain.exception.NegocioException;
import com.leodegario.springfood.domain.model.Pedido;
import com.leodegario.springfood.domain.model.Usuario;
import com.leodegario.springfood.domain.service.EmissaoPedidoService;
import com.leodegario.springfood.infrastructure.repository.spec.PedidoSpecs;
import com.leodegario.springfood.repository.PedidoRepository;
import com.leodegario.springfood.repository.filter.PedidoFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoController {

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
    
//    @GetMapping
//    public MappingJacksonValue listar(
//            @RequestParam(required = false) String campos
//    ) {
//        List<Pedido> todosPedidos = pedidoRepository.findAll();
//        List<PedidoResumoModel> pedidosModel = pedidoResumoModelAssembler.toCollectionModel(todosPedidos);
//
//        MappingJacksonValue pedidosWrapper = new MappingJacksonValue(pedidosModel);
//        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
////        filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter
////                .filterOutAllExcept("codigo", "valorTotal"));
//
//        if(campos == null || campos.trim().isEmpty()){
//            filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.serializeAll());
//        }else{
//            filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter
//                .filterOutAllExcept(campos.split(",")));
//        }
//
//        pedidosWrapper.setFilters(filterProvider);
//
//
//        return pedidosWrapper;
//    }

    @GetMapping
    public List<PedidoResumoModel> pesquisar(PedidoFilter filtro) {
        List<Pedido> todosPedidos = pedidoRepository.findAll(PedidoSpecs.usandoFiltro(filtro));

        return pedidoResumoModelAssembler.toCollectionModel(todosPedidos);
    }


    @GetMapping("/{pedidoId}")
    public PedidoModel buscar(@PathVariable String codigoPedido) {
        Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);
        
        return pedidoModelAssembler.toModel(pedido);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoModel adicionar(@Valid @RequestBody PedidoInput pedidoInput) {
        try {
            Pedido novoPedido = pedidoInputDisassembler.toDomainObject(pedidoInput);

            // TODO pegar usuário autenticado
            novoPedido.setCliente(new Usuario());
            novoPedido.getCliente().setId(1L);

            novoPedido = emissaoPedido.emitir(novoPedido);

            return pedidoModelAssembler.toModel(novoPedido);
        } catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }
}           