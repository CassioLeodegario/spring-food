package com.leodegario.springfood;

import com.leodegario.springfood.domain.exception.CozinhaNaoEncontradaException;
import com.leodegario.springfood.domain.exception.EntidadeEmUsoException;
import com.leodegario.springfood.domain.model.Cozinha;
import com.leodegario.springfood.domain.service.CadastroCozinhaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CadastroCozinhaIntegration2IT {

    @Autowired
    private CadastroCozinhaService cadastroCozinha;

    @Test
    public void deveAtribuirId_quandoCadastrarCozinhaComDadosCorretos(){
        Cozinha cozinha = new Cozinha();
        cozinha.setNome("Chinesa");

        cozinha = cadastroCozinha.salvar(cozinha);

        assertThat(cozinha).isNotNull();
        assertThat(cozinha.getId()).isNotNull();
    }

    @Test
    public void deveFalhar_QuandoCadastrarCozinhaSemNome() {
        Cozinha cozinha = new Cozinha();
        cozinha.setNome(null);
        assertThrows(ConstraintViolationException.class,
                () -> cadastroCozinha.salvar(cozinha));
    }

    @Test
    public void deveFalhar_QuandoExcluirCozinhaEmUso() {
        assertThrows(EntidadeEmUsoException.class,
                () -> cadastroCozinha.excluir(1L));
    }

    @Test
    public void deveFalhar_QuandoExcluirCozinhaInexistente() {
        assertThrows(CozinhaNaoEncontradaException.class,
                () -> cadastroCozinha.excluir(100L));
    }

}
