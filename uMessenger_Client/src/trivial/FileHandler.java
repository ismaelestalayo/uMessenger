package trivial;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileHandler {
    
    FileReader fr;
    BufferedReader br;
    
    private String fileName = null;
    private String thisLine = null;
    private int nOfLines = 0;
    private int nOfQuestions = 0;
    private int lim = 1;
    
//////CONSTRUCTOR///////////////////////////////////////////////////////////////
    public FileHandler(String filename){
        
        this.fileName = filename;
        
        getTotalNumberOfLines();
            
        System.out.println("total N of lines: " + nOfLines);
        System.out.println("total N of questions: " + nOfQuestions);

    }
    
//////METHODS///////////////////////////////////////////////////////////////////
    private void openReaders(){
        try {
            fr = new FileReader(fileName);
            br = new BufferedReader(fr);
            
        } catch (FileNotFoundException ex) {
            System.out.println("ERROR OPENING READERS: " + ex);
        }
    }
    private void closeReaders(){
        try {
            if (fr != null)
                fr.close();
            if (br != null)
                br.close();
            
        } catch (IOException ex) {
            System.out.println("   ERROR CLOSING READERS: " + ex);
        }
    }
////////////////////////////////////////////////////////////////////////////////
    private void getTotalNumberOfLines(){
        openReaders();
        
        try {
            while( br.readLine() != null)
                nOfLines++;
            nOfQuestions = nOfLines/2;
            
        } catch (IOException ex) {
            System.out.println("   ERROR GETTING TOTAL NUMBER OF LINES: " + ex);
        }
        
        closeReaders();
    }
    public String getQuestion(){
        try{
            FileReader fr = new FileReader("trivial.txt");
            BufferedReader br = new BufferedReader(fr);                
            for(int i = 0; i < lim; i++)
                thisLine = br.readLine();
            lim++;
            br.close();
        }
        catch(IOException ex){
            System.out.println("ERROR GETTING NEXT QUESTION: " + ex);
        }
        return thisLine;
    }

}
