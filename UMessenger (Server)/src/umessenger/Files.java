package umessenger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Files {    
    
    private File f = null;
    private FileReader fr;
    private BufferedReader br;
    
    private FileWriter fw;
    private BufferedWriter bw;
    private PrintWriter pw;
    
    private String fileName;
    private int nOfLines;
    
    //CONSTRUCTOR///////////////////////////////////////////////////////////////
//    public Files(){
//        
//        final JFileChooser fc = new JFileChooser();
//        fc.showOpenDialog(null);
//
//        f = fc.getSelectedFile();
//        fileName = f.getName();
//    }
    
    public Files(String fileName){
        
        this.fileName = fileName;
        
        f = new File(fileName);
    }
    
    //METHODS///////////////////////////////////////////////////////////////////
    public void openReadMode(){
        
        try {
            fr = new FileReader(fileName);
            br = new BufferedReader(fr);
            
        } catch (IOException ex) {
            System.out.println("ERROR OPENING FILE IN READING MODE.\n" + ex);
        }
    }
    public void closeReadMode(){
        
        try {
            if(br != null)
                br.close();
            if(fr != null)
                fr.close();
            
        } catch (IOException ex) {
            System.out.println("ERROR CLOSING FILE IN READING MODE.\n" + ex);
        }
    }
    private void openWriteMode(){
        
        try {
            fw = new FileWriter(fileName, true);
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);
            
        } catch (IOException ex) {
            System.out.println("ERROR OPENING FILE IN WRITE MODE.\n" + ex);
        }
    }
    private void closeWriteMode(){
        
        try {
            if(pw != null)
                pw.close();
            if(bw != null)
                bw.close();
            if(fw != null)
                fw.close();
            
        } catch (IOException ex) {
            System.out.println("ERROR CLOSING FILE IN WRITE MODE.\n" + ex);
        }
    }
    
//******************************************************************************
    public String getFileName(){
        return fileName;
    }
    
    public void addToList(String name){
        
        openWriteMode();
        pw.write(name);
        closeWriteMode();
    }
    
    private void countLines(){
        
        openReadMode();
        try {
            while( br.readLine() != null)
                nOfLines++;
            
        } catch (IOException ex) {
            System.out.println("ERROR CONTANDO LINEAS:|\n" + ex);
        }
        
        closeReadMode();
    }
    
    public String readLine(){
        
        String s;
        
        try {
            s = br.readLine();
            
        } catch (IOException ex) {
            System.out.println("ERROR LEYENDO LINEA:\n" + ex);
            s = null;
        }
        
        return s;
    }
}

