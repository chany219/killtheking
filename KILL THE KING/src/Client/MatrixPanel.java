package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Client.*;


/**
 * This class creates a map in which the characters move and 
 * displays the location of the characters received by the client on the map.
 */
public class MatrixPanel extends JPanel {

	public static String first_position=""; 
	public static boolean flag=false;
	
	/**
	 * Set the image for each character and background
	 */
	ImageIcon orgimage=new ImageIcon("tree.png");
	Image temp=orgimage.getImage();
	Image tmp=temp.getScaledInstance(70, 70, ABORT);
	ImageIcon image=new ImageIcon(tmp);

	ImageIcon orgimage2=new ImageIcon("king.png");
	Image temp2=orgimage2.getImage();
	Image tmp2=temp2.getScaledInstance(75, 75, ABORT);
	ImageIcon image2=new ImageIcon(tmp2);

	ImageIcon orgimage3=new ImageIcon("ash.png");
	Image temp3=orgimage3.getImage();
	Image tmp3=temp3.getScaledInstance(70, 70, ABORT);
	ImageIcon image3=new ImageIcon(tmp3);

	ImageIcon orgimage4=new ImageIcon("citizen.png");
	Image temp4=orgimage4.getImage();
	Image tmp4=temp4.getScaledInstance(70, 70, ABORT);
	ImageIcon image4=new ImageIcon(tmp4);

	ImageIcon orgimage5=new ImageIcon("citizendead.png");
	Image temp5=orgimage5.getImage();
	Image tmp5=temp5.getScaledInstance(70, 70, ABORT);
	ImageIcon image5=new ImageIcon(tmp5);
	
	ImageIcon orgimage6=new ImageIcon("citizenwin.png");
	Image temp6=orgimage6.getImage();
	Image tmp6=temp6.getScaledInstance(70, 70, ABORT);
	ImageIcon image6=new ImageIcon(tmp6);
	
	ImageIcon orgimage7=new ImageIcon("kingwin.png");
	Image temp7=orgimage7.getImage();
	Image tmp7=temp7.getScaledInstance(70, 70, ABORT);
	ImageIcon image7=new ImageIcon(tmp7);
	
	ImageIcon orgimage8=new ImageIcon("spy.png");
	Image temp8=orgimage8.getImage();
	Image tmp8=temp8.getScaledInstance(70, 70, ABORT);
	ImageIcon image8=new ImageIcon(tmp8);
	
	Color c=new Color(40,55,91);
	JButton[][]bnt= new JButton[8][8];
	Listener listener=new Listener();

	
	/**
	 * Constructor creates the 8 by 8 matrix
	 * and set the background
	 */
	MatrixPanel(){
		setOpaque(false);
		setLayout(new GridLayout(8,8));

		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++)
			{
				bnt[i][j]=new JButton(image);

				add(bnt[i][j]);
				bnt[i][j].setBackground(c);
				bnt[i][j].addActionListener(listener);
				bnt[i][j].setEnabled(false);
			}

	}

	/**
     * This method control the matrix to be active or not
     */
	public void setEnabledMatrix(int a)
	{
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++)
				if(a==1)
					bnt[i][j].setEnabled(true);
				else
					bnt[i][j].setEnabled(false);
	}
	
	/**
     * This method takes the image and location that should be represented in matrix
     * from Client class and displays it in the matrix.
     */
	public void Moving(int role , int i,int j, int prev_i, int prev_j) {
		if(role==0)
		{
			bnt[i][j].setIcon(image2);
			bnt[prev_i][prev_j].setIcon(image);
		}
		else if(role==1)
		{
			bnt[i][j].setIcon(image8);
			bnt[prev_i][prev_j].setIcon(image3);
		}
		else
		{
			bnt[i][j].setIcon(image4);
			bnt[prev_i][prev_j].setIcon(image3);
		}
	}
	
	/**
	 * Overloading for Moving method
	 */
	public void Moving(int role, int i,int j) {

		if(role==0)
			bnt[i][j].setIcon(image2);
		else if(role==1)
			bnt[i][j].setIcon(image8);
		else
			bnt[i][j].setIcon(image4);
	}

	/**
	 * This method receives the position and image to represent.
	 * Then set the image in certain position.
	 * It is used for Observer.
	 */
	public void Observer(int i,int j,int x)
	{
		if(x==0)
			bnt[i][j].setIcon(image2);
		if(x==1)
			bnt[i][j].setIcon(image8);
		else if(x==2||x==3||x==4)
			bnt[i][j].setIcon(image4);
		else if(x==5)
			bnt[i][j].setIcon(image);
		else if(x==6)
			bnt[i][j].setIcon(image3);
		else if(x==7)
			bnt[i][j].setIcon(image5); 
		else if(x==8)
			bnt[i][j].setIcon(image6); // citizen win
		else if(x==9)
			bnt[i][j].setIcon(image7); // king win
	}

	/**
	 * This action listener stores the character's first location that is selected by user.
	 * Then Send it to Client class
	 */
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