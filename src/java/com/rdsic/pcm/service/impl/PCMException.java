/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdsic.pcm.service.impl;

/**
 *
 * @author langl
 */
public class PCMException
  extends Exception
{
  private String description;
  private String code;
  
  public String getDescription()
  {
    return this.description;
  }
  
  public void setDescription(String description)
  {
    this.description = description;
  }
  
  public String getCode()
  {
    return this.code;
  }
  
  public void setCode(String code)
  {
    this.code = code;
  }
  
  public PCMException(String description, String code)
  {
    super(description);
    this.description = description;
    this.code = code;
  }
}
