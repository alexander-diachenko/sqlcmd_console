package sqlcmd.main;

import sqlcmd.databasemanager.DatabaseManager;
import sqlcmd.databasemanager.JDBCDatabaseManager;
import sqlcmd.view.Console;
import sqlcmd.view.View;

public class Main {
    public static void main(String[] args) {
        View view = new Console();
        DatabaseManager manager = new JDBCDatabaseManager();
        Controller controller = new Controller(view, manager);

        controller.run();

    }
}