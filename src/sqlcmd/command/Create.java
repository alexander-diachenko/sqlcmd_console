package sqlcmd.command;

import java.sql.*;

public class Create {

    public void doCreate(Connection connection, String command) {

        Find find = new Find();

        String[] data = command.split("\\|");

        if (data.length < 2) {
            System.out.println(String.format("Incorrect command '%s'. Must be 'create|tableName|column1Value|column2Value|...|columnNValue'", command));
            return;
        }

        String tableName = data[1];
        try (Statement stmt = connection.createStatement()) {

            int columnsCount = find.getColumnsCount(tableName, stmt);

            if (data.length != columnsCount + 2) {
                System.out.println(String.format("Incorrect command '%s'. Must be 'create|tableName|column1Value|column2Value|...|columnNValue'", command));
                return;
            }

            String string = "INSERT INTO public." + tableName + " VALUES('";
            for (int index = 2; index < data.length; index++) {
                string += data[index] + "', '";
            }
            String result = string.substring(0, string.length() - 5);
            result += "')";
            stmt.executeUpdate(result);

            System.out.println("Successfully created");

        } catch (SQLException e) {
            System.out.println(String.format("%s does not exist", tableName));
        }
    }

}