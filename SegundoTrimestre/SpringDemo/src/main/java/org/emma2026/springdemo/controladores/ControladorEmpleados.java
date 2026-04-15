package org.emma2026.springdemo.controladores;

import org.emma2026.springdemo.modelo.dao.IEmployeeEntityDAO;
import org.emma2026.springdemo.modelo.dto.EmployeeDTO;
import org.emma2026.springdemo.modelo.entidades.Employee;
import org.emma2026.springdemo.service.ServiceEmployee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//Indicamos que esta clase es una API REST
//Cada metodo devuelve datos JSON
@RestController
//Aca definimos la ruta base de este controlador
// GET /empleados GET /empleados/{id} POST /empleados
@RequestMapping("/empleados")
public class ControladorEmpleados {

    //Creamos automaticamente el DAO
    //Este controlador depende del DAO, no sabe acceder a la base de datos
    @Autowired
    IEmployeeEntityDAO empleadoDao;
    @Autowired
    ServiceEmployee serviceEmployee;

    @GetMapping
    //Devuelve una lista con todos los empleados
    //Ejemplo: /empleados
    public List<EmployeeDTO> buscarEmpleados() {
        return serviceEmployee.buscarTodosEmpleados();
    }

    @GetMapping(params = "id")
    //Devuelve un empleado segun su id
    //Ejemplo: empleados?id=7499
    public ResponseEntity<Employee> buscarEmpleado(@RequestParam(name = "id") int id) {
        //El optional es para no tener nulos nunca
        Optional<Employee> empleado = empleadoDao.findById(id);

        if(empleado.isPresent()) {
            //Aca metemos un ok, en el body va el dato del empleado
            return ResponseEntity.ok().body(empleado.get()); //200 OK + empleado
        }else {
            return ResponseEntity.notFound().build(); //404 Not Found
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> actualizarEmpleado(@RequestBody EmployeeDTO dto, @PathVariable int id){

        Optional<EmployeeDTO> resultado = serviceEmployee.actualizarEmpleado(id, dto);

        if(resultado.isPresent()) {
            return ResponseEntity.ok().body(resultado.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEmpleado(@PathVariable(value = "id") int id) {
        boolean eliminado = serviceEmployee.eliminarEmpleado(id);

        if(eliminado) {
            return ResponseEntity.ok().body("Borrado"); //204 No Content
        }else {
            return ResponseEntity.notFound().build(); //404 Not Found
        }
    }
}