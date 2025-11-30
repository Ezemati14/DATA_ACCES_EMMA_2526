package org.emma2025.fpfa.controller;

import org.emma2025.fpfa.model.entities.Student;
import org.emma2025.fpfa.repository.StudentRepository;
import org.emma2025.fpfa.service.ServiceScores;
import org.emma2025.fpfa.service.StudentService;
import org.hibernate.Session;

import java.util.List;
import java.util.Scanner;

public class StudentController {

    private StudentService service;
    private final Session session;

    public StudentController(Session session) {
        this.session = session;
        this.service = new StudentService(session, new StudentRepository(session));
    }

    public void addFromXML(String filename) {
        System.out.println("Leyendo archivo XML...");

        List<Student> students = LeerXML.readStudentsXML(filename);
        System.out.println("Estudiantes encontrados: " + students.size());

        service.insertarListaEstudiantes(students);
    }

    public void enrollStudent(Scanner scanner) {
        System.out.print("Introduce student ID: ");
        String studentId = scanner.nextLine().trim();
        System.out.print("Introduce course ID: ");
        int courseId = Integer.parseInt(scanner.nextLine().trim());

        try {
            List<Object[]> subjects = service.enrollStudent(studentId, courseId);

            System.out.println("Asignaturas matriculadas:");
            for (Object[] sub : subjects) {
                System.out.println("ID: " + sub[0] + " - Nombre: " + sub[1]);
            }

        } catch (Exception e) {
            System.out.println("Error al matricular estudiante: " + e.getMessage());
        }
    }

    public void qualify(Scanner sc) {
        //Una vez que llega, le vuelve a preguntar que
        //Id de estudiante consultar y el curso
        //Ejemplo studentId = 12332003 y courseId = 2
        System.out.print("Student ID: ");
        String studentId = sc.nextLine();
        System.out.print("Course ID: ");
        int courseId = Integer.parseInt(sc.nextLine());
        //Le pasamos por parametro session, para que se siga usando la misma sesion
        //Utilizando la misma de hibernate que ya esta abierta
        //Con esto nos ahorramos de crear una nueva session
        ServiceScores serviceScores = new ServiceScores(session);
        serviceScores.qualifySubjects(studentId, courseId, sc);
    }

    public void printResults(Scanner sc) {
        System.out.print("Student ID: ");
        String studentId = sc.nextLine();

        System.out.print("Course ID: ");
        int courseId = Integer.parseInt(sc.nextLine());

        ServiceScores serviceScores = new ServiceScores(session);
        serviceScores.printResults(studentId, courseId);
    }
}
