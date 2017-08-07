/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.sm.dao;

import com.ftu.hibernate.GenericDAOHibernate;
import com.ftu.hibernate.HibernateUtil;
import com.ftu.inventory.bo.Attachs;
import com.ftu.sm.bo.*;
import com.ftu.sm.bo.ActionGoodsMap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author E5420
 */
public class ActionGoodsMapDAO extends GenericDAOHibernate<ActionGoodsMap, Serializable> {

    public ActionGoodsMapDAO() {
        super(ActionGoodsMap.class);
    }

    @Override
    public void saveOrUpdate(ActionGoodsMap a) {
        getSession().saveOrUpdate(a);
        getSession().flush();
    }

    public List<ActionGoodsMap> findByActionCode(String actionCode) {
        Query query = getSession().getNamedQuery("ActionGoodsMap.findByActionCode");
        query.setParameter("actionCode", actionCode);
        if (query.list().isEmpty()) {
            return null;
        } else {
            return query.list();
        }
    }
}
