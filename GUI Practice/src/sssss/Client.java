package sssss;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JFrame;

import zzzzz.Chat;
import zzzzz.Graphic;
public class Client {

	public static void main(String[] args) throws IOException {
		
	
		Graphic frame=new Graphic();
		
		Chat client = new Chat();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
		
	      frame.setSize(600, 600);
	      frame.getContentPane().add(new Graphic());
	      frame.setLocationRelativeTo(null);
	      frame.setBackground(Color.LIGHT_GRAY);
	      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      frame.setVisible(true);  
	      
		
	      
	}

}
