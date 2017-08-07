/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.dao;

import com.ftu.inventory.bo.GoodsStatusTransSerial;
import com.ftu.hibernate.GenericDAOHibernate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author E5420
 */
public class GoodsStatusTransSerialDAO extends GenericDAOHibernate<GoodsStatusTransSerial, Long> {

    public GoodsStatusTransSerialDAO() {
        super(GoodsStatusTransSerial.class);
    }

    @Override
    public void saveOrUpdate(GoodsStatusTransSerial g) {
        if (g != null) {
            super.saveOrUpdate(g);
        }
        getSession().flush();
    }

    @Override
    public void delete(GoodsStatusTransSerial g) {
        super.delete(g);
        getSession().flush();
    }

    private GoodsStatusTransSerial convertObjectToGoodsStatusTransSerial(Object[] o) {
        GoodsStatusTransSerial rs = new GoodsStatusTransSerial();
        rs.setGoodsStatusTransSerialId(o[0]==null?null:Long.parseLong(o[0].toString()));
        rs.setGoodsId(o[1]==null?null:Long.parseLong(o[1].toString()));
        rs.setSerial(o[2]==null?null:o[2].toString());
        rs.setOldGoodsStatus(o[3]==null?null:Long.parseLong(o[3].toString()));
        rs.setNewGoodsStatus(o[4]==null?null:Long.parseLong(o[4].toString()));
        rs.setGoodsStatusTransId(o[5]==null?null:Long.parseLong(o[5].toString()));
        rs.setProviderId(o[6]==null?null:Long.parseLong(o[6].toString()));
        rs.setTransDatetime(o[7]==null?null:(Date) o[7]);
        return rs;
    }

    public List<GoodsStatusTransSerial> getBySerial(Long prvId, Long gId, String serial) {
        Query q = getSession().createSQLQuery("SELECT g.GOODS_STATUS_TRANS_SERIAL_ID, g.GOODS_ID, g.SERIAL, g.OLD_GOODS_STATUS, g.NEW_GOODS_STATUS, g.GOODS_STATUS_TRANS_ID, g.PROVIDER_ID, g.TRANS_DATETIME FROM GOODS_STATUS_TRANS_SERIAL g JOIN GOODS_STATUS_TRANS gt ON g.GOODS_STATUS_TRANS_ID = gt.GOODS_STATUS_TRANS_ID WHERE g.serial = ? AND g.GOODS_ID = ? AND g.PROVIDER_ID = ? ORDER BY gt.TRANS_DATETIME");
        q.setParameter(0, serial);
        q.setParameter(1, gId);
        q.setParameter(2, prvId);
        List<Object[]> a = q.list();
        List<GoodsStatusTransSerial> rs = new ArrayList<>();
        for (Object[] o : a) {
            rs.add(convertObjectToGoodsStatusTransSerial(o));
        }
        return rs;
    }
}
