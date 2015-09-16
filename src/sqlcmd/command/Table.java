package sqlcmd.command;

import sqlcmd.databasemanager.DatabaseManager;
import sqlcmd.view.View;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Table implements Command{

    private View view;
    private DatabaseManager manager;

    public Table(DatabaseManager manager, View view) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("table|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");

        if (!isCorrect(command, data)) {
            return;
        }

        String tableName = data[1];
        String primaryKey = data[2];

        Map<String, Object> map = new HashMap<>();
        for (int index = 3; index < data.length; index += 2) {
            map.put(data[index], data[index + 1]);
        }

        try {
            manager.table(tableName, primaryKey, map);
            view.write(String.format("Таблица '%s' успешно создана", tableName));
        } catch (SQLException e) {
            view.write(String.format("Не удалось создать таблицу '%s' " +
                    "по причине: %s", tableName, e.getMessage()));
        }
    }

    private boolean isCorrect(String command, String[] data) {
        if(data.length < 3){
            view.write(String.format("Неправильная команда '%s'. " +
                    "'table|tableName|primaryKeyName|column1Name|column1Type|...|" +
                    "columnNName|columnNType'", command));
            return false;
        }
        return true;
    }
}