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


    public String run() {

        try {
            while (true) {
                System.out.println("Введите команду или 'help' для помощи:");
                String command = console.read();

                for(Command comm : commands){
                    if(comm.canProcess(command)) {
                        comm.process(command);
                        break;
                    }
                }
            }
        } catch (NullPointerException e) {
            System.out.println("До свидания");
        }
        return null;
    }
}

