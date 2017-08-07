package com.ftu.hibernate;

import com.ftu.hibernate.HibernateUtil;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;

public abstract class GenericHibernateDAO<T, ID extends Serializable> {

    private Class<T> persistentClass;
    private HashMap<String, Session> sessions;

    public GenericHibernateDAO() {
        this.persistentClass = ((Class) ((java.lang.reflect.ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    public void setSessions(HashMap<String, Session> sessions) {
        this.sessions = sessions;
    }

    protected HashMap<String, Session> getSessions() {
        if (this.sessions == null) {
            this.sessions = HibernateUtil.getCurrentSessions();
        }
        return this.sessions;
    }

    protected Session getSession() {
        return getSession("default session");
    }

    protected Session getSession(String sessionName) {
        Session session = (Session) getSessions().get(sessionName);
        if (!session.getTransaction().isActive()) {
            session.beginTransaction();
        }
        return session;
    }

    public Class<T> getPersistentClass() {
        return this.persistentClass;
    }

    public T findById(String sessionName, ID id, boolean lock) {
        Object entity;
        if (lock) /*  88 */ {
            entity = getSession(sessionName).load(getPersistentClass(), id, LockMode.UPGRADE);
        } else {
            entity = getSession(sessionName).load(getPersistentClass(), id);
        }
        return (T) entity;
    }

    public List<T> findAll(String sessionName) {
        return findByCriteria(sessionName, new Criterion[0]);
    }

    public List<T> findByExample(String sessionName, T exampleInstance, String[] excludeProperty) {
        Criteria crit = getSession(sessionName).createCriteria(getPersistentClass());
        Example example = Example.create(exampleInstance);
        for (String exclude : excludeProperty) {
            example.excludeProperty(exclude);
        }
        crit.add(example);
        return crit.list();
    }

    public List<T> findByExample(String sessionName, T exampleInstance, boolean enableLike, boolean ignoreCase, String[] excludeProperty) {
        Criteria crit = getSession(sessionName).createCriteria(getPersistentClass());
        Example example = Example.create(exampleInstance);
        for (String exclude : excludeProperty) {
            example.excludeProperty(exclude);
        }
        if (enableLike) {
            example.enableLike(MatchMode.ANYWHERE);
        }
        if (ignoreCase) {
            example.ignoreCase();
        }

        crit.add(example);
        return crit.list();
    }

    public T makePersistent(String sessionName, T entity) {
        getSession(sessionName).saveOrUpdate(entity);
        return entity;
    }

    public void makeTransient(String sessionName, T entity) {
        getSession(sessionName).delete(entity);
    }

    public void flush(String sessionName) {
        getSession(sessionName).flush();
    }

    public void clear(String sessionName) {
        getSession(sessionName).clear();
    }

    protected List<T> findByCriteria(String sessionName, Criterion[] criterion) {
        return findByCriteria(sessionName, -1, -1, criterion);
    }

    protected List<T> findByCriteria(String sessionName, int firstResult, int maxResult, Criterion[] criterion) {
        Criteria crit = getSession(sessionName).createCriteria(getPersistentClass());
        for (Criterion c : criterion) {
            crit.add(c);
        }
        if (firstResult >= 0) {
            crit.setFirstResult(firstResult);
        }
        if (maxResult >= 0) {
            crit.setMaxResults(maxResult);
        }
        return crit.list();
    }

    protected List<T> findByCriteria(int firstResult, int maxResult, Criterion[] criterion) {
        return findByCriteria("default session", firstResult, maxResult, criterion);
    }

    protected List<T> findByCriteria(Criterion[] criterion) {
        return findByCriteria("default session", criterion);
    }

    public T findById(ID id, boolean lock) {
        return findById("default session", id, lock);
    }

    public List<T> findByExample(T exampleInstance, String[] excludeProperty) {
        return findByExample("default session", exampleInstance, excludeProperty);
    }

    public List<T> findByExample(T exampleInstance, boolean enableLike, boolean ignoreCase, String[] excludeProperty) {
        return findByExample("default session", exampleInstance, enableLike, ignoreCase, excludeProperty);
    }

    public T makePersistent(T entity) {
        return makePersistent("default session", entity);
    }

    public void makeTransient(T entity) {
        makeTransient("default session", entity);
    }

    public void flush() {
        flush("default session");
    }

    public void clear() {
        clear("default session");
    }

    public List<T> findAll() {
        return findAll("default session");
    }
}
