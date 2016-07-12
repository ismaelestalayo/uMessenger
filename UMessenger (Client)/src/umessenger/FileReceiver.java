package umessenger;

public class FileReceiver {
    
    public FileReceiver(){
        main(null);
    }
    
    public static void main(String[] args){
        FilesConnection ms = new FilesConnection(5077);
        
        String fileName = ms.receiveString();
        if(fileName.equals("NULL") ){
            System.out.println("  >Sender didnt select a file.");
            System.out.println("  >Exiting...");
            
        } else{
            System.out.println("  >File to receive: " + fileName);
            FilesArray receivedArray = ms.receiveArray();

            FileReceiverHandler binario = new FileReceiverHandler(fileName);
            binario.dumpArrayToFile(receivedArray);
        }
        ms.closeAllSockets();
    }
    
}
