package com.rdsic.pcm.data.entity;
// Generated Jul 21, 2016 12:09:09 PM by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * Permission generated by hbm2java
 */
public class Permission  implements java.io.Serializable {


     private PermissionId id;
     private Date starttime;
     private Date endtime;

    public Permission() {
    }

	
    public Permission(PermissionId id) {
        this.id = id;
    }
    public Permission(PermissionId id, Date starttime, Date endtime) {
       this.id = id;
       this.starttime = starttime;
       this.endtime = endtime;
    }
   
    public PermissionId getId() {
        return this.id;
    }
    
    public void setId(PermissionId id) {
        this.id = id;
    }
    public Date getStarttime() {
        return this.starttime;
    }
    
    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }
    public Date getEndtime() {
        return this.endtime;
    }
    
    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }




}


