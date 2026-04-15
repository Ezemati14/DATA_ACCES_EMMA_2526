package org.emma2026.springdemo.modelo.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dept")
public class Dept {
    @Id
    @Column(name = "deptno", nullable = false)
    private Integer id;

    @Column(name = "dname", length = 14)
    private String dname;

    @Column(name = "loc", length = 13)
    private String loc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

}