package org.emma.catchupperiod.repositorys;

import org.emma.catchupperiod.entities.Book;
import org.emma.catchupperiod.entities.Lending;
import org.emma.catchupperiod.entities.Reservation;
import org.emma.catchupperiod.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ILending extends CrudRepository<Lending, Integer> {

    int countByBorrowerAndReturningdateIsNull(User borrower);

    int countByBookAndReturningdateIsNull(Book book);

    boolean existsByBookAndBorrowerAndReturningdateIsNull(Book book, User borrower);

    boolean existsByBorrowerAndReturningdateIsNull(User borrower);

    Optional<Lending> findByBookAndBorrowerAndReturningdateIsNull(Book book, User borrower);

    List<Lending> findByBorrower(User user);

    List<Lending> findByReturningdateIsNull();

    Boolean existsByBorrower(User user);

    List<Lending> findByBorrower_Code(String code);

    List<Lending> findByLendingdateBetween(LocalDate desde, LocalDate hasta);
}
