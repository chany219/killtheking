package zzzzz;

import javax.swing.*;

import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.color.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

import javax.swing.JButton;
import javax.swing.JFrame;


public class Graphic extends JFrame{
	
	ImageIcon icon= new ImageIcon("zzzzzz.png");
	   JLabel k=new JLabel();
	   
	   public Graphic(){
		      this.setTitle("10ÇÈ¼¿¾¿ ÀÌµ¿");
		      this.setSize(1000,1000);
		      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		      this.setLayout(null);

		      k.setIcon(icon);
		      k.setLocation(50,50);
		      k.setSize(1000,1500);
		      k.setBounds(00, 400, 500, 500);
		      this.add(k);
		      this.setVisible(true);

	   }
	public void paint(Graphics g){

		g.fillRect(100, 100, 400, 400);
		for(int i=100;i<=400;i+=100){
			for(int j=100;j<=400;j+=100)
			{
				g.clearRect(i, j, 50, 50);
			}
		}

		for(int i=150;i<=450;i+=100)
		{
			for(int j=150;j<=450;j+=100)
			{
				g.clearRect(i, j, 50, 50);
			}
		}
	}
}