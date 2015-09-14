package sqlcmd.view;

public interface View {

    String read();

    void write(String message);
}
