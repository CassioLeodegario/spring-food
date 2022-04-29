package com.leodegario.springfood.domain.service;

import com.leodegario.springfood.domain.filter.VendaDiariaFilter;
import com.leodegario.springfood.domain.model.dto.VendaDiaria;

import java.util.List;

public interface VendaQueryService {

    List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro, String timeOffset);

}
