package Client;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

/**
 * ButtonPanel allows you to move characters over the matrix
 * we use the JButton class.
 */

public class ButtonPanel extends JPanel  {

	
	/**
     * These boolean variables allows you to press the
     * button once per turn
     */
	public static boolean flag=true;
	public static String direction;
	public static boolean backreplayflag=true;
	public static boolean forwardreplayflag=true;

	
	/**
     * Create Button and constructor
     */
	Button []btn=new Button[9];
	ButtonPanel(){
		setOpaque(false);
		setLayout(new GridLayout(3,3));
		btn[0]=new Button("BACK");
		btn[1]=new Button("¡ã");
		btn[2]=new Button("GO");
		btn[3]=new Button("¢¸");
		btn[4]=new Button("");
		btn[5]=new Button("¢º");
		btn[6]=new Button("");
		btn[7]=new Button("¡å");
		btn[8]=new Button("");
		
		btn[0].setVisible(false);
		btn[2].setVisible(false);
		btn[6].setVisible(false);
		btn[8].setVisible(false);
		
		btn[0].setForeground(Color.BLUE);
		btn[2].setForeground(Color.BLUE);
		
		
		/**
	     * Button Action
	     */
		for(int i=0;i<btn.length;i++)
		{
			add(btn[i]);
			btn[i].setFont(new Font("°íµñ,",Font.BOLD,30));
			if(i!=0&&i!=2)
				btn[i].setBackground(Color.WHITE);
			else
				btn[i].setBackground(Color.GRAY);
			btn[i].setEnabled(false);
		}


		ActionListener btn0=new ActionListener(){

			public void actionPerformed(ActionEvent e){
				if(backreplayflag)
				{
					btn[0].setBackground(Color.PINK);
					backreplayflag=false;
				}
			}
		};
		btn[0].addActionListener(btn0);


		ActionListener btn1=new ActionListener(){

			public void actionPerformed(ActionEvent e){
				if(flag)
				{
					btn[1].setBackground(Color.YELLOW);

					direction="up";
					flag=false;
				}
			}
		};
		btn[1].addActionListener(btn1);

		ActionListener btn2=new ActionListener(){

			public void actionPerformed(ActionEvent e){
				if(forwardreplayflag)
				{
					btn[2].setBackground(Color.PINK);
					forwardreplayflag=false;
				}
			}
		};
		btn[2].addActionListener(btn2);


		ActionListener btn3=new ActionListener(){

			public void actionPerformed(ActionEvent e){
				if(flag)
				{
					btn[3].setBackground(Color.YELLOW);
					direction="left";
					flag=false;
				}
			}
		};

		btn[3].addActionListener(btn3);

		ActionListener btn5=new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(flag)
				{
					btn[5].setBackground(Color.YELLOW);
					direction="right";
					flag=false;
				}
			}
		};
		btn[5].addActionListener(btn5);
		
		ActionListener btn7=new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(flag)
				{
					btn[7].setBackground(Color.YELLOW);
					direction="down";
					flag=false;
				}
			}
		};
		
		btn[7].addActionListener(btn7);
	}
	
	
	/**
     * This method control the button to be active or not
     */
	public void setEnabledButton(int a)
	{
		for(int i=0;i<btn.length;i++)
		{
			if(a==1)
			{
				if(i==1||i==3||i==5||i==7)
					btn[i].setEnabled(true);
			}
			else
				btn[i].setEnabled(false);
		}
	}

	
	/**
     * If the game is finished, this method activates the replay button
     */
	public void setReplayButton()
	{
		for(int i=0;i<btn.length;i++)
			if(i==0||i==2)
			{
				btn[i].setVisible(true);
				btn[i].setEnabled(true);
				btn[i].setBackground(Color.WHITE);
			}
			else
			{
				btn[i].setBackground(Color.WHITE);
				btn[i].setEnabled(false);
			}
	}

	/**
     * If Client selects the button then button color changes to yellow.
     * And the turn is finished, this method changes 
     * the color of the button to white. 
     */
	public void setButtonBackground()
	{
		for(int i=0;i<btn.length;i++)
		{
			if(i!=0&&i!=2)
				btn[i].setBackground(Color.WHITE);			
		}
	}

	/**
     * If Client selects the replay button then button color changes to pink.
     * And the turn is finished, this method changes 
     * the color of the replay button to white. 
     */
	public void setReplayButtonBackground()
	{
		for(int i=0;i<btn.length;i++)
		{
			if(i==0||i==2)
				btn[i].setBackground(Color.WHITE);			
		}
	}
}


