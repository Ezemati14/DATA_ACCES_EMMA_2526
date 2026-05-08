package org.emma.catchupperiod.repositorys;

import org.emma.catchupperiod.entities.Book;
import org.emma.catchupperiod.entities.Reservation;
import org.emma.catchupperiod.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReservation extends CrudRepository<Reservation, Integer> {

    List<Reservation> findByBookOrderByDateAsc(Book book);

    boolean existsByBookAndBorrower(Book book, User borrower);
}
