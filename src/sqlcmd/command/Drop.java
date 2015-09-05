package sqlcmd.command;

import sqlcmd.Console;
import java.sql.*;

public class Drop {

    public void doDrop(Connection connection, String command, Console console){

        String[] data = command.split("\\|");

        if (data.length != 2) {
            System.out.println(String.format("Incorrect command '%s'. Must be 'drop|tableName'", command));
            return;
        }

        String tableName = data[1];

        System.out.println(String.format("CAUTION! You are trying to delete '%s'. 'y' for delete or 'n' for cancel", tableName));
        String check = console.read();
        if(check.equals("n")){
            System.out.println("");
            return;
        }

        try (Statement stmt = connection.createStatement()){

            stmt.executeUpdate("DROP TABLE public." + tableName);

            System.out.println(String.format("'%s' successfully dropped", tableName));

        }catch (SQLException e){
            System.out.println(String.format("%s does not exist", tableName));
        }
    }
}