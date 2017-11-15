package game;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
public class Server {


	private static final int PORT = 8888;
	public static HashMap<String,PrintWriter>clients = new HashMap<String,PrintWriter>();
	public static HashSet<PrintWriter>writers = new HashSet<PrintWriter>();
	//role == 0 KING 1 SPY 2,3,4 CITIZEN
	public static HashMap<String,Integer>role =new HashMap<String,Integer>(); 



	public static void main(String[] args) throws Exception {
		System.out.println("THE GAME SERVER IS RUNNING");
		ServerSocket listener = new ServerSocket(PORT);
		try {
			while(true) {
				new Handler(listener.accept()).start();	
			}
		} finally {
			listener.close();
		}
	}



	private static class Handler extends Thread {
		private String nickname;
		private Socket socket;
		private BufferedReader in;
		private PrintWriter out;

	
		public Handler(Socket socket){
			this.socket = socket;
		}



		public void run()
		{

			try{

				in = new BufferedReader (new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter (socket.getOutputStream(),true);
				while (true) {
					out.println("SUBMITNICKNAME");
					nickname = in.readLine();
					if(nickname==null) {
						return;
					}
					synchronized (clients) {
						if (!clients.containsKey(nickname)) {
							clients.put(nickname,out);
							break;
						}
					}
				}
				out.println("NICKNAMEACCEPTED");
				writers.add(out);
		
			
				
					Random random = new Random();
					while(true) {
						int temp = (int)random.nextInt(5);
						
						synchronized(role) {
							if(!role.containsValue(temp)) {
								role.put(nickname,temp);
								break;
							}
						}
					}
					
				
				for(PrintWriter writer : writers) {
					writer.println("MESSAGE "+nickname+" has entered game. ("+clients.size()+"/5)");
				}
				// In while loop
				// Before the game starts
				// only can use chat
				while(true) {
					if(clients.size()==3) {
						for(PrintWriter writer : writers){
							writer.println("GAMESTART");
						}
					}
					String input = in.readLine();
					if(input.equals("GAMESTART")){
						break;
					}
					else {
						for(PrintWriter writer : writers){
							writer.println("MESSAGE "+ nickname + ": "+input);
						}
					}
				}
				// when 5 clients has entered, game is start. 
				int roleNum=role.get(nickname);
				if(roleNum==0){
					out.println("BROADCAST You are the King.");
				}
				else if(roleNum==1){
					out.println("BROADCAST You are the Spy.");
				}
				else
					out.println("BROADCAST You are the Citizen.");
				
				out.println("SELLECT_LOCATE");
				while(true) {
					
					String input = in.readLine();
					if(input==null)
						return;
					else{
						for(PrintWriter writer : writers){
							writer.println("MESSAGE "+ nickname + ": "+input);
						}
					}
				}

			}
			catch(IOException e) {
				System.out.println(e);
			} finally {

				for(PrintWriter writer : writers) {
					writer.println("MESSAGE "+nickname+ "has left game.");
				}
				if(out!=null){
					writers.remove(out);
				}
				if(nickname!=null){
					clients.remove(nickname);
				}
				try {
					socket.close();
				} catch (IOException e) {
				}
			}

		}

	}















}
