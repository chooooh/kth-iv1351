package se.kth.iv1351.jdbcintro.controller;

import se.kth.iv1351.jdbcintro.integration.Soundgood;
import se.kth.iv1351.jdbcintro.integration.SoundgoodException;
import se.kth.iv1351.jdbcintro.model.Instrument;
import se.kth.iv1351.jdbcintro.model.InstrumentException;
import se.kth.iv1351.jdbcintro.model.Student;
import se.kth.iv1351.jdbcintro.model.StudentException;

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

    public List<Instrument> findRentalInstrumentsByName(String instrumentName) throws InstrumentException {
        String errorMsg = "Could not find rental instrument by name: " + instrumentName;
        try {
            return soundgood.findRentalInstruments(instrumentName.toLowerCase());
        } catch (SoundgoodException e) {
            throw new InstrumentException(errorMsg, e);
        }
    }

    public void rentInstrument(int rentalInstrumentId, int duration) throws InstrumentException {
        rentInstrument(rentalInstrumentId, duration, null);
    }

    public List<Instrument> findStudentRentals() throws StudentException {
        if (student.getStudentId() == 0)
            throw new StudentException("Set a student id");
        String errorMsg = "Could not find student rentals for id: " + student.getStudentId();

        try {
            return soundgood.findStudentRentals(student.getStudentId());
        } catch (SoundgoodException e) {
            throw new StudentException(errorMsg, e);
        }
    }

    public void terminateRental(int rentalInstrumentId) throws InstrumentException {
        String errorMsg = "Could not terminate instrument with rental id: " + rentalInstrumentId;
        try {
            soundgood.terminateRental(rentalInstrumentId, student.getStudentId());
        } catch (SoundgoodException e) {
            throw new InstrumentException(errorMsg, e);
        }
    }

    public void rentInstrument(int rentalInstrumentId, int duration, String instrumentSkill) throws InstrumentException {
        String errorMsg = "Could not rent instrument with rental id: " + rentalInstrumentId;
        try {
            List<Instrument> studentInstruments = soundgood.findStudentRentals(student.getStudentId());
            student.setRentalInstruments(studentInstruments);
            int instrumentId = soundgood.findInstrumentIdByRentalInstrumentId(rentalInstrumentId);
            student.validateRentalInstrument(instrumentId);
            if (instrumentSkill == null)
                soundgood.rentInstrument(rentalInstrumentId, student.getStudentId(), duration, null);
            else
                soundgood.rentInstrument(rentalInstrumentId, student.getStudentId(), duration, instrumentSkill.toLowerCase());
        } catch (Exception e) {
            throw new InstrumentException(errorMsg, e);
        }
    }

    public List<Student> findStudents() throws StudentException {
        String errorMsg = "Could not find students.";
        try {
            return soundgood.findStudents();
        } catch (SoundgoodException e) {
            throw new StudentException(errorMsg, e);
        }
    }

}
