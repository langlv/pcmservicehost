/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdsic.pcm.service;

import com.rdsic.pcm.service.impl.GenericQueryImpl;
import javax.jws.WebService;

/**
 *
 * @author langl
 */
@WebService(serviceName = "GenericQuery", portName = "GenericQueryPort", endpointInterface = "com.rdsic.pileconstructionmanagement.service.genericquery.GenericQueryPortType", targetNamespace = "http://rdsic.com/pileconstructionmanagement/service/genericquery", wsdlLocation = "WEB-INF/wsdl/GenericQuery/GenericQuery_v1.0.wsdl")
public class GenericQuery {

    public com.rdsic.pileconstructionmanagement.type.genericquery.SelectQueryRes select(com.rdsic.pileconstructionmanagement.type.genericquery.SelectQueryReq req) {
        return GenericQueryImpl.select(req);
    }

    public com.rdsic.pileconstructionmanagement.type.genericquery.AddOrUpdateRes addOrUpdate(com.rdsic.pileconstructionmanagement.type.genericquery.AddOrUpdateReq req) {
        return GenericQueryImpl.addOrUpdate(req);
    }

}
