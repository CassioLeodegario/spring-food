package com.leodegario.springfood.api.v1.controller;

import com.leodegario.springfood.api.v1.SpringFoodLinks;
import com.leodegario.springfood.api.v1.assembler.PermissaoModelAssembler;
import com.leodegario.springfood.api.v1.model.PermissaoModel;
import com.leodegario.springfood.api.v1.openapi.controller.GrupoPermissaoControllerOpenApi;
import com.leodegario.springfood.core.security.CheckSecurity;
import com.leodegario.springfood.core.security.SpringFoodSecurity;
import com.leodegario.springfood.domain.model.Grupo;
import com.leodegario.springfood.domain.service.CadastroGrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/grupos/{grupoId}/permissoes")
public class GrupoPermissaoController implements GrupoPermissaoControllerOpenApi {

	@Autowired
	private CadastroGrupoService cadastroGrupo;
	
	@Autowired
	private PermissaoModelAssembler permissaoModelAssembler;

	@Autowired
	private SpringFoodLinks springFoodLinks;

	@Autowired
	private SpringFoodSecurity springFoodSecurity;

	@CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
	@Override
	@GetMapping
	public CollectionModel<PermissaoModel> listar(@PathVariable Long grupoId) {
		Grupo grupo = cadastroGrupo.buscarOuFalhar(grupoId);

		CollectionModel<PermissaoModel> permissoesModel
				= permissaoModelAssembler.toCollectionModel(grupo.getPermissoes())
				.removeLinks();

		permissoesModel.add(springFoodLinks.linkToGrupoPermissoes(grupoId));

		if (springFoodSecurity.podeEditarUsuariosGruposPermissoes()) {
			permissoesModel.add(springFoodLinks.linkToGrupoPermissaoAssociacao(grupoId, "associar"));

			permissoesModel.getContent().forEach(permissaoModel -> {
				permissaoModel.add(springFoodLinks.linkToGrupoPermissaoDesassociacao(
						grupoId, permissaoModel.getId(), "desassociar"));
			});
		}

		return permissoesModel;
	}

	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	@Override
	@DeleteMapping("/{permissaoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> desassociar(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
		cadastroGrupo.desassociarPermissao(grupoId, permissaoId);

		return ResponseEntity.noContent().build();
	}

	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	@Override
	@PutMapping("/{permissaoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> associar(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
		cadastroGrupo.associarPermissao(grupoId, permissaoId);

		return ResponseEntity.noContent().build();
	}

}