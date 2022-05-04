package com.leodegario.springfood.api.assembler;

import com.leodegario.springfood.api.model.input.CidadeInput;
import com.leodegario.springfood.domain.model.Cidade;
import com.leodegario.springfood.domain.model.Estado;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CidadeInputDisassembler {

    @Autowired
    private ModelMapper modelMapper;
    
    public Cidade toDomainObject(CidadeInput cidadeInput) {
        return modelMapper.map(cidadeInput, Cidade.class);
    }
    
    public void copyToDomainObject(CidadeInput cidadeInput, Cidade cidade) {
        cidade.setEstado(new Estado());
        
        modelMapper.map(cidadeInput, cidade);
    }
    
}