package org.emma2026.springdemo.service;

import org.emma2026.springdemo.modelo.dao.IEmployeeEntityDAO;
import org.emma2026.springdemo.modelo.dto.EmployeeDTO;
import org.emma2026.springdemo.modelo.entidades.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceEmployee {
    @Autowired
    private IEmployeeEntityDAO employeeEntityDAO;

    public List<EmployeeDTO> buscarTodosEmpleados() {
        List<Employee> empleados = employeeEntityDAO.findAll();

        return empleados.stream()
                .map(employee -> {
                    EmployeeDTO dto = new EmployeeDTO();
                    //Con get leo el dato, y si existe
                    //lo pongo o guardo en el dto con set
                    dto.setIdDto(employee.getId());
                    dto.setNameDto(employee.getEname());
                    dto.setJobDto(employee.getJob());
                    //devolvemos el employeeDTO completo, con todos los datos
                    //Con get leo el dato, con set lo guardo en el dto
                    return dto;
                })
                .toList();
    }

    public Optional<EmployeeDTO> actualizarEmpleado(int id, EmployeeDTO empleadoDTO) {
        //Va a la base de datos a buscar el empleado por id
        Optional<Employee> employeeOpt = employeeEntityDAO.findById(id);
        //Si viene vacio, se devuelve un optional vacio
        if(employeeOpt.isEmpty()) {
            return Optional.empty();
        }
        //Con get obtenemos el empleado que viene de la base de datos
        // y lo guardamos en una variable employee
        Employee empploye = employeeOpt.get();
        empploye.setEname(empleadoDTO.getNameDto());
        empploye.setJob(empleadoDTO.getJobDto());
        //Y aca el employe ya actualizado lo guardamos en la base de datos
        Employee empleadoActualizado = employeeEntityDAO.save(empploye);

        EmployeeDTO resultadoDTO = new EmployeeDTO();
        resultadoDTO.setIdDto(empleadoActualizado.getId());
        resultadoDTO.setNameDto(empleadoActualizado.getEname());
        resultadoDTO.setJobDto(empleadoActualizado.getJob());

        return Optional.of(resultadoDTO);
    }

    public boolean eliminarEmpleado(int id) {
        if(!employeeEntityDAO.existsById(id)) {
            return false;
        }
        employeeEntityDAO.deleteById(id);
        return true;
    }
}
