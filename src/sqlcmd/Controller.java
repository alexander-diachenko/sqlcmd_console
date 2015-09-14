package sqlcmd;

import sqlcmd.command.*;
import sqlcmd.databasemanager.DatabaseManager;
import sqlcmd.view.View;

public class Controller {

    private Command[] commands;
    private DatabaseManager manager;
    private View view;

    public Controller(View view, DatabaseManager manager) {
        this.manager = manager;
        this.view = view;
        this.commands = new Command[]{
                new Exit(view),
                new Help(view),
                new Connect(manager, view),
                new isConnected(manager, view),
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
        System.out.println("Добро пожаловать!");

        while (true) {
            System.out.println("Введите команду или 'help' для помощи:");
            String command = view.read();

            for (Command comm : commands) {
                if (comm.canProcess(command)) {
                    comm.process(command);
                    break;
                }
            }
        }
    }
}

