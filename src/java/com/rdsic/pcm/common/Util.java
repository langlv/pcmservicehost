/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdsic.pcm.common;

import com.rdsic.pcm.data.entity.User;
import com.rdsic.pcm.data.entity.VUserpermission;
import com.rdsic.pcm.service.impl.Logger;
import com.rdsic.pcm.service.impl.PCMException;
import com.rdsic.pileconstructionmanagement.type.common.service.BaseReq;
import com.rdsic.pileconstructionmanagement.type.common.service.BaseRes;
import java.io.StringWriter;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 *
 * @author langl
 */
public class Util {

    private static final int DEFAULT_ACTIVE_TOKEN = 5; //minutes
    private static final HashMap<String, Marshaller> entityMarshallers = new HashMap();
    private static final HashMap<String, Object> entityMarshallerLock = new HashMap();

    public static final DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

    public static boolean isNullOrEmpty(String t) {
        if (t == null) {
            return true;
        }
        return t.trim().equals("");
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    /**
     * Convert Date to XmlGregorianCalendar to used in xml soap message
     *
     * @param d
     * @return
     */
    public static XMLGregorianCalendar toXmlGregorianCalendar(Date d) {
        try {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(d);
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (DatatypeConfigurationException ex) {
            Logger.defaultLogger.error(ex.getMessage(), ex);
        }
        return null;
    }

    /**
     * Convert from XmlGregorianCalendar to Date
     *
     * @param cal
     * @return
     */
    public static Date toDate(XMLGregorianCalendar cal) {
        if (cal == null) {
            return null;
        }
        return cal.toGregorianCalendar().getTime();
    }

    /**
     * Convert JAXB object to XML String
     *
     * @param obj
     * @return
     */
    public static String toXMLString(Object obj) {
        if (obj == null) {
            return null;
        }

        String pkgName = obj.getClass().getPackage().getName();
        try {
            if (!entityMarshallers.containsKey(pkgName)) {
                JAXBContext jc = JAXBContext.newInstance(pkgName);
                entityMarshallers.put(pkgName, jc.createMarshaller());
                entityMarshallerLock.put(pkgName, new Object());
            }

            StringWriter sw = new StringWriter();
            Marshaller marshaller = entityMarshallers.get(pkgName);
            Object locker = entityMarshallerLock.get(pkgName);

            synchronized (locker) {
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Configuration.getBoolean(Constant.CONFIG_KEY.PCM_XML_PRETTY_PRINT));
                marshaller.marshal(obj, sw);
            }

            return sw.toString();

        } catch (Exception e) {
            Logger.defaultLogger.error(e.getMessage(), e);
        }
        return "";
    }

    /**
     * Get BaseReq object from general request
     *
     * @param req
     * @return
     */
    public static BaseReq extractHeader(BaseReq req) {
        if (req == null) {
            return null;
        }
        BaseReq header = new BaseReq();
        header.setClientId(req.getClientId());
        header.setRequestDateTime(req.getRequestDateTime());
        header.setRequestId(req.getRequestId());
        header.setToken(req.getToken());
        return header;
    }

    /**
     * Validation for each request from client
     *
     * @param req
     * @param fnAddr
     * @param action
     * @param output
     * @return
     */
    public static boolean validateRequest(BaseReq req, String fnAddr, String action, BaseRes output) {
        String token = req.getToken();
        Date now = new Date();
        boolean authorized = false;
        if (output == null) {
            output = new BaseRes();
            output.setResponseDateTime(toXmlGregorianCalendar(now));
        }
        output.setOriginalClientReq(extractHeader(req));
        try {
            if ((token == null) || (token.isEmpty())) {
                throw new PCMException("Token is require for this request", Constant.STATUS_CODE.ERR_USER_TOKEN_REQUIRED);
            }
            List<User> t = GenericHql.INSTANCE.query("from User where token=:token", "token", token);
            if (t.isEmpty()) {
                throw new PCMException("Token is not valid", Constant.STATUS_CODE.ERR_USER_TOKEN_INVALID);
            }
            User ut = (User) t.get(0);
            if (now.getTime() - ut.getLastactive().getTime() > Configuration.getInt(Constant.CONFIG_KEY.PCM_TOKEN_TTL) * 60000) {
                throw new PCMException("Token for this request is expired", Constant.STATUS_CODE.ERR_USER_TOKEN_EXPIRE);
            }
            ut.setLastactive(now);

            HibernateUtil.beginTransaction();
            HibernateUtil.currentSession().save(ut);
            HibernateUtil.commit();

            List<VUserpermission> fl = GenericHql.INSTANCE.query("from VUserpermission WHERE userid=:userid", "userid", ut.getUserid());
            if (!fl.isEmpty()) {
                for (VUserpermission f : fl) {
                    if ((f.getId().getAddress().equalsIgnoreCase(fnAddr)) && (f.getId().getOperation().equalsIgnoreCase(action))) {
                        authorized = true;
                    }
                }
            }
            if (!authorized) {
                throw new PCMException("Unauthorized action", Constant.STATUS_CODE.ERR_UNAUTHORIZED_ACTION);
            }
        } catch (PCMException pe) {
            handleException(pe, output);
            return false;
        } catch (Exception e) {
            HibernateUtil.rollback();
            handleException(e, output);
            return false;
        }
        output.setStatus(Constant.STATUS_CODE.OK);
        return true;
    }

    public static void handleException(Exception e, BaseRes res) {
        if (e == null) {
            return;
        }
        res.setStatus(Constant.STATUS_CODE.FAIL);
        res.setResponseDateTime(toXmlGregorianCalendar(new Date()));
        if ((e instanceof PCMException)) {
            PCMException pe = (PCMException) e;
            res.setErrorCode(pe.getCode());
            res.setErrorMessage(pe.getDescription());
        } else {
            res.setErrorCode(Constant.STATUS_CODE.ERR_SYSTEM_FAIL);
            res.setErrorMessage(e.getMessage());
        }
        e.printStackTrace();
        Logger.defaultLogger.error(e.getMessage(), e);
    }
}
