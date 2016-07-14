package uMessenger;

import fileTransfer.*;
import java.net.*;  
import java.io.*;
import java.util.Scanner;

public final class ChatClient implements Runnable{

    private Socket socket = null;
    private Thread thread = null;
    private ChatClientThread client = null;
    
    private DataOutputStream dos = null;
    private static Scanner keyboard = new Scanner(System.in);
    
    private String userName = "";
    
    public static final String C_RST = "\u001B[0m";
    public static final String C_BLACK = "\u001B[30m";
    public static final String C_RED = "\u001B[31m";
    public static final String C_GREEN = "\u001B[32m";
    public static final String C_YELLOW = "\u001B[33m";
    public static final String C_BLUE = "\u001B[34m";
    public static final String C_PURPLE = "\u001B[35m";
    public static final String C_CYAN = "\u001B[36m";
    public static final String C_WHITE = "\u001B[37m";
    
//////CONSTRUCTOR///////////////////////////////////////////////////////////////
    public ChatClient(String dir, int port) {
        
        System.out.print("Choose an user name: " + C_RST);
        userName = keyboard.nextLine();
        
        System.out.print(C_GREEN + "Connecting, please wait...");
        while(socket == null){
            try {
                socket = new Socket(dir, port);
                System.out.println("\nConnected to: " 
                        + socket.getInetAddress().getHostAddress() + C_RST );
                start();

            } catch (IOException ex) {
                System.out.print(".");
            }
        }
        
        System.out.println(C_GREEN + welcomeMessage() );
    }
    
    //MAIN//////////////////////////////////////////////////////////////////////
    public static void main(String args[]) {
        ChatClient client = null;
        
        System.out.print(C_GREEN + "(If you are the server, put your IP, not 'localhost')\n" + C_RST);
        System.out.print(C_GREEN + "Insert the IP of the server: " + C_RST);
        String dir = keyboard.nextLine();
        
        client = new ChatClient(dir, 5000);
    }
    
//////METHODS///////////////////////////////////////////////////////////////////
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
                + "Welcome to UMessenger Beta 1.1 (by Ismael Estalayo).\n"
                + "\n"
                + "This is a small messenger-like app made with Sockets in Java designed to\n"
                + "run in the command line and on the same LAN (or using NAT on a server). \n"
                + "Use " + C_YELLOW+"/help"+C_GREEN+" for available commands and "
                + C_YELLOW+"/fin"+C_GREEN+" for ending the session.\n"
                + "\n"
                + "Full info and suggestions at www.GitHub.com/IsmaelEstalayo\n";
    }
    
    public void start() throws IOException {
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
                dos.writeUTF( keyboard.nextLine() );   //Read from keyboard
                dos.flush();
                
            } catch (IOException ex) {
                System.out.println("Sending error: " + ex);
            }
        }
    }

    public void chatting(Object obj) {
        
        String type = obj.getType();
        String user = obj.getUser();
        String msg = obj.getMsg();
        
        switch (type) {
            case "CHAT":
                //Only print the messages from others
                if(!user.equals(userName) ){
                    System.out.print(C_CYAN + user + ": " + C_RST);
                    System.out.println(msg);
                }
                else{
                    //System.out.println("\u221a");
                    //Windows cmd doesnt print this character correctly
                }
                break;
                
            case "INFO":
                //Only print it to the user that did the request
                if(user.equals(userName) )
                    System.out.println(C_GREEN + msg + C_RST);
                
                break;
              
            case "FIN":
                //For the user that requested it, end it's session
                if(user.equals(userName) ){
                    System.out.println("_________________________________________");
                    System.out.print("Good bye. Press RETURN to exit...");
                    closeAll();
                }
                //Rest of users, message of dissconect
                else{
                    System.out.println(C_RED + "User " +user+ " dissconected." + C_RST);
                }
                break;
            
            case "FILE":
                if(user.equals(userName) ){
                    System.out.println(C_GREEN + "Opening subprocess for sending a file..");
                    FileSender sender = new FileSender(msg);
                    System.out.print(C_RST);
                    
                } else{
                    System.out.println(C_GREEN + "Opening subprocess for receiving a file..");
                    FileReceiver receiver = new FileReceiver();
                    System.out.print(C_RST);
                    
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
}