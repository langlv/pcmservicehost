package com.rdsic.pcm.data.entity;
// Generated Jul 9, 2016 9:47:03 AM by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * Usertoken generated by hbm2java
 */
public class Usertoken  implements java.io.Serializable {


     private String tokenid;
     private String userid;
     private Date starttime;
     private Date lastactive;

    public Usertoken() {
    }

	
    public Usertoken(String tokenid) {
        this.tokenid = tokenid;
    }
    public Usertoken(String tokenid, String userid, Date starttime, Date lastactive) {
       this.tokenid = tokenid;
       this.userid = userid;
       this.starttime = starttime;
       this.lastactive = lastactive;
    }
   
    public String getTokenid() {
        return this.tokenid;
    }
    
    public void setTokenid(String tokenid) {
        this.tokenid = tokenid;
    }
    public String getUserid() {
        return this.userid;
    }
    
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public Date getStarttime() {
        return this.starttime;
    }
    
    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }
    public Date getLastactive() {
        return this.lastactive;
    }
    
    public void setLastactive(Date lastactive) {
        this.lastactive = lastactive;
    }




}


