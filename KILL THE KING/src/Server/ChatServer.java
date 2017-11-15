package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ChatServer {

	private static final int PORT = 9001;

	//I use HashMap in order to manage many print writers for all the clients.
	//The key value is print writers, and value is print writer's ID.
	//It's easy to find certain print writers through the id.
	private static HashMap<PrintWriter,String> writers = new HashMap<PrintWriter,String>();

	public static void main(String[] args) throws Exception {
		System.out.println("The chat server is running.");
		ServerSocket listener = new ServerSocket(PORT);
		try {
			while (true) {
				new Handler(listener.accept()).start();
			}
		} finally {
			listener.close();
		}
	}

	private static class Handler extends Thread {
		private String name;
		private Socket socket;
		private BufferedReader in;
		private PrintWriter out;

		
		public Handler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {
				// Create character streams for the socket.
				in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);

				// Request a name from this client.  Keep requesting until
				// a name is submitted that is not already used.  Note that
				// checking for the existence of a name and adding the name
				// must be done while locking the set of names.
				while (true) {
					out.println("SUBMITNAME");
					name = in.readLine();
					if (name == null) {
						return;
					}
					synchronized (writers) {
						if (!writers.containsValue(name)) {
							break;
						}
					}
				}

				//Broadcast to all clients in the chatting room that new user enters.
				for (PrintWriter writer : writers.keySet()) {
					writer.println("MESSAGE "+"<" + name + "> enters our chatting room");
				}

				// Now that a successful name has been chosen, add the
				// socket's print writer to the set of all writers so
				// this client can receive broadcast messages.
				out.println("NAMEACCEPTED");
				
				//Store the print writer and id in the HashMap.
				writers.put(out,name);

				// Accept messages from this client and broadcast them.
				// Ignore other clients that cannot be broadcasted to.
				while (true) {

					String input = in.readLine();
					if (input == null) {
						return;
					}

					//If server Whisper messages that starts with "/w"
					else if(input.startsWith("/w"))
					{
						//Using split, seperate name and message, then store in array. 
						String[] whisper=input.split(" ");

						//find the whispered client.
						for (PrintWriter writer : writers.keySet()) {
							String s=writers.get(writer);
							if(s.equals(whisper[1]))
							{
								String msg="";
								for(int i=2;i<whisper.length;i++)
									msg+=(whisper[i]+" ");
								
								//broadcast the whisper message only sending client and sent client
								writer.println("MESSAGE "+"Whisper from <"+name+"> : "+msg);
								out.println("MESSAGE "+"Whisper to <"+whisper[1]+"> : "+msg);
								break;
							}
							
						}         
					}
					else
						//broadcast message to all clients in the chatting room
						for (PrintWriter writer : writers.keySet()) 
							writer.println("MESSAGE " + name + ": " + input);
				}

			} catch (IOException e) {
				System.out.println(e);
			} finally {
				// This client is going down!  Remove its name and its print
				// writer from the sets, and close its socket.
				if (out != null) {
					writers.remove(out);
				}
				//Broadcast to all clients in the chatting room that user exits.
				for (PrintWriter writer : writers.keySet()) {
					writer.println("MESSAGE "+"<" + name + "> exits from our chatting room");
				}
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
	}
}