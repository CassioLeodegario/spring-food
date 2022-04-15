package com.leodegario.springfood.api.assembler;

import com.leodegario.springfood.api.model.input.RestauranteInput;
import com.leodegario.springfood.domain.model.Cidade;
import com.leodegario.springfood.domain.model.Cozinha;
import com.leodegario.springfood.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestauranteInputDisassembler {


    @Autowired
    private ModelMapper modelMapper;

    public Restaurante toDomainModel(RestauranteInput restauranteInput){
        return modelMapper.map(restauranteInput, Restaurante.class);
    }

    public void copyToDomainObject(RestauranteInput restauranteInput, Restaurante restaurante){
        restaurante.setCozinha(new Cozinha());
        if(restaurante.getEndereco() != null ){
            restaurante.getEndereco().setCidade(new Cidade());
        }
        modelMapper.map(restauranteInput, restaurante);
    }

}
