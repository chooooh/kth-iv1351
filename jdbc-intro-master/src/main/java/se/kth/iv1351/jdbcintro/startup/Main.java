package se.kth.iv1351.jdbcintro.startup;

import se.kth.iv1351.jdbcintro.controller.Controller;
import se.kth.iv1351.jdbcintro.view.BlockingInterpreter;
import se.kth.iv1351.jdbcintro.view.View;

public class Main {
    public static void main(String[] args) {
//        View view = new View(new Controller());
//        view.sampleExecution();
        System.out.println("Type help for commands. Some commands provide information when entered without any parameters.");
        new BlockingInterpreter(new Controller()).handleCmds();
    }
}
