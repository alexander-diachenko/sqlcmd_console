package sqlcmd.command;

public interface Commands {

    boolean canProcess(String command);

    void process(String command);
}
