package org.emma.catchupperiod.repositorys;

import org.emma.catchupperiod.entities.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategory extends CrudRepository<Category, String> {
}
