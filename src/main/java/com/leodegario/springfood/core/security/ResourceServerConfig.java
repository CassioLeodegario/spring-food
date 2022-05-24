package com.leodegario.springfood.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()

                .and()
                .cors().and()
                .oauth2ResourceServer()
                .jwt();
    }

    @Bean
    public JwtDecoder jwtDecoder(){
        var secretKey = new SecretKeySpec("asd4as65d4as654d65as4d65as4d65as4d6a4sd564asd65as4".getBytes(), "HmacSHA256");

        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }


}
