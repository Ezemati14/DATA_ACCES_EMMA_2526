package org.emma.catchupperiod.repositorys;

import org.emma.catchupperiod.entities.Book;
import org.emma.catchupperiod.entities.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBooks extends CrudRepository<Book, String> {
    List<Book> findByCategory_Code(String categoryCode);

    List<Book> findByCategory(Category category);
}
