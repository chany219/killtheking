package Client;

import javax.swing.*;
import java.awt.*;
import Client.*;

public class Matix_Graphic extends JPanel {

   /*
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
   */
	ImageIcon orgimage=new ImageIcon("tree.png");
	   Image temp=orgimage.getImage();
	   Image tmp=temp.getScaledInstance(70, 70, ABORT);
	   ImageIcon image=new ImageIcon(tmp);
	   
	   ImageIcon orgimage2=new ImageIcon("king.png");
	   Image temp2=orgimage2.getImage();
	   Image tmp2=temp2.getScaledInstance(70, 70, ABORT);
	   ImageIcon image2=new ImageIcon(tmp2);
	   
	   ImageIcon orgimage3=new ImageIcon("ash.png");
	   Image temp3=orgimage3.getImage();
	   Image tmp3=temp3.getScaledInstance(70, 70, ABORT);
	   ImageIcon image3=new ImageIcon(tmp3);
	   
	   ImageIcon orgimage4=new ImageIcon("citizen.png");
	   Image temp4=orgimage4.getImage();
	   Image tmp4=temp4.getScaledInstance(70, 70, ABORT);
	   ImageIcon image4=new ImageIcon(tmp4);
   JButton[][]bnt= new JButton[8][8];
   
   Matix_Graphic(){
	   setOpaque(false);
	   setLayout(new GridLayout(8,8));
         
      for(int i=0;i<8;i++)
         for(int j=0;j<8;j++)
         {
            bnt[i][j]=new JButton(image);
            
            add(bnt[i][j]);
            bnt[i][j].setBackground(Color.WHITE);
         }
      
   }
   public void moving(int i,int j) {
	  
	   bnt[i][j].setIcon(image4); 
	 
   }
}