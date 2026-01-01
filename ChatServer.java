import java.io.*;
import java.net.*;
import java.util.*;

/*
 * Program Name : ChatServer
 * Description  : Multithreaded chat server using Java sockets
 * Author       : Jayashree A
 */

public class ChatServer {

    // Store all client output streams
    private static Set<PrintWriter> clientWriters = new HashSet<>();

    public static void main(String[] args) {
        System.out.println("Chat Server started...");

        try (ServerSocket serverSocket = new ServerSocket(1234)) {

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected!");

                // Create new thread for each client
                new ClientHandler(clientSocket).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Thread class for handling clients
    static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                synchronized (clientWriters) {
                    clientWriters.add(out);
                }

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Received: " + message);
                    broadcast(message);
                }

            } catch (IOException e) {
                System.out.println("Client disconnected.");
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {}
                synchronized (clientWriters) {
                    clientWriters.remove(out);
                }
            }
        }

        // Broadcast message to all clients
        private void broadcast(String message) {
            synchronized (clientWriters) {
                for (PrintWriter writer : clientWriters) {
                    writer.println(message);
                }
            }
        }
    }
}
