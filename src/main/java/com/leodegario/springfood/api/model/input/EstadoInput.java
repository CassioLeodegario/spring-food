package com.leodegario.springfood.api.model.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class EstadoInput {

    @NotBlank
    private String nome;
    
}