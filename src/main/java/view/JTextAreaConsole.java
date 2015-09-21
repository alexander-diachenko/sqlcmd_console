package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by POSITIV on 21.09.2015.
 */
public class JTextAreaConsole extends JFrame implements ActionListener, View {


    static String outputMessage = "";
    static String inputMessage = null;

    JTextField textField;
    JTextArea areaOutput;

    public JTextAreaConsole() {
        createGui();
    }

    public void createGui() {
        textField = new JTextField();
        textField.addActionListener(this);
        areaOutput = new JTextArea(30, 60);
        areaOutput.setCaretPosition(areaOutput.getDocument()
                .getLength());
        areaOutput.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaOutput,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        GridBagLayout gridBag = new GridBagLayout();
        Container contentPane = getContentPane();
        contentPane.setLayout(gridBag);
        GridBagConstraints gridCons1 = new GridBagConstraints();
        gridCons1.gridwidth = GridBagConstraints.REMAINDER;
        gridCons1.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(textField, gridCons1);
        GridBagConstraints gridCons2 = new GridBagConstraints();
        gridCons2.weightx = 2.0;
        gridCons2.weighty = 2.0;
        contentPane.add(scrollPane, gridCons2);
    }
    public void actionPerformed(ActionEvent evt) {
        inputMessage = textField.getText();
        areaOutput.append(inputMessage + "\n" + outputMessage);
        outputMessage = "";
        textField.selectAll();
        textField.replaceSelection("");
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
