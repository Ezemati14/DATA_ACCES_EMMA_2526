package org.emma2025.fpfa.model.entities;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "students", schema = "_da_vtschool_2526")
public class Student {
    @Id
    //@SequenceGenerator(name = "students_id_gen", sequenceName = "scores_code_seq", allocationSize = 1)
    @Column(name = "idcard", nullable = false, length = 8)
    private String idcard;

    @Column(name = "firstname", nullable = false, length = 50)
    private String firstname;

    @Column(name = "lastname", nullable = false, length = 100)
    private String lastname;

    @Column(name = "phone", length = 12)
    private String phone;

    @Column(name = "email", length = 100)
    private String email;

    @OneToMany(mappedBy = "student")
    private Set<Enrollment> enrollments = new LinkedHashSet<>();

    public Student() {}

    public Student(String idcard, String firstname, String lastname, String email, String phone) {
        this.idcard = idcard;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(Set<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

}