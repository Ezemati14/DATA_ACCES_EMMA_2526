package org.emma.catchupperiod.secutiry;

import org.emma.catchupperiod.repositorys.IUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IUsers userRepository;

    @Override
    //Pasamos por parametro el surname, porque es el campo que utilizamos para logearnos
    //Este metodo no lo llamamos nosotros, lo llama Spring Security cada vez que alguien intenta logearse
    public UserDetails loadUserByUsername(String surname) {
    //Si nos llega library, entramos y creamos un usuario con ese nombre, contraseña library, y rol LIBRARY
        if(surname.equals("library")) {
            return User.withUsername("library")
                    .password("{noop}library")
                    .roles("LIBRARY")
                    .build();
            /*  POR SI EN ALGUN MOMENTO BUSCO POR ID
            return new UsuarioAutenticado(
            "library",
            "{noop}library",
            "LIBRARY",
            List.of(new SimpleGrantedAuthority("ROLE_LIBRARY"))
            );
            */
         }

        //Si no es library, buscamos en la base de datos un usuario con ese surname
        //Buscamos por el apellido , que nos llega por parametro
        org.emma.catchupperiod.entities.User user = userRepository.findBySurname(surname)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return new UsuarioAutenticado(
                user.getSurname(),
                "{noop}" + user.getCode(),
                user.getCode(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

}

