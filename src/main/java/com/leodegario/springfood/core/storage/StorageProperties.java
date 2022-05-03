package com.leodegario.springfood.core.storage;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Getter
@Setter
@Component
@ConfigurationProperties("algafood.storage")
public class StorageProperties {

    private S3 s3 = new S3();
    private Local local = new Local();

    @Getter
    @Setter
    public class Local{
        private Path diretorioFotos;
    }

    public class S3{
        private String idChaveAcesso;
        private String chaveAcessoSecreta;
        private String bucket;
        private String regiao;
        private String diretorioFotos;
    }
}
