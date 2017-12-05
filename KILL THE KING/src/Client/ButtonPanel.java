package Client;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class ButtonPanel extends JPanel  {

	public static boolean flag=true;
	public static String direction;

	Button []btn=new Button[6];
	ButtonPanel(){
		setOpaque(false);
		setLayout(new GridLayout(2,3));
		btn[0]=new Button(" ");
		btn[1]=new Button("��");
		btn[2]=new Button(" ");
		btn[3]=new Button("��");
		btn[4]=new Button("��");
		btn[5]=new Button("��");

		for(int i=0;i<btn.length;i++)
		{
			add(btn[i]);
			btn[i].setFont(new Font("����,",Font.BOLD,30));
			if(i!=0&&i!=2)
				btn[i].setBackground(Color.WHITE);
			else
				btn[i].setBackground(Color.GRAY);
			btn[i].setEnabled(false);
		}


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

		ActionListener btn4=new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(flag)
				{
					btn[4].setBackground(Color.YELLOW);
					direction="down";
					flag=false;
				}
			}
		};
		btn[4].addActionListener(btn4);
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
	}
	public void setEnabledButton(int a)
	{
		for(int i=0;i<btn.length;i++)
			if(a==1)
			btn[i].setEnabled(true);
			else
				btn[i].setEnabled(false);
	}
	public void setButtonBackground()
	{
		for(int i=0;i<btn.length;i++)
		{
			if(i!=0&&i!=2)
				btn[i].setBackground(Color.WHITE);
		}
	}
}


