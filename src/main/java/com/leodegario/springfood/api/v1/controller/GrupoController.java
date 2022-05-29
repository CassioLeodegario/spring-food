package com.leodegario.springfood.api.v1.controller;

import com.leodegario.springfood.api.v1.assembler.GrupoInputDisassembler;
import com.leodegario.springfood.api.v1.assembler.GrupoModelAssembler;
import com.leodegario.springfood.api.v1.model.GrupoModel;
import com.leodegario.springfood.api.v1.model.input.GrupoInput;
import com.leodegario.springfood.api.v1.openapi.controller.GrupoControllerOpenApi;
import com.leodegario.springfood.core.security.CheckSecurity;
import com.leodegario.springfood.domain.model.Grupo;
import com.leodegario.springfood.domain.service.CadastroGrupoService;
import com.leodegario.springfood.repository.GrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/grupos")
public class GrupoController implements GrupoControllerOpenApi {

    @Autowired
    private GrupoRepository grupoRepository;
    
    @Autowired
    private CadastroGrupoService cadastroGrupo;
    
    @Autowired
    private GrupoModelAssembler grupoModelAssembler;
    
    @Autowired
    private GrupoInputDisassembler grupoInputDisassembler;

    @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<GrupoModel> listar() {
        List<Grupo> todosGrupos = grupoRepository.findAll();

        return grupoModelAssembler.toCollectionModel(todosGrupos);
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
    @GetMapping(path = "/{grupoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GrupoModel buscar(@PathVariable Long grupoId) {
        Grupo grupo = cadastroGrupo.buscarOuFalhar(grupoId);
        
        return grupoModelAssembler.toModel(grupo);
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @PostMapping(MediaType.APPLICATION_JSON_VALUE)
    public GrupoModel adicionar(@RequestBody @Valid GrupoInput grupoInput) {
        Grupo grupo = grupoInputDisassembler.toDomainObject(grupoInput);
        
        grupo = cadastroGrupo.salvar(grupo);
        
        return grupoModelAssembler.toModel(grupo);
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @PutMapping(path = "/{grupoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GrupoModel atualizar(@PathVariable Long grupoId,
            @RequestBody @Valid GrupoInput grupoInput) {
        Grupo grupoAtual = cadastroGrupo.buscarOuFalhar(grupoId);
        
        grupoInputDisassembler.copyToDomainObject(grupoInput, grupoAtual);
        
        grupoAtual = cadastroGrupo.salvar(grupoAtual);
        
        return grupoModelAssembler.toModel(grupoAtual);
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @DeleteMapping("/{grupoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long grupoId) {
        cadastroGrupo.excluir(grupoId);	
    }   
} 