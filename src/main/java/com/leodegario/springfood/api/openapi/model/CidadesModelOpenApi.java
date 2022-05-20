package com.leodegario.springfood.api.openapi.model;

import com.leodegario.springfood.api.model.CidadeModel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.hateoas.Links;

import java.util.List;

@ApiModel("CidadesModel")
@Data
public class CidadesModelOpenApi {

    private CidadeEmbeddedModelOpenApi _embedded;
    private Links _links;

    @ApiModel("CidadesEmbeddeModel")
    @Data
    public static class CidadeEmbeddedModelOpenApi{

        private List<CidadeModel> cidades;

    }

}
