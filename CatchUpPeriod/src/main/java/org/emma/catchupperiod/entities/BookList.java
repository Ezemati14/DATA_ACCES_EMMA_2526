package org.emma.catchupperiod.entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class BookList {

    //Indica que no se envuelva la lista de libros en una etiqueta adicional
    @JacksonXmlElementWrapper(useWrapping = false)
    //Nombre de la etiqueta que envuelve a cada libro, en este caso "book"
    @JacksonXmlProperty(localName = "book")
    private List<Book> books;

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
