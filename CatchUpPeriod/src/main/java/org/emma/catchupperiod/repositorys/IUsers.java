package org.emma.catchupperiod.repositorys;

import org.emma.catchupperiod.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUsers extends CrudRepository<User, String> {

    Optional<User> findBySurnameAndCode(String surname, String code);
    Optional<User> findBySurname(String surname);

}
