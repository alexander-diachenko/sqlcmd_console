import org.junit.Before;
import org.junit.Test;

import sqlcmd.databasemanager.DatabaseManager;
import sqlcmd.databasemanager.JDBCDatabaseManager;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class DatabaseManagerTest {

    DatabaseManager manager = new JDBCDatabaseManager();

    @Before
    public void run() {
        manager.connect("connect|sqlcmd|postgres|123");
    }

    @Test
    public void testGetAllTableNames() {
        String tableNames = manager.list("list");
        assertEquals("[car, user]", tableNames);
    }

    @Test
    public void testFindAllTableDataWithCorrectData() {
        assertEquals("+---------------------------------------+\n" +
                "| id      | name    | color   | age     |\n" +
                "+---------------------------------------+\n" +
                "| 2       | ferrari | red     | 6       |\n" +
                "+---------------------------------------+\n", manager.getTableData("find|car"));
    }

    @Test
    public void testFindAllTableDataWithIncorrectData() {
        assertEquals("", manager.getTableData("find|qwe"));
    }

    @Test
    public void testFindLimitOffsetTableDataWithCorrectData() {
        assertEquals("+---------------------------------------+\n" +
                "| id      | name    | color   | age     |\n" +
                "+---------------------------------------+\n" +
                "| 2       | ferrari | red     | 6       |\n" +
                "+---------------------------------------+\n", manager.getTableData("find|car|1|1"));
    }

    @Test
    public void testFindLimitOffsetTableDataWithIncorrectLimitOffsetData() {
        assertEquals("+-------------------------------+\n" +
                "| id    | name  | color | age   |\n" +
                "+-------------------------------+\n" +
                "+-------------------------------+\n", manager.getTableData("find|car|4|5"));
    }

    @Test
    public void testFindLimitOffsetTableDataWithIncorrectData() {
        assertEquals("", manager.getTableData("find|qwe|1|2"));
    }

//    @Test
//    public void create() {
//        manager.create("create|car|2|ferrari|red|6");
//        assertEquals("+---------------------------------------+\n" +
//                "| id      | name    | color   | age     |\n" +
//                "+---------------------------------------+\n" +
//                "| 2       | ferrari | red     | 6       |\n" +
//                "+---------------------------------------+", manager.getTableData("find|car"));
//        manager.clear("clear|car");
//        System.out.println("y");
//    }

//    @Test
//    public void clear(){
//        manager.clear("clear|car");
//        assertEquals("", manager.getTableData("find|car"));
//    }
}
