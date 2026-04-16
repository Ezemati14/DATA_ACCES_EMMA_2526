package org.emma.catchupperiod.repositorys;

import org.emma.catchupperiod.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUsers extends CrudRepository<User, String> {
}
