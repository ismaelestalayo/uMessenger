package uMessenger;

import fileTransfer.FileReceiver;
import fileTransfer.FileSender;
import fileTransfer.FileSenderHandler;
import java.net.*;
import java.io.*;

public class ChatServer implements Runnable {

    private ChatServerThread clients[] = new ChatServerThread[10];
    private ServerSocket server = null;
    private Thread thread = null;
    
    private int clientCount = 0;
    
    public final String C_RST = "\u001B[0m";
    public final String C_RED = "\u001B[31m";
    public final String C_CYAN = "\u001B[36m";
    
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
        server = new ChatServer(5000);
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
        
        if(msg.equals("/forcedFin")){
            for (int i = 0; i < clientCount; i++)
                //don't send it to the user who closed the window
                if(!clients[i].getUserName().equals(userName))
                    clients[i].sendMsg("FIN", userName, "doesn't mind");
            
            removeClient(ID);
        }
        
        else if(msg.equals("/newUser")){
            for (int i = 0; i < clientCount; i++)
                clients[i].sendMsg("NEW", userName, "doesn't mind");
            
        }
        
        /////// my commands   ↑ //////////////////////////////////////
        /////// chat commands ↓ //////////////////////////////////////
        
        else if(msg.equals("/fin") ){
            for (int i = 0; i < clientCount; i++)
                clients[i].sendMsg("FIN", userName, "doesn't mind");
            
            removeClient(ID);
        } 
        
        else if(msg.equals("/help") ){
            for (int i = 0; i < clientCount; i++)
                if(clients[i].getUserName().equals(userName) )
                    clients[i].sendMsg("INFO", userName, helpMessage() );
            
        }
        
        else if(msg.equals("/IPs")){
            String userList = "";
            
            for (int i = 0; i < clientCount; i++){
                if(i != clientCount -1){
                    userList += "   " + clients[i].getUserName() + " (" +clients[i].getUserIP()+ ")\n";
                } else{
                    userList += "   " + clients[i].getUserName() + " (" +clients[i].getUserIP()+ ")";
                }
            }
            
            for (int i = 0; i < clientCount; i++)
                if(clients[i].getUserName().equals(userName) )
                    clients[i].sendMsg("INFO", userName, userList );
        }
        
        else if(msg.startsWith("/send") ){
            //If someone puts /send but no username, try-catch:
            try{
                String target = msg.split("/send")[1];
                String targetIP = "";
                String fileName = "";
                
                boolean sent = false;
                for (int i = 0; i < clientCount; i++) {
                    if (clients[i].getUserName().equals(target)) {
                        targetIP = clients[i].getUserIP();
                        sent = true;
                    }
                }
                //If there was an user with that name:
                if (sent) {
                    for (int i = 0; i < clientCount; i++) {
                        if (clients[i].getUserName().equals(userName)) {
                            clients[i].sendMsg("FILE", userName, "doesnt mind");
                            FileReceiver receiver = new FileReceiver();
                            fileName = receiver.getFileName();
                        }
                    }
                    for(int i = 0; i < clientCount; i++) {
                        if (clients[i].getUserName().equals(target)) {
                            clients[i].sendMsg("FILE", userName, "doesn't mind");
                            FileSender sender = new FileSender(targetIP, fileName );
                            
                        }
                    }
                } else {
                    for (int i = 0; i < clientCount; i++) {
                        if (clients[i].getUserName().equals(userName)) {
                            clients[i].sendMsg("INFO", userName, "   User "
                                    + target + " was not found.");
                        }
                    }
                }
                
            } catch(ArrayIndexOutOfBoundsException ex){
                for(int i = 0; i < clientCount; i++){
                    if(clients[i].getUserName().equals(userName))
                        clients[i].sendMsg("INFO", userName, "   You must write"
                                + " who you want to send it to (/sendAnon)");
                    
                }
            }
        }
        
        else if(msg.equals("/trivial")){
            fileTransfer.FileSenderHandler f = new FileSenderHandler("trivial.txt");
        }
        
        else if(msg.equals("/users") ){
            String userList = "";
            
            for (int i = 0; i < clientCount; i++){
                if(i != clientCount-1){
                    userList += "   " + clients[i].getUserName() + "\n";
                } else{
                    userList += "   " + clients[i].getUserName();
                }
            }
            
            for (int i = 0; i < clientCount; i++)
                if(clients[i].getUserName().equals(userName) )
                    clients[i].sendMsg("INFO", userName, userList );
            
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
                    clients[i].sendMsg("FIN", userToKick, "doesn't mind");
            }
            else{
                for (int i = 0; i < clientCount; i++){
                    if(clients[i].getUserName().equals(userName) )
                        clients[i].sendMsg("INFO", userName, "User "
                                +userToKick+ " wasn't found." );
                }
            }
        }
        
        //Normal messages:
        else {
            for (int i = 0; i < clientCount; i++) {
                clients[i].sendMsg("CHAT", userName, msg);
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
            System.out.print("New client added. ");
            
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
                  "    /fin - Ends your current session.\n"
                + "    /help - This one\n"
                + "    /IPs - Prints a list of users and their IPs\n"
                + "    /sendUser - Send a file to the specified user.\n"
                + "    /users - Prints a list of online users";
    }
}


