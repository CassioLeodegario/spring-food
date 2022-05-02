package com.leodegario.springfood.api.controller;

import com.leodegario.springfood.api.assembler.FotoProdutoModelAssembler;
import com.leodegario.springfood.api.model.FotoProdutoModel;
import com.leodegario.springfood.api.model.input.FotoProdutoInput;
import com.leodegario.springfood.domain.model.FotoProduto;
import com.leodegario.springfood.domain.service.CadastroProdutoService;
import com.leodegario.springfood.domain.service.CatalogoFotoProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos/{produtoId}/foto")
public class RestauranteProdutoFotoController {

    @Autowired
    CatalogoFotoProdutoService catalogoFotoProduto;

    @Autowired
    CadastroProdutoService cadastroProdutoService;

    @Autowired
    FotoProdutoModelAssembler fotoProdutoModelAssembler;

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FotoProdutoModel atualizarFoto(
            @PathVariable Long restauranteId,
            @PathVariable Long produtoId,
            @Valid FotoProdutoInput fotoProdutoInput) throws IOException {
        var produto = cadastroProdutoService.buscarOuFalhar(restauranteId, produtoId);

        MultipartFile arquivo = fotoProdutoInput.getArquivo();

        FotoProduto foto = new FotoProduto();
        foto.setProduto(produto);
        foto.setDescricao(fotoProdutoInput.getDescricao());
        foto.setContentType(arquivo.getContentType());
        foto.setTamanho(arquivo.getSize());
        foto.setNomeArquivo(arquivo.getOriginalFilename());

        return fotoProdutoModelAssembler.toModel(catalogoFotoProduto.salvar(foto, arquivo.getInputStream()));
    }

    @GetMapping
    public FotoProdutoModel obterFotoProduto(
            @PathVariable Long restauranteId,
            @PathVariable Long produtoId){
        return fotoProdutoModelAssembler
                .toModel(catalogoFotoProduto.buscarOuFalhar(restauranteId, produtoId));
    }

}
