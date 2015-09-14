import org.junit.Before;
import org.junit.Test;
import sqlcmd.Console;
import sqlcmd.command.Command;
import sqlcmd.command.Find;
import sqlcmd.databasemanager.DatabaseManager;
import sqlcmd.databasemanager.JDBCDatabaseManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class DatabaseManagerTest {

    Console console = new Console();
    DatabaseManager manager = new JDBCDatabaseManager(console);

    @Before
    public void run() throws SQLException {
        manager.connect("sqlcmd", "postgres", "123");
        manager.clear("car");
        manager.create("car", "'1' , 'ferrari' , 'red' ,'6'");
        manager.create("car", "'2' , 'porche' , 'black' ,'1'");
        manager.create("car", "'3' , 'bmw' , 'blue' ,'3'");
    }

    @Test
    public void testGetTableNames() throws Exception {
        String[] tableNames = manager.getTableNames();
        assertEquals("[car, user]", Arrays.toString(tableNames));
    }

    @Test
    public void testFindAllTableDataWithCorrectData() throws SQLException {

        ArrayList<String> list = manager.getTableData("car");
        String tableData = "";
        for(String s : list){
            tableData += s + ",";
        }
        tableData = tableData.substring(0, tableData.length() - 1);

        assertEquals("4,id,name,color,age,3,ferrari,red,6", tableData);
    }

//    @Test
//    public void testFindAllTableDataWithIncorrectData1() throws SQLException {
//
//        ArrayList<String> list = manager.getTableData("qwe");
//        String tableData = "";
//        for(String s : list){
//            tableData += s + ",";
//        }
//        tableData = tableData.substring(0, tableData.length() - 1);
//        assertEquals("", tableData);
//    }
//
//    @Test
//    public void testFindAllTableDataWithIncorrectData2() throws SQLException {
//
//        ArrayList<String> list = manager.getTableData("qwe|vdv");
//        String tableData = "";
//        for(String s : list){
//            tableData += s + ",";
//        }
//        tableData = tableData.substring(0, tableData.length() - 1);
//        assertEquals("", tableData);
//    }
//
    @Test
    public void testFindLimitOffsetTableDataWithCorrectData() throws SQLException {

        ArrayList<String> list = manager.getTableData("car LIMIT 2 OFFSET 1");
        String tableData = "";
        for(String s : list){
            tableData += s + ",";
        }
        tableData = tableData.substring(0, tableData.length() - 1);

        assertEquals("4,id,name,color,age,2,porche,black,1,3,bmw,blue,3", tableData);
    }
//
//    @Test
//    public void testFindLimitOffsetTableDataWithIncorrectLimitOffsetData() {
//
//        assertEquals("+-------------------------------+\n" +
//                "| id    | name  | color | age   |\n" +
//                "+-------------------------------+\n" +
//                "+-------------------------------+\n", manager.getTableData("find|car|4|5"));
//    }
//
//    @Test
//    public void testFindLimitOffsetTableDataWithIncorrectData() {
//
//        assertEquals("Таблицы 'qwe' не существует\n", manager.getTableData("find|qwe|1|2"));
//    }
//
    @Test
    public void testClearWithCorrectData() throws SQLException {

        manager.clear("car");
        assertEquals("4", manager.getTableData("find|car"));
    }
//
//    @Test
//    public void testClearWithIncorrectData1() {
//
//        manager.clear("clear|");
//        assertEquals("+---------------------------------------+\n" +
//                "| id      | name    | color   | age     |\n" +
//                "+---------------------------------------+\n" +
//                "| 2       | ferrari | red     | 6       |\n" +
//                "+---------------------------------------+\n", manager.getTableData("find|car"));
//    }
//
//    @Test
//    public void testClearWithIncorrectData2() {
//
//        manager.clear("clear|qwe");
//        assertEquals("+---------------------------------------+\n" +
//                "| id      | name    | color   | age     |\n" +
//                "+---------------------------------------+\n" +
//                "| 2       | ferrari | red     | 6       |\n" +
//                "+---------------------------------------+\n", manager.getTableData("find|car"));
//    }
//
//    @Test
//    public void testCreateWithCorrectData() {
//
//        manager.clear("clear|car");
//        manager.create("create|car|2|ferrari|red|6");
//        assertEquals("+---------------------------------------+\n" +
//                "| id      | name    | color   | age     |\n" +
//                "+---------------------------------------+\n" +
//                "| 2       | ferrari | red     | 6       |\n" +
//                "+---------------------------------------+\n", manager.getTableData("find|car"));
//    }
//
//    @Test
//    public void testCreateWithIncorrectData() {
//
//        manager.clear("clear|car");
//        manager.create("create|car|bv|fgg|red|6");
//        assertEquals("+-------------------------------+\n" +
//                "| id    | name  | color | age   |\n" +
//                "+-------------------------------+\n" +
//                "+-------------------------------+\n", manager.getTableData("find|car"));
//    }
//
//    @Test
//    public void testCreateWithIncorrectNumberData1() {
//
//        manager.clear("clear|car");
//        manager.create("create|car|vfvxv");
//        assertEquals("+-------------------------------+\n" +
//                "| id    | name  | color | age   |\n" +
//                "+-------------------------------+\n" +
//                "+-------------------------------+\n", manager.getTableData("find|car"));
//    }
//
//    @Test
//    public void testCreateWithIncorrectNumberData2() {
//
//        manager.create("create|");
//        assertEquals("+---------------------------------------+\n" +
//                "| id      | name    | color   | age     |\n" +
//                "+---------------------------------------+\n" +
//                "| 2       | ferrari | red     | 6       |\n" +
//                "+---------------------------------------+\n", manager.getTableData("find|car"));
//    }
//
//
//    @Test
//    public void testUpdateWithCorrectData() {
//
//        manager.update("uodate|car|id|2|name|lamborghini|color|yellow|age|3");
//        assertEquals("+-------------------------------------------------------+\n" +
//                "| id          | name        | color       | age         |\n" +
//                "+-------------------------------------------------------+\n" +
//                "| 2           | lamborghini | yellow      | 3           |\n" +
//                "+-------------------------------------------------------+\n", manager.getTableData("find|car"));
//    }
//
//    @Test
//    public void testUpdateWithIncorrectData() {
//
//        manager.update("update|car|id|2|");
//        assertEquals("+---------------------------------------+\n" +
//                "| id      | name    | color   | age     |\n" +
//                "+---------------------------------------+\n" +
//                "| 2       | ferrari | red     | 6       |\n" +
//                "+---------------------------------------+\n", manager.getTableData("find|car"));
//    }
//
//    @Test
//    public void testUpdateWithIncorrectNumberData() {
//
//        manager.update("uodate|car|id|2|fgxf|lamborghini|color|yellow|age|3");
//        assertEquals("+---------------------------------------+\n" +
//                "| id      | name    | color   | age     |\n" +
//                "+---------------------------------------+\n" +
//                "| 2       | ferrari | red     | 6       |\n" +
//                "+---------------------------------------+\n", manager.getTableData("find|car"));
//    }
//
//    @Test
//    public void testDeleteWithCorrectData() {
//
//        manager.delete("delete|car|id|2");
//        assertEquals("+-------------------------------+\n" +
//                "| id    | name  | color | age   |\n" +
//                "+-------------------------------+\n" +
//                "+-------------------------------+\n", manager.getTableData("find|car"));
//    }
//
//    @Test
//    public void testDeleteWithIncorrectData() {
//
//        manager.delete("delete|car|id|3");
//        assertEquals("+---------------------------------------+\n" +
//                "| id      | name    | color   | age     |\n" +
//                "+---------------------------------------+\n" +
//                "| 2       | ferrari | red     | 6       |\n" +
//                "+---------------------------------------+\n", manager.getTableData("find|car"));
//    }
//
//    @Test
//    public void testDeleteWithIncorrectNumberData() {
//
//        manager.delete("delete|car|id");
//        assertEquals("+---------------------------------------+\n" +
//                "| id      | name    | color   | age     |\n" +
//                "+---------------------------------------+\n" +
//                "| 2       | ferrari | red     | 6       |\n" +
//                "+---------------------------------------+\n", manager.getTableData("find|car"));
//    }
//
//    @Test
//    public void testDeleteWithIncorrectTableName() {
//
//        manager.delete("delete|qwe|id|2");
//        assertEquals("+---------------------------------------+\n" +
//                "| id      | name    | color   | age     |\n" +
//                "+---------------------------------------+\n" +
//                "| 2       | ferrari | red     | 6       |\n" +
//                "+---------------------------------------+\n", manager.getTableData("find|car"));
//    }
}
