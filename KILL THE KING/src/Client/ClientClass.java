package Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Client.*;

public class ClientClass {

	BufferedReader in_chat;
	PrintWriter out_chat;

	JFrame frame = new JFrame("KILL THE KING");
	LoginScreen frame2 = new LoginScreen("KILL THE KING");
	JPanel EntirePanel = new JPanel();
	JPanel leftPanel = new JPanel();
	JPanel chatPanel= new JPanel();
	static JPanel buttonPanel =new ButtonPanel();
	static JPanel matrixPanel=new MatrixPanel();
	JTextField textField = new JTextField(35);
	JTextArea messageArea = new JTextArea(20, 35);
	static int[][] status=new int[8][8];
	public static int roleNum;
	
	public ClientClass() {

		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++)
				status[i][j]=6;
		
		// Layout GUI
		textField.setEditable(false);
		messageArea.setEditable(false);

		chatPanel.setLayout(new BorderLayout());
		chatPanel.add(textField, BorderLayout.CENTER);
		chatPanel.add(new JScrollPane(messageArea), BorderLayout.NORTH);
		chatPanel.setPreferredSize(new Dimension(100,400));
		buttonPanel.setPreferredSize(new Dimension(200,140));

		leftPanel.setLayout(new BorderLayout());
		leftPanel.add(chatPanel,BorderLayout.NORTH);
		leftPanel.add(buttonPanel,BorderLayout.SOUTH);
		leftPanel.setPreferredSize(new Dimension(300,540));

		EntirePanel.setLayout(new GridLayout(1,2));
		EntirePanel.add(leftPanel);
		EntirePanel.add(matrixPanel);
		EntirePanel.setPreferredSize(new Dimension(300,540));

		frame.getContentPane().add(EntirePanel);
		frame.pack();

		textField.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				out_chat.println(textField.getText());
				textField.setText("");
			}
		});
	}

	private String getServerAddress() {
		return JOptionPane.showInputDialog(
				frame2,
				"Enter IP Address of the Server:",
				"Welcome to the Killtheking",
				JOptionPane.QUESTION_MESSAGE);
	}

	private String getName() {
		return JOptionPane.showInputDialog(
				frame2,
				"Choose a screen name:",
				"Nickname selection",
				JOptionPane.PLAIN_MESSAGE);
	}

	private void run() throws IOException {

		// Make connection and initialize streams
		String serverAddress = getServerAddress();
		Socket socket1 = new Socket(serverAddress, 9001);
		in_chat = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
		out_chat = new PrintWriter(socket1.getOutputStream(), true);
		String broadcast=" *** ";
		// Process all messages from server, according to the protocol.
		while (true) {
			String line = in_chat.readLine();
			if (line.startsWith("SUBMITNAME")) {
				out_chat.println(getName());
				frame2.setVisible(false);
			} else if (line.startsWith("NAMEACCEPTED")) {
				textField.setEditable(true);
			} else if (line.startsWith("GAMESTART")){
				out_chat.println("GAMESTART");
				messageArea.append(broadcast+" GAME START! "+broadcast+"\n");
				line=in_chat.readLine();
				ClientClass.roleNum=Integer.parseInt(line);
			} else if (line.startsWith("BROADCAST")){
				messageArea.append(broadcast+line.substring(10)+broadcast+"\n");
			}
			else if (line.startsWith("MESSAGE")) {
				messageArea.append(line.substring(8) + "\n");
			} 
		}
	}

	public static class moving extends Thread{
		BufferedReader in_button;
		PrintWriter out_button;
		int prev_i, prev_j;

		public void run() {
			try {
				Socket socket2 = new Socket("localhost",9002);
				in_button = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
				out_button = new PrintWriter(socket2.getOutputStream(), true);
				while(true) {
					if(MatrixPanel.flag) {
						out_button.println(MatrixPanel.first_position);
						String line2=in_button.readLine();
						int i=Integer.parseInt(line2.substring(0,1));
						int j=Integer.parseInt(line2.substring(2,3));
						System.out.println(i+" "+j);
						prev_i=i;
						prev_j=j;
						status[i][j]=roleNum;
						((MatrixPanel) matrixPanel).Moving(roleNum, i,j);
						break;
					}
				}
				while(true) {
					if(!ButtonPanel.flag) {
						out_button.println(ButtonPanel.direction);
						String line2=in_button.readLine();
						int i=Integer.parseInt(line2.substring(0,1));
						int j=Integer.parseInt(line2.substring(2,3));
						((MatrixPanel) matrixPanel).Moving(roleNum,i,j,prev_i,prev_j);
						prev_i=i; prev_j=j;
						ButtonPanel.flag=true;
					}
				}
			} catch(IOException E) {

			}
		}

		/*private boolean status_check() {
			//if(ButtonPanel.direction.equals("up"))
				
			
			return false;
		}
	*/}
	
	public static void main(String[] args) throws Exception {
		ClientClass client = new ClientClass();

		client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.frame.setLocation(600, 200);
		client.frame.setSize(1050,600);
		client.frame.setVisible(true);
		new moving().start();
		client.run();
	}
}
