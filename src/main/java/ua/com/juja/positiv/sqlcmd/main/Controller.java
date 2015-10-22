package ua.com.juja.positiv.sqlcmd.main;

import ua.com.juja.positiv.sqlcmd.command.*;
import ua.com.juja.positiv.sqlcmd.command.secondary.Exit;
import ua.com.juja.positiv.sqlcmd.command.secondary.ExitException;
import ua.com.juja.positiv.sqlcmd.view.View;

public class Controller {

    private Command[] commands;
    private View view;

    public Controller(View view, Command[] commands) {
        this.view = view;
        this.commands = commands;
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
