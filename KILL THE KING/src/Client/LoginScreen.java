package Client;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridLayout;
import java.awt.*;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.Container;

import javax.swing.UIManager;
import java.awt.event.ActionListener;
import java.net.URL;
import java.awt.event.ActionEvent;

public class LoginScreen extends JFrame {



	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;

	String address;
	String name;
    boolean success=false;
	public LoginScreen() {


		setMinimumSize(new Dimension(1050, 600));
		setTitle("Kill The King");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocation(600, 200);


		JLabel lblNewLabel = new JLabel("IP Address");
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel.setBounds(643, 261, 94, 16);
		contentPane.add(lblNewLabel);

		textField = new JTextField();
		textField.setBounds(739, 254, 225, 33);
		contentPane.add(textField);
		textField.setColumns(10);

		JLabel lblName = new JLabel("Name");
		lblName.setForeground(new Color(255, 255, 255));
		lblName.setFont(new Font("Arial", Font.PLAIN, 18));
		lblName.setBounds(676, 306, 61, 16);
		contentPane.add(lblName);

		textField_1 = new JTextField();
		textField_1.setBounds(739, 299, 225, 33);
		contentPane.add(textField_1);
		textField_1.setColumns(10);

		JButton btnNewButton = new JButton("Enter");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				address=textField.getText();
				name=textField_1.getText();
				success=true;
			}
		});
		btnNewButton.setBounds(847, 344, 117, 29);
		contentPane.add(btnNewButton);
		
		JLabel label = new JLabel("");
		Image img= new ImageIcon("loginImage.png").getImage();
		label.setIcon(new ImageIcon(img));
		label.setBounds(0, 0, 1050, 600);
		contentPane.add(label);

	}
	public String getAddress()
	{
		return address;
	}

	public String getName()
	{
		return name;
	}
	
	public boolean getSUCCESS()
	{
		return success;
	}
}