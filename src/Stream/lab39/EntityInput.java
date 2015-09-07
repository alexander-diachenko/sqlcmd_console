package Stream.lab39;

import java.io.*;

public interface EntityInput {
    public Person readPerson() throws IOException;

    public Point readPoint() throws IOException;
}
