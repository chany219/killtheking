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
import javax.swing.JLabel;
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
	JPanel EntirePanel = new JPanel();
	JPanel leftPanel = new JPanel();
	JPanel chatPanel = new JPanel();
	static JTextField textField = new JTextField(35);
	static JTextArea messageArea = new JTextArea(20, 35);
	static JPanel buttonPanel = new ButtonPanel();
	static JPanel matrixPanel = new MatrixPanel();

	LoginScreen frame2 = new LoginScreen();
	static int[][] status = new int[8][8];
	public int roleNum;
	public String role;
	public static boolean ready = false;
	String Serveraddress;
	String name;

	Color c1 = new Color(240, 25, 80); // 공지
	Color c2 = new Color(220, 142, 44); // 일반

	public ClientClass() {

		// Create the array that stores status of matrix
		// 5 is tree, 6 is ash
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				status[i][j] = 5;
		frame2.setVisible(true);

		// Layout GUI
		// textField.setForeground(c2);
		textField.setEditable(false);
		messageArea.setEditable(false);

		chatPanel.setLayout(new BorderLayout());
		chatPanel.add(textField, BorderLayout.CENTER);
		chatPanel.add(new JScrollPane(messageArea), BorderLayout.NORTH);
		chatPanel.setPreferredSize(new Dimension(100, 400));
		buttonPanel.setPreferredSize(new Dimension(200, 140));

		leftPanel.setLayout(new BorderLayout());
		leftPanel.add(chatPanel, BorderLayout.NORTH);
		leftPanel.add(buttonPanel, BorderLayout.SOUTH);
		leftPanel.setPreferredSize(new Dimension(300, 540));

		EntirePanel.setLayout(new GridLayout(1, 2));
		EntirePanel.add(leftPanel);
		EntirePanel.add(matrixPanel);
		EntirePanel.setPreferredSize(new Dimension(300, 540));

		frame.getContentPane().add(EntirePanel);
		frame.pack();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(600, 200);
		frame.setSize(1050, 600);
		frame.setMinimumSize(new Dimension(1050, 600));

		textField.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				out_chat.println(textField.getText());
				textField.setText("");
			}
		});
	}

	private void run() throws IOException {

		// Make connection and initialize streams

		Socket socket1 = new Socket(Serveraddress, 9001);
		in_chat = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
		out_chat = new PrintWriter(socket1.getOutputStream(), true);
		String broadcast = " *** ";

		// Check the login
		while (true) {
			if (frame2.getSUCCESS()) {
				int userCount = Integer.parseInt(in_chat.readLine());
				if (userCount >= 4) {
					JOptionPane.showMessageDialog(frame2, "GAME already starts!! You can't enter.", "Message",
							JOptionPane.PLAIN_MESSAGE);
					socket1.close();
					System.exit(-1);
				} else {
					Serveraddress = frame2.getAddress();
					name = frame2.getName();
					JOptionPane.showMessageDialog(frame2, "Login Success!! Welcome to Kill the king", "Message",
							JOptionPane.PLAIN_MESSAGE);
					frame2.setVisible(false);
					frame.setVisible(true);
					break;
				}
			}
		}
		// Process all messages from server, according to the protocol.
		while (true) {
			String line = in_chat.readLine();
			if (line.startsWith("SUBMITNAME")) {
				out_chat.println(name);
			} else if (line.startsWith("NAMEACCEPTED")) {
				// receive role assigned by Server randomly
				String input = in_chat.readLine();
				roleNum = Integer.parseInt(input);
				if (roleNum == 0)
					role = "King";
				else if (roleNum == 1)
					role = "Spy";
				else
					role = "Citizen";
				textField.setEditable(true);
				// GAME button thread creates
				new moving(Serveraddress, roleNum).start();
			} else if (line.startsWith("GAMESTART")) {
				out_chat.println("GAMESTARTACCEPTED");
				messageArea.append(broadcast + " GAME START! " + broadcast + "\n");
				messageArea.setCaretPosition(messageArea.getDocument().getLength());
				JOptionPane.showMessageDialog(frame, "GameStart!! Your role is " + role + ". Select the first position",
						"Message", JOptionPane.PLAIN_MESSAGE);
				ready = true;
				((MatrixPanel) matrixPanel).setEnabledMatrix(1);
			} else if (line.startsWith("BROADCAST")) {
				messageArea.append(broadcast + line.substring(10) + broadcast + "\n");
				messageArea.setCaretPosition(messageArea.getDocument().getLength());
			} else if (line.startsWith("MESSAGE")) {
				messageArea.append(line.substring(8) + "\n");
				messageArea.setCaretPosition(messageArea.getDocument().getLength());
			}
		}
	}

	public static class moving extends Thread {
		BufferedReader in_button;
		PrintWriter out_button;
		String serverAddress;

		int roleNum;
		int prev_i, prev_j;
		int turnCount = 1;
		int replayCount = -1;

		public moving(String serverAddress, int roleNum) {
			this.roleNum = roleNum;
			this.serverAddress = serverAddress;
		}

		public void run() {
			try {
				Socket socket2 = new Socket(serverAddress, 9002);
				in_button = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
				out_button = new PrintWriter(socket2.getOutputStream(), true);
				out_button.println(roleNum);

				// Set the first location
				while (true) {
					if (MatrixPanel.flag) {
						JOptionPane.showMessageDialog(matrixPanel,
								"Your selection is completed! Please wait for other users.", "Message",
								JOptionPane.PLAIN_MESSAGE);
						out_button.println(MatrixPanel.first_position);
						String line2 = in_button.readLine();

						// Valid means all client's first location is valid
						if (line2.startsWith("VALID")) {
							out_button.println("VALIDACCEPTED");
							if (roleNum == 0) {
								String temp = in_button.readLine();
								String tmp[] = temp.split(" ");
								for (int k = 0; k < tmp.length; k++) {
									int x = Integer.parseInt(tmp[k].substring(0, 1));
									int y = Integer.parseInt(tmp[k].substring(1, 2));
									status[x][y] = k;
									if (k == 1)
										((MatrixPanel) matrixPanel).Moving(2, x, y);
									else
										((MatrixPanel) matrixPanel).Moving(k, x, y);
									if (k == 0) {
										prev_i = x;
										prev_j = y;
									}
								}
							} else {
								int i = Integer.parseInt(MatrixPanel.first_position.substring(0, 1));
								int j = Integer.parseInt(MatrixPanel.first_position.substring(2, 3));
								prev_i = i;
								prev_j = j;
								status[i][j] = roleNum;
								((MatrixPanel) matrixPanel).Moving(roleNum, i, j);
							}
							((ButtonPanel) buttonPanel).setEnabledButton(1);
							JOptionPane.showMessageDialog(matrixPanel,
									"All users select first Position. Turn " + turnCount + " Start."
											+ "Select the direction using button.",
									"Message", JOptionPane.PLAIN_MESSAGE);
							break;
						}
						// INValid means all client's first location is invalid
						else if (line2.startsWith("INVALID")) {
							out_button.println("INVALIDACCEPTED");
							JOptionPane.showMessageDialog(matrixPanel, "Please select the position again.", "Message",
									JOptionPane.PLAIN_MESSAGE);
							MatrixPanel.first_position = "";
							MatrixPanel.flag = false;
						}
					}
				}
				// Button action
				while (true) {
					if (!ButtonPanel.flag) {
						if (status_check(roleNum)) {

							String str[];
							int tmp1 = -1, tmp2 = -1, tmp3 = -1, tmp4 = -1;

							if (roleNum != 10) {
								out_button.println(ButtonPanel.direction);
								JOptionPane.showMessageDialog(matrixPanel,
										"Your selection is completed! Please wait for other users.", "Message",
										JOptionPane.PLAIN_MESSAGE);
							}

							String line4 = in_button.readLine();
							if (line4.startsWith("KINGWIN")) {
								out_button.println("GAMEFINISH");
								if (line4.equals("KINGWIN"))
									messageArea.append(
											" *** All citizen dead. The winner of the game is king!! ***" + "\n");
								else
									messageArea.append(
											" *** Citizen don't catch the king until turn "+turnCount+". "
													+ "The winner of game is king!! ***" + "\n");
								messageArea.setCaretPosition(messageArea.getDocument().getLength());
								if (roleNum == 0 || roleNum == 1)
									JOptionPane.showMessageDialog(matrixPanel,
											"Turn " + turnCount + " is finished. You are win!!", "Message",
											JOptionPane.PLAIN_MESSAGE);
								else
									JOptionPane.showMessageDialog(matrixPanel,
											"Turn " + turnCount + " is finished. You are lose!!", "Message",
											JOptionPane.PLAIN_MESSAGE);
								for (int k = 0; k < 8; k++)
									for (int l = 0; l < 8; l++)
										((MatrixPanel) matrixPanel).Observer(k, l, 9);
								((ButtonPanel) buttonPanel).setReplayButton();
								break;
							}
							// CITIZEN WIN means king is win , and then game is
							// over
							else if (line4.startsWith("CITIZENWIN")) {
								out_button.println("GAMEFINISH");
								if (line4.equals("CITIZENWIN"))
									messageArea
											.append(" *** Citizen catches the king. The winner of the game is citizen!! ***"
													+ "\n");
								else
									messageArea
											.append(" *** The king is locked by Citizen. The winner of the game is citizen!! ***"
													+ "\n");
								messageArea.setCaretPosition(messageArea.getDocument().getLength());
								if (roleNum == 0 || roleNum == 1)
									JOptionPane.showMessageDialog(matrixPanel,
											"Turn " + turnCount + " is finished. You are lose!!", "Message",
											JOptionPane.PLAIN_MESSAGE);
								else
									JOptionPane.showMessageDialog(matrixPanel,
											"Turn " + turnCount + " is finished. You are win!!", "Message",
											JOptionPane.PLAIN_MESSAGE);
								for (int k = 0; k < 8; k++)
									for (int l = 0; l < 8; l++)
										((MatrixPanel) matrixPanel).Observer(k, l, 8);
								((ButtonPanel) buttonPanel).setReplayButton();
								break;
							} else if (line4.startsWith("NOTDEAD")) {
								String line3 = in_button.readLine();
								if (line3.startsWith("SAFE")) {
									JOptionPane.showMessageDialog(matrixPanel,
											"Turn " + turnCount + " is finished. KING is safe ! " + "Turn "
													+ (turnCount + 1) + " starts. Select the direction using button. ",
											"Message", JOptionPane.PLAIN_MESSAGE);
									turnCount++;
									if (roleNum == 0) {

										String temp = in_button.readLine();
										String tmp[] = temp.split(" ");
										int length = (tmp.length / 3);

										for (int k = 0; k < length; k++) {
											int collision = 0;
											int temp_i = Integer.parseInt(tmp[k].substring(0, 1));
											int temp_j = Integer.parseInt(tmp[k].substring(1, 2));
											int i = Integer.parseInt(tmp[k + length].substring(0, 1));
											int j = Integer.parseInt(tmp[k + length].substring(1, 2));
											int r = Integer.parseInt(tmp[k + length + length].substring(0, 1));
											if (status[temp_i][temp_j] != r) {
												status_update(r, i, j);
												collision = 1;
											} else
												status_update(r, i, j, temp_i, temp_j);
											if (collision == 0) {
												if (r == 1)
													((MatrixPanel) matrixPanel).Moving(2, i, j, temp_i, temp_j);
												else
													((MatrixPanel) matrixPanel).Moving(r, i, j, temp_i, temp_j);
											} else {
												if (r == 1)
													((MatrixPanel) matrixPanel).Moving(2, i, j);
												else
													((MatrixPanel) matrixPanel).Moving(r, i, j);
											}
											if (k == 0) {
												prev_i = i;
												prev_j = j;
											}
											// ((ButtonPanel)
											// buttonPanel).setButtonBackground();
										}
										for (int k = 0; k < 8; k++) {
											for (int l = 0; l < 8; l++) {
												if (status[k][l] == tmp3 || status[k][l] == tmp4) {
													((MatrixPanel) matrixPanel).Observer(k, l, 6);
													status[k][l] = 6;
												}
											}
										}
										ButtonPanel.flag = true;
									} else {
										String temp = in_button.readLine();
										int i = Integer.parseInt(temp.substring(0, 1));
										int j = Integer.parseInt(temp.substring(1, 2));
										status_update(i, j, prev_i, prev_j);
										((MatrixPanel) matrixPanel).Moving(roleNum, i, j, prev_i, prev_j);
										prev_i = i;
										prev_j = j;
										// ((ButtonPanel)
										// buttonPanel).setButtonBackground();
										ButtonPanel.flag = true;
									}
									out_button.println("SAFEACCEPTED");
								}
							} else if (line4.startsWith("ISDEAD")) {

								str = line4.split("/");
								tmp1 = Integer.parseInt(str[1].substring(0, 1));
								tmp2 = Integer.parseInt(str[1].substring(1, 2));
								tmp3 = Integer.parseInt(str[2].substring(0, 1));
								tmp4 = Integer.parseInt(str[2].substring(1, 2));
								messageArea.append(" *** " + str[0].substring(6) + " *** " + "\n");
								messageArea.setCaretPosition(messageArea.getDocument().getLength());
								String line3 = in_button.readLine();

								if (roleNum == 0) {
									status[tmp1][tmp2] = 6;
									((MatrixPanel) matrixPanel).Observer(tmp1, tmp2, 7);
								}

								// KINGWIN means king is win , and then game is
								// over
								// DEAD means client received this message
								// died.So they become observer.
								if (line3.startsWith("DEAD")) {
									JOptionPane.showMessageDialog(matrixPanel,
											"Turn " + turnCount
													+ " is finished. You are dead!! Now, you are an Observer",
											"Message", JOptionPane.PLAIN_MESSAGE);
									roleNum = 10;
									textField.setEditable(false);
									((ButtonPanel) buttonPanel).setEnabledButton(0);
									ButtonPanel.flag = false;
									String temp = in_button.readLine();
									String tmp[] = temp.split(" ");
									int count = 0;
									status[tmp1][tmp2] = 6;
									((MatrixPanel) matrixPanel).Observer(tmp1, tmp2, 7);
									for (int k = 0; k < 8; k++) {
										for (int l = 0; l < 8; l++) {
											status[k][l] = Integer.parseInt(tmp[count].substring(0, 1));
											count++;
											if ((k == tmp1) && (l == tmp2))
												continue;
											((MatrixPanel) matrixPanel).Observer(k, l, status[k][l]);
										}
									}

									out_button.println("DEADACCEPTED");
									out_button.println("DEADACCEPTED");
								}
								// SAFE means client received this message is
								// safe. So next turn is progressed
								else if (line3.startsWith("SAFE")) {
									JOptionPane.showMessageDialog(matrixPanel,
											"Turn " + turnCount + " is finished. KING is safe ! " + "Turn "
													+ (turnCount + 1) + " starts. Select the direction using button. ",
											"Message", JOptionPane.PLAIN_MESSAGE);
									turnCount++;
									if (roleNum == 0) {

										String temp = in_button.readLine();
										String tmp[] = temp.split(" ");
										int length = (tmp.length / 3);

										for (int k = 0; k < length; k++) {
											int collision2 = 0;
											int temp_i = Integer.parseInt(tmp[k].substring(0, 1));
											int temp_j = Integer.parseInt(tmp[k].substring(1, 2));
											int i = Integer.parseInt(tmp[k + length].substring(0, 1));
											int j = Integer.parseInt(tmp[k + length].substring(1, 2));
											int r = Integer.parseInt(tmp[k + length + length].substring(0, 1));
											if (status[temp_i][temp_j] != r) {
												status_update(r, i, j);
												collision2 = 1;
											} else
												status_update(r, i, j, temp_i, temp_j);
											if (collision2 == 0) {
												if (r == 1)
													((MatrixPanel) matrixPanel).Moving(2, i, j, temp_i, temp_j);
												else
													((MatrixPanel) matrixPanel).Moving(r, i, j, temp_i, temp_j);
											} else {
												if (r == 1)
													((MatrixPanel) matrixPanel).Moving(2, i, j);
												else
													((MatrixPanel) matrixPanel).Moving(r, i, j);
											}
											if (k == 0) {
												prev_i = i;
												prev_j = j;
											}

										}
										for (int k = 0; k < 8; k++) {
											for (int l = 0; l < 8; l++) {
												if (status[k][l] == tmp3 || status[k][l] == tmp4) {
													((MatrixPanel) matrixPanel).Observer(k, l, 6);
													status[k][l] = 6;
												}
											}
										}
										ButtonPanel.flag = true;
									} else {
										String temp = in_button.readLine();
										int i = Integer.parseInt(temp.substring(0, 1));
										int j = Integer.parseInt(temp.substring(1, 2));
										status_update(i, j, prev_i, prev_j);
										((MatrixPanel) matrixPanel).Moving(roleNum, i, j, prev_i, prev_j);
										prev_i = i;
										prev_j = j;
										ButtonPanel.flag = true;
									}
									out_button.println("SAFEACCEPTED");
								}
							} else if (line4.startsWith("OBSERVER")) {
								JOptionPane.showMessageDialog(matrixPanel,
										"Turn " + turnCount + " is finished. KING is safe ! " + "Turn "
												+ (turnCount + 1) + " starts. Select the direction using button. ",
										"Message", JOptionPane.PLAIN_MESSAGE);
								turnCount++;
								String temp = in_button.readLine();
								String tmp[] = temp.split(" ");
								int length = (tmp.length / 3);

								for (int k = 0; k < length; k++) {
									int temp_i = Integer.parseInt(tmp[k].substring(0, 1));
									int temp_j = Integer.parseInt(tmp[k].substring(1, 2));
									int i = Integer.parseInt(tmp[k + length].substring(0, 1));
									int j = Integer.parseInt(tmp[k + length].substring(1, 2));
									int r = Integer.parseInt(tmp[k + length + length].substring(0, 1));
									status_update(r, i, j, temp_i, temp_j);
									((MatrixPanel) matrixPanel).Moving(r, i, j, temp_i, temp_j);
									if (k == 0) {
										prev_i = i;
										prev_j = j;
									}
								}
								out_button.println("OBSERVERACCEPTED");
							}
						}
						((ButtonPanel) buttonPanel).setButtonBackground();
					}
				}

				while (true) {
					if (!ButtonPanel.backreplayflag) {
						if (replayCount <= 0) {
							JOptionPane.showMessageDialog(matrixPanel, "This is first scene. ", "Message",
									JOptionPane.WARNING_MESSAGE);
						} else {
							replayCount--;
							int k = 0;
							out_button.println("REPLAY" + replayCount);
							String arr = in_button.readLine();
							String[] replay = arr.split(" ");
							for (int i = 0; i < 8; i++) {
								for (int j = 0; j < 8; j++) {
									((MatrixPanel) matrixPanel).Observer(i, j,
											Integer.parseInt(replay[k].substring(0, 1)));
									k++;
								}
							}
						}
						ButtonPanel.backreplayflag = true;
						((ButtonPanel) buttonPanel).setReplayButtonBackground();
					}

					if (!ButtonPanel.forwardreplayflag) {
						if (turnCount == replayCount) {
							JOptionPane.showMessageDialog(matrixPanel, "This is last scene. ", "Message",
									JOptionPane.WARNING_MESSAGE);
						} else {
							replayCount++;

							int k = 0;
							out_button.println("REPLAY" + replayCount);
							String arr = in_button.readLine();
							String[] replay = arr.split(" ");
							for (int i = 0; i < 8; i++) {
								for (int j = 0; j < 8; j++) {
									((MatrixPanel) matrixPanel).Observer(i, j,
											Integer.parseInt(replay[k].substring(0, 1)));
									k++;
								}
							}

						}
						ButtonPanel.forwardreplayflag = true;
						((ButtonPanel) buttonPanel).setReplayButtonBackground();
					}
				}
			} catch (IOException E) {

			}
		}

		public void status_update(int role, int i, int j) {
			status[i][j] = role;
		}

		// Update the status array
		public void status_update(int i, int j, int prev_i, int prev_j) {

			status[i][j] = roleNum;
			if (roleNum == 0)
				status[prev_i][prev_j] = 5;
			else
				status[prev_i][prev_j] = 6;
		}

		// Update the status array according to role
		public void status_update(int a, int i, int j, int prev_i, int prev_j) {

			if (a == 0) {
				status[i][j] = roleNum;
				status[prev_i][prev_j] = 5;
			} else {
				status[i][j] = a;
				status[prev_i][prev_j] = 6;
			}
		}

		// Check the selected position is valid or not
		public boolean status_check(int roleNum) {
			int tmpx = prev_i;
			int tmpy = prev_j;

			switch (ButtonPanel.direction) {
			case "up":
				tmpx--;
				break;
			case "down":
				tmpx++;
				break;
			case "right":
				tmpy++;
				break;
			case "left":
				tmpy--;
				break;
			}

			if (roleNum == 10)
				return true;
			if (tmpx > 7 || tmpx < 0 || tmpy > 7 || tmpy < 0) {
				JOptionPane.showMessageDialog(matrixPanel, "You cannot go there. Choose again please ", "Message",
						JOptionPane.WARNING_MESSAGE);
				ButtonPanel.flag = true;
				return false;
			} else if (roleNum == 0)
				if (status[tmpx][tmpy] == 6 || status[tmpx][tmpy] == 1 || status[tmpx][tmpy] == 2
						|| status[tmpx][tmpy] == 3) {
					JOptionPane.showMessageDialog(matrixPanel, "You cannot go there. Choose again please ", "Message",
							JOptionPane.WARNING_MESSAGE);
					ButtonPanel.flag = true;
					return false;
				}
			return true;
		}
	}

	public static void main(String[] args) throws Exception {
		ClientClass client = new ClientClass();
		client.run();
	}
}