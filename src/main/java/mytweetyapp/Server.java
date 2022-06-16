package mytweetyapp;

import java.net.*;
import java.io.*;

/**
 * This class enables listening on local port 8888
 * which is passed to ServerHandle when listening for client access
 * 
 * @author Jiang Aiwei
 *
 */

public class Server extends Thread {
    // The server will start listening on the local port 8888
    private static int portNumber = 8888;

    public void run() {
        try {
            // initialzing the server
            ServerSocket server = new ServerSocket(portNumber);
            System.out.println("The server is started successfully. The port number is: " + portNumber);
            while (true) {
                Socket socket = server.accept();
                if (socket != null) {
                    new ServerHandle(socket).start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        new Thread(new Server()).start();
    }

}