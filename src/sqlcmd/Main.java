package sqlcmd;

import sqlcmd.command.Command;

public class Main {
    public static void main(String[] args) {

//        Console console = new Console();
        Command command = new Command();

        System.out.println("Welcome!");
        System.out.println("Enter command or 'help' for command list:");

        command.getCommand();
    }
}