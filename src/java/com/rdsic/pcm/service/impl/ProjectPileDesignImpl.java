/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdsic.pcm.service.impl;

import com.rdsic.pcm.common.Util;
import com.rdsic.pcm.common.HibernateUtil;
import com.rdsic.pcm.common.Constant;
import com.rdsic.pcm.common.GenericHql;
import com.rdsic.pcm.data.entity.Drillingmachine;
import com.rdsic.pcm.data.entity.Employee;
import com.rdsic.pcm.data.entity.Mixingplant;
import com.rdsic.pcm.data.entity.Pcproject;
import com.rdsic.pcm.data.entity.Piledesign;
import com.rdsic.pcm.data.entity.Pileplan;
import com.rdsic.pcm.data.entity.Projectasset;
import com.rdsic.pcm.data.entity.Silo;
import com.rdsic.pcm.data.entity.VUserinformation;
import com.rdsic.pileconstructionmanagement.type.projectpiledesign.*;
import com.rdsic.pileconstructionmanagement.type.usermanagement.user.UserSummaryType;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 * @author langl
 */
public class ProjectPileDesignImpl {

    /**
     * Save pile design from XML data type
     *
     * @param pile
     * @param immediateCommit
     * @return
     * @throws Exception
     */
    public static String savePileDesign(PileDesignType pile, boolean immediateCommit)
            throws Exception {
        if (pile == null) {
            throw new Exception("Object is null");
        }
        String code = pile.getCODE();
        try {
            List<Piledesign> list = GenericHql.INSTANCE.query("from Piledesign where code=:code", new Object[]{"code", code});
            Piledesign pd = list.isEmpty() ? new Piledesign() : list.get(0);

            pd.setCode(code);
            pd.setDiameter(pile.getDIAMETER());
            pd.setEl0(pile.getEL0());
            pd.setEl1(pile.getEL1());
            pd.setEl2(pile.getEL2());
            pd.setEps(pile.getEPS());
            pd.setLd(pile.getLD());
            pd.setLp(pile.getLP());
            pd.setN(pile.getN());
            pd.setName(pile.getNAME());
            pd.setNlim(pile.getNLIM());
            pd.setQ(pile.getQ());
            pd.setQlim(pile.getQLIM());
            pd.setQtotal(pile.getQTOTAL());
            pd.setC(pile.getC());
            pd.setWc(pile.getWC());

            HibernateUtil.beginTransaction();
            HibernateUtil.currentSession().save(pd);
            if (immediateCommit) {
                HibernateUtil.commit();
            }
        } catch (Exception e) {
            if (immediateCommit) {
                HibernateUtil.rollback();
            }
            e.printStackTrace();
        }
        return code;
    }

    /**
     * Implementation method for operation GetProjectDetail
     *
     * @param req
     * @return
     */
    public static GetProjectDetailRes getProjectDetail(GetProjectDetailReq req) {
        String key = UUID.randomUUID().toString();
        String opr = "ProjectPileDesign/GetProjectDetail";
        Logger.LogReq(key, opr, req);

        Date now = new Date();
        GetProjectDetailRes res = new GetProjectDetailRes();
        if (!Util.validateRequest(req, opr, Constant.FUNCTIONALITY_ACTION.WS_INVOKE, res)) {
            Logger.LogRes(key, opr, res);
            return res;
        }
        try {
            String prCode = req.getProject().getCode();
            List<Pcproject> list = GenericHql.INSTANCE.query("From Pcproject where code=:code", new Object[]{"code", prCode});
            if (list.isEmpty()) {
                throw new PCMException("Record not found with project code " + prCode, Constant.STATUS_CODE.ERR_NO_RECORD_FOUND);
            }
            Pcproject project = (Pcproject) list.get(0);

            PCProjectType projectType = new PCProjectType();
            projectType.setPRID(project.getPrid());
            projectType.setCODE(project.getCode());
            projectType.setINVNAME(project.getInvname());
            projectType.setIMPLNAME(project.getImplname());
            projectType.setPMNAME(project.getPmname());
            projectType.setADDRESS(project.getAddress());
            projectType.setSTART(project.getStart() == null ? null : Util.toXmlGregorianCalendar(project.getStart()));
            projectType.setFINISH(project.getFinish() == null ? null : Util.toXmlGregorianCalendar(project.getFinish()));
            projectType.setACTSTART(project.getActstart() == null ? null : Util.toXmlGregorianCalendar(project.getActstart()));
            projectType.setACTFINISH(project.getActfinish() == null ? null : Util.toXmlGregorianCalendar(project.getActfinish()));
            projectType.setNPM(project.getNpm());
            projectType.setNPMLIM(project.getNpmlim());
            projectType.setEPS(project.getEps());

            GetProjectDetailResType detail = new GetProjectDetailResType();
            detail.setProjectDetail(projectType);

            res.setData(detail);
            res.setStatus(Constant.STATUS_CODE.OK);
        } catch (Exception e) {
            Util.handleException(e, res);
        }
        res.setResponseDateTime(Util.toXmlGregorianCalendar(now));
        Logger.LogRes(key, opr, res);
        return res;
    }

    /**
     * Implementation method for operation AddOrUpdateProject
     *
     * @param req
     * @return
     */
    public static AddOrUpdateProjectRes addOrUpdateProject(AddOrUpdateProjectReq req) {
        String key = UUID.randomUUID().toString();
        String opr = "ProjectPileDesign/AddOrUpdateProject";
        Logger.LogReq(key, opr, req);

        Date now = new Date();

        AddOrUpdateProjectRes res = new AddOrUpdateProjectRes();
        if (!Util.validateRequest(req, opr, Constant.FUNCTIONALITY_ACTION.WS_INVOKE, res)) {
            Logger.LogRes(key, opr, res);
            return res;
        }
        try {
            String projectCode = req.getData().getProjectDetail().getCODE();
            List<Pcproject> list = GenericHql.INSTANCE.query("from Pcproject where code=:code", new Object[]{"code", projectCode});
            int prId = list.isEmpty() ? 0 : ((Pcproject) list.get(0)).getPrid();

            HibernateUtil.beginTransaction();

            PCProjectType detail = req.getData().getProjectDetail();
            Pcproject p = new Pcproject();
            if (prId != 0) {
                p.setPrid(prId);
            }
            p.setCode(projectCode);
            p.setName(detail.getNAME());
            p.setInvname(detail.getINVNAME());
            p.setImplname(detail.getIMPLNAME());
            p.setPmname(detail.getPMNAME());
            p.setAddress(detail.getADDRESS());
            p.setStart(detail.getSTART() == null ? null : Util.toDate(detail.getSTART()));
            p.setFinish(detail.getFINISH() == null ? null : Util.toDate(detail.getFINISH()));
            p.setActstart(detail.getACTSTART() == null ? null : Util.toDate(detail.getACTSTART()));
            p.setActfinish(detail.getACTFINISH() == null ? null : Util.toDate(detail.getACTFINISH()));
            p.setNpm(detail.getNPM());
            p.setNpmlim(detail.getNPMLIM());
            p.setEps(detail.getEPS());

            HibernateUtil.currentSession().save(p);
            HibernateUtil.commit();

            res.setPRID(p.getPrid());
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
     * Implementation method for operation GetPileDesign
     *
     * @param req
     * @return
     */
    public static GetPileDesignRes getPileDesign(GetPileDesignReq req) {
        String key = UUID.randomUUID().toString();
        String opr = "ProjectPileDesign/GetPileDesign";
        Logger.LogReq(key, opr, req);

        Date now = new Date();
        GetPileDesignRes res = new GetPileDesignRes();
        if (!Util.validateRequest(req, opr, Constant.FUNCTIONALITY_ACTION.WS_INVOKE, res)) {
            Logger.LogRes(key, opr, res);
            return res;
        }
        try {
            String code = req.getPileDesign().getPileCode();
            List<Piledesign> list = GenericHql.INSTANCE.query("From Piledesign where code=:code", new Object[]{"code", code});
            if (list.isEmpty()) {
                throw new PCMException("Record not found with pile design code " + code, Constant.STATUS_CODE.ERR_NO_RECORD_FOUND);
            }
            Piledesign pd = (Piledesign) list.get(0);
            PileDesignType pile = new PileDesignType();

            pile.setCODE(pd.getCode());
            pile.setDIAMETER(pd.getDiameter());
            pile.setEL0(pd.getEl0());
            pile.setEL1(pd.getEl1());
            pile.setEL2(pd.getEl2());
            pile.setEPS(pd.getEps());
            pile.setLD(pd.getLd());
            pile.setLP(pd.getLp());
            pile.setN(pd.getN());
            pile.setNAME(pd.getName());
            pile.setNLIM(pd.getNlim());
//            pile.setPID(pd.getPid());
            pile.setQ(pd.getQ());
            pile.setQLIM(pd.getQlim());
            pile.setQTOTAL(pd.getQtotal());
            pile.setCADFILE("");
            pile.setC(pd.getC());
            pile.setWC(pd.getWc());

            GetPileDesignResType pileDesign = new GetPileDesignResType();
            pileDesign.setPileDesign(pile);

            res.setData(pileDesign);
            res.setStatus(Constant.STATUS_CODE.OK);
        } catch (Exception e) {
            Util.handleException(e, res);
        }
        res.setResponseDateTime(Util.toXmlGregorianCalendar(now));
        Logger.LogRes(key, opr, res);
        return res;
    }

    /**
     * Implementation method for operation AddOrUpdatePileDesign
     *
     * @param req
     * @return
     */
    public static AddOrUpdatePileDesignRes addOrUpdatePileDesign(AddOrUpdatePileDesignReq req) {
        String key = UUID.randomUUID().toString();
        String opr = "ProjectPileDesign/AddOrUpdatePileDesign";
        Logger.LogReq(key, opr, req);

        Date now = new Date();

        AddOrUpdatePileDesignRes res = new AddOrUpdatePileDesignRes();
        if (!Util.validateRequest(req, opr, Constant.FUNCTIONALITY_ACTION.WS_INVOKE, res)) {
            Logger.LogRes(key, opr, res);
            return res;
        }
        try {
            String code = savePileDesign(req.getData().getPileDesign(), true);

            res.setCODE(code);
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
     * Implementation method for operation AddOrUpdateProjectPilePlan
     *
     * @param req
     * @return
     */
    public static AddOrUpdatePilePlanRes addOrUpdateProjectPilePlan(AddOrUpdatePilePlanReq req) {
        String key = UUID.randomUUID().toString();
        Date now = new Date();
        String opr = "ProjectPileDesign/AddOrUpdateProjectPilePlan";
        Logger.LogReq(key, opr, req);

        AddOrUpdatePilePlanRes res = new AddOrUpdatePilePlanRes();
        if (!Util.validateRequest(req, opr, Constant.FUNCTIONALITY_ACTION.WS_INVOKE, res)) {
            Logger.LogRes(key, opr, res);
            return res;
        }
        try {
            PilePlanType ppt = req.getData().getPilePlan();
            int prid = ppt.getPRID();
            String pcode = ppt.getCODE();
            if (prid <= 0) {
                throw new PCMException("Project id is required for adding pile construction plan", Constant.STATUS_CODE.ERR_DATA_REQUIRED);
            }
            List<Piledesign> prlist = GenericHql.INSTANCE.query("from Pcproject where prid=:prid", new Object[]{"prid", prid});
            if (prlist.isEmpty()) {
                throw new PCMException("Project id is not valid for adding or updating pile plan PRID=" + prid, Constant.STATUS_CODE.ERR_ID_INVALID);
            }
            HibernateUtil.beginTransaction();

            List<Piledesign> pdlist = GenericHql.INSTANCE.query("from Piledesign where code=:pcode", new Object[]{"pcode", pcode});
            if (pdlist.isEmpty()) {
                PileDesignType pd = new PileDesignType();
                pd.setCODE(Util.generateToken());
                pd.setDIAMETER(0.0D);
                pd.setEL0(ppt.getREL0());
                pd.setEL1(ppt.getREL1());
                pd.setEL2(ppt.getREL2());
                pd.setEPS(0.0D);
                pd.setLD(ppt.getRLD());
                pd.setLP(ppt.getRLP());
                pd.setN(ppt.getFN());
                pd.setNAME("Generated pile design");
                pd.setNLIM(ppt.getNMIN());
                pd.setQ(ppt.getRQ());
                pd.setQLIM(ppt.getRQ() != null ? ppt.getRQ() * 0.95D : 0.0D);
                pd.setQTOTAL(0.0D);

                pcode = savePileDesign(pd, false);
            }
            Pileplan pp = new Pileplan();
            if (ppt.getPPID() > 0) {
                pp.setPpid(ppt.getPPID());
            }
            pp.setActend(ppt.getACTEND() == null ? null : Util.toDate(ppt.getACTEND()));
            pp.setActstart(ppt.getACTSTART() == null ? null : Util.toDate(ppt.getACTSTART()));
            pp.setCementtype(ppt.getCEMENTTYPE());
            pp.setDmid(ppt.getDMID());
            pp.setDriver(ppt.getDRIVER());
            pp.setDspdavg(ppt.getDSPDAVG());
            pp.setDspdmax(ppt.getDSPDMAX());
            pp.setEnd(ppt.getEND() == null ? null : Util.toDate(ppt.getEND()));
            pp.setFl(ppt.getFL());
            pp.setFn(ppt.getFN());
            pp.setFq(ppt.getFQ());
            pp.setFs(ppt.getFS());
            pp.setMpid(ppt.getMPID());
            pp.setNav(ppt.getNAV());
            pp.setNmin(ppt.getNMIN());
            pp.setCode(pcode);
            pp.setPrid(prid);
            pp.setRel0(ppt.getREL0());
            pp.setRel1(ppt.getREL1());
            pp.setRel2(ppt.getREL2());
            pp.setRld(ppt.getRLD());
            pp.setRlp(ppt.getRLP());
            pp.setRq(ppt.getRQ());
            pp.setRx(ppt.getRX());
            pp.setRy(ppt.getRY());
            pp.setSbid(ppt.getSBID());
            pp.setSid(ppt.getSID());
            pp.setStart(ppt.getSTART() == null ? null : Util.toDate(ppt.getSTART()));
            pp.setTeam(ppt.getTEAM());
            pp.setUspdavg(ppt.getUSPDAVG());
            pp.setUspdmax(ppt.getUSPDMAX());
            pp.setVertically(ppt.getVERTICALLY());
            pp.setX(ppt.getX());
            pp.setY(ppt.getY());
            pp.setStatus(ppt.getSTATUS());

            HibernateUtil.currentSession().save(pp);
            HibernateUtil.commit();

            res.setPPID(pp.getPpid());
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
     * Implementation method for operation GetProjectPilePlan
     *
     * @param req
     * @return
     */
    public static GetPilePlanDetailRes getPilePlanDetail(GetPilePlanDetailReq req) {
        String key = UUID.randomUUID().toString();
        String opr = "ProjectPileDesign/GetProjectPilePlan";
        Logger.LogReq(key, opr, req);

        Date now = new Date();
        GetPilePlanDetailRes res = new GetPilePlanDetailRes();
        if (!Util.validateRequest(req, opr, Constant.FUNCTIONALITY_ACTION.WS_INVOKE, res)) {
            Logger.LogRes(key, opr, res);
            return res;
        }
        try {
            int ppid = req.getPilePlan().getPPID() != null ? req.getPilePlan().getPPID() : -1;
            int prid = req.getPilePlan().getPlan() != null ? req.getPilePlan().getPlan().getPRID() : -1;
            String pcode = req.getPilePlan().getPlan() != null ? req.getPilePlan().getPlan().getCODE() : "";
            List<Pileplan> list;
            if (ppid >= 0) {
                list = GenericHql.INSTANCE.query("from Pileplan where ppid=:ppid", new Object[]{"ppid", ppid});
            } else {
                list = GenericHql.INSTANCE.query("from Pileplan where prid=:prid and code=:pcode", new Object[]{"prid", prid, "pcode", pcode});
            }
            if (list.isEmpty()) {
                throw new PCMException("No record found with given pile plan id or project id", Constant.STATUS_CODE.ERR_NO_RECORD_FOUND);
            }
            GetPilePlanDetailResType detail = new GetPilePlanDetailResType();
            for (Pileplan pp : list) {
                PilePlanType ppt = new PilePlanType();
                ppt.setACTEND(pp.getActend() == null ? null : Util.toXmlGregorianCalendar(pp.getActend()));
                ppt.setACTSTART(pp.getActstart() == null ? null : Util.toXmlGregorianCalendar(pp.getActstart()));
                ppt.setCEMENTTYPE(pp.getCementtype());
                ppt.setDMID(pp.getDmid());
                ppt.setDRIVER(pp.getDriver());
                ppt.setDSPDAVG(pp.getDspdavg());
                ppt.setDSPDMAX(pp.getDspdmax());
                ppt.setEND(pp.getEnd() == null ? null : Util.toXmlGregorianCalendar(pp.getEnd()));
                ppt.setFL(pp.getFl());
                ppt.setFN(pp.getFn());
                ppt.setFQ(pp.getFq());
                ppt.setFS(pp.getFs());
                ppt.setMPID(pp.getMpid());
                ppt.setNAV(pp.getNav());
                ppt.setNMIN(pp.getNmin());
                ppt.setCODE(pp.getCode());
                ppt.setPPID(pp.getPpid());
                ppt.setPRID(pp.getPrid());
                ppt.setREL0(pp.getRel0());
                ppt.setREL1(pp.getRel1());
                ppt.setREL2(pp.getRel2());
                ppt.setRLD(pp.getRld());
                ppt.setRLP(pp.getRlp());
                ppt.setRQ(pp.getRq());
                ppt.setRX(pp.getRx());
                ppt.setRY(pp.getRy());
                ppt.setSBID(pp.getSbid());
                ppt.setSID(pp.getSid());
                ppt.setSTART(pp.getStart() == null ? null : Util.toXmlGregorianCalendar(pp.getStart()));
                ppt.setSTATUS(pp.getStatus());
                ppt.setTEAM(pp.getTeam());
                ppt.setUSPDAVG(pp.getUspdavg());
                ppt.setUSPDMAX(pp.getUspdmax());
                ppt.setVERTICALLY(pp.getVertically());
                ppt.setX(pp.getX());
                ppt.setY(pp.getY());

                detail.getPilePlan().add(ppt);
            }
            res.setData(detail);

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
     * Implementation method for operation GetProjectAssetSummary
     *
     * @param req
     * @return
     */
    public static GetProjectAssetSummaryRes getProjectAssetSummary(GetProjectAssetSummaryReq req) {
        String key = UUID.randomUUID().toString();
        String opr = "ProjectPileDesign/GetProjectAssetSummary";
        Logger.LogReq(key, opr, req);

        Date now = new Date();
        GetProjectAssetSummaryRes res = new GetProjectAssetSummaryRes();
        if (!Util.validateRequest(req, opr, Constant.FUNCTIONALITY_ACTION.WS_INVOKE, res)) {
            Logger.LogRes(key, opr, res);
            return res;
        }

        try {
            int prid = req.getPRID();

            List<Projectasset> listAss = GenericHql.INSTANCE.query("from Projectasset where prid=:prid", "prid", prid);
            if (listAss.isEmpty()) {
                throw new PCMException("Project id is not correct or project asset was not defined", Constant.STATUS_CODE.ERR_NO_RECORD_FOUND);
            }

            Projectasset prAs = listAss.get(0);
            ProjectAssetSummaryType prjAsset = new ProjectAssetSummaryType();
            String ID_SEPARATOR = ",";

            // get silos
            if (prAs.getSilo() != null) {
                String[] siloIdListStr = prAs.getSilo().split(ID_SEPARATOR);
                Integer[] siloIdList = new Integer[siloIdListStr.length];
                for (int i = 0; i < siloIdListStr.length; i++) {
                    siloIdList[i] = Integer.parseInt(siloIdListStr[i]);
                }

                List<Silo> listSilo = GenericHql.INSTANCE.queryWithParamList("from Silo where sid in (:sidlist)", "sidlist", siloIdList);
                ProjectAssetSummaryType.Silos silos = new ProjectAssetSummaryType.Silos();
                for (Silo sl : listSilo) {
                    if (sl != null) {
                        SiloType st = new SiloType();
                        st.setSID(sl.getSid());
                        st.setNAME(sl.getName());
                        st.setLASTUPDATED(sl.getLastupdated() == null ? null : Util.toXmlGregorianCalendar(sl.getLastupdated()));
                        st.setDESIGNVOL(sl.getDesignvol());
                        st.setCODE(sl.getCode());
                        st.setACTUALVOL(sl.getActualvol());
                        st.setACTUALCM(sl.getActualcm());
                        silos.getSilo().add(st);
                    }
                }
                prjAsset.setSilos(silos);

            }

            // get employee
            if (prAs.getEmp() != null) {
                String[] empIdListStr = prAs.getEmp().split(ID_SEPARATOR);
                Integer[] empIdList = new Integer[empIdListStr.length];
                for (int i = 0; i < empIdListStr.length; i++) {
                    empIdList[i] = Integer.parseInt(empIdListStr[i]);
                }

                List<Employee> listEmp = GenericHql.INSTANCE.queryWithParamList("from Employee where eid in (:eidlist)", "eidlist", empIdList);
                ProjectAssetSummaryType.Employees emp = new ProjectAssetSummaryType.Employees();
                for (Employee e : listEmp) {
                    if (e != null) {
                        EmployeeType et = new EmployeeType();
                        et.setEID(e.getEid());
                        et.setFULLNAME(e.getFullname());
                        et.setROLE(e.getRole());
                        et.setSHORTNAME(e.getShortname());
                        et.setTEAM(e.getTeam());
                        et.setUSERID(e.getUserid());
                        emp.getEmployee().add(et);
                    }
                }

                prjAsset.setEmployees(emp);
            }

            // get mixing plant
            if (prAs.getMp() != null) {
                String[] mpIdListStr = prAs.getMp().split(ID_SEPARATOR);
                Integer[] mpIdList = new Integer[mpIdListStr.length];
                for (int i = 0; i < mpIdListStr.length; i++) {
                    mpIdList[i] = Integer.parseInt(mpIdListStr[i]);
                }

                List<Mixingplant> listMp = GenericHql.INSTANCE.queryWithParamList("from Mixingplant where mpid in (:mplist)", "mplist", mpIdList);
                ProjectAssetSummaryType.MixingPlants mixingPlant = new ProjectAssetSummaryType.MixingPlants();
                for (Mixingplant mp : listMp) {
                    if (mp != null) {
                        MixingPlantType mptype = new MixingPlantType();
                        mptype.setCODE(mp.getCode());
                        mptype.setMPID(mp.getMpid());
                        mptype.setNAME(mp.getName());
                        mptype.setSID1(mp.getSid1());
                        mptype.setSID2(mp.getSid2());
                        mptype.setSID3(mp.getSid3());
                        mptype.setSID4(mp.getSid4());
                        mptype.setSID5(mp.getSid5());
                        mixingPlant.getMixingPlant().add(mptype);
                    }
                }

                prjAsset.setMixingPlants(mixingPlant);
            }

            // get driling machine
            if (prAs.getDm() != null) {
                String[] dmIdListStr = prAs.getDm().split(ID_SEPARATOR);
                Integer[] dmIdList = new Integer[dmIdListStr.length];
                for (int i = 0; i < dmIdListStr.length; i++) {
                    dmIdList[i] = Integer.parseInt(dmIdListStr[i]);
                }

                List<Drillingmachine> listDm = GenericHql.INSTANCE.queryWithParamList("from Drillingmachine where dmid in (:dmidlist)", "dmidlist", dmIdList);
                ProjectAssetSummaryType.DrillingMachines drillingMachine = new ProjectAssetSummaryType.DrillingMachines();
                for (Drillingmachine dm : listDm) {
                    if (dm != null) {
                        DrillingMachineType d = new DrillingMachineType();
                        d.setALIM(dm.getAlim());
                        d.setCODE(dm.getCode());
                        d.setCUTWING(dm.getCutwing());
                        d.setDMID(dm.getDmid());
                        d.setDRIVER1(dm.getDriver1());
                        d.setDRIVER2(dm.getDriver2());
                        d.setFMID1(dm.getFmid1());
                        d.setFMID2(dm.getFmid2());
                        d.setMPID(dm.getMpid());
                        d.setNAME(dm.getName());
                        d.setVLIM(dm.getVlim());

                        drillingMachine.getDrillingMachine().add(d);
                    }
                }
                prjAsset.setDrillingMachines(drillingMachine);
            }

            // get users
            if (prAs.getUser() != null) {
                String[] userIdList = prAs.getUser().split(ID_SEPARATOR);
                List<VUserinformation> listUser = GenericHql.INSTANCE.queryWithParamList("from VUserinformation where userid in (:useridlist)", "useridlist", userIdList);
                ProjectAssetSummaryType.Users users = new ProjectAssetSummaryType.Users();
                for (VUserinformation user : listUser) {
                    if (user != null) {
                        UserSummaryType us = new UserSummaryType();
                        us.setUserID(user.getId().getUserId());
                        us.setDateCreate(Util.toXmlGregorianCalendar(user.getId().getDateCreate()));
                        us.setDateExpire(Util.toXmlGregorianCalendar(user.getId().getDateExpire()));
                        us.setFirstName(user.getId().getFirstName());
                        us.setLastName(user.getId().getLastName());
                        us.setUserClass(user.getId().getUserClass());

                        users.getUser().add(us);
                    }
                }
                prjAsset.setUsers(users);
            }

            res.setProjectAsset(prjAsset);
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
