package com.rdsic.pcm.data.entity;
// Generated Jun 20, 2016 9:33:45 AM by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * Dlog generated by hbm2java
 */
public class Dlog  implements java.io.Serializable {


     private DlogId id;
     private String userid;
     private String clientid;
     private Date reqat;
     private Date resat;
     private String operation;
     private String rawreq;
     private String rawres;
     private String recid;
     private String tbl;
     private String lfld;
     private String lval;

    public Dlog() {
    }

	
    public Dlog(DlogId id, String clientid) {
        this.id = id;
        this.clientid = clientid;
    }
    public Dlog(DlogId id, String userid, String clientid, Date reqat, Date resat, String operation, String rawreq, String rawres, String recid, String tbl, String lfld, String lval) {
       this.id = id;
       this.userid = userid;
       this.clientid = clientid;
       this.reqat = reqat;
       this.resat = resat;
       this.operation = operation;
       this.rawreq = rawreq;
       this.rawres = rawres;
       this.recid = recid;
       this.tbl = tbl;
       this.lfld = lfld;
       this.lval = lval;
    }
   
    public DlogId getId() {
        return this.id;
    }
    
    public void setId(DlogId id) {
        this.id = id;
    }
    public String getUserid() {
        return this.userid;
    }
    
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getClientid() {
        return this.clientid;
    }
    
    public void setClientid(String clientid) {
        this.clientid = clientid;
    }
    public Date getReqat() {
        return this.reqat;
    }
    
    public void setReqat(Date reqat) {
        this.reqat = reqat;
    }
    public Date getResat() {
        return this.resat;
    }
    
    public void setResat(Date resat) {
        this.resat = resat;
    }
    public String getOperation() {
        return this.operation;
    }
    
    public void setOperation(String operation) {
        this.operation = operation;
    }
    public String getRawreq() {
        return this.rawreq;
    }
    
    public void setRawreq(String rawreq) {
        this.rawreq = rawreq;
    }
    public String getRawres() {
        return this.rawres;
    }
    
    public void setRawres(String rawres) {
        this.rawres = rawres;
    }
    public String getRecid() {
        return this.recid;
    }
    
    public void setRecid(String recid) {
        this.recid = recid;
    }
    public String getTbl() {
        return this.tbl;
    }
    
    public void setTbl(String tbl) {
        this.tbl = tbl;
    }
    public String getLfld() {
        return this.lfld;
    }
    
    public void setLfld(String lfld) {
        this.lfld = lfld;
    }
    public String getLval() {
        return this.lval;
    }
    
    public void setLval(String lval) {
        this.lval = lval;
    }




}


