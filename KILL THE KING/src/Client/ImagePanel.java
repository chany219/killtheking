package Client;
import java.awt.*;
import javax.swing.*;

public class ImagePanel extends JPanel{
   Image image;

   public ImagePanel(){
      image = new ImageIcon("LoginScreenimage.png").getImage();
   }

   public void paint(Graphics g){
      g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
      setOpaque(false);
      super.paint(g);   
   }
}