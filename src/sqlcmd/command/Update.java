package sqlcmd.command;

import sqlcmd.databasemanager.DatabaseManager;
import sqlcmd.view.View;

import java.sql.SQLException;

public class Update implements Command {

    private DatabaseManager manager;
    private View view;

    public Update(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("update|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");

        if (data.length < 6 || data.length % 2 == 1) {
            view.write(String.format("Неправильные данные '%s'. " +
                    "Должно быть 'update|tableName|primaryKeyColumnName|primaryKeyValue|" +
                    "column1Name|column1NewValue|column2Name|column2NewValue|...|" +
                    "columnNName|columnNNewValue'.", command));
            return;
        }

        String tableName = data[1];
        String primaryKey = data[2] + " =" + data[3];
        String value;
        try {
            for (int index = 4; index < data.length; index += 2) {
                value = data[index] + " ='" + data[index + 1] + "'";
                manager.update(tableName, value, primaryKey);
            }
            view.write("Поле успешно обновлено.");
        }catch (SQLException e){
            view.write(String.format("Не удалось обновить поле по причине %s", e.getMessage()));
        }
    }
}