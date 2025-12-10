package org.emma2025.fpfa.repository;

import org.hibernate.Session;

public class RepositoryCourses {

    private final Session session;

    public RepositoryCourses(Session session) {
        this.session = session;
    }


}
