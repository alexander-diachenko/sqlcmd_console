package sqlcmd.command;

import java.sql.*;

public class Find {

    public void doFind(Connection connection, String command) {

        String[] data = command.split("\\|");

        if (data.length != 2 && data.length != 4) {
            System.out.println(String.format("Incorrect command '%s'. Must be 'find|tableName' or 'find|tableName|limit|offset'", command));
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
            System.out.println(String.format("'%s' does not exist", tableName));
        }
    }

    private void allTableData(Statement stmt, int columnsCount, String tableName) {

        try (ResultSet resultSet = stmt.executeQuery("SELECT * FROM public." + tableName)) {

            printTableData(columnsCount, resultSet);

        } catch (SQLException e) {
            System.out.println(String.format("%s does not exist", tableName));
        }
    }

    private void limitOffsetTableData(Statement stmt, int columnsCount, String tableName, String[] data) {

        int limit = Integer.parseInt(data[2]);
        int offset = Integer.parseInt(data[3]) - 1;

        try (ResultSet resultSet = stmt.executeQuery("SELECT * FROM public." + tableName + " LIMIT " + limit + " OFFSET " + offset)) {

            printTableData(columnsCount, resultSet);

        } catch (SQLException e) {
            System.out.format(String.format("%s does not exist", tableName));
        }
    }

    private void printTableData(int columnsCount, ResultSet resultSet) {

        try {

            int maxSize = getMaxSize(columnsCount, resultSet);

            printSeparator(columnsCount, maxSize);
            System.out.print("|");
            for (int index = 1; index <= columnsCount; index++) {
                System.out.format("%-" + maxSize + "s", " " + resultSet.getMetaData().getColumnName(index));
                System.out.print("| ");
            }
            System.out.println("");
            printSeparator(columnsCount, maxSize);

            resultSet.beforeFirst();
            while (resultSet.next()) {
                System.out.print("|");
                for (int i = 1; i <= columnsCount; i++) {
                    System.out.format("%-" + maxSize + "s", " " + resultSet.getString(i));
                    System.out.print("| ");
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
        for (int i = 0; i <= separatorLength; i++) {
            System.out.print("-");
        }
        System.out.println("+");
    }

    private int getMaxSize(int columnsCount, ResultSet resultSet) throws SQLException {
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
    }

    public int getColumnsCount(String tableName, Statement stmt) {

        try (ResultSet resultSetCount = stmt.executeQuery("SELECT * FROM public." + tableName)) {
            ResultSetMetaData rsmd = resultSetCount.getMetaData();

            return rsmd.getColumnCount();

        } catch (SQLException e) {
            return -1;
        }
    }
}