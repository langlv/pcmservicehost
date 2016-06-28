/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdsic.pcm.service.impl;

import com.rdsic.pcm.common.HibernateUtil;
import com.rdsic.pcm.common.Util;
import com.rdsic.pcm.data.entity.Dlog;
import com.rdsic.pcm.data.entity.DlogId;
import com.rdsic.pileconstructionmanagement.type.common.service.BaseReq;
import com.rdsic.pileconstructionmanagement.type.common.service.BaseRes;
import java.util.Date;

/**
 *
 * @author langl
 */
public class Logger {

    public static final org.apache.log4j.Logger defaultLogger = org.apache.log4j.Logger.getLogger(Logger.class);

    public static void LogReq(String key, String action, BaseReq req) {
        Util.executorService.submit(() -> {
            String raw = Util.toXMLString(req);
            String userid = Util.isNullOrEmpty(req.getToken()) ? "login" : req.getToken();
            String clientid = Util.isNullOrEmpty(req.getClientId()) ? "anonymous" : req.getClientId();

            defaultLogger.info(String.format("[<<<<id=%s,clientid=%s,userid=%s][request_data=%s]", new Object[]{key, clientid, userid, raw}));
            try {
                Date now = new Date();
                DlogId logId = new DlogId(now, key, 0);
                Dlog log = new Dlog(logId, clientid);
                log.setUserid(userid);
                log.setOperation(action);
                log.setRawreq(raw);
                log.setReqat(now);
                HibernateUtil.beginTransaction();
                HibernateUtil.currentSession().save(log);
                HibernateUtil.commit();
            } catch (Exception e) {
                HibernateUtil.rollback();
                defaultLogger.error(e.getMessage(), e);
                e.printStackTrace();
            }
        });
    }

//    public static void LogReq(String key, String action, BaseReq req) {
//
//        String raw = Util.toXMLString(req);
//        String userid = Util.isNullOrEmpty(req.getToken()) ? "login" : req.getToken();
//        String clientid = Util.isNullOrEmpty(req.getClientId()) ? "anonymous" : req.getClientId();
//
//        defaultLogger.info(String.format("[<<<<id=%s,clientid=%s,userid=%s][request_data=%s]", new Object[]{key, clientid, userid, raw}));
//        try {
//            Date now = new Date();
//            DlogId logId = new DlogId(now, key, 0);
//            Dlog log = new Dlog(logId, clientid);
//            log.setUserid(userid);
//            log.setOperation(action);
//            log.setRawreq(raw);
//            log.setReqat(now);
//            HibernateUtil.beginTransaction();
//            HibernateUtil.currentSession().save(log);
//            HibernateUtil.commit();
//        } catch (Exception e) {
//            HibernateUtil.rollback();
//            defaultLogger.error(e.getMessage(), e);
//            e.printStackTrace();
//        }
//    }
    public static void LogRes(String key, String action, BaseRes res) {
        Util.executorService.submit(() -> {
            String raw = Util.toXMLString(res);
            String userid = Util.isNullOrEmpty(res.getOriginalClientReq().getToken()) ? "login" : res.getOriginalClientReq().getToken();
            String clientid = Util.isNullOrEmpty(res.getOriginalClientReq().getClientId()) ? "anonymous" : res.getOriginalClientReq().getClientId();

            defaultLogger.info(String.format("[>>>>id=%s,clientid=%s,userid=%s][response_data=%s]", new Object[]{key, clientid, userid, raw}));

            try {
                Date now = new Date();
                DlogId logId = new DlogId(now, key, 1);
                Dlog log = new Dlog(logId, clientid);
                log.setUserid(userid);
                log.setOperation(action);
                log.setRawres(raw);
                log.setResat(now);

                HibernateUtil.beginTransaction();
                HibernateUtil.currentSession().save(log);
                HibernateUtil.commit();
            } catch (Exception e) {
                HibernateUtil.rollback();
                defaultLogger.error(e.getMessage(), e);
                e.printStackTrace();
            }
        });
    }
}
