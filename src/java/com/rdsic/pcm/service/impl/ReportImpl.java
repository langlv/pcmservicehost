/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdsic.pcm.service.impl;

import com.rdsic.pcm.common.Constant;
import com.rdsic.pcm.common.GenericHql;
import com.rdsic.pcm.common.Util;
import com.rdsic.pcm.data.entity.Reportdef;
import com.rdsic.pcm.data.entity.Reportparam;
import com.rdsic.pileconstructionmanagement.type.reporting.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.json.JSONObject;

/**
 * Implementation class for Report service
 *
 * @author langl
 */
public class ReportImpl {

    private static final String PARAM_SEPARATOR = ",";

    /**
     * Implementation method for operation RunDayend
     *
     * @param req
     * @return
     */
    public static DayendRes runDayend(DayendReq req) {
        String key = UUID.randomUUID().toString();
        String opr = "Report/RunDayend";
        ServiceLogger.LogReq(key, opr, req);

        Date now = new Date();
        DayendRes res = new DayendRes();
        if (!Util.validateRequest(req, opr, Constant.FUNCTIONALITY_ACTION.WS_INVOKE, res)) {
            ServiceLogger.LogRes(key, opr, res);
            return res;
        }

        try {
            String defaultProc = "DAYEND";
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            String defaultParam = "CURRDT=" + formatter.format(Util.toDate(req.getWorkingDate()));

            String result = execProc(defaultProc, defaultParam);

            res.setStatus(Constant.STATUS_CODE.OK);
            res.setErrorMessage(result);
        } catch (Exception e) {
            Util.handleException(e, res);
        }
        res.setResponseDateTime(Util.toXmlGregorianCalendar(now));
        ServiceLogger.LogRes(key, opr, res);
        return res;
    }

    /**
     * Implementation method for operation ExecReport
     *
     * @param req
     * @return
     */
    public static ExecReportRes execReport(ExecReportReq req) {
        String key = UUID.randomUUID().toString();
        String opr = "Report/ExecReport";
        ServiceLogger.LogReq(key, opr, req);

        Date now = new Date();
        ExecReportRes res = new ExecReportRes();
        if (!Util.validateRequest(req, opr, Constant.FUNCTIONALITY_ACTION.WS_INVOKE, res)) {
            ServiceLogger.LogRes(key, opr, res);
            return res;
        }

        try {
            // TODO: implementation code here
            String rptName = req.getReportName();
            String rptParam = req.getParameter();

            String rptData = execProc(rptName, rptParam);

            res.setData(rptData);
            res.setStatus(Constant.STATUS_CODE.OK);
        } catch (Exception e) {
            Util.handleException(e, res);
        }
        res.setResponseDateTime(Util.toXmlGregorianCalendar(now));
        ServiceLogger.LogRes(key, opr, res);
        return res;
    }

    /**
     * Prepare parameters for report Input in format:
     * param1=val1#param2=val2#....
     *
     * @param rptName
     * @param paramString
     * @return
     * @throws PCMException
     */
    private static String prepareParameter(String rptName, String paramString) throws PCMException {
        String resParam = "";

        List<Reportdef> listRpt = GenericHql.INSTANCE.query("from Reportdef where code=:rptname", "rptname", rptName);
        if (listRpt.isEmpty()) {
            throw new PCMException("The report name was not defined: " + rptName, Constant.STATUS_CODE.ERR_INVALID_INPUT_DATA);
        }

        List<Reportparam> listParam = GenericHql.INSTANCE.query("from Reportparam where code=:rptname and pardir=:pardir order by ord", "rptname", rptName, "pardir", "IN");
        Map<String, Reportparam> paramDef = new HashMap<>();
        Map<String, String> params = new HashMap<>();

        for (Reportparam p : listParam) {
            paramDef.put(p.getId().getParname(), p);
            params.put(p.getId().getParname(), ""); // need to provide default value ?
        }

        for (String par : paramString.split(PARAM_SEPARATOR)) {
            String[] p = par.split("=");
            if (p.length < 2) {
                throw new PCMException("The input parameter was not in correct format: " + par, Constant.STATUS_CODE.ERR_INVALID_INPUT_DATA);
            }

            String parName = p[0];
            String parVal = p[1];

            if (!paramDef.containsKey(parName)) {
                throw new PCMException("Parameter was not defined: " + par, Constant.STATUS_CODE.ERR_INVALID_INPUT_DATA);
            }
            Reportparam parDef = paramDef.get(parName);

            // try to validate data type
            try {
                switch (parDef.getPartype().toLowerCase()) {
                    case "int":
                        int intVall = Integer.parseInt(parVal);
                    case "double":
                        double doubleVal = Double.parseDouble(parVal);
                    case "date":
                        SimpleDateFormat sdf = new SimpleDateFormat(parDef.getFormat());
                        Date dateVal = sdf.parse(parVal);
                    default:
                    // do nothing
                }
            } catch (NumberFormatException | ParseException e) {
                throw new PCMException("The input parameter value was not correct format and datatype: " + par, Constant.STATUS_CODE.ERR_INVALID_INPUT_DATA);
            }

            params.put(parName, parVal);
        }

        for (String k : params.keySet()) {
            resParam += k + "=" + params.get(k) + PARAM_SEPARATOR;
        }

        return resParam.substring(0, resParam.length() - 1);
    }

    /**
     * Execute a stored procedure in DB by providing parameter list in format:
     * par1=val1#par2=val2#...
     *
     * @param proc
     * @param parameter
     * @return
     */
    private static String execProc(String proc, String parameter) throws PCMException {
        String param = prepareParameter(proc, parameter);

        System.out.println(String.format("Calling proc [%s] with parameter [%s]", proc, parameter));

        List<Map<String, Object>> data = GenericHql.INSTANCE.querySQL(String.format(" CALL %s(:param) ", proc), -1, "param", param);

        // this setting for all null object to empty string to advoid item missing in output json
        for (Map<String, Object> m : data) {
            for (String k : m.keySet()) {
                if (m.get(k) == null) {
                    m.put(k, "");
                }
            }
        }
        JSONObject json = new JSONObject();
        json.put("items", data);

        return json.toString();
    }

}
