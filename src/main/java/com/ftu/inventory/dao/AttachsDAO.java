/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.dao;

import com.ftu.inventory.bo.Attachs;
import com.ftu.hibernate.GenericDAOHibernate;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author E5420
 */
public class AttachsDAO extends GenericDAOHibernate<Attachs, Serializable> {

    public AttachsDAO() {
        super(Attachs.class);
    }

    @Override
    public void saveOrUpdate(Attachs a) {
        getSession().saveOrUpdate(a);
        getSession().flush();
    }

    public Attachs getById(Long id) {
        Query query = getSession().createQuery("Select a from Attachs a where attachId = ? and ( isDeleted is null or isDeleted = 0 )");
        query.setParameter(0, id);
        if (query.list().isEmpty()) {
            return null;
        } else {
            return (Attachs) query.list().get(0);
        }
    }

    public List<Attachs> getByObjectId(Long objectID) {
        Query query = getSession().createQuery("Select a from Attachs a where objectId = ? and ( isDeleted is null or isDeleted = 0 )");
        query.setParameter(0, objectID);
        if (query.list().isEmpty()) {
            return null;
        } else {
            return (List<Attachs>) query.list().get(0);
        }
    }

    public Attachs getLastByObjectId(Long objectID) {
        Query query = getSession().createQuery("Select a from Attachs a where objectId = ? and ( isDeleted is null or isDeleted = 0 )");
        query.setParameter(0, objectID);
        if (query.list().isEmpty()) {
            return null;
        } else {
            return (Attachs) query.list().get(0);
        }
    }

    @Override
    public void delete(Attachs a) {
        a.setIsDeleted(1L);
        saveOrUpdate(a);
        getSession().flush();
    }
}
