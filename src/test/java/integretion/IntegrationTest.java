package integretion;

import main.Main;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

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
    public void clearIn() throws IOException {
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
                "[car, client]\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testFindWithoutConnect() {
        in.add("find|car");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //find|car
                "Вы не можете пользоваться командой 'find|car' без подключения к базе.\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //exit
                "До свидания!\r\n", getData());
    }

    @Test
    public void testFindWithConnect() {
        in.add("connect|sqlcmd|postgres|123");
        in.add("find|car");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Добро пожаловать!\r\n" +
                "Введите команду или 'help' для помощи:\r\n" +
                //connect
                "Подключение к базе 'sqlcmd' прошло успешно.\r\n" +
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
    public void testTableWithIncorrectData() {
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
    public void testCreateDeleteWithCorrectData() {
        in.add("connect|sqlcmd|postgres|123");
        in.add("create|car|id|4|name|mercedes|color|white|age|5");
        in.add("delete|car|id|4");
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
}
