/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdsic.pcm.common;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author langl
 */
public class Configuration {

    private static final String CONFIG_DIR_PROPERTY = "jboss.server.config.dir";
    private static final String PROPERTIES_FILE = Constant.GLOBAL.PCM_CONFIG_FILE;
    private static final Properties PROPERTIES = new Properties();

    static {
        String configDir = System.getProperty("jboss.server.config.dir");
        configDir = configDir + File.separator;

        String path = configDir + PROPERTIES_FILE;
        try {
            PROPERTIES.load(new FileInputStream(path));
        } catch (Exception e) {
            System.out.println("Unable to load PCM configuration file: " + path);
            e.printStackTrace();
        }
    }

    public static boolean hasProp(String key) {
        for (Object k : PROPERTIES.keySet()) {
            if (key.equalsIgnoreCase(k.toString())) {
                return true;
            }
        }
        return PROPERTIES.contains(key.toUpperCase());
    }

    public static String getString(String key)
            throws Exception {
        if (!hasProp(key)) {
            throw new Exception(String.format("Configuration key %s does not exists", new Object[]{key}));
        }
        return PROPERTIES.getProperty(key);
    }

    public static int getInt(String key)
            throws Exception {
        if (!hasProp(key)) {
            throw new Exception(String.format("Configuration key %s does not exists", new Object[]{key}));
        }
        return Integer.parseInt(PROPERTIES.getProperty(key));
    }

    public static boolean getBoolean(String key)
            throws Exception {
        if (!hasProp(key)) {
            throw new Exception(String.format("Configuration key %s does not exists", new Object[]{key}));
        }
        return Boolean.getBoolean(PROPERTIES.getProperty(key));
    }

    public static List<String> getList(String key)
            throws Exception {
        String val = getString(key);
        ArrayList<String> res = new ArrayList();
        Collections.addAll(res, val.split(","));
        return res;
    }

}
