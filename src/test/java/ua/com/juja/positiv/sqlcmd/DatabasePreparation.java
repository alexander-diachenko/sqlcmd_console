package ua.com.juja.positiv.sqlcmd;

import ua.com.juja.positiv.sqlcmd.databasemanager.DatabaseManager;
import ua.com.juja.positiv.sqlcmd.databasemanager.JDBCDatabaseManager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by POSITIV on 14.10.2015.
 */
public class DatabasePreparation {

    DatabaseManager manager = new JDBCDatabaseManager();

    public void run() {
        try {
            connectDatabase();
            clearDatabase();
            createFirstTable();
            createSecondTable();
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private void connectDatabase() throws SQLException, ClassNotFoundException {
        manager.connect("sqlcmd", "postgres", "postgres");
    }

    private void createFirstTable() throws SQLException {
        Map<String, Object> tableCar = new LinkedHashMap<>();
        tableCar.put("name", "text");
        tableCar.put("color", "text");
        tableCar.put("year", "int");
        manager.table("car", "id", tableCar);

        Map<String, Object> field1 = new HashMap<>();
        field1.put("id", 1);
        field1.put("name", "ferrari");
        field1.put("color", "red");
        field1.put("year", 2002);
        manager.create("car", field1);

        Map<String, Object> field2 = new HashMap<>();
        field2.put("id", 2);
        field2.put("name", "porsche");
        field2.put("color", "black");
        field2.put("year", 1964);
        manager.create("car", field2);

        Map<String, Object> field3 = new HashMap<>();
        field3.put("id", 3);
        field3.put("name", "bmw");
        field3.put("color", "blue");
        field3.put("year", 2001);
        manager.create("car", field3);
    }

    private void createSecondTable() throws SQLException {
        Map<String, Object> tableClient = new LinkedHashMap<>();
        manager.table("client", "id", tableClient);
    }

    private void clearDatabase() throws SQLException {
        Set<String> tables = manager.getTableNames();
        for (String table : tables) {
            manager.drop(table);
        }
    }
}
