package org.emma.catchupperiod.controllerThymeleaf;

import org.emma.catchupperiod.entities.Book;
import org.emma.catchupperiod.entities.Lending;
import org.emma.catchupperiod.entities.User;
import org.emma.catchupperiod.entitiesDTO.LendingDTO;
import org.emma.catchupperiod.entitiesDTO.UserDetailsDto;
import org.emma.catchupperiod.repositorys.ICategory;
import org.emma.catchupperiod.services.BookService;
import org.emma.catchupperiod.services.LendingService;
import org.emma.catchupperiod.services.ReservationService;
import org.emma.catchupperiod.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping()
public class ControllerLibraryTh {
    @Autowired
    private BookService bookService;
    @Autowired
    private LendingService lendingService;
    @Autowired
    private ICategory categoryRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ReservationService reservationService;

    //-------------- FUNCIONES PARA PRESTAR UN LIBRO A UN USUARIO ----------------

    //URL para mostrar la pagina o cargar "lending", y muestra todas los libros
    @GetMapping("/library/lending")
    public String lendding(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "lending";
    }
    //Funcion que se conecta con la parte de CLIENTE, y devuelve los prestamos activos en formato .txt
    @GetMapping("/pretamos-activos")
    @ResponseBody
    public List<LendingDTO> activeLendings() {
        return lendingService.getActiveLendings();
    }

    //Esto muestra en una pagina html los prestamos activos, los libros que no fueron devueltos
    // con datos de ese usuario que tiene el libro.
    @GetMapping("/library/active-lendings")
    public String activeLendings(Model model) {
        model.addAttribute("lendings", lendingService.getActiveLendings());
        return "activeLendings";
    }

    //Este post sirve para cuando le damos a prestar libro, nos llega el isbn y el usariio por paramtetros,
    // y con eso se hace la reserva, y se muestra un mensaje de exito o error
    //@RequestParam usamos esto porque se envian 2 param de diferentes objetos
    //El nombre del paramatro debe ser igual al name del html th:value="${book.isbn}" y name="userCode"
    @PostMapping("/library/lending")
    public String lending(@RequestParam String isbn,
                          @RequestParam String userCode,
                          RedirectAttributes redirectAttributes) {
        try {
            String mensaje = lendingService.lendBook(isbn, userCode, false);
            redirectAttributes.addFlashAttribute("success", mensaje);
        }catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Error al reservar el libro: " + e.getMessage());
        }
        return "redirect:/library/lending";
    }

    //-------------- FUNCIONES PARA DEVOLVER LIBRO ----------------

    @GetMapping("/library/returning")
    public String returningShow() {
        return "returning";
    }
    @PostMapping("/library/returnBook")
    public String returninBook(@RequestParam String isbn,
                               @RequestParam String userCode,
                               RedirectAttributes redirectAttributes) {
        try {
            //Como devuelve un string, guardamos el mensaje en una variable
            String mensaje = lendingService.returnBook(isbn, userCode);
            //y lo devolves por aca, y lo conectamos con html.
            redirectAttributes.addFlashAttribute("success", mensaje);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al reservar el libro: " + e.getMessage());
        }
        return "redirect:/library/returning";
    }


    //-------------- FUNCIONES PARA AGREGAR UN LIBRO ----------------

    //URL para mostrar la pagina o cargar "lend", y muestra todas las categorias
    @GetMapping("/library/lend")
    public String lend(Model model) {

        model.addAttribute("categories", categoryRepository.findAll());
        return "lend";
    }
    //Usamos @ModelAttribute porque enviamos un objeto completo
    //que en este caso es un libro
    @PostMapping("/library/saveBook")
    public String guardarLibro(@ModelAttribute Book book, RedirectAttributes redirectAttributes) {
        try {
            bookService.save(book);
            redirectAttributes.addFlashAttribute("success", "Libro agregado correctamente");
        }catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al agregar el libro: " + e.getMessage());
        }
        return "redirect:/library/lend";
    }

    //-------------- FUNCIONES PARA AGREGAR UN USUARIO ----------------

    @GetMapping("/library/createUser")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new User());
        return "createUser";
    }

    @PostMapping("/library/save")
    public String saveUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            userService.save(user);
            redirectAttributes.addFlashAttribute("success","Usuario creado correctamente");
            return "redirect:/library/createUser";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/library/createUser";
        }
    }
    //Cargamos tod0s los usuarios para mostralos en la pagina html users
    //y luego con model, los pasamos al html mediante "users"
    @GetMapping("/library/users")
    public String showUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users";
    }
    //Al darle al boton eliminar, nos pasa el userCode por el parametro de aqui,
    //y ese code lo lleva hasta el service, y ahi se encarga de comprobar.
    @PostMapping("/library/deleteUser")
    public String deleteUser(@RequestParam String userCode, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(userCode);
            redirectAttributes.addFlashAttribute("success", "Usuario eliminado correctamente");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/library/users";
    }
    //Para hacer esto el nombre que esta en el parametro tiene que ser igual al name del html
    @GetMapping("/library/userDetails")
    public String userDetails(@RequestParam String userCode, Model model) {
        List<UserDetailsDto> lista = lendingService.getUserDetails(userCode);
        model.addAttribute("details", lista);
        return "userDetails";
    }


    //-------------- FUNCIONES PARA CANCELAR UNA RESERVA GET Y POST ----------------

    //Mostramos la pagina para cancelar. Solamente es esto porq no mostramos ningun dato de nada.
    @GetMapping("/library/cancelReservation")
    public String cancelReservationPage() {
        return "cancelReservation";
    }
    //Cancelar una reserva pasando el isbn y el userCode, y se encarga de eliminar la reserva
    //https://localhost:8080/library/cancelReservation?isbn=0141189207445&userCode=A786543
    @PostMapping("/library/cancelReservation")
    public String cancelarReserva(@RequestParam String isbn, @RequestParam String userCode,
                                                Model model) {
        try {
            String resultado = reservationService.cancelReservation(isbn, userCode);
            model.addAttribute("mensaje", resultado);
        }catch (IllegalArgumentException e) {
            model.addAttribute("mensaje", e.getMessage());
        }
        return "cancelReservation";
    }

    @GetMapping("/library/lendingsByYear")
    public String lendingsByYearPage() {
        return "lendingsByYear";
    }
    //CAMBIO
    //Funcion para buscar los lendings por fecha, pasandole el año solamente
    @PostMapping("/library/lendingsByYear")
    public String getLendingsByYear(@RequestParam Integer from, @RequestParam Integer to
            ,Model model) {
        model.addAttribute("lendings", lendingService.obtenerPorAño(from, to));
        return "lendingsByYear";
    }
}
