package org.emma.catchupperiod.controllerThymeleaf;

import org.emma.catchupperiod.entities.Book;
import org.emma.catchupperiod.repositorys.ICategory;
import org.emma.catchupperiod.secutiry.UsuarioAutenticado;
import org.emma.catchupperiod.services.BookService;
import org.emma.catchupperiod.services.LendingService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.emma.catchupperiod.entities.User;
import org.emma.catchupperiod.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping()
public class ControllerUserTh {
    @Autowired
    private UserService userService;
    @Autowired
    private BookService bookService;
    @Autowired
    private ICategory categoryRepository;
    @Autowired
    private LendingService lendingService;

    //https://localhost:8080/login
    //Cuando entramos a esta url, se dirige a login.html, y nos muestra esa pagina
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }
    //url para cargar la pagina html de books
    @GetMapping("/books")
    public String showBooks(Model model) {
        //Con esto cargamos en la misma pagina, TODAS las categorias
        //la guardamos en categories, y llenamos la ventana del html con esos datos
        model.addAttribute("categories", categoryRepository.findAll());

        return "books";
    }
    //Cuando le dimos al boton de seleccionar en el html, category se llena con el valor seleccionado
    // category = LIT. category vale LIT, y con eso hace la busqueda
    @GetMapping("/books/search")
    public String buscarLibros(@RequestParam String category, Model model) {
        try{
            //category vale LIT, entonces va al service, y busca todos los libros que tengan esa categoria
            List<Book> books = bookService.buscarPorCategoria(category);
            //Una vez encontrado la lista de libros con esa categoria, con model
            //lo asignamos a la variable books, y lo pasamos al html
            model.addAttribute("books", books);
            model.addAttribute("categories", categoryRepository.findAll());

        }catch(Exception e){
            model.addAttribute("error", e.getMessage());
        }
        return "books";
    }

    //Mostramos la pantalla https://localhost:8080/editUser
    //Y buscamos al usuario que esta logueado, y luego con model
    //se lo pasamos a la vista editUser.html
    @GetMapping("/editUser")
    public String showEditUser(Model model) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        UsuarioAutenticado usuario =
                (UsuarioAutenticado) auth.getPrincipal();

        String code = usuario.getCode();

        User user = userService.findUserById(code);

        model.addAttribute("user", user);
        return "editUser";
    }
    //Cuando estamos en edituser.html, y le damos al click el formulario busca esta url
    //y con el ModelAttribute nos llega el usuario que viene del formulario
    //Luego obtenemos el code del usuario
    //El @ModelAttribute lo usamos para cuando pasamos un objeto completo
    @PostMapping("/editUser/save")
    public String miPerfil(@ModelAttribute User userForm, Model model
                        , RedirectAttributes redirectAttributes) {

        try {
            //El user que enviamos, entra a esta funcion y se validan los datos
            //Para que no lleguen vacios, ni nada
            userService.validarUsuario(userForm);
            //Con esto obtenemos el usuario que esta logueado
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            //Una vez que tenemos al usuario, lo que hacemos es tranformalo en una clase
            //Spirng lo gurada como algo generico, y nosotros lo guardamos como clase
            UsuarioAutenticado usuarioAutenticado = (UsuarioAutenticado) auth.getPrincipal();
            //Y por ultimo lo que hacemos es sacar de ese usuario que ya es clase, su code
            String code = usuarioAutenticado.getCode();
            //Aca una vez que tenemos el usuario, y ya buscamos el code, lo obtenemos con setCode()
            userForm.setCode(code);
            redirectAttributes.addFlashAttribute("success","Usuario actualizado correctamente");
            //Como ya tenemos el usaurio, lo enviamos al updateService, para que actualice los datos
            userService.updateUser(userForm);
        }catch (IllegalArgumentException e ) {
            model.addAttribute("error", e.getMessage());
            return "editUser";
        }
        return "redirect:/editUser";
    }

    @GetMapping("/mylendings")
    public String showMyLendings(Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UsuarioAutenticado usuarioAutenticado = (UsuarioAutenticado) auth.getPrincipal();
            String code = usuarioAutenticado.getCode();

            model.addAttribute("lendings", lendingService.getAllLendings(code));
            model.addAttribute("reservations", lendingService.getAllReservations(code));
        }catch (IllegalArgumentException e ) {
            model.addAttribute("error", e.getMessage());
        }
        return "mylendings";
    }


    //Endpoint para reservar un libro, con el isbn del libro que queremos reservar
    @PostMapping("/books/reserve")
    public String reservarLibro(@RequestParam String isbn, RedirectAttributes redirectAttributes
                                ,Model model) {
        try {
            //Conseguimos al usuario mediante la autenticacion
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UsuarioAutenticado usuarioAutenticado = (UsuarioAutenticado) auth.getPrincipal();
            //Obtenemos el code del usuario
            String code = usuarioAutenticado.getCode();
            model.addAttribute("success","Libro reservado correctamente");
            //Y le pasamos el isbn del libro y el code del usuario autenticado
            lendingService.reserveBook(isbn, code);
        }catch (IllegalArgumentException e ) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/books";
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
