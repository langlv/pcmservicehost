package com.rdsic.pcm.data.entity;
// Generated Jul 19, 2016 4:32:10 PM by Hibernate Tools 4.3.1



/**
 * Team generated by hbm2java
 */
public class Team  implements java.io.Serializable {


     private TeamId id;
     private String name;
     private Integer empcnt;

    public Team() {
    }

	
    public Team(TeamId id) {
        this.id = id;
    }
    public Team(TeamId id, String name, Integer empcnt) {
       this.id = id;
       this.name = name;
       this.empcnt = empcnt;
    }
   
    public TeamId getId() {
        return this.id;
    }
    
    public void setId(TeamId id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public Integer getEmpcnt() {
        return this.empcnt;
    }
    
    public void setEmpcnt(Integer empcnt) {
        this.empcnt = empcnt;
    }




}


