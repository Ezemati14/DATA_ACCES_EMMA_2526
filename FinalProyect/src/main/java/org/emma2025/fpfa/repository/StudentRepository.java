package org.emma2025.fpfa.repository;

import org.emma2025.fpfa.model.entities.Student;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import java.util.List;

public class StudentRepository {

    private Session session;

    public StudentRepository(Session session) {
        this.session = session;
    }

    public void save(Student student) {
        session.persist(student);
    }

    public Student findById(String idCard) {
        return session.get(Student.class, idCard);
    }

    //Creamos una funcion que devuelve una lista de objeto, pasandole por parametro el id de studen y curso
    public List<Object[]> enrollStudent(String studentId, int courseId) {
        //Cremos la query llamando a la funcion creda en pdadmin
        String sql = "SELECT * FROM _da_vtschool_2526.enroll_student_emma_2526(:studentId, :courseId)";
        //Con session creamos la query pasandole por parametro el strin sql
        NativeQuery<Object[]> query = session.createNativeQuery(sql);
        //Luego con esa query obtenemos los parametros
        query.setParameter("studentId", studentId);
        query.setParameter("courseId", courseId);
        //Y retornamos el resultado de esa lista
        return query.getResultList();

    }
}
