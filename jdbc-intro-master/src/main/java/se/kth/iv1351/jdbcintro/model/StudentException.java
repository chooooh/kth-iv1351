package se.kth.iv1351.jdbcintro.model;

public class StudentException extends Exception {

    public StudentException(String message) {
        super(message);
    }

    public StudentException(String message, Throwable cause) {
        super(message, cause);
    }
}
