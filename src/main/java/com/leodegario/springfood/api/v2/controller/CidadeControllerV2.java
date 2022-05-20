package com.leodegario.springfood.api.v2.controller;

import com.leodegario.springfood.api.ResourceUriHelper;
import com.leodegario.springfood.api.v2.assembler.CidadeInputDisassemblerV2;
import com.leodegario.springfood.api.v2.assembler.CidadeModelAssemblerV2;
import com.leodegario.springfood.api.v2.model.CidadeModelV2;
import com.leodegario.springfood.api.v2.model.input.CidadeInputV2;
import com.leodegario.springfood.core.web.FoodMediaTypes;
import com.leodegario.springfood.domain.exception.EstadoNaoEncontradoException;
import com.leodegario.springfood.domain.exception.NegocioException;
import com.leodegario.springfood.domain.model.Cidade;
import com.leodegario.springfood.domain.service.CadastroCidadeService;
import com.leodegario.springfood.repository.CidadeRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "Cidades")
@RestController
@RequestMapping(value = "/cidades")
public class CidadeControllerV2 {

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private CadastroCidadeService cadastroCidade;

    @Autowired
    private CidadeModelAssemblerV2 cidadeModelAssembler;

    @Autowired
    private CidadeInputDisassemblerV2 cidadeInputDisassembler;

    @GetMapping(produces = FoodMediaTypes.V2_APPLICATION_JSON_VALUE)
    public CollectionModel<CidadeModelV2> listar() {
        List<Cidade> todasCidades = cidadeRepository.findAll();

        return cidadeModelAssembler.toCollectionModel(todasCidades);
    }

    @GetMapping(path = "/{cidadeId}", produces = FoodMediaTypes.V2_APPLICATION_JSON_VALUE)
    public CidadeModelV2 buscar(
            @ApiParam(value = "ID de uma cidade", example = "1") @PathVariable Long cidadeId
    ) {
        Cidade cidade = cadastroCidade.buscarOuFalhar(cidadeId);

        return cidadeModelAssembler.toModel(cidade);
    }

    @PostMapping(produces = FoodMediaTypes.V2_APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CidadeModelV2 adicionar(
            @ApiParam(name = "corpo", value = "Representação de uma nova cidade")
            @RequestBody @Valid CidadeInputV2 cidadeInput
    ) {
        try {
            Cidade cidade = cidadeInputDisassembler.toDomainObject(cidadeInput);

            cidade = cadastroCidade.salvar(cidade);

            CidadeModelV2 cidadeModel = cidadeModelAssembler.toModel(cidade);

            ResourceUriHelper.addUriInResponseHeader(cidadeModel.getIdCidade());

            return cidadeModel;
        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @PutMapping(path = "/{cidadeId}", produces = FoodMediaTypes.V2_APPLICATION_JSON_VALUE)
    public CidadeModelV2 atualizar(
            @ApiParam(value = "ID de uma cidade", example = "1") @PathVariable Long cidadeId,
            @ApiParam(name = "corpo", value = "Representação de uma cidade")
            @RequestBody @Valid CidadeInputV2 cidadeInput
    ) {
        try {
            Cidade cidadeAtual = cadastroCidade.buscarOuFalhar(cidadeId);

            cidadeInputDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);

            cidadeAtual = cadastroCidade.salvar(cidadeAtual);

            return cidadeModelAssembler.toModel(cidadeAtual);
        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @DeleteMapping(value = "/{cidadeId}", produces = FoodMediaTypes.V2_APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(
            @ApiParam(value = "ID de uma cidade", example = "1") @PathVariable Long cidadeId
    ) {
        cadastroCidade.excluir(cidadeId);
    }

}