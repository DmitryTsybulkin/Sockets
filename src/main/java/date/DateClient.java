package date;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class DateClient {

    public static void main(String[] args) throws IOException {
        String serverAddress = JOptionPane.showInputDialog("Enter server address: ");
        Socket socket = new Socket(serverAddress, 9090);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String answer = reader.readLine();
        JOptionPane.showMessageDialog(null, answer);
        System.exit(0);
    }

}
