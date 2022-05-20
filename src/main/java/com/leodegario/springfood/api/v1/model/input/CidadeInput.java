package com.leodegario.springfood.api.v1.model.input;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class CidadeInput {

    @ApiModelProperty(example = "Uberlândia")
    @NotBlank
    private String nome;
    
    @Valid
    @NotNull
    private EstadoIdInput estado;
    
}