package sqlcmd.databasemanager;

import sqlcmd.Console;

import java.sql.*;

public class JDBCDatabaseManager implements DatabaseManager {

    private Connection connection;

    @Override
    public void exit() {
        System.out.println("До свидания!");
        System.exit(0);
    }

    @Override
    public String list(String command) {
        if (connection != null) {
            try {
                DatabaseMetaData metaData = connection.getMetaData();
                ResultSet resultSet1 = metaData.getTables(null, "public", "%", new String[]{"TABLE"});
                String tables = "[";
                while (resultSet1.next()) {
                    tables += resultSet1.getString(3) + ", ";
                }
                String result;
                result = tables.substring(0, tables.length() - 2);
                result += "]";

                System.out.println(result);

                resultSet1.close();
                return result;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void help() {
        System.out.println("'connect|database|user|password'\n" +
                "\t подключение к базе");
        System.out.println("'list'\n" +
                "\t вывод списка всех таблиц");
        System.out.println("'find|tableName'\n" +
                "\t вывод всей таблицы");
        System.out.println("'find|tableName|limit|offset'\n" +
                "\t вывод части таблицы");
        System.out.println("'create|tableName|column1Value|column2Value|...|columnNValue'\n" +
                "\t создание поля");
        System.out.println("'update|tableName|primaryKeyColumnName|primaryKeyValue|column1Name|column1NewValue|column2Name|column2NewValue|...|columnNName|columnNNewValue'\n" +
                "\t обновление поля");
        System.out.println("'delete|tableName|primaryKeyColumnName|primaryKeyValue'\n" +
                "\t удаление поле");
        System.out.println("'clear|tableName'\n" +
                "\t очистка таблицы");
        System.out.println("'drop|tableName'\n" +
                "\t удаление таблицы");
        System.out.println("'exit'\n" +
                "\t выход из програмы");
    }

    @Override
    public void unsupported(String command) {
        System.out.println(String.format("Команды '%s' не существует", command));
    }

    @Override
    public void update(String command) {
        String[] data = command.split("\\|");

        if (data.length < 6 || data.length % 2 == 1) {
            System.out.println(String.format("Неправильные данные '%s'. " +
                    "Должно быть 'update|tableName|primaryKeyColumnName|primaryKeyValue|column1Name|column1NewValue|column2Name|column2NewValue|...|columnNName|columnNNewValue'", command));
            return;
        }

        String tableName = data[1];

        try (Statement stmt = connection.createStatement()) {
            for (int index = 5; index < data.length; index += 2) {
                stmt.executeUpdate("UPDATE public." + tableName + " SET " + data[index - 1] + "='" + data[index] + "' WHERE " + data[2] + " =" + data[3]);
            }

        } catch (SQLException e) {
            System.out.println("Неправильные данные");
            return;
        }
        System.out.println("Поле успешно обновлено");
    }

    @Override
    public void drop(String command) {
        Console console = new Console();
        String[] data = command.split("\\|");

        if (data.length != 2) {
            System.out.println(String.format("Неправильные данные '%s'. Должно быть 'drop|tableName'", command));
            return;
        }

        String tableName = data[1];

        System.out.println(String.format("ВНИМАНИЕ! Вы собираетесь удалить таблицу '%s'. 'y' для удаления,'n' для отмены", tableName));
        String check = console.read();
        if (check.equals("n")) {
            System.out.println("");
            return;
        }

        try (Statement stmt = connection.createStatement()) {

            stmt.executeUpdate("DROP TABLE public." + tableName);

            System.out.println(String.format("Таблица '%s' успешно удалена", tableName));

        } catch (SQLException e) {
            System.out.println(String.format("Таблицы '%s' не существует", tableName));
        }
    }

    @Override
    public void create(String command) {

        String[] data = command.split("\\|");

        if (data.length < 2) {
            System.out.println(String.format("Неправильные данные'%s'. Должно быть 'create|tableName|column1Value|column2Value|...|columnNValue'", command));
            return;
        }

        String tableName = data[1];
        try (Statement stmt = connection.createStatement()) {

            int columnsCount = getColumnsCount(tableName, stmt);

            if (data.length != columnsCount + 2) {
                System.out.println(String.format("Неправильные данные '%s'. Должно быть 'create|tableName|column1Value|column2Value|...|columnNValue'", command));
                return;
            }

            String string = "INSERT INTO public." + tableName + " VALUES('";
            for (int index = 2; index < data.length; index++) {
                string += data[index] + "', '";
            }
            String result = string.substring(0, string.length() - 4);
            result += "')";
            stmt.executeUpdate(result);

            System.out.println("Запись успешно создана");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(String.format("Таблицы '%s' не существует", tableName));
        }
    }

    @Override
    public void clear(String command) {
        Console console = new Console();
        String[] data = command.split("\\|");
        String tableName = data[1];


        if (data.length != 2) {
            System.out.println(String.format("Неправильная команда '%s'. Должно быть 'clear|tableName'", command));
            return;
        }

        System.out.println(String.format("ВНИМАНИЕ! Вы собираетесь удалить все данные с таблицы '%s'. 'y' для подтверждения, 'n' для отмены", tableName));
        String check = console.read();
        if (check.equals("n")) {
            return;
        }

        try (Statement stmt = connection.createStatement()) {

            stmt.executeUpdate("DELETE FROM public." + tableName);

            System.out.println(String.format("Таблица '%s' успешно очищена", tableName));

        } catch (SQLException e) {
            System.out.println(String.format("Таблицы '%s' не существует", tableName));
        }
    }

    @Override
    public void delete(String command) {
        String[] data = command.split("\\|");

        if (data.length != 4) {
            System.out.println(String.format("Неправильная команда '%s'. Должно быть 'delete|tableName|primaryKeyColumnName|primaryKeyValue'", command));
            return;
        }

        String tableName = data[1];
        String primaryKeyColumnName = data[2];
        String primaryKeyValue = data[3];

        try (Statement stmt = connection.createStatement()) {

            stmt.executeUpdate("DELETE FROM public." + tableName + " WHERE " + primaryKeyColumnName + "=" + primaryKeyValue);

            System.out.println("Успешно удалено");

        } catch (SQLException e) {
            System.out.println(String.format("Таблицы '%s' не существует", tableName));
        }
    }

    @Override
    public void connect(String command) {
        String[] data = command.split("\\|");

        if (data.length != 4) {
            System.out.println(String.format("Неправильная команда '%s'. Должно быть 'connect|database|user|password'", command));
            return;
        }

        String database = data[1];
        String user = data[2];
        String password = data[3];

        try {
            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {
            System.out.println("Cant find jdbc jar");
        }

        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/" + database + "", "" + user + "",
                    "" + password + "");

        } catch (SQLException e) {
            System.out.println(String.format("Неправильные данные для database:'%s' user:'%s'. ", database, user));
            connection = null;

        }
        if (connection != null) {
            System.out.println("Подключение OK");
        }
    }

    @Override
    public void find(String command) {
        String[] data = command.split("\\|");

        if (data.length != 2 && data.length != 4) {
            System.out.println(String.format("Неправильная команда '%s'. Должно быть 'find|tableName' или 'find|tableName|limit|offset'", command));
            return;
        }

        String tableName = data[1];

        try (Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            int columnsCount = getColumnsCount(tableName, stmt);

            if (data.length == 2) {
                allTableData(stmt, columnsCount, tableName);
            } else {
                limitOffsetTableData(stmt, columnsCount, tableName, data);
            }

        } catch (SQLException e) {
            System.out.format(String.format("Таблицы '%s' не существует", tableName));
        }
    }

    private void allTableData(Statement stmt, int columnsCount, String tableName) {
        try (ResultSet resultSet = stmt.executeQuery("SELECT * FROM public." + tableName)) {

            printTableData(columnsCount, resultSet);

        } catch (SQLException e) {
            System.out.format(String.format("Таблицы '%s' не существует", tableName));
        }
    }

    private void limitOffsetTableData(Statement stmt, int columnsCount, String tableName, String[] data) {
        int limit = Integer.parseInt(data[2]);
        int offset = Integer.parseInt(data[3]) - 1;

        try (ResultSet resultSet = stmt.executeQuery("SELECT * FROM public." + tableName + " LIMIT " + limit + " OFFSET " + offset)) {

            printTableData(columnsCount, resultSet);

        } catch (SQLException e) {
            System.out.format(String.format("Таблицы '%s' не существует", tableName));
        }
    }

    private void printTableData(int columnsCount, ResultSet resultSet) {
        try {

            int maxSize = getMaxSize(columnsCount, resultSet);

            printSeparator(columnsCount, maxSize);
            System.out.print("|");
            for (int index = 1; index <= columnsCount; index++) {
                System.out.format("%-" + maxSize + "s", " " + resultSet.getMetaData().getColumnName(index));
                System.out.print("|");
            }
            System.out.println("");
            printSeparator(columnsCount, maxSize);

            resultSet.beforeFirst();
            while (resultSet.next()) {
                System.out.print("|");
                for (int i = 1; i <= columnsCount; i++) {
                    System.out.format("%-" + maxSize + "s", " " + resultSet.getString(i));
                    System.out.print("|");
                }
                System.out.println("");
            }
            printSeparator(columnsCount, maxSize);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void printSeparator(int columnsCount, int maxSize) {
        int separatorLength = columnsCount * maxSize + columnsCount;
        System.out.print("+");
        for (int i = 0; i <= separatorLength - 2; i++) {
            System.out.print("-");
        }
        System.out.println("+");
    }

    private int getMaxSize(int columnsCount, ResultSet resultSet) {
        try {
            int longestColumnName = 0;
            for (int index = 1; index <= columnsCount; index++) {
                int columnNameLength = resultSet.getMetaData().getColumnName(index).length();
                if (longestColumnName < columnNameLength) {
                    longestColumnName = columnNameLength;
                }
            }

            int longestColumnValue = 0;
            while (resultSet.next()) {
                for (int index = 1; index <= columnsCount; index++) {
                    int columnValueLength = resultSet.getString(index).length();
                    if (longestColumnValue < columnValueLength) {
                        longestColumnValue = columnValueLength;
                    }
                }
            }
            return Math.max(longestColumnName, longestColumnValue) + 2;
        } catch (SQLException e) {
            return -1;
        }
    }

    private int getColumnsCount(String tableName, Statement stmt) {
        try (ResultSet resultSetCount = stmt.executeQuery("SELECT * FROM public." + tableName)) {
            ResultSetMetaData rsmd = resultSetCount.getMetaData();

            return rsmd.getColumnCount();

        } catch (SQLException e) {
            return -1;
        }
    }
}
