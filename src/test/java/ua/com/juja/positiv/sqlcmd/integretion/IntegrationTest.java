package ua.com.juja.positiv.sqlcmd.integretion;


import ua.com.juja.positiv.sqlcmd.databasemanager.DatabaseManager;
import ua.com.juja.positiv.sqlcmd.databasemanager.JDBCDatabaseManager;
import ua.com.juja.positiv.sqlcmd.main.Main;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by POSITIV on 16.09.2015.
 */
public class IntegrationTest {

    private static final String CONNECT_DATABASE_DATA = "connect|sqlcmd|postgres|123";
    private static ConfigurableInputStream in;
    private static ByteArrayOutputStream out;

    DatabaseManager manager = new JDBCDatabaseManager();

    @BeforeClass
    public static void setup() {
        in = new ConfigurableInputStream();
        out = new ByteArrayOutputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    @Before
    public void run() throws IOException, SQLException, ClassNotFoundException {
        in.add(CONNECT_DATABASE_DATA);
        in.add("drop|car");
        in.add("car");
        in.add("drop|client");
        in.add("client");

        in.add("table|car|id|name|text|color|text|age|int");
        in.add("create|car|id|1|name|ferrari|color|red|age|6");
        in.add("create|car|id|2|name|porsche|color|black|age|1");
        in.add("create|car|id|3|name|bmw|color|blue|age|3");

        in.add("table|client|id");
        in.add("exit");

        Main.main(new String[0]);

        in.reset();
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
                //help
                "connect|database|user|password\n" +
                    "\t подключение к базе\r\n" +
                "table|tableName|primaryKeyName|column1Name|column1Type|...|" +
                "columnNName|columnNType\n" +
                   "\t для создания таблицы\r\n" +
                "list\n" +
                   "\t вывод списка всех таблиц\r\n" +
                "find|tableName\n" +
                  "\t вывод всей таблицы\r\n" +
                "find|tableName|limit|offset\n" +
                   "\t вывод части таблицы\r\n" +
                "create|tableName|column1Name|column1Value|..." +
                "|columnNName|columnNValue\n" +
                   "\t создание поля\r\n" +
                "update|tableName|primaryKeyColumnName|primaryKeyValue" +
                "|column1Name|column1NewValue|...|" +
                "columnNName|columnNNewValue\n" +
                  "\t обновление поля\r\n" +
                "delete|tableName|primaryKeyColumnName|primaryKeyValue\n" +
                  "\t удаление поля\r\n" +
                "clear|tableName\n" +
                  "\t очистка таблицы\r\n" +
                "drop|tableName\n" +
                   "\t удаление таблицы\r\n" +
                "exit\n" +
                  "\t выход из програмы\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
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
    public void testList_WithConnect() {
        in.add(CONNECT_DATABASE_DATA);
        in.add("list");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //list
                "[car, client]\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testFind_WithCorrectData() {
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
                "| id      | name    | color   | age     |\n" +
                "+---------------------------------------+\n" +
                "| 1       | ferrari | red     | 6       |\n" +
                "| 2       | porsche | black   | 1       |\n" +
                "| 3       | bmw     | blue    | 3       |\n" +
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
                "| id      | name    | color   | age     |\n" +
                "+---------------------------------------+\n" +
                "| 2       | porsche | black   | 1       |\n" +
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
    public void testTable_WithCorrectData() {
        in.add(CONNECT_DATABASE_DATA);
        in.add("table|test|id|name|text|population|int");
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
                "Таблица 'test' успешно создана\r\n" +
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
                "Неправильная команда 'table|'. 'table|tableName|" +
                "primaryKeyName|column1Name|column1Type|...|columnNName|columnNType'\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testDrop_WithCorrectData() {
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
                "Таблица 'test' успешно создана\r\n" +
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
        in.add(CONNECT_DATABASE_DATA);
        in.add("create|car|id|4|name|mercedes|color|white|age|5");
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
        in.add("create|car|qwe|4|name|mercedes|color|white|age|5");
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
                "  Позиция: 29\r\n" +
                "Введите команду или help для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testDelete_WithCorrectData() {
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
}
