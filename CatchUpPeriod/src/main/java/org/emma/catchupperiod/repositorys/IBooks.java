package org.emma.catchupperiod.repositorys;

import org.emma.catchupperiod.entities.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBooks extends CrudRepository<Book, String> {
}
