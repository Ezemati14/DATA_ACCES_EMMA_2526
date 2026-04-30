package org.emma.catchupperiod.controllerThymeleaf;

import org.emma.catchupperiod.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UniversalController {

    @Autowired
    private BookService bookService;

    //GetMapping esta va con la URL
    //http://localhost:8080/books-view
    @GetMapping("/books-view")
    public String books(Model model) {
        //Con model, lo que hacemos es guardar en este atributo lo que nos llega por
        //esta funcion. Como una lista
        //En el books.html cuando hacemos esto ${books} nos referimos al atributo
        model.addAttribute("books", bookService.getAllBooks());
        //Y el retrun no devuelve el atributo, si no de devuelve la vista
        return "books";
    }
}
