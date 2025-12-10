package org.emma2025.fpfa.controller;

import org.emma2025.fpfa.repository.RepositoryEnrollment;
import org.emma2025.fpfa.service.ServiceEnrollment;
import org.hibernate.Session;

import java.util.Scanner;

public class EnrollmentController {

    public final Session session;

    public EnrollmentController(Session session) {
        this.session = session;
    }

    public void eliminaMatricula(Scanner sc) {
        System.out.print("Student ID: ");
        String studentId = sc.nextLine();

        RepositoryEnrollment repo = new RepositoryEnrollment(session);
        repo.cancelCurrentYearEnrollments(studentId);
    }
    public void deleteStudent(Scanner sc) {
        System.out.print("Student ID: ");
        String studentId = sc.nextLine();

        ServiceEnrollment service = new ServiceEnrollment(session);
        service.deleteStudent(studentId);
    }
}
