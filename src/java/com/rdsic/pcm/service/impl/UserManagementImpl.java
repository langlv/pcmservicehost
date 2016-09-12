/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdsic.pcm.service.impl;

import com.rdsic.pcm.data.entity.VUserpermission;
import com.rdsic.pcm.data.entity.VUseractive;
import com.rdsic.pcm.data.entity.User;
import com.rdsic.pcm.data.entity.Userclass;
import com.rdsic.pcm.data.entity.Usertoken;
import com.rdsic.pcm.common.Util;
import com.rdsic.pcm.common.HibernateUtil;
import com.rdsic.pcm.common.Constant;
import com.rdsic.pcm.common.GenericHql;
import com.rdsic.pileconstructionmanagement.type.usermanagement.user.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author langl
 */
public class UserManagementImpl {

    /**
     * Implementation for UserLogin operation
     *
     * @param req
     * @return
     */
    public static UserLoginRes getUserByLogin(UserLoginReq req) {
        String key = UUID.randomUUID().toString();
        String opr = "UserManagement/Login";
        ServiceLogger.LogReq(key, opr, req);

        Date now = new Date();
        XMLGregorianCalendar nowXml = Util.toXmlGregorianCalendar(now);
        boolean validated = true;

        UserLoginRes res = new UserLoginRes();
        res.setOriginalClientReq(Util.extractHeader(req));

        try {
            // load user information
            List<User> usrList = GenericHql.INSTANCE.query("from User where userid=:userid", "userid", req.getUserId());
            if (usrList.isEmpty()) {
                throw new PCMException("Invalid user id", Constant.STATUS_CODE.ERR_USER_INVALID);
            }
            User user = usrList.get(0);

            // load user class
            List<Userclass> cl = GenericHql.INSTANCE.query("from Userclass where classId=:userclass", "userclass", user.getUserclass());
            Userclass cls = cl.get(0);

            if (user.getPassword() == null ? req.getPassword() != null : !user.getPassword().equals(req.getPassword())) {
                validated = false;
                res.setStatus("1");
                res.setErrorCode(Constant.STATUS_CODE.ERR_PASS_INVALID);
                res.setErrorMessage("Invalid user id or password");
                res.setResponseDateTime(nowXml);
                user.setLoginfailcount(user.getLoginfailcount() + 1);
                if (user.getLoginfailcount() >= cls.getFailcount()) {
                    user.setStatus("D");
                }
            }

            HibernateUtil.beginTransaction();
            // update user last active time
            user.setLastactive(now);
            HibernateUtil.currentSession().save(user);

            if (validated && Constant.CODEDEF.USER_STATUS_L.equalsIgnoreCase(user.getStatus())) {
                validated = false;
                res.setStatus("1");
                res.setErrorCode(Constant.STATUS_CODE.ERR_USER_DISABLED);
                res.setErrorMessage("User has been locked");
                res.setResponseDateTime(nowXml);
            }

            if (validated) {
                String token = Util.generateToken();
                user.setLastlogin(now);
                user.setLastactive(now);
                user.setToken(token);
                user.setLoginfailcount(0);

                UserInformationType uit = new UserInformationType();
                uit.setUserID(req.getUserId());
                uit.setLoginFailCount(user.getLoginfailcount());
                uit.setFirstName(user.getFirstname());
                uit.setLastName(user.getLastname());
                uit.setLastLogin(nowXml);
                uit.setLastActive(nowXml);
                uit.setDateCreate(Util.toXmlGregorianCalendar(user.getLastlogin()));
                uit.setDateExpire(Util.toXmlGregorianCalendar(user.getDateexpire()));
                uit.setActiveToken(token);

                UserClassType userClass = new UserClassType();
                userClass.setClassId(user.getUserclass());
                userClass.setDeactiaveOnFailCount(cls.getFailcount());
                userClass.setStatus(cls.getStatus());
                userClass.setName(cls.getName());

                uit.setUserClass(userClass);

                // load user permission
                List<VUserpermission> fl = GenericHql.INSTANCE.query("from VUserpermission WHERE userid=:userid", "userid", user.getUserid());
                if (!fl.isEmpty()) {
                    UserLoginRes.Permission permission = new UserLoginRes.Permission();
                    fl.stream().filter((f) -> (f != null)).map((f) -> {
                        UserPermisionType p = new UserPermisionType();
                        p.setAddress(f.getId().getAddress());
                        p.setOperation(f.getId().getOperation());
                        p.setType(f.getId().getType());
                        return p;
                    }).forEach((p) -> {
                        permission.getFunction().add(p);
                    });
                    res.setPermission(permission);
                }
                res.setUserInformation(uit);

                // save token to db
                Usertoken newToken = new Usertoken();
                newToken.setUserid(uit.getUserID());
                newToken.setTokenid(token);
                newToken.setStarttime(now);
                newToken.setLastactive(now);
                HibernateUtil.currentSession().save(newToken);

                res.setStatus(Constant.STATUS_CODE.OK);
            }

        } catch (Exception e) {
            HibernateUtil.rollback();
            Util.handleException(e, res);
        } finally {
            HibernateUtil.commit();
        }

        res.setResponseDateTime(nowXml);
        ServiceLogger.LogRes(key, opr, res);
        return res;
    }

    /**
     * Implementation for UnlockUser operation
     *
     * @param req
     * @return
     */
    public static UnlockUserRes unlockUser(UnlockUserReq req) {
        String key = UUID.randomUUID().toString();
        String opr = "UserManagement/UnlockUser";
        ServiceLogger.LogReq(key, opr, req);

        UnlockUserRes res = new UnlockUserRes();
        if (!Util.validateRequest(req, opr, Constant.FUNCTIONALITY_ACTION.WS_INVOKE, res)) {
            ServiceLogger.LogRes(key, opr, res);
            return res;
        }

        try {
            int cnt = GenericHql.INSTANCE.update("update User set status=:status where userid=:userid", true, "status", Constant.CODEDEF.USER_STATUS_A, "userid", req.getUserId());
            if (cnt <= 0) {
                throw new Exception("Unable to update user");
            }
            res.setStatus(Constant.STATUS_CODE.OK);
        } catch (Exception e) {
            Util.handleException(e, res);
        }

        res.setResponseDateTime(Util.toXmlGregorianCalendar(new Date()));
        ServiceLogger.LogRes(key, opr, res);
        return res;
    }

    /**
     * Implementation method for ResetPassword operation
     *
     * @param req
     * @return
     */
    public static ResetPasswordRes resetPassword(ResetPasswordReq req) {
        String key = UUID.randomUUID().toString();
        String opr = "UserManagement/ResetPassword";
        ServiceLogger.LogReq(key, opr, req);

        ResetPasswordRes res = new ResetPasswordRes();
        if (!Util.validateRequest(req, opr, Constant.FUNCTIONALITY_ACTION.WS_INVOKE, res)) {
            ServiceLogger.LogRes(key, opr, res);
            return res;
        }

        try {
            int cnt = GenericHql.INSTANCE.update("update User set password=:newpass where userid=:userid", true, "newpass", req.getNewPassword(), "userid", req.getUserId());
            if (cnt <= 0) {
                throw new Exception("Unable to update user");
            }
            res.setStatus(Constant.STATUS_CODE.OK);
        } catch (Exception e) {
            Util.handleException(e, res);
        }

        res.setResponseDateTime(Util.toXmlGregorianCalendar(new Date()));
        ServiceLogger.LogRes(key, opr, res);
        return res;
    }

    /**
     * Implementation method for AddOrUpdateUser operation
     *
     * @param req
     * @return
     */
    public static AddOrUpdateUserRes addOrUpdateUser(AddOrUpdateUserReq req) {
        String key = UUID.randomUUID().toString();
        String opr = "UserManagement/AddOrUpdateUser";
        ServiceLogger.LogReq(key, opr, req);

        Date now = new Date();

        AddOrUpdateUserRes res = new AddOrUpdateUserRes();
        if (!Util.validateRequest(req, opr, Constant.FUNCTIONALITY_ACTION.WS_INVOKE, res)) {
            ServiceLogger.LogRes(key, opr, res);
            return res;
        }
        try {
            HibernateUtil.beginTransaction();

            boolean isNewUser = true;
            User user = null;
            // load user information
            List<User> usrList = GenericHql.INSTANCE.query("from User where userid=:userid", "userid", req.getUserID());
            if (!usrList.isEmpty()) {
                user = usrList.get(0);
                isNewUser = false;
            } else {
                user = new User();
            }
            user.setUserid(req.getUserID());
            user.setUserclass(req.getUserClass());
            user.setPassword(req.getPassword());
            user.setFirstname(req.getFirstName());
            user.setLastname(req.getLastName());
            user.setDateexpire(Util.toDate(req.getDateExpire()));

            if (isNewUser) {
                user.setDatecreate(now);
                user.setLastactive(null);
                user.setLastlogin(null);
                user.setLoginfailcount(0);
                user.setStatus("A");
                user.setToken(null);
            }

            HibernateUtil.currentSession().save(user);
            HibernateUtil.commit();

            res.setStatus(Constant.STATUS_CODE.OK);
        } catch (Exception e) {
            HibernateUtil.rollback();
            Util.handleException(e, res);
        }

        res.setResponseDateTime(Util.toXmlGregorianCalendar(now));
        ServiceLogger.LogRes(key, opr, res);
        return res;
    }

    /**
     * Implementation method for operation Logout
     *
     * @param req
     * @return
     */
    public static UserLogoutRes userLogout(UserLogoutReq req) {
        String key = UUID.randomUUID().toString();
        String opr = "UserManagement/Logout";
        ServiceLogger.LogReq(key, opr, req);

        Date now = new Date();

        UserLogoutRes res = new UserLogoutRes();
        try {
            if (req.getUserId().isEmpty() || req.getToken().isEmpty()) {
                throw new PCMException("User id and token are required", Constant.STATUS_CODE.ERR_USER_TOKEN_REQUIRED);
            }

            // load user information
            List<VUseractive> usrList = GenericHql.INSTANCE.query("from VUseractive where userid=:userid and token=:token", "userid", req.getUserId(), "token", req.getToken());
            if (usrList.isEmpty()) {
                res.setStatus("1");
                res.setErrorCode(Constant.STATUS_CODE.ERR_USER_INVALID);
                res.setErrorMessage("User or token is invalid");
            } else {
                VUseractive user = usrList.get(0);
                int c = GenericHql.INSTANCE.update("update User set token='' where userid=:userid", true, "userid", user.getId().getUserid());
                if (c <= 0) {
                    throw new Exception("Unable to logout user");
                }
                res.setStatus(Constant.STATUS_CODE.OK);
            }

        } catch (Exception e) {
            HibernateUtil.rollback();
            Util.handleException(e, res);
        }

        res.setResponseDateTime(Util.toXmlGregorianCalendar(now));
        ServiceLogger.LogRes(key, opr, res);
        return res;

    }

    /**
     * Implementation for LockUser operation
     *
     * @param req
     * @return
     */
    public static LockUserRes lockUser(LockUserReq req) {
        String key = UUID.randomUUID().toString();
        String opr = "UserManagement/LockUser";
        ServiceLogger.LogReq(key, opr, req);

        LockUserRes res = new LockUserRes();
        if (!Util.validateRequest(req, opr, Constant.FUNCTIONALITY_ACTION.WS_INVOKE, res)) {
            ServiceLogger.LogRes(key, opr, res);
            return res;
        }

        try {
            int cnt = GenericHql.INSTANCE.update("update User set status=:status where userid=:userid", true, "status", Constant.CODEDEF.USER_STATUS_L, "userid", req.getUserId());
            if (cnt <= 0) {
                throw new Exception("Unable to update user");
            }
            res.setStatus(Constant.STATUS_CODE.OK);
        } catch (Exception e) {
            Util.handleException(e, res);
        }

        res.setResponseDateTime(Util.toXmlGregorianCalendar(new Date()));
        ServiceLogger.LogRes(key, opr, res);
        return res;
    }

}
