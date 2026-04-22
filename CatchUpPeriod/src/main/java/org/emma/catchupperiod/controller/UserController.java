package org.emma.catchupperiod.controller;

import org.emma.catchupperiod.entities.User;
import org.emma.catchupperiod.entities.UserList;
import org.emma.catchupperiod.entitiesDTO.UserDto;
import org.emma.catchupperiod.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getUsers();
    }

    @PostMapping("/inicio-sesion")
    public ResponseEntity<String> login(@RequestBody User user){
        User user1 = userService.loginUser(user);

        if (user1 != null) {
            return ResponseEntity.ok("Bienvenido " + user1.getName());
        }else {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }
    }
    //Pasamos el usuario por el body
    @PutMapping("/update-user")
    public ResponseEntity<String> updateUser(@RequestBody User user){
        try {
            userService.updateUser(user);
            return ResponseEntity.ok("Usuario ACTUALIZADO correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/add-xml", consumes = "application/xml")
    public ResponseEntity<String> addUser(@RequestBody UserList userList){
        try {
            userService.saveAll(userList.getUsers());
            return ResponseEntity.ok("Usuarios creados correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
