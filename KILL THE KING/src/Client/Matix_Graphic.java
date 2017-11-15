package Client;

import javax.swing.*;
import java.awt.*;
import Client.*;

public class Matix_Graphic extends JPanel {

	public void paint(java.awt.Graphics g) {

		int i=0;
		g.setColor(Color.WHITE);
		g.fillRect(20, 25, 480, 480);

		g.setColor(Color.BLACK);
		for(i=0;i<=480; i=i+60){
			g.drawLine(20, 25+i, 500, 25+i);
		}

		for(i=0;i<=480; i=i+60)
			g.drawLine(20+i, 25, 20+i, 25+480);
	}
}
