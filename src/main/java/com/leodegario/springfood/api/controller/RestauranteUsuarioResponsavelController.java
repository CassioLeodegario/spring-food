package com.leodegario.springfood.api.controller;

import com.leodegario.springfood.api.assembler.UsuarioModelAssembler;
import com.leodegario.springfood.api.model.UsuarioModel;
import com.leodegario.springfood.api.openapi.controller.RestauranteUsuarioResponsavelControllerOpenApi;
import com.leodegario.springfood.domain.model.Restaurante;
import com.leodegario.springfood.domain.service.CadastroRestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/restaurantes/{restauranteId}/responsaveis")
public class RestauranteUsuarioResponsavelController implements RestauranteUsuarioResponsavelControllerOpenApi {

    @Autowired
    private CadastroRestauranteService cadastroRestaurante;
    
    @Autowired
    private UsuarioModelAssembler usuarioModelAssembler;
    
    @GetMapping
    public List<UsuarioModel> listar(@PathVariable Long restauranteId) {
        Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);
        
        return usuarioModelAssembler.toCollectionModel(restaurante.getResponsaveis());
    }
    
    @DeleteMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desassociar(@PathVariable Long restauranteId, @PathVariable Long usuarioId) {
        cadastroRestaurante.desassociarResponsavel(restauranteId, usuarioId);
    }
    
    @PutMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void associar(@PathVariable Long restauranteId, @PathVariable Long usuarioId) {
        cadastroRestaurante.associarResponsavel(restauranteId, usuarioId);
    }
}