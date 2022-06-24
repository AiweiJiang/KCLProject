package mytweetyapp;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class UI {

	public static void main(String[] args) throws UnknownHostException, IOException {
		//Make a request to port 8888 of the local computer
        final Socket socket = new Socket("127.0.0.1", 8888);
        // An input stream 'in' that receives information passed by the server
        final InputStream in = socket.getInputStream();
        // An output stream that transmits information to a server
        final OutputStream out = socket.getOutputStream();
        final ArrayList<String> diseaseList = new ArrayList<>();
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
        
		
		final JFrame f = new JFrame("argument-based decision making system");
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		f.setSize(d);
		f.setLayout(null);
		
		
		final JTextArea textArea = new JTextArea(10,20);
		textArea.setBounds(50, 50, 400, 150);;
		//textArea.setEditable(false);
		textArea.setLineWrap(true);
		
		JButton b = new JButton("Upload");
		b.setBounds(500, 115, 100, 30);
		
		
		
		
		JLabel l = new JLabel();
		l.setText("Welcome to use the argument-based desition making system!!");
		l.setBounds(0, 0, 500, 20);
		
		JLabel l1 = new JLabel();
		l1.setText("please enter the inference rules or primises in the following box:");
		l1.setBounds(0, 25, 500, 20);
		
		JLabel l2 = new JLabel();
		l2.setText("According to the rules of inference and the premises, the acceptable arguments are:");
		l2.setBounds(0, 205, 500, 20);
		
		
		final JTextArea ta1 = new JTextArea(10,20);
		ta1.setBounds(50, 240, 1100, 150);
		ta1.setEditable(false);
		ta1.setLineWrap(true);
		
		JScrollPane sp = new JScrollPane(ta1);
		sp.setBounds(50, 240, 1100, 150);
		
		JLabel l3 = new JLabel();
		l3.setText("According to the arguments, the possible diseases are:");
		l3.setBounds(0, 400, 340, 20);
		
		final JTextArea ta2 = new JTextArea();
		ta2.setBounds(340, 400, 150, 50);
		ta2.setEditable(false);
		ta2.setLineWrap(true);
		
		JScrollPane sp1 = new JScrollPane(ta2);
		sp1.setBounds(340, 400, 150, 50);
		
		JLabel l4 = new JLabel();
		l4.setText("Enter a new argument in the following box if you need a rebuttal");
		l4.setBounds(650, 25, 500, 20);
		
		final JTextArea ta3 = new JTextArea();
		ta3.setBounds(650, 50, 400, 150);
		ta3.setLineWrap(true);
		
		JButton b1 = new JButton("Attack");
		b1.setBounds(1100, 115, 100, 30);
		
		JLabel l5 = new JLabel();
		l5.setText("According to the position-based ranking, the best prescription is:");
		l5.setBounds(0, 500, 500, 20);
		
		final JTextArea ta4 = new JTextArea();
		ta4.setBounds(400, 500, 150, 60);
		ta4.setLineWrap(true);
		ta4.setEditable(false);
		
		JScrollPane sp2 = new JScrollPane(ta4);
		sp2.setBounds(400, 500, 150, 60);
		
		JButton startButton = new JButton("Start");
		startButton.setBounds(1200, 300, 100, 30);
		
		startButton.addActionListener(new ActionListener() {
            // 当按钮被点击时，就会触发 ActionEvent事件
            // actionPerformed 方法就会被执行
            public void actionPerformed(ActionEvent e) {
            	byte[] command = "Start".getBytes();
            	try {
					out.write(command);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
            	// Receive the acceptable arguments of server 
                byte[] resultByte = new byte[1024 * 3]; 
                try {
					in .read(resultByte);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                String resultArg = new String(resultByte);
                String[] resultList = resultArg.split(";");
                String accArg = "";
                
                for (int i = 0; i < resultList.length; i++) {
                    if (i != resultList.length - 1) {
                        accArg += resultList[i] + "\r\n";
                    } else {
                        accArg += resultList[i];
                    }
                }
                
                ta1.setText(accArg);
                
                ArrayList<String> accDisease = new ArrayList<>();
                String diseaseResult = "";
                for (int i = 0; i < resultList.length; i++) {
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
                for(int i = 0; i < accDisease.size(); i++) {
                    if(i != accDisease.size()-1) {
                    	diseaseResult += accDisease.get(i) + "\r\n";
                    }else {
                    	diseaseResult += accDisease.get(i);
					}
                }
                ta2.setText(diseaseResult);
                
                byte[] finalMedicineByte = new byte[254];
                try {
					in.read(finalMedicineByte);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
                ta4.setText(new String(finalMedicineByte));
            }
        });
		
		// 给按钮 增加 监听
        b1.addActionListener(new ActionListener() {
            // 当按钮被点击时，就会触发 ActionEvent事件
            // actionPerformed 方法就会被执行
            public void actionPerformed(ActionEvent e) {
            	byte[] command = "Attack".getBytes();
            	try {
					out.write(command);
					out.flush();
					String rules = ta3.getText();
					int rulesLength = rules.length();
					String lengthStr = Integer.toString(rulesLength);
					System.out.println(lengthStr);
					out.write(lengthStr.getBytes());
					out.flush();
					if (rules.length() == 0) {
						JOptionPane.showMessageDialog(f, "The text content cannot be empty. Please enter it again!");
	                    textArea.grabFocus();
					}else {
						byte[] rulesByte = rules.getBytes();
		            	out.write(rulesByte);
		            	
		            	byte[] resultByte = new byte[1024 * 3]; 
						in .read(resultByte);
		                String resultArg = new String(resultByte);
		                String[] resultList = resultArg.split(";");
		                String accArg = "";
		                
		                for (int i = 0; i < resultList.length; i++) {
		                    if (i != resultList.length - 1) {
		                        accArg += resultList[i] + "\r\n";
		                    } else {
		                        accArg += resultList[i];
		                    }
		                }
		                
		                ta1.setText(accArg);
		                
		                ArrayList<String> accDisease = new ArrayList<>();
		                String diseaseResult = "";
		                for (int i = 0; i < resultList.length; i++) {
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
		                for(int i = 0; i < accDisease.size(); i++) {
		                    if(i != accDisease.size()-1) {
		                    	diseaseResult += accDisease.get(i) + "\r\n";
		                    }else {
		                    	diseaseResult += accDisease.get(i);
							}
		                }
		                ta2.setText(diseaseResult);
		                
		                byte[] finalMedicineByte = new byte[254];
		                try {
							in.read(finalMedicineByte);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
		                ta4.setText(new String(finalMedicineByte));
					}
	            	
	            	
				} catch (IOException e1) {
					e1.printStackTrace();
					System.err.println("The client failed to send the command!!!");
				}
           	
            }
        });
		        
        b.addActionListener(new ActionListener() {
            // 当按钮被点击时，就会触发 ActionEvent事件
            // actionPerformed 方法就会被执行
            public void actionPerformed(ActionEvent e) {
            	byte[] command = "Upload".getBytes();
            	try {
					out.write(command);
					out.flush();
					String rules = textArea.getText();
					int rulesLength = rules.length();
					String lengthStr = Integer.toString(rulesLength);
					out.write(lengthStr.getBytes());
					out.flush();
					if (rules.length() == 0) {
						JOptionPane.showMessageDialog(f, "The text content cannot be empty. Please enter it again!");
	                    textArea.grabFocus();
					}else {
						byte[] rulesByte = rules.getBytes();
		            	out.write(rulesByte);
		            	out.flush();
		            	
		            	byte[] resultByte = new byte[256];
		            	in.read(resultByte);
		            	String result = new String(resultByte);
		            	
		            	if(result.trim().equals("successful")) {
		            		JOptionPane.showMessageDialog(null, "Data has been written successful!", "Result", JOptionPane.INFORMATION_MESSAGE);
		            	}else {
		            		JOptionPane.showConfirmDialog(null, "Errow! Failed to write data!", "choose one", JOptionPane.YES_NO_OPTION);
						}
					}
	            	
	            	
				} catch (IOException e1) {
					e1.printStackTrace();
					System.err.println("The client failed to send the command!!!");
				}
            	
           	 
            }
        });
        
        JButton endButton = new JButton("close");
        endButton.setBounds(600, 600, 100, 30);
        
        endButton.addActionListener(new ActionListener() {
            // 当按钮被点击时，就会触发 ActionEvent事件
            // actionPerformed 方法就会被执行
            public void actionPerformed(ActionEvent e) {
            	byte[] command = "close".getBytes();
            	try {
					out.write(command);
					socket.close();
					f.dispose();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
            }
        });
		
		f.add(b);
		f.add(textArea);
		f.add(l);
		f.add(l1);
		f.add(l2);
		f.add(sp);
		f.add(l3);
		f.add(sp1);
		f.add(l4);
		f.add(ta3);
		f.add(b1);
		f.add(l5);
		f.add(sp2);
		f.add(startButton);
		f.add(endButton);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        f.setVisible(true);
	}

}
