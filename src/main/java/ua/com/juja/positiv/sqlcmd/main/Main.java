package ua.com.juja.positiv.sqlcmd.main;

import ua.com.juja.positiv.sqlcmd.databasemanagertest.DatabaseManager;
import ua.com.juja.positiv.sqlcmd.databasemanagertest.JDBCDatabaseManager;
import ua.com.juja.positiv.sqlcmd.view.Console;
import ua.com.juja.positiv.sqlcmd.view.JText;
import ua.com.juja.positiv.sqlcmd.view.View;


/**
 * Created by POSITIV on 16.09.2015.
 */
public class Main{
    public static void main(String[] args) {
//        JText.startGUI();
//        View view = new JText();

        View view = new Console();
        DatabaseManager manager = new JDBCDatabaseManager();
        Controller controller = new Controller(view, manager);

        controller.run();
    }

}
