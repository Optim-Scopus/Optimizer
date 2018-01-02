package com.tcc.optimizer.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public abstract class GenericDaoImpl<T, ID extends Serializable> implements
        GenericDao<T, ID> {

    private Class<T> persistentClass;
    @PersistenceContext(unitName = "TccConn")
    private EntityManager entityManager;
    private String fromClause;

    @SuppressWarnings("unchecked")
    public GenericDaoImpl() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.fromClause = "SELECT x FROM " + this.persistentClass.getSimpleName() + " as x ";
    }

    protected EntityManager getEntityManager() {
        if (entityManager == null) {
            throw new IllegalStateException(
                    "Fail to inject EntityManager");
        }
        return entityManager;
    }

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    @Override
    public T create(T newInstance) {
        if (newInstance != null) {
            this.getEntityManager().persist(newInstance);
            return newInstance;
        }
        return null;
    }

    @Override
    public T read(ID id) {
        if (id != null) {
            return this.getEntityManager().find(persistentClass, id);
        }
        return null;
    }
    
    @Override
    public void detach(T transientObject){
        if(transientObject!=null){
            this.entityManager.detach(transientObject);
        }
    }

    @Override
    public void update(T transientObject) {
        if (transientObject != null) {
            this.getEntityManager().merge(transientObject);
            this.getEntityManager().flush();
        }
    }

    @Override
    public void delete(T persistentObject) {
        if (persistentObject != null) {
            this.getEntityManager().remove(persistentObject);
            this.getEntityManager().flush();
        }
    }
    
    @Override
    public List<T> getObjectsWithId(Long... ids) {
        if(ids.length==0){
            return new ArrayList<T>();
        }
        String el = createELStringById(ids);
        Query q = this.getEntityManager().createQuery(el);
        for (int i = 0; i < ids.length; i++) {
            q.setParameter(i+1, ids[i]);
        }
        return q.getResultList();
    }

    protected boolean exists(String field, Object o) {
        Query q = this.getEntityManager().createQuery("select x from " + this.getPersistentClass().getSimpleName() + " as x where " + field + " = ?1");
        q.setParameter(1, o);
        return q.getResultList().size() > 0;
    }

    @SuppressWarnings("unchecked")
    protected T getByUniqueField(String field, Object o) {
        Query q = this.getEntityManager().createQuery("select x from " + this.getPersistentClass().getSimpleName() + " as x where " + field + " = ?1");
        q.setParameter(1, o);
        q.setMaxResults(1);
        try {
            return (T) q.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    protected List<T> getByField(String field, Object o) {
         Query q = this.getEntityManager().createQuery(
                "SELECT x FROM " + this.getPersistentClass().getSimpleName() + " AS x WHERE "
                + "x." + field + " = ?1");
        q.setParameter("1", o);
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<T> readAll() {
        return this.getEntityManager().createQuery(fromClause).getResultList();
    }

    protected String createELStringById(Long... ids) {
        String start = "SELECT x FROM " + this.getPersistentClass().getSimpleName() + " AS x WHERE x.id IN (";
        String end = " )";
        String interrogacao = "?";
        StringBuilder sb = new StringBuilder();
        sb.append(start);
        for (int i = 1; i <= ids.length; i++) {
            sb.append(interrogacao);
            sb.append(i);
            if (i != ids.length) {
                sb.append(", ");
            }
        }
        sb.append(end);
        return sb.toString();
    }
}
