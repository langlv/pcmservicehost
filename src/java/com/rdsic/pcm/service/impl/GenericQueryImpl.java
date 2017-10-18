/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdsic.pcm.service.impl;

import com.rdsic.pcm.common.Util;
import com.rdsic.pcm.common.Constant;
import com.rdsic.pcm.common.GenericHql;
import com.rdsic.pcm.common.Configuration;
import com.rdsic.pileconstructionmanagement.type.genericquery.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.json.JSONObject;

/**
 *
 * @author langl
 */
public class GenericQueryImpl {

    /**
     * Implementation method for operation Select
     *
     * @param req
     * @return
     */
    public static SelectQueryRes select(SelectQueryReq req) {
        String key = UUID.randomUUID().toString();
        String opr = "GenericQuery/Select";
        ServiceLogger.LogReq(key, opr, req);

        Date now = new Date();
        SelectQueryRes res = new SelectQueryRes();
        if (!Util.validateRequest(req, opr, Constant.FUNCTIONALITY_ACTION.WS_INVOKE, res)) {
            ServiceLogger.LogRes(key, opr, res);
            return res;
        }

        try {
            int maxRow = req.getQuery().getMaxRowCount() == null ? Configuration.getInt(Constant.CONFIG_KEY.PCM_QUERY_MAX_ROW) : req.getQuery().getMaxRowCount();
            String sql = req.getQuery().getSQL();

            List<Object> params = new ArrayList<>();

            if (req.getQuery().getParams() != null) {
                if (req.getQuery().getParams().getDatetimeParam() != null) {
                    req.getQuery().getParams().getDatetimeParam().stream().map((p) -> {
                        params.add(p.getName());
                        return p;
                    }).forEach((p) -> {
                        params.add(Util.toDate(p.getValue()));
                    });
                }

                if (req.getQuery().getParams().getNumericParam() != null) {
                    req.getQuery().getParams().getNumericParam().stream().map((p) -> {
                        params.add(p.getName());
                        return p;
                    }).forEach((p) -> {
                        params.add(p.getValue());
                    });
                }

                if (req.getQuery().getParams().getStringParam() != null) {
                    req.getQuery().getParams().getStringParam().stream().map((p) -> {
                        params.add(p.getName());
                        return p;
                    }).forEach((p) -> {
                        params.add(p.getValue());
                    });
                }
            }
            // execute the query
            List<Map<String, Object>> list = GenericHql.INSTANCE.querySQL(sql, maxRow, params.toArray());

            // this setting for all null object to empty string to advoid item missing in output json
            for (Map<String, Object> m : list) {
                for (String k : m.keySet()) {
                    if (m.get(k) == null) {
                        m.put(k, "");
                    }
                }
            }
            JSONObject json = new JSONObject();
            json.put("items", list);

            SelectQueryResponseType response = new SelectQueryResponseType();
            response.setRecordCount(list.size());
            response.setJSONData(json.toString());

            res.setDataSet(response);

            res.setStatus(Constant.STATUS_CODE.OK);
        } catch (Exception e) {
            Util.handleException(e, res);
        }

        res.setResponseDateTime(Util.toXmlGregorianCalendar(now));
        ServiceLogger.LogRes(key, opr, res);
        return res;

    }

    /**
     * Implementation method for operation AddOrUpdate
     *
     * @param req
     * @return
     */
    public static AddOrUpdateRes addOrUpdate(AddOrUpdateReq req) {
        String key = UUID.randomUUID().toString();
        String opr = "GenericQuery/AddOrUpdate";
        ServiceLogger.LogReq(key, opr, req);

        Date now = new Date();
        AddOrUpdateRes res = new AddOrUpdateRes();
        if (!Util.validateRequest(req, opr, Constant.FUNCTIONALITY_ACTION.WS_INVOKE, res)) {
            ServiceLogger.LogRes(key, opr, res);
            return res;
        }

        try {
            List<Object> params = new ArrayList<>();

            if (req.getQuery().getParams() != null) {

                if (req.getQuery().getParams().getDatetimeParam() != null) {
                    for (QueryParamType.DatetimeParam dp : req.getQuery().getParams().getDatetimeParam()) {
                        params.add(dp.getName());
                        params.add(Util.toDate(dp.getValue()));
                    }
                }

                if (req.getQuery().getParams().getNumericParam() != null) {
                    for (QueryParamType.NumericParam np : req.getQuery().getParams().getNumericParam()) {
                        params.add(np.getName());
                        params.add(np.getValue());
                    }
                }

                if (req.getQuery().getParams().getStringParam() != null) {
                    for (QueryParamType.StringParam np : req.getQuery().getParams().getStringParam()) {
                        params.add(np.getName());
                        params.add(np.getValue());
                    }
                }
            }
            String sql = req.getQuery().getSQL().trim();
            String action = sql.substring(0, sql.indexOf(" ")).toLowerCase();

            // quick validate the query
            if (!("insert".equalsIgnoreCase(action) || "update".equalsIgnoreCase(action) || "delete".equalsIgnoreCase(action))) {
                throw new SQLException("Invalid SQL string. The input SQL must be started with insert/update/delete");
            }

            int recNum = GenericHql.INSTANCE.updateSQL(sql, true, params.toArray());

            AddOrUpdateQueryResponseType result = new AddOrUpdateQueryResponseType();
            result.setUpdatedAction(action);
            result.setUpdatedRecordCount(recNum);

            res.setResult(result);
            res.setStatus(Constant.STATUS_CODE.OK);

        } catch (Exception e) {
            res.setStatus(Constant.STATUS_CODE.FAIL);
            res.setErrorCode(Constant.STATUS_CODE.ERR_UPDATE_FAIL);
            res.setErrorMessage(e.getMessage());
            e.printStackTrace();
        }

        res.setResponseDateTime(Util.toXmlGregorianCalendar(now));
        ServiceLogger.LogRes(key, opr, res);
        return res;
    }
}
