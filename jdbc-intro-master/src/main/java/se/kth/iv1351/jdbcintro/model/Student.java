package se.kth.iv1351.jdbcintro.model;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private List<Instrument> rentalInstruments;
    private int studentId;
    private String email;

    public Student() {
        rentalInstruments = new ArrayList<>();
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

    public void addRentalInstruments(List<Instrument> rentalInstruments) throws Exception {
        rentalInstruments.addAll(rentalInstruments);
    }

    public void validateRentalInstrument(int instrumentId) throws Exception {
        if (rentalInstruments.size() >= 2)
            throw new Exception("Cannot rent more than 2 instruments at a time.");
        for (Instrument instrument : rentalInstruments)
            if (instrument.getInstrumentId() == instrumentId)
                throw new Exception("Cannot rent already rented instruments");
    }

}
