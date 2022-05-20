package com.leodegario.springfood.api.openapi.model;

import com.leodegario.springfood.api.model.ProdutoModel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.hateoas.Links;

import java.util.List;

@ApiModel("ProdutosModel")
@Data
public class ProdutosModelOpenApi {

    private ProdutosEmbeddedModelOpenApi _embedded;
    private Links _links;
    
    @ApiModel("ProdutosEmbeddedModel")
    @Data
    public static class ProdutosEmbeddedModelOpenApi {
        
        private List<ProdutoModel> produtos;
        
    }    
}