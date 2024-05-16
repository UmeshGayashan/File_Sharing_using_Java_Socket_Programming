import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Client {

    private static final int BROADCAST_PORT = 8888;
    static Vector<String> messageVector = new Vector<>();
    static String receiver = "";

    public static void main(String[] args) {

        // start reciveing broacast message
        new Thread(Client::listenForServers).start();

        final File[] fileToSend = new File[1];

        JFrame jFrame = new JFrame("Send File");
        jFrame.setSize(450, 450);
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel JlTitle = new JLabel("Java Network Programming");
        JlTitle.setFont(new Font("Arial", Font.BOLD, 25));
        JlTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        JlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel jlFileName = new JLabel("Choose a file to send");
        jlFileName.setFont(new Font("Arial", Font.BOLD, 20));
        jlFileName.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        jlFileName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel jpButton = new JPanel();
        jpButton.setBorder(BorderFactory.createEmptyBorder(75, 0, 10, 0));

        JButton jbSendFile = new JButton("Send File");
        jbSendFile.setPreferredSize(new Dimension(150, 75));
        jbSendFile.setFont(new Font("Arial", Font.BOLD, 20));

        JButton jbChooseFile = new JButton("Choose File");
        jbChooseFile.setPreferredSize(new Dimension(150, 75));
        jbChooseFile.setFont(new Font("Arial", Font.BOLD, 20));

        JButton jbRefresh = new JButton("Select Reciever");
        jbRefresh.setPreferredSize(new Dimension(200, 75));
        jbRefresh.setFont(new Font("Arial", Font.BOLD, 16));

        jpButton.add(jbSendFile);
        jpButton.add(jbChooseFile);
        jpButton.add(jbRefresh);

        JPanel serverPanel = new JPanel();
        serverPanel.setLayout(new BoxLayout(serverPanel, BoxLayout.Y_AXIS));
        serverPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel availableServersLabel = new JLabel("Available Servers: ");
        availableServersLabel.setFont(new Font("Arial", Font.BOLD, 16));
        serverPanel.add(availableServersLabel);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> messageList = new JList<>(listModel);
        messageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        messageList.setFont(new Font("Arial", Font.PLAIN, 14));

        messageList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int index = messageList.getSelectedIndex();
                    if (index != -1) {
                        receiver = messageVector.get(index);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(messageList);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        serverPanel.add(scrollPane);

        jFrame.add(JlTitle);
        jFrame.add(jlFileName);
        jFrame.add(jpButton);
        // jFrame.add(serverPanel);
        jFrame.setVisible(true);

        jbChooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setDialogTitle("Choose a file to send");

                if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    fileToSend[0] = jFileChooser.getSelectedFile();
                    jlFileName.setText("The file you want to send is: " + fileToSend[0].getName());
                }
            }
        });

        jbSendFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (receiver.equals("")) {
                    jlFileName.setText("Please Choose a Reciever first.");

                }
                if (fileToSend[0] == null) {
                    jlFileName.setText("Please Choose a file first.");
                } else {
                    try {
                        FileInputStream fileInputStream = new FileInputStream(fileToSend[0].getAbsolutePath());
                        Socket socket = new Socket(receiver, 1234);

                        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                        String fileName = fileToSend[0].getName();
                        byte[] fileNameBytes = fileName.getBytes();

                        byte[] fileContentBytes = new byte[(int) fileToSend[0].length()];
                        fileInputStream.read(fileContentBytes);

                        dataOutputStream.writeInt(fileNameBytes.length);
                        dataOutputStream.write(fileNameBytes);

                        dataOutputStream.writeInt(fileContentBytes.length);
                        dataOutputStream.write(fileContentBytes);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });

        jbRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame jfPreview = createFrame();
                jfPreview.setVisible(true);

            }
        });

    }

    public static void listenForServers() {
        try {
            DatagramSocket socket = new DatagramSocket(BROADCAST_PORT);
            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());

                if (!messageVector.contains(message)) {
                    messageVector.add(message);
                }
                updateMessageList();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateMessageList() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String msg : messageVector) {
            listModel.addElement(msg);
        }
    }

    public static JFrame createFrame() {
        JFrame f = new JFrame();
        final JLabel label = new JLabel();
        label.setSize(500, 100);
        JButton b = new JButton("Select");
        b.setBounds(200, 150, 80, 30);
        final JList<String> list1 = new JList<>(messageVector);
        list1.setBounds(100, 100, 75, 75);
        f.add(list1);
        f.add(b);
        f.add(label);
        f.setSize(450, 450);
        f.setLayout(null);
        f.setVisible(true);
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String data = "";
                if (list1.getSelectedIndex() != -1 && messageVector.size() != 0) {
                    String[] parts = separateIpAndHostName(list1.getSelectedValue());
                    data = "Reciever Selected: " + parts[0];
                    receiver = parts[1];
                    System.out.println(receiver);
                    label.setText(data);
                }

                label.setText(data);
            }
        });

        return f;
    }

    public static String[] separateIpAndHostName(String input) {
        String[] parts = input.split(":");
        String ipAddress = parts[0];
        String hostName = parts[1].trim();
        return new String[] { ipAddress, hostName };

    }
}
