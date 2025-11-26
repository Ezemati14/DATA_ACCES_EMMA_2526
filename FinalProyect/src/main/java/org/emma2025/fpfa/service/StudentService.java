package org.emma2025.fpfa.service;

import org.emma2025.fpfa.model.entities.Student;
import org.emma2025.fpfa.repository.StudentRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class StudentService {

    private final StudentRepository repo;
    private final Session session;

    public StudentService(Session session, StudentRepository repo) {
        this.session = session;
        this.repo = repo;
    }
    //Creamos una funcion que devuelva la lista , le pasamos por parametro los 2 id
    public List<Object[]> enrollStudent(String studentId, int courseId) {
        //Y solamente retornamos llamando a la funcion que viene de StudenRepository
        return repo.enrollStudent(studentId, courseId);
    }

    public void insertarListaEstudiantes(List<Student> students) {
        Transaction tx = session.beginTransaction();

        try {
            // Verificar duplicados en la BD
            for (Student s : students) {
                if (repo.findById(s.getIdcard()) != null) {
                    throw new RuntimeException("ID duplicado en BD: " + s.getIdcard());
                }
            }

            for (Student s : students) {
                repo.save(s);
            }

            tx.commit();
            System.out.println("Todos los estudiantes fueron guardados en el xml");

        } catch (Exception e) {
            tx.rollback();
            System.out.println("ERROR: " + e.getMessage());
            System.out.println("ERROR EN EL XML, NO SE GUARDO NINGUN ESTUDIANTE.");
        }
    }

}
