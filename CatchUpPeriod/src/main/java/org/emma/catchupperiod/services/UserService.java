package org.emma.catchupperiod.services;

import org.emma.catchupperiod.entities.User;
import org.emma.catchupperiod.entitiesDTO.UserDto;
import org.emma.catchupperiod.repositorys.IUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService{

    @Autowired
    private IUsers userRepository;

    public List<UserDto> getUsers() {
        //Buscamos los usuario en la base de datos, y lo guardamos en una lista
        List<User> usuarios = (List<User>) userRepository.findAll();
        //Creamos una lista nueva, que guarda usuario DTO
        List<UserDto> usuariosDto = new ArrayList<>();
        //Recorremos la lista con un for
        for(User usuario : usuarios){
            //Con add vamos guardando en la lista lo que obtenemos de la lista User
            //Cada usuario lo guardo en el constructor del UserDTO
            if(usuario.getFined() != null) {
                usuariosDto.add( new UserDto(
                        usuario.getName(),
                        usuario.getSurname(),
                        usuario.getEmail()
                ));
            }
        }
        return usuariosDto;
    }
}
