package org.emma2025.fpfa.repository;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class RepositoryEnrollment {

    private final Session session;

    public RepositoryEnrollment(Session session) {
        this.session = session;
    }

    public void cancelCurrentYearEnrollments(String studentId) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            //Verificar si tiene matrícula este año
            String checkSql = "SELECT COUNT(*) FROM _da_vtschool_2526.enrollments WHERE student = ? AND year = 2024";
            Long count = (Long) session.createNativeQuery(checkSql)
                    .setParameter(1, studentId)
                    .uniqueResult();

            if (count == 0) {
                System.out.println("El estudiante " + studentId + " no está matriculado en 2024.");
                tx.commit();
                return;
            }

            //Poner notas a NULL
            String updateSql = "UPDATE _da_vtschool_2526.scores SET score = NULL " +
                    "WHERE enrollment_id IN (SELECT code FROM _da_vtschool_2526.enrollments " +
                    "WHERE student = ? AND year = 2024)";

            session.createNativeQuery(updateSql)
                    .setParameter(1, studentId)
                    .executeUpdate();

            //Eliminar scores
            String deleteScoresSql = "DELETE FROM _da_vtschool_2526.scores " +
                    "WHERE enrollment_id IN (SELECT code FROM _da_vtschool_2526.enrollments " +
                    "WHERE student = ? AND year = 2024)";

            session.createNativeQuery(deleteScoresSql)
                    .setParameter(1, studentId)
                    .executeUpdate();

            //Eliminar matrícula
            String deleteEnrollmentSql = "DELETE FROM _da_vtschool_2526.enrollments WHERE student = ? AND year = 2024";

            session.createNativeQuery(deleteEnrollmentSql)
                    .setParameter(1, studentId)
                    .executeUpdate();

            tx.commit();
            System.out.println("Matrícula del estudiante " + studentId + " cancelada");

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void deleteStudents(String studentId) {
        Transaction tx = session.beginTransaction();
        //Eliminamos las notas
        session.createNativeQuery("DELETE FROM _da_vtschool_2526.scores WHERE enrollment_id IN " +
                        "(SELECT code FROM _da_vtschool_2526.enrollments WHERE student = ?)")
                .setParameter(1, studentId)
                .executeUpdate();
        //Eliminamos las matrículas
        session.createNativeQuery("DELETE FROM _da_vtschool_2526.enrollments WHERE student = ?")
                .setParameter(1, studentId)
                .executeUpdate();
        //Eliminamos el estudiante
        session.createNativeQuery("DELETE FROM _da_vtschool_2526.students WHERE idcard = ?")
                .setParameter(1, studentId)
                .executeUpdate();
        tx.commit();
    }

}