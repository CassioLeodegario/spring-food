package com.leodegario.springfood.api.v1.controller;

import com.leodegario.springfood.api.v1.assembler.UsuarioInputDisassembler;
import com.leodegario.springfood.api.v1.assembler.UsuarioModelAssembler;
import com.leodegario.springfood.api.v1.model.UsuarioModel;
import com.leodegario.springfood.api.v1.model.input.SenhaInput;
import com.leodegario.springfood.api.v1.model.input.UsuarioComSenhaInput;
import com.leodegario.springfood.api.v1.model.input.UsuarioInput;
import com.leodegario.springfood.api.v1.openapi.controller.UsuarioControllerOpenApi;
import com.leodegario.springfood.core.security.CheckSecurity;
import com.leodegario.springfood.domain.model.Usuario;
import com.leodegario.springfood.domain.service.CadastroUsuarioService;
import com.leodegario.springfood.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/usuarios")
public class UsuarioController implements UsuarioControllerOpenApi {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private CadastroUsuarioService cadastroUsuario;
    
    @Autowired
    private UsuarioModelAssembler usuarioModelAssembler;
    
    @Autowired
    private UsuarioInputDisassembler usuarioInputDisassembler;

    @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
    @GetMapping
    public CollectionModel<UsuarioModel> listar() {
        List<Usuario> todasUsuarios = usuarioRepository.findAll();
        
        return usuarioModelAssembler.toCollectionModel(todasUsuarios);
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
    @GetMapping("/{usuarioId}")
    public UsuarioModel buscar(@PathVariable Long usuarioId) {
        Usuario usuario = cadastroUsuario.buscarOuFalhar(usuarioId);
        
        return usuarioModelAssembler.toModel(usuario);
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioModel adicionar(@RequestBody @Valid UsuarioComSenhaInput usuarioInput) {
        Usuario usuario = usuarioInputDisassembler.toDomainObject(usuarioInput);
        usuario = cadastroUsuario.salvar(usuario);
        
        return usuarioModelAssembler.toModel(usuario);
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeAlterarUsuario
    @PutMapping("/{usuarioId}")
    public UsuarioModel atualizar(@PathVariable Long usuarioId,
            @RequestBody @Valid UsuarioInput usuarioInput) {
        Usuario usuarioAtual = cadastroUsuario.buscarOuFalhar(usuarioId);
        usuarioInputDisassembler.copyToDomainObject(usuarioInput, usuarioAtual);
        usuarioAtual = cadastroUsuario.salvar(usuarioAtual);
        
        return usuarioModelAssembler.toModel(usuarioAtual);
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeAlterarUsuario
    @PutMapping("/{usuarioId}/senha")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void alterarSenha(@PathVariable Long usuarioId, @RequestBody @Valid SenhaInput senha) {
        cadastroUsuario.alterarSenha(usuarioId, senha.getSenhaAtual(), senha.getNovaSenha());
    }            
}              