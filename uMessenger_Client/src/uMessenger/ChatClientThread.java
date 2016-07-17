/* 
 * Copyright (C) 2016 Ismael Estalayo Mena
                        http://github.com/ismaelestalayo

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uMessenger;

import java.net.*;
import java.io.*;

public final class ChatClientThread extends Thread{
    private Socket socket = null;
    private ChatClient client = null;
    
    private ObjectInputStream ois = null;
    
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
            ois = new ObjectInputStream(socket.getInputStream() );
            
        } catch (IOException ioe) {
            System.out.println("Error getting input stream: " + ioe);
            client.closeAll();
        }
    }
    public void close() {
        try {
            if (ois != null)
                ois.close();
            
        } catch (IOException ioe) {
            System.out.println("Error closing input stream: " + ioe);
        }
    }

    @Override
    public void run() {
        client.sendUserName();  //Self explanatory
        
        while (true) {
            try {
                Object obj = (Object)ois.readObject();
                client.chatting(obj);
                
            } catch (IOException ex) {
                System.out.println(C_RED + "_________________________________________");
                System.out.println("Lost connection with the server: " + ex + C_RST);
                System.out.println("Press enter to close.");
                client.closeAll();
            } catch (ClassNotFoundException ex) {
                System.out.println(C_RED + "_________________________________________");
                System.out.println("Lost connection with the server: " + ex + C_RST);
                System.out.println("Press enter to close.");
                client.closeAll();
            }
        }
    } 
}

