package uMessenger;

import java.net.*;
import java.io.*;

public class ChatServerThread extends Thread {

    private ChatServer server = null;
    private Socket socket = null;
    private DataInputStream dis = null;
    private ObjectOutputStream oos = null;
        
    private int ID = -1;
    private String userName = "";
    
    private final String C_RST = "\u001B[0m";
    private final String C_BLUE = "\u001B[34m";
    private final String C_CYAN = "\u001B[36m";
    private final String C_GREEN = "\u001B[32m";
    private final String C_PURPLE = "\u001B[35m";
    private final String C_RED = "\u001B[31m";
    private final String C_YELLOW = "\u001B[33m";
    
    private String[] colors = {C_BLUE, C_CYAN, C_GREEN, C_PURPLE, C_RED, C_YELLOW};
    
//////CONSTRUCTOR///////////////////////////////////////////////////////////////
    public ChatServerThread(ChatServer cs, Socket socket) {
        super();
        this.server = cs;
        this.socket = socket;
        ID = socket.getPort();
    }
    
    //METHODS///////////////////////////////////////////////////////////////////
    @Override
    public void run() {
        readUserName();
        checkNewUser();
        
        System.out.println(C_CYAN + "Added client " + userName + " (" + getUserIP()
                + ") on thread " + ID + C_RST);
        server.broadcast(userName, ID, "/newUser");
        
        while (true) {
            try {
                server.broadcast(userName, ID, dis.readUTF() );
                
            } catch (IOException ex) {
                System.out.println(C_RED + "Client " + getUserName() + " closed the window." + C_RST);
                server.broadcast(userName, ID, "/forcedFin");
            }
        }
    }
    
    public void sendMsg(String type, String user, String msg) {
        try {
            Object obj = new Object(type, user, msg);
            oos.writeObject(obj);
            oos.flush();
            
        } catch (IOException ex) {
            server.broadcast(userName, ID, "/forcedFin");
        }
    }
    private void readUserName(){
        
        try {
            userName = dis.readUTF();
            
        } catch (IOException ex) {
            System.out.println("ERROR reading userName:\n" + ex);
        }
    }
    
    private void checkNewUser(){
        
        boolean newUser = true;
        UserListHandler f = new UserListHandler("userList.txt");
        
        f.openReadMode();
        String line = null;
        while( (line = f.readLine()) != null){
            if( line.equals(userName) )
                newUser = false;
        }
        
        f.closeReadMode();
        
        if(newUser){
            f.addToList(userName + "\n");
            sendMsg("INFO", userName, "Welcome to UMessenger, new user " + userName + "!");
        }
            
        else{
            sendMsg("INFO", userName, "Welcome back, " + userName + "!");
        }
    }
    public int getID() {
        return ID;
    }
    public String getUserName(){
        return userName;
    }
    public String getUserIP(){
        return socket.getInetAddress().getHostAddress();
    }
    public void openStreams() throws IOException {
        dis = new DataInputStream(new BufferedInputStream(socket.getInputStream() ) );
        oos = new ObjectOutputStream(socket.getOutputStream() );
    }
    public void close() throws IOException {
        if (dis != null)
            dis.close();
        if (oos != null)
            oos.close();
        if (socket != null)
            socket.close();
    }

}
