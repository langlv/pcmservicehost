package com.rdsic.pcm.data.entity;
// Generated Jul 18, 2016 2:28:00 PM by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * User generated by hbm2java
 */
public class User  implements java.io.Serializable {


     private String userid;
     private String firstname;
     private String lastname;
     private String password;
     private Date datecreate;
     private Date dateexpire;
     private String userclass;
     private Date lastlogin;
     private int loginfailcount;
     private Date lastactive;
     private String token;
     private String status;
     private Integer eid;

    public User() {
    }

	
    public User(String userid, String password, String userclass, int loginfailcount, String status) {
        this.userid = userid;
        this.password = password;
        this.userclass = userclass;
        this.loginfailcount = loginfailcount;
        this.status = status;
    }
    public User(String userid, String firstname, String lastname, String password, Date datecreate, Date dateexpire, String userclass, Date lastlogin, int loginfailcount, Date lastactive, String token, String status, Integer eid) {
       this.userid = userid;
       this.firstname = firstname;
       this.lastname = lastname;
       this.password = password;
       this.datecreate = datecreate;
       this.dateexpire = dateexpire;
       this.userclass = userclass;
       this.lastlogin = lastlogin;
       this.loginfailcount = loginfailcount;
       this.lastactive = lastactive;
       this.token = token;
       this.status = status;
       this.eid = eid;
    }
   
    public String getUserid() {
        return this.userid;
    }
    
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getFirstname() {
        return this.firstname;
    }
    
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public String getLastname() {
        return this.lastname;
    }
    
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    public Date getDatecreate() {
        return this.datecreate;
    }
    
    public void setDatecreate(Date datecreate) {
        this.datecreate = datecreate;
    }
    public Date getDateexpire() {
        return this.dateexpire;
    }
    
    public void setDateexpire(Date dateexpire) {
        this.dateexpire = dateexpire;
    }
    public String getUserclass() {
        return this.userclass;
    }
    
    public void setUserclass(String userclass) {
        this.userclass = userclass;
    }
    public Date getLastlogin() {
        return this.lastlogin;
    }
    
    public void setLastlogin(Date lastlogin) {
        this.lastlogin = lastlogin;
    }
    public int getLoginfailcount() {
        return this.loginfailcount;
    }
    
    public void setLoginfailcount(int loginfailcount) {
        this.loginfailcount = loginfailcount;
    }
    public Date getLastactive() {
        return this.lastactive;
    }
    
    public void setLastactive(Date lastactive) {
        this.lastactive = lastactive;
    }
    public String getToken() {
        return this.token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    public Integer getEid() {
        return this.eid;
    }
    
    public void setEid(Integer eid) {
        this.eid = eid;
    }




}


