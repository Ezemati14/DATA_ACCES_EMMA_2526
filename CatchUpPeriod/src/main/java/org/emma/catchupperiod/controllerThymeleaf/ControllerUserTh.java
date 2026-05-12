package org.emma.catchupperiod.controllerThymeleaf;

import jakarta.servlet.http.HttpSession;
import org.emma.catchupperiod.entities.User;
import org.emma.catchupperiod.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/library")
public class ControllerUserTh {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/lend")
    public String lend() {
        return "lend";
    }

    /*@PostMapping("/login")
    public String login(@RequestParam String surname,
                        @RequestParam String code,
                        Model model, HttpSession session) {
        //Caja que puede tener o no al usuario
        Optional<User> user = userService.loginUserTh(surname, code);
        if(user.isPresent()) {
            //Utilizamos sesion para poder recordar datos entre nuestras paginas
            //Aca obtenemos los datos del user.get()
            session.setAttribute("user", user.get());
            //Vamos el endpoint, y hace toda la logica que tenga ese endpoint
            return "redirect:/user-login";
        } else {
            model.addAttribute("error", "Usuario o código incorrecto");
         return "login";
        }
    }

    @GetMapping("/user-login")
    public String userLogin( Model model, HttpSession session) {
        //Guardamos los datos del "user" en la variable user
        User user = (User) session.getAttribute("user");
        if(user == null) {
            return "login";
        }
        //Pasamos los datos a las vista, a nuestro userlogin.html
        model.addAttribute("user", user);
        //enviamos los datos user al html, para poder hacer esto th:text="${user.code}"
        return "userlogin";
    }*/
}
