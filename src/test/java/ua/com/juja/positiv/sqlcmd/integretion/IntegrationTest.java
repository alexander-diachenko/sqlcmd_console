package ua.com.juja.positiv.sqlcmd.integretion;

import ua.com.juja.positiv.sqlcmd.main.Main;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by POSITIV on 16.09.2015.
 */
public class IntegrationTest {

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
    public void testConnectWithIncorrectData() {
        in.add("connect|sqlcmd|postgres|qwe");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //connect
                "Не удалось подключиться к базе 'sqlcmd' по причине: " +
                "FATAL: password authentication failed for user \"postgres\"\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testConnectWithIncorrectDataLength() {
        in.add("connect|");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //connect
                "Неправильная команда 'connect|'. " +
                "Должно быть 'connect|database|user|password'.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testExit() {
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testHelp() {
        in.add("help");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //help
                "'connect|database|user|password'\n" +
                    "\t подключение к базе\r\n" +
                "'table|tableName|primaryKeyName|column1Name|column1Type|...|" +
                "columnNName|columnNType'\n" +
                   "\t для создания таблицы\r\n" +
                "'list'\n" +
                   "\t вывод списка всех таблиц\r\n" +
                "'find|tableName'\n" +
                  "\t вывод всей таблицы\r\n" +
                "'find|tableName|limit|offset'\n" +
                   "\t вывод части таблицы\r\n" +
                "'create|tableName|column1Value|...|columnNValue'\n" +
                   "\t создание поля\r\n" +
                "'update|tableName|primaryKeyColumnName|primaryKeyValue" +
                "|column1Name|column1NewValue|...|" +
                "columnNName|columnNNewValue'\n" +
                  "\t обновление поля\r\n" +
                "'delete|tableName|primaryKeyColumnName|primaryKeyValue'\n" +
                  "\t удаление поле\r\n" +
                "'clear|tableName'\n" +
                  "\t очистка таблицы\r\n" +
                "'drop|tableName'\n" +
                   "\t удаление таблицы\r\n" +
                "'exit'\n" +
                  "\t выход из програмы\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testListWithoutConnect() {
        in.add("list");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //list
                "Вы не можете пользоваться командой 'list' без подключения к базе.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testListWithConnect() {
        in.add("connect|sqlcmd|postgres|123");
        in.add("list");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //list
                "[car, client]\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testFindWithCorrectData() {
        in.add("connect|sqlcmd|postgres|123");
        in.add("clear|car");
        in.add("car");
        in.add("create|car|id|1|name|ferrari|color|red|age|6");
        in.add("create|car|id|2|name|porsche|color|black|age|1");
        in.add("create|car|id|3|name|bmw|color|blue|age|3");
        in.add("find|car");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //clear
                "ВНИМАНИЕ! Вы собираетесь удалить все данные с таблицы 'car'. " +
                "Введите название таблицы для подтверждения.\r\n" +
                //car
                "Таблица 'car' успешно очищена.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //create
                "Запись успешно создана.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                "Запись успешно создана.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                "Запись успешно создана.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //find|car
                "+---------------------------------------+\n" +
                "| id      | name    | color   | age     |\n" +
                "+---------------------------------------+\n" +
                "| 1       | ferrari | red     | 6       |\n" +
                "| 2       | porsche | black   | 1       |\n" +
                "| 3       | bmw     | blue    | 3       |\n" +
                "+---------------------------------------+\n" +
                "\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testFindLimitOffsetWithCorrectData() {
        in.add("connect|sqlcmd|postgres|123");
        in.add("clear|car");
        in.add("car");
        in.add("create|car|id|1|name|ferrari|color|red|age|6");
        in.add("create|car|id|2|name|porsche|color|black|age|1");
        in.add("create|car|id|3|name|bmw|color|blue|age|3");
        in.add("find|car|1|1");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //clear
                "ВНИМАНИЕ! Вы собираетесь удалить все данные с таблицы 'car'. " +
                "Введите название таблицы для подтверждения.\r\n" +
                //car
                "Таблица 'car' успешно очищена.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //create
                "Запись успешно создана.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                "Запись успешно создана.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                "Запись успешно создана.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //find|car|1|1
                "+---------------------------------------+\n" +
                "| id      | name    | color   | age     |\n" +
                "+---------------------------------------+\n" +
                "| 2       | porsche | black   | 1       |\n" +
                "+---------------------------------------+\n" +
                "\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testFindLimitOffsetWithIncorrectData() {
        in.add("connect|sqlcmd|postgres|123");
        in.add("find|car|qwe|qwe");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //find|car|1|1
                "Неправильные данные. limit и offset должны быть целыми числами.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testFindWithIncorrectDataLength() {
        in.add("connect|sqlcmd|postgres|123");
        in.add("find|");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //find|
                "Неправильная команда 'find|'. " +
                "Должно быть 'find|tableName' или 'find|tableName|limit|offset'\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testFindWithIncorrectData() {
        in.add("connect|sqlcmd|postgres|123");
        in.add("find|qwe");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //find|qwe
                "Не удалось отобразить таблицу 'qwe' по причине:" +
                " ERROR: relation \"public.qwe\" does not exist\n" +
                "  Позиция: 15\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testTableWithCorrectData() {
        in.add("connect|sqlcmd|postgres|123");
        in.add("table|city|id|name|text|population|int");
        in.add("drop|city");
        in.add("city");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //table|city
                "Таблица 'city' успешно создана\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //drop|city
                "ВНИМАНИЕ! Вы собираетесь удалить таблицу 'city'. Введите название таблицы для подтверждения.\r\n" +
                //city
                "Таблица 'city' успешно удалена.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testTableWithIncorrectDataType() {
        in.add("connect|sqlcmd|postgres|123");
        in.add("table|city|id|name|population");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //create
                "Не удалось создать таблицу 'city' по причине: ERROR: type \"population\" does not exist\n" +
                "  Позиция: 54\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testTableWithIncorrectDataLength() {
        in.add("connect|sqlcmd|postgres|123");
        in.add("table|");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //create
                "Неправильная команда 'table|'. 'table|tableName|" +
                "primaryKeyName|column1Name|column1Type|...|columnNName|columnNType'\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testDropWithIncorrectData() {
        in.add("connect|sqlcmd|postgres|123");
        in.add("drop|qwe");
        in.add("qwe");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //drop|qwe
                "ВНИМАНИЕ! Вы собираетесь удалить таблицу 'qwe'. " +
                "Введите название таблицы для подтверждения.\r\n" +
                //qwe
                "Не удалочь удалить таблицу 'qwe' по причине: ERROR: table \"qwe\" does not exist\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testDropWithIncorrectDataLength() {
        in.add("connect|sqlcmd|postgres|123");
        in.add("drop|qwe|qwe");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //drop|qwe|qwe
                "Неправильные данные 'drop|qwe|qwe'. Должно быть 'drop|tableName'.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testCreateWithCorrectData() {
        in.add("connect|sqlcmd|postgres|123");
        in.add("create|car|id|4|name|mercedes|color|white|age|5");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //create
                "Запись успешно создана.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testCreateWithIncorrectDataLength() {
        in.add("connect|sqlcmd|postgres|123");
        in.add("create|car");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //create
                "Неправильные данные 'create|car'. " +
                "Должно быть 'create|tableName|column1VName|column1Value|...|" +
                "columnNName|columnNValue'.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testDeleteWithCorrectData() {
        in.add("connect|sqlcmd|postgres|123");
        in.add("create|car|id|5|name|qwe|color|qwe|age|1");
        in.add("delete|car|id|5");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //create
                "Запись успешно создана.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //delete
                "Успешно удалено.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testDeleteWithIncorrectDataLength() {
        in.add("connect|sqlcmd|postgres|123");
        in.add("delete|car");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //delete
                "Неправильная команда 'delete|car'. " +
                "Должно быть 'delete|tableName|primaryKeyColumnName|primaryKeyValue'.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testCreateDeleteWithIncorrectData() {
        in.add("connect|sqlcmd|postgres|123");
        in.add("create|car|qwe|4|name|mercedes|color|white|age|5");
        in.add("delete|car|qwe|4");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //create
                "Не удалось создать поле по причине: ERROR: column \"qwe\" of relation \"car\" does not exist\n" +
                "  Позиция: 29\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //delete
                "Не удалось удалить поле по причине: ERROR: column \"qwe\" does not exist\n" +
                "  Позиция: 30\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testUnsupported() {
        in.add("connect|sqlcmd|postgres|123");
        in.add("qwe");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //qwe
                "Команды 'qwe' не существует.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testClearWithCorrectData() {
        in.add("connect|sqlcmd|postgres|123");
        in.add("table|city|id");
        in.add("clear|city");
        in.add("city");
        in.add("drop|city");
        in.add("city");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //table|city
                "Таблица 'city' успешно создана\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //clear|city
                "ВНИМАНИЕ! Вы собираетесь удалить все данные с таблицы 'city'. " +
                "Введите название таблицы для подтверждения.\r\n" +
                //city
                "Таблица 'city' успешно очищена.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //drop|city
                "ВНИМАНИЕ! Вы собираетесь удалить таблицу 'city'. " +
                "Введите название таблицы для подтверждения.\r\n" +
                //city
                "Таблица 'city' успешно удалена.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testClearWithIncorrectData() {
        in.add("connect|sqlcmd|postgres|123");
        in.add("clear|qwe");
        in.add("qwe");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //clear|qwe
                "ВНИМАНИЕ! Вы собираетесь удалить все данные с таблицы 'qwe'. " +
                "Введите название таблицы для подтверждения.\r\n" +
                //qwe
                "Не удалось очистить таблицу 'qwe' по причине: " +
                "ERROR: relation \"public.qwe\" does not exist\n" +
                "  Позиция: 13\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testClearWithIncorrectDataLength() {
        in.add("connect|sqlcmd|postgres|123");
        in.add("clear|");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //clear|
                "Неправильная команда 'clear|'. " +
                "Должно быть 'clear|tableName'.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testUpdateWithIncorrectDataLength() {
        in.add("connect|sqlcmd|postgres|123");
        in.add("update|");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //update|
                "Неправильные данные 'update|'. " +
                "Должно быть 'update|tableName|primaryKeyColumnName|" +
                "primaryKeyValue|column1Name|column1NewValue|" +
                "...|columnNName|columnNNewValue'.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testUpdateWithIncorrectData() {
        in.add("connect|sqlcmd|postgres|123");
        in.add("update|qwe|qwe|qwe|qwe|qwe");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //update|qwe|qwe|qwe|qwe|qwe
                "Не удалось обновить по причине ERROR: relation \"public.qwe\" does not exist\n" +
                "  Позиция: 8\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testUpdateWithCorrectData() {
        in.add("connect|sqlcmd|postgres|123");
        in.add("clear|car");
        in.add("car");
        in.add("create|car|id|4|name|mercedes|color|white|age|5");
        in.add("update|car|id|4|name|mercedes");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //update|car|id|1|name|mercedes
                "ВНИМАНИЕ! Вы собираетесь удалить все данные с таблицы 'car'. " +
                "Введите название таблицы для подтверждения.\r\n" +
                "Таблица 'car' успешно очищена.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                "Запись успешно создана.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //update|car|id|1|name|ferrari
                "Все данные успешно обновлены.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }
}
