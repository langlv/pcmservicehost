package com.rdsic.pcm.data.entity;
// Generated Jun 20, 2016 9:33:45 AM by Hibernate Tools 4.3.1



/**
 * Userclass generated by hbm2java
 */
public class Userclass  implements java.io.Serializable {


     private String classid;
     private String name;
     private String status;
     private Integer failcount;

    public Userclass() {
    }

	
    public Userclass(String classid) {
        this.classid = classid;
    }
    public Userclass(String classid, String name, String status, Integer failcount) {
       this.classid = classid;
       this.name = name;
       this.status = status;
       this.failcount = failcount;
    }
   
    public String getClassid() {
        return this.classid;
    }
    
    public void setClassid(String classid) {
        this.classid = classid;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    public Integer getFailcount() {
        return this.failcount;
    }
    
    public void setFailcount(Integer failcount) {
        this.failcount = failcount;
    }




}


