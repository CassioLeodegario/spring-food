package com.leodegario.springfood.domain.service;

import com.leodegario.springfood.domain.exception.PermissaoNaoEncontradaException;
import com.leodegario.springfood.domain.model.Permissao;
import com.leodegario.springfood.repository.PermissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CadastroPermissaoService {

    @Autowired
    private PermissaoRepository permissaoRepository;
    
    public Permissao buscarOuFalhar(Long permissaoId) {
        return permissaoRepository.findById(permissaoId)
            .orElseThrow(() -> new PermissaoNaoEncontradaException(permissaoId));
    }
}