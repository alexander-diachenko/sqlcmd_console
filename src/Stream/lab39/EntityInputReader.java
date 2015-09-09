package Stream.lab39;

import java.io.*;

public class EntityInputReader implements EntityInput {

    private Reader in;
    public EntityInputReader(Reader in){
        this.in = new InputStreamReader(System.in);
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
