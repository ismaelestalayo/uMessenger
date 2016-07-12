package umessenger;

import java.net.*;
import java.io.*;

public class ChatServer implements Runnable {

    private ChatServerThread clients[] = new ChatServerThread[10];
    private ServerSocket server = null;
    private Thread thread = null;
    private int clientCount = 0;
    
    public final String C_RST = "\u001B[0m";
    public final String C_BLACK = "\u001B[30m";
    public final String C_RED = "\u001B[31m";
    public final String C_GREEN = "\u001B[32m";
    public final String C_YELLOW = "\u001B[33m";
    public final String C_BLUE = "\u001B[34m";
    public final String C_PURPLE = "\u001B[35m";
    public final String C_CYAN = "\u001B[36m";
    public final String C_WHITE = "\u001B[37m";
    
    //CONSTRUCTOR///////////////////////////////////////////////////////////////
    public ChatServer(int port) {
        try {
            System.out.println("Binding to port " + port + ", please wait...");
            server = new ServerSocket(port);
            System.out.println("Server started successfully. ");
            start();
            
        } catch (IOException ex) {
            System.out.println("Can not bind to port " + port + ": " + ex);
        }
    }
    
    //MAIN//////////////////////////////////////////////////////////////////////
    public static void main(String args[]) {
        ChatServer server = null;
        server = new ChatServer(1234);
    }
    
    //METHODS///////////////////////////////////////////////////////////////////
    @Override
    public void run() {
        while (thread != null) {
            try {
                System.out.println("Waiting for a new client...");
                addThread(server.accept() );
                
            } catch (IOException ex) {
                System.out.println("Server accept error: " + ex);
                closeAll();
            }
        }
    }
    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }
    public void closeAll() {
        if (thread != null) {
            thread.stop();
            thread = null;
        }
   }
    private int findClient(int ID) {
        for (int i = 0; i < clientCount; i++) {
            if (clients[i].getID() == ID)
                return i;
        }
        
        return -1;
    }

    public synchronized void broadcast(String userName, int ID, String msg){
        
        if(msg.equals("/fin") ){
            for (int i = 0; i < clientCount; i++)
                clients[i].sendMsg("FIN" +"º"+ userName +"º"+ "doesn't mind");
            
            removeClient(ID);
        } 
        
        else if(msg.equals("/help") ){
            for (int i = 0; i < clientCount; i++)
                if(clients[i].getUserName().equals(userName) )
                    clients[i].sendMsg("INFO" +"º"+ userName +"º"+ helpMessage() );
            
        }
        
        else if(msg.equals("/IPs")){
            String userList = "";
            
            for (int i = 0; i < clientCount; i++)
                userList += clients[i].getUserName() + " - " +clients[i].getUserIP()+ "\n";
            
            for (int i = 0; i < clientCount; i++)
                if(clients[i].getUserName().equals(userName) )
                    clients[i].sendMsg("INFO" +"º"+ userName +"º"+ userList );
        }
        
        else if(msg.startsWith("/send") ){
            String target = msg.split("/send")[1];
            String targetIP = "";
            boolean sent = false;
            for(int i = 0; i < clientCount; i++){
                if(clients[i].getUserName().equals(target)){
                    targetIP = clients[i].getUserIP();
                    sent = true;
                }
            }
            //If there was an user with that name:
            if(sent){
                for (int i = 0; i < clientCount; i++){
                    if(clients[i].getUserName().equals(userName)){
                        clients[i].sendMsg("FILE" +"º"+ userName +"º"+ targetIP);
                    }
                    else if(clients[i].getUserName().equals(target) ){
                        clients[i].sendMsg("FILE" +"º"+ userName +"º"+ "doesn't mind" );
                    }
                }
            }
            else{
                for (int i = 0; i < clientCount; i++){
                    if(clients[i].getUserName().equals(userName)){
                        clients[i].sendMsg("INFO" +"º"+ userName +"º"+ "User " 
                                +target+ " wasnt found.");
                    }
                }
            }
        }
        
        else if(msg.equals("/users") ){
            String userList = "";
            //For some reason concat() is not working properly for me
            for (int i = 0; i < clientCount; i++)
                userList += clients[i].getUserName() + "\n";
            
            for (int i = 0; i < clientCount; i++)
                if(clients[i].getUserName().equals(userName) )
                    clients[i].sendMsg("INFO" +"º"+ userName +"º"+ userList );
            
        }
        
        else if(msg.startsWith("/GODkick") ){
            String userToKick = msg.split("/GODkick")[1];
            Boolean kicked = false;
            for(int i = 0; i < clientCount; i++){
                if(clients[i].getUserName().equals(userToKick)){
                    removeClient(clients[i].getID() );
                    kicked = true;
                }
            }
            if(kicked){
                for (int i = 0; i < clientCount; i++)
                    clients[i].sendMsg("FIN" +"º"+ userToKick +"º"+ "doesn't mind");
            }
            else{
                for (int i = 0; i < clientCount; i++){
                    if(clients[i].getUserName().equals(userName) )
                        clients[i].sendMsg("INFO" +"º"+ userName +"º"+ "User "
                                +userToKick+ " wasn't found." );
                }
            }
        }
        
        //Normal messages:
        else {
            for (int i = 0; i < clientCount; i++) {
                clients[i].sendMsg("CHAT" +"º"+ userName +"º"+ msg);
            }
        }
    }

    public synchronized void removeClient(int ID) {
        int pos = findClient(ID);
        if (pos >= 0) {
            ChatServerThread threadToClose = clients[pos];
            System.out.println(C_RED
                    + "Removing " + clients[pos].getUserName()
                    + " at thread " + ID + C_RST);
            if (pos < clientCount - 1) {
                for (int i = pos + 1; i < clientCount; i++) {
                    clients[i - 1] = clients[i];
                }
            }
            clientCount--;
            try {
                threadToClose.close();
            } catch (IOException ioe) {
                System.out.println("Error closing thread: " + ioe);
            }
            threadToClose.stop();
        }
    }
    private void addThread(Socket socket) {
        if (clientCount < clients.length) {
            clients[clientCount] = new ChatServerThread(this, socket);
            System.out.println(C_CYAN
                    + "Client accepted: "
                    + socket.getInetAddress().getHostAddress() + C_RST );
            
            try {
                clients[clientCount].openStreams();
                clients[clientCount].start();
                clientCount++;
                
            } catch (IOException ioe) {
                System.out.println("Error opening thread: " + ioe);
            }
            
        } else {
            System.out.println(C_RED
                    + "Maximum client number (" + clients.length
                    + ") reached." + C_RST);
        }
    }
    
    //**************************************************************************
    //**************************************************************************
    //**************************************************************************
    //**************************************************************************
    //**************************************************************************
    
    private String helpMessage(){
        return 
                  "/fin - Ends your current session.\n"
                + "/help - This one\n"
                + "/IPs - Prints a list of users and their IPs\n"
                + "/sendUser - Send a file to the specified user.\n"
                + "/users - Prints a list of online users\n";
    }
}


