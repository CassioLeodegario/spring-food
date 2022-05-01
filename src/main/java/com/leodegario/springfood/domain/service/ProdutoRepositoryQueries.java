package com.leodegario.springfood.domain.service;

import com.leodegario.springfood.domain.model.FotoProduto;

public interface ProdutoRepositoryQueries {

    FotoProduto save(FotoProduto fotoProduto);
    void delete(FotoProduto foto);
}
