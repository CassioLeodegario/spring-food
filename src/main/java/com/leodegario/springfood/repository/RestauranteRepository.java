package com.leodegario.springfood.repository;

import com.leodegario.springfood.domain.model.Restaurante;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface RestauranteRepository extends
        CustomJpaRepository<Restaurante, Long>,
        JpaSpecificationExecutor<Restaurante>,
        RestauranteRepositoryQueries {

    @Query("from Restaurante r join r.cozinha")
    List<Restaurante> findAll();

    List<Restaurante> findByTaxaFreteBetween(BigDecimal taxaInicial, BigDecimal taxaFinal);

    List<Restaurante> consultarPorNome(String nome, @Param("cozinhaId") Long cozinha);


}
