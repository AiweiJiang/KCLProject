package mytweetyapp;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.chrono.ThaiBuddhistChronology;
import java.util.ArrayList;

import org.jgrapht.alg.spanning.EsauWilliamsCapacitatedMinimumSpanningTree;
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
            boolean flag = true;
            
            while (flag == true) {
                byte[] command = new byte[256];
                in.read(command);
                String commandStr = new String(command);
                
                if (commandStr.trim().equals("Upload")) {
                	byte[] lengthByte = new byte[256];
					in.read(lengthByte);
					String lengthStr = new String(lengthByte);
					//System.out.println(lengthStr);
					int length = Integer.valueOf(lengthStr.trim()).intValue();
					if (length == 0) {
						System.err.println("Error: client enter null rules!");
					}else {
						byte[] rulesByte = new byte[1024*3];
						in.read(rulesByte);
						String rulesStr = new String(rulesByte);
						System.out.println("The inference rules which is passed from client is:" + rulesStr);
						ExtractSymptom ES = new ExtractSymptom(rulesStr);
						ArrayList<String> symptoms = ES.extract();
						// Write each argument to a local ASPIC text file
		                try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("C:\\Users\\dell\\Desktop\\aspictest.txt", true))) {
		                	for (int i = 0; i <= symptoms.size() - 1; i++) {
		                    	if (i == 0) {
		                    		bufferedWriter.newLine();
		                    		bufferedWriter.write(symptoms.get(i).trim() + "\r\n");
		                    		bufferedWriter.flush();
		                    	}else if (i != symptoms.size() - 1 && i != 0) {
		                            bufferedWriter.write(symptoms.get(i).trim() + "\r\n");
		                            bufferedWriter.flush();
		                        }else if (i == symptoms.size() - 1) {
		                        	bufferedWriter.write(symptoms.get(i).trim());
		                            bufferedWriter.flush();
								}
		                    }
		                }
		                byte[] resultByte = "successful".getBytes();
		                out.write(resultByte);
		                System.out.println("Data has been written successfully!");
					}
					
				}else if (commandStr.trim().equals("Start")) {
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
	                out.flush();
	                Medicine finalMedcine = null;
	                RankbasedOnPosition rank = new RankbasedOnPosition(accArgList);
	                finalMedcine = rank.bestMedicine();
	                out.write(finalMedcine.getName().getBytes());
	                out.flush();
				}else if (commandStr.trim().equals("Attack")) {
					byte[] lengthByte = new byte[256];
					in.read(lengthByte);
					String lengthStr = new String(lengthByte);
					int length = Integer.valueOf(lengthStr.trim()).intValue();
					if (length == 0) {
						System.err.println("Error: client enter null rules!");
					}else {
						byte[] rulesByte = new byte[1024*3];
						in.read(rulesByte);
						String rulesStr = new String(rulesByte);
						System.out.println("The rebuts rules which is passed from client is:" + rulesStr);
						ExtractSymptom ES = new ExtractSymptom(rulesStr);
						ArrayList<String> symptoms = ES.extract();
						// Write each argument to a local ASPIC text file
		                try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("C:\\Users\\dell\\Desktop\\aspictest.txt", true))) {
		                    for (int i = 0; i <= symptoms.size() - 1; i++) {
		                    	if (i == 0) {
		                    		bufferedWriter.newLine();
		                    		bufferedWriter.write(symptoms.get(i).trim() + "\r\n");
		                    		bufferedWriter.flush();
		                    	}else if (i != symptoms.size() - 1 && i != 0) {
		                            bufferedWriter.write(symptoms.get(i).trim() + "\r\n");
		                            bufferedWriter.flush();
		                        }else if (i == symptoms.size() - 1) {
		                        	bufferedWriter.write(symptoms.get(i).trim());
		                            bufferedWriter.flush();
								}
		                    }
		                }
		                
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
		                out.flush();
		                Medicine finalMedcine = null;
		                RankbasedOnPosition rank = new RankbasedOnPosition(accArgList);
		                finalMedcine = rank.bestMedicine();
		                out.write(finalMedcine.getName().getBytes());
		                out.flush();
					}
					
				}else if (commandStr.trim().equals("close")) {
					flag = false;
				}
				
			}
            System.out.println("Procession complete!");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}