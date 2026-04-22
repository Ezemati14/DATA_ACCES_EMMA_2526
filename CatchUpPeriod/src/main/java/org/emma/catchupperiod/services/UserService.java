package org.emma.catchupperiod.services;

import org.emma.catchupperiod.entities.User;
import org.emma.catchupperiod.entitiesDTO.UserDto;
import org.emma.catchupperiod.repositorys.IUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public User loginUser(User user) {
        //Para porder usar el orElse, en el repository tenemos que marcar el metodo como Optional.
     return userRepository.findBySurnameAndCode(user.getSurname(), user.getCode())
               .orElse(null);
    }

    @Transactional
    public void saveAll(List<User> users) {

        for(User user : users) {
            if(user.getCode() == null || user.getCode().isEmpty()) {
                throw new IllegalArgumentException("No se puede registrar el usuario si el CODIGO viene vacio");
            }
            if(user.getName() == null || user.getName().isEmpty()) {
                throw new IllegalArgumentException("El NOMBRE no puede estar vacio");
            }
            if(user.getSurname() == null || user.getSurname().isEmpty()) {
                throw new IllegalArgumentException("El SURNAME no puede estar vacio");
            }
            if(user.getPhone() == null || user.getPhone().isEmpty()) {
                throw new IllegalArgumentException("Obligatorio que nos deje un numero de TELEFONO");
            }
            if(user.getEmail() == null || user.getEmail().isEmpty()) {
                throw new IllegalArgumentException("Obligatorio que nos deje un CORREO");
            }
        }//cierre del for
        userRepository.saveAll(users);
    }

}
