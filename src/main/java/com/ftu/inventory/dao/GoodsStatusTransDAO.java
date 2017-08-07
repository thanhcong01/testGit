/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.dao;

import com.ftu.inventory.bo.GoodsStatusTrans;
import com.ftu.hibernate.GenericDAOHibernate;

/**
 *
 * @author E5420
 */
public class GoodsStatusTransDAO extends GenericDAOHibernate<GoodsStatusTrans, Long> {

    public GoodsStatusTransDAO() {
        super(GoodsStatusTrans.class);
    }

    @Override
    public void saveOrUpdate(GoodsStatusTrans account) {
        if (account != null) {
            super.saveOrUpdate(account);
        }
        getSession().flush();
    }

    @Override
    public void delete(GoodsStatusTrans account) {
        super.delete(account);
        getSession().flush();
    }

}
