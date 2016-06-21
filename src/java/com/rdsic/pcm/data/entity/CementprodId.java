package com.rdsic.pcm.data.entity;
// Generated Jun 20, 2016 9:33:45 AM by Hibernate Tools 4.3.1



/**
 * CementprodId generated by hbm2java
 */
public class CementprodId  implements java.io.Serializable {


     private String name;
     private String producer;

    public CementprodId() {
    }

    public CementprodId(String name, String producer) {
       this.name = name;
       this.producer = producer;
    }
   
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getProducer() {
        return this.producer;
    }
    
    public void setProducer(String producer) {
        this.producer = producer;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof CementprodId) ) return false;
		 CementprodId castOther = ( CementprodId ) other; 
         
		 return ( (this.getName()==castOther.getName()) || ( this.getName()!=null && castOther.getName()!=null && this.getName().equals(castOther.getName()) ) )
 && ( (this.getProducer()==castOther.getProducer()) || ( this.getProducer()!=null && castOther.getProducer()!=null && this.getProducer().equals(castOther.getProducer()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getName() == null ? 0 : this.getName().hashCode() );
         result = 37 * result + ( getProducer() == null ? 0 : this.getProducer().hashCode() );
         return result;
   }   


}


