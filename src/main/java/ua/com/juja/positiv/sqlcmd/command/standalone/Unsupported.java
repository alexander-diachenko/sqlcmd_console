package ua.com.juja.positiv.sqlcmd.command.standalone;

import ua.com.juja.positiv.sqlcmd.command.Command;
import ua.com.juja.positiv.sqlcmd.view.View;

public class Unsupported implements Command {

    private View view;

    public Unsupported(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return true;
    }

    @Override
    public void process(String command) {
        view.write(String.format("Команды '%s' не существует.", command));
    }
}