package com.leodegario.springfood.api.openapi.model;

import com.leodegario.springfood.api.model.CidadeModel;
import com.leodegario.springfood.api.model.CozinhaModel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.hateoas.Links;

import java.util.List;

@ApiModel("CozinhasModel")
public class CozinhasModelOpenApi {

    private CozinhasEmbeddedModelOpenApi _embedded;
    private Links _links;
    private PageModelOpenApi page;

    @ApiModel("CozinhasEmbeddeModel")
    @Data
    public static class CozinhasEmbeddedModelOpenApi{

        private List<CidadeModel> cidades;

    }
}
