package umessenger;

import java.net.*;
import java.io.*;

public class ChatServerThread extends Thread {

    private ChatServer server = null;
    private Socket socket = null;
    private int ID = -1;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    
    //CONSTRUCTOR///////////////////////////////////////////////////////////////
    public ChatServerThread(ChatServer s, Socket ss) {
        super();
        server = s;
        socket = ss;
        ID = socket.getPort();
    }
    
    //METHODS///////////////////////////////////////////////////////////////////
    public void sendMsg(String msg) {
        try {
            dos.writeUTF(msg);
            dos.flush();
            
        } catch (IOException ex) {
            System.out.println(ID + "ERROR sending: " + ex);
            server.remove(ID);
            stop();
        }
    }

    public int getID() {
        return ID;
    }

    @Override
    public void run() {
        System.out.println("Server Thread " + ID + " running.");
        while (true) {
            try {
                server.handle("prueba" ,ID, dis.readUTF());
            } catch (IOException ex) {
                System.out.println(ID + " ERROR reading: " + ex);
                server.remove(ID);
                stop();
            }
        }
    }

    public void open() throws IOException {
        dis = new DataInputStream(new BufferedInputStream(socket.getInputStream() ) );
        dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream() ) );
    }

    public void close() throws IOException {
        if (socket != null)
            socket.close();
        if (dis != null)
            dis.close();
        if (dos != null)
            dos.close();
    }
}
