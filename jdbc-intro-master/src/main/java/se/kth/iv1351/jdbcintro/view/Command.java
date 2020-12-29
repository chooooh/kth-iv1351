/*
 * This class is taken from https://github.com/KTH-IV1351/jdbc-bank/tree/master/src/main/java/se/kth/iv1351/bankjdbc/view and modified.
 */

package se.kth.iv1351.jdbcintro.view;

/**
 * Defines all commands that can be performed by a user of the chat application.
 */
public enum Command {
    /**
     * Creates a new account.
     */
    NEW,
    /**
     * Lists all existing accounts.
     */
    LIST,
    /**
     * Deletes the specified account.
     */
    STUDENT,
    STUDENTS,
    RENTALS,
    RENT,
    TERMINATE,
    HELP,
    /**
     * Leave the chat application.
     */
    QUIT,
    /**
     * None of the valid commands above was specified.
     */
    ILLEGAL_COMMAND
}