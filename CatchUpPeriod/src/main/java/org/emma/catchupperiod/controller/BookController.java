package org.emma.catchupperiod.controller;


import org.emma.catchupperiod.entities.Book;
import org.emma.catchupperiod.entities.BookList;
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

    //Devuelve una lista de libros pero sin DTO
    @GetMapping
    public List<BookDto> getAllBooks() {
        return bookService.findAll();
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
}
