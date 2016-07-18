package com.rdsic.pcm.data.entity;
// Generated Jul 18, 2016 2:28:00 PM by Hibernate Tools 4.3.1



/**
 * Oprtag generated by hbm2java
 */
public class Oprtag  implements java.io.Serializable {


     private OprtagId id;
     private String type;
     private Integer seq;
     private Integer direction;
     private Boolean required;
     private String defval;
     private String description;

    public Oprtag() {
    }

	
    public Oprtag(OprtagId id) {
        this.id = id;
    }
    public Oprtag(OprtagId id, String type, Integer seq, Integer direction, Boolean required, String defval, String description) {
       this.id = id;
       this.type = type;
       this.seq = seq;
       this.direction = direction;
       this.required = required;
       this.defval = defval;
       this.description = description;
    }
   
    public OprtagId getId() {
        return this.id;
    }
    
    public void setId(OprtagId id) {
        this.id = id;
    }
    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    public Integer getSeq() {
        return this.seq;
    }
    
    public void setSeq(Integer seq) {
        this.seq = seq;
    }
    public Integer getDirection() {
        return this.direction;
    }
    
    public void setDirection(Integer direction) {
        this.direction = direction;
    }
    public Boolean getRequired() {
        return this.required;
    }
    
    public void setRequired(Boolean required) {
        this.required = required;
    }
    public String getDefval() {
        return this.defval;
    }
    
    public void setDefval(String defval) {
        this.defval = defval;
    }
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }




}


