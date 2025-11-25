package org.emma2025.fpfa.model.entities;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "courses", schema = "_da_vtschool_2526")
public class Cours {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "courses_id_gen")
    @SequenceGenerator(name = "courses_id_gen", sequenceName = "courses_code_seq", allocationSize = 1)
    @Column(name = "code", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 90)
    private String name;

    @OneToMany(mappedBy = "course")
    private Set<Enrollment> enrollments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "course")
    private Set<SubjectCours> subjectCourses = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(Set<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    public Set<SubjectCours> getSubjectCourses() {
        return subjectCourses;
    }

    public void setSubjectCourses(Set<SubjectCours> subjectCourses) {
        this.subjectCourses = subjectCourses;
    }

}