package uMessenger;

import java.io.Serializable;

public class Object implements Serializable{
    
    private static final long serialVersionUID = 5950169519310163575L;
    private String type;
    private String user;
    private String msg;
    
    public Object(String type, String user, String msg){
        this.type = type;
        this.user = user;
        this.msg = msg;
    }

    public String getType(){
        return type;
    }
    public String getUser(){
        return user;
    }
    public String getMsg(){
        return msg;
    }
    
}
