package umessenger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.FileNotFoundException;
import javax.swing.JFileChooser;

public class Files {    
    
    private FileInputStream fis;
    private BufferedInputStream bis;
    private DataInputStream dis;
    
    private FileOutputStream fos;
    private BufferedOutputStream bos;
    private DataOutputStream dos;
    
    private File f = null;
    private long dim;
    private String fileName;
    
    //CONSTRUCTOR///////////////////////////////////////////////////////////////
    public Files(){
        
        final JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(null);

        f = fc.getSelectedFile();
        fileName = f.getName();
    }
    
    public Files(String fileName){
        this.fileName = fileName;
    }
    
    //METHODS///////////////////////////////////////////////////////////////////
    private void openReadMode(){
        
        try {
            fis = new FileInputStream(f.getAbsoluteFile() );
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            
        } catch (FileNotFoundException ex) {
            System.out.println("ERROR OPENING FILE IN READING MODE.\n" + ex);
        }
    }
    private void closeReadMode(){
        
        try {
            if(dis != null)
                dis.close();
            if(bis != null)
                bis.close();
            if(fis != null)
                fis.close();
            
        } catch (Exception ex) {
            System.out.println("ERROR CLOSING FILE IN READING MODE.\n" + ex);
        }
    }
    private void openWriteMode(){
        
        try {
            fos = new FileOutputStream(fileName );
            bos = new BufferedOutputStream(fos);
            dos = new DataOutputStream(bos);
            
        } catch (FileNotFoundException ex) {
            System.out.println("ERROR OPENING FILE IN WRITE MODE.\n" + ex);
        }
    }
    private void closeWriteMode(){
        
        try {
            if(dos != null)
                dos.close();
            if(bos != null)
                bos.close();
            if(fos != null)
                fos.close();
            
        } catch (Exception ex) {
            System.out.println("ERROR CLOSING FILE IN WRITE MODE.\n" + ex);
        }
    }
    
//******************************************************************************
    public String getFileName(){
        return fileName;
    }
    
    public long getDimension(){
        
        openReadMode();
        try {
            dim = dis.available();
            
        } catch (Exception ex) {
            System.out.println("ERROR GETTING FILE DIMENSION.\n" + ex);
        }
        
        closeReadMode();
        return dim;
    }
    
    public Array fileToArray(){
        
        long dimArray = getDimension();
        
        openReadMode();
        Array a = new Array(dimArray);
        
        try {
            dis.read( a.returnArray() );
            
        } catch (IOException ex) {
            System.out.println("ERROR PASSING FILE TO ARRAY.\n" + ex);
        }
        
        closeReadMode();
        return a;
    }
    
    public void arrayToFile(Array a){
        
        openWriteMode();
        System.out.println("Saving file " + fileName + "...");
        
        try {
            dos.write( a.returnArray() );
            
        } catch (IOException ex) {
            System.out.println("ERROR PASSING ARRAY TO FILE.\n" + ex);
        }
        
        closeWriteMode();
    }
}

