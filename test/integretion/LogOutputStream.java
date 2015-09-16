package integretion;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by POSITIV on 16.09.2015.
 */
public class LogOutputStream extends OutputStream{

    private String log;

    @Override
    public void write(int b) throws IOException {
        log += String.valueOf((char)b);
    }
}
