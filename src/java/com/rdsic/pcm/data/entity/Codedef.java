package com.rdsic.pcm.data.entity;
// Generated Jun 14, 2016 9:17:57 AM by Hibernate Tools 4.3.1



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


