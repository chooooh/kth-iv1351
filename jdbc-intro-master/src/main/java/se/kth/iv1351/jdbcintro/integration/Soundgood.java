package se.kth.iv1351.jdbcintro.integration;

import se.kth.iv1351.jdbcintro.model.Instrument;
import se.kth.iv1351.jdbcintro.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles and encapsulates all database calls in the Soundgood application.
 */
public class Soundgood {
    private static final String RENTAL_INSTRUMENT_TABLE_NAME = "rental_instrument";
    private static final String INSTRUMENT_TABLE_NAME = "instrument";
    private static final String PERSON_INSTRUMENT_TABLE_NAME = "person_instrument";
    private static final String APPLICATION_TABLE_NAME = "application";
    private static final String STUDENT_TABLE_NAME = "student";
    private static final String PERSON_TABLE_NAME = "person";
    private static final String STOCK_TABLE_NAME = "stock";
    private static final String EMAIL_TABLE_NAME = "email";
    private static final String PERSON_EMAIL_TABLE_NAME = "person_email";


    private Connection connection;
    private PreparedStatement findRentalInstrumentsStmt;
    private PreparedStatement findStudentApplicationStmt;
    private PreparedStatement findStudentInstrumentSkillStmt;
    private PreparedStatement findStudentPersonIdStmt;
    private PreparedStatement findInstrumentIdByRentalInstrumentIdStmt;
    private PreparedStatement findRentalInstrumentStmt;
    private PreparedStatement findStudentOngoingRentalsStmt;
    private PreparedStatement findStudentsStmt;
    private PreparedStatement updateStockQuantityStmt;
    private PreparedStatement addRentalInstrumentBackStmt;
    private PreparedStatement setRentalIsRentedStatusStmt;
    private PreparedStatement setRentalDurationStmt;
    private PreparedStatement setRentalIsTerminatedStatusStmt;
    private PreparedStatement rentInstrumentStmt;
    private PreparedStatement terminateRentalStmt;

    /**
     * Creates a new Soundgood object and attempts to connect to the database.
     */
    public Soundgood() {
        try {
            connectToDB();
            prepareStatements();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds all rental instruments by instrument name (e.g. piano, guitar, ...)
     * @param instrumentType the specified instrument name
     * @return A list containing found instruments
     * @throws SoundgoodException on failure to find rental instruments
     */
    public List<Instrument> findRentalInstruments(String instrumentType) throws SoundgoodException {
        String errorMsg = "Could not find rental instruments.";
        ResultSet results = null;
        List<Instrument> rentalInstruments = new ArrayList<>();
        try {
            findRentalInstrumentsStmt.setString(1, instrumentType);
            results = findRentalInstrumentsStmt.executeQuery();
            while (results.next()) {
                String name = results.getString(1);
                String brand = results.getString(2);
                int fee = results.getInt(3);
                Date date = results.getDate(4);
                Time time = results.getTime(5);
                int duration = results.getInt(6);
                int rentalId = results.getInt(7);
                int instrumentId = results.getInt(8);
                rentalInstruments.add(new Instrument(rentalId, instrumentId, brand, fee, date, time, duration, name));
            }
            connection.commit();
        } catch (SQLException e) {
            handleException(errorMsg, e);
        } finally {
            closeResultSet(errorMsg, results);
        }
        return rentalInstruments;
    }

    /**
     * Finds all students in the database.
     * @return A list of all students.
     * @throws SoundgoodException on failure to retrieve all students.
     */
    public List<Student> findStudents() throws SoundgoodException {
        String errorMsg = "Could not find students.";
        List<Student> students = new ArrayList<>();
        try {
            ResultSet results = findStudentsStmt.executeQuery();
            while (results.next()) {
                int studentId = results.getInt(1);
                String email = results.getString(2);
                students.add(new Student(studentId, email));
            }
        } catch (SQLException e) {
            handleException(errorMsg, e);
        }
        return students;
    }

    /**
     * Rents an instrument.
     * @param rentalInstrumentId the specified rental instrument id.
     * @param studentId the specified student id.
     * @param duration the specified duration in months.
     * @param instrumentSkill the specified instrument skill (beginner, intermediate or advanced).
     * @throws SoundgoodException on failure to rent instrument.
     */
    public void rentInstrument(int rentalInstrumentId, int studentId, int duration, String instrumentSkill) throws SoundgoodException {
        String errorMsg = "Could not rent instrument.";
        if (duration > 12)
            handleException("Cannot rent instruments for more than 12 months.", null);
        try {
            if (checkIfRentalInstrumentIsRentable(rentalInstrumentId) || findStudentRentals(studentId).size() >= 2)
                handleException(errorMsg, null);
            int instrumentId = findInstrumentIdByRentalInstrumentId(rentalInstrumentId);
            int personId = findStudentPersonId(studentId);
            rentInstrumentStmt.setInt(1, instrumentId);
            rentInstrumentStmt.setInt(2, personId);
            rentInstrumentStmt.setString(3, instrumentSkill);
            rentInstrumentStmt.executeUpdate();
            setRentalDuration(rentalInstrumentId, duration);
            setRentalIsRentedStatus(rentalInstrumentId, true);
            updateStockQuantity(instrumentId, -1);
            connection.commit();
        } catch (SQLException e) {
            handleException(errorMsg, e);
        }
    }

    /**
     * Finds student rentals by student id.
     * @param studentId the specified student id.
     * @return A list of a student's ongoing rental instruments
     * @throws SoundgoodException on failure to retrieve a student's ongoing rental instruments
     */
    public List<Instrument> findStudentRentals(int studentId) throws SoundgoodException {
        String errorMsg = "Could not find student's ongoing instrument rentals.";
        List<Instrument> instruments = new ArrayList<>();
        ResultSet results = null;
        try {
            findStudentOngoingRentalsStmt.setInt(1, studentId);
            results = findStudentOngoingRentalsStmt.executeQuery();
            while (results.next()) {
                String name = results.getString(1);
                String brand = results.getString(2);
                int fee = results.getInt(3);
                Date date = results.getDate(4);
                Time time = results.getTime(5);
                int duration = results.getInt(6);
                int rentalId = results.getInt(7);
                int instrumentId = results.getInt(8);

                instruments.add(new Instrument(rentalId, instrumentId, brand, fee, date, time, duration, name));
            }
        } catch (SQLException e) {
            handleException(errorMsg, e);
        } finally {
            closeResultSet(errorMsg, results);
        }
        return instruments;
    }

    /**
     * Terminates an ongoing instrument rental.
     * @param rentalInstrumentId the specified rental instrument id.
     * @param studentId the specified student id.
     * @throws SoundgoodException on failure to terminate rental.
     */
    public void terminateRental(int rentalInstrumentId, int studentId) throws SoundgoodException {
        String errorMsg = "Could not terminate instrument rental.";
        try {
            int personId = findStudentPersonId(studentId);
            Integer instrumentId = findInstrumentIdByRentalInstrumentId(rentalInstrumentId);
            if (deleteRentalInstrumentRecord(personId, instrumentId) == 0)
                handleException(errorMsg, null);
            setRentalIsRentedStatus(rentalInstrumentId, false);
            setRentalIsTerminatedStatus(rentalInstrumentId, true);
            updateStockQuantity(instrumentId, 1);

            Instrument instrument = findRentalInstrument(rentalInstrumentId);
            addRentalInstrumentBack(instrument);
            connection.commit();
        } catch (SQLException e) {
            handleException(errorMsg, e);
        }
    }

    private Integer findStudentPersonId(int studentId) throws SoundgoodException {
        String errorMsg = "Could not find student's person id.";
        ResultSet result = null;
        try {
            findStudentPersonIdStmt.setInt(1, studentId);
            result = findStudentPersonIdStmt.executeQuery();
            if (result.next())
                return result.getInt("id");
            else
                handleException(errorMsg, null);
            connection.commit();
        } catch (SQLException e) {
            handleException(errorMsg, e);
        } finally {
            closeResultSet(errorMsg, result);
        }
        return null;
    }

    private Integer findInstrumentIdByRentalInstrumentId(int rentalInstrumentId) throws SoundgoodException {
        String errorMsg = "Could not find instrument id by rental instrument id.";
        ResultSet result = null;
        try {
            findInstrumentIdByRentalInstrumentIdStmt.setInt(1, rentalInstrumentId);
            result = findInstrumentIdByRentalInstrumentIdStmt.executeQuery();
            if (result.next())
                return result.getInt(1);
            else
                handleException(errorMsg, null);
            connection.commit();
        } catch (SQLException e) {
            handleException(errorMsg, e);
        } finally {
            closeResultSet(errorMsg, result);
        }
        return null;
    }

    private boolean checkIfRentalInstrumentIsRentable(int rentalInstrumentId) throws SoundgoodException {
        String errorMsg = "Could not find rental instrument.";
        ResultSet result = null;
        try {
            findRentalInstrumentStmt.setInt(1, rentalInstrumentId);
            result = findRentalInstrumentStmt.executeQuery();
            if (result.next())
                return result.getBoolean("is_rented") || result.getBoolean("is_terminated");
            connection.commit();
        } catch (SQLException e) {
            handleException(errorMsg, e);
        } finally {
            closeResultSet(errorMsg, result);
        }
        return false;
    }

    private void setRentalIsTerminatedStatus(int rentalInstrumentId, boolean b) throws SQLException {
        setRentalIsTerminatedStatusStmt.setBoolean(1, b);
        setRentalIsTerminatedStatusStmt.setInt(2, rentalInstrumentId);
        setRentalIsTerminatedStatusStmt.executeUpdate();
    }

    private void setRentalDuration(int rentalInstrumentId, int duration) throws SQLException {
        setRentalDurationStmt.setInt(1, duration);
        setRentalDurationStmt.setInt(2, rentalInstrumentId);
        setRentalDurationStmt.executeUpdate();
    }

    private void setRentalIsRentedStatus(int rentalInstrumentId, boolean b) throws SQLException {
        setRentalIsRentedStatusStmt.setBoolean(1, b);
        setRentalIsRentedStatusStmt.setInt(2, rentalInstrumentId);
        setRentalIsRentedStatusStmt.executeUpdate();
    }

    private void updateStockQuantity(int instrumentId, int numberToAdd) throws SQLException {
        updateStockQuantityStmt.setInt(1, numberToAdd);
        updateStockQuantityStmt.setInt(2, instrumentId);
        updateStockQuantityStmt.executeUpdate();
    }

    private Instrument findRentalInstrument(int rentalInstrumentId) throws SQLException {
        findRentalInstrumentStmt.setInt(1, rentalInstrumentId);
        ResultSet result = findRentalInstrumentStmt.executeQuery();
        if (result.next()) {
            String brand = result.getString("brand");
            int instrumentId = result.getInt("instrument_id");
            int fee = result.getInt("fee");
            return new Instrument(rentalInstrumentId, instrumentId, brand, fee);
        }
        return null;
    }

    private void addRentalInstrumentBack(Instrument instrument) throws SQLException {
        String brand = instrument.getBrand();
        int instrumentId = instrument.getInstrumentId();
        int fee = instrument.getFee();
        addRentalInstrumentBackStmt.setInt(1, instrumentId);
        addRentalInstrumentBackStmt.setString(2, brand);
        addRentalInstrumentBackStmt.setInt(3, fee);
        addRentalInstrumentBackStmt.setBoolean(4, false);
        addRentalInstrumentBackStmt.setBoolean(5, false);

        addRentalInstrumentBackStmt.executeUpdate();
    }

    private int deleteRentalInstrumentRecord(int personId, int instrumentId) throws SQLException {
        terminateRentalStmt.setInt(1, instrumentId);
        terminateRentalStmt.setInt(2, personId);
        return terminateRentalStmt.executeUpdate();
    }

    private void connectToDB() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/soundgood",
                "postgres", "example");

        connection.setAutoCommit(false);
    }

    private void prepareStatements() throws SQLException {
        findRentalInstrumentsStmt = connection.prepareStatement("SELECT i.instrument_name, ri.brand, ri.fee, ri.date, ri.time, ri.duration, ri.id, i.id FROM " + RENTAL_INSTRUMENT_TABLE_NAME + " AS ri INNER JOIN " + INSTRUMENT_TABLE_NAME + " AS i ON ri.instrument_id = i.id WHERE i.instrument_name=? AND ri.is_rented=false AND ri.is_terminated=false");
        findStudentApplicationStmt = connection.prepareStatement("SELECT * FROM " + APPLICATION_TABLE_NAME + " WHERE student_id = ? AND instrument_id = ?");
        findStudentInstrumentSkillStmt = connection.prepareStatement("SELECT a.instrument_skill FROM " + APPLICATION_TABLE_NAME + " AS a INNER JOIN " + STUDENT_TABLE_NAME + " AS s on a.student_id = s.id INNER JOIN " + PERSON_TABLE_NAME + " AS p on p.id = s.person_id WHERE s.id = ? AND a.instrument_id = ?;");
        findStudentPersonIdStmt = connection.prepareStatement("SELECT s.id FROM " + STUDENT_TABLE_NAME + " AS s INNER JOIN " + PERSON_TABLE_NAME + " AS p ON s.person_id = p.id WHERE s.id = ?");
        findInstrumentIdByRentalInstrumentIdStmt = connection.prepareStatement("SELECT ri.instrument_id FROM " + RENTAL_INSTRUMENT_TABLE_NAME + " AS ri WHERE ri.id = ?");
        findRentalInstrumentStmt = connection.prepareStatement("SELECT * FROM " + RENTAL_INSTRUMENT_TABLE_NAME + " WHERE id = ?");
        findStudentOngoingRentalsStmt = connection.prepareStatement("SELECT i.instrument_name, ri.brand, ri.fee, ri.date, ri.time, ri.duration, ri.id, i.id FROM " + STUDENT_TABLE_NAME + " s INNER JOIN " + PERSON_INSTRUMENT_TABLE_NAME + " pi ON pi.person_id = s.person_id INNER JOIN " + INSTRUMENT_TABLE_NAME + " i ON i.id = pi.instrument_id INNER JOIN " + RENTAL_INSTRUMENT_TABLE_NAME + " ri ON ri.instrument_id = i.id WHERE s.id = ? AND ri.is_rented = true");
        findStudentsStmt = connection.prepareStatement("SELECT s.id, e.email FROM " + STUDENT_TABLE_NAME + " s " + "INNER JOIN " + PERSON_TABLE_NAME + " p ON p.id = s.person_id " + "INNER JOIN " + PERSON_EMAIL_TABLE_NAME + " pe ON pe.person_id = p.id " + "INNER JOIN " + EMAIL_TABLE_NAME + " e ON e.id = pe.email_id ");

        rentInstrumentStmt = connection.prepareStatement("INSERT INTO " + PERSON_INSTRUMENT_TABLE_NAME + " (instrument_id, person_id, present_skill) VALUES (?, ?, ?)");
        addRentalInstrumentBackStmt = connection.prepareStatement("INSERT INTO " + RENTAL_INSTRUMENT_TABLE_NAME + " (instrument_id, brand, fee, is_rented, is_terminated) VALUES (?, ?, ?, ?, ?)");

        setRentalIsRentedStatusStmt = connection.prepareStatement("UPDATE " + RENTAL_INSTRUMENT_TABLE_NAME + " SET is_rented = ?, date = CURRENT_DATE, time = CURRENT_TIME(0) WHERE id = ?");
        setRentalDurationStmt = connection.prepareStatement("UPDATE " + RENTAL_INSTRUMENT_TABLE_NAME + " SET duration = ?, date = CURRENT_DATE, time = CURRENT_TIME(0) WHERE id = ?");
        setRentalIsTerminatedStatusStmt = connection.prepareStatement("UPDATE " + RENTAL_INSTRUMENT_TABLE_NAME + " SET is_terminated = ? WHERE id = ?");
        updateStockQuantityStmt = connection.prepareStatement("UPDATE " + STOCK_TABLE_NAME + " SET instrument_quantity = instrument_quantity + ? WHERE instrument_id = ?");

        terminateRentalStmt = connection.prepareStatement("DELETE FROM " + PERSON_INSTRUMENT_TABLE_NAME + " pi WHERE pi.instrument_id = ? AND pi.person_id = ? ");
    }

    private void handleException(String failureMsg, Exception cause) throws SoundgoodException {
        String failureMSG = failureMsg;
        try {
            connection.rollback();
        } catch (SQLException e) {
            failureMSG = failureMsg + " Rollback transaction failed due to following: " + e.getMessage();
        }

        if (cause != null)
            throw new SoundgoodException(failureMSG, cause);
        else
            throw new SoundgoodException(failureMSG);
    }

    private void closeResultSet(String failureMsg, ResultSet result) throws SoundgoodException {
        try {
            result.close();
        } catch (Exception e) {
            throw new SoundgoodException(failureMsg + " Could not close ResultSet object.", e);
        }
    }
}
