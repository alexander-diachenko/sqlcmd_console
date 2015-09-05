package sqlcmd.command;

import java.sql.*;

public class Delete {

    public void doDelete(Connection connection, String command) {

        String[] data = command.split("\\|");

        if (data.length != 4) {
            System.out.println(String.format("Incorrect command '%s'. Must be 'delete|tableName|primaryKeyColumnName|primaryKeyValue'", command));
            return;
        }

        String tableName = data[1];
        String primaryKeyColumnName = data[2];
        String primaryKeyValue = data[3];

        try (Statement stmt = connection.createStatement()) {

            stmt.executeUpdate("DELETE FROM public." + tableName + " WHERE " + primaryKeyColumnName + "=" + primaryKeyValue);

            System.out.println("Successfully deleted");

        } catch (SQLException e) {
            System.out.println(String.format("%s does not exist", tableName));
        }
    }
}
