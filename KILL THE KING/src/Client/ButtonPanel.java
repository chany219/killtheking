package Client;

import java.awt.Button;
import java.awt.GridLayout;

import javax.swing.JPanel;

public class ButtonPanel extends JPanel {

	ButtonPanel(){
		setOpaque(false);
		setLayout(new GridLayout(2,3));
		add(new Button(""));
		add(new Button("ก่"));
		add(new Button(""));
		add(new Button("ก็"));
		add(new Button("ก้"));
		add(new Button("กๆ"));
	}
}

