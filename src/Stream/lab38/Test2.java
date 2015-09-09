package Stream.lab38;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Test2 {
    public static void main(String[] args) throws IOException {
        Point expectedPoint = new Point(5, 10);


        ByteArrayOutputStream outByteArray = new ByteArrayOutputStream();
        EntityOutputStream outEntity = new EntityOutputStream(outByteArray);

        outEntity.writePoint(expectedPoint);

        byte[] rawByteArray = outByteArray.toByteArray();


        //call
        ByteArrayInputStream in = new ByteArrayInputStream(rawByteArray);
        EntityInputStream stream = new EntityInputStream(in);

        Point actualPoint = stream.readPoint();

        //check

        if (actualPoint.getX() != expectedPoint.getX())
            throw new AssertionError("Expected Point X equals " + expectedPoint.getX() + " but found " + actualPoint.getX());

        if (actualPoint.getY() != expectedPoint.getY())
            throw new AssertionError("Expected Person Y equals " + expectedPoint.getY() + " but found " + actualPoint.getY());

        System.out.print("OK");
    }
}