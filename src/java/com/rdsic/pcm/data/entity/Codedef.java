package com.rdsic.pcm.data.entity;
// Generated Jul 19, 2016 4:32:10 PM by Hibernate Tools 4.3.1



/**
 * Codedef generated by hbm2java
 */
public class Codedef  implements java.io.Serializable {


     private CodedefId id;
     private String text;

    public Codedef() {
    }

    public Codedef(CodedefId id, String text) {
       this.id = id;
       this.text = text;
    }
   
    public CodedefId getId() {
        return this.id;
    }
    
    public void setId(CodedefId id) {
        this.id = id;
    }
    public String getText() {
        return this.text;
    }
    
    public void setText(String text) {
        this.text = text;
    }




}


