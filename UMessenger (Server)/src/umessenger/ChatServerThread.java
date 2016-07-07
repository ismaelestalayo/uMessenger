package umessenger;

import java.net.*;
import java.io.*;

public class ChatServerThread extends Thread {

    private ChatServer server = null;
    private Socket socket = null;
    public DataInputStream dis = null;
    private DataOutputStream dos = null;
        
    private int ID = -1;
    private String userName = "";
    
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
    public ChatServerThread(ChatServer s, Socket ss) {
        super();
        server = s;
        socket = ss;
        ID = socket.getPort();
    }
    
    //METHODS///////////////////////////////////////////////////////////////////
    @Override
    public void run() {
        System.out.println("Server Thread " + ID + " running.");
        
        readUserName();
        checkNewUser();
        
        while (true) {
            try {
                server.broadcast(userName, ID, dis.readUTF() );
                
            } catch (IOException ex) {
                System.out.println(C_RED + "ERROR WITH CLIENT " + ID +": " + ex + C_RST);
                server.removeClient(ID);
                stop();
            }
        }
    }
    
    public void sendMsg(String X) {
        try {
            dos.writeUTF(X);
            dos.flush();
            
        } catch (IOException ex) {
            System.out.println(ID + "ERROR sending: " + ex);
            server.removeClient(ID);
            stop();
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
        Files f = new Files("userList.txt");
        
        f.openReadMode();
        String line = null;
        while( (line = f.readLine()) != null){
            if( line.equalsIgnoreCase(userName) )
                newUser = false;
        }
        
        f.closeReadMode();
        
        if(newUser){
            f.addToList(userName + "\n");
            sendMsg("INFOº" + userName + "ºWelcome to UMessenger, new user " + userName + "!");
        }
            
        else{
            sendMsg("INFOº" + userName + "ºWelcome back, " + userName + "!");
        }
    }
    public int getID() {
        return ID;
    }
    public String getUserName(){
        return userName;
    }
    public void openStreams() throws IOException {
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
    
    //**************************************************************************
//    private void sendLong(long x){
//        try{
//            dos.writeLong(x);
//            
//        } catch(IOException ex){
//            System.out.println("ERROR SENDING LONG.\n" + ex);
//        }
//    }
//    private long receiveLong(){
//        long x = 0;
//        
//        try {
//            x = dis.readLong();
//            
//        } catch (IOException ex) {
//            System.out.println("ERROR RECEVING LONG.\n" + ex);
//        }
//        
//        return x;
//    }
//    public Array receiveArray(){
//        System.out.println("> Preparing to receive array.");
//        long dim = receiveLong();
//        System.out.println("> Size of the file: " + dim);
//        long segments = receiveLong();
//        System.out.println("> Size of the segments: " + segments);
//        
//        int i = 0;
//        Array b = new Array(dim);
//        while( i < (dim/segments)){
//            
//            try {
//                dis.read( b.returnArray(), i*(int)segments, (int)segments);
//                
//            } catch (IOException ex) {
//                System.out.println("> ERROR RECIBIENDO ARRAY EN SEGMENTOS");
//                System.out.println(ex);
//            }
//            
//            System.out.println("    [" +(i*segments)+ "/" +dim+ "]" );
//            i++;
//        }
//        
//        try {
//            dis.read( b.returnArray(), i*(int)segments, (int)(dim%segments) );
//            System.out.println("    [" +(i*segments)+ "/" +dim+ "]" );
//            
//        } catch (IOException ex) {
//            System.out.println("> ERROR RECIBIENDO EL ULTIMO SEGMENTO\n" + ex);
//        }
//        System.out.println("    [" +(dim)+ "/" +dim+ "]" );
//        
//        return b;
//    }
//    public void sendArray(String user, Array a, long segment){
//        System.out.println("> Preparing to send the file from " + user);
//        long dim = a.getDimension();
//        System.out.println("> Size of the file: " + dim);
//        sendLong(dim);
//        System.out.println("> Size of the segments: " + segment);
//        sendLong(segment);
//        
//        /*All the file except last segment 
//        (Because it may be smaller than segment size)*/
//        int i = 0;
//        while(i < (dim/segment)){
//            
//            try {
//                dos.write( a.returnArray(), i*(int)segment, (int)segment);
//                
//            } catch (IOException ex) {
//                System.out.println("> ERROR SENDING ARRAY IN SEGMENTS");
//                System.out.println(ex);
//            }
//            
//            System.out.println("    [" +(i*segment)+ "/" +dim+ "]" );
//            i++;
//        }
//        
//        try {
//            dos.write( a.returnArray(), i*(int)segment, (int)(dim%segment) );
//            System.out.println("    [" +(i*segment)+ "/" +dim+ "]" );
//            
//        } catch (IOException ex) {
//            System.out.println("> ERROR SENDING LAST SEGMENT\n" + ex);
//        }
//        System.out.println("    [" +(dim)+ "/" +dim+ "]" );
//    }
}
