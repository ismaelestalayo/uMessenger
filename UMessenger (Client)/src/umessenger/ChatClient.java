package umessenger;

import java.net.*;  
import java.io.*;
import java.util.Scanner;

public final class ChatClient implements Runnable{

    private Socket socket = null;
    private Thread thread = null;
    private ChatClientThread client = null;
    private DataInputStream dis = null;
    private DataInputStream input = null;
    private DataOutputStream dos = null;
    
    public static final String C_RST = "\u001B[0m";
    public static final String C_BLACK = "\u001B[30m";
    public static final String C_RED = "\u001B[31m";
    public static final String C_GREEN = "\u001B[32m";
    public static final String C_YELLOW = "\u001B[33m";
    public static final String C_BLUE = "\u001B[34m";
    public static final String C_PURPLE = "\u001B[35m";
    public static final String C_CYAN = "\u001B[36m";
    public static final String C_WHITE = "\u001B[37m";
    
    private static Scanner keyboard = null;
    private String userName = "";
    
    //CONSTRUCTOR///////////////////////////////////////////////////////////////
    public ChatClient(String dir, int port) {
        
        System.out.print("Choose an user name: " + C_RST);
        keyboard = new Scanner(System.in);
        userName = keyboard.nextLine();
        
        System.out.print(C_GREEN + "Connecting, please wait...");
        do{
            try {
                socket = new Socket(dir, port);
                System.out.println("\nConnected to: " 
                        + socket.getInetAddress().getHostAddress() + C_RST );
                start();

            } catch (IOException ex) {
                System.out.print(".");
            }
        } while (socket == null);
        
        System.out.println(C_GREEN + welcomeMessage() );
    }
    
    //MAIN//////////////////////////////////////////////////////////////////////
    public static void main(String args[]) {
        ChatClient client = null;
        System.out.print(C_GREEN + "Insert the IP of the server: " + C_RST);
        keyboard = new Scanner(System.in);
        String dir = keyboard.nextLine();
        client = new ChatClient(dir, 1234);
    }
    
    //METHODS///////////////////////////////////////////////////////////////////
    private String welcomeMessage(){
        return "\n"
                + " _   ____  ___                                         \n"
                + "| | | |  \\/  |                                         \n"
                + "| | | | .  . | ___  ___ ___  ___ _ __   __ _  ___ _ __ \n"
                + "| | | | |\\/| |/ _ \\/ __/ __|/ _ \\ '_ \\ / _` |/ _ \\ '__|\n"
                + "| |_| | |  | |  __/\\__ \\__ \\  __/ | | | (_| |  __/ |   \n"
                + " \\___/\\_|  |_/\\___||___/___/\\___|_| |_|\\__, |\\___|_|   \n"
                + "                                        __/ |          \n"
                + "                                       |___/           \n"
                + "Welcome to UMessenger Alpha 0.2 (by Ismael Estalayo).\n\n"
                + "This is a messenger app made with Sockets in Java designed to run\n"
                + "in the command line and on the same LAN. Use "
                + C_YELLOW+"/help"+C_GREEN+" for available\n"
                + "commands and "+ C_YELLOW+"/fin"+C_GREEN+" for ending the session.\n\n"
                + "More info and suggestions at www.GitHub.com/IsmaelEstalayo\n";
    }
    
    public void start() throws IOException {
        input = new DataInputStream(System.in);
        dis = new DataInputStream(socket.getInputStream() );
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
                dos.writeUTF( input.readLine() );   //Read from keyboard
                dos.flush();
                
            } catch (IOException ex) {
                System.out.println("Sending error: " + ex);
            }
        }
    }

    public void chatting(String X) {
        String type = X.split("º")[0];
        String user = X.split("º")[1];
        String msg = X.split("º")[2];
        
        switch (type) {
            case "CHAT":
                //Only print the messages from others
                if(!user.equals(userName) ){
                    System.out.print(C_CYAN + user + ": " + C_RST);
                    System.out.println(msg);
                }
                break;
                
            case "INFO":
                //Only print it to the user that did the request
                if(user.equals(userName) )
                    System.out.println(C_GREEN + msg + C_RST);
                
                break;
              
            case "FIN":
                if(user.equals(userName) ){
                    System.out.println("_________________________________________");
                    System.out.print("Good bye. Press RETURN to exit...");
                    closeAll();
                }
                else{
                    System.out.println(C_RED + ">User " +user+ 
                            " dissconected." + C_RST);
                }
                break;
                
            case "FILE":
                if(user.equals(userName) ){
                    Files f = new Files();
                    String aux = f.getFileName();
                    System.out.println(C_GREEN+"File to send: " + aux + C_RST);
                    sendString(aux);
                    sendString("SEND");
                    
                    sendArray(userName, f.fileToArray(), 200);
                    System.out.println(C_GREEN + "File sent!" + C_RST);
                }
                else{
                    String zz = receiveString().split("º")[2];
                    System.out.println(C_GREEN+"File to receive: " + zz + C_RST);
                    
                    Files f = new Files(zz);
//                    f.arrayToFile(receiveArray() );
                }
                break;
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
            if (input != null)
                input.close();
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
    public void sendUserName(){
        try {
            dos.writeUTF(userName);
            
        } catch (IOException ex) {
            System.out.println("Error sending UTF:\n" + ex);
        }
    }
    public void sendString(String x){
        try {
            dos.writeUTF(x);
            
        } catch (IOException ex) {
            System.out.println("Error sending String:\n" + ex);
        }
    }
    public String receiveString(){
        String x = "X";
        try {
            x = dis.readUTF();
            
        } catch (IOException ex) {
            System.out.println("ERROR reading String:\n" + ex);
        }
        return x;
    }
    public void sendArray(String user, Array a, long segment){
        System.out.println("> Preparing to send the file.");
        long dim = a.getDimension();
        System.out.println("> Size of the file: " + dim);
        sendLong(dim);
        System.out.println("> Size of the segments: " + segment);
        sendLong(segment);
        
        /*All the file except last segment 
        (Because it may be smaller than segment size)*/
        int i = 0;
        while(i < (dim/segment)){
            
            try {
                dos.write( a.returnArray(), i*(int)segment, (int)segment);
                
            } catch (IOException ex) {
                System.out.println("> ERROR SENDING ARRAY IN SEGMENTS");
                System.out.println(ex);
            }
            
            System.out.println("    [" +(i*segment)+ "/" +dim+ "]" );
            i++;
        }
        
        try {
            dos.write( a.returnArray(), i*(int)segment, (int)(dim%segment) );
            System.out.println("    [" +(i*segment)+ "/" +dim+ "]" );
            
        } catch (IOException ex) {
            System.out.println("> ERROR SENDING LAST SEGMENT\n" + ex);
        }
        System.out.println("    [" +(dim)+ "/" +dim+ "]" );
    }
    private void sendLong(long x){
        try{
            dos.writeLong(x);
            
        } catch(IOException ex){
            System.out.println("ERROR SENDING LONG.\n" + ex);
        }
    }
}
