package com.leodegario.springfood.infrastructure.repository;

import com.leodegario.springfood.domain.model.FotoProduto;
import com.leodegario.springfood.domain.service.ProdutoRepositoryQueries;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ProdutoRepositoryImpl implements ProdutoRepositoryQueries {

    @PersistenceContext
    private EntityManager manager;

    @Transactional
    @Override
    public FotoProduto save(FotoProduto fotoProduto) {
        return  manager.merge(fotoProduto);
    }

    @Transactional
    @Override
    public void delete(FotoProduto foto) {
        manager.remove(foto);
    }
}
