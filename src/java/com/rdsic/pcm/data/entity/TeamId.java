package com.rdsic.pcm.data.entity;
// Generated Jul 19, 2016 4:32:10 PM by Hibernate Tools 4.3.1



/**
 * TeamId generated by hbm2java
 */
public class TeamId  implements java.io.Serializable {


     private int prid;
     private String code;

    public TeamId() {
    }

    public TeamId(int prid, String code) {
       this.prid = prid;
       this.code = code;
    }
   
    public int getPrid() {
        return this.prid;
    }
    
    public void setPrid(int prid) {
        this.prid = prid;
    }
    public String getCode() {
        return this.code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof TeamId) ) return false;
		 TeamId castOther = ( TeamId ) other; 
         
		 return (this.getPrid()==castOther.getPrid())
 && ( (this.getCode()==castOther.getCode()) || ( this.getCode()!=null && castOther.getCode()!=null && this.getCode().equals(castOther.getCode()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getPrid();
         result = 37 * result + ( getCode() == null ? 0 : this.getCode().hashCode() );
         return result;
   }   


}


