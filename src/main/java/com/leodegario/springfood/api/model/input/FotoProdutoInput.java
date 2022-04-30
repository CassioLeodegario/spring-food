package com.leodegario.springfood.api.model.input;

import com.leodegario.springfood.core.validation.FileSize;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class FotoProdutoInput {

    @FileSize(max = "500KB")
    @NotNull
    private MultipartFile arquivo;

    @NotBlank
    private String descricao;
}
