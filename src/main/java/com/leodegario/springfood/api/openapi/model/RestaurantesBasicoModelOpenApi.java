package com.leodegario.springfood.api.openapi.model;

import com.leodegario.springfood.api.model.RestauranteBasicoModel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.hateoas.Links;

import java.util.List;

@ApiModel("RestaurantesBasicoModel")
@Data
public class RestaurantesBasicoModelOpenApi {

    private RestaurantesEmbeddedModelOpenApi _embedded;
    private Links _links;
    
    @ApiModel("RestaurantesEmbeddedModel")
    @Data
    public static class RestaurantesEmbeddedModelOpenApi {
        
        private List<RestauranteBasicoModel> restaurantes;
        
    }
    
}  