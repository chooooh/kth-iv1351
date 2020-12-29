package se.kth.iv1351.jdbcintro.controller;

import se.kth.iv1351.jdbcintro.integration.Soundgood;
import se.kth.iv1351.jdbcintro.integration.SoundgoodException;
import se.kth.iv1351.jdbcintro.model.Instrument;
import se.kth.iv1351.jdbcintro.model.Student;

import java.util.List;

public class Controller {
    private final Soundgood soundgood;
    private Student student;

    public Controller() {
        soundgood = new Soundgood();
        student = new Student();
    }

    public void setStudentId(int studentId) throws Exception {
        if (studentId < 0)
            throw new Exception("invalid student id");
        student.setStudentId(studentId);
    }

    public List<Instrument> findRentalInstrumentsByName(String instrumentName) {
        try {
            return soundgood.findRentalInstruments(instrumentName.toLowerCase());
        } catch (SoundgoodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void rentInstrument(int rentalInstrumentId, int duration) throws Exception {
        rentInstrument(rentalInstrumentId, duration, null);
    }

    public List<Instrument> findStudentRentals() {
        try {
            return soundgood.findStudentRentals(student.getStudentId());
        } catch (SoundgoodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void terminateRental(int rentalInstrumentId) {
        try {
            soundgood.terminateRental(rentalInstrumentId, student.getStudentId());
        } catch (SoundgoodException e) {
            e.printStackTrace();
        }
    }

    public void rentInstrument(int rentalInstrumentId, int duration, String instrumentSkill) throws Exception {
        try {
            List<Instrument> instruments = soundgood.findStudentRentals(student.getStudentId());
            if (instrumentSkill == null)
                soundgood.rentInstrument(rentalInstrumentId, student.getStudentId(), duration, null);
            else
                soundgood.rentInstrument(rentalInstrumentId, student.getStudentId(), duration, instrumentSkill.toLowerCase());
        } catch (SoundgoodException e) {
            e.printStackTrace();
        }
    }

    public List<Student> findStudents() {
        try {
            return soundgood.findStudents();
        } catch (SoundgoodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
