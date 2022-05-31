package com.leodegario.springfood;

import com.leodegario.springfood.core.io.Base64ProtocolResolver;
import com.leodegario.springfood.infrastructure.repository.CustomJpaRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.TimeZone;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomJpaRepositoryImpl.class)
public class AlgafoodApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        var app = new SpringApplication(AlgafoodApplication.class);
        app.addListeners(new Base64ProtocolResolver());
        app.run(args);

//        SpringApplication.run(AlgafoodApplication.class, args);
    }

}
