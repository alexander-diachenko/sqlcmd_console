import org.junit.Before;
import org.junit.Test;
import sqlcmd.view.Console;
import sqlcmd.databasemanager.DatabaseManager;
import sqlcmd.databasemanager.JDBCDatabaseManager;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class testDatabaseManager {

    Console console = new Console();
    DatabaseManager manager = new JDBCDatabaseManager(console);

    @Before
    public void run() throws SQLException {
        manager.connect("sqlcmd", "postgres", "123");
        manager.clear("car");
        manager.create("car", "'1' , 'ferrari' , 'red' ,'6'");
        manager.create("car", "'2' , 'porsche' , 'black' ,'1'");
        manager.create("car", "'3' , 'bmw' , 'blue' ,'3'");
    }

    @Test
    public void testDeleteWithCorrectData() throws SQLException {

        manager.delete("car", "id =3");

        ArrayList<String> tableData = manager.getTableData("car");
        assertEquals("[4, id, name, color, age, 1, ferrari, red, 6, 2, porsche, black, 1]", tableData.toString());

        manager.create("car", "'3' , 'bmw' , 'blue' ,'3'");
    }

    @Test
    public void testGetTableNames() throws Exception {
        ArrayList<String> tableNames = manager.getTableNames();
        assertEquals("[car, user]", tableNames.toString());
    }

    @Test
    public void testFindAllTableDataWithCorrectData() throws SQLException {

        ArrayList<String> tableData = manager.getTableData("car");
        assertEquals("[4, id, name, color, age, 1, ferrari, red, 6, 2, porsche, black, 1, 3, bmw, blue, 3]", tableData.toString());
    }

    @Test
    public void testFindLimitOffsetTableDataWithCorrectData() throws SQLException {

        ArrayList<String> tableData = manager.getTableData("car LIMIT 2 OFFSET 1");
        assertEquals("[4, id, name, color, age, 2, porsche, black, 1, 3, bmw, blue, 3]", tableData.toString());
    }

    @Test
    public void testUpdateWithCorrectData() throws SQLException {

        manager.update("car", "name ='mercedes'", "id =3");
        manager.update("car", "color ='white'", "id =3");
        manager.update("car", "age ='10'", "id =3");

        ArrayList<String> tableData = manager.getTableData("car");
        assertEquals("[4, id, name, color, age, 1, ferrari, red, 6," +
                " 2, porsche, black, 1, 3, mercedes, white, 10]", tableData.toString());
    }

    @Test
    public void testClearWithCorrectData() throws SQLException {

        manager.clear("car");

        ArrayList<String> tableData = manager.getTableData("car");
        assertEquals("[4, id, name, color, age]", tableData.toString());
    }

    @Test
    public void testCreateWithCorrectData() throws SQLException {

        manager.clear("car");
        manager.create("car", "'1' , 'ferrari' , 'red' ,'6'");
        manager.create("car", "'2' , 'porsche' , 'black' ,'1'");
        manager.create("car", "'3' , 'bmw' , 'blue' ,'3'");

        ArrayList<String> tableData = manager.getTableData("car");
        assertEquals("[4, id, name, color, age, 1, ferrari, red, 6, 2, porsche, black, 1, 3, bmw, blue, 3]", tableData.toString());
    }
}
