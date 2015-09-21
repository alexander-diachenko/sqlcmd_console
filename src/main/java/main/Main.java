package main;

import databasemanagertest.DatabaseManager;
import databasemanagertest.JDBCDatabaseManager;
import view.Console;
import view.JTextAreaConsole;
import view.View;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by POSITIV on 16.09.2015.
 */
public class Main{
    public static void main(String[] args) {
//        JTextAreaConsole text = new JTextAreaConsole();
//        text.pack();
//        text.addWindowListener(new WindowAdapter() {
//            public void windowClosing(WindowEvent e) {
//                System.exit(0);
//            }
//        });
//        text.setVisible(true);

        View view = new Console();
        DatabaseManager manager = new JDBCDatabaseManager();
        Controller controller = new Controller(view, manager);

        controller.run();
    }
}
