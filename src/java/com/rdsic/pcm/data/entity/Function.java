package com.rdsic.pcm.data.entity;
// Generated Jul 21, 2016 12:09:09 PM by Hibernate Tools 4.3.1



/**
 * Function generated by hbm2java
 */
public class Function  implements java.io.Serializable {


     private Integer fid;
     private String address;
     private String type;
     private String operation;

    public Function() {
    }

    public Function(String address, String type, String operation) {
       this.address = address;
       this.type = type;
       this.operation = operation;
    }
   
    public Integer getFid() {
        return this.fid;
    }
    
    public void setFid(Integer fid) {
        this.fid = fid;
    }
    public String getAddress() {
        return this.address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    public String getOperation() {
        return this.operation;
    }
    
    public void setOperation(String operation) {
        this.operation = operation;
    }




}


