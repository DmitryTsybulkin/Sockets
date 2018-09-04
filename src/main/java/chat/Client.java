package chat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private BufferedReader in;
    private PrintWriter out;
    private JFrame frame = new JFrame("Chat");
    private JTextField textField = new JTextField(40);
    private JTextArea message = new JTextArea(8, 40);

    public Client() {
        textField.setEditable(false);
        message.setEditable(false);
        frame.getContentPane().add(textField, "North");
        frame.getContentPane().add(new JScrollPane(message), "Center");
        frame.pack();

        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
                textField.setText("");
            }
        });
    }

    public String getServerAddress() {
        return JOptionPane.showInputDialog(frame, "Enter server address: ", JOptionPane.QUESTION_MESSAGE);
    }

    private String getName() {
        return JOptionPane.showInputDialog(frame, "Enter name: ", JOptionPane.PLAIN_MESSAGE);
    }

    private void run() throws IOException {
        Socket socket = new Socket("127.0.0.1", 9090);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        System.out.println(in.readLine());
        while (true) {
            String line = in.readLine();
            if (line.startsWith("SUBMITNAME")) {
                out.println(getName());
            } else if (line.startsWith("NAMEACCEPTED")) {
                textField.setEditable(true);
            } else if (line.startsWith("MESSAGE")) {
                message.append(line.substring(8) + "\n");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }
}
