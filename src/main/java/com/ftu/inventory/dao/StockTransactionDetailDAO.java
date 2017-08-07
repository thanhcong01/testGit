/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.dao;

import com.ftu.inventory.bo.StockTransactionDetail;
import com.ftu.hibernate.GenericDAOHibernate;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author E5420
 */
public class StockTransactionDetailDAO extends GenericDAOHibernate<StockTransactionDetail, Long> {

    public StockTransactionDetailDAO() {
        super(StockTransactionDetail.class);
    }

    @Override
    public void saveOrUpdate(StockTransactionDetail g) {
        if (g != null) {
            getSession().saveOrUpdate(g);
        }
        getSession().flush();
    }

    public void save(StockTransactionDetail g) {
        if (g != null) {
            getSession().save(g);
        }
        getSession().flush();
    }

    @Override
    public void delete(StockTransactionDetail g) {
        super.delete(g);
        getSession().flush();
    }

    public void deleteByTaId(Long taId) {
        Query q2 = getSession().createQuery("Delete from StockTransactionDetail where stockTransactionId in (Select stockTransactionId from StockTransaction where transactionActionId = ?   )");
        q2.setParameter(0, taId);
        q2.executeUpdate();
        getSession().flush();
    }

    public List<StockTransactionDetail> getDetailsByTransaction(Long taId) {
        Query q = getSession().createQuery("Select s from StockTransactionDetail s where stockTransactionId in ( Select stockTransactionId from StockTransaction where transactionActionId = ? )");
        q.setParameter(0, taId);
        return q.list();
    }

    public StockTransactionDetail getDetailsByStockAndGoods(Long stId, Long gId, Long gsts) {
        Query q = getSession().createQuery("Select s from StockTransactionDetail s where stockTransactionId = ? and goodsId = ? and goodsStatus = ?");
        q.setParameter(0, stId);
        q.setParameter(1, gId);
        q.setParameter(2, gsts);
        if (q.list().isEmpty()) {
            return null;
        } else {
            return (StockTransactionDetail) q.list().get(0);
        }
    }

    public List<StockTransactionDetail> getDetailsByStock(Long stockId) {
        Query q = getSession().createQuery("Select s from StockTransactionDetail s where stockTransactionId = ?");
        q.setParameter(0, stockId);
        return q.list();
    }
    public StockTransactionDetail findStockTransactionDetailById(Long stockId) {
        Query q = getSession().createQuery("Select s from StockTransactionDetail s where stockTransactionDetailId = ?");
        q.setParameter(0, stockId);
        List lst = q.list();
        if(lst != null && !lst.isEmpty()){
            return (StockTransactionDetail)lst.get(0);
        }
        return null;
    }
}
