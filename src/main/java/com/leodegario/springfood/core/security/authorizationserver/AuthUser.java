package com.leodegario.springfood.core.security.authorizationserver;

import com.leodegario.springfood.domain.model.Usuario;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class AuthUser extends User {

    private String fullName;
    private Long userId;

    public AuthUser(Usuario usuario, Collection<? extends GrantedAuthority> authorities){
        super(usuario.getEmail(), usuario.getSenha(),authorities);

        this.fullName = usuario.getNome();
        this.userId = usuario.getId();
    }

}
