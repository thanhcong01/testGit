/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.sm.dao;

import com.ftu.hibernate.GenericDAOHibernate;
import com.ftu.hibernate.HibernateUtil;
import com.ftu.sm.bo.TransactionDetail;
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
public class TransactionDetailDAO extends GenericDAOHibernate<TransactionDetail, Serializable> {

    public TransactionDetailDAO() {
        super(TransactionDetail.class);
    }

    @Override
    public void saveOrUpdate(TransactionDetail a) {
        getSession().saveOrUpdate(a);
        getSession().flush();

    }
}
