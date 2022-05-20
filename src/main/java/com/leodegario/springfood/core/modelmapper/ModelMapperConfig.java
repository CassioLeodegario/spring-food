package com.leodegario.springfood.core.modelmapper;

import com.leodegario.springfood.api.v1.model.EnderecoModel;
import com.leodegario.springfood.api.v1.model.input.ItemPedidoInput;
import com.leodegario.springfood.api.v2.model.input.CidadeInputV2;
import com.leodegario.springfood.domain.model.Cidade;
import com.leodegario.springfood.domain.model.Endereco;
import com.leodegario.springfood.domain.model.ItemPedido;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        var modelMapper = new ModelMapper();

        modelMapper.createTypeMap(CidadeInputV2.class, Cidade.class)
                .addMappings(mapper -> mapper.skip(Cidade::setId));

        var enderecoToEnderecoModelTypeMap = modelMapper.createTypeMap(
                Endereco.class, EnderecoModel.class);

        modelMapper.createTypeMap(ItemPedidoInput.class, ItemPedido.class)
                .addMappings(mapper -> mapper.skip(ItemPedido::setId));

        enderecoToEnderecoModelTypeMap.<String>addMapping(
                enderecoSrc -> enderecoSrc.getCidade().getEstado().getNome(),
                (enderecoModelDest, value) -> enderecoModelDest.getCidade().setEstado(value));

        return new ModelMapper();
    }
}
