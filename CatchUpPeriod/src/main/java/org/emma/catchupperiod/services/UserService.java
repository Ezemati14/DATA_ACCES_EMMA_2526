package org.emma.catchupperiod.services;

import org.emma.catchupperiod.entities.User;
import org.emma.catchupperiod.entitiesDTO.UserDto;
import org.emma.catchupperiod.repositorys.ILending;
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
    @Autowired
    private ILending lendingRepository;

    public User findUserById(String id){
        return userRepository.findById(String.valueOf(id))
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

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

    public void save(User user) {
        validarUsuario(user);
        userRepository.save(user);
    }

    public void deleteUser(String userCode) {
        User user = userRepository.findById(userCode)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        Boolean tienePrestamos = lendingRepository.existsByBorrowerAndReturningdateIsNull(user);
        Boolean tieneHistorialPrestamo = lendingRepository.existsByBorrower(user);

        if(tienePrestamos) {
            throw new IllegalArgumentException("No se puede eliminar el usuario, tiene prestamos activos");
        }
        if(tieneHistorialPrestamo) {
            throw new IllegalArgumentException("No se puede borrar el usuario porque tiene historial de préstamos");
        }
        userRepository.delete(user);
    }

    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    public void validarUsuario(User user) {
        if(user.getCode() == null || user.getCode().isEmpty()) {
            //Si el nombre es null o esta vacio, lanzamos una excepcion con un mensaje
            //con esto hacemos que vaya al catch directamente, y ejecuta el mensaje de error
            throw new IllegalArgumentException("El DNI no puede estar vacio");
        }
        if (!user.getCode().matches("^[A-Z][0-9]{6}$")) {
            throw new IllegalArgumentException(
                    "Formato erroneo. Es letra Mayuscula + 6 numeros = (Ej: A123456)"
            );
        }
        if(user.getName() == null || user.getName().isEmpty()) {
            //Si el nombre es null o esta vacio, lanzamos una excepcion con un mensaje
            //con esto hacemos que vaya al catch directamente, y ejecuta el mensaje de error
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
        if(user.getBirthdate() == null) {
            throw new IllegalArgumentException("Seleecionar una fecha de cumpleaños");
        }
    }

    //--------------- FUNCIONES PRIVADAS -----------------
    //--------------- FUNCIONES PRIVADAS -----------------
    //--------------- FUNCIONES PRIVADAS -----------------
    private void actualizarDatos(User user, User usuDB) {
            //Obtenemos el nombre y si es diferente de null.
            if(user.getName() != null && !user.getName().isBlank()) {
                //Con set guardamos actualizamos el nombre que nos llego del body
                usuDB.setName(user.getName());
            }
            if(user.getSurname() != null) {
                usuDB.setSurname(user.getSurname());
            }
            if(user.getBirthdate() != null) {
                usuDB.setBirthdate(user.getBirthdate());
            }
            if(user.getPhone() != null && !user.getPhone().isBlank()) {
                usuDB.setPhone(user.getPhone());
            }
            if(user.getEmail() != null && !user.getEmail().isBlank()) {
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
}
