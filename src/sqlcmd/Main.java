package sqlcmd;

import sqlcmd.command.Command;

public class Main {
    public static void main(String[] args) {

        Command command = new Command();

        System.out.println("Welcome!");

        command.getCommand();

    }
}