package sqlcmd.command;

import java.sql.Connection;

public class Help {

    public void doHelp(){
        System.out.println("'connect|database|user|password'\n" +
                "\t for connect database");
        System.out.println("'exit'\n" +
                "\t for exit program");
        System.out.println("Enter command or 'help' for command list:");
    }

    public void doHelp(Connection connection){

        System.out.println("'connect|database|user|password'\n" +
                "\t for connect database");
        System.out.println("'list'\n" +
                "\t for list all tables");
        System.out.println("'find|tableName'\n" +
                "\t for show all table data");
        System.out.println("'find|tableName|limit|offset'\n" +
                "\t for show part of table data");
        System.out.println("'create|tableName|column1Value|column2Value|...|columnNValue'\n" +
                "\t for create row");
        System.out.println("'update|tableName|primaryKeyColumnName|primaryKeyValue|column1Name|column1NewValue|column2Name|column2NewValue|...|columnNName|columnNNewValue'\n" +
                "\t for update row");
        System.out.println("'delete|tableName|primaryKeyColumnName|primaryKeyValue'\n" +
                "\t for delete row");
        System.out.println("'clear|tableName'\n" +
                "\t for clear all table data");
        System.out.println("'drop|tableName'\n" +
                "\t for delete table");
        System.out.println("'exit'\n" +
                "\t for exit program");
    }
}