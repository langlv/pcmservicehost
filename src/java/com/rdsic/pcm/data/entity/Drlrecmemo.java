package com.rdsic.pcm.data.entity;
// Generated Jul 18, 2016 2:28:00 PM by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * Drlrecmemo generated by hbm2java
 */
public class Drlrecmemo  implements java.io.Serializable {


     private DrlrecmemoId id;
     private int ppid;
     private int prid;
     private int dmid;
     private Double deepmeter;
     private Integer direction;
     private Boolean emptydrill;
     private Double drillmeter;
     private Double rotatemeter;
     private Double rq;
     private Double rqtotal;
     private Double amp;
     private Double psr;
     private Double ndn;
     private Double rdq;
     private Double rdqtotal;
     private Boolean startrec;
     private Boolean endrec;
     private Integer recby;
     private Date rectime;
     private String appby;
     private Date apptime;
     private String appsts;

    public Drlrecmemo() {
    }

	
    public Drlrecmemo(DrlrecmemoId id, int ppid, int prid, int dmid, String appsts) {
        this.id = id;
        this.ppid = ppid;
        this.prid = prid;
        this.dmid = dmid;
        this.appsts = appsts;
    }
    public Drlrecmemo(DrlrecmemoId id, int ppid, int prid, int dmid, Double deepmeter, Integer direction, Boolean emptydrill, Double drillmeter, Double rotatemeter, Double rq, Double rqtotal, Double amp, Double psr, Double ndn, Double rdq, Double rdqtotal, Boolean startrec, Boolean endrec, Integer recby, Date rectime, String appby, Date apptime, String appsts) {
       this.id = id;
       this.ppid = ppid;
       this.prid = prid;
       this.dmid = dmid;
       this.deepmeter = deepmeter;
       this.direction = direction;
       this.emptydrill = emptydrill;
       this.drillmeter = drillmeter;
       this.rotatemeter = rotatemeter;
       this.rq = rq;
       this.rqtotal = rqtotal;
       this.amp = amp;
       this.psr = psr;
       this.ndn = ndn;
       this.rdq = rdq;
       this.rdqtotal = rdqtotal;
       this.startrec = startrec;
       this.endrec = endrec;
       this.recby = recby;
       this.rectime = rectime;
       this.appby = appby;
       this.apptime = apptime;
       this.appsts = appsts;
    }
   
    public DrlrecmemoId getId() {
        return this.id;
    }
    
    public void setId(DrlrecmemoId id) {
        this.id = id;
    }
    public int getPpid() {
        return this.ppid;
    }
    
    public void setPpid(int ppid) {
        this.ppid = ppid;
    }
    public int getPrid() {
        return this.prid;
    }
    
    public void setPrid(int prid) {
        this.prid = prid;
    }
    public int getDmid() {
        return this.dmid;
    }
    
    public void setDmid(int dmid) {
        this.dmid = dmid;
    }
    public Double getDeepmeter() {
        return this.deepmeter;
    }
    
    public void setDeepmeter(Double deepmeter) {
        this.deepmeter = deepmeter;
    }
    public Integer getDirection() {
        return this.direction;
    }
    
    public void setDirection(Integer direction) {
        this.direction = direction;
    }
    public Boolean getEmptydrill() {
        return this.emptydrill;
    }
    
    public void setEmptydrill(Boolean emptydrill) {
        this.emptydrill = emptydrill;
    }
    public Double getDrillmeter() {
        return this.drillmeter;
    }
    
    public void setDrillmeter(Double drillmeter) {
        this.drillmeter = drillmeter;
    }
    public Double getRotatemeter() {
        return this.rotatemeter;
    }
    
    public void setRotatemeter(Double rotatemeter) {
        this.rotatemeter = rotatemeter;
    }
    public Double getRq() {
        return this.rq;
    }
    
    public void setRq(Double rq) {
        this.rq = rq;
    }
    public Double getRqtotal() {
        return this.rqtotal;
    }
    
    public void setRqtotal(Double rqtotal) {
        this.rqtotal = rqtotal;
    }
    public Double getAmp() {
        return this.amp;
    }
    
    public void setAmp(Double amp) {
        this.amp = amp;
    }
    public Double getPsr() {
        return this.psr;
    }
    
    public void setPsr(Double psr) {
        this.psr = psr;
    }
    public Double getNdn() {
        return this.ndn;
    }
    
    public void setNdn(Double ndn) {
        this.ndn = ndn;
    }
    public Double getRdq() {
        return this.rdq;
    }
    
    public void setRdq(Double rdq) {
        this.rdq = rdq;
    }
    public Double getRdqtotal() {
        return this.rdqtotal;
    }
    
    public void setRdqtotal(Double rdqtotal) {
        this.rdqtotal = rdqtotal;
    }
    public Boolean getStartrec() {
        return this.startrec;
    }
    
    public void setStartrec(Boolean startrec) {
        this.startrec = startrec;
    }
    public Boolean getEndrec() {
        return this.endrec;
    }
    
    public void setEndrec(Boolean endrec) {
        this.endrec = endrec;
    }
    public Integer getRecby() {
        return this.recby;
    }
    
    public void setRecby(Integer recby) {
        this.recby = recby;
    }
    public Date getRectime() {
        return this.rectime;
    }
    
    public void setRectime(Date rectime) {
        this.rectime = rectime;
    }
    public String getAppby() {
        return this.appby;
    }
    
    public void setAppby(String appby) {
        this.appby = appby;
    }
    public Date getApptime() {
        return this.apptime;
    }
    
    public void setApptime(Date apptime) {
        this.apptime = apptime;
    }
    public String getAppsts() {
        return this.appsts;
    }
    
    public void setAppsts(String appsts) {
        this.appsts = appsts;
    }




}


