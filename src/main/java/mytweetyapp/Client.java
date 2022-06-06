package mytweetyapp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        try {
            //Make a request to port 8888 of the local computer
            Socket socket = new Socket("127.0.0.1", 8888);
            // An input stream 'in' that receives information passed by the server
            InputStream in = socket.getInputStream();
            //BufferedReader br = new BufferedReader(new InputStreamReader(in));
            // An output stream that transmits information to a server
            OutputStream out = socket.getOutputStream();
            //PrintWriter pw = new PrintWriter(out);


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
                out.write(str1.getBytes());
                out.flush();
                System.out.println("Continue to enter parameters or not:(yes or no)");
                flag = input.nextLine();
                out.write(flag.getBytes());
                out.flush();
                byte[] str = new byte[24];
                in.read(str);
                System.out.println("The result of the server running: " + new String(str));
            }
            // To attack an acceptable argument, the user enters an argument. If not, the user gets an acceptable
            // set of arguments.
            while (attackFlag.trim().equals("yes")) {
            	
            	System.out.println("The acceptable arguments: ");
            	byte[] resultByte = new byte[1024];
            	in.read(resultByte);
            	String resultArg = new String(resultByte);
            	//System.out.println(resultArg);
            	String[] resultList = resultArg.split(";");
            	for(int i = 0; i < resultList.length; i++) {
            		System.out.println(resultList[i]);
            	}
            	// Decide whether to rebut
            	System.out.println("Whether to enter arguments to refute (please enter yes or not):");
				attackFlag = input.nextLine();
				out.write(attackFlag.getBytes());
				out.flush();
				if (attackFlag.trim().equals("yes")) {
					System.out.println("Please enter the rebuttal argument£º");
					String refutedArgument = input.nextLine();
					out.write(refutedArgument.getBytes());
					out.flush();

				}else {
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