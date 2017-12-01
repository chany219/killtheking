package Client;

import java.awt.*;
import javax.swing.*;

public class  LoginScreen extends JFrame{

   Container content;
   ImagePanel imgPanel;

   public LoginScreen(String title){
      super(title);
      imgPanel = new ImagePanel();
      content = getContentPane();     
      content.add(imgPanel, BorderLayout.CENTER);
      setSize(1050,600);
      setLocation(600, 200);
      setVisible(true);
   }
} 