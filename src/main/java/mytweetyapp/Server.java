package mytweetyapp;

import java.net.*;
import java.io.*;

public class Server extends Thread {
    // ����������ʼ�������ض˿� 8888
    private static int portNumber = 8888;

    @Override
    public void run() {
        try {
            ServerSocket server = new ServerSocket(portNumber);
            System.out.println("���������ɹ����˿ں�Ϊ" + portNumber);
            while (true) {
                Socket socket = server.accept();
                if (socket != null) {
                    new ServerHandle(socket).start();
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        new Thread(new Server()).start();
    }

}