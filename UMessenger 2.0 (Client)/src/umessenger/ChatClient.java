package umessenger;

import java.net.*;  
import java.io.*;
import java.util.Scanner;

public final class ChatClient implements Runnable{

    private Socket socket = null;
    private Thread thread = null;
    private ChatClientThread client = null;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    
    private Scanner keyboard = null;
    
    //CONSTRUCTOR///////////////////////////////////////////////////////////////
    public ChatClient(String dir, int port) {
        
        System.out.print("Choose an user name: ");
        //keyboard = new Scanner(System.in);
        //userName = keyboard.nextLine();
        
        System.out.print("Connecting, please wait...");
        do{
            try {
                socket = new Socket(dir, port);
                System.out.println("\nConnected to: " + socket.getInetAddress().getHostAddress() );
                start();

            } catch (IOException ex) {
                System.out.print(".");
            }
        } while (socket == null);
    }
    
    //MAIN//////////////////////////////////////////////////////////////////////
    public static void main(String args[]) {
        ChatClient client = null;        
        client = new ChatClient("localhost", 1234);
    }
    
    //METHODS///////////////////////////////////////////////////////////////////
    public void start() throws IOException {
        dis = new DataInputStream(System.in);
        dos = new DataOutputStream(socket.getOutputStream() );
        
        if (thread == null) {
            client = new ChatClientThread(this, socket);
            thread = new Thread(this);
            thread.start();
        }
    }
    
    @Override
    public void run() {
        while (thread != null) {
            try {
                dos.writeUTF( dis.readLine() );
                dos.flush();
            } catch (IOException ex) {
                System.out.println("Sending error: " + ex);
            }
        }
    }

    public void chatting(String msg) {
        if (msg.equalsIgnoreCase("fin")) {
            System.out.print("Good bye. Press RETURN to exit...");
            closeAll();
            
        } else {
            if(msg.startsWith(Integer.toString(client.userNumber)) ){
                //nothing
            }
            else{
                System.out.println("    >" + msg);
            }
        }
    }

    public void closeAll() {
        if (thread != null) {
            thread.stop();
            thread = null;
        }
        try {
            if (dis != null)
                dis.close();
            if (dos != null)
                dos.close();
            if (socket != null)
                socket.close();
            
        } catch (IOException ex) {
            System.out.println("Error closing: " + ex);
        }
        client.close();
        client.stop();
    }
}
