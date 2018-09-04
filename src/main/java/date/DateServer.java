package date;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class DateServer {

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(9090)) {
            try (Socket client = server.accept()) {
                PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
                writer.println(new Date().toString());
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

}
