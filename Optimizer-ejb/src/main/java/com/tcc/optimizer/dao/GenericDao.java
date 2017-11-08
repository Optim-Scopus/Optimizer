package com.tcc.optimizer.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Provides basic access to a generic entity. <br>
 * Its possible to create, update, read and delete a entry at Table
 *
 * @param <T> Entity
 * @param <PK> Primary Key
 */
public interface GenericDao <T, PK extends Serializable> {

    T create(T newInstance);

    T read(PK id);

    void update(T transientObject);

    void delete(T persistentObject);

    List<T> readAll();
    
    List<T> getObjectsWithId(Long... ids);
    
    public void detach(T transientObject);
}