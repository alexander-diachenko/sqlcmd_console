package ua.com.juja.positiv.sqlcmd.main;

import ua.com.juja.positiv.sqlcmd.command.Command;
import ua.com.juja.positiv.sqlcmd.command.connect.Connect;
import ua.com.juja.positiv.sqlcmd.command.connect.isConnected;
import ua.com.juja.positiv.sqlcmd.command.secondary.Exit;
import ua.com.juja.positiv.sqlcmd.command.secondary.Help;
import ua.com.juja.positiv.sqlcmd.command.secondary.Unsupported;
import ua.com.juja.positiv.sqlcmd.command.show.Find;
import ua.com.juja.positiv.sqlcmd.command.show.List;
import ua.com.juja.positiv.sqlcmd.command.update.database.CreateBase;
import ua.com.juja.positiv.sqlcmd.command.update.database.DropBase;
import ua.com.juja.positiv.sqlcmd.command.update.table.*;
import ua.com.juja.positiv.sqlcmd.databasemanager.DatabaseManager;
import ua.com.juja.positiv.sqlcmd.databasemanager.JDBCDatabaseManager;
import ua.com.juja.positiv.sqlcmd.view.Console;
import ua.com.juja.positiv.sqlcmd.view.View;

/**
 * Created by POSITIV on 16.09.2015.
 */
public class Main {
    public static void main(String[] args) {
        View view = new Console();
        DatabaseManager manager = new JDBCDatabaseManager();
        Command[] commands =  new Command[]{
                new Exit(view),
                new Help(view),
                new Connect(manager, view),
                new isConnected(manager, view),
                new CreateBase(manager, view),
                new DropBase(manager, view),
                new Table(manager, view),
                new List(view, manager),
                new Find(manager, view),
                new Create(manager, view),
                new Update(manager, view),
                new Delete(manager, view),
                new Clear(manager, view),
                new Drop(manager, view),
                new Unsupported(view)};

        Controller controller = new Controller(view, commands);
        controller.run();
    }
}

