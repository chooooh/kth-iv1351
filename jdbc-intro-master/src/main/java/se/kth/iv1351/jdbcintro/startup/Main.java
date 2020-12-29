package se.kth.iv1351.jdbcintro.startup;

import se.kth.iv1351.jdbcintro.controller.Controller;
import se.kth.iv1351.jdbcintro.view.BlockingInterpreter;

public class Main {
    public static void main(String[] args) {
        System.out.println("Type help for commands. Some commands provide information when entered without any parameters.");
        new BlockingInterpreter(new Controller()).handleCmds();
    }
}
