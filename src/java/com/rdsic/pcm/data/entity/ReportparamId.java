package com.rdsic.pcm.data.entity;
// Generated Jul 19, 2016 4:32:10 PM by Hibernate Tools 4.3.1



/**
 * ReportparamId generated by hbm2java
 */
public class ReportparamId  implements java.io.Serializable {


     private String code;
     private String parname;
     private int pardir;

    public ReportparamId() {
    }

    public ReportparamId(String code, String parname, int pardir) {
       this.code = code;
       this.parname = parname;
       this.pardir = pardir;
    }
   
    public String getCode() {
        return this.code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    public String getParname() {
        return this.parname;
    }
    
    public void setParname(String parname) {
        this.parname = parname;
    }
    public int getPardir() {
        return this.pardir;
    }
    
    public void setPardir(int pardir) {
        this.pardir = pardir;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof ReportparamId) ) return false;
		 ReportparamId castOther = ( ReportparamId ) other; 
         
		 return ( (this.getCode()==castOther.getCode()) || ( this.getCode()!=null && castOther.getCode()!=null && this.getCode().equals(castOther.getCode()) ) )
 && ( (this.getParname()==castOther.getParname()) || ( this.getParname()!=null && castOther.getParname()!=null && this.getParname().equals(castOther.getParname()) ) )
 && (this.getPardir()==castOther.getPardir());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getCode() == null ? 0 : this.getCode().hashCode() );
         result = 37 * result + ( getParname() == null ? 0 : this.getParname().hashCode() );
         result = 37 * result + this.getPardir();
         return result;
   }   


}


