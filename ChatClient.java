import java.io.*;
import java.net.*;

/*
 * Program Name : ChatClient
 * Description  : Chat client using Java sockets
 * Author       : Jayashree A
 */

public class ChatClient {

    public static void main(String[] args) {

        try {
            Socket socket = new Socket("localhost", 1234);

            BufferedReader serverIn =
                new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter serverOut =
                new PrintWriter(socket.getOutputStream(), true);

            BufferedReader userInput =
                new BufferedReader(new InputStreamReader(System.in));

            // Thread to read messages from server
            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = serverIn.readLine()) != null) {
                        System.out.println("Message: " + msg);
                    }
                } catch (IOException e) {}
            }).start();

            // Send user messages
            String input;
            while ((input = userInput.readLine()) != null) {
                serverOut.println(input);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
