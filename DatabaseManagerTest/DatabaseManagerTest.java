import org.junit.Before;
import org.junit.Test;
import sqlcmd.databasemanager.DatabaseManager;
import sqlcmd.databasemanager.JDBCDatabaseManager;
import static org.junit.Assert.assertEquals;

public class DatabaseManagerTest {

    DatabaseManager manager = new JDBCDatabaseManager();

    @Before
    public void run() {
        manager.connect("connect|sqlcmd|postgres|rffynbg");
        manager.clear("clear|car");
        manager.create("create|car|2|ferrari|red|6");
    }

    @Test
    public void testGetAllTableNames() {

        String tableNames = manager.getTableNames("list");
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
    public void testFindAllTableDataWithIncorrectData1() {

        assertEquals("Таблицы 'qwe' не существует\n", manager.getTableData("find|qwe"));
    }

    @Test
    public void testFindAllTableDataWithIncorrectData2() {

        assertEquals("Неправильная команда 'find|qwe|dfdg'. Должно быть 'find|tableName' или 'find|tableName|limit|offset'", manager.getTableData("find|qwe|dfdg"));
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

        assertEquals("Таблицы 'qwe' не существует\n", manager.getTableData("find|qwe|1|2"));
    }

    @Test
    public void testClearWithCorrectData() {

        manager.clear("clear|car");
        assertEquals("+-------------------------------+\n" +
                "| id    | name  | color | age   |\n" +
                "+-------------------------------+\n" +
                "+-------------------------------+\n", manager.getTableData("find|car"));
    }

    @Test
    public void testClearWithIncorrectData1() {

        manager.clear("clear|");
        assertEquals("+---------------------------------------+\n" +
                "| id      | name    | color   | age     |\n" +
                "+---------------------------------------+\n" +
                "| 2       | ferrari | red     | 6       |\n" +
                "+---------------------------------------+\n", manager.getTableData("find|car"));
    }

    @Test
    public void testClearWithIncorrectData2() {

        manager.clear("clear|qwe");
        assertEquals("+---------------------------------------+\n" +
                "| id      | name    | color   | age     |\n" +
                "+---------------------------------------+\n" +
                "| 2       | ferrari | red     | 6       |\n" +
                "+---------------------------------------+\n", manager.getTableData("find|car"));
    }

    @Test
    public void testCreateWithCorrectData() {

        manager.clear("clear|car");
        manager.create("create|car|2|ferrari|red|6");
        assertEquals("+---------------------------------------+\n" +
                "| id      | name    | color   | age     |\n" +
                "+---------------------------------------+\n" +
                "| 2       | ferrari | red     | 6       |\n" +
                "+---------------------------------------+\n", manager.getTableData("find|car"));
    }

    @Test
    public void testCreateWithIncorrectData() {

        manager.clear("clear|car");
        manager.create("create|car|bv|fgg|red|6");
        assertEquals("+-------------------------------+\n" +
                "| id    | name  | color | age   |\n" +
                "+-------------------------------+\n" +
                "+-------------------------------+\n", manager.getTableData("find|car"));
    }

    @Test
    public void testCreateWithIncorrectNumberData1() {

        manager.clear("clear|car");
        manager.create("create|car|vfvxv");
        assertEquals("+-------------------------------+\n" +
                "| id    | name  | color | age   |\n" +
                "+-------------------------------+\n" +
                "+-------------------------------+\n", manager.getTableData("find|car"));
    }

    @Test
    public void testCreateWithIncorrectNumberData2() {

        manager.create("create|");
        assertEquals("+---------------------------------------+\n" +
                "| id      | name    | color   | age     |\n" +
                "+---------------------------------------+\n" +
                "| 2       | ferrari | red     | 6       |\n" +
                "+---------------------------------------+\n", manager.getTableData("find|car"));
    }


    @Test
    public void testUpdateWithCorrectData() {

        manager.update("uodate|car|id|2|name|lamborghini|color|yellow|age|3");
        assertEquals("+-------------------------------------------------------+\n" +
                "| id          | name        | color       | age         |\n" +
                "+-------------------------------------------------------+\n" +
                "| 2           | lamborghini | yellow      | 3           |\n" +
                "+-------------------------------------------------------+\n", manager.getTableData("find|car"));
    }

    @Test
    public void testUpdateWithIncorrectData() {

        manager.update("update|car|id|2|");
        assertEquals("+---------------------------------------+\n" +
                "| id      | name    | color   | age     |\n" +
                "+---------------------------------------+\n" +
                "| 2       | ferrari | red     | 6       |\n" +
                "+---------------------------------------+\n", manager.getTableData("find|car"));
    }

    @Test
    public void testUpdateWithIncorrectNumberData() {

        manager.update("uodate|car|id|2|fgxf|lamborghini|color|yellow|age|3");
        assertEquals("+---------------------------------------+\n" +
                "| id      | name    | color   | age     |\n" +
                "+---------------------------------------+\n" +
                "| 2       | ferrari | red     | 6       |\n" +
                "+---------------------------------------+\n", manager.getTableData("find|car"));
    }

    @Test
    public void testDeleteWithCorrectData() {

        manager.delete("delete|car|id|2");
        assertEquals("+-------------------------------+\n" +
                "| id    | name  | color | age   |\n" +
                "+-------------------------------+\n" +
                "+-------------------------------+\n", manager.getTableData("find|car"));
    }

    @Test
    public void testDeleteWithIncorrectData() {

        manager.delete("delete|car|id|3");
        assertEquals("+---------------------------------------+\n" +
                "| id      | name    | color   | age     |\n" +
                "+---------------------------------------+\n" +
                "| 2       | ferrari | red     | 6       |\n" +
                "+---------------------------------------+\n", manager.getTableData("find|car"));
    }

    @Test
    public void testDeleteWithIncorrectNumberData() {

        manager.delete("delete|car|id");
        assertEquals("+---------------------------------------+\n" +
                "| id      | name    | color   | age     |\n" +
                "+---------------------------------------+\n" +
                "| 2       | ferrari | red     | 6       |\n" +
                "+---------------------------------------+\n", manager.getTableData("find|car"));
    }

    @Test
    public void testDeleteWithIncorrectTableName() {

        manager.delete("delete|qwe|id|2");
        assertEquals("+---------------------------------------+\n" +
                "| id      | name    | color   | age     |\n" +
                "+---------------------------------------+\n" +
                "| 2       | ferrari | red     | 6       |\n" +
                "+---------------------------------------+\n", manager.getTableData("find|car"));
    }

    @Test
    public void testConnectWithCorrectData() {

        manager.connect("connect|sqlcmd|postgres|rffynbg");
        assertEquals("+---------------------------------------+\n" +
                "| id      | name    | color   | age     |\n" +
                "+---------------------------------------+\n" +
                "| 2       | ferrari | red     | 6       |\n" +
                "+---------------------------------------+\n", manager.getTableData("find|car"));
    }

    @Test
    public void testConnectWithIncorrectData() {

        manager.connect("connect|sqlcmd|postgres|xfdv");
        assertEquals("Вы не подключились к базе данных", manager.getTableData("find|car"));
    }

    @Test
    public void testConnectWithIncorrectNumberData() {

        manager.connect("connect|sqlcmd|postgres");
        assertEquals("+---------------------------------------+\n" +
                "| id      | name    | color   | age     |\n" +
                "+---------------------------------------+\n" +
                "| 2       | ferrari | red     | 6       |\n" +
                "+---------------------------------------+\n", manager.getTableData("find|car"));
    }

    @Test
    public void testUnsupported() {
        manager.unsupported("qwe");
        assertEquals("+---------------------------------------+\n" +
                "| id      | name    | color   | age     |\n" +
                "+---------------------------------------+\n" +
                "| 2       | ferrari | red     | 6       |\n" +
                "+---------------------------------------+\n", manager.getTableData("find|car"));
    }

    @Test
    public void testHelp() {
        manager.help();
        assertEquals("+---------------------------------------+\n" +
                "| id      | name    | color   | age     |\n" +
                "+---------------------------------------+\n" +
                "| 2       | ferrari | red     | 6       |\n" +
                "+---------------------------------------+\n", manager.getTableData("find|car"));
    }
}
