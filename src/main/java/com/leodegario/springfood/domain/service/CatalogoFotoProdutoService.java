package com.leodegario.springfood.domain.service;

import com.leodegario.springfood.domain.exception.FotoProdutoNaoEncontradaException;
import com.leodegario.springfood.domain.model.FotoProduto;
import com.leodegario.springfood.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Optional;

@Service
public class CatalogoFotoProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private FotoStorageService fotoStorageService;

    @Transactional
    public FotoProduto salvar(FotoProduto foto, InputStream dadosArquivo){
        Long restauranteId = foto.getRestauranteId();
        Long produtoId = foto.getProduto().getId();
        String nomeNovoArquivo = fotoStorageService.gerarNomeArquivo(foto.getNomeArquivo());
        String nomeArquivoExistente = null;

        Optional<FotoProduto> fotoExistente =
                produtoRepository.findFotoById(restauranteId, produtoId);

        if(fotoExistente.isPresent()){
            nomeArquivoExistente = fotoExistente.get().getNomeArquivo();
            produtoRepository.delete(fotoExistente.get());
        }

        foto.setNomeArquivo(nomeNovoArquivo);
        foto = produtoRepository.save(foto);
        produtoRepository.flush();

        FotoStorageService.NovaFoto novaFoto = FotoStorageService.NovaFoto.builder()
                .inputStream(dadosArquivo)
                .contentType(foto.getContentType())
                .nomeArquivo(foto.getNomeArquivo())
                .build();

        fotoStorageService.substituir(nomeArquivoExistente, novaFoto);

        return foto;
    }

    @Transactional
    public void excluir(Long restauranteId, Long produtoId) {
        FotoProduto foto = buscarOuFalhar(restauranteId, produtoId);

        produtoRepository.delete(foto);
        produtoRepository.flush();

        fotoStorageService.remover(foto.getNomeArquivo());
    }

    public FotoProduto buscarOuFalhar(Long restauranteId, Long produtoId) {
        return produtoRepository.findFotoById(restauranteId, produtoId)
                .orElseThrow(() -> new FotoProdutoNaoEncontradaException(restauranteId, produtoId));
    }


}
