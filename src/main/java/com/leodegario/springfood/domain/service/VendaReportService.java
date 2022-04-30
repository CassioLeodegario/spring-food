package com.leodegario.springfood.domain.service;

import com.leodegario.springfood.domain.filter.VendaDiariaFilter;

public interface VendaReportService {
    byte[] emitirVendasDiarias(VendaDiariaFilter filtro, String timeOffset);
}
