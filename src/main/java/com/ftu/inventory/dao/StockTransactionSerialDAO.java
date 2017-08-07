/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.dao;

import com.ftu.constanst.InventoryConstanst;
import com.ftu.hibernate.GenericDAOHibernate;
import com.ftu.inventory.bo.StockTransactionDetail;
import com.ftu.inventory.bo.StockTransactionSerial;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;

/**
 *
 * @author E5420
 */
public class StockTransactionSerialDAO extends GenericDAOHibernate<StockTransactionSerial, Long> {

    public StockTransactionSerialDAO() {
        super(StockTransactionSerial.class);
    }

    @Override
    public void saveOrUpdate(StockTransactionSerial g) {
        if (g != null) {
            super.saveOrUpdate(g);
        }
        getSession().flush();
    }

    public void insertByEtag(StockTransactionDetail d,Long taId) {
        Query q = getSession().createSQLQuery("INSERT INTO STOCK_TRANSACTION_SERIAL ("
        		+ "STOCK_TRANSACTION_SERIAL_ID,STOCK_TRANSACTION_DETAIL_ID,IM_TRANSACTION_ID,GOODS_ID,SERIAL,GOODS_STATUS,PROVIDER_ID,EQUIPMENT_CODE,SERIAL_SEARCH) "
        		+ "SELECT STOCK_TRANSACTION_SERIAL_SEQ.nextval, ?, IM_TRANSACTION_ID, EQUIPMENT_PROFILE_ID,SERIAL, EQUIPMENT_STATUS, PROVIDER_ID, EQUIPMENT_CODE, SERIAL FROM EQUIPMENT_DETAIL WHERE EQUIPMENT_PROFILE_ID = ? AND EQUIPMENT_STATUS = ? AND IM_TRANSACTION_ID = ? ");
        q.setParameter(0, d.getStockTransactionDetailId());
        q.setParameter(1, d.getGoodsId());
        q.setParameter(2, d.getGoodsStatus());
        q.setParameter(3, taId);
        q.executeUpdate();
    }

    public void save(StockTransactionSerial g) {
        if (g != null) {
            getSession().save(g);
        }
        getSession().flush();
    }

    public List<StockTransactionSerial> searchWaran(String serial) {
        List param = new ArrayList();
        StringBuilder s = new StringBuilder("Select s from StockTransactionSerial s where  1 = 1 ");

        if(serial!=null && !serial.isEmpty()){
            s.append(" and s.serial = ? ");
            param.add(serial);
        }
        s.append(" and (s.equipmentProfileStatus = ? OR s.equipmentProfileStatus = ?) ");
        param.add(InventoryConstanst.GoodsStatus.ERROR);
        param.add(InventoryConstanst.GoodsStatus.USED);

        s.append("ORDER BY SERIAL ");
        Query q = getSession().createQuery(s.toString());
        for (int i = 0; i < param.size(); i++) {
            q.setParameter(i, param.get(i));
        }

        if (q.list().isEmpty()) {
            return new ArrayList<StockTransactionSerial>();
        } else {
            return q.list();
        }
    }

    private List<StockTransactionSerial> search(boolean isNotError,  String from, String to, Long providerId, Long gid, Long gstatus, Long taId, Integer start, Integer get, String sortField, Boolean desc) {
        List param = new ArrayList();
        StringBuilder s = new StringBuilder("Select s from StockTransactionSerial s where  imTransactionId= ? ");
        param.add(taId);
        if (gid != null && gid > 0) {
            s.append(" AND  equipmentProfileId = ? ");
            param.add(gid);
        }
//        param.add(gid);
        if (providerId != null && providerId > 0) {
            s.append(" AND s.providerId = ? ");
            param.add(providerId);
        }
        if (from != null && !from.trim().isEmpty()) {
            s.append(" AND UPPER(SERIAL) LIKE ? ");
            param.add("%"+from.trim().toUpperCase()+"%");
        }
//        if (from != null && !from.trim().isEmpty()) {
//            s.append(" AND TO_NUMBER(s.serialSearch) >= TO_NUMBER(?) ");
//            param.add(from);
//        }
//        if (to != null && !to.trim().isEmpty()) {
//            s.append(" AND TO_NUMBER(s.serialSearch) <= TO_NUMBER(?) ");
//            param.add(to);
//        }
//        s.append(" AND s.serialSearch is not null ");
        if (gstatus != null && gstatus > 0) {
            s.append(" and s.equipmentProfileStatus = ? ");
            param.add(gstatus);
        }

        //congdt3_waranty
        if(isNotError){
            s.append(" AND s.equipmentProfileStatus <> ? ");
            param.add(InventoryConstanst.GoodsStatus.ERROR);
        }
        //end
//        s.append("ORDER BY LENGTH(SERIAL), TO_NUMBER(s.serialSearch) ");
        if(sortField != null){
            if(desc){
                s.append(" order by UPPER(" + sortField + ") desc ");
            } else{
                s.append(" order by UPPER(" + sortField + ")  asc ");
            }
        }else {
            s.append("ORDER BY SERIAL");
        }
        Query q = getSession().createQuery(s.toString());
        for (int i = 0; i < param.size(); i++) {
            q.setParameter(i, param.get(i));
        }
        if (start != null && get != null && get > 0) {
            q.setFirstResult(start);
            q.setMaxResults(get);
        }
        return q.list();
    }

    public Long searchSize(boolean isNotError, String from, String to, Long providerId, Long gid, Long gstatus, Long taId) {
        List param = new ArrayList();
        StringBuilder s = new StringBuilder("Select Count(s) from StockTransactionSerial s where imTransactionId= ? and equipmentProfileId = ?");
        param.add(taId);
        param.add(gid);
        if (providerId != null && providerId > 0) {
            s.append(" AND s.providerId = ? ");
            param.add(providerId);
        }
        if (from != null && !from.trim().isEmpty()) {
            s.append(" AND UPPER(SERIAL) LIKE ? ");
            param.add("%"+from.trim().toUpperCase()+"%");
        }
//        if (to != null && !to.trim().isEmpty()) {
//            s.append(" AND TO_NUMBER(SERIAL_SEARCH) <= TO_NUMBER(?) ");
//            param.add(to);
//        }
//        s.append(" AND SERIAL_SEARCH is not null ");
        if (gstatus != null && gstatus > 0) {
            s.append(" and equipmentProfileStatus = ? ");
            param.add(gstatus);
        }
        //congdt3_waranty
        if(isNotError){
            s.append(" AND s.equipmentProfileStatus <> ? ");
            param.add(InventoryConstanst.GoodsStatus.ERROR);
        }
        //end
//        System.out.println("Query searchSize: ");
//		System.out.println(s.toString());
//		System.out.println("Params: ");
//		System.out.println(param);
        Query q = getSession().createQuery(s.toString());
        for (int i = 0; i < param.size(); i++) {
            q.setParameter(i, param.get(i));
        }
        return (Long) q.uniqueResult();
    }

    private StockTransactionSerial convertRowToStockTransactionSerial(Object[] o) {
        StockTransactionSerial sts = new StockTransactionSerial();
        sts.setStockTransactionDetailId(o[0] == null ? null : Long.parseLong(o[0].toString()));
        sts.setStockTransactionSerialId(o[1] == null ? null : Long.parseLong(o[1].toString()));
        sts.setStsDatetime(o[2] == null ? null : (Date) o[2]);
        sts.setEquipmentProfileId(o[3] == null ? null : Long.parseLong(o[3].toString()));
        sts.setSerial(o[4] == null ? null : o[4].toString());
        sts.setImTransactionId(o[5] == null ? null : Long.parseLong(o[5].toString()));
        sts.setEquipmentProfileStatus(o[6] == null ? null : Long.parseLong(o[6].toString()));
        sts.setProviderId(o[7] == null ? null : Long.parseLong(o[7].toString()));
//        sts.setEquipmentCode(o[8] == null ? null : o[8].toString());
        sts.setSerialSearch(o[8] == null ? null : o[8].toString());
        sts.setReason(o[9] == null ? null : o[9].toString());
        return sts;
    }


    public List<StockTransactionSerial> searchTransSearch(String from, String to, Long providerId, Long gid, Long gstatus,
                                                          Long taId, Long stockId,Long detailId, Integer start, Integer get,
                                                          String sortField, Boolean desc) {
        if (stockId == null) {
            return search(true,from, to, providerId, gid, gstatus, taId, start, get, sortField, desc);
        }
        List param = new ArrayList();
        StringBuilder s = new StringBuilder("SELECT s.STOCK_TRANSACTION_DETAIL_ID, s.STOCK_TRANSACTION_SERIAL_ID, " +
                " s.STS_DATETIME, s.GOODS_ID, s.SERIAL, s.IM_TRANSACTION_ID, " +
                " s.GOODS_STATUS, s.PROVIDER_ID, s.SERIAL_SEARCH, s.REASON " +
                " FROM STOCK_TRANSACTION_SERIAL s LEFT JOIN STOCK_TRANSACTION_DETAIL std " +
                " ON s.STOCK_TRANSACTION_DETAIL_ID=std.STOCK_TRANSACTION_DETAIL_ID " +
                " WHERE 1=1 AND s.IM_TRANSACTION_ID= ? " +
                " ");
//        param.add(stockId);
        param.add(taId);

        if (stockId != null && stockId > 0) {
            s.append(" AND std.STOCK_TRANSACTION_ID = ? ");
            param.add(stockId);
        }
//        param.add(gid);
        if (gid != null && gid > 0) {
            s.append(" AND s.GOODS_ID = ? ");
            param.add(gid);
        }
        if (providerId != null && providerId > 0) {
            s.append(" AND s.PROVIDER_ID = ? ");
            param.add(providerId);
        }
        if (from != null && !from.trim().isEmpty()) {
            s.append(" AND UPPER(SERIAL) LIKE ? ");
            param.add("%"+from.trim().toUpperCase()+"%");
        }
//        if (from != null && !from.trim().isEmpty()) {
//            s.append(" AND TO_NUMBER(SERIAL_SEARCH) >= TO_NUMBER(?) ");
//            param.add(from);
//        }
//        if (to != null && !to.trim().isEmpty()) {
//            s.append(" AND TO_NUMBER(SERIAL_SEARCH) <= TO_NUMBER(?) ");
//            param.add(to);
//        }
//        s.append(" AND SERIAL_SEARCH is not null ");
        if (gstatus != null && gstatus > 0) {
            s.append(" AND s.GOODS_STATUS = ? ");
            param.add(gstatus);
        }
        if (detailId != null && detailId > 0) {
            s.append(" AND s.STOCK_TRANSACTION_DETAIL_ID = ? ");
            param.add(detailId);
        }
        if(sortField != null){
            if(desc){
                s.append(" order by UPPER(" + sortField + ") desc ");
            } else{
                s.append(" order by UPPER(" + sortField + ")  asc ");
            }
        }else {
            s.append("ORDER BY SERIAL");
        }

        Query q = getSession().createSQLQuery(s.toString());
        for (int i = 0; i < param.size(); i++) {
            q.setParameter(i, param.get(i));
        }
        if (start != null && get != null && get > 0) {
            q.setFirstResult(start);
            q.setMaxResults(get);
        }
        List<Object[]> a = q.list();
        if (a.isEmpty()) {
            return new ArrayList<StockTransactionSerial>();
        } else {
            List<StockTransactionSerial> rs = new ArrayList<>();
            for (Object[] o : a) {
                rs.add(convertRowToStockTransactionSerial(o));
            }
            return rs;
        }
    }

    public List<StockTransactionSerial> searchTransSearchXK(String from, String to, Long providerId, Long gid, Long gstatus,
                                                          Long taId, Long stockId,Long detailId, Integer start, Integer get,
                                                            String sortField, Boolean desc) {
        if (stockId == null) {
            return search(false,from, to, providerId, gid, gstatus, taId, start, get, sortField, desc);
        }
        List param = new ArrayList();
        StringBuilder s = new StringBuilder("SELECT s.STOCK_TRANSACTION_DETAIL_ID, s.STOCK_TRANSACTION_SERIAL_ID, " +
                " s.STS_DATETIME, s.GOODS_ID, s.SERIAL, s.IM_TRANSACTION_ID, " +
                " s.GOODS_STATUS, s.PROVIDER_ID, s.SERIAL_SEARCH, s.REASON " +
                " FROM STOCK_TRANSACTION_SERIAL s LEFT JOIN STOCK_TRANSACTION_DETAIL std " +
                " ON s.STOCK_TRANSACTION_DETAIL_ID=std.STOCK_TRANSACTION_DETAIL_ID " +
                " WHERE 1=1 AND s.IM_TRANSACTION_ID= ? " +
                " ");
//        param.add(stockId);
        param.add(taId);

        if (stockId != null && stockId > 0) {
            s.append(" AND std.STOCK_TRANSACTION_ID = ? ");
            param.add(stockId);
        }
//        param.add(gid);
        if (gid != null && gid > 0) {
            s.append(" AND s.GOODS_ID = ? ");
            param.add(gid);
        }
        if (providerId != null && providerId > 0) {
            s.append(" AND s.PROVIDER_ID = ? ");
            param.add(providerId);
        }
        if (from != null && !from.trim().isEmpty()) {
            s.append(" AND UPPER(SERIAL) LIKE ? ");
            param.add("%"+from.trim().toUpperCase()+"%");
        }
//        if (from != null && !from.trim().isEmpty()) {
//            s.append(" AND TO_NUMBER(SERIAL_SEARCH) >= TO_NUMBER(?) ");
//            param.add(from);
//        }
//        if (to != null && !to.trim().isEmpty()) {
//            s.append(" AND TO_NUMBER(SERIAL_SEARCH) <= TO_NUMBER(?) ");
//            param.add(to);
//        }
//        s.append(" AND SERIAL_SEARCH is not null ");
        if (gstatus != null && gstatus > 0) {
            s.append(" AND s.GOODS_STATUS = ? ");
            param.add(gstatus);
        }
        if (detailId != null && detailId > 0) {
            s.append(" AND s.STOCK_TRANSACTION_DETAIL_ID = ? ");
            param.add(detailId);
        }
        if(sortField != null){
            if(desc){
                s.append(" order by UPPER(" + sortField + ") desc ");
            } else{
                s.append(" order by UPPER(" + sortField + ")  asc ");
            }
        }else {
            s.append("ORDER BY SERIAL");
        }

        Query q = getSession().createSQLQuery(s.toString());
        for (int i = 0; i < param.size(); i++) {
            q.setParameter(i, param.get(i));
        }
        if (start != null && get != null && get > 0) {
            q.setFirstResult(start);
            q.setMaxResults(get);
        }
        List<Object[]> a = q.list();
        if (a.isEmpty()) {
            return new ArrayList<StockTransactionSerial>();
        } else {
            List<StockTransactionSerial> rs = new ArrayList<>();
            for (Object[] o : a) {
                rs.add(convertRowToStockTransactionSerial(o));
            }
            return rs;
        }
    }

    public Long searchTransSearchSize(String from, String to, Long providerId, Long gid, Long gstatus, Long taId, Long stockId) {
        if (stockId == null) {
            return searchSize(true,from, to, providerId, gid, gstatus, taId);
        }
        List param = new ArrayList();
        StringBuilder s = new StringBuilder("SELECT COUNT(1) FROM STOCK_TRANSACTION_SERIAL s JOIN STOCK_TRANSACTION_DETAIL std ON s.STOCK_TRANSACTION_DETAIL_ID=std.STOCK_TRANSACTION_DETAIL_ID WHERE std.STOCK_TRANSACTION_Id = ? AND s.IM_TRANSACTION_ID= ?  AND s.GOODS_ID = ? ");
        param.add(stockId);
        param.add(taId);
        param.add(gid);
        if (providerId != null && providerId > 0) {
            s.append(" AND s.PROVIDER_ID = ? ");
            param.add(providerId);
        }
        if (from != null && !from.trim().isEmpty()) {
            s.append(" AND UPPER(SERIAL) LIKE ? ");
            param.add("%"+from.trim().toUpperCase()+"%");
        }
////        if (from != null && !from.trim().isEmpty()) {
////            s.append(" AND TO_NUMBER(SERIAL_SEARCH) >= TO_NUMBER(?) ");
////            param.add(from);
////        }
////        if (to != null && !to.trim().isEmpty()) {
////            s.append(" AND TO_NUMBER(SERIAL_SEARCH) <= TO_NUMBER(?) ");
////            param.add(to);
////        }
//        s.append(" AND SERIAL_SEARCH is not null ");
        if (gstatus != null && gstatus > 0) {
            s.append(" AND s.GOODS_STATUS = ? ");
            param.add(gstatus);
        }
        Query q = getSession().createSQLQuery(s.toString());
        for (int i = 0; i < param.size(); i++) {
            q.setParameter(i, param.get(i));
        }
        return Long.parseLong(q.uniqueResult().toString());
    }

    public List<StockTransactionSerial> getByDetail(Long detailId) {
        Query q = getSession().createQuery("Select s from StockTransactionSerial s where stockTransactionDetailId= ? ORDER BY  SERIAL  ");
        q.setParameter(0, detailId);
        if (q.list().isEmpty()) {
            return null;
        } else {
            return q.list();
        }
    }

    public Long getSizeByDetail(Long detailId) {
        Query q = getSession().createQuery("Select count(1) from StockTransactionSerial s where stockTransactionDetailId= ? ORDER BY LENGTH(SERIAL), SERIAL  ");
        q.setParameter(0, detailId);
        return (Long) q.uniqueResult();
    }

    public Long getSize(Long providerId, Long gid, Long taId, Long goodsStatus) {
        Query q = getSession().createQuery("Select count(1) from StockTransactionSerial s where imTransactionId= ? and providerId = ? and equipmentProfileId = ? and equipmentProfileStatus = ? ");
        q.setParameter(0, taId);
        q.setParameter(1, providerId);
        q.setParameter(2, gid);
        q.setParameter(3, goodsStatus);
        return (Long) q.uniqueResult();
    }

    @Override
    public void delete(StockTransactionSerial g) {
        super.delete(g);
        getSession().flush();
    }

    public List<StockTransactionSerial> getStockSerials(Long taId) {
        Query q = getSession().createQuery("Select s from StockTransactionSerial s where imTransactionId = ? ");
        q.setParameter(0, taId);
        return q.list();
    }

    public void deleteByTaId(Long taId) {
        Query q1 = getSession().createQuery("Delete from StockTransactionSerial where imTransactionId = ? ");
        q1.setParameter(0, taId);
        q1.executeUpdate();
        getSession().flush();
    }

    public void InsertByStockEquipmentsInvSerial(Long detailId, Long taId, Long goodsId, Long goodsStatus) {
        StringBuilder s = new StringBuilder("Insert into StockTransactionSerial(imTransactionId,stockTransactionDetailId," +
                " equipmentProfileId,serial,equipmentProfileStatus,providerId "
        		+ ",serialSearch"
        		+ ") select ? , ? , equipmentProfileId,serial, equipmentProfileStatus, providerId "
        		+ ", serialSearch"
        		+ " from StockGoodsInvSerial sg where sg.equipmentProfileId = ?  and sg.currentTaId = ?");
        Query q = getSession().createQuery(s.toString());
        q.setParameter(0, taId);
        q.setParameter(1, detailId);
        q.setParameter(2, goodsId);
        q.setParameter(3, taId);
        q.executeUpdate();
        getSession().flush();
    }

    public void InsertByStockEquipmentsInvSerialWaran(Long detailId, Long taId, Long goodsId) {
        StringBuilder s = new StringBuilder("Insert into StockTransactionSerial(imTransactionId,stockTransactionDetailId,equipmentProfileId,serial,equipmentProfileStatus,providerId "
                + ",serialSearch"
                + ") select ? , ? , equipmentProfileId,serial, equipmentProfileStatus, providerId "
                + ", serialSearch"
                + " from StockGoodsInvSerial sg where sg.equipmentProfileId = ?  and sg.currentTaId = ?");
        Query q = getSession().createQuery(s.toString());
        q.setParameter(0, taId);
        q.setParameter(1, detailId);
        q.setParameter(2, goodsId);
        q.setParameter(3, taId);
        q.executeUpdate();
        getSession().flush();
    }

    public void InsertByStockTransactionSerial(Long newDetail, Long oldDetail, Long taId) {
        StringBuilder s = new StringBuilder("Insert into StockTransactionSerial(imTransactionId,stockTransactionDetailId,equipmentProfileId,serial,equipmentProfileStatus,providerId) select ? , ? , equipmentProfileId,serial, equipmentProfileStatus, providerId from StockTransactionSerial where ss.stockTransactionDetailId = ? ");
        Query q = getSession().createQuery(s.toString());
        q.setParameter(0, taId);
        q.setParameter(1, newDetail);
        q.setParameter(2, oldDetail);
        q.executeUpdate();
        getSession().flush();
    }
    public StockTransactionSerial getStockSerials(String serial, Long goodId) {
        Query q = getSession().createQuery("Select s from StockTransactionSerial s " +
                " where serial like ?  and equipmentProfileId = ? ");
        q.setParameter(0, serial);
        q.setParameter(1, goodId);
        List lst =q.list();
        if(lst != null && !lst.isEmpty()){
            lst.get(0);
        }
        return null;
    }
}
