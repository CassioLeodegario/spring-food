package com.leodegario.springfood.infrastructure.repository;

import com.leodegario.springfood.domain.filter.VendaDiariaFilter;
import com.leodegario.springfood.domain.model.dto.VendaDiaria;
import com.leodegario.springfood.domain.service.VendaQueryService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;

public class VendaQueryServiceImpl implements VendaQueryService {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro) {

        return null;
    }

}
