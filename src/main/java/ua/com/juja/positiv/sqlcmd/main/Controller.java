package ua.com.juja.positiv.sqlcmd.main;

import ua.com.juja.positiv.sqlcmd.command.*;
import ua.com.juja.positiv.sqlcmd.databasemanager.DatabaseManager;
import ua.com.juja.positiv.sqlcmd.view.View;

public class Controller {

    private Command[] commands;
    private View view;

    public Controller(View view, DatabaseManager manager) {
        this.view = view;
        this.commands = new Command[]{
                new Exit(view),
                new Help(view),
                new Connect(manager, view),
                new isConnected(manager, view),
                new Table(manager, view),
                new List(view, manager),
                new Find(manager, view),
                new Create(manager, view),
                new Update(manager, view),
                new Delete(manager, view),
                new Clear(manager, view),
                new Drop(manager, view),
                new Unsupported(view)};
    }

    public void run() {
        view.write("Добро пожаловать!");

        try {
            while (true) {
                view.write("Введите команду или help для помощи:");
                String input = view.read();
                if (input == null) {
                    new Exit(view).process(input);
                }

                for (Command comm : commands) {
                    if (comm.canProcess(input)) {
                        comm.process(input);
                        break;
                    }
                }
            }
        } catch (ExitException e) {
            //do nothing
        }
    }
}
