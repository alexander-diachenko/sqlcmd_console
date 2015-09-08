package sqlcmd;

import sqlcmd.command.Controller;
import sqlcmd.databasemanager.DatabaseManager;
import sqlcmd.databasemanager.JDBCDatabaseManager;

public class Main {


    public static void main(String[] args) {
    DatabaseManager manager = new JDBCDatabaseManager();

        Console console = new Console();
        Controller controller = new Controller(console,manager);

        System.out.println("Welcome!");

        controller.getCommand();

    }
}