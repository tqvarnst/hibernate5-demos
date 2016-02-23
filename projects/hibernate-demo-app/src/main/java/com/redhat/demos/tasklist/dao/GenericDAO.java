package com.redhat.demos.tasklist.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.LockModeType;

public interface GenericDAO<T, ID extends Serializable> {
	
    T findById(ID id);

    T findById(ID id, LockModeType lockModeType);

    T findReferenceById(ID id);

    List<T> findAll();

    Long getCount();

    T makePersistent(T entity);

    void makeTransient(T entity);

    void checkVersion(T entity, boolean forceUpdate);

}
