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

        String[] updateData = new String[5];
        updateData[0] = data[1];
        updateData[1] = data[2];
        updateData[2] = data[3];
        boolean success = true;
        for (int index = 4; index < data.length; index += 2) {
            updateData[3] = data[index];
            updateData[4] = data[index + 1];
            try {
                manager.update(updateData);
            } catch (SQLException e) {
                view.write(String.format("Не удалось обновить по причине %s", e.getMessage()));
                success = false;
            }
        }
        if (success) {
            view.write("Все данные успешно обновлены.");
        }
    }
}