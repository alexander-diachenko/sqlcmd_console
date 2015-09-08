package sqlcmd.command;

import sqlcmd.Console;
import sqlcmd.databasemanager.DatabaseManager;

public class Controller {

    private  Command[] commands;
    private DatabaseManager manager;
    private  Console console;

    public Controller(Console console, DatabaseManager manager) {
        this.manager = manager;
        this.console = console;
        this.commands = new Command[]{
                new Exit(manager),
                new Help(manager),
                new Connect(manager),
                new List(manager),
                new Find(manager),
                new Create(manager),
                new Update(manager),
                new Delete(manager),
                new Clear(manager),
                new Drop(manager),
                new Unsupported(manager)};
    }


    public String getCommand() {

        try {
            while (true) {
                System.out.println("Введите команду или 'help' для помощи:");
                String command = console.read();
                manager.connect(command);

                if (commands[1].canProcess(command)) {
                    commands[1].process(command);

                } else if (commands[0].canProcess(command)) {
                    commands[0].process(command);
                    break;

                } else if (commands[5].canProcess(command)) {
                    commands[5].process(command);

                } else if (commands[7].canProcess(command)) {
                    commands[7].process(command);

                } else if (commands[4].canProcess(command)) {
                    commands[4].process(command);

                } else if (commands[3].canProcess(command)) {
                    commands[3].process(command);

                } else if (commands[6].canProcess(command)) {
                    commands[6].process(command);

                } else if (commands[10].canProcess(command)) {
                    commands[10].process(command);

                } else if (commands[2].canProcess(command)) {
                    commands[2].process(command);

                } else if (commands[8].canProcess(command)) {
                    commands[8].process(command);

                } else {
                    commands[10].process(command);
                }
            }
        } catch (NullPointerException e) {

        }
        return null;
    }
}

