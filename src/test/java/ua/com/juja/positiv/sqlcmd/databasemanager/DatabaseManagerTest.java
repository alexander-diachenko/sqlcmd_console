package ua.com.juja.positiv.sqlcmd.databasemanager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by POSITIV on 16.09.2015.
 */
public class DatabaseManagerTest {

    DatabaseManager manager = new JDBCDatabaseManager();

    @Before
    public void run() throws SQLException, ClassNotFoundException {
        manager.connect("sqlcmd", "postgres", "123");

        Map<String, Object> tableCar = new LinkedHashMap<>();
        tableCar.put("name", "text");
        tableCar.put("color", "text");
        tableCar.put("age", "int");
        manager.table("car", "id", tableCar);

        Map<String, Object> field1 = new HashMap<>();
        field1.put("id", 1);
        field1.put("name", "ferrari");
        field1.put("color", "red");
        field1.put("age", 6);
        manager.create("car", field1);

        Map<String, Object> field2 = new HashMap<>();
        field2.put("id", 2);
        field2.put("name", "porsche");
        field2.put("color", "black");
        field2.put("age", 1);
        manager.create("car", field2);

        Map<String, Object> field3 = new HashMap<>();
        field3.put("id", 3);
        field3.put("name", "bmw");
        field3.put("color", "blue");
        field3.put("age", 3);
        manager.create("car", field3);

        Map<String, Object> tableClient = new LinkedHashMap<>();
        manager.table("client", "id", tableClient);
    }

    @Test
    public void testDeleteWithCorrectData() throws SQLException {
        manager.delete("car", "id", "3");

        List<String> tableData = manager.getTableData("car");
        assertEquals("[4, id, name, color, age, " +
                         "1, ferrari, red, 6, " +
                         "2, porsche, black, 1]", tableData.toString());
    }

    @Test(expected = SQLException.class)
    public void testDeleteWithIncorrectData() throws SQLException {
        manager.delete("qwe", "id", "3");
    }

    @Test
    public void testGetTableNames() throws Exception {
        List<String> tableNames = manager.getTableNames();
        assertEquals("[car, client]", tableNames.toString());
    }

    @Test
    public void testFindAllTableDataWithCorrectData() throws SQLException {
        List<String> tableData = manager.getTableData("car");
        assertEquals("[4, id, name, color, age, " +
                         "1, ferrari, red, 6, " +
                         "2, porsche, black, 1, " +
                         "3, bmw, blue, 3]", tableData.toString());
    }

    @Test(expected = SQLException.class)
    public void testFindAllTableDataWithIncorrectData() throws SQLException {
        manager.getTableData("qwe");
    }

    @Test
    public void testFindLimitOffsetTableDataWithCorrectData() throws SQLException {
        List<String> tableData = manager.getTableData("car LIMIT 2 OFFSET 1");
        assertEquals("[4, id, name, color, age, " +
                         "2, porsche, black, " +
                         "1, 3, bmw, blue, 3]", tableData.toString());
    }

    @Test
    public void testUpdateAllWithCorrectData() throws SQLException {
        manager.update("car", "id", "3", new String[]{"name", "mercedes"});
        manager.update("car", "id", "3", new String[]{"color", "white"});
        manager.update("car", "id", "3", new String[]{"age", "10"});


        List<String> tableData = manager.getTableData("car");
        assertEquals("[4, id, name, color, age, " +
                         "1, ferrari, red, 6, " +
                         "2, porsche, black, 1, " +
                         "3, mercedes, white, 10]", tableData.toString());
    }

    @Test
    public void testUpdateSingleWithCorrectData() throws SQLException {
        manager.update("car", "id" , "3", new String[]{"name", "mercedes"});

        List<String> tableData = manager.getTableData("car");
        assertEquals("[4, id, name, color, age, " +
                         "1, ferrari, red, 6, " +
                         "2, porsche, black, 1, " +
                         "3, mercedes, blue, 3]", tableData.toString());
    }

    @Test(expected = SQLException.class)
        public void testUpdateWithIncorrectData() throws SQLException {
        manager.update("qwe", "id", "3", new String[]{"name", "mercedes"});
    }

    @Test
    public void testClearWithCorrectData() throws SQLException {
        manager.clear("car");

        List<String> tableData = manager.getTableData("car");
        assertEquals("[4, id, name, color, age]", tableData.toString());
    }

    @Test(expected = SQLException.class)
    public void testClearWithIncorrectData() throws SQLException {
        manager.clear("qwe");
    }

    @Test
    public void testCreateWithAllCorrectData() throws SQLException {
        manager.clear("car");
        Map<String, Object> data = new HashMap<>();
        data.put("id", "1");
        data.put("name", "ferrari");
        data.put("color", "red");
        data.put("age", "6");
        manager.create("car", data);

        List<String> tableData = manager.getTableData("car");
        assertEquals("[4, id, name, color, age, " +
                         "1, ferrari, red, 6]", tableData.toString());
    }

    @Test
    public void testCreateWithSingleCorrectData() throws SQLException {
        manager.clear("car");
        Map<String, Object> data = new HashMap<>();
        data.put("id", "2");
        manager.create("car", data);

        List<String> tableData = manager.getTableData("car");
        assertEquals("[4, id, name, color, age, " +
                         "2, , , ]", tableData.toString());
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testCreateWithIncorrectDataLength() throws SQLException {
        Map<String, Object> map = new HashMap<>();
        manager.create("qwe", map);
    }

    @Test(expected = SQLException.class)
    public void testCreateWithIncorrectData() throws SQLException {
        Map<String, Object> data = new HashMap<>();
        data.put("id", "2");
        manager.create("qwe", data);
    }

    @Test
    public void testTableWithCorrectData() throws SQLException {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "text");
        data.put("population", "int");
        data.put("county", "text");
        manager.table("city", "id", data);

        List<String> tableNames = manager.getTableNames();
        assertEquals("[car, city, client]", tableNames.toString());
        manager.drop("city");
    }

    @Test(expected = SQLException.class)
    public void testTableWithIncorrectData() throws SQLException {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "");
        manager.table("city", "id", data);
    }

    @After
    public void dropTestTables() throws SQLException {
        manager.drop("car");
        manager.drop("client");
    }
}
