package ua.com.juja.positiv.sqlcmd.command.secondary;

import ua.com.juja.positiv.sqlcmd.command.Command;
import ua.com.juja.positiv.sqlcmd.view.View;

/**
 * Created by POSITIV on 16.09.2015.
 */
public class Exit implements Command {

    private View view;

    public Exit(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("exit");
    }

    @Override
    public void process(String command) {
        view.write("До свидания!");
        throw new ExitException();
    }
}