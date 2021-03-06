package com.rdsic.pcm.data.entity;
// Generated Jun 20, 2016 9:33:45 AM by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * DrlrecmemoId generated by hbm2java
 */
public class DrlrecmemoId  implements java.io.Serializable {


     private String drid;
     private Date drilltime;

    public DrlrecmemoId() {
    }

    public DrlrecmemoId(String drid, Date drilltime) {
       this.drid = drid;
       this.drilltime = drilltime;
    }
   
    public String getDrid() {
        return this.drid;
    }
    
    public void setDrid(String drid) {
        this.drid = drid;
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
		 if ( !(other instanceof DrlrecmemoId) ) return false;
		 DrlrecmemoId castOther = ( DrlrecmemoId ) other; 
         
		 return ( (this.getDrid()==castOther.getDrid()) || ( this.getDrid()!=null && castOther.getDrid()!=null && this.getDrid().equals(castOther.getDrid()) ) )
 && ( (this.getDrilltime()==castOther.getDrilltime()) || ( this.getDrilltime()!=null && castOther.getDrilltime()!=null && this.getDrilltime().equals(castOther.getDrilltime()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getDrid() == null ? 0 : this.getDrid().hashCode() );
         result = 37 * result + ( getDrilltime() == null ? 0 : this.getDrilltime().hashCode() );
         return result;
   }   


}


