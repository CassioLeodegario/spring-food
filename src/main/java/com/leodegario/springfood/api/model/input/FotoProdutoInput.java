package com.leodegario.springfood.api.model.input;

import com.leodegario.springfood.core.validation.FileContentType;
import com.leodegario.springfood.core.validation.FileSize;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class FotoProdutoInput {

    @FileSize(max = "500KB")
    @FileContentType(allowed = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    @NotNull
    private MultipartFile arquivo;

    @NotBlank
    private String descricao;
}
