package org.emma.catchupperiod.controller;


import org.emma.catchupperiod.entities.Book;
import org.emma.catchupperiod.entitiesDTO.BookDto;
import org.emma.catchupperiod.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    //http://localhost:8080/books/list-category?category=LIT
    @GetMapping("/list-category")
    //Nos llega por parametro un "LIT" que es el code de Category
    public ResponseEntity<List<BookDto>> getCategoryBooks(@RequestParam(required = false) String category) {
        //Ese LIT, lo pasa por parametro al service
       List<BookDto> books = bookService.getCategoryBooksDto(category);
       //List<Book> books1 = bookService.getCategoryBooks(category);
       return ResponseEntity.ok(books);
    }

    //Endpoint para agergar UN SOLO LIBRO
    //Con el request body, pasamos el libro que queremos agregar
    @PostMapping
    //Por el book nos llega el body que agregamos por postman o bruno
    public ResponseEntity<String> addBook(@RequestBody Book book){
        bookService.save(book);
        return ResponseEntity.ok("Libro creado correctamente");
    }

    //endpoint para agregar una LISTA de libros
    @PostMapping(value = "/add-books")
    public ResponseEntity<String> addBooks(@RequestBody List<Book> books) {
        try {
            bookService.saveAll(books);
            return ResponseEntity.ok("Libro creado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    /*----------------------- ROL BIBLIOTECARIO ----------------------*/
    /*----------------------- ROL BIBLIOTECARIO ----------------------*/
    /*----------------------- ROL BIBLIOTECARIO ----------------------*/

    //http://localhost:8080/books/add-book/rol?rol=library
    @PostMapping("/add-book/rol")
    //Pasamos el rol y por el body, el libro.
    public ResponseEntity<String> addBookRol(@RequestParam(required = false) String rol,
                                             @RequestBody Book book){
        try {
            bookService.saveRol(book, rol);
            return ResponseEntity.ok("Libro creado correctamente");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
