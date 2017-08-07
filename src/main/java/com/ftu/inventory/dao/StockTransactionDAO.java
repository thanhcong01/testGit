/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.dao;

import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.bo.StockTransaction;
import com.ftu.hibernate.GenericDAOHibernate;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author E5420
 */
public class StockTransactionDAO extends GenericDAOHibernate<StockTransaction, Long> {

    public StockTransactionDAO() {
        super(StockTransaction.class);
    }

    @Override
    public StockTransaction findById(Long id) {
        Query q = getSession().getNamedQuery("StockTransaction.findByStockTransactionId");
        q.setParameter("stockTransactionId", id);
        if (q.list().isEmpty()) {
            return null;
        } else {
            return (StockTransaction) q.list().get(0);
        }
    }

    @Override
    public void saveOrUpdate(StockTransaction g) {
        if (g != null) {
            getSession().saveOrUpdate(g);
        }
        getSession().flush();
    }

    public List<StockTransaction> getStockTransactionList(Long taId) {
        Query q = getSession().createQuery("Select t from StockTransaction t where transactionActionId = ? order by finishDatetime ");
        q.setParameter(0, taId);
        if (q.list().isEmpty()) {
            return null;
        } else {
            return q.list();
        }
    }

    public StockTransaction getByTransaction(Long taId, String status) {
        Query q = getSession().createQuery("Select t from StockTransaction t where transactionActionId = ? and stockTransStatus = ? ");
        q.setParameter(0, taId);
        q.setParameter(1, status);
        q.setFirstResult(0);
        q.setMaxResults(1);
        if (q.list().isEmpty()) {
            return null;
        } else {
            return (StockTransaction) q.list().get(0);
        }
    }

    public StockTransaction getLastByTransaction(Long taId) {
        Query q = getSession().createQuery("Select t from StockTransaction t where transactionActionId = ? order by stockTransactionDatetime desc ");
        q.setParameter(0, taId);
        q.setFirstResult(0);
        q.setMaxResults(1);
        if (q.list().isEmpty()) {
            return null;
        } else {
            return (StockTransaction) q.list().get(0);
        }
    }

    public StockTransaction getByTransactionAndType(Long taId, String type) {
        Query q = getSession().createQuery("Select t from StockTransaction t where transactionActionId = ? and stockTransactionType = ? ");
        q.setParameter(0, taId);
        q.setParameter(1, type);
        q.setFirstResult(0);
        q.setMaxResults(1);
        if (q.list().isEmpty()) {
            return null;
        } else {
            return (StockTransaction) q.list().get(0);
        }
    }

    @Override
    public void delete(StockTransaction g) {
        super.delete(g);
        getSession().flush();
    }

    public void rejectStock(Long taId, Long staffId) {
        Query q1 = getSession().createQuery("Update StockTransaction set stockTransStatus = ? , deliveryType = ? , importStaffId = ? , finishDatetime = ? where transactionActionId = ?");
        q1.setParameter(0, InventoryConstanst.StockTransactionStatus.REJECT);
        q1.setParameter(1, InventoryConstanst.StockTransactionDeliveryType.TRANS);
        q1.setParameter(2, staffId);
        q1.setParameter(3, new Date()); 
        q1.setParameter(4, taId);
        q1.executeUpdate();
        getSession().flush();
    }

    public void approveStock(Long taId, Long staffId) {
        Query q1 = getSession().createQuery("Update StockTransaction set stockTransStatus = ? , deliveryType = ? , importStaffId = ? , finishDatetime = ? where transactionActionId = ?");
        q1.setParameter(0, InventoryConstanst.StockTransactionStatus.SUCCESS);
        q1.setParameter(1, InventoryConstanst.StockTransactionDeliveryType.TRANS);
        q1.setParameter(2, staffId);
        q1.setParameter(3, new Date());     
        q1.setParameter(4, taId);
        q1.executeUpdate();
        getSession().flush();
    }

    public void deleteByTaId(Long taId) {
        Query q3 = getSession().createQuery("Delete from StockTransaction where transactionActionId = ? ");
        q3.setParameter(0, taId);
        q3.executeUpdate();
        getSession().flush();
    }
}
