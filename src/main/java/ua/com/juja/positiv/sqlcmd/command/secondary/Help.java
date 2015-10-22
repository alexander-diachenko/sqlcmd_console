package ua.com.juja.positiv.sqlcmd.command.secondary;

import ua.com.juja.positiv.sqlcmd.command.Command;
import ua.com.juja.positiv.sqlcmd.command.NullFormat;
import ua.com.juja.positiv.sqlcmd.view.View;

/**
 * Created by POSITIV on 16.09.2015.
 */
public class Help extends NullFormat implements Command {

    private View view;
    private Command[] commands;

    public Help(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("help");
    }

    @Override
    public void process(String command) {
        for (Command comm : commands) {
            if (comm.format() != null) {
                view.write(comm.format());
                view.write("\t " + comm.description());
            }
        }
    }

    public void setCommands(Command[] commands) {
        this.commands = commands;
    }
}