package game;

class ProgressInfo {
	
	
	public static int turnSeq;
	public static int[][][] location=new int[5][100][2];
	public static int[][] tempLocation =new int[5][2];  
	public static boolean[] state={false,false,false,false,false};
	public static boolean[] ready={false,false,false,false,false};
	
	public static boolean checkStart() {
		if(ready[0]==true&&ready[1]==true&&ready[2]==true&&ready[3]==true&&ready[4]==true)
			return true;
		else return false;
	}
	public static boolean checkFirstLocation() {
		for(int i=0;i<5;i++) {
			for(int j=0;j<5;j++) {
				if(i==j)
					continue;
				if(tempLocation[i][0]==tempLocation[j][0]&&tempLocation[i][1]==tempLocation[j][1])
					return false;
			}
		}
		return true;
	}
}
