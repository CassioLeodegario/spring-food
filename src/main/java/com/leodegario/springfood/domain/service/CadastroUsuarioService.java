package com.leodegario.springfood.domain.service;

import com.leodegario.springfood.domain.exception.NegocioException;
import com.leodegario.springfood.domain.exception.UsuarioNaoEncontradoException;
import com.leodegario.springfood.domain.model.Usuario;
import com.leodegario.springfood.repository.UsuarioRepository;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class CadastroUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Transactional
    public Usuario salvar(Usuario usuario) {
        //detach user to avoid entity manager retrieve it from the database without being saved
        usuarioRepository.detach(usuario);

        Optional<Usuario> usuarioExistente = usuarioRepository
                .findByEmail(usuario.getEmail());

        if(usuarioExistente.isPresent()
                && !usuarioExistente.get().equals(usuario)){
            throw new NegocioException(
                    String.format("Já existe um usuário cadastrado com o e-mail %s", usuario.getEmail())
            );
        }

        return usuarioRepository.save(usuario);
    }
    
    @Transactional
    public void alterarSenha(Long usuarioId, String senhaAtual, String novaSenha) {
        Usuario usuario = buscarOuFalhar(usuarioId);
        
        if (usuario.senhaNaoCoincideCom(senhaAtual)) {
            throw new NegocioException("Senha atual informada não coincide com a senha do usuário.");
        }
        
        usuario.setSenha(novaSenha);
    }

    public Usuario buscarOuFalhar(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));
    }            
}                  