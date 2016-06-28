package com.rdsic.pcm.data.entity;
// Generated Jun 20, 2016 9:33:45 AM by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * Cementrecord generated by hbm2java
 */
public class Cementrecord  implements java.io.Serializable {


     private Integer crid;
     private int sid;
     private Date rectime;
     private String note;
     private int curvol;
     private String curcm;
     private Integer sumcementin;
     private Integer sumcementout;
     private Integer sumcementdrl;
     private Double siloloss;
     private Double drlloss;

    public Cementrecord() {
    }

	
    public Cementrecord(int sid, Date rectime, int curvol) {
        this.sid = sid;
        this.rectime = rectime;
        this.curvol = curvol;
    }
    public Cementrecord(int sid, Date rectime, String note, int curvol, String curcm, Integer sumcementin, Integer sumcementout, Integer sumcementdrl, Double siloloss, Double drlloss) {
       this.sid = sid;
       this.rectime = rectime;
       this.note = note;
       this.curvol = curvol;
       this.curcm = curcm;
       this.sumcementin = sumcementin;
       this.sumcementout = sumcementout;
       this.sumcementdrl = sumcementdrl;
       this.siloloss = siloloss;
       this.drlloss = drlloss;
    }
   
    public Integer getCrid() {
        return this.crid;
    }
    
    public void setCrid(Integer crid) {
        this.crid = crid;
    }
    public int getSid() {
        return this.sid;
    }
    
    public void setSid(int sid) {
        this.sid = sid;
    }
    public Date getRectime() {
        return this.rectime;
    }
    
    public void setRectime(Date rectime) {
        this.rectime = rectime;
    }
    public String getNote() {
        return this.note;
    }
    
    public void setNote(String note) {
        this.note = note;
    }
    public int getCurvol() {
        return this.curvol;
    }
    
    public void setCurvol(int curvol) {
        this.curvol = curvol;
    }
    public String getCurcm() {
        return this.curcm;
    }
    
    public void setCurcm(String curcm) {
        this.curcm = curcm;
    }
    public Integer getSumcementin() {
        return this.sumcementin;
    }
    
    public void setSumcementin(Integer sumcementin) {
        this.sumcementin = sumcementin;
    }
    public Integer getSumcementout() {
        return this.sumcementout;
    }
    
    public void setSumcementout(Integer sumcementout) {
        this.sumcementout = sumcementout;
    }
    public Integer getSumcementdrl() {
        return this.sumcementdrl;
    }
    
    public void setSumcementdrl(Integer sumcementdrl) {
        this.sumcementdrl = sumcementdrl;
    }
    public Double getSiloloss() {
        return this.siloloss;
    }
    
    public void setSiloloss(Double siloloss) {
        this.siloloss = siloloss;
    }
    public Double getDrlloss() {
        return this.drlloss;
    }
    
    public void setDrlloss(Double drlloss) {
        this.drlloss = drlloss;
    }




}


