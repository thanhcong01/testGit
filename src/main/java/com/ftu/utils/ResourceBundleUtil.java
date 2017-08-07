 package com.ftu.utils;

import java.io.UnsupportedEncodingException;
 import java.util.ResourceBundle;

 public class ResourceBundleUtil  {
    /* 20 */ private static ResourceBundle rb = ResourceBundle.getBundle("config");
    
     public static String getString(String key)  {
        /* 24 */ return rb.getString(key);
            }
    public static String getString(String key,String fileName) throws UnsupportedEncodingException
     {
                 ResourceBundle rb = ResourceBundle.getBundle(fileName);
                 return rb.getString(key);
            }
     public static String getRequestMonitor() {
        /* 28 */ return rb.getString("RequestMonitor");
            }
    
     public static String getDAOPackage()  {
        /* 33 */ return rb.getString("dao_package");
            }
    
     public static String getHibernateConfigFileLocation() {
        /* 37 */ return rb.getString("hibernate_config_file_location");
            }
    
     public static String getDBCConfigFileLocation() {
        /* 41 */ return rb.getString("dbc_config_file_location");
            }
    
     public static String getClientRepo() {
        /* 45 */ return rb.getString("client_repo");
            }
    
     public static String getClientKey() {
        /* 49 */ return rb.getString("client_keys");
            }
    
     public static String getKeyUser() {
        /* 53 */ return rb.getString("key_user");
            }
    
     public static String getKeyPass() {
        /* 57 */ return rb.getString("key_pass");
            }
     public static String getLogFactoryClassPath() {
        /* 60 */ return rb.getString("WF.LogFactory");
            }
     }
