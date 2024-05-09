import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Main {
    public static void main(String[] args) throws IOException {

        // Frame for All Content
        JFrame jFrame = new JFrame("Simple File Transfer App");
        jFrame.setSize(450, 450);
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Label for Title
        JLabel JlTitle = new JLabel("Welcome");
        JlTitle.setFont(new Font("Arial", Font.BOLD, 25));
        JlTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
        JlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Label for Instruction
        JLabel jlFileName = new JLabel("What do your need ?");
        jlFileName.setFont(new Font("Arial", Font.BOLD, 20));
        jlFileName.setBorder(new EmptyBorder(50, 0, 0, 0));
        jlFileName.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Panel for Buttons
        JPanel jpButton = new JPanel();
        jpButton.setBorder(new EmptyBorder(75, 0, 10, 0));

        // Button for Sending
        JButton jbSendFile = new JButton("Send File");
        jbSendFile.setPreferredSize(new Dimension(150, 75));
        jbSendFile.setFont(new Font("Arial", Font.BOLD, 20));

        // Button for File Choosing
        JButton jbRecieveFile = new JButton("Recieve File");
        jbRecieveFile.setPreferredSize(new Dimension(150, 75));
        jbRecieveFile.setFont(new Font("Arial", Font.BOLD, 20));

        // Adding buttons in to Panel
        jpButton.add(jbSendFile);
        jpButton.add(jbRecieveFile);

        jbRecieveFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Start the server on a new thread
                Thread serverThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Server.main(args);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
                serverThread.start();
            }
        });

        jbSendFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread clientThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Client.main(args);

                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                    }
                });
                clientThread.start();
            }
        });

        jFrame.add(JlTitle);
        jFrame.add(jlFileName);
        jFrame.add(jpButton);
        jFrame.setVisible(true);
    }
}
