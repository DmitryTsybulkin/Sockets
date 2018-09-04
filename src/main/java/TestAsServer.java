import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TestAsServer {
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(3345)) {
            Socket client = server.accept();
            System.out.println("Connection accepted.");

            DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
            DataInputStream inputStream = new DataInputStream(client.getInputStream());

            while (!client.isClosed()) {
                System.out.println("Server reading info from channel...");
                String entry = inputStream.readUTF();
                System.out.println("Read from client message: " + entry);
                System.out.println("Server trying writing to channel...");

                if (entry.equalsIgnoreCase("quit")) {
                    System.out.println("Client initialize closing of connection.");
                    outputStream.writeUTF("Server reply " + entry + " - OK");
                    outputStream.flush();

                    Thread.sleep(3000);
                    break;
                }

                outputStream.writeUTF("Server reply " + entry + " - OK");
                System.out.println("Server Wrote message to client.");
                outputStream.flush();
            }

            System.out.println("Client disconnected. Closing connection...");

            inputStream.close();
            outputStream.close();

            client.close();

            System.out.println("Connection is closed.");
        } catch (InterruptedException e) {
            e.getLocalizedMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
