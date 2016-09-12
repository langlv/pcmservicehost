/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdsic.pcm.common;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

/**
 *
 * @author langl
 * @param <T>
 */
public class GenericHql<T extends Serializable> {

    public static GenericHql INSTANCE = new GenericHql();

    public void GenericHql() {
    }

       /**
     * Execute a hql query
     *
     * @param hql
     * @param maxRows
     * @param params list of parameters and values: par1,val1,par2,val2,...
     * @return
     */
    public List<T> query(String hql, Object... params) {
        Query q = HibernateUtil.currentSession().createQuery(hql);
        HibernateUtil.currentSession().setCacheMode(CacheMode.IGNORE);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                q.setParameter(params[i].toString(), params[++i]);
            }
        }
        //q.setMaxResults(maxRows);
        return q.list();
    }

    /**
     * Execute a "SELECT ... FROM ... WHRE parm_name IN (value_list)" query
     *
     * @param hql
     * @param param
     * @param paramValues
     * @return
     */
    public List<T> queryWithParamList(String hql, String param, Object[] paramValues) throws Exception {
        return queryWithParamList(hql, Configuration.getInt(Constant.CONFIG_KEY.PCM_QUERY_MAX_ROW), param, paramValues);
    }

    /**
     * Execute a "SELECT ... FROM ... WHRE parm_name IN (value_list)" query
     *
     * @param hql
     * @param maxRows
     * @param param
     * @param paramValues
     * @return
     */
    public List<T> queryWithParamList(String hql, int maxRows, String param, Object[] paramValues) {
        Query q = HibernateUtil.currentSession().createQuery(hql);
        HibernateUtil.currentSession().setCacheMode(CacheMode.IGNORE);
        if (param != null && paramValues != null) {
            q.setParameterList(param, paramValues);
        }
        q.setMaxResults(maxRows);
        return q.list();
    }

    /**
     * Execute a native SQL query
     *
     * @param sql The SQL select query
     * @param maxRow Limit the number of rows to be returned
     * @param params List of parameters follow by name,value
     * @return
     */
    public List<Map<String, T>> querySQL(String sql, int maxRow, Object... params) {
        Query q = HibernateUtil.currentSession().createSQLQuery(sql);
        q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        q.setMaxResults(maxRow);

        HibernateUtil.currentSession().setCacheMode(CacheMode.IGNORE);

        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                q.setParameter(params[i].toString(), params[++i]);
            }
        }
        return q.list();
    }

    /**
     * execute an hql update query
     *
     * @param hql
     * @param autoCommit
     * @param params list of parameters and values: par1,val1,par2,val2,...
     * @return number of record updated
     */
    public int update(String hql, boolean autoCommit, Object... params) {
        Query q = HibernateUtil.currentSession().createQuery(hql);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                q.setParameter(params[i].toString(), params[++i]);
            }
        }

        if (autoCommit) {
            HibernateUtil.beginTransaction();
        }
        int numOfRecords = 0;
        try {
            numOfRecords = q.executeUpdate();
            if (autoCommit) {
                HibernateUtil.commit();
            }
        } catch (Exception e) {
            if (autoCommit) {
                HibernateUtil.rollback();
            }
            throw e;
        }
        return numOfRecords;
    }

    /**
     * Execute an update SQL, including INSERT/UPDATE/DELETE
     *
     * @param sql
     * @param autoCommit
     * @param params
     * @return
     */
    public int updateSQL(String sql, boolean autoCommit, Object... params) {
        Query q = HibernateUtil.currentSession().createSQLQuery(sql);

        HibernateUtil.currentSession().setCacheMode(CacheMode.IGNORE);

        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                q.setParameter(params[i].toString(), params[++i]);
            }
        }

        if (autoCommit) {
            HibernateUtil.beginTransaction();
        }
        int numOfRecords = 0;
        try {
            numOfRecords = q.executeUpdate();
            if (autoCommit) {
                HibernateUtil.commit();
            }
        } catch (Exception e) {
            if (autoCommit) {
                HibernateUtil.rollback();
            }
            throw e;
        }
        return numOfRecords;
    }
}
