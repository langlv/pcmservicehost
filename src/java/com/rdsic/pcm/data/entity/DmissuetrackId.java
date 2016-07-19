package com.rdsic.pcm.data.entity;
// Generated Jul 19, 2016 4:32:10 PM by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * DmissuetrackId generated by hbm2java
 */
public class DmissuetrackId  implements java.io.Serializable {


     private int prid;
     private String dmid;
     private Date issuebegin;

    public DmissuetrackId() {
    }

    public DmissuetrackId(int prid, String dmid, Date issuebegin) {
       this.prid = prid;
       this.dmid = dmid;
       this.issuebegin = issuebegin;
    }
   
    public int getPrid() {
        return this.prid;
    }
    
    public void setPrid(int prid) {
        this.prid = prid;
    }
    public String getDmid() {
        return this.dmid;
    }
    
    public void setDmid(String dmid) {
        this.dmid = dmid;
    }
    public Date getIssuebegin() {
        return this.issuebegin;
    }
    
    public void setIssuebegin(Date issuebegin) {
        this.issuebegin = issuebegin;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof DmissuetrackId) ) return false;
		 DmissuetrackId castOther = ( DmissuetrackId ) other; 
         
		 return (this.getPrid()==castOther.getPrid())
 && ( (this.getDmid()==castOther.getDmid()) || ( this.getDmid()!=null && castOther.getDmid()!=null && this.getDmid().equals(castOther.getDmid()) ) )
 && ( (this.getIssuebegin()==castOther.getIssuebegin()) || ( this.getIssuebegin()!=null && castOther.getIssuebegin()!=null && this.getIssuebegin().equals(castOther.getIssuebegin()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getPrid();
         result = 37 * result + ( getDmid() == null ? 0 : this.getDmid().hashCode() );
         result = 37 * result + ( getIssuebegin() == null ? 0 : this.getIssuebegin().hashCode() );
         return result;
   }   


}


