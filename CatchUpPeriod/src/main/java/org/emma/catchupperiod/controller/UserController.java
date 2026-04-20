package org.emma.catchupperiod.controller;

import org.emma.catchupperiod.entities.User;
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

}
