package org.emma2025.fpfa.repository;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class RepositoryEnrollment {

    private final Session session;

    public RepositoryEnrollment(Session session) {
        this.session = session;
    }


}