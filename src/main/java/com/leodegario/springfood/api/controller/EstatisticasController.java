package com.leodegario.springfood.api.controller;

import com.leodegario.springfood.domain.filter.VendaDiariaFilter;
import com.leodegario.springfood.domain.model.dto.VendaDiaria;
import com.leodegario.springfood.domain.service.VendaQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path="/estatisticas")
public class EstatisticasController {

    @Autowired
    private VendaQueryService vendaQueryService;

    @GetMapping("/vendas-diarias")
    public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro){
        return vendaQueryService.consultarVendasDiarias(filtro);
    }

}
