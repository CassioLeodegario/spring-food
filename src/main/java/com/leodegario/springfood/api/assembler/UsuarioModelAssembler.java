package com.leodegario.springfood.api.assembler;

import com.leodegario.springfood.api.controller.CidadeController;
import com.leodegario.springfood.api.controller.EstadoController;
import com.leodegario.springfood.api.controller.UsuarioController;
import com.leodegario.springfood.api.controller.UsuarioGrupoController;
import com.leodegario.springfood.api.model.CidadeModel;
import com.leodegario.springfood.api.model.UsuarioModel;
import com.leodegario.springfood.domain.model.Cidade;
import com.leodegario.springfood.domain.model.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UsuarioModelAssembler  extends RepresentationModelAssemblerSupport<Usuario, UsuarioModel> {

    @Autowired
    private ModelMapper modelMapper;

    public UsuarioModelAssembler() {
        super(UsuarioController.class, UsuarioModel.class);
    }

    public UsuarioModel toModel(Usuario usuario) {
        UsuarioModel usuarioModel = createModelWithId(usuario.getId(), usuario);

        modelMapper.map(usuario, usuarioModel);

        usuarioModel.add(linkTo(methodOn(UsuarioController.class)
                .listar()).withRel("usuarios"));

        usuarioModel.add(linkTo(methodOn(UsuarioGrupoController.class)
                .listar(usuario.getId())).withRel("grupos-usuario"));

        return  usuarioModel;
    }

    @Override
    public CollectionModel<UsuarioModel> toCollectionModel(Iterable<? extends Usuario> entities) {
        return super.toCollectionModel(entities)
                .add(linkTo(UsuarioController.class).withSelfRel());
    }

}