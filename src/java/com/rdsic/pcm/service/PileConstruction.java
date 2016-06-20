/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdsic.pcm.service;

import com.rdsic.pcm.service.impl.PileConstructionImpl;
import javax.jws.WebService;

/**
 *
 * @author NAPAS
 */
@WebService(serviceName = "PileConstruction", portName = "PileConstructionEndpoint", endpointInterface = "com.rdsic.pileconstructionmanagement.service.pileconstruction.PieConstructionPort", targetNamespace = "http://rdsic.com/pileconstructionmanagement/service/pileconstruction", wsdlLocation = "WEB-INF/wsdl/PileConstruction/PileConstruction_v1.0.wsdl")
public class PileConstruction {

    public com.rdsic.pileconstructionmanagement.type.pileconstruction.GetDrillingRecordDetailRes getDrillingRecordDetail(com.rdsic.pileconstructionmanagement.type.pileconstruction.GetDrillingRecordDetailReq req) {
        return PileConstructionImpl.getDrillingRecordDetail(req);
    }

    public com.rdsic.pileconstructionmanagement.type.pileconstruction.AddOrUpdateMixingRecordRes addOrUpdateMixingRecord(com.rdsic.pileconstructionmanagement.type.pileconstruction.AddOrUpdateMixingRecordReq req) {
        return PileConstructionImpl.addOrUpdateMixingRecord(req);
    }

    public com.rdsic.pileconstructionmanagement.type.pileconstruction.CreateDrillingRecordRes createDrillingRecord(com.rdsic.pileconstructionmanagement.type.pileconstruction.CreateDrillingRecordReq req) {
        return PileConstructionImpl.createDrillingRecord(req);
    }

    public com.rdsic.pileconstructionmanagement.type.pileconstruction.ApproveDrillingRecordRes approveDrillingRecord(com.rdsic.pileconstructionmanagement.type.pileconstruction.ApproveDrillingRecordReq req) {
        return PileConstructionImpl.approveDrillingRecord(req);
    }

    public com.rdsic.pileconstructionmanagement.type.pileconstruction.LoadCementToSiloRes loadCementToSilo(com.rdsic.pileconstructionmanagement.type.pileconstruction.LoadCementToSiloReq req) {
        return PileConstructionImpl.loadCementToSilo(req);
    }

    public com.rdsic.pileconstructionmanagement.type.pileconstruction.ModifyDrillingRecordRes modifyDrillingRecord(com.rdsic.pileconstructionmanagement.type.pileconstruction.ModifyDrillingRecordReq req) {
        return PileConstructionImpl.modifyDrillingRecord(req);
    }

    public com.rdsic.pileconstructionmanagement.type.pileconstruction.CheckCementInSiloRes checkCementInSilo(com.rdsic.pileconstructionmanagement.type.pileconstruction.CheckCementInSiloReq req) {
        return PileConstructionImpl.checkCementInSilo(req);
    }

}
