package sqlcmd.command;

import java.sql.*;

public class Update {

    public void doUpdate(Connection connection, String command) {

        String[] data = command.split("\\|");

        if (data.length < 6 || data.length % 2 == 1) {
            System.out.println(String.format("Incorrect command '%s'. " +
                    "Must be 'update|tableName|primaryKeyColumnName|primaryKeyValue|column1Name|column1NewValue|column2Name|column2NewValue|...|columnNName|columnNNewValue'", command));
            return;
        }

        String tableName = data[1];

        try (Statement stmt = connection.createStatement()){
            for (int index = 5; index < data.length; index += 2) {
                stmt.executeUpdate("UPDATE public." + tableName + " SET " + data[index - 1] + "='" + data[index] + "' WHERE " + data[2] + " =" + data[3]);
            }

        } catch (SQLException e) {
            System.out.println("Incorrect data");
            return;
        }
        System.out.println("Successfully updated");
    }
}