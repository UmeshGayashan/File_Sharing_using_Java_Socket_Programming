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
    public Main() {
    }

    public static void main(final String[] var0) throws IOException {
        JFrame var1 = new JFrame("Simple File Transfer App");
        var1.setSize(450, 450);
        var1.setLayout(new BoxLayout(var1.getContentPane(), 1));
        var1.setDefaultCloseOperation(3);
        JLabel var2 = new JLabel("Welcome");
        var2.setFont(new Font("Arial", 1, 25));
        var2.setBorder(new EmptyBorder(20, 0, 10, 0));
        var2.setAlignmentX(0.5F);
        JLabel var3 = new JLabel("What do your need ?");
        var3.setFont(new Font("Arial", 1, 20));
        var3.setBorder(new EmptyBorder(50, 0, 0, 0));
        var3.setAlignmentX(0.5F);
        JPanel var4 = new JPanel();
        var4.setBorder(new EmptyBorder(75, 0, 10, 0));
        JButton var5 = new JButton("Send File");
        var5.setPreferredSize(new Dimension(150, 75));
        var5.setFont(new Font("Arial", 1, 20));
        JButton var6 = new JButton("Recieve File");
        var6.setPreferredSize(new Dimension(150, 75));
        var6.setFont(new Font("Arial", 1, 20));
        var4.add(var5);
        var4.add(var6);

        // Server
        var6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {

                Thread var2 = new Thread(new Runnable() {
                    public void run() {
                        try {
                            Server.main(var0);
                        } catch (IOException var2) {
                            var2.printStackTrace();
                        }

                    }
                });
                var2.start();
            }
        });

        // Client
        var5.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent var1) {
                Thread var2 = new Thread(new Runnable() {
                    public void run() {
                        try {
                            Client.main(var0);
                        } catch (Exception var2) {
                        }

                    }
                });
                var2.start();
            }
        });
        var1.add(var2);
        var1.add(var3);
        var1.add(var4);
        var1.setVisible(true);
    }
}