package org.emma2026.springdemo.modelo.dao;

import org.emma2026.springdemo.modelo.entidades.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IEmployeeEntityDAO extends CrudRepository<Employee, Integer> {
    //Metodo para obtener todos los empleados
    List<Employee> findAll();
    //Metodo para buscar un empleado por su id
    Optional<Employee> findById(Integer id);

    void deleteById(Integer id);
    boolean existsById(Integer id);
}
