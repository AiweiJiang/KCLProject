package mytweetyapp;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        try {
            //�򱾻���8888�˿ڷ����ͻ�����
            Socket socket = new Socket("127.0.0.1", 8888);
            // ���ܷ������˴���������Ϣ
            InputStream in = socket.getInputStream(); //���ܷ���������������
            // ��agent�����arguments���͸���������
            OutputStream out = socket.getOutputStream();
            // ����������룬�Ӷ�ʹagent����arguments
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
                System.out.println("���������������ݣ�" + new String(str));
            }
            while (attackFlag.trim().equals("yes")) {
            	byte[] argumentByte = new byte[256];
            	in.read(argumentByte);
            	System.out.println("�ɽ��ܵ��۵�Ϊ�� " + new String(argumentByte));
            	System.out.println("�Ƿ������۵��Է���(please enter yes or not):");
				attackFlag = input.nextLine();
				out.write(attackFlag.getBytes());
				out.flush();
				if (attackFlag.trim().equals("yes")) {
					System.out.println("�����������Է����Ĺ۵㣺");
					String refutedArgument = input.nextLine();
					out.write(refutedArgument.getBytes());
					out.flush();
				}else {
					break;
				}
				
			}
            
            // �ر�������
            in .close();
            out.flush(); //ˢ�»�����
            out.close();
            input.close();
            socket.close(); //�ر�Socket
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}