package com.rdsic.pcm.data.entity;
// Generated Jul 9, 2016 9:47:03 AM by Hibernate Tools 4.3.1



/**
 * Projectasset generated by hbm2java
 */
public class Projectasset  implements java.io.Serializable {


     private int prid;
     private String silo;
     private String emp;
     private String mp;
     private String dm;
     private String user;

    public Projectasset() {
    }

	
    public Projectasset(int prid) {
        this.prid = prid;
    }
    public Projectasset(int prid, String silo, String emp, String mp, String dm, String user) {
       this.prid = prid;
       this.silo = silo;
       this.emp = emp;
       this.mp = mp;
       this.dm = dm;
       this.user = user;
    }
   
    public int getPrid() {
        return this.prid;
    }
    
    public void setPrid(int prid) {
        this.prid = prid;
    }
    public String getSilo() {
        return this.silo;
    }
    
    public void setSilo(String silo) {
        this.silo = silo;
    }
    public String getEmp() {
        return this.emp;
    }
    
    public void setEmp(String emp) {
        this.emp = emp;
    }
    public String getMp() {
        return this.mp;
    }
    
    public void setMp(String mp) {
        this.mp = mp;
    }
    public String getDm() {
        return this.dm;
    }
    
    public void setDm(String dm) {
        this.dm = dm;
    }
    public String getUser() {
        return this.user;
    }
    
    public void setUser(String user) {
        this.user = user;
    }




}


