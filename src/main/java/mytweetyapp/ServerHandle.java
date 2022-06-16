package mytweetyapp;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.chrono.ThaiBuddhistChronology;
import java.util.ArrayList;
import org.tweetyproject.arg.aspic.syntax.AspicArgumentationTheory;
import org.tweetyproject.arg.dung.semantics.Extension;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.Attack;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.logics.pl.syntax.PlFormula;

/**
 * This class is the main script for implementing server functions, which mainly include:
 * 1. Receive inference rules and premises from client.
 * 2. Write the rules and premises to a local file and parse it to build a argumentation theory.
 * 3. Convert ASPIC+ Argumentation theory to Dung theory and get the acceptable arguments.
 * 2. Send acceptable arguments to the client.
 * 3. Receive rebuttal arguments from the client.
 * 4. Send the optimal prescription to the client.
 * 
 * @author Jiang Aiwei
 *
 */

public class ServerHandle extends Thread {

    private Socket socket;
    public ServerHandle(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            // An input stream that receives information from the server
            InputStream in = socket.getInputStream();
            // An output stream that transmits data to the client 
            OutputStream out = socket.getOutputStream();

            System.out.println("The server is processing。。。");
            // Initialzing the list 'argumentList' which stores the arguments
            String[] argumentList = null;
            // Initialize arguments whether to continue input parameters and whether to attack
            String flag = "yes";
            String attackFlag = "yes";
            // determine whether continue to enter inference rules
            while (flag.trim().equals("yes")) {
            	// Accept inference rules passed from the client
                byte[] str = new byte[1024*3];
                in .read(str);
                System.out.println("The inference rules passed from client��" + new String(str));
                // Separate arguments sent by the client with commas, then store the arguments into
                // comma-separated List 'argumentList'
                String str1 = new String(str);
                argumentList = str1.split(",");
                
                // Write each argument to a local ASPIC text file
                try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("C:\\Users\\dell\\Desktop\\aspictest.txt", true))) {
                    for (int i = 0; i <= argumentList.length - 1; i++) {
                        if (i != argumentList.length - 1) {
                            bufferedWriter.write(argumentList[i].trim() + "\r\n");
                            bufferedWriter.flush();
                        }else {
                        	bufferedWriter.write(argumentList[i].trim());
                            bufferedWriter.flush();
						}
                    }
                }
                
                // Transfer "data has been writen" to client side
                //byte[] s = new byte[64];
                byte[] s = "Execution complete! Data has been writen".getBytes();
                out.write(s);
                out.flush();
                // Receive the flag which decide whether to continue entering the inference rules
                byte[] flagStr = new byte[256]; in .read(flagStr);
                flag = new String(flagStr);

            }
            // Determine if there is a point to attack。
            while (attackFlag.trim().equals("yes")) {
            	Extension<DungTheory> Ex1 = new Extension<DungTheory>();
                System.out.println("Start reasoning");
                // Initialize an instance of BuiltAT that creates argumentation Theory 
                // according to inference rules in the ASPIC document.
                BulidAT bAt = new BulidAT();
                AspicArgumentationTheory < PlFormula > t = bAt.buildArgT();
                DungTheory aaf = t.asDungTheory();
                // Content in argList is acceptable arguments
                ArrayList < Argument > argList = new ArrayList < > ();
                String accArg = "";
                for (Argument arg: aaf) {
                	Ex1.add(arg);
                    argList.add(arg);
                }
                ArrayList<Argument> accArgList = new ArrayList<>();
                for(Argument arg:aaf) {
                	if(aaf.isAcceptable(arg,Ex1)) {
                		accArgList.add(arg);
                	}
                }
                // Separate arguments by colons
                for (int i = 0; i < accArgList.size(); i++) {
                    if (i != accArgList.size() - 1) {
                        accArg += accArgList.get(i).toString() + ";";
                    } else {
                        accArg += accArgList.get(i).toString();
                    }
                }
                System.out.println(accArg);
                System.out.println("The attacks in the argumentation system is:");
                for(Attack at:aaf.getAttacks()) {
                	System.out.println(at);
                }
                // Transmit acceptable arguments to client side
                byte[] resultByte = accArg.getBytes();
                out.write(resultByte);
                // Receive the flag which determine whether to enter argument to rebut
                byte[] attackFlagByte = new byte[256];
                in .read(attackFlagByte);
                attackFlag = new String(attackFlagByte);
                // Have an attack argument
                if (attackFlag.trim().equals("yes")) {
                    byte[] refutedArgumentByte = new byte[1024]; in .read(refutedArgumentByte);
                    String refutedArgument = new String(refutedArgumentByte);
                    System.out.println("The rebuttal argument is: " + refutedArgument);
                    String[] rebuttalList = refutedArgument.split(",");
                    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("C:\\Users\\dell\\Desktop\\aspictest.txt", true))) {
                        for (int i = 0; i <= rebuttalList.length - 1; i++) {
                        	if(i == 0) {
                        		bufferedWriter.newLine();
                        		bufferedWriter.write(rebuttalList[i].trim());
                        		bufferedWriter.flush();
                        	}else if (i != rebuttalList.length - 1) {
                        		bufferedWriter.write(rebuttalList[i].trim() + "\r\n");
                                bufferedWriter.flush();
							}else if (i == rebuttalList.length - 1) {
                            	bufferedWriter.write(argumentList[i].trim());
                                bufferedWriter.flush();
							}
                        }    
                    }
                }else {
                	   // Deliver the optimal prescription to the client
                	   Medicine finalMedicine = null;
                	   RankbasedOnPosition rank = new RankbasedOnPosition(accArgList);
                	   finalMedicine = rank.bestMedicine();
                	   byte[] medicineByte = finalMedicine.name.getBytes();
                	   out.write(medicineByte);
					   break;
				}
            }

            // close the data stream
            in .close();
            out.close();
            System.out.println("Server processing complete。。。");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}