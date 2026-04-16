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


    /*//Metodo que devuelve lista de libros, pero usando DTO
    @GetMapping("/dto")
    public List<BookDto> getAllBooksDto() {
        return bookService.findAllDto();
    }*/

    //Devuelve una lista de libros pero sin DTO
    @GetMapping
    public List<BookDto> getAllBooks() {
        return bookService.findAll();
    }

    //Endpoint para agergar libros
    //Con el request body, pasamos el libro que queremos agregar
    @PostMapping
    //Por el book nos llega el body que agregamos por postman o bruno
    public ResponseEntity<String> addBook(@RequestBody Book book){
        bookService.save(book);
        return ResponseEntity.ok("Libro creado correctamente");
    }
}
