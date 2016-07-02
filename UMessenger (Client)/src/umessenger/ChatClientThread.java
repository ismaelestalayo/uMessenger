package umessenger;

import java.net.*;
import java.io.*;

public final class ChatClientThread extends Thread{
    private Socket socket = null;
    private ChatClient client = null;
    
    private DataInputStream dis = null;
    public int userNumber;
    
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
        try {
            userNumber = dis.readInt();
        } catch (IOException ex) {
            System.out.println("ERROR READING USER NUMBER:\n" + ex);
        }
        
        while (true) {
            try {
                client.chatting(dis.readUTF() );
                
            } catch (IOException ex) {
                System.out.println("Listening error: " + ex);
                client.closeAll();
            }
        }
    }
    
}
