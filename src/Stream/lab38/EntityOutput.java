package Stream.lab38;

import java.io.*;

public interface EntityOutput {
    public void writePerson(Person person) throws IOException;
    public void writePoint(Point point) throws IOException;
}