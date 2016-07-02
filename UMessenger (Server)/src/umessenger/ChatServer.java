package umessenger;

import java.net.*;
import java.io.*;

public class ChatServer implements Runnable {

    private ChatServerThread clients[] = new ChatServerThread[50];
    private ServerSocket server = null;
    private Thread thread = null;
    private int clientCount = 0;
    
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

    public synchronized void broadcast(String userName, int ID, String msg) {
        if (msg.equalsIgnoreCase("fin")) {
            clients[findClient(ID)].sendMsg("FIN");
            removeClient(ID);
        } 
        
        else {
            for (int i = 0; i < clientCount; i++) {
                clients[i].sendMsg(ID+ ": " +msg);
            }
        }
    }

    public synchronized void removeClient(int ID) {
        int pos = findClient(ID);
        if (pos >= 0) {
            ChatServerThread toTerminate = clients[pos];
            System.out.println("Removing client thread " + ID);
            if (pos < clientCount - 1) {
                for (int i = pos + 1; i < clientCount; i++) {
                    clients[i - 1] = clients[i];
                }
            }
            clientCount--;
            try {
                toTerminate.close();
            } catch (IOException ioe) {
                System.out.println("Error closing thread: " + ioe);
            }
            toTerminate.stop();
        }
    }

    private void addThread(Socket socket) {
        if (clientCount < clients.length) {
            System.out.println("Client accepted: " + socket.getInetAddress().getHostAddress() );
            System.out.println("_________________________________________");
            clients[clientCount] = new ChatServerThread(this, socket);
            
            try {
                clients[clientCount].openStreams();
                clients[clientCount].start();
                clientCount++;
                
            } catch (IOException ioe) {
                System.out.println("Error opening thread: " + ioe);
            }
            
        } else {
            System.out.println("Maximum client number (" + clients.length + ") reached.");
        }
    }
    
    
}


