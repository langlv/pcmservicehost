/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdsic.pcm.common;

import java.io.Serializable;
import java.util.List;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

/**
 *
 * @author langl
 * @param <T>
 * @param <ID>
 */
public interface IDAO<T, ID extends Serializable> {

    public void commit();

    public void newTransaction();

    public void rollback();

    public void flush();

    public void close();

    /**
     * **************************************************************
     * Description: Commit transaction, flush and close session Input : Outputs
     * : Returns :
     * **************************************************************
     */
    public void done();

    /**
     * **************************************************************
     * Description: Get the Class of the entity Input : Outputs : Returns :
     * Class
     *
     * **************************************************************
     */
    public Class<T> getEntityClass();

    /**
     * **************************************************************
     * Description: Get the Class of the entity's id Input : Outputs : Returns :
     * Class **************************************************************
     */
    public Class<ID> getIdClass();

    /**
     * **************************************************************
     * Description: Get first record of <T> from database, all criterion and
     * order are cleared after exit. Input : Outputs : Returns : object type T,
     * null if no record found
     * **************************************************************
     */
    public T first();

    /**
     * **************************************************************
     * Description: Get paged List of <T> from database, all criterion, order
     * and paging info are cleared after exit. Input : "FirstResult": first
     * record of data returned, set -1 to bypass this parameter. "MaxResult":
     * maximum record to be retrieved, set -1 to bypass this parameter Outputs :
     * Returns : List<T>, Empty list returned if no data found (not null value)
     * **************************************************************
     */
    public List<T> list(int FirstResult, int MaxResult);

    /**
     * **************************************************************
     * Description: Get List of <T> from database, all criterion and order are
     * cleared after exit. Input : Outputs : Returns : List<T>, Empty list
     * returned if no data found (not null value) 15/07/2010
     * **************************************************************
     */
    public List<T> list();

    /**
     * **************************************************************
     * Description: Set Criterion for next get data function such as list()
     * Input : Varargs of Criterion Outputs : Returns : Dao<T, ID>
     *
     * **************************************************************
     */
    public GenericDAO<T, ID> setCrits(Criterion... criterions);

    /**
     * **************************************************************
     * Description: Set Ordering of data return by next get data function such
     * as list() Input : Varargs of Order Outputs : Returns : Dao<T, ID>
     * **************************************************************
     */
    public GenericDAO<T, ID> setOrders(Order... ords);

    //public List<T> getByNamedQuery(final String queryName, Object... params);
    //public List<T> getByNamedQueryAndNamedParams(final String queryName, final Map<String, ?extends Object> params);
    /**
     * **************************************************************
     * Description: Count records match all Criterion that previous set by
     * setCrits(). All Criterion will not be removed Input : Outputs : Returns :
     * integer value >= 0 if success, -1 if failed 15/07/2010
     * *************************************************************
     */
    public long count();

    /**
     * **************************************************************
     * Description: Count records match data that provided by an instance of
     * entity. Input : exampleInstance: instance of entity contains data to
     * filter. Outputs : Returns : integer value >= 0 if success, -1 if failed
     * **************************************************************
     */
    public int countByExp(final T exampleInstance);// throws HibernateException;

    /**
     * **************************************************************
     * Description: Get records that match data was provided by an instance of
     * entity. Input : exampleInstance: instance of entity contains data to
     * filter. Outputs : Returns : List<T>, Empty list returned if no data found
     * (not null value)
     *
     * **************************************************************
     */
    public List<T> getByExp(final T exampleInstance);

    /**
     * **************************************************************
     * Description: Get an entity by its Id Input : "id": id of entity Outputs :
     * Returns : Entity as T, null if item not found or failed Date : 15/07/2010
     * **************************************************************
     */
    public T getById(final ID id);

    /**
     * **************************************************************
     * Description: Load an entity by its Id via proxy class so never return
     * null Input : "id": id of entity Outputs : Returns : proxy extends T
     * **************************************************************
     */
    public T loadById(final ID id);

    /**
     * **************************************************************
     * Description: Save an entity to db by its self Input : "entity": entity to
     * be saved Outputs : Returns : Entity was saved, null if action canceled or
     * **************************************************************
     */
    public T save(final T entity) throws Exception;

    /**
     * **************************************************************
     * Description: Delete an entity from db by its self Input : "entity":
     * entity to be deleted Outputs : Returns : "true" if successful, "false"
     * when action canceled or failed
     * **************************************************************
     */
    public boolean delete(final T entity) throws Exception;
}
