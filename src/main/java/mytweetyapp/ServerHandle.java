package mytweetyapp;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerHandle extends Thread {

    private Socket socket;
    public ServerHandle(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // 客户端数据接受流
            InputStream in = socket.getInputStream();
            // 服务器端数据输出流
            OutputStream out = socket.getOutputStream();
            System.out.println("服务器正在处理。。。");
            // 分割传过来的arguments储存到argumentList变量中去
            String[] argumentList = null;
            // 用来控制是否继续输入论点的变量flag
            String flag = "yes";
            String attackFlag = "yes";
            while (flag.trim().equals("yes")) {
                byte[] str = new byte[1024]; in .read(str);
                System.out.println("客户端输入的论点：" + new String(str));
                // 将客户端发送过来的信息以逗号分段
                String str1 = new String(str);
                argumentList = str1.split(",");
                // 将每一段写进ASPIC文件中去
                try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("C:\\Users\\dell\\Desktop\\AspicTest.aspic", true))) {
                    for (int i = 0; i <= argumentList.length - 1; i++) {
                        bufferedWriter.write(argumentList[i] + "\n");
                    }
                }
                byte[] s = new byte[64];
                s = "数据以写入".getBytes();
                out.write(s);
                out.flush();
                byte[] flagStr = new byte[256];
                in.read(flagStr);
                flag = new String(flagStr);
            }
            // 判断flag变量，如果该变量是yes则说明有攻击论点。
            while (attackFlag.trim().equals("yes")) {
				System.out.println("开始推理");
				byte[] resultByte = new byte[256];
				resultByte = "可接受的论点".getBytes();
				out.write(resultByte);
				byte[] attackFlagByte = new byte[256];
				in.read(attackFlagByte);
				attackFlag = new String(attackFlagByte);
				if (attackFlag.trim().equals("yes")) {
					byte[] refutedArgumentByte = new byte[256];
					in.read(refutedArgumentByte);
					System.out.println("反驳的论点为：" + new String(refutedArgumentByte));	
				}
			}

            // 关闭数据流
            out.close(); 
            in .close();
            System.out.println("服务器处理完成。。。");
            socket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}