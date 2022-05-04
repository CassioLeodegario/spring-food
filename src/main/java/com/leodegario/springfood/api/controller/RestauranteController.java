package com.leodegario.springfood.api.controller;

import com.leodegario.springfood.api.assembler.RestauranteInputDisassembler;
import com.leodegario.springfood.api.assembler.RestauranteModelAssembler;
import com.leodegario.springfood.api.model.RestauranteModel;
import com.leodegario.springfood.api.model.input.RestauranteInput;
import com.leodegario.springfood.api.model.view.RestauranteView;
import com.leodegario.springfood.domain.exception.CidadeNaoEncontradaException;
import com.leodegario.springfood.domain.exception.CozinhaNaoEncontradaException;
import com.leodegario.springfood.domain.exception.NegocioException;
import com.leodegario.springfood.domain.exception.RestauranteNaoEncontradoException;
import com.leodegario.springfood.domain.model.Restaurante;
import com.leodegario.springfood.domain.service.CadastroRestauranteService;
import com.leodegario.springfood.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    @Autowired
    RestauranteRepository restauranteRepository;

    @Autowired
    CadastroRestauranteService cadastroRestauranteService;

    @Autowired
    private RestauranteModelAssembler restauranteModelAssembler;

    @Autowired
    private RestauranteInputDisassembler restauranteInputDisassembler;

    @GetMapping
    public MappingJacksonValue todos(@RequestParam(required = false) String projecao) {
        List<Restaurante> restaurantes = restauranteRepository.findAll();
        List<RestauranteModel> restaurantesModel = restauranteModelAssembler.toCollectionModel(restaurantes);
        MappingJacksonValue restaurantesWrapper = new MappingJacksonValue(restaurantesModel);

        restaurantesWrapper.setSerializationView(RestauranteView.Resumo.class);

        if("apenas-nome".equals(projecao)){
            restaurantesWrapper.setSerializationView(RestauranteView.ApenasNome.class);
        }else if("completo".equals(projecao)){
            restaurantesWrapper.setSerializationView(null);
        }

        return restaurantesWrapper;
    }

    @GetMapping("/{restauranteId}")
    public RestauranteModel buscar(@PathVariable Long restauranteId) {
        Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(restauranteId);

        return restauranteModelAssembler.toModel(restaurante);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestauranteModel adicionar(@Valid @RequestBody RestauranteInput restauranteInput) {
        try {
            Restaurante restaurante = restauranteInputDisassembler.toDomainModel(restauranteInput);
            return restauranteModelAssembler.toModel(cadastroRestauranteService.salvar(restaurante));
        } catch (CozinhaNaoEncontradaException e) {
            throw new NegocioException(e.getMessage());
        }
    }

    @PutMapping("/{restauranteId}")
    public RestauranteModel atualizar(@PathVariable Long restauranteId,
                                      @Valid @RequestBody RestauranteInput restaurante) {
        try {
            Restaurante restauranteAtual = cadastroRestauranteService.buscarOuFalhar(restauranteId);

            restauranteInputDisassembler.copyToDomainObject(restaurante, restauranteAtual);

            return restauranteModelAssembler.toModel(cadastroRestauranteService.salvar(restauranteAtual));
        } catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage());
        }
    }

    @PutMapping("/{restauranteId}/ativo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativar(@PathVariable Long restauranteId) {
        cadastroRestauranteService.ativar(restauranteId);
    }

    @DeleteMapping("/{restauranteId}/ativo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inativar(@PathVariable Long restauranteId) {
        cadastroRestauranteService.inativar(restauranteId);
    }

    @PutMapping("/ativacoes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativarMultiplos(@RequestBody List<Long> restauranteIds) {
        try {
            cadastroRestauranteService.ativar(restauranteIds);
        } catch (RestauranteNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/ativacoes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inativarMultiplos(@RequestBody List<Long> restauranteIds) {
        try {
            cadastroRestauranteService.inativar(restauranteIds);
        } catch (RestauranteNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }


    @PutMapping("/{restauranteId}/abertura")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void abrir(@PathVariable Long restauranteId) {
        cadastroRestauranteService.abrir(restauranteId);
    }

    @PutMapping("/{restauranteId}/fechamento")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void fechar(@PathVariable Long restauranteId) {
        cadastroRestauranteService.fechar(restauranteId);
    }

}
