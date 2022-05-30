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
            // �ͻ������ݽ�����
            InputStream in = socket.getInputStream();
            // �����������������
            OutputStream out = socket.getOutputStream();
            System.out.println("���������ڴ�������");
            // �ָ������arguments���浽argumentList������ȥ
            String[] argumentList = null;
            // ���������Ƿ���������۵�ı���flag
            String flag = "yes";
            String attackFlag = "yes";
            while (flag.trim().equals("yes")) {
                byte[] str = new byte[1024]; in .read(str);
                System.out.println("�ͻ���������۵㣺" + new String(str));
                // ���ͻ��˷��͹�������Ϣ�Զ��ŷֶ�
                String str1 = new String(str);
                argumentList = str1.split(",");
                // ��ÿһ��д��ASPIC�ļ���ȥ
                try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("C:\\Users\\dell\\Desktop\\AspicTest.aspic", true))) {
                    for (int i = 0; i <= argumentList.length - 1; i++) {
                        bufferedWriter.write(argumentList[i] + "\n");
                    }
                }
                byte[] s = new byte[64];
                s = "������д��".getBytes();
                out.write(s);
                out.flush();
                byte[] flagStr = new byte[256];
                in.read(flagStr);
                flag = new String(flagStr);
            }
            // �ж�flag����������ñ�����yes��˵���й����۵㡣
            while (attackFlag.trim().equals("yes")) {
				System.out.println("��ʼ����");
				byte[] resultByte = new byte[256];
				resultByte = "�ɽ��ܵ��۵�".getBytes();
				out.write(resultByte);
				byte[] attackFlagByte = new byte[256];
				in.read(attackFlagByte);
				attackFlag = new String(attackFlagByte);
				if (attackFlag.trim().equals("yes")) {
					byte[] refutedArgumentByte = new byte[256];
					in.read(refutedArgumentByte);
					System.out.println("�������۵�Ϊ��" + new String(refutedArgumentByte));	
				}
			}

            // �ر�������
            out.close(); 
            in .close();
            System.out.println("������������ɡ�����");
            socket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}