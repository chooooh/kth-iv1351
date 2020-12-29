package se.kth.iv1351.jdbcintro.view;

import se.kth.iv1351.jdbcintro.controller.Controller;
import se.kth.iv1351.jdbcintro.model.Instrument;

import java.util.List;

public class View {
    private Controller controller;

    public View(Controller controller) {
        this.controller = controller;
    }

    private void findRentalInstrumentsByName(String name) {
        List<Instrument> rentalInstruments = controller.findRentalInstrumentsByName(name);
        System.out.println("rental instruments by " + name + ": " + rentalInstruments);
    }

    public void sampleExecution() {
        try {
            controller.setStudentId(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        findRentalInstrumentsByName("guitar");
        findRentalInstrumentsByName("piano");

//        try {
//            controller.rentInstrument(7,2);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        controller.terminateRental(7);

        List<Instrument> ongoingStudentRentals = controller.findStudentRentals();
        System.out.println("student rentals: " + ongoingStudentRentals);

    }

}
