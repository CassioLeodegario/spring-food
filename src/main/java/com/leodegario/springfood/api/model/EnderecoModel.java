package com.leodegario.springfood.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoModel {
    private String cep;
    private String logradouro;
    private String complemento;
    private String numero;
    private String bairro;
    private CidadeResumoModel cidade;

}
