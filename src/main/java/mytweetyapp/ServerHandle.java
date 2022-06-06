package mytweetyapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import org.tweetyproject.arg.aspic.syntax.AspicArgumentationTheory;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import javassist.expr.NewArray;

public class ServerHandle extends Thread {

    private Socket socket;
    public ServerHandle(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // An input stream that receives information from the server
            InputStream in = socket.getInputStream();
            //BufferedReader br = new BufferedReader(new InputStreamReader(in));
            // An output stream that transmits data to the client 
            OutputStream out = socket.getOutputStream();
           // PrintWriter pw = new PrintWriter(out);
            
            System.out.println("The server is processing。。。");
            // Initialzing the list 'argumentList' which stores the arguments
            String[] argumentList = null;
            // Initialize arguments whether to continue input parameters and whether to attack
            String flag = "yes";
            String attackFlag = "yes";
            // Accept arguments passed from the client
            while (flag.trim().equals("yes")) {
	        	byte[] str = new byte[1024];
	        	in.read(str);
                System.out.println("The arguments passed from client：" + new String(str));
                // Separate arguments sent by the client with commas, then store the arguments into
                // comma-separated List 'argumentList'
                String str1 = new String(str);
                argumentList = str1.split(",");
                // Write each argument to a local ASPIC document
                try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("C:\\Users\\dell\\Desktop\\aspictest.txt", true))) {
                    for (int i = 0; i <= argumentList.length - 1; i++) {
                        if(i != argumentList.length - 1) {
                        	bufferedWriter.write(argumentList[i].trim() + "\r\n");
                        	bufferedWriter.flush();
                        }
                    }
                }
                
                byte[] s = new byte[64];
                s = "Data has been writen".getBytes();
                out.write(s);
                out.flush();
                
                byte[] flagStr = new byte[256];
                in.read(flagStr);
                flag = new String(flagStr);
                
                
            }
            // Determine if there is a point to attack。
            while (attackFlag.trim().equals("yes")) {
				System.out.println("Start reasoning");
				// Initialize an instance of BuiltAT that creates argumentation Theory 
				// according to inference rules in the ASPIC document.
				BulidAT bAt = new BulidAT();
				AspicArgumentationTheory<PlFormula> t = bAt.buildArgT();
				DungTheory aaf = t.asDungTheory();
				// 把argList里的内容换成可接受的论点
				ArrayList<Argument> argList = new ArrayList<>();
				String accArg = "";
				for(Argument arg:aaf) {
					argList.add(arg);
				}
				
				for(int i = 0; i < argList.size(); i++ ) {
					if(i != argList.size() - 1) {
						accArg += argList.get(i).toString() + ";";
					}else {
						accArg += argList.get(i).toString();
					}
				}
				System.out.println(accArg);
				byte[] resultByte = accArg.getBytes();
				out.write(resultByte);
				
				byte[] attackFlagByte = new byte[256];
				in.read(attackFlagByte);
				attackFlag = new String(attackFlagByte);
				if (attackFlag.trim().equals("yes")) {
					byte[] refutedArgumentByte = new byte[1024];
					in.read(refutedArgumentByte);
					String refutedArgument = new String(refutedArgumentByte);
					System.out.println("The rebuttal argument is: " + refutedArgument);
					String[] rebuttalList = refutedArgument.split(",");
					try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("C:\\Users\\dell\\Desktop\\aspictest.txt", true))) {
	                    for (int i = 0; i <= rebuttalList.length - 1; i++) {
	                    	 if(i != rebuttalList.length - 1) {
	                         	bufferedWriter.write(rebuttalList[i].trim() + "\r\n");
	                         	bufferedWriter.flush();
	                         }
	                    }
	                }
				}
			}

            // 关闭数据流
            in.close(); 
            out.close();
            System.out.println("服务器处理完成。。。");
            socket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}