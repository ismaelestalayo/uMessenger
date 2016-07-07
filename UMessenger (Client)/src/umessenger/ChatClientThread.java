package umessenger;

import java.net.*;
import java.io.*;

public final class ChatClientThread extends Thread{
    private Socket socket = null;
    private ChatClient client = null;
    
    private DataInputStream dis = null;
    
    public static final String C_RST = "\u001B[0m";
    public static final String C_BLACK = "\u001B[30m";
    public static final String C_RED = "\u001B[31m";
    
    //CONSTRUCTOR///////////////////////////////////////////////////////////////
    public ChatClientThread(ChatClient cc, Socket ss) {
        client = cc;
        socket = ss;
        
        openStream();
        start();
    }
    
    //METHODS///////////////////////////////////////////////////////////////////
    public void openStream() {
        try {
            dis = new DataInputStream(socket.getInputStream() );
            
        } catch (IOException ioe) {
            System.out.println("Error getting input stream: " + ioe);
            client.closeAll();
        }
    }
    public void close() {
        try {
            if (dis != null)
                dis.close();
            
        } catch (IOException ioe) {
            System.out.println("Error closing input stream: " + ioe);
        }
    }

    @Override
    public void run() {
        client.sendUserName();  //Sends your UserName
        
        while (true) {
            try {
                client.chatting(dis.readUTF() );
                
            } catch (IOException ex) {
                System.out.println(C_RED + "_________________________________________");
                System.out.println("Listeningg error: " + ex + C_RST);
                System.out.println("Press enter to close.");
                client.closeAll();
            }
        }
    }
    
    
}
