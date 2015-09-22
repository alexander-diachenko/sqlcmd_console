package ua.com.juja.positiv.sqlcmd.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by POSITIV on 21.09.2015.
 */
public class JText extends JFrame implements ActionListener, View {


    static String outputMessage = "";
    static String inputMessage = null;
    JTextField inputField;
    JTextArea outputField;

    public static void startGUI() {
        JText text = new JText();
        text.pack();
        text.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        text.setVisible(true);
    }

    public JText() {
        createGui();
    }

    public void createGui() {
        inputField = new JTextField();
        inputField.addActionListener(this);
        outputField = new JTextArea(30, 60);
        outputField.setCaretPosition(outputField.getDocument()
                .getLength());
        outputField.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputField,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        GridBagLayout gridBag = new GridBagLayout();
        Container contentPane = getContentPane();
        contentPane.setLayout(gridBag);
        GridBagConstraints gridCons1 = new GridBagConstraints();
        gridCons1.gridwidth = GridBagConstraints.REMAINDER;
        gridCons1.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(inputField, gridCons1);
        GridBagConstraints gridCons2 = new GridBagConstraints();
        gridCons2.weightx = 2.0;
        gridCons2.weighty = 2.0;
        contentPane.add(scrollPane, gridCons2);
    }

    public void actionPerformed(ActionEvent evt) {
        inputMessage = inputField.getText();
        outputField.append(inputMessage + "\n" + outputMessage);
        outputMessage = "";
        inputField.selectAll();
        inputField.replaceSelection("");
    }

    @Override
    public String read() {
        while (inputMessage == null){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String command = inputMessage;
        inputMessage = null;
        return command;
    }

    @Override
    public void write(String message) {
        outputMessage += message + "\n";
    }
}
