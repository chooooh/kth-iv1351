package se.kth.iv1351.jdbcintro.model;

import java.util.List;

public class Student {
    private List<Instrument> rentalInstruments;
    private int studentId;
    private String email;

    public Student() {
    }

    public Student(int studentId) {
        this.studentId = studentId;
    }

    public Student(int studentId, String email) {
        this.studentId = studentId;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public List<Instrument> getRentalInstruments() {
        return rentalInstruments;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setRentalInstruments(List<Instrument> rentalInstruments) {
        this.rentalInstruments = rentalInstruments;
    }

}
