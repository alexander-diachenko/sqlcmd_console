package main;

import databasemanagertest.DatabaseManager;
import databasemanagertest.JDBCDatabaseManager;
import view.Console;
import view.View;

/**
 * Created by POSITIV on 16.09.2015.
 */
public class Main {
    public static void main(String[] args) {
        View view = new Console();
        DatabaseManager manager = new JDBCDatabaseManager();
        Controller controller = new Controller(view, manager);

        controller.run();
    }
}