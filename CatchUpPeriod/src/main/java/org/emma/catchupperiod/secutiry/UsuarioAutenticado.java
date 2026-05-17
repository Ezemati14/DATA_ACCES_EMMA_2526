package org.emma.catchupperiod.secutiry;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UsuarioAutenticado extends User {

    private String code;

    public UsuarioAutenticado(
            String surname,
            String password,
            String code,
            Collection<? extends GrantedAuthority> permisos) {

        super(surname, password, permisos);

        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
