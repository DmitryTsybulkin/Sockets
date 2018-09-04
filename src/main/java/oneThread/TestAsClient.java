package oneThread;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestAsClient {
    public static void main(String[] args) throws InterruptedException {
        try (Socket socket = new Socket("localhost", 3345)){
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());

            System.out.println("Client initialized and connected to socket.");

            while (!socket.isOutputShutdown()) {
                if (bufferedReader.ready()) {
                    System.out.println("Client start writing in channel...");
                    Thread.sleep(1000);
                    String clientCommand = bufferedReader.readLine();

                    outputStream.writeUTF(clientCommand);
                    outputStream.flush();
                    System.out.println("Client sent message: " + clientCommand + " to server.");
                    Thread.sleep(1000);

                    if (clientCommand.equalsIgnoreCase("quit")) {
                        System.out.println("Client kill connection.");
                        Thread.sleep(2000);

                        if (inputStream.read() > -1) {
                            System.out.println("Reading...");
                            String in = inputStream.readUTF();
                            System.out.println(in);
                        }
                        break;
                    }
                    System.out.println("Client sent message & start waiting for data from server...");
                    Thread.sleep(2000);

                    // получение от сервера сообщения после выполнения команды
                    if (inputStream.read() > -1) {
                        System.out.println("Reading!..");
                        String in = inputStream.readUTF();
                        System.out.println(in);
                    }
                }
            }

            System.out.println("Client kill connection.");

            inputStream.close();
            outputStream.close();
            bufferedReader.close();

        } catch (UnknownHostException e) {
            e.getLocalizedMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
