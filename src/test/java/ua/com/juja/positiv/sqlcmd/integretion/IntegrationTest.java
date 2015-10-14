package ua.com.juja.positiv.sqlcmd.integretion;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.juja.positiv.sqlcmd.databasemanager.DatabaseManager;
import ua.com.juja.positiv.sqlcmd.databasemanager.JDBCDatabaseManager;
import ua.com.juja.positiv.sqlcmd.main.Main;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by POSITIV on 16.09.2015.
 */
public class IntegrationTest {

    private static final String CONNECT_DATABASE_DATA = "connect|sqlcmd|postgres|postgres";
    private static ConfigurableInputStream in;
    private static ByteArrayOutputStream out;

    @BeforeClass
    public static void setup() {
        in = new ConfigurableInputStream();
        out = new ByteArrayOutputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    @Before
    public void run() throws IOException {
        in.reset();
    }

    private void cleanDatabase() {
        try {
            DatabaseManager manager = new JDBCDatabaseManager();
            manager.connect("sqlcmd", "postgres", "postgres");

            Set<String> tables = manager.getTableNames();
            for (String table : tables) {
                manager.drop(table);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        in.add(CONNECT_DATABASE_DATA);
        in.add("table|car|id|name|text|color|text|year|int");
        in.add("create|car|id|1|name|ferrari|color|red|year|2002");
        in.add("create|car|id|2|name|porsche|color|black|year|1964");
        in.add("create|car|id|3|name|bmw|color|blue|year|2001");

        in.add("table|client|id");
        in.add("exit");

        Main.main(new String[0]);

        try {
            in.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.reset();
    }

    public String getData() {
        try {
            String result = new String(out.toByteArray(), "UTF-8");
            out.reset();
            return result;
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }

    @Test
    public void testConnect_WithIncorrectData() {
        in.add("connect|qwe|qwe|qwe");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect qwe
                "Не удалось подключиться к базе 'qwe' по причине: " +
                "FATAL: password authentication failed for user \"qwe\"\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testConnect_WithIncorrectData_Length() {
        in.add("connect|");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect|
                "Неправильная команда 'connect|'. " +
                "Должно быть 'connect|database|user|password'.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testExit() {
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testHelp() {
        in.add("help");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                "connect|database|user|password\n" +
                "\t подключение к базе\r\n" +
                "list\n" +
                "\t вывод списка всех таблиц\r\n" +
                "createbase|databaseName\n" +
                "\t создание базы\r\n" +
                "table|tableName|primaryKeyName|column1Name|column1Type|...|columnNName|columnNType\n" +
                "\t создание таблицы\r\n" +
                "find|tableName\n" +
                "\t вывод всей таблицы\r\n" +
                "find|tableName|limit|offset\n" +
                "\t вывод части таблицы\r\n" +
                "create|tableName|column1Name|column1Value|...|columnNName|columnNValue\n" +
                "\t создание поля\r\n" +
                "update|tableName|primaryKeyColumnName|primaryKeyValue|column1Name|column1NewValue|...|columnNName|columnNNewValue\n" +
                "\t обновление поля\r\n" +
                "delete|tableName|primaryKeyColumnName|primaryKeyValue\n" +
                "\t удаление поля\r\n" +
                "clear|tableName\n" +
                "\t очистка таблицы\r\n" +
                "drop|tableName\n" +
                "\t удаление таблицы\r\n" +
                "dropbase|databaseName\n" +
                "\t удаление базы\r\n" +
                "exit\n" +
                "\t выход из програмы\r\n" +
                "Введите команду или help для помощи:\r\n" +
                "До свидания!\r\n", getData());
    }

    @Test
    public void testList_WithoutConnect() {
        in.add("list");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //list
                "Вы не можете пользоваться командой 'list' без подключения к базе.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testFind_WithCorrectData() {
        cleanDatabase();
        in.add(CONNECT_DATABASE_DATA);
        in.add("find|car");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //find|car
                "+---------------------------------------+\n" +
                "| id      | name    | color   | year    |\n" +
                "+---------------------------------------+\n" +
                "| 1       | ferrari | red     | 2002    |\n" +
                "| 2       | porsche | black   | 1964    |\n" +
                "| 3       | bmw     | blue    | 2001    |\n" +
                "+---------------------------------------+\n" +
                "\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testFind_WithIncorrectData_TableName() {
        in.add(CONNECT_DATABASE_DATA);
        in.add("find|qwe");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //find|qwe
                "Не удалось отобразить таблицу 'qwe' по причине:" +
                " ERROR: relation \"qwe\" does not exist\n" +
                "  Позиция: 15\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testFind_WithIncorrectData_Length() {
        in.add(CONNECT_DATABASE_DATA);
        in.add("find|");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //find|
                "Неправильная команда 'find|'. " +
                "Должно быть 'find|tableName' или 'find|tableName|limit|offset'\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testFindLimitOffset_WithCorrectData() {
        cleanDatabase();
        in.add(CONNECT_DATABASE_DATA);
        in.add("find|car|1|1");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //find|car|1|1
                "+---------------------------------------+\n" +
                "| id      | name    | color   | year    |\n" +
                "+---------------------------------------+\n" +
                "| 2       | porsche | black   | 1964    |\n" +
                "+---------------------------------------+\n" +
                "\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testFindLimitOffset_WithIncorrectData_LimitOffsetType() {
        in.add(CONNECT_DATABASE_DATA);
        in.add("find|car|qwe|qwe");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //find|car|qwe|qwe
                "Неправильные данные. limit и offset должны быть целыми числами.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testTable_WithIncorrectData_Type() {
        in.add(CONNECT_DATABASE_DATA);
        in.add("table|test|id|name|population");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //table
                "Не удалось создать таблицу 'test' по причине: " +
                "ERROR: type \"population\" does not exist\n" +
                "  Позиция: 54\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testTable_WithIncorrectData_Length() {
        in.add(CONNECT_DATABASE_DATA);
        in.add("table|");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //table|
                "Неправильная команда 'table|'. Должно быть 'table|tableName|" +
                "primaryKeyName|column1Name|column1Type|...|columnNName|columnNType'\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testDrop_WithCorrectData() {
        cleanDatabase();
        in.add(CONNECT_DATABASE_DATA);
        in.add("table|test|id");
        in.add("drop|test");
        in.add("test");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //table|test
                "Таблица 'test' успешно создана.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //drop|test
                "ВНИМАНИЕ! Вы собираетесь удалить таблицу 'test'. " +
                "Введите название таблицы для подтверждения.\r\n" +
                //test
                "Таблица 'test' успешно удалена.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testDrop_WithIncorrectData_TableName() {
        in.add(CONNECT_DATABASE_DATA);
        in.add("drop|qwe");
        in.add("qwe");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //drop|qwe
                "ВНИМАНИЕ! Вы собираетесь удалить таблицу 'qwe'. " +
                "Введите название таблицы для подтверждения.\r\n" +
                //qwe
                "Не удалочь удалить таблицу 'qwe' по причине: " +
                "ERROR: table \"qwe\" does not exist\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testDrop_WithIncorrectData_Length() {
        in.add(CONNECT_DATABASE_DATA);
        in.add("drop|qwe|qwe");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //drop|qwe|qwe
                "Неправильные данные 'drop|qwe|qwe'. " +
                "Должно быть 'drop|tableName'.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testTable_WithIncorrectData_Confirm() {
        in.add(CONNECT_DATABASE_DATA);
        in.add("drop|car");
        in.add("qwe");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //drop|car
                "ВНИМАНИЕ! Вы собираетесь удалить таблицу 'car'. " +
                "Введите название таблицы для подтверждения.\r\n" +
                //qwe
                "Удаление отменено.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testCreate_WithCorrectData() {
        cleanDatabase();
        in.add(CONNECT_DATABASE_DATA);
        in.add("create|car|id|4|name|mercedes|color|white|year|2015");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //create
                "Запись успешно создана.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testCreate_WithIncorrectData_Length() {
        in.add(CONNECT_DATABASE_DATA);
        in.add("create|car");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //create|car
                "Неправильные данные 'create|car'. " +
                "Должно быть 'create|tableName|column1VName|column1Value|...|" +
                "columnNName|columnNValue'.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testCreate_WithIncorrectData_Key() {
        in.add(CONNECT_DATABASE_DATA);
        in.add("create|car|qwe|4|name|mercedes|color|white|year|2015");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //create|car|qwe
                "Не удалось создать поле по причине: " +
                "ERROR: column \"qwe\" of relation \"car\" does not exist\n" +
                "  Позиция: 37\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testDelete_WithCorrectData() {
        cleanDatabase();
        in.add(CONNECT_DATABASE_DATA);
        in.add("delete|car|id|1");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //delete
                "Успешно удалено.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testDelete_WithIncorrectData_Length() {
        in.add(CONNECT_DATABASE_DATA);
        in.add("delete|car");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //delete|car
                "Неправильная команда 'delete|car'. " +
                "Должно быть 'delete|tableName|primaryKeyColumnName|primaryKeyValue'.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testDelete_WithIncorrectData_TableName() {
        in.add(CONNECT_DATABASE_DATA);
        in.add("delete|qwe|id|1");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //delete|qwe|id|1
                "Не удалось удалить поле по причине: ERROR: relation \"qwe\" does not exist\n" +
                "  Позиция: 13\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testUnsupported() {
        in.add(CONNECT_DATABASE_DATA);
        in.add("qwe");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //qwe
                "Команды 'qwe' не существует.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testClear_WithCorrectData() {
        in.add(CONNECT_DATABASE_DATA);
        in.add("clear|car");
        in.add("car");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //clear|car
                "ВНИМАНИЕ! Вы собираетесь удалить все данные с таблицы 'car'. " +
                "Введите название таблицы для подтверждения.\r\n" +
                //car
                "Таблица 'car' успешно очищена.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testClear_WithIncorrectData_TableName() {
        in.add(CONNECT_DATABASE_DATA);
        in.add("clear|qwe");
        in.add("qwe");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //clear|qwe
                "ВНИМАНИЕ! Вы собираетесь удалить все данные с таблицы 'qwe'. " +
                "Введите название таблицы для подтверждения.\r\n" +
                //qwe
                "Не удалось очистить таблицу 'qwe' по причине: " +
                "ERROR: relation \"qwe\" does not exist\n" +
                "  Позиция: 13\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testClear_WithIncorrectData_Length() {
        in.add(CONNECT_DATABASE_DATA);
        in.add("clear|");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //clear|
                "Неправильная команда 'clear|'. " +
                "Должно быть 'clear|tableName'.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testClear_WithIncorrectData_Confirm() {
        in.add(CONNECT_DATABASE_DATA);
        in.add("clear|car");
        in.add("qwe");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //clear|car
                "ВНИМАНИЕ! Вы собираетесь удалить все данные с таблицы 'car'. " +
                "Введите название таблицы для подтверждения.\r\n" +
                //qwe
                "Очистка отменена.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testUpdate_WithCorrectData() {
        cleanDatabase();
        in.add(CONNECT_DATABASE_DATA);
        in.add("update|car|id|1|name|mercedes");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //update
                "Все данные успешно обновлены.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testUpdate_WithIncorrectData_Length() {
        in.add(CONNECT_DATABASE_DATA);
        in.add("update|");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //update|
                "Неправильные данные 'update|'. " +
                "Должно быть 'update|tableName|primaryKeyColumnName|" +
                "primaryKeyValue|column1Name|column1NewValue|" +
                "...|columnNName|columnNNewValue'.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testUpdate_WithIncorrectData_TableName() {
        in.add(CONNECT_DATABASE_DATA);
        in.add("update|qwe|qwe|qwe|qwe|qwe");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //update|qwe|qwe|qwe|qwe|qwe
                "Не удалось обновить по причине " +
                "ERROR: relation \"qwe\" does not exist\n" +
                "  Позиция: 8\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testCreateDropBase_WithCorrectData() {
        in.add(CONNECT_DATABASE_DATA);
        in.add("createbase|test123321");
        in.add("dropbase|test123321");
        in.add("test123321");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //createbase
                "База 'test123321' успешно создана.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //dropbase
                "ВНИМАНИЕ! Вы собираетесь удалить базу 'test123321'. Введите название базы для подтверждения.\r\n" +
                "База 'test123321' успешно удалена.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testCreateBase_WithIncorrectData_Length() {
        in.add(CONNECT_DATABASE_DATA);
        in.add("createbase|");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //createbase|
                "Неправильная команда 'createbase|'. " +
                "Должно быть 'createBase|databaseName'.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testCreateBase_WithIncorrectData_DatabaseName() {
        in.add(CONNECT_DATABASE_DATA);
        in.add("createbase|sqlcmd");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //createbase|sqlcmd
                "Не удалось создать базу 'sqlcmd' по причине: " +
                "ERROR: database \"sqlcmd\" already exists\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testDropBase_WithIncorrectData_Length() {
        in.add(CONNECT_DATABASE_DATA);
        in.add("dropbase|");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //dropbase|
                "Неправильная команда 'dropbase|'. " +
                "Должно быть 'dropBase|databaseName'.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testDropBase_WithIncorrectData_DatabaseName() {
        in.add(CONNECT_DATABASE_DATA);
        in.add("dropbase|qwe");
        in.add("qwe");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //dropbase|qwe
                "ВНИМАНИЕ! Вы собираетесь удалить базу 'qwe'. " +
                "Введите название базы для подтверждения.\r\n" +
                //qwe
                "Не удалось удалить базу 'qwe' по причине: " +
                "ERROR: database \"qwe\" does not exist\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testDropBase_WithIncorrectData_Confirm() {
        in.add(CONNECT_DATABASE_DATA);
        in.add("createbase|test123321");
        in.add("dropbase|test123321");
        in.add("qwe");
        in.add("dropbase|test123321");
        in.add("test123321");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //createbase
                "База 'test123321' успешно создана.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //dropbase
                "ВНИМАНИЕ! Вы собираетесь удалить базу 'test123321'. Введите название базы для подтверждения.\r\n" +
                //qwe
                "Удаление отменено.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //dropbase
                "ВНИМАНИЕ! Вы собираетесь удалить базу 'test123321'. Введите название базы для подтверждения.\r\n" +
                //test123321
                "База 'test123321' успешно удалена.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }
}
