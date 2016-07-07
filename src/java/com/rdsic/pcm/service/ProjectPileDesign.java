/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdsic.pcm.service;

import com.rdsic.pcm.service.impl.ProjectPileDesignImpl;
import javax.jws.WebService;

/**
 *
 * @author NAPAS
 */
@WebService(serviceName = "ProjectPileDesign", portName = "ProjectPileDesignEndpoint", endpointInterface = "com.rdsic.pileconstructionmanagement.service.projectpiledesign.ProjectPileDesignPortType", targetNamespace = "http://rdsic.com/pileconstructionmanagement/service/projectpiledesign", wsdlLocation = "WEB-INF/wsdl/ProjectPileDesign/ProjectPileDesign_v1.0.wsdl")
public class ProjectPileDesign {

    public com.rdsic.pileconstructionmanagement.type.projectpiledesign.GetPileDesignRes getPileDesign(com.rdsic.pileconstructionmanagement.type.projectpiledesign.GetPileDesignReq parameter) {
        return ProjectPileDesignImpl.getPileDesign(parameter);
    }

    public com.rdsic.pileconstructionmanagement.type.projectpiledesign.AddOrUpdatePileDesignRes addOrUpdatePileDesign(com.rdsic.pileconstructionmanagement.type.projectpiledesign.AddOrUpdatePileDesignReq parameter) {
        return ProjectPileDesignImpl.addOrUpdatePileDesign(parameter);
    }

    public com.rdsic.pileconstructionmanagement.type.projectpiledesign.AddOrUpdateProjectRes addOrUpdateProject(com.rdsic.pileconstructionmanagement.type.projectpiledesign.AddOrUpdateProjectReq parameter) {
        return ProjectPileDesignImpl.addOrUpdateProject(parameter);
    }

    public com.rdsic.pileconstructionmanagement.type.projectpiledesign.AddOrUpdatePilePlanRes addOrUpdatePilePlan(com.rdsic.pileconstructionmanagement.type.projectpiledesign.AddOrUpdatePilePlanReq parameter) {
        return ProjectPileDesignImpl.addOrUpdateProjectPilePlan(parameter);
    }

    public com.rdsic.pileconstructionmanagement.type.projectpiledesign.GetProjectDetailRes getProjectDetail(com.rdsic.pileconstructionmanagement.type.projectpiledesign.GetProjectDetailReq parameter) {
        return ProjectPileDesignImpl.getProjectDetail(parameter);
    }

    public com.rdsic.pileconstructionmanagement.type.projectpiledesign.GetProjectAssetSummaryRes getProjectAssetSummary(com.rdsic.pileconstructionmanagement.type.projectpiledesign.GetProjectAssetSummaryReq req) {
        return ProjectPileDesignImpl.getProjectAssetSummary(req);
    }

    public com.rdsic.pileconstructionmanagement.type.projectpiledesign.GetPilePlanDetailRes getProjectPilePlan(com.rdsic.pileconstructionmanagement.type.projectpiledesign.GetPilePlanDetailReq parameter) {
        return ProjectPileDesignImpl.getPilePlanDetail(parameter);
    }

}
