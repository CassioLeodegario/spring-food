package com.leodegario.springfood.api.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
@ApiModel("Problema")
public class Problem {

    @ApiModelProperty(example = "400")
    private Integer status;

    private String type;

    @ApiModelProperty(example = "Dados inválidos")
    private String title;

    @ApiModelProperty(example = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.")
    private String detail;

    @ApiModelProperty(example = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.")
    private String userMessage;

    @ApiModelProperty(example = "2022-05-09T07:33:02.70844Z")
    private OffsetDateTime timestamp;

    @ApiModelProperty("Objetos ou campos que geraram o erro")
    private List<Object> objects;

    @ApiModel("ObjetoProblema")
    @Getter
    @Builder
    public static class Object {


        @ApiModelProperty(example = "Preço")
        private String name;

        @ApiModelProperty(example = "Preço Obrigatório")
        private String userMessage;
    }

}
