package com.leodegario.springfood.api.assembler;

import com.leodegario.springfood.api.controller.CidadeController;
import com.leodegario.springfood.api.controller.EstadoController;
import com.leodegario.springfood.api.model.CidadeModel;
import com.leodegario.springfood.domain.model.Cidade;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CidadeModelAssembler extends RepresentationModelAssemblerSupport<Cidade, CidadeModel> {

    @Autowired
    private ModelMapper modelMapper;

    public CidadeModelAssembler() {
        super(CidadeController.class, CidadeModel.class);
    }

    @Override
    public CidadeModel toModel(Cidade cidade) {
        CidadeModel cidadeModel = createModelWithId(cidade.getId(), cidade);

        modelMapper.map(cidade, cidadeModel);

        cidadeModel.add(linkTo(methodOn(CidadeController.class)
                .listar()).withRel("cidades"));

        cidadeModel.getEstado().add(linkTo(methodOn(EstadoController.class)
                .buscar(cidadeModel.getEstado().getId())).withSelfRel());

        return cidadeModel;
    }

    @Override
    public CollectionModel<CidadeModel> toCollectionModel(Iterable<? extends Cidade> entities) {
        return super.toCollectionModel(entities)
                .add(linkTo(CidadeController.class).withSelfRel());
    }
}