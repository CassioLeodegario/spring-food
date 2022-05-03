package com.leodegario.springfood.api.controller;

import com.leodegario.springfood.api.assembler.FotoProdutoModelAssembler;
import com.leodegario.springfood.api.model.FotoProdutoModel;
import com.leodegario.springfood.api.model.input.FotoProdutoInput;
import com.leodegario.springfood.domain.exception.EntidadeNaoEncontradaException;
import com.leodegario.springfood.domain.model.FotoProduto;
import com.leodegario.springfood.domain.service.CadastroProdutoService;
import com.leodegario.springfood.domain.service.CatalogoFotoProdutoService;
import com.leodegario.springfood.domain.service.FotoStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos/{produtoId}/foto")
public class RestauranteProdutoFotoController {

    @Autowired
    CatalogoFotoProdutoService catalogoFotoProduto;

    @Autowired
    CadastroProdutoService cadastroProdutoService;

    @Autowired
    FotoProdutoModelAssembler fotoProdutoModelAssembler;

    @Autowired
    FotoStorageService fotoStorageService;

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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public FotoProdutoModel obterFotoProduto(
            @PathVariable Long restauranteId,
            @PathVariable Long produtoId) {
        return fotoProdutoModelAssembler
                .toModel(catalogoFotoProduto.buscarOuFalhar(restauranteId, produtoId));
    }

    @GetMapping
    public ResponseEntity<InputStreamResource> servirFoto(
            @PathVariable Long restauranteId,
            @PathVariable Long produtoId,
            @RequestHeader(name= "Accept") String acceptHeader
    ) {
        try{
            FotoProduto fotoProduto = catalogoFotoProduto.buscarOuFalhar(restauranteId, produtoId);

            MediaType mediaTypeFoto = MediaType.parseMediaType(fotoProduto.getContentType());
            List<MediaType> mediaTypeAceitas = MediaType.parseMediaTypes(acceptHeader);

            verificarCompatibilidade(mediaTypeFoto, mediaTypeAceitas);

            InputStream inputStream = fotoStorageService.recuperar(fotoProduto.getNomeArquivo());

            return ResponseEntity.ok()
                    .contentType(mediaTypeFoto)
                    .body(new InputStreamResource(inputStream));
        }catch (EntidadeNaoEncontradaException | HttpMediaTypeNotAcceptableException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long restauranteId,
                        @PathVariable Long produtoId) {
        catalogoFotoProduto.excluir(restauranteId, produtoId);
    }

    private void verificarCompatibilidade(MediaType mediaTypeFoto,
                                          List<MediaType> mediaTypeAceitas) throws HttpMediaTypeNotAcceptableException {
        boolean compativel = mediaTypeAceitas.stream()
                .anyMatch(mediaTypeAceita -> mediaTypeAceita.isCompatibleWith(mediaTypeFoto));
        if(!compativel){
            throw new HttpMediaTypeNotAcceptableException(mediaTypeAceitas);
        }
    }

}
