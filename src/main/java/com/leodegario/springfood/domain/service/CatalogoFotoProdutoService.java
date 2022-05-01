package com.leodegario.springfood.domain.service;

import com.leodegario.springfood.domain.model.FotoProduto;
import com.leodegario.springfood.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CatalogoFotoProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Transactional
    public FotoProduto salvar(FotoProduto foto){
        return produtoRepository.save(foto);
    }


}
