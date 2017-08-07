package com.ftu.hibernate;

import com.ftu.utils.ResourceBundleUtil;
import com.ftu.utils.EncryptDecryptUtils;
import com.ftu.config.Config;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.xml.sax.SAXException;

public class HibernateUtil {

    //private static Logger log = WFUtil.getLogger(HibernateUtil.class);
    private static HashMap<String, SessionFactory> sessionFactories = new HashMap();
    private static String dbcConfigFile = "";
    private static Config dbcConfig = new Config();
    private static final String TRANS_EX_MSG_TRANSNOTSTARTED = "Transaction not successfully started";

    private static void loadEncryptedDBConfig(Configuration config, String filePath) {
        // URL file = Thread.currentThread().getContextClassLoader().getResource(filePath);
        InputStream f = HibernateUtil.class.getResourceAsStream(filePath);
        // String decryptString = EncryptDecryptUtils.decryptFile(URLDecoder.decode(file.getPath()));
        String decryptString = EncryptDecryptUtils.decryptFile(f);
        String[] properties = decryptString.split("\r\n");
        for (String property : properties) {
            String[] temp = property.split("=", 2);
            if (temp.length == 2) {
                config.setProperty(temp[0], temp[1]);
            }
        }
    }

    public static Session getSession() {
        Session session = (Session) getCurrentSessions().get("default session");
        if (!session.getTransaction().isActive()) {
            session.beginTransaction();
        }
        return session;
    }

    public static void startup() {
    }

    public static void shutdown() {
        for (String key : getSessionFactories().keySet()) {
            ((SessionFactory) getSessionFactories().get(key)).close();
        }
    }

    public static HashMap<String, SessionFactory> getSessionFactories() {
        return sessionFactories;
    }

    public static SessionFactory getSessionFactory(String sessionName) {
        return (SessionFactory) getSessionFactories().get(sessionName.toLowerCase());
    }

    public static SessionFactory getSessionFactory() {
        return (SessionFactory) getSessionFactories().get("default session");
    }

    public static HashMap<String, Session> getCurrentSessions() {
        HashMap sessions = new HashMap();
        for (String key : getSessionFactories().keySet()) {
            sessions.put(key, ((SessionFactory) getSessionFactories().get(key)).getCurrentSession());
        }
        return sessions;
    }

    public static Session getSessionAndBeginTransaction() {
        return getSessionAndBeginTransaction("default session");
    }

    public static Session getSessionAndBeginTransaction(int transTimeout) {
        return getSessionAndBeginTransaction("default session", transTimeout);
    }

    public static Session getSessionAndBeginTransaction(String sessionName) {
        if (getSessionFactory(sessionName) == null) {
            return null;
        }
        Session session = getSessionFactory(sessionName).getCurrentSession();
        session.beginTransaction();
        return session;
    }

    public static Session getSessionAndBeginTransaction(String sessionName, int transTimeout) {
        if (getSessionFactory(sessionName) == null) {
            return null;
        }
        Session session = getSessionFactory(sessionName).getCurrentSession();
        session.getTransaction().setTimeout(transTimeout);
        session.getTransaction().begin();

        return session;
    }

    public static HashMap<String, Session> commitCurrentSessions()
            throws Exception {
        HashMap sessions = getCurrentSessions();

        HashMap sessionsToRollBack = new HashMap();
        boolean hasExceptionDuringCommit = false;

        for (Object sessionName : sessions.keySet()) {
            Session session = (Session) sessions.get(sessionName);
            if (session.isOpen()) {
                Transaction t = session.getTransaction();

                if ((t.isActive()) && (!hasExceptionDuringCommit)) {
                    try {
                        t.commit();
                    } catch (Throwable ex) {
                        hasExceptionDuringCommit = true;
                        sessionsToRollBack.put(sessionName, session);
                    }
                } else if (hasExceptionDuringCommit) {
                    sessionsToRollBack.put(sessionName, session);
                }
            }
        }
        return sessionsToRollBack;
    }

    public static void rollBackSessions(HashMap<String, Session> sessionsToRollBack) {
        if (sessionsToRollBack != null) {
            for (String sessionName : sessionsToRollBack.keySet()) {
                Session session = (Session) sessionsToRollBack.get(sessionName);
                if (session.isOpen()) {
                    Transaction t = session.getTransaction();
                    try {
                        t.rollback();
                    } catch (Exception ex) {
                        if (ex.getMessage().equals("Transaction not successfully started")) {
                        } else {
                        }
                    } catch (Throwable ta) {
                    }
                }
            }
        }
    }

    public static void closeCurrentSessions()
            throws Exception {
        HashMap sessionMaps = getCurrentSessions();
        if (sessionMaps != null) {
            for (Object key : sessionMaps.keySet()) {
                Session session = (Session) sessionMaps.get(key);
                try {
                    session.getTransaction().rollback();
                    if (session.isOpen()) {
                        session.close();
                    }
                } catch (Exception ex) {
                }
            }
        }
    }

    static {
        try {
            dbcConfigFile = "config/database/DBCConfig.xml";
            dbcConfigFile = ResourceBundleUtil.getDBCConfigFileLocation();
            dbcConfig.loadInstance(dbcConfigFile);
            HashMap dbcConfigInfo = dbcConfig.getSessions();

            if (dbcConfigInfo != null) {
                for (Object key : dbcConfigInfo.keySet()) {
                    String path = (String) dbcConfigInfo.get(key);
                    try {
                    AnnotationConfiguration dbConfig = new AnnotationConfiguration().configure(path);
                    String encryptedFile = dbcConfigFile.substring(0, dbcConfigFile.lastIndexOf("/") + 1) + key;
                    try {
                        // loadEncryptedDBConfig(dbConfig, encryptedFile);
                    } catch (Throwable ex) {
//                            log.info("Error while reading encrypted file: " + encryptedFile, ex);
//                            log.info("Read HibernateConfigFile again, file to read: " + path);
                        dbConfig = new AnnotationConfiguration().configure(path);
                    }

                    SessionFactory sessionFactory = dbConfig.buildSessionFactory();
                    sessionFactories.put(key.toString().toLowerCase(), sessionFactory);
                    } catch (Throwable ex) {
                        String msg = ex.getMessage();
                        System.out.print(msg);
                     }
                }
            }

            if (sessionFactories.isEmpty()) {
            }
        } catch (IOException ex) {
            throw new ExceptionInInitializerError(ex);
        } catch (SAXException ex) {
            throw new ExceptionInInitializerError(ex);
        } catch (Exception ex) {
            Logger.getLogger(HibernateUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
