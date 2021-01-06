package se.kth.iv1351.jdbcintro.model;

public class InstrumentException extends Exception {
    public InstrumentException(String message) {
        super(message);
    }

    public InstrumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
