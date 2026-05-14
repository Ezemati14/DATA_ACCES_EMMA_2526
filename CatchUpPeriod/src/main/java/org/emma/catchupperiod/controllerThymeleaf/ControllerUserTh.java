package org.emma.catchupperiod.controllerThymeleaf;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.emma.catchupperiod.entities.User;
import org.emma.catchupperiod.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
public class ControllerUserTh {
    @Autowired
    private UserService userService;

    //https://localhost:8080/login
    //Cuando entramos a esta url, se dirige a login.html, y nos muestra esa pagina
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/editUser")
    public String miPerfil(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String surname = auth.getName();

        User user = userService.findBySurname(surname);

        model.addAttribute("user", user);
        return "editUser";
    }
    //Con esta url, nos va a redirigir segun el rol que tengamos
    @GetMapping("/default")
    public String defaultAfterLogin() {
        //Obtenemos el usuario que se logueo, sea library o etc...
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //Guardamos el nombre de la auth , en la variable surname
        String surname = auth.getName();
        //Si es library, nos redirige a la pagina de library/lend
        if (surname.equals("library")) {
            return "redirect:/library/lend";
        }
        //Con cualquier otro, nos lleva al editUser.
        return "redirect:/editUser";
    }

    @PostMapping("/editUser/save")
    public String saveUser(@ModelAttribute User user) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String surname = authentication.getName();

        userService.updateUserTh(user, surname);
        return "redirect:/editUser";
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
