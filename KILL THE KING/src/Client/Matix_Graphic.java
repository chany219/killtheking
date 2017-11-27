package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Client.*;

public class Matix_Graphic extends JPanel {

	public static String first_position=""; 
	public static boolean flag=false;
	ImageIcon orgimage=new ImageIcon("tree.png");
	Image temp=orgimage.getImage();
	Image tmp=temp.getScaledInstance(70, 70, ABORT);
	ImageIcon image=new ImageIcon(tmp);

	ImageIcon orgimage2=new ImageIcon("king.png");
	Image temp2=orgimage2.getImage();
	Image tmp2=temp2.getScaledInstance(70, 70, ABORT);
	ImageIcon image2=new ImageIcon(tmp2);

	ImageIcon orgimage3=new ImageIcon("ash.png");
	Image temp3=orgimage3.getImage();
	Image tmp3=temp3.getScaledInstance(70, 70, ABORT);
	ImageIcon image3=new ImageIcon(tmp3);

	ImageIcon orgimage4=new ImageIcon("citizen.png");
	Image temp4=orgimage4.getImage();
	Image tmp4=temp4.getScaledInstance(70, 70, ABORT);
	ImageIcon image4=new ImageIcon(tmp4);

	JButton[][]bnt= new JButton[8][8];
	Listener listener=new Listener();

	Matix_Graphic(){
		setOpaque(false);
		setLayout(new GridLayout(8,8));

		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++)
			{
				bnt[i][j]=new JButton(image);

				add(bnt[i][j]);
				bnt[i][j].setBackground(Color.WHITE);
				bnt[i][j].addActionListener(listener);
			}

	}
	public void Moving(String role , int i,int j, int prev_i, int prev_j) {
		if(role.equalsIgnoreCase("king"))
		{
			bnt[i][j].setIcon(image2);
			bnt[prev_i][prev_j].setIcon(image);
		}
		else if(role.equalsIgnoreCase("citizen"))
		{
			bnt[i][j].setIcon(image4);
			bnt[prev_i][prev_j].setIcon(image3);
		}
	}
	 public void Moving(String role, int i,int j) {
		 
		 if(role.equalsIgnoreCase("king"))
	      bnt[i][j].setIcon(image2);
		 else
			 bnt[i][j].setIcon(image4);
	    
	   }
	 
	class Listener implements ActionListener{
		public void actionPerformed(ActionEvent e)
		{
			for(int i=0;i<8;i++)
				for(int j=0;j<8;j++)
				{
					if(flag)
						break;
					if(e.getSource()==bnt[i][j])
					{
						first_position+=(i+" "+j);
						flag=true;
						break;
					}
				}
		}
	}
}