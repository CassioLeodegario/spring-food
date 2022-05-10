package com.leodegario.springfood.api.controller;

import com.leodegario.springfood.api.assembler.CidadeInputDisassembler;
import com.leodegario.springfood.api.assembler.CidadeModelAssembler;
import com.leodegario.springfood.api.controller.openapi.CidadeControllerOpenApi;
import com.leodegario.springfood.api.model.CidadeModel;
import com.leodegario.springfood.api.model.input.CidadeInput;
import com.leodegario.springfood.domain.exception.EstadoNaoEncontradoException;
import com.leodegario.springfood.domain.exception.NegocioException;
import com.leodegario.springfood.domain.model.Cidade;
import com.leodegario.springfood.domain.service.CadastroCidadeService;
import com.leodegario.springfood.repository.CidadeRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "Cidades")
@RestController
@RequestMapping(value = "/cidades")
public class CidadeController implements CidadeControllerOpenApi {

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private CadastroCidadeService cadastroCidade;

    @Autowired
    private CidadeModelAssembler cidadeModelAssembler;

    @Autowired
    private CidadeInputDisassembler cidadeInputDisassembler;

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CidadeModel> listar() {
        List<Cidade> todasCidades = cidadeRepository.findAll();

        return cidadeModelAssembler.toCollectionModel(todasCidades);
    }

    @Override
    @GetMapping(path = "/{cidadeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CidadeModel buscar(
            @ApiParam(value = "ID de uma cidade", example = "1") @PathVariable Long cidadeId
    ) {
        Cidade cidade = cadastroCidade.buscarOuFalhar(cidadeId);

        return cidadeModelAssembler.toModel(cidade);
    }

    @Override
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CidadeModel adicionar(
            @ApiParam(name = "corpo", value = "Representação de uma nova cidade")
            @RequestBody @Valid CidadeInput cidadeInput
    ) {
        try {
            Cidade cidade = cidadeInputDisassembler.toDomainObject(cidadeInput);

            cidade = cadastroCidade.salvar(cidade);

            return cidadeModelAssembler.toModel(cidade);
        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @Override
    @PutMapping(path = "/{cidadeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CidadeModel atualizar(
            @ApiParam(value = "ID de uma cidade", example = "1") @PathVariable Long cidadeId,
            @ApiParam(name = "corpo", value = "Representação de uma cidade")
            @RequestBody @Valid CidadeInput cidadeInput
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

    @Override
    @DeleteMapping("/{cidadeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(
            @ApiParam(value = "ID de uma cidade", example = "1") @PathVariable Long cidadeId
    ) {
        cadastroCidade.excluir(cidadeId);
    }

}