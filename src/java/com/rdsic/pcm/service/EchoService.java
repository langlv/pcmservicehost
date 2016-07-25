/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdsic.pcm.service;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author langlv
 */
@WebService(serviceName = "EchoService")
public class EchoService {

    /**
     * This is a sample web service operation
     * @param txt
     * @return 
     */
    @WebMethod(operationName = "Ping")
    public String Ping(@WebParam(name = "clientName") String txt) {
        return "Hello " + txt + " !";
    }
}
