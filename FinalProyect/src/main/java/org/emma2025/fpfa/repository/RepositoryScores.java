package org.emma2025.fpfa.repository;

import org.hibernate.Session;

import java.util.List;

public class RepositoryScores {

    private final Session session;

    public RepositoryScores(Session session) {
        this.session = session;
    }

    //Con esta funcion hacemos una peticion para obtener
    //las asignaturas sin calificar, de un estudiante y un curso especifico
    public List<Object[]> getScoreWithNull(String studentId, int courseId) {
        /*En la tabla scores guardamos la calificaciones
        *     JOIN =enrollments = a que matricula pertenece / subject = a que asignatura / score = la nota
        *           Luego con el join conectamos la tabla scores con enrollments y tambien scores con subjects
        *    WHERE = Y luego con el where nos traemos asignaturas de ese estudiante
        *            el curso especificado sea 1 o 2
        *            y que no tenga nota especificada
        * */
        List resultList = session.createNativeQuery(
                        "SELECT scores.code AS score_id, subjects.name " +
                                "FROM _da_vtschool_2526.scores scores " +
                                "JOIN _da_vtschool_2526.enrollments enrollments ON enrollments.code = scores.enrollment_id " +
                                "JOIN _da_vtschool_2526.subjects subjects ON subjects.code = scores.subject_id " +
                                "WHERE enrollments.student = :studentId " +
                                "AND enrollments.course = :courseId " +
                                "AND scores.score IS NULL"
                )
                //Estas son las variables dentro de la query
                //Los valores que llegan por los parametros de la funcion
                //Se pasan por aca, y van para la query, y la transforma en la consulta con esos datos
                .setParameter("studentId", studentId)
                .setParameter("courseId", courseId)
                .getResultList();
        return resultList;
    }
    //Aca guardamos la nota introducida por el usuario
    public void updateScore(int scoreId, Integer scoreValue) {
        session.createNativeQuery(
                //Aca de la tabla scores vamos a modificar la columna score, con el
                //parametro :score que se pasa
                //Y con el scoreId, localizamos el que queremos cambiar
                        "UPDATE _da_vtschool_2526.scores SET score = :score WHERE code = :scoreId"
                )
                .setParameter("score", scoreValue)
                .setParameter("scoreId", scoreId)
                .executeUpdate();
    }
    //En esta query nos devuelve todas las asignaturas de un estudiante y curso especifico
    public List<Object[]> getAllScores(String studentId, int courseId) {
        List resultList = session.createNativeQuery(
                //Year = enrollment ( 1 o 2 año) / NAME nombre asignaruta / score nota de asignatura
                        "SELECT enrollments.year, subjects.name, scores.score " +
                                //Hacemos una join con las tablas enrollments y subjects
                                "FROM _da_vtschool_2526.scores scores " +
                                "JOIN _da_vtschool_2526.enrollments enrollments ON enrollments.code = scores.enrollment_id " +
                                "JOIN _da_vtschool_2526.subjects subjects ON subjects.code = scores.subject_id " +
                                //Y en la tabla enrollments buscamos por el id del estudiante
                                "WHERE enrollments.student = :studentId " +
                                //Y en la misma tabla, por el curso (1 o 2)
                                "AND enrollments.course = :courseId " +
                                //Ordenamos por año, y luego nombre de asignarutas
                                "ORDER BY enrollments.year, subjects.name"
                )
                .setParameter("studentId", studentId)
                .setParameter("courseId", courseId)
                .getResultList();
        return resultList;
    }

}
