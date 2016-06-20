/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdsic.pcm.common;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author langl
 */
import org.apache.log4j.Logger;
import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class HibernateUtil {

    private static final Logger log = Logger.getLogger(HibernateUtil.class);
    private static final String DEFAULT_CONFIG_FILE = "hibernate.cfg.xml";
    private static SessionFactory sessionFactory;

    private static void initSessionFactory() {
        initSessionFactory(DEFAULT_CONFIG_FILE);
    }

    private static void initSessionFactory(String cfgFile) {
        if (sessionFactory == null) {
            try {
                System.out.println("Loading hibernate configuration...");

                org.hibernate.cfg.Configuration hconfig = new org.hibernate.cfg.Configuration().configure(cfgFile);

                hconfig.setProperty("hibernate.connection.driver_class", Configuration.getString(Constant.CONFIG_KEY.PCM_DB_DRIVER));
                hconfig.setProperty("hibernate.connection.url", Configuration.getString(Constant.CONFIG_KEY.PCM_DB_URL));
                hconfig.setProperty("hibernate.connection.username", Configuration.getString(Constant.CONFIG_KEY.PCM_DB_USER));
                hconfig.setProperty("hibernate.connection.password", Configuration.getString(Constant.CONFIG_KEY.PCM_DB_PASSWORD));
                sessionFactory = hconfig.buildSessionFactory();

                System.out.println("Hibernate load completed...");
            } catch (Throwable ex) {
                log.error("Initial SessionFactory creation failed.", ex);
                throw new ExceptionInInitializerError(ex);
            }
        }
    }

    public static final ThreadLocal session = new ThreadLocal();

    public static void createSchema(String filename) {
        org.hibernate.cfg.Configuration cfg = new AnnotationConfiguration().configure();
        new SchemaExport(cfg).setDelimiter(";").setOutputFile(filename).create(false, false);
    }

    public static void createSchema(String source_filename, String target_filename) {
        org.hibernate.cfg.Configuration cfg = new AnnotationConfiguration();
        cfg.configure(source_filename);
        new SchemaExport(cfg).setDelimiter(";").setOutputFile(target_filename).create(false, false);
    }

    public static SessionFactory currentSessionFactory() {
        if (sessionFactory == null) {
            initSessionFactory();
        }
        return sessionFactory;
    }

    public static SessionFactory currentSessionFactory(String conf) {
        if (sessionFactory == null) {
            initSessionFactory(conf);
        }
        return sessionFactory;
    }

    public static Session currentSession() {
        initSessionFactory("hibernate.cfg.xml");
        Session s = (Session) session.get();
        if ((s == null) || (!s.isConnected())) {
            s = sessionFactory.openSession();
            s.setFlushMode(FlushMode.COMMIT);
            s.setCacheMode(CacheMode.IGNORE);
            session.set(s);
        }
        return s;
    }

    public static Session currentSession(String cfgFile) {
        initSessionFactory(cfgFile);
        Session s = (Session) session.get();
        if ((s == null) || (!s.isConnected())) {
            s = sessionFactory.openSession();
            s.setFlushMode(FlushMode.COMMIT);
            s.setCacheMode(CacheMode.IGNORE);
            session.set(s);
        }
        return s;
    }

    public static void closeSession() {
        Session s = (Session) session.get();
        if (s != null) {
            s.flush();
            s.close();
        }
        session.set(null);
    }

    public static void beginTransaction() {
        Transaction tx = currentSession().getTransaction();
        if ((tx != null) && (tx.isActive())) {
            tx.rollback();
        }
        tx.begin();
    }

    public static void commit() {
        Transaction tx = currentSession().getTransaction();
        if ((tx != null) && (tx.isActive())) {
            tx.commit();
            currentSession().flush();
        }
        currentSession().clear();
    }

    public static void rollback() {
        Transaction tx = currentSession().getTransaction();
        if ((tx != null) && (tx.isActive())) {
            tx.rollback();
        }
        currentSession().clear();
    }
}
