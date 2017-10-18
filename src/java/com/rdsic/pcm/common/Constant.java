/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdsic.pcm.common;

/**
 *
 * @author langl
 */
public class Constant {

    public static class GLOBAL {

        public static String PCM_CONFIG_FILE = "pcmconfig.properties";
        public static String PCM_DATA_DELIMITER = "|";
    }

    public static class STATUS_CODE {

        public static String OK = "0";
        public static String FAIL = "1";
        public static String ERR_SYSTEM_FAIL = "S00001";
        public static String ERR_UPDATE_FAIL = "S00002";
        public static String ERR_NO_RECORD_FOUND = "S00003";
        public static String ERR_ID_INVALID = "S00004";
        public static String ERR_UNAUTHORIZED_ACTION = "C00001";
        public static String ERR_DATA_REQUIRED = "C00002";
        public static String ERR_USER_INVALID = "U00001";
        public static String ERR_PASS_INVALID = "U00002";
        public static String ERR_USER_DISABLED = "U00003";
        public static String ERR_USER_TOKEN_REQUIRED = "U00004";
        public static String ERR_USER_TOKEN_EXPIRE = "U00005";
        public static String ERR_USER_TOKEN_INVALID = "U00006";
        public static String ERR_INVALID_INPUT_DATA = "U00007";
        public static String ERR_INVALID_CONFIG_DATA = "U00008";

    }

    public static class FUNCTIONALITY_ACTION {

        public static String WS_INVOKE = "INVOKE";
    }

    public static class FUNCTIONALITY {

        public static String WS_USERMANAGEMENT_LOGIN = "UserManagement/Login";
    }

    public static class CONFIG_KEY {

        public static String PCM_DB_DRIVER = "PCM_DB_DRIVER";
        public static String PCM_DB_URL = "PCM_DB_URL";
        public static String PCM_DB_USER = "PCM_DB_USER";
        public static String PCM_DB_PASSWORD = "PCM_DB_PASSWORD";
        public static String PCM_TOKEN_TTL = "PCM_TOKEN_TTL";
        public static String PCM_QUERY_MAX_ROW = "PCM_QUERY_MAX_ROW";
        public static String PCM_XML_PRETTY_PRINT = "PCM_XML_PRETTY_PRINT";

    }

    public static class CODEDEF {

        public static final String FUNCTION_TYPE_SA = "SA";
        public static final String FUNCTION_TYPE_BA = "BA";
        public static final String FUNCTION_TYPE_MN = "MN";
        public static final String FUNCTION_OPERATION_INVOKE = "INVOKE";
        public static final String FUNCTION_OPERATION_ACCESS = "ACCESS";
        public static final String DRLMONITOR_MTYPE_SUMQ = "SUMQ";
        public static final String DRLMONITOR_MTYPE_ALIM = "ALIM";
        public static final String DRLMONITOR_MTYPE_VLIM = "VLIM";
        public static final String PILEPLAN_STATUS_N = "N";
        public static final String PILEPLAN_STATUS_D = "D";
        public static final String PILEPLAN_STATUS_C = "C";
        public static final String PILEPLAN_STATUS_F = "F";
        public static final String DRLRECMEMO_STATUS_N = "N";
        public static final String DRLRECMEMO_STATUS_A = "A ";

        public static final String USER_STATUS_A = "A";
        public static final String USER_STATUS_L = "L";
        public static final String USER_STATUS_N = "N";

    }
}
