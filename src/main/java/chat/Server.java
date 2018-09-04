package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class Server {

    private static final int PORT = 9090;
    private static final HashSet<String> names = new HashSet<>();
    private static HashSet<PrintWriter> printWriters = new HashSet<>();

    public static void main(String[] args) throws IOException {
        System.out.println("The chat server is running");
        try (ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                new Handler(server.accept()).start();
                System.out.println("User connected.");
            }
        }
    }

    public static class Handler extends Thread {
        private String name;
        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream());

                while (true) {
                    writer.println("SUBMITNAME");
                    System.out.println("anume");
                    name = reader.readLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (names) {
                        if (!names.contains(name)) {
                            names.add(name);
                            break;
                        }
                    }
                }
                writer.println("NAMEACCEPTED");
                printWriters.add(writer);

                while (true) {
                    String input = reader.readLine();
                    if (input == null) {
                        return;
                    }
                    for (PrintWriter printWriter : printWriters) {
                        printWriter.println("MESSAGE" + name + " : " + input);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (name != null) {
                    names.remove(name);
                }
                if (writer != null) {
                    printWriters.remove(writer);
                }
            }
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }

}
