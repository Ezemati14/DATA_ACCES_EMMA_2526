package org.emma.catchupperiod.services;

import org.emma.catchupperiod.entities.Book;
import org.emma.catchupperiod.entities.Category;
import org.emma.catchupperiod.entitiesDTO.BookDto;
import org.emma.catchupperiod.repositorys.IBooks;
import org.emma.catchupperiod.repositorys.ICategory;
import org.emma.catchupperiod.repositorys.ILending;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private IBooks booksInterface;
    @Autowired
    private ICategory categoryRepository;
    @Autowired
    private ILending lendingRepository;

    //Del controller por parametro llega el LIT
    public List<BookDto> getCategoryBooksDto(String category) {
        //Creamos una lista
        List<Book> listBooks;

        if(category != null) {
            //Y esa lista la rellenamos
            //Usamos el signo _ para navegar relaciones es igual que hacer = "book.category.code"
            listBooks = booksInterface.findByCategory_Code(category);
        }else {
            listBooks = (List<Book>) booksInterface.findAll();
        }
        List<BookDto> result = new ArrayList<>();

        for(Book book : listBooks) {
            int activos = lendingRepository.countByBookAndReturningdateIsNull(book);
            int disponibles = book.getCopies() - activos;

            result.add(new BookDto(
                    book.getIsbn(),
                    book.getTitle(),
                    disponibles
            ));
        }
        return result;
    }

    //LLega al Service los datos del libro por el parametros
    //book vale lo que se pasa por el body en postman o bruno
    public void save( Book book){
        validateBook(book);
        Category category = categoryRepository.findById(book.getCategory().getCode())
                .orElseThrow(() -> new IllegalArgumentException("Category no existe"));
        // 2. Comprobamos duplicado por ISBN
        //Con el metodo exists, buscamos por el ISBN
        if(booksInterface.existsById(book.getIsbn())) {
            throw new IllegalArgumentException("El ISBN ya existe");
        }
        book.setCategory(category);
        booksInterface.save(book);
    }

    //LLega al Service los datos del libro por el parametros
    //book vale lo que se pasa por el body en postman o bruno
    public void saveRol( Book book, String rol){
        if(rol == null || !rol.equals("library")) {
            throw new IllegalArgumentException("No tiene permisos para agregar libro");
        }

        validateBook(book);
        Category category = categoryRepository.findById(book.getCategory().getCode())
                .orElseThrow(() -> new IllegalArgumentException("Category no existe"));

        // 2. Comprobamos duplicado por ISBN
        //Con el metodo exists, buscamos por el ISBN
        if(booksInterface.existsById(book.getIsbn())) {
            throw new IllegalArgumentException("El ISBN ya existe");
        }

        book.setCategory(category);
        booksInterface.save(book);
    }

    @Transactional
    public void saveAll(List<Book> books) {
        for (Book book : books) {
            if (book.getIsbn() == null || book.getIsbn().isEmpty()) {
                throw new IllegalArgumentException("ISBN obligatiorio" + book);
            }

            if(book.getTitle() == null || book.getTitle().isEmpty()) {
                throw new IllegalArgumentException("Titulo obligatiorio" + book);
            }

            if (book.getCopies() == null || book.getCopies() <= 0) {
                throw new IllegalArgumentException("Copias incorrectas: " + book);
            }

            if (booksInterface.existsById(book.getIsbn())) {
                throw new IllegalArgumentException("ISBN duplicado: " + book.getIsbn());
            }
        } //cierre del for
        booksInterface.saveAll(books);
    }

    /*-----------FUNCIONES PRIVADAS----------------*/

    private void validateBook(Book book) {
        // 1. Validaciones del ISBN, título y número de copias
        if(book.getIsbn() == null || book.getIsbn().isBlank()) {
            throw new IllegalArgumentException("El ISBN no puede ser nulo o vacío");
        }
        if(book.getTitle() == null || book.getTitle().isBlank()) {
            throw new IllegalArgumentException("El título no puede ser nulo o vacío");
        }
        if(book.getCopies() == null || book.getCopies() < 0) {
            throw new IllegalArgumentException("Numero de copias obligatorio");
        }

        if (book.getCategory() == null || book.getCategory().getCode() == null) {
            throw new IllegalArgumentException("Category obligatoria");
        }
    }
}
