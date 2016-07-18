package com.rdsic.pcm.data.entity;
// Generated Jul 18, 2016 2:28:00 PM by Hibernate Tools 4.3.1



/**
 * Employee generated by hbm2java
 */
public class Employee  implements java.io.Serializable {


     private Integer eid;
     private String shortname;
     private String fullname;
     private String team;
     private String role;
     private String userid;

    public Employee() {
    }

    public Employee(String shortname, String fullname, String team, String role, String userid) {
       this.shortname = shortname;
       this.fullname = fullname;
       this.team = team;
       this.role = role;
       this.userid = userid;
    }
   
    public Integer getEid() {
        return this.eid;
    }
    
    public void setEid(Integer eid) {
        this.eid = eid;
    }
    public String getShortname() {
        return this.shortname;
    }
    
    public void setShortname(String shortname) {
        this.shortname = shortname;
    }
    public String getFullname() {
        return this.fullname;
    }
    
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    public String getTeam() {
        return this.team;
    }
    
    public void setTeam(String team) {
        this.team = team;
    }
    public String getRole() {
        return this.role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    public String getUserid() {
        return this.userid;
    }
    
    public void setUserid(String userid) {
        this.userid = userid;
    }




}


