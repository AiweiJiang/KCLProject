package mytweetyapp;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        try {
            //向本机的8888端口发出客户请求
            Socket socket = new Socket("127.0.0.1", 8888);
            // 接受服务器端传过来的信息
            InputStream in = socket.getInputStream(); //接受服务器发来的数据
            // 将agent输入的arguments发送给服务器端
            OutputStream out = socket.getOutputStream();
            // 定义键盘输入，从而使agent输入arguments
            Scanner input = new Scanner(System.in);
            String flag = "yes";
            String attackFlag = "yes";
            while (flag.trim().equals("yes")) {
                System.out.println("please enter your arguments");
                String str1 = input.nextLine();
                out.write(str1.getBytes());
                System.out.println("Continue to enter parameters or not:(yes or no)");
                flag = input.nextLine();
                out.write(flag.getBytes());
                out.flush();
                byte[] str = new byte[64];
                in .read(str);
                System.out.println("服务器发来的数据：" + new String(str));
            }
            while (attackFlag.trim().equals("yes")) {
            	byte[] argumentByte = new byte[256];
            	in.read(argumentByte);
            	System.out.println("可接受的论点为： " + new String(argumentByte));
            	System.out.println("是否输入论点以反驳(please enter yes or not):");
				attackFlag = input.nextLine();
				out.write(attackFlag.getBytes());
				out.flush();
				if (attackFlag.trim().equals("yes")) {
					System.out.println("请输入你用以反驳的观点：");
					String refutedArgument = input.nextLine();
					out.write(refutedArgument.getBytes());
					out.flush();
				}else {
					break;
				}
				
			}
            
            // 关闭数据流
            in .close();
            out.flush(); //刷新缓冲区
            out.close();
            input.close();
            socket.close(); //关闭Socket
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}