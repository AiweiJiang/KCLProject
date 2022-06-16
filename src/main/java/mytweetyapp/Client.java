package mytweetyapp;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is the main script for implementing user functions, which mainly include:
 * 1. Input inference rules and premises.
 * 2. Receive acceptable arguments from the server.
 * 3. Send rebuttal arguments to the server.
 * 4. Receive the optimal prescription from the server.
 * 
 * @author Jiang Aiwei
 *
 */

public class Client {

    public static void main(String[] args) {

        try {
            //Make a request to port 8888 of the local computer
            Socket socket = new Socket("127.0.0.1", 8888);
            // An input stream 'in' that receives information passed by the server
            InputStream in = socket.getInputStream();
            // An output stream that transmits information to a server
            OutputStream out = socket.getOutputStream();
            
            ArrayList<String> diseaseList = new ArrayList<>();
            diseaseList.add("asthma");
            diseaseList.add("obstructivePulmonary");
            diseaseList.add("COVID");
            diseaseList.add("cysticFibrosis");
            diseaseList.add("pulmonaryFibrosis");
            diseaseList.add("respiratoryInfection");
            diseaseList.add("Tuberculosis");
            diseaseList.add("lungCancer");
            diseaseList.add("mesothelioma");
            diseaseList.add("pneumonia");
            

            // Input from the keybard, allowing agents to input their inference rules
            Scanner input = new Scanner(System.in);
            // Initialzing the flags. When 'flag' is true, the agent can continue to enter inference rules.
            // When 'attackFlag' is true, the agent can enter argument to attack acceptable arguments.
            String flag = "yes";
            String attackFlag = "yes";
            // Start accepting inference rules entered by the user and pass them to the server
            while (flag.trim().equals("yes")) {
            	System.out.println("Welcome to use the argument-based paramedic system!!!");
                System.out.println("please enter inference rules and premises which will help the system make decisions:");
                String str1 = input.nextLine();
                // Transfer the inference rules typed by agents to server
                out.write(str1.getBytes());
                out.flush();
                System.out.println("====================================================");
                System.out.println("Continue to enter inference rules or not:(yes or no)");
                flag = input.nextLine();
                // Transfer the flag which decide whether continue to enter inference rules
                out.write(flag.getBytes());
                out.flush();
                // Receive the processing result of server
                byte[] str = new byte[128];
                in .read(str);
                System.out.println("====================================================");
                System.out.println("The result of the server running: " + new String(str));
                //System.out.println("----------------------------------------------------");
            }
            // To attack an acceptable argument, the user enters an argument. If not, the user gets an acceptable
            // set of arguments.
            while (attackFlag.trim().equals("yes")) {
            	ArrayList<String> accDisease = new ArrayList<>();
            	System.out.println("====================================================");
                System.out.println("According to the rules of inference and the premises, the acceptable arguments are: ");
                // Receive the acceptable arguments of server 
                byte[] resultByte = new byte[1024 * 3]; 
                in .read(resultByte);
                String resultArg = new String(resultByte);
                String[] resultList = resultArg.split(";");
                for (int i = 0; i < resultList.length; i++) {
                    System.out.println(resultList[i]);
                    String[] argSplit = resultList[i].toString().split("\\s+");
        			int flag1 = 0;
        			for(String str:argSplit) {
        				if(!str.equals("=>")) {
        					flag1++;
        				}else {
        					break;
        				}
        			}
        			for(String strDisease: diseaseList) {
        				if(argSplit[flag1 + 1].equals(strDisease)) {
        					accDisease.add(strDisease);
        				}
        			}
                }
                System.out.println("The possible diseases are:");
                for(String disease:accDisease) {
                	System.out.println(disease);
                }
                
                // Decide whether to rebut
                System.out.println("==============================================================");
                System.out.println("Whether to enter arguments to refute (please enter yes or no):");
                attackFlag = input.nextLine();
                out.write(attackFlag.getBytes());
                out.flush();
                if (attackFlag.trim().equals("yes")) {
                	System.out.println("===================================");
                    System.out.println("Please enter the rebuttal argument��");
                    String refutedArgument = input.nextLine();
                    out.write(refutedArgument.getBytes());
                    out.flush();

                } else {
                    // Accept the optimal prescription from the server
                	System.out.println("===================================");
                	System.out.println("According to the position-based ranking, the best prescription is:");
                	byte[] finalMedcinebyte = new byte[256];
                	in.read(finalMedcinebyte);
                	String finalMedicine = new String(finalMedcinebyte);
                	System.out.println(finalMedicine.trim());
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