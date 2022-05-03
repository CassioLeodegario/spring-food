package com.leodegario.springfood.infrastructure.service.storage;

import com.leodegario.springfood.core.storage.StorageProperties;
import com.leodegario.springfood.domain.service.FotoStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

//@Service
public class LocalFotoStorageService implements FotoStorageService {

    @Autowired
    StorageProperties storageProperties;


    @Override
    public void armazenar(NovaFoto novaFoto) {
        try {
            Path arquivoPath = getArquivoPath(novaFoto.getNomeArquivo());

            FileCopyUtils.copy(novaFoto.getInputStream(), Files.newOutputStream(arquivoPath));
        } catch (IOException e) {
            throw new StorageException("Não foi possível armazenar arquivo", e);
        }
    }

    @Override
    public void remover(String nomeArquivo) {
        try {
            Path arquivoPath = getArquivoPath(nomeArquivo);

            Files.deleteIfExists(arquivoPath);
        } catch (IOException e) {
            throw new StorageException("Não foi possível excluir arquivo", e);
        }

    }

    @Override
    public InputStream recuperar(String nomeArquivo) {
        try{
        Path arquivoPath = getArquivoPath(nomeArquivo);

            return Files.newInputStream(arquivoPath);
        } catch (IOException e) {
            throw new StorageException("Não foi possível recuperar o arquivo", e);
        }
    }

    private Path getArquivoPath(String nomeArquivo) {
        return storageProperties.getLocal().getDiretorioFotos()
                .resolve(nomeArquivo);
    }
}
