package org.emma.catchupperiod.controllerThymeleaf;

import org.emma.catchupperiod.entities.Book;
import org.emma.catchupperiod.repositorys.ICategory;
import org.emma.catchupperiod.services.BookService;
import org.emma.catchupperiod.services.LendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping()
public class ControllerLibraryTh {
    @Autowired
    private BookService bookService;
    @Autowired
    private LendingService lendingService;
    @Autowired
    private ICategory categoryRepository;

    //-------------- FUNCIONES PARA PRESTAR UN LIBRO A UN USUARIO ----------------
    //URL para mostrar la pagina o cargar "lending", y muestra todas los libros
    @GetMapping("/library/lending")
    public String lendding(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "lending";
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
}
