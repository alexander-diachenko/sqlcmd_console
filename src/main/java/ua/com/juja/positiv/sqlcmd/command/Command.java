package ua.com.juja.positiv.sqlcmd.command;

/**
 * Created by POSITIV on 16.09.2015.
 */
public interface Command {

    boolean canProcess(String command);

    void process(String command);

    String format();

    String description();
}
