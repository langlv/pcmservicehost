package com.rdsic.pcm.data.entity;
// Generated Jul 21, 2016 12:09:09 PM by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * DlogId generated by hbm2java
 */
public class DlogId  implements java.io.Serializable {


     private Date date;
     private String actionid;
     private int direction;

    public DlogId() {
    }

    public DlogId(Date date, String actionid, int direction) {
       this.date = date;
       this.actionid = actionid;
       this.direction = direction;
    }
   
    public Date getDate() {
        return this.date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    public String getActionid() {
        return this.actionid;
    }
    
    public void setActionid(String actionid) {
        this.actionid = actionid;
    }
    public int getDirection() {
        return this.direction;
    }
    
    public void setDirection(int direction) {
        this.direction = direction;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof DlogId) ) return false;
		 DlogId castOther = ( DlogId ) other; 
         
		 return ( (this.getDate()==castOther.getDate()) || ( this.getDate()!=null && castOther.getDate()!=null && this.getDate().equals(castOther.getDate()) ) )
 && ( (this.getActionid()==castOther.getActionid()) || ( this.getActionid()!=null && castOther.getActionid()!=null && this.getActionid().equals(castOther.getActionid()) ) )
 && (this.getDirection()==castOther.getDirection());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getDate() == null ? 0 : this.getDate().hashCode() );
         result = 37 * result + ( getActionid() == null ? 0 : this.getActionid().hashCode() );
         result = 37 * result + this.getDirection();
         return result;
   }   


}


