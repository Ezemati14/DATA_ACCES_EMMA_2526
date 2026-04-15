package org.emma2026.springdemo.modelo.dto;

public class EmployeeDTO {
    private int id;
    private String name;
    private String job;

    //leer el valor  EJ : 7639
    public int getIdDto() {
        return id;
    }

    public String getNameDto() {
        return name;
    }

    public String getJobDto() {
        return job;
    }
    //guardar el valor EJ: 7639
    public void setIdDto(int id) {
        this.id = id;
    }

    public void setNameDto(String name) {
        this.name = name;
    }

    public void setJobDto(String job) {
        this.job = job;
    }
}
