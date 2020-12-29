package se.kth.iv1351.jdbcintro.integration;

public class SoundgoodException extends Exception {

    public SoundgoodException(String cause) {
        super(cause);
    }

    public SoundgoodException(String cause, Exception e) {
        super(cause, e);
    }
}
