package sqlcmd.command;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    Connection connection;

    public void doConnect(String command) {

    Command commands = new Command();

        String[] data = command.split("\\|");

        if (data.length != 4) {
            System.out.println(String.format("Incorrect command '%s'. Must be 'connect|database|user|password'", command));
            return;
        }

        String database = data[1];
        String user = data[2];
        String password = data[3];

        try {
            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {
            System.out.println("Cant find jdbc jar");
        }

        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/" + database + "", "" + user + "",
                    "" + password + "");

        } catch (SQLException e) {
            System.out.println(String.format("Cant get connection for database:'%s' user:'%s'. ", database, user));
            connection = null;

        }
        if (connection != null) {
            System.out.println("Connection OK");
        }

        try {
            commands.getCommand(connection);

        } catch (NullPointerException e) {

        }
    }

}