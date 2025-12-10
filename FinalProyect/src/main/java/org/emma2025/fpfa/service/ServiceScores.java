package org.emma2025.fpfa.service;

import org.emma2025.fpfa.repository.RepositoryScores;
import org.hibernate.Session;

import java.util.List;
import java.util.Scanner;

public class ServiceScores {

    private final RepositoryScores repo;
    private final Session session;

    public ServiceScores(Session session) {
        this.session = session;
        this.repo = new RepositoryScores(session);
    }

    public void qualifySubjects(String studentId, int courseId, Scanner scanner) {

        List<Object[]> pendientes = repo.getScoreWithNull(studentId, courseId);

        if(pendientes.isEmpty()){
            System.out.println("No hay asignaturas pendientes de calificar.");
            return;
        }

        for(Object[] filas : pendientes){

            int scoreId = (int) filas[0];
            String subject = (String) filas[1];

            System.out.print("Calificación de " + subject + " (0-10, 99 omitir): ");
            int nota = Integer.parseInt(scanner.nextLine());

            if (nota == 99) {
                System.out.println("Asignatura omitida.");
                continue;
            }

            if (nota < 0 || nota > 10) {
                System.out.println("Valor inválido. Debe ser 0-10.");
                continue;
            }

            session.beginTransaction();
            repo.updateScore(scoreId, nota);
            session.getTransaction().commit();

            System.out.println("Guardado.");
        }
        System.out.println("Calificaciones completadas.");
    }

    public void printStudentResult(int courseId) {
        List<Object[]> results = repo.getAllStudents(courseId);
        if(results.isEmpty()){
            System.out.println("No hay asignaturas pendientes.");
        }

        System.out.println("\nStudent                        Score");
        System.out.println("--------------------------------------------------");

        for(Object[] result: results){
            String name = (String) result[0];
            int score = (int) result[1];
            System.out.println("Name: " + name + " Score: " + score);
        }
    }

    public void printResults(String studentId, int courseId) {
        List<Object[]> results = repo.getAllScores(studentId, courseId);
        if (results.isEmpty()) {
            System.out.println("No se encontraron asignaturas para este estudiante.");
            return;
        }

        System.out.println("\nYear   Subjects                            Score");
        System.out.println("--------------------------------------------------");

        for (Object[] row : results) {
            Integer year = (Integer) row[0];
            String subject = (String) row[1];
            Integer score = (Integer) row[2];

            System.out.printf("%-6d %-35s %s%n",
                    year,
                    subject,
                    (score == null ? "" : score.toString())
            );
        }
    }
}
