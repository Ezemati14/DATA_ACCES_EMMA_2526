package org.emma2025.fpfa.service;

import org.emma2025.fpfa.model.entities.Enrollment;
import org.emma2025.fpfa.repository.RepositoryEnrollment;
import org.hibernate.Session;

public class ServiceEnrollment {

    private final RepositoryEnrollment repo;

    public ServiceEnrollment(Session session) {
        this.repo = new RepositoryEnrollment(session);
    }

}
