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

public class ServerHandle1 {
	private Socket socket;
    public ServerHandle1(Socket socket) {
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
					byte[] rulesByte = new byte[1024*3];
					in.read(rulesByte);
					String rulesStr = new String(rulesByte);
					String[] rulesSplit = rulesStr.split(",");
					// Write each argument to a local ASPIC text file
	                try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("C:\\Users\\dell\\Desktop\\aspictest.txt", true))) {
	                    for (int i = 0; i <= rulesSplit.length - 1; i++) {
	                    	if (i == 0) {
	                    		bufferedWriter.newLine();
	                    		bufferedWriter.write(rulesSplit[i].trim() + "\r\n");
	                    		bufferedWriter.flush();
	                    	}
	                        if (i != rulesSplit.length - 1) {
	                            bufferedWriter.write(rulesSplit[i].trim() + "\r\n");
	                            bufferedWriter.flush();
	                        }else {
	                        	bufferedWriter.write(rulesSplit[i].trim());
	                            bufferedWriter.flush();
							}
	                    }
	                }
	                
	                byte[] resultByte = "successful".getBytes();
	                out.write(resultByte);
				}
				
			}
            
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
