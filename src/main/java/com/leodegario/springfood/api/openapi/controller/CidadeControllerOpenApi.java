package com.leodegario.springfood.api.openapi.controller;

import com.leodegario.springfood.api.exceptionhandler.Problem;
import com.leodegario.springfood.api.model.CidadeModel;
import com.leodegario.springfood.api.model.input.CidadeInput;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

public interface CidadeControllerOpenApi {
    @ApiOperation("Lista as Cidades")
    @GetMapping
    CollectionModel<CidadeModel> listar();

    @ApiOperation("Busca cidade por Id")
    @ApiResponses({
            @ApiResponse(code = 400, message = "ID da cidade inválido", response = Problem.class),
            @ApiResponse(code = 404, message = "Cidade não encontrada", response = Problem.class)
    })
    @GetMapping("/{cidadeId}")
    CidadeModel buscar(
            @ApiParam(value = "ID de uma cidade", example = "1", required = true)
                    Long cidadeId);

    @ApiOperation("Cadastra uma cidade")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cidade cadastrada")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CidadeModel adicionar(
            @ApiParam(name = "corpo", value = "Representação de uma nova cidade", required = true)
                    CidadeInput cidadeInput);

    @ApiOperation("Atualiza uma cidade")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cidade atualizada"),
            @ApiResponse(code = 404, message = "Cidade não encontrada", response = Problem.class)
    })
    @PutMapping("/{cidadeId}")
    CidadeModel atualizar(
            @ApiParam(value = "ID de uma cidade", example = "1", required = true)
                    Long cidadeId,
            @ApiParam(name = "corpo", value = "Representação de uma cidade com os novos dados")
                    CidadeInput cidadeInput);

    @ApiOperation("Exclui uma cidade")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Cidade excluída"),
            @ApiResponse(code = 404, message = "Cidade não encontrada", response = Problem.class)
    })
    @DeleteMapping("/{cidadeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void remover(
            @ApiParam(value = "ID de uma cidade", example = "1", required = true)
                    Long cidadeId);
}
