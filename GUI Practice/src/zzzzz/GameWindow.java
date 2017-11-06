package zzzzz;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class GameWindow extends JFrame implements ActionListener
	{
	  JButton b[][] = new JButton[5][5];

	  int v1[] = { 2, 5, 3, 7, 10 };
	  int v2[] = { 3, 5, 6, 9, 12 };

	  public GameWindow(String title)
	  {
	    super(title);

	    setLayout(new GridLayout(5, 5));
	    setDefaultCloseOperation(EXIT_ON_CLOSE );

	    for (int i = 0; i < 5; i++)
	      for (int j = 0; j < 5; j++)
	      {
	        b[i][j] = new JButton();
	       // b[i][j].addActionListener((ActionListener) this);
	        add(b[i][j]);
	      }
	      
	  }

	  @Override
	  public void actionPerformed(ActionEvent ae) {
	    ((JButton)ae.getSource()).setBackground(Color.red);
	  }
	}

