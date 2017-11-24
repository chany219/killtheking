package Client;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class ButtonPanel extends JPanel {

	public static String direction;
	public static boolean flag=true;
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
			btn[i].setFont(new Font("���,",Font.BOLD,30));
			if(i!=0&&i!=2)
				btn[i].setBackground(Color.YELLOW);
			else
				btn[i].setBackground(Color.WHITE);
		}

		ActionListener btn1=new ActionListener(){

			public void actionPerformed(ActionEvent e){
				if(flag)
				{
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
					direction="left";
					flag=false;
				}
			}
		};


		ActionListener btn4=new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(flag)
				{
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
					direction="right";
					flag=false;
				}
			}
		};
		btn[5].addActionListener(btn5);
	}
}

