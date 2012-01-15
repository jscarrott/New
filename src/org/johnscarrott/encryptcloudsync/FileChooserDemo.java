package org.johnscarrott.encryptcloudsync;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;

public class FileChooserDemo extends JFrame {

    private JTextArea log;
    private JFileChooser fc = new JFileChooser();

    private String newline = System.getProperty("line.separator");

    public FileChooserDemo() {
        super("FileChooserDemo");

        JButton openButton = new JButton("Open", new ImageIcon("images/open.gif"));
        openButton.addActionListener(new OpenListener());

        JButton saveButton = new JButton("Save", new ImageIcon("images/save.gif"));
        saveButton.addActionListener(new SaveListener());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(openButton);
        buttonPanel.add(saveButton);

        log = new JTextArea(5,20);
        log.setMargin(new Insets(5,5,5,5));
        JScrollPane logScrollPane = new JScrollPane(log);

        Container contentPane = getContentPane();
        contentPane.add(buttonPanel, BorderLayout.NORTH);
        contentPane.add(logScrollPane, BorderLayout.CENTER);
    }

    private class OpenListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int returnVal = fc.showOpenDialog(FileChooserDemo.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //this is where a real application would open the file.
                log.append("Opening: " + file.getName() + "." + newline);
            } else {
                log.append("Open command cancelled by user." + newline);
            }
        }
    }

    private class SaveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int returnVal = fc.showSaveDialog(FileChooserDemo.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //this is where a real application would save the file.
                log.append("Saving: " + file.getName() + "." + newline);
            } else {
                log.append("Save command cancelled by user." + newline);
            }
        }
    }

    public static void main(String s[]) {
        JFrame frame = new FileChooserDemo();

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });

        frame.pack();
        frame.setVisible(true);
    }
}
