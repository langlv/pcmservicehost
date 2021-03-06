package com.rdsic.pcm.data.entity;
// Generated Jun 20, 2016 9:33:45 AM by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * VUseractiveId generated by hbm2java
 */
public class VUseractiveId  implements java.io.Serializable {


     private String userid;
     private String token;
     private Date lastactive;
     private Date starttime;

    public VUseractiveId() {
    }

	
    public VUseractiveId(String userid) {
        this.userid = userid;
    }
    public VUseractiveId(String userid, String token, Date lastactive, Date starttime) {
       this.userid = userid;
       this.token = token;
       this.lastactive = lastactive;
       this.starttime = starttime;
    }
   
    public String getUserid() {
        return this.userid;
    }
    
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getToken() {
        return this.token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    public Date getLastactive() {
        return this.lastactive;
    }
    
    public void setLastactive(Date lastactive) {
        this.lastactive = lastactive;
    }
    public Date getStarttime() {
        return this.starttime;
    }
    
    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof VUseractiveId) ) return false;
		 VUseractiveId castOther = ( VUseractiveId ) other; 
         
		 return ( (this.getUserid()==castOther.getUserid()) || ( this.getUserid()!=null && castOther.getUserid()!=null && this.getUserid().equals(castOther.getUserid()) ) )
 && ( (this.getToken()==castOther.getToken()) || ( this.getToken()!=null && castOther.getToken()!=null && this.getToken().equals(castOther.getToken()) ) )
 && ( (this.getLastactive()==castOther.getLastactive()) || ( this.getLastactive()!=null && castOther.getLastactive()!=null && this.getLastactive().equals(castOther.getLastactive()) ) )
 && ( (this.getStarttime()==castOther.getStarttime()) || ( this.getStarttime()!=null && castOther.getStarttime()!=null && this.getStarttime().equals(castOther.getStarttime()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getUserid() == null ? 0 : this.getUserid().hashCode() );
         result = 37 * result + ( getToken() == null ? 0 : this.getToken().hashCode() );
         result = 37 * result + ( getLastactive() == null ? 0 : this.getLastactive().hashCode() );
         result = 37 * result + ( getStarttime() == null ? 0 : this.getStarttime().hashCode() );
         return result;
   }   


}


