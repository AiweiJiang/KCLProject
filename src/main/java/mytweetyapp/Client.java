package mytweetyapp;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        try {
            //Make a request to port 8888 of the local computer
            Socket socket = new Socket("127.0.0.1", 8888);
            // An input stream 'in' that receives information passed by the server
            InputStream in = socket.getInputStream();
            // An output stream that transmits information to a server
            OutputStream out = socket.getOutputStream();

            // Input from the keybard, allowing agents to input their inference rules
            Scanner input = new Scanner(System.in);
            // Initialzing the flags. When 'flag' is true, the agent can continue to enter inference rules.
            // When 'attackFlag' is true, the agent can enter argument to attack acceptable arguments.
            String flag = "yes";
            String attackFlag = "yes";
            // Start accepting inference rules entered by the user and pass them to the server
            while (flag.trim().equals("yes")) {
                System.out.println("please enter your arguments");
                String str1 = input.nextLine();
                // Transfer the inference rules typed by agents to server
                out.write(str1.getBytes());
                out.flush();
                System.out.println("Continue to enter parameters or not:(yes or no)");
                flag = input.nextLine();
                // Transfer the flag which decide whether continue to enter inference rules
                out.write(flag.getBytes());
                out.flush();
                // Receive the processing result of server
                byte[] str = new byte[24];
                in .read(str);
                System.out.println("The result of the server running: " + new String(str));
            }
            // To attack an acceptable argument, the user enters an argument. If not, the user gets an acceptable
            // set of arguments.
            while (attackFlag.trim().equals("yes")) {
                System.out.println("The acceptable arguments: ");
                // Receive the acceptable arguments of server 
                byte[] resultByte = new byte[1024]; 
                in .read(resultByte);
                String resultArg = new String(resultByte);
                String[] resultList = resultArg.split(";");
                for (int i = 0; i < resultList.length; i++) {
                    System.out.println(resultList[i]);
                }
                // Decide whether to rebut
                System.out.println("Whether to enter arguments to refute (please enter yes or not):");
                attackFlag = input.nextLine();
                out.write(attackFlag.getBytes());
                out.flush();
                if (attackFlag.trim().equals("yes")) {
                    System.out.println("Please enter the rebuttal argument：");
                    String refutedArgument = input.nextLine();
                    out.write(refutedArgument.getBytes());
                    out.flush();

                } else {
                    // 客户端应该接受服务器发送过来的最终答案
                    break;
                }

            }

            // close streams
            in .close();
            out.flush(); // Flash the buffer
            out.close();
            input.close();
            socket.close(); // close the socket
        } catch (Exception e) {
            // handle exception
            e.printStackTrace();
        }
    }
}