package com.rdsic.pcm.data.entity;
// Generated Jul 18, 2016 2:28:00 PM by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * Drlmonitor generated by hbm2java
 */
public class Drlmonitor  implements java.io.Serializable {


     private DrlmonitorId id;
     private Date drilltime;
     private Double limitval;
     private Double actval;
     private String status;

    public Drlmonitor() {
    }

	
    public Drlmonitor(DrlmonitorId id, Date drilltime) {
        this.id = id;
        this.drilltime = drilltime;
    }
    public Drlmonitor(DrlmonitorId id, Date drilltime, Double limitval, Double actval, String status) {
       this.id = id;
       this.drilltime = drilltime;
       this.limitval = limitval;
       this.actval = actval;
       this.status = status;
    }
   
    public DrlmonitorId getId() {
        return this.id;
    }
    
    public void setId(DrlmonitorId id) {
        this.id = id;
    }
    public Date getDrilltime() {
        return this.drilltime;
    }
    
    public void setDrilltime(Date drilltime) {
        this.drilltime = drilltime;
    }
    public Double getLimitval() {
        return this.limitval;
    }
    
    public void setLimitval(Double limitval) {
        this.limitval = limitval;
    }
    public Double getActval() {
        return this.actval;
    }
    
    public void setActval(Double actval) {
        this.actval = actval;
    }
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }




}


