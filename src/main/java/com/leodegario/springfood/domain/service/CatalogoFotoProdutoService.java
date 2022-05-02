package com.leodegario.springfood.domain.service;

import com.leodegario.springfood.domain.model.FotoProduto;
import com.leodegario.springfood.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

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

        Optional<FotoProduto> fotoExistente =
                produtoRepository.findFotoById(restauranteId, produtoId);

        fotoExistente.ifPresent(fotoProduto -> produtoRepository.delete(fotoProduto));

        foto.setNomeArquivo(nomeNovoArquivo);
        foto = produtoRepository.save(foto);
        produtoRepository.flush();

        FotoStorageService.NovaFoto novaFoto = FotoStorageService.NovaFoto.builder()
                .inputStream(dadosArquivo)
                .nomeArquivo(foto.getNomeArquivo())
                .build();

        fotoStorageService.armazenar(novaFoto);

        return foto;
    }


}
