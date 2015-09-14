package sqlcmd.command;

import sqlcmd.databasemanager.DatabaseManager;
import sqlcmd.view.View;

import java.sql.SQLException;

public class Connect implements Command {

    private DatabaseManager manager;
    private View view;

    public Connect(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("connect|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");

        if (data.length != 4) {
            view.write(String.format("Неправильная команда '%s'. " +
                    "Должно быть 'connect|database|user|password'.", command));
            return;
        }

        String database = data[1];
        String user = data[2];
        String password = data[3];

        try {
            manager.connect(database, user, password);
            view.write(String.format("Подключение к базе '%s' прошло успешно.", database));
        } catch (SQLException e) {
            view.write(String.format("Не удалось подключиться к базе '%s' " +
                    "по причине: %s", database, e.getMessage()));
        } catch (ClassNotFoundException e) {
            view.write("Не могу найти драйвер (jar). Добавьте его в библитеку проекта.");
        }
    }
}