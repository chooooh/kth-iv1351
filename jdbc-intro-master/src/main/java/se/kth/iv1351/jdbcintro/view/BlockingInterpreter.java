/*
 * This class is taken from https://github.com/KTH-IV1351/jdbc-bank/tree/master/src/main/java/se/kth/iv1351/bankjdbc/view and modified.
 */

package se.kth.iv1351.jdbcintro.view;

import se.kth.iv1351.jdbcintro.controller.Controller;
import se.kth.iv1351.jdbcintro.model.Instrument;
import se.kth.iv1351.jdbcintro.model.Student;

import java.util.List;
import java.util.Scanner;

public class BlockingInterpreter {
    private static final String PROMPT = "> ";
    private final Scanner console = new Scanner(System.in);
    private Controller controller;
    private boolean keepReceivingCmds = false;

    /**
     * Creates a new instance that will use the specified controller for all operations.
     *
     * @param controller The controller used by this instance.
     */
    public BlockingInterpreter(Controller controller) {
        this.controller = controller;
    }

    /**
     * Stops the commend interpreter.
     */
    public void stop() {
        keepReceivingCmds = false;
    }

    /**
     * Interprets and performs user commands. This method will not return until the
     * UI has been stopped. The UI is stopped either when the user gives the
     * "quit" command, or when the method <code>stop()</code> is called.
     */
    public void handleCmds() {
        keepReceivingCmds = true;
        while (keepReceivingCmds) {
            try {
                CmdLine cmdLine = new CmdLine(readNextLine());
                switch (cmdLine.getCmd()) {
                    case HELP:
                        for (Command command : Command.values()) {
                            if (command == Command.ILLEGAL_COMMAND) {
                                continue;
                            }
                            System.out.println(command.toString().toLowerCase());
                        }
                        break;
                    case QUIT:
                        keepReceivingCmds = false;
                        break;
                    case STUDENT: {
                        if (cmdLine.getParameter(0).equals("")) {
                            System.out.println("Use following syntax to set a studentId: STUDENT [studentId]");
                            break;
                        }
                        int studentId = Integer.parseInt(cmdLine.getParameter(0));
                        controller.setStudentId(studentId);
                        break;
                    }
                    case STUDENTS: {
                        List<Student> students = controller.findStudents();
                        System.out.println("students: ");
                        for (Student student : students)
                            System.out.println("\tstudent id: " + student.getStudentId() + ", email: " + student.getEmail());
                        break;
                    }
                    case RENTALS: {
                        List<Instrument> instruments = controller.findStudentRentals();
                        System.out.println("rentals: ");
                        for (Instrument instrument : instruments) {
                            System.out.println("\trental id: " + instrument.getRentalId() + ", "
                                    + "name: " + instrument.getName() + ", "
                                    + "brand: " + instrument.getBrand() + ", "
                                    + "fee: " + instrument.getFee() + " SEK/month"
                            );
                        }
                        break;
                    }
                    case RENT:
                        if (cmdLine.getParamLength() == 2)
                            controller.rentInstrument(Integer.parseInt(cmdLine.getParameter(0)), Integer.parseInt(cmdLine.getParameter(1)));
                        else if (cmdLine.getParamLength() == 3)
                            controller.rentInstrument(Integer.parseInt(cmdLine.getParameter(0)), Integer.parseInt(cmdLine.getParameter(1)), cmdLine.getParameter(2));
                        else {
                            System.out.println("Invalid arguments. Use following syntax:" +
                                    " \"RENT [rentalInstrumentId] [duration]\" or \"RENT [rentalInstrumentId] [duration] [skill]\".");
                            break;
                        }
                        break;
                    case LIST: {
                        List<Instrument> instruments = null;
                        String specifiedInstrument = cmdLine.getParameter(0);
                        if (specifiedInstrument.equals("")) {
                            System.out.println("To list available rental instruments, write following: " +
                                    "\"LIST [guitar] or [piano] or ... \"");
                            break;
                        }
                        else
                            instruments = controller.findRentalInstrumentsByName(specifiedInstrument);
                        System.out.println("available instruments: ");
                        for (Instrument instrument : instruments) {
                            System.out.println("\trental id: " + instrument.getRentalId() + ", "
                                    + "name: " + instrument.getName() + ", "
                                    + "brand: " + instrument.getBrand() + ", "
                                    + "fee: " + instrument.getFee()
                            );
                        }
                        break;
                    }
                    case TERMINATE: {
                        if (cmdLine.getParameter(0).equals(""))
                            System.out.println("To terminate an ongoing rental instrument, list all ongoing rentals with LIST_RENTALS" +
                                    " write following: \"> TERMINATE [rentalInstrumentId] \"");
                        controller.terminateRental(Integer.parseInt(cmdLine.getParameter(0)));
                        break;
                    }
                    default:
                        System.out.println("illegal command");
                }
            } catch (Exception e) {
                System.out.println("Operation failed");
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private String readNextLine() {
        System.out.print(PROMPT);
        return console.nextLine();
    }
}