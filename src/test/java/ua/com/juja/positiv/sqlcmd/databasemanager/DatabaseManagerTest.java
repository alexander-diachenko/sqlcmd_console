package ua.com.juja.positiv.sqlcmd.databasemanager;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.positiv.sqlcmd.DatabaseLogin;
import ua.com.juja.positiv.sqlcmd.DatabasePreparation;

import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by POSITIV on 16.09.2015.
 */
public class DatabaseManagerTest {

    //---===ОСТОРОЖНО! Тесты очищают текущую базу!===----

    DatabaseLogin login = new DatabaseLogin();
    DatabasePreparation preparation = new DatabasePreparation();
    DatabaseManager manager = new JDBCDatabaseManager();

    @Before
    public void run() throws SQLException, ClassNotFoundException {
        manager.connect(login.getDatabase(), login.getUser(), login.getPassword());

    }

    @Test
    public void testDelete_WithCorrectData() throws SQLException {
        preparation.run();
        manager.delete("car", "id", "3");

        List<String> tableData = manager.getTableData("car");
        assertEquals("[4, id, name, color, year, " +
                "1, ferrari, red, 2002, " +
                "2, porsche, black, 1964]", tableData.toString());
    }

    @Test(expected = SQLException.class)
    public void testDelete_WithIncorrectData_TableName() throws SQLException {
        manager.delete("qwe", "id", "3");
    }

    @Test
    public void testGetTableNames() throws Exception {
        preparation.run();
        Set<String> tableNames = manager.getTableNames();
        assertEquals("[car, client]", tableNames.toString());
    }

    @Test
    public void testFind_WithCorrectData() throws SQLException {
        preparation.run();
        List<String> tableData = manager.getTableData("car");
        assertEquals("[4, id, name, color, year, " +
                "1, ferrari, red, 2002, " +
                "2, porsche, black, 1964, " +
                "3, bmw, blue, 2001]", tableData.toString());
    }

    @Test(expected = SQLException.class)
    public void testFind_WithIncorrectData_TableName() throws SQLException {
        manager.getTableData("qwe");
    }

    @Test
    public void testFindLimitOffset_WithCorrectData() throws SQLException {
        preparation.run();
        List<String> tableData = manager.getTableData("car LIMIT 2 OFFSET 1");
        assertEquals("[4, id, name, color, year, " +
                "2, porsche, black, 1964, " +
                "3, bmw, blue, 2001]", tableData.toString());
    }

    @Test
    public void testUpdateAll_WithCorrectData() throws SQLException {
        preparation.run();
        Map<String, Object> columnData = new LinkedHashMap<>();
        columnData.put("name", "mercedes");
        columnData.put("color", "white");
        columnData.put("year", "2008");
        manager.update("car", "id", "3", columnData);

        List<String> tableData = manager.getTableData("car");
        assertEquals("[4, id, name, color, year, " +
                "1, ferrari, red, 2002, " +
                "2, porsche, black, 1964, " +
                "3, mercedes, white, 2008]", tableData.toString());
    }

    @Test
    public void testUpdateSingle_WithCorrectData() throws SQLException {
        preparation.run();
        Map<String, Object> columnData = new LinkedHashMap<>();
        columnData.put("name", "mercedes");
        manager.update("car", "id", "3", columnData);

        List<String> tableData = manager.getTableData("car");
        assertEquals("[4, id, name, color, year, " +
                "1, ferrari, red, 2002, " +
                "2, porsche, black, 1964, " +
                "3, mercedes, blue, 2001]", tableData.toString());
    }

    @Test(expected = SQLException.class)
    public void testUpdate_WithIncorrectData_TableName() throws SQLException {
        Map<String, Object> columnData = new LinkedHashMap<>();
        columnData.put("name", "mercedes");
        manager.update("qwe", "id", "3", columnData);
    }

    @Test
    public void testClear_WithCorrectData() throws SQLException {
        preparation.run();
        manager.clear("car");

        List<String> tableData = manager.getTableData("car");
        assertEquals("[4, id, name, color, year]", tableData.toString());
    }

    @Test(expected = SQLException.class)
    public void testClear_WithIncorrectData_TableName() throws SQLException {
        manager.clear("qwe");
    }

    @Test
    public void testCreateAll_WithCorrectData() throws SQLException {
        preparation.run();
        Map<String, Object> data = new HashMap<>();
        data.put("id", "4");
        data.put("name", "ferrari");
        data.put("color", "red");
        data.put("year", "6");
        manager.create("car", data);

        List<String> tableData = manager.getTableData("car");
        assertEquals("[4, id, name, color, year, " +
                "1, ferrari, red, 2002, " +
                "2, porsche, black, 1964, " +
                "3, bmw, blue, 2001, " +
                "4, ferrari, red, 6]", tableData.toString());
    }

    @Test
    public void testCreateSingle_WithCorrectData() throws SQLException {
        preparation.run();
        Map<String, Object> data = new HashMap<>();
        data.put("id", "4");
        manager.create("car", data);

        List<String> tableData = manager.getTableData("car");
        assertEquals("[4, id, name, color, year, " +
                "1, ferrari, red, 2002, " +
                "2, porsche, black, 1964, " +
                "3, bmw, blue, 2001, " +
                "4, , , ]", tableData.toString());
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testCreate_WithIncorrectData_Length() throws SQLException {
        Map<String, Object> map = new HashMap<>();
        manager.create("qwe", map);
    }

    @Test(expected = SQLException.class)
    public void testCreate_WithIncorrectData_TableName() throws SQLException {
        Map<String, Object> data = new HashMap<>();
        data.put("id", "2");
        manager.create("qwe", data);
    }

    @Test
    public void testTable_WithCorrectData() throws SQLException {
        preparation.run();
        Map<String, Object> data = new HashMap<>();
        data.put("name", "text");
        data.put("population", "int");
        data.put("county", "text");
        manager.table("city", "id", data);

        Set<String> tableNames = manager.getTableNames();
        assertEquals("[car, city, client]", tableNames.toString());
        manager.drop("city");
    }

    @Test(expected = SQLException.class)
    public void testTable_WithIncorrectData_Type() throws SQLException {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "");
        manager.table("city", "id", data);
    }
}
