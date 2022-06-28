package mytweetyapp;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
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
		// Define an array of drugs for later retrieval
		ArrayList<Medicine> medcineList = new ArrayList<>();
		medcineList.add(new Medicine("corticosteroidHormone", 8, 900 , 7));
		medcineList.add(new Medicine("albuterol", 6, 1000, 3));
		medcineList.add(new Medicine("inhaledBeta-2Agonists", 7, 400,5));
		medcineList.add(new Medicine("antimuscarinic", 7, 100,1));
		medcineList.add(new Medicine("steroidInhalers", 8, 800,6));
		medcineList.add(new Medicine("bronchodilators", 2, 145,2));
		medcineList.add(new Medicine("codeineLinctus", 3, 500,18));
		medcineList.add(new Medicine("codeinePhosphate", 6, 1500,39));
		medcineList.add(new Medicine("morphineSulfateOralSolution", 9, 2000,27));
		medcineList.add(new Medicine("paracetamol", 7, 250,21));
		medcineList.add(new Medicine("Ibuprofen", 8, 225,41));
		medcineList.add(new Medicine("antibioticSpray", 5, 350,50));
		medcineList.add(new Medicine("digestiveEnzyme", 4, 290,32));
		medcineList.add(new Medicine("lungTransplantation", 10, 100000,89));
		medcineList.add(new Medicine("Pirefenidone", 5, 120,10));
		medcineList.add(new Medicine("nintedanib", 2, 532,24));
		medcineList.add(new Medicine("oxygenTherapy", 6, 999,53));
		medcineList.add(new Medicine("antibiotics", 1, 322,16));
		medcineList.add(new Medicine("moistOxygen", 6, 352,73));
		medcineList.add(new Medicine("tylenol", 9, 654,93));
		medcineList.add(new Medicine("isoniazid", 3, 807,52));
		medcineList.add(new Medicine("rifampin", 6, 235,16));
		medcineList.add(new Medicine("pyrazizamide", 7, 549,73));
		medcineList.add(new Medicine("ethambutol", 3, 293,48));
		medcineList.add(new Medicine("radiotherapy", 6, 526,36));
		medcineList.add(new Medicine("chemotherapy", 3, 937,84));
		medcineList.add(new Medicine("immunotherapy", 7, 829,39));
		medcineList.add(new Medicine("painkiller", 2, 550,38));
		medcineList.add(new Medicine("drinkplenty", 3, 203,64));
		
    	try {
    		// An input stream that receives information from the server
            InputStream in = socket.getInputStream();
            DataInputStream dis = new DataInputStream(in);
            // An output stream that transmits data to the client 
            OutputStream out = socket.getOutputStream();
            boolean flag = true;
            // When flag is true, the handler will always accept the command
            while (flag == true) {
                byte[] command = new byte[256];
                in.read(command);
                String commandStr = new String(command);
                // When the command is upload, hanlder uploads the content sent by the client to a local file
                if (commandStr.trim().equals("Upload")) {
                	int length = dis.readInt();
                	// If the input length is 0, an error is reported
					if (length == 0) {
						System.err.println("Error: client enter null rules!");
					}else {
						// Receive the rule passed by the client
						byte[] rulesByte = new byte[1024*3];
						in.read(rulesByte);
						String rulesStr = new String(rulesByte);
						System.out.println("The inference rules which is passed from client is:" + rulesStr);
						// Extract symptoms from the incoming string
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
		                // Returns a success result to the client
		                byte[] resultByte = "successful".getBytes();
		                out.write(resultByte);
		                System.out.println("Data has been written successfully!");
					}
					// If the command that is passed is start
					// then reasoning starts based on the rules in the local file
				}else if (commandStr.trim().equals("Start")) {
					// Receive medicine selection preferences
					byte[] choice = new byte[256];
					in.read(choice);
					String mode = new String(choice);
					// Initialize extension to determine if the argument is acceptable
					Extension<DungTheory> Ex1 = new Extension<DungTheory>();
			        // Initialize an instance of BuiltAT that creates argumentation Theory 
			        // according to inference rules in the ASPIC document.
			        BulidAT bAt = new BulidAT();
			        AspicArgumentationTheory < PlFormula > t = bAt.buildArgT();
			        DungTheory aaf = t.asDungTheory();
			        // Content in argList is acceptable arguments
			        ArrayList < Argument > argList = new ArrayList < > ();
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
			        
			        String accArg = "";
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
	                System.out.println(resultByte.length);
	                out.write(resultByte);
	                out.flush();
			        // An argument system based on valueArgument to select results from different preferences
			        ArrayList<VauleArgument> VAlist = new ArrayList<>();
			        for (Argument accarg: accArgList) {
			        	//System.out.println(accarg);
			        	String[] argSplit = accarg.toString().split("\\s+");
			        	int flag0 = 0;
						for(String str:argSplit) {
							if(!str.equals("=>")) {
								flag0++;
							}else {
								break;
							}
						}
						// Create valueargument objects
						if (argSplit[flag0 + 1].trim().equals("cured")) {
							String action = argSplit[1];
							String purpose = argSplit[flag0 + 1];
							String value = null;
							boolean statu = true;
							
							VauleArgument VA = new VauleArgument(action, purpose, value, statu);
							VAlist.add(VA);
						}
			        }
			        // Find where => is used to locate other elements
			        for (Argument accarg: accArgList) {
			        	String[] argSplit = accarg.toString().split("\\s+");
			        	int flag1 = 0;
						for(String str:argSplit) {
							if(!str.equals("=>")) {
								flag1++;
							}else {
								break;
							}
						}
						for (Medicine med:medcineList) {
							if (argSplit[1].trim().equals(med.name) && !argSplit[flag1 + 1].trim().equals("cured")) {
								String value = argSplit[flag1 + 1];
							    for (VauleArgument Varg: VAlist) {
							    	if(Varg.getAction().trim().equals(argSplit[1])) {
							    		Varg.value = value;
							    	}
							    }
							}
						}
			        }
			        
			        String perference = mode;
			        for (int i = 0; i < VAlist.size(); i++) {
			        	for (int j = i+1;j < VAlist.size(); j++) {
			        		if(perference.indexOf(VAlist.get(i).value) < perference.indexOf(VAlist.get(j).value)) {
			        			VAlist.get(i).isDefeated = true;
			        			System.out.println(VAlist.get(j).action + " defeat " + VAlist.get(i).action);
			        		}else if (perference.indexOf(VAlist.get(i).value) > perference.indexOf(VAlist.get(j).value)){
			        			VAlist.get(j).isDefeated = true;
			        			System.out.println(VAlist.get(i).action + " defeat " + VAlist.get(j).action);
			        		}
			        	}
			        }
			        ArrayList<VauleArgument> accVAList = new ArrayList<>();
			        for (VauleArgument VArg: VAlist) {
			        	if(VArg.isDefeated == false) {
			        		System.out.println(VArg.action);
			        		accVAList.add(VArg);
			        	}
			        }
					
			        if (accVAList.size() == 1) {
			        	out.write(accVAList.get(0).action.getBytes());
			        	out.flush();
			        }else {
			        	Medicine finalMedcine = null;
		                RankbasedOnPosition rank = new RankbasedOnPosition(accVAList);
		                finalMedcine = rank.bestMedicine();
		                out.write(finalMedcine.getName().getBytes());
		                out.flush();
			        }
	                
	                
				}else if (commandStr.trim().equals("Attack")) {
					byte[] lengthByte = new byte[256];
					in.read(lengthByte);
					String lengthStr = new String(lengthByte);
					int length = Integer.valueOf(lengthStr.trim()).intValue();
					if (length == 0) {
						System.err.println("Error: client enter null rules!");
					}else {
						byte[] choice = new byte[256];
						in.read(choice);
						String mode = new String(choice);
						
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
		                
		                ArrayList<VauleArgument> VAlist = new ArrayList<>();
				        for (Argument accarg: accArgList) {
				        	//System.out.println(accarg);
				        	String[] argSplit = accarg.toString().split("\\s+");
				        	int flag0 = 0;
							for(String str:argSplit) {
								if(!str.equals("=>")) {
									flag0++;
								}else {
									break;
								}
							}
							if (argSplit[flag0 + 1].trim().equals("cured")) {
								String action = argSplit[1];
								String purpose = argSplit[flag0 + 1];
								String value = null;
								boolean statu = true;
								
								VauleArgument VA = new VauleArgument(action, purpose, value, statu);
								VAlist.add(VA);
							}
				        }
				        
				        for (Argument accarg: accArgList) {
				        	String[] argSplit = accarg.toString().split("\\s+");
				        	int flag1 = 0;
							for(String str:argSplit) {
								if(!str.equals("=>")) {
									flag1++;
								}else {
									break;
								}
							}
							for (Medicine med:medcineList) {
								if (argSplit[1].trim().equals(med.name) && !argSplit[flag1 + 1].trim().equals("cured")) {
									String value = argSplit[flag1 + 1];
								    for (VauleArgument Varg: VAlist) {
								    	if(Varg.getAction().trim().equals(argSplit[1])) {
								    		Varg.value = value;
								    	}
								    }
								}
							}
				        }
				        
				        String perference = mode;
				        for (int i = 0; i < VAlist.size(); i++) {
				        	for (int j = i+1;j < VAlist.size(); j++) {
				        		if(perference.indexOf(VAlist.get(i).value) < perference.indexOf(VAlist.get(j).value)) {
				        			VAlist.get(i).isDefeated = true;
				        			System.out.println(VAlist.get(j).action + " defeat " + VAlist.get(i).action);
				        		}else if (perference.indexOf(VAlist.get(i).value) > perference.indexOf(VAlist.get(j).value)){
				        			VAlist.get(j).isDefeated = true;
				        			System.out.println(VAlist.get(i).action + " defeat " + VAlist.get(j).action);
				        		}
				        	}
				        }
				        ArrayList<VauleArgument> accVAList = new ArrayList<>();
				        for (VauleArgument VArg: VAlist) {
				        	if(VArg.isDefeated == false) {
				        		System.out.println(VArg.action);
				        		accVAList.add(VArg);
				        	}
				        }
						
				        if (accVAList.size() == 1) {
				        	out.write(accVAList.get(0).action.getBytes());
				        	out.flush();
				        }else {
				        	Medicine finalMedcine = null;
			                RankbasedOnPosition rank = new RankbasedOnPosition(accVAList);
			                finalMedcine = rank.bestMedicine();
			                out.write(finalMedcine.getName().getBytes());
			                out.flush();
				        }
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