/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.dao;

import com.ftu.inventory.bo.EtagDetail;
import com.ftu.hibernate.GenericDAOHibernate;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author E5420
 */
public class EtagDetailDAO extends GenericDAOHibernate<EtagDetail, Serializable> {

    public EtagDetailDAO() {
        super(EtagDetail.class);
    }

    @Override
    public void saveOrUpdate(EtagDetail a) {
        getSession().saveOrUpdate(a);
        getSession().flush();
    }

    public void save(EtagDetail a) {
        getSession().save(a);
        getSession().flush();
    }

    public void insertBysql(EtagDetail a) {
        Query q = getSession().createSQLQuery("INSERT INTO ETAG_DETAIL ( ID , SERIAL , ITC , PID , IM_TRANSACTION_ID , PROVIDER_ID , GOODS_ID , GOODS_STATUS , ETAG_NUMBER, SERIAL_SEARCH ) VALUES ( ETAG_DETAIL_SEQ.nextval , :serial , :itc , :pid , :taId , :prvId , :gid , :gsts , :eNum, :sSearch )");
        q.setParameter("serial", a.getSerial());
        q.setParameter("itc", a.getItc());
        q.setParameter("pid", a.getPid());
        q.setParameter("taId", a.getImTransactionId().toString());
        q.setParameter("prvId", a.getProviderId().toString());
        q.setParameter("gid", a.getGoodsId().toString());
        q.setParameter("gsts", a.getGoodsStatus().toString());
        q.setParameter("eNum", a.getEtagNumber());
        q.setParameter("sSearch", a.getSerialSearch());
        q.executeUpdate();
    }

    public EtagDetail getBySerial(Long taId, String serial) {
        Query query = getSession().createQuery("Select a from EtagDetail a where imTransactionId = ? and serial = ?");
        query.setParameter(0, taId);
        query.setParameter(1, serial);
        if (query.list().isEmpty()) {
            return null;
        } else {
            return (EtagDetail) query.list().get(0);
        }
    }

    public void deleteByTaId(Long taId) {
        Query q4 = getSession().createQuery("Delete from EtagDetail where imTransactionId = ? ");
        q4.setParameter(0, taId);
        q4.executeUpdate();
        getSession().flush();
    }
}
