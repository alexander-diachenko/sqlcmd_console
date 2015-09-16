package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Console implements View {

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public String read() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void write(String message) {
        System.out.println(message);
    }
}