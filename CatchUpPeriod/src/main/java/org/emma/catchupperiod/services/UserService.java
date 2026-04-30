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
     return userRepository.findBySurnameAndCode(user.getSurname(), user.getCode()).orElse(null);
    }

    //Lo que nos llega por parametro es el usuario con los datos que vamos actualizar
    public User updateUser(User user) {
        //En esta variable guardamos el usuario que tenemos en la base de datos.
        //Que lo buscamos con el codigo que obtenemos del usuario que nos llega por parametro.
        User usuDB = userRepository.findById(user.getCode())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        //Funcion para acttualizar los datos, pasandole usuario de BD y el usuario que nos llega por parametro.
        actualizarDatos(user, usuDB);
        return userRepository.save(usuDB);
    }
    //Transactional para si alguno tiene alguno error, que no agrege ninguno
    @Transactional
    public void saveAll(List<User> users) {
        validarUsuarios(users);
        userRepository.saveAll(users);
    }



    //--------------- FUNCIONES PRIVADAS -----------------
    //--------------- FUNCIONES PRIVADAS -----------------
    //--------------- FUNCIONES PRIVADAS -----------------
    private void actualizarDatos(User user, User usuDB) {
        //Obtenemos el nombre y si es diferente de null.
        if(user.getName() != null) {
            //Con set guardamos actualizamos el nombre que nos llego del body
            usuDB.setName(user.getName());
        }
        if(user.getSurname() != null) {
            usuDB.setSurname(user.getSurname());
        }
        if(user.getBirthdate() != null) {
            usuDB.setBirthdate(user.getBirthdate());
        }
        if(user.getPhone() != null) {
            usuDB.setPhone(user.getPhone());
        }
        if(user.getEmail() != null) {
            usuDB.setEmail(user.getEmail());
        }
    }
    //Funcion que se le pasa la lista, y con el for recorre la lista, y comienza a validar
    private void validarUsuarios(List<User> users) {
        for(User user : users) {
            if(userRepository.existsById(user.getCode())) {
                throw new IllegalArgumentException("El usuario ya existe" + user.getCode());
            }
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
    }


    /************************FUNCIONES QUE USAN LOS CONTROLADORES DE THYMELEAF****************/
    /************************FUNCIONES QUE USAN LOS CONTROLADORES DE THYMELEAF****************/
    /************************FUNCIONES QUE USAN LOS CONTROLADORES DE THYMELEAF****************/

    public Optional<User> loginUserTh(String surname, String code) {
        return userRepository.findBySurnameAndCode(surname, code);
    }

    //SIN USO POR EL MOMENTO
    public Optional<User> findIdTh(String code) {
        return userRepository.findById(code);
    }

}
