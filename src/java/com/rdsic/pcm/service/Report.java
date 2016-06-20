/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdsic.pcm.service;

import com.rdsic.pcm.service.impl.ReportImpl;
import javax.jws.WebService;

/**
 *
 * @author NAPAS
 */
@WebService(serviceName = "Report", portName = "ReportingEndpoint", endpointInterface = "com.rdsic.pileconstructionmanagement.service.report.ReportingPort", targetNamespace = "http://rdsic.com/pileconstructionmanagement/service/report", wsdlLocation = "WEB-INF/wsdl/Report/Report_v1.0.wsdl")
public class Report {

    public com.rdsic.pileconstructionmanagement.type.reporting.ExecReportRes execReport(com.rdsic.pileconstructionmanagement.type.reporting.ExecReportReq req) {
        return ReportImpl.execReport(req);
    }

    public com.rdsic.pileconstructionmanagement.type.reporting.DayendRes runDayend(com.rdsic.pileconstructionmanagement.type.reporting.DayendReq req) {
        return ReportImpl.runDayend(req);
    }

}
