package sqlcmd.command;

import sqlcmd.Console;
import sqlcmd.databasemanager.DatabaseManager;
import sqlcmd.databasemanager.JDBCDatabaseManager;


public class Command {

    public String getCommand() {

        Console console = new Console();
        DatabaseManager manager = new JDBCDatabaseManager();

        try {
            while (true) {
                System.out.println("Введите команду или 'help' для помощи:");
                String command = console.read();

                if (command.equals("help")) {
                    manager.help();

                } else if (command.equals("exit")) {
                   manager.exit();
                    break;

                } else if (command.startsWith("create|")) {
                   manager.create(command);

                } else if (command.startsWith("delete|")) {
                manager.delete(command);

                } else if (command.startsWith("find|")) {
                   manager.find(command);

                } else if (command.equals("list")) {
                    manager.list(command);

                } else if (command.startsWith("update|")) {
                   manager.update(command);

                } else if (command.startsWith("drop|")) {
                  manager.drop(command);

                } else if (command.startsWith("connect|")) {
                    manager.connect(command);

                } else if (command.startsWith("clear|")) {
                    manager.clear(command);

                } else {
                   manager.unsupported(command);
                }
            }
        } catch (NullPointerException e) {

        }
        return null;
    }
}
//    public String getCommand() {
//        Console console = new Console();
//        String command = console.read();


//        try {
//            boolean flag = true;
//            while (flag) {
//                if (command.equals("help")) {
//                    manager.help(command);
//                    getCommand();
//                    flag = false;
//
//                } else if (command.equals("exit")) {
//                   manager.exit();
//                    break;
//
//                } else if (command.startsWith("connect|")) {
//                    manager.connect(command);
//                    break;
//

//                } else {
//                    System.out.println("Please enter existing command or type 'help' for command list:");
//                    getCommand();
//                }
//            }
//        } catch (NullPointerException e) {
//            System.out.println("Goodbye");
//        }
//        return null;
//    }
