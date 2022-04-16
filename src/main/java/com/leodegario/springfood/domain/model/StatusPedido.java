package com.leodegario.springfood.domain.model;

public enum StatusPedido {

    CRIADO("Criado"),
    CONFIRMADO("Confirmado"),
    ENTREGUE("Entregue"),
    CANCELADO("Cancelado");

    private final String descricao;

    StatusPedido(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}