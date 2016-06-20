/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdsic.pcm.common;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * JPA implementation of the IDAO. Note that this implementation also expects
 * Hibernate as JPA implementation. That's because we like the Criteria API.
 *
 * @author Jurgen Lust
 * @author $LastChangedBy: jlust $
 *
 * @version $LastChangedRevision: 257 $
 *
 * @param <T> The persistent type
 * @param <ID> The primary key type
 */
public class GenericDAO<T, ID extends Serializable> implements IDAO<T, ID> {

    private final Class<T> persistentClass;
    private final Class<ID> persistentClassId;
    private List<Criterion> crits = null;
    private List<Order> orders = null;
    private int firstResult, maxResult;
    private Session session = null;
    private StatelessSession stlsession = null;
    private SessionFactory sessionFactory = null;
    private Transaction transaction = null;

    @SuppressWarnings("unchecked")
    public GenericDAO() {
        this(null, null);
        init();
    }

    public GenericDAO(Session sess) {
        this(sess, null);
        init();
    }

    public GenericDAO(Session sess, Transaction trans) {
        Type[] types = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
        this.persistentClass = (Class<T>) types[0];
        this.persistentClassId = (Class<ID>) types[1];

        if (sess != null) {
            session = sess;
        }
        if (trans != null) {
            transaction = trans;
        }
    }

    private void init() {
        crits = new ArrayList<Criterion>();
        orders = new ArrayList<Order>();
        firstResult = -1;
        maxResult = -1;
        getTransaction();
        //getSession().setFlushMode(FlushMode.COMMIT);
    }

    private void clearParams() {
        setCrits();
        setOrders();
        firstResult = -1;
        maxResult = -1;
    }

    public Transaction getTransaction() {
        if (transaction == null) {
            transaction = getSession().beginTransaction();
        }
        return transaction;
    }

    public Session getSession() {
        if (session == null || !session.isOpen()) {
            session = getSessionFactory().openSession();
        }
        return session;
    }

    public StatelessSession getStatelessSession() {
        if (stlsession == null || !session.isOpen()) {
            stlsession = getSessionFactory().openStatelessSession();
        }
        return stlsession;
    }

    public SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = HibernateUtil.currentSessionFactory("hibernate.cfg.xml");
        }
        return sessionFactory;
    }

    @Override
    public void commit() {
        if (transaction != null && transaction.isActive()) {
            transaction.commit();
        }
    }

    @Override
    public void newTransaction() {
        transaction = getSession().beginTransaction();
    }

    @Override
    public void rollback() {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }
    }

    @Override
    public void flush() {
        Session sess = getSession();
        if (sess != null && sess.isOpen()) {
            sess.flush();
        }
    }

    @Override
    public void close() {
        //rollback();
        Session sess = getSession();
        if (sess != null && sess.isOpen()) {
            sess.close();
        }
    }

    @Override
    public void done() {
        flush();
        commit();
        Session sess = getSession();
        if (sess != null && sess.isOpen()) {
            sess.close();
        }
    }

    @Override
    public Class<T> getEntityClass() {
        return persistentClass;
    }

    @Override
    public Class<ID> getIdClass() {
        return persistentClassId;
    }

    @Override
    public T first() {
        List<T> lst = list(0, 1);
        return lst.size() == 1 ? lst.get(0) : null;
    }

    @Override
    public List<T> list(int FirstResult, int MaxResult) {
        this.firstResult = FirstResult;
        this.maxResult = MaxResult;
        return list();
    }

    @Override
    public List<T> list() {
        List<T> res = null;

        // make criteria of T
        Criteria criteria = getSession().createCriteria(getEntityClass());

        // add Criterions
        if (crits != null) {
            for (Criterion crit : crits) {
                criteria.add(crit);
            }
        }
        // add orders
        if (orders != null) {
            for (Order ord : orders) {
                criteria.addOrder(ord);
            }
        }
        // paging
        if (firstResult >= 0) {
            criteria.setFirstResult(firstResult);
        }
        if (maxResult >= 0) {
            criteria.setMaxResults(maxResult);
        }
        // make result
        res = criteria.list();
        // and clear all params, next command must set again
        clearParams();

        if (res == null) {
            res = new ArrayList<T>();
        }
        return res;
    }

//    String toHsqlValue(Object fValue, boolean fIgnoreCase) {
//        if(fValue instanceof String){
//            if(fIgnoreCase){
//                return String.format("'%s'", fValue.toString())
//            }
//        }
//    }
//
//    String buidHsqlCrit(String fOp, String fName, Object fValue, boolean fIgnoreCase) {
//        if(fValue == null){
//            return String.format(" %s is null ", fName);
//        }
//        if (fOp.equalsIgnoreCase("=")) {
//            return String.format(" %s = %s ", fName, toHsqlValue(fValue, fIgnoreCase));
//        }
//        return null;
//    }
//
//    public void delete() throws IllegalArgumentException, IllegalAccessException, InvalidValueException {
//        //hsession.createQuery("DELETE FROM Message WHERE id=?").setString(0, messageId).executeUpate();
//        String name = getEntityClass().getSimpleName();
//        StringBuilder hsql = new StringBuilder("DELETE FROM ");
//        hsql.append(name);
//        if (!crits.isEmpty()) {
//            hsql.append(" WHERE 1=1 ");
//        }
//
//        // add Criterions
//        for (Criterion crit : crits) {
//            Field Fs[] = crit.getClass().getDeclaredFields();
//            String fName = (String) SBCUtils.getPrivateField(Fs, "propertyName", crit);
//            Object fValue = SBCUtils.getPrivateField(Fs, "value", crit);
//            String fOp = (String) SBCUtils.getPrivateField(Fs, "op", crit);
//            Boolean fIgnoreCase = (Boolean) SBCUtils.getPrivateField(Fs, "ignoreCase", crit);
//
//            hsql.append(buidHsqlCrit(fOp, fName, fValue, fIgnoreCase));
//
//            int i = 1;
//        }
//    }
    @Override
    public GenericDAO<T, ID> setCrits(Criterion... criterions) {
        if (crits == null) {
            crits = new ArrayList<Criterion>();
        } else {
            crits.clear();
        }

        for (Criterion c : criterions) {
            if (c != null) {
                crits.add(c);
            }
        }
        return this;
    }

    @Override
    public GenericDAO<T, ID> setOrders(Order... ords) {
        if (orders == null) {
            orders = new ArrayList<Order>();
        } else {
            orders.clear();
        }

        orders.addAll(Arrays.asList(ords));
        return this;
    }

    @Override
    public long count() {
        long rcount = -1;
        Criteria crit = getSession().createCriteria(getEntityClass());
        crit.setProjection(Projections.rowCount());

        for (final Criterion c : crits) {
            crit.add(c);
        }
        try {
            rcount = (Long) crit.list().get(0);
        } catch (ClassCastException e) {
            rcount = (Integer) crit.list().get(0);
        }
        return rcount;
    }

    @Override
    public int countByExp(final T exampleInstance) throws HibernateException {
        int rcount = -1;
        Criteria crit = getSession().createCriteria(getEntityClass());
        crit.setProjection(Projections.rowCount());
        crit.add(Example.create(exampleInstance));

        rcount = (Integer) crit.list().get(0);

        return rcount;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> getByExp(final T exampleInstance) throws HibernateException {
        List<T> res = null;

        Criteria crit = getSession().createCriteria(getEntityClass());
        res = crit.list();

        if (res == null) {
            res = new ArrayList<T>();
        }
        return res;
    }

    @Override
    public T getById(final ID id) {

        T res = null;
        res = (T) getSession().get(getEntityClass(), id);
        return res;
    }

    @Override
    public T loadById(final ID id) {
        T res = null;
        res = (T) getSession().load(getEntityClass(), id);
        return res;
    }

    @Override
    public boolean delete(T entity) throws Exception {
        if (entity == null) {
            return false;
        }
        getSession().delete(entity);
        return true;
    }

    @Override
    public T save(T entity) throws Exception {
        if (entity == null) {
            return null;
        }
        return (T) getSession().merge(entity);
    }

    public T statelessSave(T entity) throws Exception {
        if (entity == null) {
            return null;
        }
        T savedEntity = (T) getStatelessSession().insert(entity);
        return savedEntity;
    }

    @Override
    @SuppressWarnings("FinalizeDeclaration")
    protected void finalize() throws Throwable {
        try {
            this.close();
        } finally {
            super.finalize();
        }
    }

}
