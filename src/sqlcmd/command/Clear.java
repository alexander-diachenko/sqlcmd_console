package sqlcmd.command;

import sqlcmd.Console;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Clear {
    public void doClear(Connection connection, String command, Console console) {

        String[] data = command.split("\\|");
        String tableName = data[1];


        if (data.length != 2) {
            System.out.println(String.format("Incorrect command '%s'. Must be 'clear|tableName'", command));
            return;
        }

        System.out.println(String.format("CAUTION! You are trying to delete all data from '%s'. 'y' for delete or 'n' for cancel", tableName));
        String check = console.read();
        if (check.equals("n")) {
            return;
        }

        try (Statement stmt = connection.createStatement()) {

            stmt.executeUpdate("DELETE FROM public." + tableName);

            System.out.println("Successfully deleted");

        } catch (SQLException e) {
            System.out.println(String.format("%s does not exist", tableName));
        }
    }
}