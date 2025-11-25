package org.emma2025.fpfa.repository;

import org.emma2025.fpfa.model.entities.Student;
import org.hibernate.Session;

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
}
