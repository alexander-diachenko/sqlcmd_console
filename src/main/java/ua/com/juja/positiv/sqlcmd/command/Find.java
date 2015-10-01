package ua.com.juja.positiv.sqlcmd.command;

import ua.com.juja.positiv.sqlcmd.databasemanager.DatabaseManager;
import ua.com.juja.positiv.sqlcmd.view.View;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by POSITIV on 16.09.2015.
 */
public class Find implements Command {

    private DatabaseManager manager;
    private View view;

    public Find(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("find|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (!isCorrect(command, data)) {
            return;
        }

        String tableName = getTableName(data);
        try {
            view.write(formatted(tableName));
        } catch (SQLException e) {
            view.write(String.format("Не удалось отобразить таблицу '%s' " +
                    "по причине: %s", tableName, e.getMessage()));
        }
    }

    private boolean isCorrect(String command, String[] data) {
        if (data.length != 2 && data.length != 4) {
            view.write(String.format("Неправильная команда '%s'. " +
                    "Должно быть 'find|tableName' или 'find|tableName|limit|offset'", command));
            return false;
        }
        if (data.length == 4 && !isNumeric(data)) {
            return false;
        }
        return true;
    }

    private String getTableName(String[] data) {
        String tableName;
        if (data.length == 4) {
            Integer limit = Integer.valueOf(data[2]);
            Integer offset = Integer.valueOf(data[3]);
            tableName = data[1] + " LIMIT " + limit + " OFFSET " + offset;
        } else {
            tableName = data[1];
        }
        return tableName;
    }

    private String formatted(String tableName) throws SQLException {
        List<String> list = manager.getTableData(tableName);

        int maxSize = getMaxSize(list);
        int columnCount = Integer.parseInt(list.get(0));

        String result = "";
        result = addSeparator(result, columnCount, maxSize) +
                addColumnNames(list, maxSize, columnCount, result) +
                addSeparator(result, columnCount, maxSize) +
                addTableData(list, maxSize, columnCount, result) +
                addSeparator(result, columnCount, maxSize);
        return result;
    }

    private String addSeparator(String tableData, int columnsCount, int maxSize) {
        int separatorLength = columnsCount * (maxSize + 2) + columnsCount;
        tableData += "+";
        for (int i = 0; i <= separatorLength - 2; i++) {
            tableData += "-";
        }
        tableData += "+\n";
        return tableData;
    }

    private String addColumnNames(List<String> list, int maxSize, int columnCount, String tableData) {
        for (int index = 1; index <= columnCount; index++) {
            tableData += "| ";
            tableData += String.format("%-" + maxSize + "s", list.get(index));
            tableData += " ";
        }
        tableData += "|\n";
        return tableData;
    }

    private String addTableData(List<String> list, int maxSize, int columnCount, String tableData) {
        int lastIndex = 1 + columnCount;
        for (int rowIndex = 0; rowIndex < getRowCount(list, columnCount); rowIndex++) {
            for (int columnIndex = lastIndex; columnIndex < lastIndex + columnCount; columnIndex++) {
                tableData += "| ";
                tableData += String.format("%-" + maxSize + "s", list.get(columnIndex));
                tableData += " ";
            }
            tableData += "|\n";
            lastIndex += columnCount;
        }
        return tableData;
    }

    private int getRowCount(List<String> list, int columnCount) {
        int rowCount = 0;
        if (columnCount > 0) {
            rowCount = (list.size() - 1 - columnCount) / columnCount;
        }
        return rowCount;
    }

    private int getMaxSize(List<String> list) {
        int maxSize = 0;
        for (int index = 1; index < list.size(); index++) {
            if (maxSize < list.get(index).length()) {
                maxSize = list.get(index).length();
            }
        }
        return maxSize;
    }

    private boolean isNumeric(String[] data) {
        for (int index = 2; index < 4; index++) {
            for (char ch : data[2].toCharArray()) {
                if (!Character.isDigit(ch)) {
                    view.write("Неправильные данные. limit и offset должны быть целыми числами.");
                    return false;
                }
            }
        }
        return true;
    }
}

