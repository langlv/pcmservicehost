package com.rdsic.pcm.data.entity;
// Generated Jul 21, 2016 12:09:09 PM by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * DrlrecmastId generated by hbm2java
 */
public class DrlrecmastId  implements java.io.Serializable {


     private String drid;
     private int ppid;
     private Date drilltime;

    public DrlrecmastId() {
    }

    public DrlrecmastId(String drid, int ppid, Date drilltime) {
       this.drid = drid;
       this.ppid = ppid;
       this.drilltime = drilltime;
    }
   
    public String getDrid() {
        return this.drid;
    }
    
    public void setDrid(String drid) {
        this.drid = drid;
    }
    public int getPpid() {
        return this.ppid;
    }
    
    public void setPpid(int ppid) {
        this.ppid = ppid;
    }
    public Date getDrilltime() {
        return this.drilltime;
    }
    
    public void setDrilltime(Date drilltime) {
        this.drilltime = drilltime;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof DrlrecmastId) ) return false;
		 DrlrecmastId castOther = ( DrlrecmastId ) other; 
         
		 return ( (this.getDrid()==castOther.getDrid()) || ( this.getDrid()!=null && castOther.getDrid()!=null && this.getDrid().equals(castOther.getDrid()) ) )
 && (this.getPpid()==castOther.getPpid())
 && ( (this.getDrilltime()==castOther.getDrilltime()) || ( this.getDrilltime()!=null && castOther.getDrilltime()!=null && this.getDrilltime().equals(castOther.getDrilltime()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getDrid() == null ? 0 : this.getDrid().hashCode() );
         result = 37 * result + this.getPpid();
         result = 37 * result + ( getDrilltime() == null ? 0 : this.getDrilltime().hashCode() );
         return result;
   }   


}


