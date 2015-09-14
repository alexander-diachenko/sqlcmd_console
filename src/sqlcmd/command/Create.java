package sqlcmd.command;

import sqlcmd.databasemanager.DatabaseManager;
import sqlcmd.view.View;

import java.sql.SQLException;

public class Create implements Command {

    private DatabaseManager manager;
    private View view;

    public Create(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("create|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");

        if (data.length < 2) {
            view.write(String.format("Неправильные данные'%s'. " +
                    "Должно быть 'create|tableName|column1Value|column2Value|...|columnNValue'.", command));
            return;
        }

        String tableName = String.valueOf(data[1]);

        String value = "'";
        for (int index = 2; index < data.length; index++) {
            value += data[index] + "', '";
        }
        value = value.substring(0, value.length() - 4);
        value += "'";

        try {
            manager.create(tableName, value);
            view.write("Запись успешно создана.");
        } catch (SQLException e) {
            view.write(String.format("Не удалось создать поле по причине: %s", e.getMessage()));
        }
    }
}