package mytweetyapp;

import java.net.*;
import java.io.*;

public class Server extends Thread {
    // 服务器将开始监听本地端口 8888
    private static int portNumber = 8888;

    @Override
    public void run() {
        try {
            ServerSocket server = new ServerSocket(portNumber);
            System.out.println("服务启动成功：端口号为" + portNumber);
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