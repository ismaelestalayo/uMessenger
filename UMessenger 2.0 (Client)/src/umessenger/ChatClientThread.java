package umessenger;

import java.net.*;
import java.io.*;

public final class ChatClientThread extends Thread{
    private Socket socket = null;
    private ChatClient client = null;
    
    private DataInputStream dis = null;
    
    //CONSTRUCTOR///////////////////////////////////////////////////////////////
    public ChatClientThread(ChatClient cc, Socket ss) {
        client = cc;
        socket = ss;
        open();
        start();
    }
    
    //METHODS///////////////////////////////////////////////////////////////////
    public void open() {
        try {
            dis = new DataInputStream(socket.getInputStream() );
            
        } catch (IOException ioe) {
            System.out.println("Error getting input stream: " + ioe);
            client.stop();
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
        while (true) {
            try {
                client.handle(dis.readUTF() );
                
            } catch (IOException ex) {
                System.out.println("Listening error: " + ex);
                client.stop();
            }
        }
    }
}
