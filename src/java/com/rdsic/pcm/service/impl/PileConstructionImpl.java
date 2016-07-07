/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdsic.pcm.service.impl;

import com.rdsic.pcm.data.entity.Mixingrecord;
import com.rdsic.pcm.data.entity.Drlrecmemo;
import com.rdsic.pcm.data.entity.Cementrecord;
import com.rdsic.pcm.data.entity.Cementin;
import com.rdsic.pcm.data.entity.Drillingmachine;
import com.rdsic.pcm.data.entity.Pileplan;
import com.rdsic.pcm.data.entity.DrlrecmemoId;
import com.rdsic.pcm.common.Util;
import com.rdsic.pcm.common.HibernateUtil;
import com.rdsic.pcm.common.Constant;
import com.rdsic.pcm.common.GenericHql;
import com.rdsic.pcm.data.entity.User;
import com.rdsic.pileconstructionmanagement.type.pileconstruction.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author langl
 */
public class PileConstructionImpl {

    /**
     * Implementation method for operation LoadCementToSilo
     *
     * @param req
     * @return
     */
    public static LoadCementToSiloRes loadCementToSilo(LoadCementToSiloReq req) {
        String key = UUID.randomUUID().toString();
        String opr = "PileConstruction/LoadCementToSilo";
        Logger.LogReq(key, opr, req);

        Date now = new Date();
        LoadCementToSiloRes res = new LoadCementToSiloRes();
        if (!Util.validateRequest(req, opr, Constant.FUNCTIONALITY_ACTION.WS_INVOKE, res)) {
            Logger.LogRes(key, opr, res);
            return res;
        }

        try {
            CementPlanType cement = req.getCementPlan();
            Cementin ci = new Cementin();
            int cid = cement.getCID();// == null ? -1 : cement.getCID();

            // validate record exists if id is specified
            if (cid > 0) {
                List<Cementin> list = GenericHql.INSTANCE.query("From Cementin where cid=:id", "id", cid);
                if (list.isEmpty()) {
                    throw new PCMException("Invalid cement plan id " + cid, Constant.STATUS_CODE.ERR_ID_INVALID);
                }
                ci.setCid(cid);
            }

            ci.setExpdate(cement.getEXPDATE() == null ? null : Util.toDate(cement.getEXPDATE()));
            ci.setLicplate(cement.getLICPLATE());
            ci.setName(cement.getNAME());
            ci.setProddate(cement.getPRODDATE() == null ? null : Util.toDate(cement.getPRODDATE()));
            ci.setProjectcode(cement.getPROJECTCODE());
            ci.setQuantity(cement.getQUANTITY());
            ci.setRcvdate(cement.getRCVDATE() == null ? null : Util.toDate(cement.getRCVDATE()));
            ci.setReceiver(cement.getRECEIVER());
            ci.setRegnum(cement.getREGNUM());
            ci.setSilo(cement.getSILO());
            ci.setTransporter(cement.getTRANSPORTER());
            ci.setTransportmtd(cement.getTRANSPORTMTD());

            HibernateUtil.beginTransaction();
            HibernateUtil.currentSession().save(ci);
            HibernateUtil.commit();

            res.setCID(ci.getCid());

            res.setStatus(Constant.STATUS_CODE.OK);
        } catch (Exception e) {
            Util.handleException(e, res);
        }
        res.setResponseDateTime(Util.toXmlGregorianCalendar(now));
        Logger.LogRes(key, opr, res);
        return res;
    }

    /**
     * Implementation method for operation CheckCementInSilo
     *
     * @param req
     * @return
     */
    public static CheckCementInSiloRes checkCementInSilo(CheckCementInSiloReq req) {
        String key = UUID.randomUUID().toString();
        String opr = "PileConstruction/CheckCementInSilo";
        Logger.LogReq(key, opr, req);

        Date now = new Date();
        CheckCementInSiloRes res = new CheckCementInSiloRes();
        if (!Util.validateRequest(req, opr, Constant.FUNCTIONALITY_ACTION.WS_INVOKE, res)) {
            Logger.LogRes(key, opr, res);
            return res;
        }

        try {
            CementRecordType cement = req.getCementRecord();
            Cementrecord cr = new Cementrecord();
            int crid = cement.getCRID();
            int sid = cement.getSID();

            // validate record exists if id is specified
            if (crid > 0) {
                List<Cementin> list = GenericHql.INSTANCE.query("From Cementrecord where crid=:id", "id", crid);
                if (list.isEmpty()) {
                    throw new PCMException("Invalid cement record id " + crid, Constant.STATUS_CODE.ERR_ID_INVALID);
                }
                cr.setCrid(crid);
            }

            // calculate cement loss
            List<Date> lstLastRec = GenericHql.INSTANCE.query("select max(rectime) from Cementrecord where sid=:sid", "sid", sid);
            Date lastRec = lstLastRec.isEmpty() || lstLastRec.get(0) == null ? new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1900") : lstLastRec.get(0);

            // calculate total cement was loaded into silo
            List<Cementin> lstCementIn = GenericHql.INSTANCE.query("from Cementin where rcvdate>=:lastdate and silo=:sid", "lastdate", lastRec, "sid", sid);
            int totalCementIn = 0;
            for (Cementin cmi : lstCementIn) {
                totalCementIn += cmi.getQuantity();
            }

            // calculate total cement was used in mixing plant
            List<Mixingrecord> lstMixRec = GenericHql.INSTANCE.query("from Mixingrecord where sid=:sid and mstart>=:lastdate", "sid", sid, "lastdate", lastRec);
            int totalCementOut = 0;
            for (Mixingrecord mr : lstMixRec) {
                totalCementOut += mr.getCementvol();
            }

            int loss1 = totalCementOut + cement.getCURVOL() - totalCementIn;

            // TODO: calculate total cement used in drilling
            cr.setCurcm(cement.getCURCM());
            cr.setCurvol(cement.getCURVOL());
            cr.setNote(cement.getNOTE());
            cr.setRectime(cement.getRECTIME() == null ? now : Util.toDate(cement.getRECTIME()));
            cr.setSid(cement.getSID());
            cr.setSumcementin(totalCementIn);
            cr.setSumcementout(totalCementOut);
            cr.setSiloloss(Double.valueOf(loss1));

            // immediately calculate cement loss
            HibernateUtil.beginTransaction();
            HibernateUtil.currentSession().save(cr);
            HibernateUtil.commit();

            res.setCRID(cr.getCrid());

            res.setStatus(Constant.STATUS_CODE.OK);
        } catch (Exception e) {
            Util.handleException(e, res);
        }
        res.setResponseDateTime(Util.toXmlGregorianCalendar(now));
        Logger.LogRes(key, opr, res);
        return res;
    }

    /**
     * Implementation method for operation AddOrUpdateMixingRecord
     *
     * @param req
     * @return
     */
    public static AddOrUpdateMixingRecordRes addOrUpdateMixingRecord(AddOrUpdateMixingRecordReq req) {
        String key = UUID.randomUUID().toString();
        String opr = "PileConstruction/AddOrUpdateMixingRecord";
        Logger.LogReq(key, opr, req);

        Date now = new Date();
        AddOrUpdateMixingRecordRes res = new AddOrUpdateMixingRecordRes();
        if (!Util.validateRequest(req, opr, Constant.FUNCTIONALITY_ACTION.WS_INVOKE, res)) {
            Logger.LogRes(key, opr, res);
            return res;
        }

        try {
            MixingRecordType mixrec = req.getMixingRecord();
            Mixingrecord mr = new Mixingrecord();
            int mrid = mixrec.getMRID();

            // validate record exists if id is specified
            if (mrid > 0) {
                List<Cementin> list = GenericHql.INSTANCE.query("From Mixingrecord where mrid=:id", "id", mrid);
                if (list.isEmpty()) {
                    throw new PCMException(Constant.STATUS_CODE.ERR_ID_INVALID, "Invalid mixing record id " + mrid);
                }
                mr.setMrid(mrid);
            }

            mr.setCementtype(mixrec.getCEMENTTYPE());
            mr.setCementvol(mixrec.getCEMENTVOL());
            mr.setMfinish(mixrec.getMFINISH() == null ? null : Util.toDate(mixrec.getMFINISH()));
            mr.setMpid(mixrec.getMPID());
            mr.setMstart(mixrec.getMSTART() == null ? null : Util.toDate(mixrec.getMSTART()));
            mr.setSid(mixrec.getSID());
            mr.setWatervol(mixrec.getWATERVOL());
            mr.setWorker(mixrec.getWORKER());

            HibernateUtil.beginTransaction();
            HibernateUtil.currentSession().save(mr);
            HibernateUtil.commit();

            res.setMRID(mr.getMrid());

            res.setStatus(Constant.STATUS_CODE.OK);
        } catch (Exception e) {
            Util.handleException(e, res);
        }
        res.setResponseDateTime(Util.toXmlGregorianCalendar(now));
        Logger.LogRes(key, opr, res);
        return res;
    }

    /**
     * Implementation method for operation CreateDrillingRecord
     *
     * @param req
     * @return
     */
    public static CreateDrillingRecordRes createDrillingRecord(CreateDrillingRecordReq req) {
        String key = UUID.randomUUID().toString();
        String opr = "PileConstruction/CreateDrillingRecord";
        Logger.LogReq(key, opr, req);

        Date now = new Date();
        CreateDrillingRecordRes res = new CreateDrillingRecordRes();
        if (!Util.validateRequest(req, opr, Constant.FUNCTIONALITY_ACTION.WS_INVOKE, res)) {
            Logger.LogRes(key, opr, res);
            return res;
        }

        try {
            DrillingRecordType drtype = req.getDrillingRecord();
            if (drtype.getDrillingData() != null) {

                HibernateUtil.beginTransaction();

                for (DrillingRecordType.DrillingData drlData : drtype.getDrillingData()) {
                    int PPID = drlData.getPPID();

                    // validate pile plan id
                    List<Pileplan> listPP = GenericHql.INSTANCE.query("from Pileplan where ppid=:id", "id", PPID);
                    if (listPP.isEmpty()) {
                        throw new PCMException(Constant.STATUS_CODE.ERR_INVALID_INPUT_DATA, "Pile plan id is not valid: " + PPID);
                    }
                    Pileplan drlPlan = listPP.get(0);

                    // validate drilling machine information
                    List<Drillingmachine> listMachine = GenericHql.INSTANCE.query("from Drillingmachine where dmid=:id ", "id", drtype.getDMID());
                    if (listMachine.isEmpty()) {
                        throw new PCMException(Constant.STATUS_CODE.ERR_INVALID_INPUT_DATA, "Drilling machine id is not valid: " + drtype.getDMID());
                    }
                    Drillingmachine machine = listMachine.get(0);
                    if (machine.getCutwing() <= 0) {
                        throw new PCMException(Constant.STATUS_CODE.ERR_INVALID_CONFIG_DATA, "Drilling machine information is not correct: CUTWING");
                    }

                    // check if this is a first drilling record of pile plan id
                    // if not, get previous record for calculation
                    List<Drlrecmemo> listDrlrec = GenericHql.INSTANCE.query("from Drlrecmemo where ppid=:id order by drilltime desc", "id", PPID);
                    Drlrecmemo lastDrlrec = listDrlrec.isEmpty() ? null : listDrlrec.get(0);
                    boolean isFirstRec = lastDrlrec == null;

                    Drlrecmemo currDrlrec = new Drlrecmemo();
                    String drid = Util.generateToken();

                    currDrlrec.setId(new DrlrecmemoId(drid, Util.toDate(drtype.getRecordTime())));
                    currDrlrec.setPpid(PPID);
                    currDrlrec.setAmp(drlData.getAMP());
                    currDrlrec.setDeepmeter(drlData.getDeepMeter());
                    currDrlrec.setDirection(isFirstRec ? 1 : lastDrlrec.getDeepmeter() <= drlData.getDeepMeter() ? 1 : 0);
                    currDrlrec.setDmid(drtype.getDMID());
                    currDrlrec.setDrillmeter(drlData.getDeepMovement());
                    currDrlrec.setEmptydrill(drlData.getConcreteMovement() == 0);
                    currDrlrec.setEndrec(false);  // need to check
                    currDrlrec.setNdn(drlData.getDeepMovement() == 0 ? 0 : drlData.getRPM() / (drlData.getDeepMovement() * machine.getCutwing()));
                    currDrlrec.setPrid(drlPlan.getPrid());
                    currDrlrec.setPsr(drlData.getPSR());
                    currDrlrec.setRdq(drlData.getDeepMovement() == 0 ? 0 : drlData.getConcreteMovement() / drlData.getDeepMovement());
                    currDrlrec.setRdqtotal(isFirstRec || drlData.getDeepMovement() == 0 ? 0 : (drlData.getConcreteMeter() - lastDrlrec.getRqtotal()) / drlData.getDeepMovement());
                    currDrlrec.setRecby(machine.getDriver1());
                    currDrlrec.setRectime(Util.toDate(drtype.getRecordTime()));
                    currDrlrec.setRotatemeter(drlData.getRPM());
                    currDrlrec.setRq(drlData.getConcreteMovement());
                    currDrlrec.setRqtotal(drlData.getConcreteMeter());
                    currDrlrec.setStartrec(isFirstRec);
                    currDrlrec.setAppsts("N"); // new

                    HibernateUtil.currentSession().save(currDrlrec);

                    // populate new drilling id
                    CreateDrillingRecordRes.DrillingRecord dr = new CreateDrillingRecordRes.DrillingRecord();
                    dr.setDRID(drid);
                    res.getDrillingRecord().add(dr);
                }

                HibernateUtil.commit();
            }

            res.setStatus(Constant.STATUS_CODE.OK);
        } catch (Exception e) {
            HibernateUtil.rollback();
            res.getDrillingRecord().clear();
            Util.handleException(e, res);
        }
        res.setResponseDateTime(Util.toXmlGregorianCalendar(now));
        Logger.LogRes(key, opr, res);
        return res;
    }

    /**
     * Implementation method for operation LoadCementToSilo
     *
     * @param req
     * @return
     */
    public static GetDrillingRecordDetailRes getDrillingRecordDetail(GetDrillingRecordDetailReq req) {
        String key = UUID.randomUUID().toString();
        String opr = "PileConstruction/GetDrillingRecordDetail";
        Logger.LogReq(key, opr, req);

        Date now = new Date();
        GetDrillingRecordDetailRes res = new GetDrillingRecordDetailRes();
        if (!Util.validateRequest(req, opr, Constant.FUNCTIONALITY_ACTION.WS_INVOKE, res)) {
            Logger.LogRes(key, opr, res);
            return res;
        }

        try {
            int ppid = req.getPPID();
            List<Drlrecmemo> listDrlrec = GenericHql.INSTANCE.query("from Drlrecmemo where ppid=:id order by drilltime desc", "id", ppid);
            for (Drlrecmemo drlrec : listDrlrec) {
                DrillingRecordDetailType drldetail = new DrillingRecordDetailType();

                drldetail.setAMP(drlrec.getAmp());
                drldetail.setAPPBY(drlrec.getAppby());
                drldetail.setAPPSTS(drlrec.getAppsts());
                drldetail.setAPPTIME(drlrec.getApptime() == null ? null : Util.toXmlGregorianCalendar(drlrec.getApptime()));
                drldetail.setDEEPMETER(drlrec.getDeepmeter());
                drldetail.setDIRECTION(drlrec.getDirection());
                drldetail.setDMID(drlrec.getDmid());
                drldetail.setDRID(drlrec.getId().getDrid());
                drldetail.setDRILLMETER(drlrec.getDrillmeter());
                drldetail.setDRILLTIME(drlrec.getId().getDrilltime() == null ? null : Util.toXmlGregorianCalendar(drlrec.getId().getDrilltime()));
                drldetail.setEMPTYDRILL(drlrec.getEmptydrill());
                drldetail.setENDREC(drlrec.getEndrec());
                drldetail.setNDN(drlrec.getNdn());
                drldetail.setPPID(ppid);
                drldetail.setPSR(drlrec.getPsr());
                drldetail.setRDQ(drlrec.getRdq());
                drldetail.setPRID(drlrec.getPrid());
                drldetail.setRDQTOTAL(drlrec.getRdqtotal());
                drldetail.setRECBY(drlrec.getRecby());
                drldetail.setRECTIME(drlrec.getRectime() == null ? null : Util.toXmlGregorianCalendar(drlrec.getRectime()));
                drldetail.setROTATEMETER(drlrec.getRotatemeter());
                drldetail.setRQ(drlrec.getRq());
                drldetail.setRQTOTAL(drlrec.getRqtotal());
                drldetail.setSTARTREC(drlrec.getStartrec());

                res.getDrillingRecord().add(drldetail);
            }

            res.setStatus(Constant.STATUS_CODE.OK);
        } catch (Exception e) {
            Util.handleException(e, res);
        }
        res.setResponseDateTime(Util.toXmlGregorianCalendar(now));
        Logger.LogRes(key, opr, res);
        return res;
    }

    /**
     * Implementation method for operation ModifyDrillingRecord
     *
     * @param req
     * @return
     */
    public static ModifyDrillingRecordRes modifyDrillingRecord(ModifyDrillingRecordReq req) {
        String key = UUID.randomUUID().toString();
        String opr = "PileConstruction/ModifyDrillingRecord";
        Logger.LogReq(key, opr, req);

        Date now = new Date();
        ModifyDrillingRecordRes res = new ModifyDrillingRecordRes();
        if (!Util.validateRequest(req, opr, Constant.FUNCTIONALITY_ACTION.WS_INVOKE, res)) {
            Logger.LogRes(key, opr, res);
            return res;
        }

        try {
            HibernateUtil.beginTransaction();

            DrillingRecordDetailType drldetail = req.getDrillingRecordDetail();

            // validate current drilling record status
            // do not allow to modify if record was already approved
            String drid = drldetail.getDRID();
            List<Drlrecmemo> listDrl = GenericHql.INSTANCE.query("from Drlrecmemo where drid=:id", "id", drid);
            if (listDrl.isEmpty()) {
                throw new PCMException("", "Requested drilling record id (DRID) is not valid: " + drid);
            }

            Drlrecmemo drlmemo = listDrl.get(0);
            if ("A".equalsIgnoreCase(drlmemo.getAppsts())) {
                throw new PCMException("", "Current drilling record has been approved. It can not be modified.");
            }

            // update new information
            // drlmemo.setId(new DrlrecmemoId(drldetail.getDRID(), Util.toDate(drldetail.getDRILLTIME())));
            drlmemo.setPpid(drldetail.getPPID());
            drlmemo.setAmp(drldetail.getAMP());
            drlmemo.setDeepmeter(drldetail.getDEEPMETER());
            drlmemo.setDirection(drldetail.getDIRECTION());
            drlmemo.setDmid(drldetail.getDMID());
            drlmemo.setDrillmeter(drldetail.getDRILLMETER());
            drlmemo.setEmptydrill(drldetail.isEMPTYDRILL());
            drlmemo.setEndrec(drldetail.isENDREC());
            drlmemo.setNdn(drldetail.getNDN());
            drlmemo.setPrid(drldetail.getPRID());
            drlmemo.setPsr(drldetail.getPSR());
            drlmemo.setRdq(drldetail.getRDQ());
            drlmemo.setRdqtotal(drldetail.getRDQTOTAL());
            drlmemo.setRecby(drldetail.getRECBY());
            drlmemo.setRectime(Util.toDate(drldetail.getRECTIME()));
            drlmemo.setRotatemeter(drldetail.getROTATEMETER());
            drlmemo.setRq(drldetail.getRQ());
            drlmemo.setRqtotal(drldetail.getRQTOTAL());
            drlmemo.setStartrec(drldetail.isSTARTREC());

            HibernateUtil.currentSession().save(drlmemo);
            HibernateUtil.commit();

            res.setStatus(Constant.STATUS_CODE.OK);
        } catch (Exception e) {
            HibernateUtil.rollback();
            Util.handleException(e, res);
        }
        res.setResponseDateTime(Util.toXmlGregorianCalendar(now));
        Logger.LogRes(key, opr, res);
        return res;
    }

    /**
     * Implementation method for operation ApproveDrillingRecord
     *
     * @param req
     * @return
     */
    public static ApproveDrillingRecordRes approveDrillingRecord(ApproveDrillingRecordReq req) {

        String key = UUID.randomUUID().toString();
        String opr = "PileConstruction/ApproveDrillingRecord";
        Logger.LogReq(key, opr, req);

        Date now = new Date();
        ApproveDrillingRecordRes res = new ApproveDrillingRecordRes();
        if (!Util.validateRequest(req, opr, Constant.FUNCTIONALITY_ACTION.WS_INVOKE, res)) {
            Logger.LogRes(key, opr, res);
            return res;
        }

        try {
            String token = req.getToken();
            List<User> t = GenericHql.INSTANCE.query("from User where token=:token", "token", token);
            if (t.isEmpty()) {
                throw new PCMException("Token is not valid", Constant.STATUS_CODE.ERR_USER_TOKEN_INVALID);
            }
            String approvalId = t.get(0).getUserid();
            Date approvalTime = Util.toDate(req.getApprovalDatetime());
            int totalRecUpdated = 0;

            HibernateUtil.beginTransaction();

            for (String drid : req.getDrillingRecords().getDRID()) {
                int cnt = GenericHql.INSTANCE.update("Update Drlrecmemo set appby=:appby, apptime=:apptime, appsts='A' where drid=:drid",
                        false,
                        "appby", approvalId,
                        "apptime", approvalTime,
                        "drid", drid);
                totalRecUpdated += cnt;
            }
            HibernateUtil.commit();
            res.setStatus(Constant.STATUS_CODE.OK);
        } catch (Exception e) {
            HibernateUtil.rollback();
            Util.handleException(e, res);
        }
        res.setResponseDateTime(Util.toXmlGregorianCalendar(now));
        Logger.LogRes(key, opr, res);
        return res;
    }
}
