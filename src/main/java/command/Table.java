package command;

import databasemanagertest.DatabaseManager;
import view.View;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by POSITIV on 16.09.2015.
 */
public class Table implements Command {

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

        if (!isCorrect(command, data)) return;

        String tableName = data[1];
        String primaryKeyName = data[2];

        Map<String, Object> parameters = new HashMap<>();
        for (int index = 3; index < data.length; index += 2) {
            parameters.put(data[index], data[index + 1]);
        }

        try {
            manager.table(tableName, primaryKeyName, parameters);
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
