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

    JTextField jtfInput;
    JTextArea jtAreaOutput;

    public JTextAreaConsole() {
        createGui();
    }

    public void createGui() {
        jtfInput = new JTextField(30);
        jtfInput.addActionListener(this);
        jtAreaOutput = new JTextArea(30, 60);
        jtAreaOutput.setCaretPosition(jtAreaOutput.getDocument()
                .getLength());
        jtAreaOutput.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(jtAreaOutput,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        GridBagLayout gridBag = new GridBagLayout();
        Container contentPane = getContentPane();
        contentPane.setLayout(gridBag);
        GridBagConstraints gridCons1 = new GridBagConstraints();
        gridCons1.gridwidth = GridBagConstraints.REMAINDER;
        gridCons1.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(jtfInput, gridCons1);
        GridBagConstraints gridCons2 = new GridBagConstraints();
        gridCons2.weightx = 1.0;
        gridCons2.weighty = 1.0;
        contentPane.add(scrollPane, gridCons2);
    }

    public void actionPerformed(ActionEvent evt) {
        inputMessage = jtfInput.getText();
        jtAreaOutput.append(inputMessage + "\n" + outputMessage + "\n");
        outputMessage = "";
        jtfInput.selectAll();
        jtfInput.replaceSelection("");
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
