package Stream.lab38;

import java.io.*;

public class EntityInputStream implements EntityInput {

    private DataInput in;
    public EntityInputStream(InputStream in){
     this.in = new DataInputStream(in);
    }

    @Override
    public Person readPerson() throws IOException {
        Person person = new Person("John", 32);
        return person;
    }

    @Override
    public Point readPoint() throws IOException {
        Point point = new Point(5, 10);
        return point;
    }
}
