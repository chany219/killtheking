package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;


/**
 * A multi-threded game server. when a client connects the server,
 * two thread(handler_chatting,handler_button) is executed.  
 * @author bumjun
 *
 */

public class ServerClass {

   /**
    *  HashMap writers : the set of all names of client in chat thread.
    *  so that we can check that new clients are not registering name 
    *  already in use.
    *  HashMap role : the set of all role number of client in chat thread.
    *  so that we can check that new clients are not registering 
    *  role number already in use.
    *  HashMap ROLE : the set of all role number of client in button thread.
    *  so that we can send button protocol to button thread in client.
    */
   private static final int PORT_CHAT = 9001;
   private static final int PORT_BUTTON = 9002;
   private static HashMap<PrintWriter,String> writers = new HashMap<PrintWriter,String>();
   private static HashMap<String,Integer>role =new HashMap<String,Integer>(); 
   private static HashMap<PrintWriter,Integer>ROLE =new HashMap<PrintWriter,Integer>();
   private static String[] nameByRole = new String[5];
   private static boolean[] ready ={false,false,false,false,false};
   private static boolean[] isLive = {true,true,true,true,true};
   private static boolean[] nowDead = {false,false,false,false,false};
   private static int[][] nowLocation = new int[4][2];
   private static int[][] prevLocation = new int[4][2];
   private static int NUMBER=4;
   private static int turnNum=0;
   private static int[][] status =new int[8][8];
   private static boolean t=false;
   private static int[] deadPosition =new int[2];
   private static String[] turnStatus =new String[20];

   /**
    * The application main method, which just listens on a port and 
    * spawns handler threads.
    * @param args
    * @throws Exception
    */
   public static void main(String[] args) throws Exception {
      System.out.println("THE GAME SERVER IS RUNNING.");
      ServerSocket listener1 = new ServerSocket(PORT_CHAT);
      ServerSocket listener2 = new ServerSocket(PORT_BUTTON);
      try {
         while (true) {
            new Handler_chatting(listener1.accept()).start();
            new Handler_button(listener2.accept()).start();
         }
      } finally {
         listener1.close();
         listener2.close();
      }
   }

   /**
    * A chat handler thread.  Handlers are spawned from the listening
    * loop and are responsible for a dealing with a single client
    * and broadcasting its messages.
    * Make sure that there are four clients in the room until game is start.
    */
   private static class Handler_chatting extends Thread {
      private String name;
      private Socket socket;
      private BufferedReader in;
      private PrintWriter out;
      private int roleNum;


      /**
       * Constructs a handler thread, squirreling away the socket.
       * All the interesting work is done in the run method.
       */
      public Handler_chatting(Socket socket) {
         this.socket = socket;
      }


      /**
       * Services this thread's client by repeatedly requesting a
       * screen name until a unique one has been submitted, then
       * acknowledges the name and registers the output stream for
       * the client in a global set, then repeatedly gets inputs and
       * broadcasts them.
       */
      public void run() {
         try {

            // Create character streams for the socket.
            in = new BufferedReader(new InputStreamReader(
                  socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            out.println(writers.size());

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


            // Now that a successful name has been chosen, add the
            // socket's print writer to the set of all writers so
            // this client can receive broadcast messages.
            // broadcast message that the client entered this chat room.

            for (PrintWriter writer : writers.keySet()) {
               writer.println("MESSAGE "+"<" + name + "> enters our room("+(writers.size()+1)+"/4)");
            }
            out.println("NAMEACCEPTED");

            writers.put(out,name);

            //Assign a non-redundant role number to clients entering the room.
            if(writers.size()<=NUMBER){

               Random random = new Random();
               while(true) {
                  int temp = (int)random.nextInt(NUMBER);

                  synchronized(role) {
                     if(!role.containsValue(temp)) {
                        role.put(name,temp);
                        nameByRole[temp]="";
                        nameByRole[temp]+=name;
                        out.println(temp);
                        break;
                     }
                  }
               }
            }
            /**
             * in while loop, receive chat string from client,
             * and broadcast the chat string to all clients.
             * check if four clients entered in the room.
             * all four clients entered, send "GAMESTART" to all clients.
             * 
             */
            while (true) {

               if(writers.size()==NUMBER) {
                  for(PrintWriter writer : writers.keySet()){
                     writer.println("GAMESTART");
                  }
               }

               String input = in.readLine();
               if (input == null) {
                  return;
               } else if(input.equals("GAMESTARTACCEPTED")) {
                  break;
               }

               else
                  for (PrintWriter writer : writers.keySet()) 
                     writer.println("MESSAGE " + name + ": " + input);
            }
            roleNum=role.get(name);
            //announce their role to each client 

            if(roleNum==0){
               out.println("BROADCAST  You are the King. ");
            }
            else if(roleNum==1){
               out.println("BROADCAST  You are the Spy. ");
            }
            else
               out.println("BROADCAST  You are the Citizen. ");

            if(roleNum==0) {
               for (PrintWriter writer : writers.keySet()) 
                  writer.println("BROADCAST "+name+ " is the King. Let's catch the king!!");
            }

            // after game starts, chat server receive string and broadcast chat contents.
            while(true) {
               String input = in.readLine();
               if (input == null) {
                  return;
               }
               for (PrintWriter writer : writers.keySet()) 
                  writer.println("MESSAGE " + name + ": " + input);
            }

         } catch (IOException e) {
            System.out.println(e);
         } finally {
            // This client is going down!  Remove its name and its print
            // writer from the sets, and close its socket.
            // broadcast message that the client exited the chat room.
            if (out != null) {
               writers.remove(out);
               role.remove(name);
            }

            for (PrintWriter writer : writers.keySet()) {
               writer.println("MESSAGE "+"<" + name + "> exits from our room");
            }
            try {
               socket.close();
            } catch (IOException e) {
            }
         }
      }
   }

   /**
    * @author bumjun
    * A button handle thread. Handler are spawned from the listening loop
    * and are responsible for a dealing with a single client.
    * When the selection of all the clients' buttons is completed,
    * judge the win / loss of the king and proceed or end the game.
    */
   public static class Handler_button extends Thread {
      private Socket socket;
      private BufferedReader in;
      private PrintWriter out;
      public Handler_button(Socket socket) {
         this.socket = socket;
      }
      public void run() {

         try {
            int x=0 ,y=0;

            String input;

            for(int i=0;i<20;i++) {
               turnStatus[i]="";
            }
            // Create character streams for the socket.
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String line=in.readLine();
            int tempNum=Integer.parseInt(line);
            ROLE.put(out, tempNum);
            /*
             * keep requesting until first matrix location of all clients is submitted 
             */
            while(true) {
               input = in.readLine();
               x=Integer.parseInt(input.substring(0,1));
               y=Integer.parseInt(input.substring(2,3));
               nowLocation[ROLE.get(out)][0]=x;
               nowLocation[ROLE.get(out)][1]=y;
               ready[ROLE.get(out)]=true;
               if(ready[0]&&ready[1]&&ready[2]&&ready[3]) {
                  for(int i=0;i<NUMBER;i++) 
                     ready[i]=false;

                  if(computeFirstLocation()) {

                     /** Set first matrix location using firstStatus function
                      *  If the client's matrix position overlaps, it sends "invalid", otherwise it sends "valid"
                      */
                     firstStatus();
                     turnStatus[turnNum]+=stringToStatus();
                     turnNum++;
                     for (PrintWriter writer : ROLE.keySet()){ 
                        writer.println("VALID");
                        if(ROLE.get(writer)==0) {
                           String temp="";
                           for(int k=0;k<NUMBER;k++) {
                              temp+=nowLocation[k][0]+""+nowLocation[k][1]+" ";
                           }
                           writer.println(temp);

                        }
                        else {
                           writer.println(nowLocation[0][0]+""+nowLocation[0][1]);
                        }
                     }

                  } else {

                     for (PrintWriter writer : ROLE.keySet()) 
                        writer.println("INVALID");
                  }
               }
               /**
                * server receive VALIDACCEPTED protocol, break while loop
                * and go to next turn.
                */
               input =in.readLine();
               if(input.equals("VALIDACCEPTED")){
                  break;
               }
            }
            while(true) {

               // receive string up/down/left/right and move that client's location
               String input2 = in.readLine();
               if(input2.startsWith("GAMEFINISH"))
                  break;
               prevLocation[ROLE.get(out)][0]=nowLocation[ROLE.get(out)][0];
               prevLocation[ROLE.get(out)][1]=nowLocation[ROLE.get(out)][1];

               ready[ROLE.get(out)]=true;
               if(input2.equals("up")) {
                  nowLocation[ROLE.get(out)][0]--;
               } else if(input2.equals("down")) {
                  nowLocation[ROLE.get(out)][0]++;
               } else if(input2.equals("left")) {
                  nowLocation[ROLE.get(out)][1]--;
               } else if(input2.equals("right")) {
                  nowLocation[ROLE.get(out)][1]++;
               }
               /**if all of clients selected location, 
                * update the status matrix, and Judge the win or loss of the game.
                */
               if(ready[0]&&ready[1]&&ready[2]&&ready[3]) {

                  statusUpdate();           
                  computeMoveLocation();
                  System.out.println(displayStatus());
                  turnStatus[turnNum]+=stringToStatus();
                  turnNum++;

                  for(int i=0;i<NUMBER;i++) {
                     if(isLive[i])
                        ready[i]=false;
                  }

               }
               /**
                * server receive GAMEFINISH, break while loop 
                * and show replay screen for all clients.
                */
               String input3=in.readLine(); 
               if(input3.startsWith("GAMEFINISH"))
                  break;
            }
            while(true) {

               String input2 = in.readLine();
               if(input2.startsWith("REPLAY")){
                  int temp=Integer.parseInt(input2.substring(6));
                  out.println(turnStatus[temp]);

               }
            }

         } catch(IOException e) {
            System.out.println(e);
         }
         finally {
            if(out!=null)
               ROLE.remove(out);
            try {
               socket.close();
            } catch(IOException e) {

            }
         }

      }

      /**
       * If any of the client's first locations overlap, it returns false.
       * otherwise, return true.
       */
      public boolean computeFirstLocation() {

         for(int i=0;i<NUMBER-1;i++) {
            for(int j=i+1;j<=NUMBER-1;j++) {
               if(i==j)
                  continue;
               if(nowLocation[i][0]==nowLocation[j][0]&&nowLocation[i][1]==nowLocation[j][1]) {
                  return false;
               }
            }
         }
         return true;
      }

      /**
       *  Each turn, each client's movement is compared to determine the king's death,
       *  client's death, and turn over. 
       */

      public void computeMoveLocation() {
         for(int i=1;i<NUMBER;i++) {
            if(!isLive[i])
               continue;
            if(nowLocation[0][0]==nowLocation[i][0]&&nowLocation[0][1]==nowLocation[i][1]) {
               status[nowLocation[0][0]][nowLocation[0][1]]=8;
               for (PrintWriter writer : ROLE.keySet()) 
                  writer.println("CITIZENWIN");
               return;
            }
         }
         //if king can't move any direction, broadcast that game is Citizen's win.
         if(isKingLocked()){
            for (PrintWriter writer : ROLE.keySet()) 
               writer.println("CITIZENWIN-");
            return;
         }

         String statusStr=stringToStatus();
         //check citizen's dead
         for(int i=1;i<NUMBER-1;i++) {
            for(int j=i+1;j<=NUMBER-1;j++) {
               if(i==j||isLive[i]==false||isLive[j]==false)
                  continue;
               if(nowLocation[i][0]==nowLocation[j][0]&&nowLocation[i][1]==nowLocation[j][1]) {
                  if(isLive[i]) {
                     for (PrintWriter writer : ROLE.keySet()) {
                        if(i==ROLE.get(writer)) {
                           nowDead[i]=true;
                           deadPosition[0]=nowLocation[i][0];
                           deadPosition[1]=nowLocation[i][1];
                           status[deadPosition[0]][deadPosition[1]]=7;

                           break;
                        }
                     }
                     isLive[i]=false;
                  }
                  if(isLive[j]) {
                     for (PrintWriter writer : ROLE.keySet()) {
                        if(j==ROLE.get(writer)) {
                           nowDead[j]=true;
                           break;
                        }
                     }
                     isLive[j]=false;
                  }
               }
            }
         }
         // if all of citizen is dead, broadcast king's win
         if(isLive[1]&&isLive[2]&&isLive[3]) {
            if(nowLocation[1][0]==nowLocation[2][0]&&nowLocation[1][1]==nowLocation[2][1]&&nowLocation[3][0]==nowLocation[1][0]&&nowLocation[3][1]==nowLocation[3][1])
            {
               for (PrintWriter writer : ROLE.keySet()) 
                  writer.println("KINGWIN");
               return;
            }

         }
         if((isLive[2]==false&&isLive[3]==false)||(isLive[2]==false&&isLive[3]==false)) {
            for (PrintWriter writer : ROLE.keySet()) 
               writer.println("KINGWIN");
            return;
         }
         // if the turn number is over 20, broadcast king's win
         if(turnNum==19) {
            for (PrintWriter writer : ROLE.keySet()) 
               writer.println("KINGWIN-");
            return;
         }
         String deadList="";
         for(int i=0;i<NUMBER;i++) {
            if(nowDead[i]) {
               t=true;
               break; 
            }   
         }
         if(t) {
            deadList+="ISDEAD ";
            t=false;
         } else {
            deadList+="NOTDEAD ";
         }
         for(int i=0;i<NUMBER;i++) {
            if(nowDead[i])
               deadList+=nameByRole[i]+" ";
         }
         deadList=deadList+"dead! /"+deadPosition[0]+deadPosition[1]+"/";
         for(int i=0;i<NUMBER;i++) {
            if(nowDead[i])
               deadList+=i;
         }
         for (PrintWriter writer : ROLE.keySet()) {
            if(isLive[ROLE.get(writer)]==true||nowDead[ROLE.get(writer)]==true)
               writer.println(deadList);
         }

         String nowTemp="";
         String prevTemp="";
         for(int i=0;i<NUMBER;i++) {
            if(isLive[i]) {
               nowTemp+=nowLocation[i][0]+""+nowLocation[i][1]+" ";
               prevTemp+=prevLocation[i][0]+""+prevLocation[i][1]+" ";
            }
         }
         for(int i=0;i<NUMBER;i++) {
            if(isLive[i]) {
               nowTemp+=i+" ";
            }
         }
         /**
          * It sends its status and coordinates to each client.
          * The king and the dead client additionally send the coordinates of all.
          */
         for (PrintWriter writer : ROLE.keySet()) {
            if(isLive[ROLE.get(writer)]&&nowDead[ROLE.get(writer)]==false)
               writer.println("SAFE");
            if(isLive[ROLE.get(writer)]&&ROLE.get(writer)!=0){
               writer.println(nowLocation[ROLE.get(writer)][0]+""+nowLocation[ROLE.get(writer)][1]);
            }
            if(isLive[ROLE.get(writer)]==false&&nowDead[ROLE.get(writer)]==false)
               writer.println("OBSERVER");
            if(ROLE.get(writer)==0||isLive[ROLE.get(writer)]==false) {
               if(nowDead[ROLE.get(writer)]==false){
                  writer.println(prevTemp+nowTemp);
               }
            }
            if(nowDead[ROLE.get(writer)]) {
               writer.println("DEAD");
               writer.println(statusStr);

               nowDead[ROLE.get(writer)]=false;
            }

         }



         // set matrix of status for each client's first location

      }
      public static void firstStatus() {
         for(int i=0;i<8;i++) {
            for(int j=0;j<8;j++) {
               status[i][j]=5;
            }
         }
         for(int i=0;i<NUMBER;i++) {
            if(i==0) {
               status[nowLocation[i][0]][nowLocation[i][1]]=i;      
            } else if(isLive[i]) {
               status[nowLocation[i][0]][nowLocation[i][1]]=i;
            }
         }
      }

      // after first turn
      // set matrix of status for each client's moved location

      public static void statusUpdate() {


         for(int i=0;i<NUMBER;i++) {
            if(i==0) {
               if(status[prevLocation[i][0]][prevLocation[i][1]]!=7&&status[prevLocation[i][0]][prevLocation[i][1]]==i){
                  status[prevLocation[i][0]][prevLocation[i][1]]=5;
               }
               status[nowLocation[i][0]][nowLocation[i][1]]=i;
            }
            else if(isLive[i]) {
               if(status[prevLocation[i][0]][prevLocation[i][1]]!=7&&status[prevLocation[i][0]][prevLocation[i][1]]==i){
                  status[prevLocation[i][0]][prevLocation[i][1]]=6;
               }
               status[nowLocation[i][0]][nowLocation[i][1]]=i;
            }
         }

         // the present matrix information change to string for sending client 
      }
      public static String stringToStatus() {
         String temp="";

         for(int i=0;i<8;i++)  {
            for(int j=0;j<8;j++) {
               temp+=status[i][j]+" ";
            }
         }
         return temp;
      }
      // display game status to console
      public static String displayStatus() {

         String dp="";

         for(int i=0;i<8;i++) {
            for(int j=0;j<8;j++) {
               dp+=status[i][j]+" ";
            }
            dp+="\n";
         }
         return dp;
      }
      // return true if king cannot move to all direction
      // otherwise return false.
      public static boolean isKingLocked() {
         boolean lock[]={false,false,false,false};
         //upper
         if(nowLocation[0][0]-1<0||status[nowLocation[0][0]-1][nowLocation[0][1]]!=5) {
            lock[0]=true;
         }
         //down
         if(nowLocation[0][0]+1>7||status[nowLocation[0][0]+1][nowLocation[0][1]]!=5) {
            lock[1]=true;
         }
         //left
         if(nowLocation[0][1]-1<0||status[nowLocation[0][0]][nowLocation[0][1]-1]!=5) {
            lock[2]=true;
         }
         //right
         if(nowLocation[0][1]+1>7||status[nowLocation[0][0]][nowLocation[0][1]+1]!=5) {
            lock[3]=true;
         }
         if(lock[0]&&lock[1]&&lock[2]&&lock[3])
            return true;
         else return false;
      }
   }
}