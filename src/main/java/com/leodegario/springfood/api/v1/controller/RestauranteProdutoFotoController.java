package com.leodegario.springfood.api.v1.controller;

import com.leodegario.springfood.api.v1.assembler.FotoProdutoModelAssembler;
import com.leodegario.springfood.api.v1.model.FotoProdutoModel;
import com.leodegario.springfood.api.v1.model.input.FotoProdutoInput;
import com.leodegario.springfood.api.v1.openapi.controller.RestauranteProdutoFotoControllerOpenApi;
import com.leodegario.springfood.core.security.CheckSecurity;
import com.leodegario.springfood.domain.exception.EntidadeNaoEncontradaException;
import com.leodegario.springfood.domain.model.FotoProduto;
import com.leodegario.springfood.domain.service.CadastroProdutoService;
import com.leodegario.springfood.domain.service.CatalogoFotoProdutoService;
import com.leodegario.springfood.domain.service.FotoStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/restaurantes/{restauranteId}/produtos/{produtoId}/foto")
public class RestauranteProdutoFotoController implements RestauranteProdutoFotoControllerOpenApi {

    @Autowired
    CatalogoFotoProdutoService catalogoFotoProduto;

    @Autowired
    CadastroProdutoService cadastroProdutoService;

    @Autowired
    FotoProdutoModelAssembler fotoProdutoModelAssembler;

    @Autowired
    FotoStorageService fotoStorageService;

    @CheckSecurity.Restaurantes.PodeEditar
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FotoProdutoModel atualizarFoto(
            @PathVariable Long restauranteId,
            @PathVariable Long produtoId,
            @Valid FotoProdutoInput fotoProdutoInput,
            @RequestPart(required = true) MultipartFile arquivo) throws IOException {
        var produto = cadastroProdutoService.buscarOuFalhar(restauranteId, produtoId);

        FotoProduto foto = new FotoProduto();
        foto.setProduto(produto);
        foto.setDescricao(fotoProdutoInput.getDescricao());
        foto.setContentType(arquivo.getContentType());
        foto.setTamanho(arquivo.getSize());
        foto.setNomeArquivo(arquivo.getOriginalFilename());

        return fotoProdutoModelAssembler.toModel(catalogoFotoProduto.salvar(foto, arquivo.getInputStream()));
    }

    @CheckSecurity.Restaurantes.PodeConsultar
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public FotoProdutoModel buscar(
            @PathVariable Long restauranteId,
            @PathVariable Long produtoId) {
        return fotoProdutoModelAssembler
                .toModel(catalogoFotoProduto.buscarOuFalhar(restauranteId, produtoId));
    }

    @CheckSecurity.Restaurantes.PodeConsultar
    @GetMapping(produces = MediaType.ALL_VALUE)
    public ResponseEntity<InputStreamResource> servir(
            @PathVariable Long restauranteId,
            @PathVariable Long produtoId,
            @RequestHeader(name= "Accept") String acceptHeader
    ) {
        try{
            FotoProduto fotoProduto = catalogoFotoProduto.buscarOuFalhar(restauranteId, produtoId);

            MediaType mediaTypeFoto = MediaType.parseMediaType(fotoProduto.getContentType());
            List<MediaType> mediaTypeAceitas = MediaType.parseMediaTypes(acceptHeader);

            verificarCompatibilidade(mediaTypeFoto, mediaTypeAceitas);

            FotoStorageService.FotoRecuperada fotoRecuperada = fotoStorageService
                    .recuperar(fotoProduto.getNomeArquivo());

            if(fotoRecuperada.temUrl()){
                return ResponseEntity
                .status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, fotoRecuperada.getUrl())
                .build();
            }

            return ResponseEntity.ok()
                    .contentType(mediaTypeFoto)
                    .body(new InputStreamResource(fotoRecuperada.getInputStream()));
        }catch (EntidadeNaoEncontradaException | HttpMediaTypeNotAcceptableException e){
            return ResponseEntity.notFound().build();
        }
    }

    @CheckSecurity.Restaurantes.PodeEditar
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
