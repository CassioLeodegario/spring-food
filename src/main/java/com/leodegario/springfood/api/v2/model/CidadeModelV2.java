package com.leodegario.springfood.api.v2.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "cidades")
@Setter
@Getter
public class CidadeModelV2 extends RepresentationModel<CidadeModelV2> {

    @ApiModelProperty(example = "1")
    private Long idCidade;

    @ApiModelProperty(value = "Nome da cidade", example = "Uberlândia")
    private String nomeCidade;

    @ApiModelProperty(example = "1")
    private Long idEstado;

    @ApiModelProperty(value = "Nome do estado", example = "Mato Grosso do Sul")
    private String nomeEstado;


}        