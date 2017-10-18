/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdsic.pcm.service;

import com.rdsic.pcm.service.impl.UserManagementImpl;
import javax.jws.WebService;

/**
 *
 * @author NAPAS
 */
@WebService(serviceName = "UserManagement", portName = "UserManagement", endpointInterface = "com.rdsic.pileconstructionmanagement.service.usermanagement.UserManagementPortType", targetNamespace = "http://rdsic.com/pileconstructionmanagement/service/usermanagement", wsdlLocation = "WEB-INF/wsdl/UserManagement/UserManagement_v1.0.wsdl")
public class UserManagement {

    public com.rdsic.pileconstructionmanagement.type.usermanagement.user.UserLogoutRes userLogout(com.rdsic.pileconstructionmanagement.type.usermanagement.user.UserLogoutReq req) {
        return UserManagementImpl.userLogout(req);
    }

    public com.rdsic.pileconstructionmanagement.type.usermanagement.user.ResetPasswordRes resetPassword(com.rdsic.pileconstructionmanagement.type.usermanagement.user.ResetPasswordReq parameter) {
        return UserManagementImpl.resetPassword(parameter);
    }

    public com.rdsic.pileconstructionmanagement.type.usermanagement.user.UserLoginRes userLogin(com.rdsic.pileconstructionmanagement.type.usermanagement.user.UserLoginReq parameter) {
        return UserManagementImpl.getUserByLogin(parameter);
    }

    public com.rdsic.pileconstructionmanagement.type.usermanagement.user.UnlockUserRes unlockUser(com.rdsic.pileconstructionmanagement.type.usermanagement.user.UnlockUserReq parameter) {
        return UserManagementImpl.unlockUser(parameter);
    }

    public com.rdsic.pileconstructionmanagement.type.usermanagement.user.AddOrUpdateUserRes addOrUpdateUser(com.rdsic.pileconstructionmanagement.type.usermanagement.user.AddOrUpdateUserReq parameter) {
        return UserManagementImpl.addOrUpdateUser(parameter);
    }

    public com.rdsic.pileconstructionmanagement.type.usermanagement.user.LockUserRes lockUser(com.rdsic.pileconstructionmanagement.type.usermanagement.user.LockUserReq req) {
        return UserManagementImpl.lockUser(req);
    }
}
