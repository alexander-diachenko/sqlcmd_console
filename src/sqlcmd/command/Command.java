package sqlcmd.command;

import sqlcmd.Console;

import java.sql.*;

public class Command {

    public String getCommand() {
        Console console = new Console();
        String command = console.read();

        try {
            boolean flag = true;
            while (flag) {
                if (command.equals("help")) {
                    Help help = new Help();
                    help.doHelp();
                    getCommand();
                    flag = false;

                } else if (command.equals("exit")) {
                    Exit exit = new Exit();
                    exit.doExit();
                    break;

                } else if (command.startsWith("connect|")) {
                    Connect connect = new Connect();
                    connect.doConnect(command);
                    break;

                } else {
                    System.out.println("Please enter existing command or type 'help' for command list:");
                    getCommand();
                }
            }
        } catch (NullPointerException e) {
            System.out.println("Goodbye");
        }
        return null;
    }

    public String getCommand(Connection connection) {
        Console console = new Console();

        try {
            while (true) {
                System.out.println("Enter command or 'help' for command list:");
                String command = console.read();

                if (command.equals("help")) {
                    Help help = new Help();
                    help.doHelp(connection);

                } else if (command.equals("exit")) {
                    Exit exit = new Exit();
                    exit.doExit();
                    break;

                } else if (command.startsWith("create|")) {
                    Create create = new Create();
                    create.doCreate(connection, command);

                } else if (command.startsWith("delete|")) {
                    Delete delete = new Delete();
                    delete.doDelete(connection, command);

                } else if (command.startsWith("find|")) {
                    Find find = new Find();
                    find.doFind(connection, command);

                } else if (command.equals("list")) {
                    List list = new List();
                    list.doList(connection);

                } else if (command.startsWith("update|")) {
                    Update update = new Update();
                    update.doUpdate(connection, command);

                } else if (command.startsWith("drop|")) {
                    Drop drop = new Drop();
                    drop.doDrop(connection, command, console);

                } else if (command.startsWith("connect|")) {
                    Connect connect = new Connect();
                    connect.doConnect(command);

                } else if (command.startsWith("clear|")) {
                    Clear clear = new Clear();
                    clear.doClear(connection, command, console);

                } else {
                    Unsupported unsupported = new Unsupported();
                    unsupported.doUnsupported();
                }
            }
        } catch (NullPointerException e) {

        }
        return null;
    }
}