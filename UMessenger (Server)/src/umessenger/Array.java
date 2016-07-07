package umessenger;

public class Array {    
    //ATRIBUTOS
    private byte [] a;
    private long dim;
    
    //CONSTRUCTORES/////////////////////////////////////////////////////////////
    public Array(){
        a = null;
    }
    
    public Array(long dim){
        a = new byte[(int)dim];
        this.dim = dim;
    }
    
    //METODOS///////////////////////////////////////////////////////////////////
    public long getDimension(){
        return dim;
    }
    
    public void addElement(byte b, int pos){
        a[pos] = b;
    }
    
    public byte getElement(int pos){
        return a[pos];
    }
    
    public byte [] returnArray(){
        return a;
    }
    public void createArray(byte[] b){
        a = b;
    }
}
