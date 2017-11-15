package Client;

import javax.swing.*;
import java.awt.*;
import Client.*;

public class Matix_Graphic extends JPanel {

	public void paint(java.awt.Graphics g) {
		

				
		int[][] primes = new int[8][8];
		int[][] prime={{1,0,0,0,0,0,0,1},{1,0,0,1,0,0,0,0},{1,0,0,1,0,0,0,0},{1,0,1,0,0,0,0,0},{1,0,0,0,0,0,0,0},
				{1,0,0,0,0,0,0,1},{1,0,0,0,1,0,0,0},{1,0,0,1,0,0,0,0}};

		int i=0;
		g.setColor(Color.WHITE);
		g.fillRect(20, 25, 480, 480); //전체 배경화면을 흰색 사각형으로 채움 (20,25)에 (480x480)크기 만

		g.setColor(Color.BLACK); //검은색으로 선을 그
		for(i=0;i<=480; i=i+60){
			g.drawLine(20, 25+i, 500, 25+i);  //시작하는 점 x,y 끝나는 점 x1,y1
		}

		for(i=0;i<=480; i=i+60)
			g.drawLine(20+i, 25, 20+i, 25+480);
		
		g.setColor(Color.lightGray);
		for(i=0; i<8; i++){
			for(int j=0; j<8; j++){
				if(prime[i][j]==1){
					
					g.fillRect(20+60*i+1, 25+60*j+1, 59, 58); //정해진 어레이에 따라 회색 사각형을 그리도록 함 아주 잘된 
				}
			}
		}
		
		
		
	}
	
	
	
}
