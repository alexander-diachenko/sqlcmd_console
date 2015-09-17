package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by POSITIV on 16.09.2015.
 */
public class Console implements View {

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public String read() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void write(String message) {
        System.out.println(message);
    }
}