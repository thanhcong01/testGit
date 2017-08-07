/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.dao;

import com.ftu.constanst.InventoryConstanst;
import com.ftu.hibernate.GenericDAOHibernate;
import com.ftu.inventory.bo.InvImExReportBo;
import com.ftu.inventory.bo.StockGoodsInvNoSerial;
import com.ftu.inventory.bo.StockGoodsInvSerial;
import com.ftu.inventory.bo.TransactionAction;
import com.ftu.inventory.dto.StockGoodsInvSerialDTO;
import com.ftu.utils.DateTimeUtils;
import com.ftu.utils.StringUtil;
import org.hibernate.Query;
import org.hibernate.SQLQuery;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author E5420
 */
public class StockGoodsInvNoSerialDAO extends GenericDAOHibernate<StockGoodsInvNoSerial, Long> {

    public StockGoodsInvNoSerialDAO() {
        super(StockGoodsInvNoSerial.class);
    }

    @Override
    public void saveOrUpdate(StockGoodsInvNoSerial g) {
        if (g != null) {
            super.saveOrUpdate(g);
        }
        getSession().flush();
    }

    public void updateStockTransBySql(StockGoodsInvNoSerial g) {
        Query q = getSession().createSQLQuery(
                "UPDATE STOCK_GOODS_INV_NOSERIAL set STOCK_STATUS = ?, CURRENT_TA_ID = ? where ID = ? and STOCK_STATUS = ?");
        q.setParameter(0, g.getStockStatus());
        q.setParameter(1, g.getCurrentTaId());
        q.setParameter(2, g.getId());
        q.setParameter(3, InventoryConstanst.StockStatus.INSTOCK);
        q.executeUpdate();
    }
    public void updateStockTransBySqlKTV(StockGoodsInvNoSerial g) {
        Query q = getSession().createSQLQuery(
                "UPDATE STOCK_GOODS_INV_NOSERIAL set STOCK_STATUS = ?, CURRENT_TA_ID = ? where ID = ? ");
        q.setParameter(0, g.getStockStatus());
        q.setParameter(1, g.getCurrentTaId());
        q.setParameter(2, g.getId());
        q.executeUpdate();
    }

    public Long getQuantityByProfileId(Long profileId) {
        Query q = getSession().createQuery(
                "Select s from StockGoodsInvNoSerial s " +
                        " where s.equipmentProfileId = ? and stockStatus <> ?  ");
        q.setParameter(0, profileId);
        q.setParameter(1, InventoryConstanst.StockStatus.CANCEL);
        if (q.list().isEmpty()) {
            return 0L;
        } else {
            List<StockGoodsInvNoSerial> lst = q.list();
            Long t = 0L;
            for (StockGoodsInvNoSerial obj:lst) {
                if(obj.getQuantity()!=null){
                    t+=obj.getQuantity();
                }
            }
            return t;
        }
    }

    public void updateStatusBySql(Long gStatus, String stockSts, Long id, Long quantity) {
        StringBuilder s = new StringBuilder("UPDATE STOCK_GOODS_INV_NOSERIAL set ");
        List param = new ArrayList();
        Boolean isTrue = false;
        if (gStatus != null) {
            s.append(" EQUIPMENT_STATUS = ? ");
            param.add(gStatus);
            isTrue = true;
        }
        if (quantity != null) {
            if (isTrue) {
                s.append(",");
            }
            s.append(" QUANTITY = ? ");
            param.add(quantity);
            isTrue = true;
        }
        if (stockSts != null) {
            if (isTrue) {
                s.append(",");
            }
            s.append(" STOCK_STATUS = ? ");
            param.add(stockSts);
        }
        s.append(" where ID = ? ");
        param.add(id);
        Query q = getSession().createSQLQuery(s.toString());
        for (int i = 0; i < param.size(); i++) {
            q.setParameter(i, param.get(i));
        }
        q.executeUpdate();
    }

    public void updateStockGoodsEquipstatus(List<Long> ids, Long equimentStatus) {
        StringBuilder s = new StringBuilder("UPDATE STOCK_GOODS_INV_NOSERIAL set ");
        List param = new ArrayList();
        s.append(" EQUIPMENT_STATUS = ? ");
        param.add(equimentStatus);

        s.append(" where ID in :ids ");
        Query q = getSession().createSQLQuery(s.toString());
        q.setParameter(0, param.get(0));
        q.setParameterList("ids", ids);
        q.executeUpdate();
    }

    @Override
    public StockGoodsInvNoSerial findById(Long id) {
        Query q = getSession().getNamedQuery("StockGoodsInvNoSerial.findById");
        q.setParameter("id", id);
        if (q.list().isEmpty()) {
            return null;
        } else {
            return (StockGoodsInvNoSerial) q.list().get(0);
        }
    }

    @Override
    public void delete(StockGoodsInvNoSerial g) {
        super.delete(g);
        getSession().flush();
    }

    public StockGoodsInvNoSerial getStockGoodsNoserial(Long goodsId, Long status, Long shopId, String stockStatus) {
        Query query = session.createQuery("Select s from StockGoodsInvNoSerial s " +
                " where equipmentProfileId = ? and shopId = ? and equipmentStatus = ? and stockStatus = ? ");
        query.setParameter(0, goodsId);
        query.setParameter(1, shopId);
        query.setParameter(2, status);
        query.setParameter(3, stockStatus);
        if (query.list().isEmpty()) {
            return null;
        } else {
            return (StockGoodsInvNoSerial) query.list().get(0);
        }
    }

    public List<StockGoodsInvNoSerial> getStockGoodsNoserialByShopId(List<Long> shopIds) {
        Query query = session.createQuery("Select s from StockGoodsInvNoSerial s " +
                " where  shopId IN :shopId ");
        query.setParameterList("shopId", shopIds);
        List<StockGoodsInvNoSerial> ls = query.list();
        if (ls.isEmpty()) {
            return null;
        } else {
            return  ls;
        }
    }

    public StockGoodsInvNoSerial getStockGoodsNoserialKTV(Long goodsId, Long status, Long shopId, String stockStatus, Long staffId) {
        Query query = session.createQuery("Select s from StockGoodsInvNoSerial s " +
                " where equipmentProfileId = ? and shopId = ? and equipmentStatus = ? and stockStatus = ? and staffId = ?");
        query.setParameter(0, goodsId);
        query.setParameter(1, shopId);
        query.setParameter(2, status);
        query.setParameter(3, stockStatus);
        query.setParameter(4, staffId);
        if (query.list().isEmpty()) {
            return null;
        } else {
            return (StockGoodsInvNoSerial) query.list().get(0);
        }
    }

    public StockGoodsInvSerialDTO convertObjectToSerial(Object[] o) {
        StockGoodsInvSerialDTO rs = new StockGoodsInvSerialDTO();
        try {
            rs.setId(o[0] == null ? null : Long.parseLong(o[0].toString()));
            rs.setShopId(o[1] == null ? null : Long.parseLong(o[1].toString()));
            rs.setEquipmentProfileId(o[2] == null ? null : Long.parseLong(o[2].toString()));
            rs.setSerial(o[3] == null ? null : o[3].toString());
            rs.setEquipmentProfileStatus(o[4] == null ? null : Long.parseLong(o[4].toString()));
            rs.setStockStatus(o[5] == null ? null : o[5].toString());
            rs.setProviderId(o[6] == null ? null : Long.parseLong(o[6].toString()));
            rs.setInstockDatetime(o[7] == null ? null : (Date) o[7]);
            rs.setOutstockDatetime(o[8] == null ? null : (Date) o[8]);
            rs.setCurrentTaId(o[9] == null ? null : Long.parseLong(o[9].toString()));
//            rs.setEtagNumber(o[10] == null ? null : o[10].toString());
            rs.setProfileName(o[11] == null ? null : o[11].toString());
            rs.setProfileCode(o[12] == null ? null : o[12].toString());
            rs.setSerialSearch(o[13] == null ? null : o[13].toString());
            rs.setEquipentGroupCode(o[14] == null ? null : o[14].toString());
            rs.setSpecification(o[15] == null ? null : o[15].toString());
            rs.setLifeCycle(o[16] == null ? null : Long.parseLong(o[16].toString()));
            rs.setMaintancePeriod(o[17] == null ? null : Long.parseLong(o[17].toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }
    public StockGoodsInvSerialDTO convertObjectToSerialAudit(Object[] o) {
        StockGoodsInvSerialDTO rs = new StockGoodsInvSerialDTO();
        try {
            int i =0;
            rs.setId(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setShopId(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setEquipmentProfileId(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setSerial(o[i] == null ? null : o[i].toString());
            i++;
            rs.setEquipmentProfileStatus(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setStockStatus(o[i] == null ? null : o[i].toString());
            i++;
            rs.setProviderId(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setInstockDatetime(o[i] == null ? null : (Date) o[i]);
            i++;
            rs.setOutstockDatetime(o[i] == null ? null : (Date) o[i]);
            i++;
            rs.setCurrentTaId(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
//            rs.setEtagNumber(o[10] == null ? null : o[10].toString());
            rs.setProfileName(o[i] == null ? null : o[i].toString());
            i++;
            rs.setProfileCode(o[i] == null ? null : o[i].toString());
            i++;
            rs.setSerialSearch(o[i] == null ? null : o[i].toString());
            i++;
            rs.setEquipentGroupCode(o[i] == null ? null : o[i].toString());
            i++;
            rs.setSpecification(o[i] == null ? null : o[i].toString());
            i++;
            rs.setCountBlockSerial(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setUnit(o[i] == null ? null :o[i].toString());
            i++;
            rs.setProviderCode(o[i] == null ? null :o[i].toString());
            i++;
            rs.setProviderName(o[i] == null ? null :o[i].toString());
            i++;
            rs.setOrigin(o[i] == null ? null :o[i].toString());
            i++;
            rs.setGroupType(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setAvailableCount(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }
    public StockGoodsInvSerialDTO convertObjectToEquipment(Object[] o) {
        StockGoodsInvSerialDTO rs = new StockGoodsInvSerialDTO();
        try {
            rs.setEquipentGroupCode(o[0] == null ? null : o[0].toString());
            rs.setProfileName(o[1] == null ? null : o[1].toString());
            rs.setProfileCode(o[2] == null ? null : o[2].toString());
            rs.setSerial(o[3] == null ? null : o[3].toString());
            rs.setEquipmentProfileStatus(o[4] == null ? null : Long.parseLong(o[4].toString()));
            rs.setStockStatus(o[5] == null ? null : o[5].toString());
            rs.setWarrantyStatus(o[6] == null ? null : Long.parseLong(o[6].toString()));
            rs.setWarrantyPeriod(o[7] == null ? null : Long.parseLong(o[7].toString()));
            rs.setWarantyExpiredDate(o[8] == null ? null : (Date) o[8]);
            rs.setProviderCode(o[9] == null ? null :o[9].toString());
            rs.setProviderName(o[10] == null ? null :o[10].toString());
            rs.setEquimentDetailId(o[11] == null ? null : Long.parseLong(o[11].toString()));
            rs.setEquipmentProfileId(o[12] == null ? null : Long.parseLong(o[12].toString()));
            rs.setGroupId(o[13] == null ? null : Long.parseLong(o[13].toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }
    public StockGoodsInvSerialDTO convertObjectEquipmentDetail(Object[] o) {
        StockGoodsInvSerialDTO rs = new StockGoodsInvSerialDTO();
        try {
            rs.setEquimentDetailId(o[0] == null ? null : Long.parseLong(o[0].toString()));
            rs.setSpecification(o[1] == null ? null : o[1].toString());
            rs.setShopId(o[2] == null ? null : Long.parseLong(o[2].toString()));
            rs.setShopCode(o[3] == null ? null : o[3].toString());
            rs.setShopName(o[4] == null ? null : o[4].toString());
            rs.setPositionCode(o[5] == null ? null : o[5].toString());
            rs.setPositionName(o[6] == null ? null : o[6].toString());
            rs.setMaintancePeriod(o[7] == null ? null : Long.parseLong(o[7].toString()));
            rs.setWaranCount(o[8] == null ? null : Long.parseLong(o[8].toString()));
            rs.setLastUsedDate(o[9] == null ? null : (Date) o[9]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }
    public StockGoodsInvSerialDTO convertObjectActionAudit(Object[] o) {
        StockGoodsInvSerialDTO rs = new StockGoodsInvSerialDTO();
        try {
            rs.setEquimentDetailId(o[0] == null ? null : Long.parseLong(o[0].toString()));
            rs.setSerial(o[1] == null ? null : o[1].toString());
            rs.setActionDate(o[2] == null ? null : (Date) o[2]);
            rs.setActionType(o[3] == null ? null : Long.parseLong(o[3].toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }
    public StockGoodsInvSerialDTO convertObjectMantenEquiment(Object[] o) {
        StockGoodsInvSerialDTO rs = new StockGoodsInvSerialDTO();
        try {
            rs.setMseId(o[0] == null ? null : Long.parseLong(o[0].toString()));
            rs.setEquimentDetailId(o[1] == null ? null : Long.parseLong(o[1].toString()));
            rs.setSerial(o[2] == null ? null : o[2].toString());
            rs.setPositionCode(o[3] == null ? null : o[3].toString());
            rs.setPositionName(o[4] == null ? null : o[4].toString());
            rs.setShopCode(o[5] == null ? null : o[5].toString());
            rs.setShopName(o[6] == null ? null : o[6].toString());
            rs.setActionDate(o[7] == null ? null : (Date) o[7]);
            rs.setStatusMse(o[8] == null ? null : Long.parseLong(o[8].toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }
    public StockGoodsInvSerialDTO convertObjectSerialAndStockGoods(Object[] o) {
        StockGoodsInvSerialDTO rs = new StockGoodsInvSerialDTO();
        try {
            int i = 0;
            rs.setId(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setShopId(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setEquipmentProfileId(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setSerial(o[i] == null ? null : o[i].toString());
            i++;
            rs.setEquipmentProfileStatus(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setStockStatus(o[i] == null ? null : o[i].toString());
            i++;
            rs.setProviderId(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setCurrentTaId(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setEquimentDetailId(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setGroupId(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setPositionId(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setLifeCycle(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setMaintancePeriod(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setWaranCount(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setWarrantyPeriod(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setCoNuber(o[i] == null ? null : o[i].toString());
            i++;
            rs.setCqNumber(o[i] == null ? null : o[i].toString());
            i++;
            rs.setWarantyExpiredDate(o[i] == null ? null : (Date) o[i]);
            i++;
            rs.setSpecification(o[i] == null ? null : o[i].toString());
            i++;
            rs.setWarrantyStatus(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setImTransaction(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setStockGoodsId(o[i] == null ? null : Long.parseLong(o[i].toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public List<StockGoodsInvSerialDTO> searchContainOutstockDateTime(Date fromOutstockDatetime, Date toOutstockDatetime,
                                                                      String from, String to, StockGoodsInvSerial sgi, Long goodsGroupId, Integer start, Integer get) {
        try {
            List param = new ArrayList();
            StringBuilder s = new StringBuilder(
                    "SELECT  s.ID,s.SHOP_ID,s.EQUIPMENT_PROFILE_ID,s.SERIAL,s.EQUIPMENT_STATUS,s.STOCK_STATUS,"
                            + "s.PROVIDER_ID,s.INSTOCK_DATETIME,s.OUTSTOCK_DATETIME,s.CURRENT_TA_ID,"
                            + " g.PROFILE_NAME, g.PROFILE_CODE"
                            + ", s.SERIAL_SEARCH, gg.EQUIPMENT_GROUP_CODE, TO_CHAR(g.SPECIFICATION), d.LIFE_CYCLE, d.MAINTANCE_PERIOD "
                            + " FROM STOCK_GOODS_INV_SERIAL s");
            if (sgi.getEquipmentProfileId() != null && sgi.getEquipmentProfileId() > 0) {
                s.append(
                        " JOIN EQUIPMENT_PROFILE g ON s.EQUIPMENT_PROFILE_ID =  g.PROFILE_ID JOIN EQUIPMENT_GROUP gg "
                                + "ON g.EQUIPMENT_GROUP_ID = gg.EQUIPMENT_GROUP_ID" +
                                " JOIN EQUIPMENT_DETAIL d on g.PROFILE_ID = d.EQUIPMENT_PROFILE_ID" +
                                " WHERE s.EQUIPMENT_PROFILE_ID = ? " +
                                " ");
                param.add(sgi.getEquipmentProfileId());
            } else if (goodsGroupId != null && goodsGroupId > 0) {
                s.append(
                        " JOIN EQUIPMENT_PROFILE g ON s.EQUIPMENT_PROFILE_ID =  g.PROFILE_ID JOIN EQUIPMENT_GROUP gg "
                                + " ON g.EQUIPMENT_GROUP_ID = gg.EQUIPMENT_GROUP_ID  " +
                                " JOIN EQUIPMENT_DETAIL d on gg.EQUIPMENT_GROUP_ID = d.EQUIPMENT_GROUP_ID " +
                                " WHERE gg.EQUIPMENT_GROUP_ID = ? ");
                param.add(goodsGroupId);
            }
            if (from != null && !from.trim().isEmpty()) {
                s.append(" AND (( TO_CHAR(SERIAL) >= TO_CHAR(?) AND LENGTH(SERIAL) = ? ) OR LENGTH(SERIAL) > ? ) ");
                param.add(from);
                param.add(from.length());
                param.add(from.length());
            }
            if (to != null && !to.trim().isEmpty()) {
                s.append(" AND (( TO_CHAR(SERIAL) <= TO_CHAR(?) AND LENGTH(SERIAL) = ? ) OR LENGTH(SERIAL) < ? ) ");
                param.add(to);
                param.add(to.length());
                param.add(to.length());
            }
            if (sgi.getCurrentTaId() != null && sgi.getCurrentTaId() > 0) {
                s.append(" and CURRENT_TA_ID = ?");
                param.add(sgi.getCurrentTaId());
            }
            if (sgi.getShopId() != null && sgi.getShopId() > 0) {
                s.append(" and SHOP_ID = ?");
                param.add(sgi.getShopId());
            }

            // if(fromOutstockDatetime != null){
            // s.append(" and OUTSTOCK_DATETIME > ? ");
            // long t = DateTimeUtils.addDay(fromOutstockDatetime, -1).getTime();
            // java.sql.Date sqlDate = new java.sql.Date(t);
            // param.add(sqlDate);
            // }
            //
            // if(toOutstockDatetime != null){
            // s.append(" and OUTSTOCK_DATETIME < ? ");
            // long time = DateTimeUtils.addDay(toOutstockDatetime, +1).getTime();
            // java.sql.Date toDate = new java.sql.Date(time);
            // param.add(toDate);
            // }
            if (fromOutstockDatetime != null) {
                s.append(" and TO_CHAR(OUTSTOCK_DATETIME,'yyyy-MM-dd') >= ? ");
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                param.add(sf.format(fromOutstockDatetime));

            }

            if (toOutstockDatetime != null) {
                s.append(" and TO_CHAR(OUTSTOCK_DATETIME,'yyyy-MM-dd') < ? ");
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                param.add(sf.format(DateTimeUtils.addDay(toOutstockDatetime, 1)));

            }

            if (sgi.getStockStatus() != null && !sgi.getStockStatus().trim().isEmpty()
                    && !sgi.getStockStatus().trim().equals("0")) {
                if (sgi.getStockStatus().trim().contains(",")) {
                    String[] k = sgi.getStockStatus().trim().split(",");

                    s.append(" and STOCK_STATUS in (");
                    for (int i = 0; i < k.length; i++) {
                        s.append(i < k.length - 1 ? "?," : "?");
                        param.add(k[i]);
                    }
                    s.append(")");
                } else {
                    s.append(" and STOCK_STATUS = ?");
                    param.add(sgi.getStockStatus());
                }
            }
            if (sgi.getEquipmentProfileStatus() != null && sgi.getEquipmentProfileStatus() > 0) {
                s.append(" and EQUIPMENT_STATUS = ?");
                param.add(sgi.getEquipmentProfileStatus());
            }
            if (sgi.getProviderId() != null && sgi.getProviderId() > 0) {
                s.append(" and PROVIDER_ID= ?");
                param.add(sgi.getProviderId());
            }
            s.append(" ORDER BY LENGTH(SERIAL), SERIAL ");
            Query q = getSession().createSQLQuery(s.toString());
            for (int i = 0; i < param.size(); i++) {
                q.setParameter(i, param.get(i));
            }
            String str = s.toString();
            int abc;

            if (start != null && get != null && get > 0) {
                q.setFirstResult(start);
                q.setMaxResults(get);
            }
            List<StockGoodsInvSerialDTO> rs = new ArrayList<>();
            List<Object[]> a = q.list();
            if (a.isEmpty()) {
                return null;
            }
            for (Object[] o : a) {
                rs.add(convertObjectToSerial(o));
            }
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<StockGoodsInvSerialDTO>();
        }
    }

    public static void main(String[] args) {
        StockGoodsInvSerialDAO dao = new StockGoodsInvSerialDAO();
        StockGoodsInvSerial serial = new StockGoodsInvSerial();
        serial.setEquipmentProfileId(1L);
        serial.setProviderId(1L);
        serial.setShopId(1L);
        serial.setEquipmentProfileStatus(2L);
        dao.searchBlockSerial(null, null, serial, null, 1L);
    }

    public StockGoodsInvSerial convertObjectToBlockSerial(Object[] o) {
        StockGoodsInvSerial rs = new StockGoodsInvSerial();
        try {
            rs.setEquipmentProfileId(o[0] == null ? null : Long.parseLong(o[0].toString()));
            rs.setEquipmentProfileStatus(o[1] == null ? null : Long.parseLong(o[1].toString()));
            rs.setStockStatus(o[2] == null ? null : o[2].toString());

            rs.setTransactionAction(new TransactionAction());

            rs.getTransactionAction().setTransactionActionCode(o[3] == null ? null : o[3].toString());
            rs.getTransactionAction().setCreateShopId(o[4] == null ? null : Long.parseLong(o[4].toString()));
            rs.getTransactionAction().setToShopId(o[5] == null ? null : Long.parseLong(o[5].toString()));
            //            rs.getTransactionAction().setFromShopId(o[8] == null ? null : Long.parseLong(o[8].toString()));
            //            rs.getTransactionAction().setCreateDatetime(o[9] == null ? null : DateTimeUtils.convertStringToDate(o[9].toString()));
            //            rs.getTransactionAction().setDescription(o[10] == null ? null : o[10].toString());
            rs.setCountBlockSerial(o[6] == null ? null : Long.parseLong(o[6].toString()));
            rs.setProviderId(o[7] == null ? null : Long.parseLong(o[7].toString()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rs;
    }

    public StockGoodsInvSerial convertObjectToGeneralBlockSerial(Object[] o) {
        StockGoodsInvSerial rs = new StockGoodsInvSerial();
        try {
            rs.setTransactionAction(new TransactionAction());
            rs.getTransactionAction().setToShopId(o[0] == null ? null : Long.parseLong(o[0].toString()));
            rs.setCountBlockSerial(o[1] == null ? null : Long.parseLong(o[1].toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public List<InvImExReportBo> searchInvImExRp(Date fromDate, Date toDate, Long fromShopId, Long toShopId, String orderType, String orderStatus, String orderCode) {
        try {
            Map<String, Object> param = new HashMap();

            StringBuilder sql = new StringBuilder();
            StringBuilder sqlImProvider = new StringBuilder();
            sql.append(" SELECT   ROWNUM as id ,                                                                     ");
            if (!orderType.equals("1")) {
                sql.append(" sg.EQUIPMENT_STATUS status,                                                               ");
            } else {
                sql.append(" '' status,                                                               ");
            }
            sql.append(" ta.TRANSACTION_ACTION_CODE orderCode,                                                    ");
            sql.append(" ta.CREATE_SHOP_ID createShopId,                                                             ");
            sql.append(" ta.TO_SHOP_ID toShopId,                                                                 ");
            sql.append(" ta.FROM_SHOP_ID fromShopId,                                                               ");
            sql.append(" ta.CREATE_DATETIME createdDate,                                                                 ");
            sql.append(" ta.DESCRIPTION  description                                                                ");
            sql.append(" FROM TRANSACTION_ACTION ta                                                 ");
            if (!orderType.equals("1")) {
                sql.append(" JOIN STOCK_GOODS_INV_SERIAL sg ON sg.CURRENT_TA_ID = ta.TRANSACTION_ACTION_ID      ");
                sql.append(" JOIN EQUIPMENT_PROFILE g on g.PROFILE_ID = sg.EQUIPMENT_PROFILE_ID                                       ");
                sql.append(" JOIN EQUIPMENT_GROUP gg on gg.EQUIPMENT_GROUP_ID = g.EQUIPMENT_GROUP_ID                    ");
            }


            sql.append(" where 1 = 1                                                                    ");

            if (fromShopId != null) {
                sql.append(" and ta.FROM_SHOP_ID = :fromShop ");
                param.put("fromShop", fromShopId);
            }

            if (toShopId != null) {
                sql.append(" and ta.TO_SHOP_ID = :toShop ");
                param.put("toShop", toShopId);
            }

            if (!StringUtil.stringIsNullOrEmty(orderType)) {
                sql.append(" and ta.TRANSACTION_TYPE = :orderType ");
                param.put("orderType", orderType);
            }

            if (!StringUtil.stringIsNullOrEmty(orderStatus)) {
                sql.append(" and ta.STATUS = :orderStatus ");
                param.put("orderStatus", orderStatus);
            }

            if (!StringUtil.stringIsNullOrEmty(orderCode)) {
                sql.append(" and ta.TRANSACTION_ACTION_CODE = :orderCode ");
                param.put("orderCode", orderCode);
            }

            if (fromDate != null) {
                sql.append(" and ta.CREATE_DATETIME >= :fromDate ");
                param.put("fromDate", fromDate);
            }

            if (toDate != null) {
                sql.append(" and ta.CREATE_DATETIME <= :toDate ");
                param.put("toDate", toDate);
            }

            sql.append(" ORDER BY ta.TRANSACTION_ACTION_CODE ");


            SQLQuery q = getSession().createSQLQuery(sql.toString()).addEntity(InvImExReportBo.class);
            for (String s : param.keySet()) {
                q.setParameter(s, param.get(s));
            }
            List<InvImExReportBo> a = q.list();

            return a;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<InvImExReportBo>();
        }
    }


    public List<StockGoodsInvSerial> searchBlockSerial(String from, String to, StockGoodsInvSerial sgi,
                                                       Long goodsGroupId, Long toShopId) {
        try {
            List param = new ArrayList();

            StringBuilder s = new StringBuilder(
                    "SELECT sg.EQUIPMENT_PROFILE_ID, sg.EQUIPMENT_STATUS, sg.STOCK_STATUS, ta.TRANSACTION_ACTION_CODE, ta.CREATE_SHOP_ID, ta.TO_SHOP_ID, COUNT(*), sg.PROVIDER_ID"
                            + " FROM STOCK_GOODS_INV_SERIAL sg JOIN TRANSACTION_ACTION ta ON sg.CURRENT_TA_ID = ta.TRANSACTION_ACTION_ID "
                            + "JOIN EQUIPMENT_PROFILE g on g.PROFILE_ID = sg.EQUIPMENT_PROFILE_ID JOIN EQUIPMENT_GROUP gg on gg.EQUIPMENT_GROUP_ID = g.EQUIPMENT_GROUP_ID ");

            if (sgi.getEquipmentProfileId() != null && sgi.getEquipmentProfileId() > 0) {
                s.append(" WHERE g.PROFILE_ID = ? ");
                param.add(sgi.getEquipmentProfileId());
            } else if (goodsGroupId != null && goodsGroupId > 0) {
                s.append(" WHERE gg.EQUIPMENT_GROUP_ID = ? ");
                param.add(goodsGroupId);
            }

            s.append(" AND sg.STOCK_STATUS in ? ");
            param.add(InventoryConstanst.StockStatus.BLOCK_TD +", "+InventoryConstanst.StockStatus.BLOCK_NC +", " +
                    InventoryConstanst.StockStatus.BLOCK_XT );

            if (sgi.getShopId() != null && sgi.getShopId() > 0) {
                s.append(" AND sg.SHOP_ID = ?");
                param.add(sgi.getShopId());
            }
            if (sgi.getEquipmentProfileStatus() != null && sgi.getEquipmentProfileStatus() > 0) {
                s.append(" AND sg.EQUIPMENT_STATUS = ?");
                param.add(sgi.getEquipmentProfileStatus());
            }
            if (sgi.getProviderId() != null && sgi.getProviderId() > 0) {
                s.append(" AND sg.PROVIDER_ID= ?");
                param.add(sgi.getProviderId());
            }
            if (toShopId != null && toShopId > 0) {
                s.append(" AND ta.TO_SHOP_ID = ?");
                param.add(toShopId);
            }

            //		if (from != null && !from.trim().isEmpty()) {
            //			s.append(" AND (( TO_CHAR(sg.SERIAL) >= TO_CHAR(?) AND LENGTH(sg.SERIAL) = ? ) OR LENGTH(sg.SERIAL) > ? ) ");
            //			param.add(from);
            //			param.add(from.length());
            //			param.add(from.length());
            //		}
            //		if (to != null && !to.trim().isEmpty()) {
            //			s.append(" AND (( TO_CHAR(sg.SERIAL) <= TO_CHAR(?) AND LENGTH(sg.SERIAL) = ? ) OR LENGTH(sg.SERIAL) < ? ) ");
            //			param.add(to);
            //			param.add(to.length());
            //			param.add(to.length());
            //		}

            s.append(
                    "GROUP BY sg.EQUIPMENT_PROFILE_ID, sg.EQUIPMENT_STATUS, sg.STOCK_STATUS, ta.TRANSACTION_ACTION_CODE, ta.CREATE_SHOP_ID, ta.TO_SHOP_ID, sg.PROVIDER_ID HAVING COUNT(*) > 0"
                            + " ORDER BY ta.TRANSACTION_ACTION_CODE ");

            Query q = getSession().createSQLQuery(s.toString());
            for (int i = 0; i < param.size(); i++) {
                q.setParameter(i, param.get(i));
            }
            List<StockGoodsInvSerial> rs = new ArrayList<>();
            List<Object[]> a = q.list();
            for (Object[] o : a) {
                rs.add(convertObjectToBlockSerial(o));
            }
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<StockGoodsInvSerial>();
        }
    }

    public List<StockGoodsInvSerial> searchGeneralBlockSerial(String from, String to, StockGoodsInvSerial sgi,
                                                              Long goodsGroupId, String codePath) {
        try {
            List param = new ArrayList();

            StringBuilder s = new StringBuilder(
                    "SELECT ta.TO_SHOP_ID, COUNT(*) "
                            + " FROM STOCK_GOODS_INV_SERIAL sg "
                            + "JOIN TRANSACTION_ACTION ta ON sg.CURRENT_TA_ID = ta.TRANSACTION_ACTION_ID "
                            + "JOIN EQUIPMENT_PROFILE g on g.PROFILE_ID = sg.EQUIPMENT_PROFILE_ID JOIN EQUIPMENT_GROUP gg "
                            + "on gg.EQUIPMENT_GROUP_ID = g.EQUIPMENT_GROUP_ID "
                            + "JOIN SHOP s on sg.SHOP_ID = s.SHOP_ID");

            if (sgi.getEquipmentProfileId() != null && sgi.getEquipmentProfileId() > 0) {
                s.append(" WHERE g.PROFILE_ID = ? ");
                param.add(sgi.getEquipmentProfileId());
            } else if (goodsGroupId != null && goodsGroupId > 0) {
                s.append(" WHERE gg.EQUIPMENT_GROUP_ID = ? ");
                param.add(goodsGroupId);
            }

            s.append(" AND sg.STOCK_STATUS IN ? ");
            param.add(InventoryConstanst.StockStatus.BLOCK_TD +", "+InventoryConstanst.StockStatus.BLOCK_NC +", " +
                    InventoryConstanst.StockStatus.BLOCK_XT );

            if (sgi.getShopId() != null && sgi.getShopId() > 0) {
                s.append(" AND sg.SHOP_ID = ?");
                param.add(sgi.getShopId());
            } else if (codePath != null && codePath != "") {
                s.append(" AND s.CODE_PATH like ?");
                codePath = "%" + codePath + "%";
                param.add(codePath);
            }

            if (sgi.getEquipmentProfileStatus() != null && sgi.getEquipmentProfileStatus() > 0) {
                s.append(" AND sg.EQUIPMENT_STATUS = ?");
                param.add(sgi.getEquipmentProfileStatus());
            }
            if (sgi.getProviderId() != null && sgi.getProviderId() > 0) {
                s.append(" AND sg.PROVIDER_ID= ?");
                param.add(sgi.getProviderId());
            }
            //		if (from != null && !from.trim().isEmpty()) {
            //			s.append(" AND (( TO_CHAR(sg.SERIAL) >= TO_CHAR(?) AND LENGTH(sg.SERIAL) = ? ) OR LENGTH(sg.SERIAL) > ? ) ");
            //			param.add(from);
            //			param.add(from.length());
            //			param.add(from.length());
            //		}
            //		if (to != null && !to.trim().isEmpty()) {
            //			s.append(" AND (( TO_CHAR(sg.SERIAL) <= TO_CHAR(?) AND LENGTH(sg.SERIAL) = ? ) OR LENGTH(sg.SERIAL) < ? ) ");
            //			param.add(to);
            //			param.add(to.length());
            //			param.add(to.length());
            //		}

            s.append(
                    "GROUP BY ta.TO_SHOP_ID HAVING COUNT(*) > 0"
                            + "");

            Query q = getSession().createSQLQuery(s.toString());
            for (int i = 0; i < param.size(); i++) {
                q.setParameter(i, param.get(i));
            }
            List<StockGoodsInvSerial> rs = new ArrayList<StockGoodsInvSerial>();
            List<Object[]> a = q.list();
            for (Object[] o : a) {
                rs.add(convertObjectToGeneralBlockSerial(o));
            }
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<StockGoodsInvSerial>();
        }
    }

    public List<StockGoodsInvSerialDTO> search(StockGoodsInvSerialDTO sgi, Integer start, Integer get) {
        try {
            List param = new ArrayList();

            StringBuilder s = new StringBuilder(
                    "SELECT \n" +
                            " s.ID,s.SHOP_ID,s.EQUIPMENT_PROFILE_ID,s.SERIAL,s.EQUIPMENT_STATUS,\n" +
                            " s.STOCK_STATUS,s.PROVIDER_ID,s.INSTOCK_DATETIME,s.OUTSTOCK_DATETIME,\n" +
                            " s.CURRENT_TA_ID, g.PROFILE_NAME, g.PROFILE_CODE, \n" +
                            " s.SERIAL_SEARCH, gg.EQUIPMENT_GROUP_CODE, TO_CHAR(g.SPECIFICATION)," +
                            " sg.QUANTITY, g.UNIT, prd.PROVIDER_CODE, prd.PROVIDER_NAME, g.ORIGIN, \n" +
                            " gg.EQUIPMENT_GROUP_TYPE, sg.AVAILABLE_QUANTITY " +
                            " FROM \n" +
                            " EQUIPMENT_PROFILE g  JOIN STOCK_GOODS_INV_SERIAL s  ON g.PROFILE_ID = s.EQUIPMENT_PROFILE_ID \n" +
                            " LEFT JOIN EQUIPMENT_GROUP gg ON g.EQUIPMENT_GROUP_ID = gg.EQUIPMENT_GROUP_ID \n" +
                            " LEFT JOIN TRANSACTION_ACTION tr ON s.CURRENT_TA_ID = tr.TRANSACTION_ACTION_ID\n" +
                            " INNER JOIN STOCK_GOODS sg ON s.EQUIPMENT_PROFILE_ID = sg.GOODS_ID and s.SHOP_ID = sg.SHOP_ID and sg.GOODS_STATUS = s.EQUIPMENT_STATUS \n" +
                            " LEFT JOIN PROVIDER prd ON s.PROVIDER_ID = prd.PROVIDER_ID\n" +
                            " WHERE 1=1 ");

//todo: sua check lai danh muc xuat kho thiet bi cap tren
//            if(sgi.getResonId()!=null){
//                s.append(" AND tr.REASON_ID = ? ");
//                param.add(sgi.getResonId());
//            }
            if (sgi.getCurrentTaId() != null && sgi.getCurrentTaId() > 0) {
                s.append(" and CURRENT_TA_ID = ?");
                param.add(sgi.getCurrentTaId());
            }
            if(sgi.getEquipmentProfileStatus()!=null){
//                s.append(" AND s.EQUIPMENT_STATUS = ? ");//g.PROFILE_ID in(SELECT d.EQUIPMENT_PROFILE_ID  from EQUIPMENT_DETAIL d WHERE d.EQUIPMENT_STATUS = ?) ");
                s.append(" AND sg.GOODS_STATUS = ? ");
                param.add(sgi.getEquipmentProfileStatus());
            }
//            boolean isAdd = false;
//            if(sgi.getCreateDateTime()!=null){
//                isAdd = true;
////                --AND tr.TRANSACTION_ACTION_ID
//////                    --  in (SELECT TRANSACTION_ACTION_ID FROM STOCK_TRANSACTION st WHERE st.STOCK_TRANSACTION_DATETIME = ? AND st.EXPORT_STAFF_ID =? )
//                s.append("AND g.PROFILE_ID " +
//                        " in(SELECT EQUIPMENT_ID FROM EQUIPMENT_HISTORY st WHERE st.CREATED_DATETIME = ? ");
//                param.add(sgi.getCreateDateTime());
//            }
//            if(sgi.getStaffId()!=null){
//                if(isAdd){
//                    s.append(" AND st.STAFF_ID = ? ");
//                    param.add(sgi.getStaffId());
//                }else {
//                    isAdd = true;
//                    s.append("AND g.PROFILE_ID " +
//                            " in(SELECT EQUIPMENT_ID FROM EQUIPMENT_HISTORY st WHERE st.STAFF_ID = ? ");
//                    param.add(sgi.getStaffId());
//                }
//
//            }
//            if(sgi.getNote()!=null && !sgi.getNote().isEmpty()){
//                if(isAdd){
//                    s.append(" AND UPPER(st.NOTE) like ? ");
//                    param.add("%"+sgi.getNote().toUpperCase()+"%");
//                }else {
//                    isAdd = true;
//                    s.append("AND g.PROFILE_ID " +
//                            " in(SELECT EQUIPMENT_ID FROM EQUIPMENT_HISTORY st WHERE SUPER(st.NOTE) LIKE ? ");
//                    param.add("%"+sgi.getNote().toUpperCase()+"%");
//                }
//
//            }
//            if(isAdd){
//                s.append(" ) ");
//            }

            if(sgi.getGroupId()!=null){
                s.append(" AND gg.EQUIPMENT_GROUP_ID = ? ");
                param.add(sgi.getGroupId());
            }
            if(sgi.getEquipmentProfileId()!=null){
                s.append(" AND g.PROFILE_ID = ? ");
                param.add(sgi.getEquipmentProfileId());
            }
            if(sgi.getProviderId()!=null){
                s.append(" AND s.PROVIDER_ID = ? ");
                param.add(sgi.getProviderId());
            }
            if(sgi.getSerial()!=null && !sgi.getSerial().trim().isEmpty()){
                s.append(" AND UPPER(s.SERIAL) like ? ");
                param.add("%"+ sgi.getSerial().trim().toUpperCase()+"%");
            }
            if(sgi.getStockStatus()!=null && !sgi.getStockStatus().isEmpty()){
                if(sgi.getStockStatus().equals("1,15,16,17")){
                    s.append(" AND s.STOCK_STATUS in (1, 15, 16, 17) ");
                }else {
                    s.append(" AND s.STOCK_STATUS = ? ");
                    param.add(sgi.getStockStatus());
                }
            }
            //lien quan den: xuat kho thiet bi cap tren
            if(sgi.getShopId()!=null){
                s.append(" AND sg.SHOP_ID = ? ");
                param.add(sgi.getShopId());
            }

            s.append(" ORDER BY s.SERIAL, PROVIDER_ID  ");
            Query q = getSession().createSQLQuery(s.toString());
            for (int i = 0; i < param.size(); i++) {
                q.setParameter(i, param.get(i));
            }
            System.out.println("Query detail: ");
            System.out.println(s);
            System.out.println("Params: ");
            System.out.println(param);
            if (start != null && get != null && get > 0) {
                q.setFirstResult(start);
                q.setMaxResults(get);
            }
            List<StockGoodsInvSerialDTO> rs = new ArrayList<StockGoodsInvSerialDTO>();
            List<Object[]> a = q.list();
            for (Object[] o : a) {
                rs.add(convertObjectToSerialAudit(o));
            }
            return rs;
        } catch (Exception ex) {
            ex.printStackTrace();
            ex.printStackTrace();
            return new ArrayList<StockGoodsInvSerialDTO>();
        }
    }


    public Long searchSize(StockGoodsInvSerialDTO sgi) {
        try {
            List param = new ArrayList();
            StringBuilder s = new StringBuilder("SELECT  COUNT(1) "+
                    " FROM \r\n" +
                    " EQUIPMENT_PROFILE g  JOIN STOCK_GOODS_INV_SERIAL s  ON g.PROFILE_ID = s.EQUIPMENT_PROFILE_ID \r\n" +
                    " LEFT JOIN EQUIPMENT_GROUP gg ON g.EQUIPMENT_GROUP_ID = gg.EQUIPMENT_GROUP_ID \r\n" +
                    " LEFT JOIN TRANSACTION_ACTION tr ON s.CURRENT_TA_ID = tr.TRANSACTION_ACTION_ID\r\n" +
                    " INNER JOIN STOCK_GOODS sg ON s.EQUIPMENT_PROFILE_ID = sg.GOODS_ID and s.SHOP_ID = sg.SHOP_ID and sg.GOODS_STATUS = s.EQUIPMENT_STATUS \r\n" +
                    " LEFT JOIN PROVIDER prd ON s.PROVIDER_ID = prd.PROVIDER_ID\r\n" +
                    " WHERE 1=1 ");
//            if(sgi.getResonId()!=null){
//                s.append(" AND tr.REASON_ID = ? ");
//                param.add(sgi.getResonId());
//            }
            if(sgi.getEquipmentProfileStatus()!=null){
                s.append(" AND g.PROFILE_ID in(SELECT d.EQUIPMENT_PROFILE_ID  from EQUIPMENT_DETAIL d WHERE d.EQUIPMENT_STATUS = ?) ");
                param.add(sgi.getEquipmentProfileStatus());
            }
//            boolean isAdd = false;
//            if(sgi.getCreateDateTime()!=null){
//                isAdd = true;
////                --AND tr.TRANSACTION_ACTION_ID
//////                    --  in (SELECT TRANSACTION_ACTION_ID FROM STOCK_TRANSACTION st WHERE st.STOCK_TRANSACTION_DATETIME = ? AND st.EXPORT_STAFF_ID =? )
//                s.append("AND g.PROFILE_ID " +
//                        " in(SELECT EQUIPMENT_ID FROM EQUIPMENT_HISTORY st WHERE st.CREATED_DATETIME = ? ");
//                param.add(sgi.getCreateDateTime());
//            }
//            if(sgi.getStaffId()!=null){
//                if(isAdd){
//                    s.append(" AND st.STAFF_ID = ? ");
//                    param.add(sgi.getStaffId());
//                }else {
//                    isAdd = true;
//                    s.append("AND g.PROFILE_ID " +
//                            " in(SELECT EQUIPMENT_ID FROM EQUIPMENT_HISTORY st WHERE st.STAFF_ID = ? ");
//                    param.add(sgi.getStaffId());
//                }
//
//            }
//            if(sgi.getNote()!=null && !sgi.getNote().isEmpty()){
//                if(isAdd){
//                    s.append(" AND UPPER(st.NOTE) like ? ");
//                    param.add("%"+sgi.getNote().toUpperCase()+"%");
//                }else {
//                    isAdd = true;
//                    s.append("AND g.PROFILE_ID " +
//                            " in(SELECT EQUIPMENT_ID FROM EQUIPMENT_HISTORY st WHERE SUPER(st.NOTE) LIKE ? ");
//                    param.add("%"+sgi.getNote().toUpperCase()+"%");
//                }
//
//            }
//            if(isAdd){
//                s.append(" ) ");
//            }

            if(sgi.getGroupId()!=null){
                s.append(" AND gg.EQUIPMENT_GROUP_ID = ? ");
                param.add(sgi.getGroupId());
            }
            if(sgi.getEquipmentProfileId()!=null){
                s.append(" AND g.PROFILE_ID = ? ");
                param.add(sgi.getEquipmentProfileId());
            }
            if(sgi.getProviderId()!=null){
                s.append(" AND s.PROVIDER_ID = ? ");
                param.add(sgi.getProviderId());
            }
            if(sgi.getSerial()!=null && !sgi.getSerial().trim().isEmpty()){
                s.append(" AND UPPER(s.SERIAL) like ? ");
                param.add("%"+ sgi.getSerial().trim().toUpperCase()+"%");
            }
            if(sgi.getStockStatus()!=null && !sgi.getStockStatus().isEmpty()){
                if(sgi.getStockStatus().equals("1,15,16,17")){
                    s.append(" AND s.STOCK_STATUS in (1,15,16,17) ");
                }else {
                    s.append(" AND s.STOCK_STATUS = ? ");
                    param.add(sgi.getStockStatus());
                }
            }
            //lien quan den: xuat kho thiet bi cap tren
            if(sgi.getShopId()!=null){
                s.append(" AND sg.SHOP_ID = ? ");
                param.add(sgi.getShopId());
            }
            System.out.println("Query size: ");
            System.out.println(s.toString());
            System.out.println("Params: ");
            System.out.println(param);
            Query q = getSession().createSQLQuery(s.toString());
            for (int i = 0; i < param.size(); i++) {
                q.setParameter(i, param.get(i));
            }
            //		System.out.println(q.uniqueResult().toString());
            return Long.parseLong(q.uniqueResult().toString());
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }

    }

    public List<StockGoodsInvSerialDTO> searchEquipment(StockGoodsInvSerialDTO sgi, Integer start, Integer get) {
        try {
            List param = new ArrayList();

            StringBuilder s = new StringBuilder(" SELECT \n" +
                    " gg.EQUIPMENT_GROUP_CODE, g.PROFILE_NAME, g.PROFILE_CODE, det.SERIAL,\n" +
                    " det.EQUIPMENT_STATUS, st.STOCK_STATUS, det.WARANTY_STATUS, det.WARRANTY_PERIOD, \n" +
                    " det.WARANTY_EXPIRED_DATE, prd.PROVIDER_CODE, prd.PROVIDER_NAME, " +
                    " det.EQUIPMENT_ID, det.EQUIPMENT_PROFILE_ID, det.EQUIPMENT_GROUP_ID \n" +
                    " FROM EQUIPMENT_DETAIL det LEFT JOIN EQUIPMENT_PROFILE g ON det.EQUIPMENT_PROFILE_ID = g.PROFILE_ID\n" +
                    " LEFT JOIN EQUIPMENT_GROUP gg ON det.EQUIPMENT_GROUP_ID = gg.EQUIPMENT_GROUP_ID\n" +
                    " LEFT JOIN STOCK_GOODS_INV_SERIAL st ON det.EQUIPMENT_PROFILE_ID = st.EQUIPMENT_PROFILE_ID and det.SERIAL = st.SERIAL\n" +
//                    " LEFT JOIN STOCK_TRANSACTION_SERIAL sts ON g.PROFILE_ID = sts.GOODS_ID\n" +
                    " LEFT JOIN PROVIDER prd ON det.PROVIDER_ID = prd.PROVIDER_ID\n" +
                    " left join POSITION p ON det.POSITION_ID = p.POSITION_ID\n" +
                    " Where 1 = 1 ");

            if (sgi.getGroupId() != null && sgi.getGroupId() > 0) {
                s.append(" and gg.EQUIPMENT_GROUP_ID = ? ");
                param.add(sgi.getGroupId());
            }
            if (sgi.getEquipmentProfileId() != null && sgi.getEquipmentProfileId() > 0) {
                s.append(" and g.PROFILE_ID = ? ");
                param.add(sgi.getEquipmentProfileId());
            }
            if (sgi.getNameStr() != null && !sgi.getNameStr().isEmpty()) {
                s.append(" and UPPER(g.PROFILE_NAME) LIKE ? ");
                param.add("%"+sgi.getNameStr().toUpperCase()+"%");
            }
            if (sgi.getSerial() != null && !sgi.getSerial().isEmpty()) {
                s.append(" and det.SERIAL LIKE ? ");
                param.add("%" + sgi.getSerial() + "%");
            }
            if (sgi.getEquipmentProfileStatus() != null) {
                s.append(" and det.EQUIPMENT_STATUS = ? ");
                param.add(sgi.getEquipmentProfileStatus());
            }

            if(sgi.getStockStatus()!=null){
                s.append(" AND st.STOCK_STATUS = ? ");
                param.add(sgi.getStockStatus());
            }
//            if(sgi.getShopId()!=null){
//                s.append(" AND st.SHOP_ID = ? ");
//                param.add(sgi.getShopId());
//            }
//            if(sgi.getTransactionActionId()!=null){
//                s.append(" AND det.IM_TRANSACTION_ID = ? ");
//                param.add(sgi.getTransactionActionId());
//            }
            if(sgi.getShopId()!=null){
//                s.append(" AND p.shop_id = ? ");
//                param.add(sgi.getShopId());
                s.append(" and det.position_id in( SELECT p.position_id \n" +
                        " FROM POSITION p WHERE p.shop_id = ?)");
                param.add(sgi.getShopId());
            }
            if(sgi.getLanCode()!=null && !sgi.getLanCode().trim().isEmpty()){
                s.append(" AND UPPER(p.LANE_CODE) LIKE ? ");
                param.add("%"+ sgi.getLanCode()+ "%");
            }
            if(sgi.getPositionId()!=null){
                s.append(" AND p.POSITION_ID = ? ");
                param.add(sgi.getPositionId());
            }
            s.append(" Order by det.LAST_USED_DATE DESC, gg.EQUIPMENT_GROUP_CODE DESC   ");
            Query q = getSession().createSQLQuery(s.toString());
            for (int i = 0; i < param.size(); i++) {
                q.setParameter(i, param.get(i));
            }
            System.out.println("Query detail: ");
            System.out.println(s);
            System.out.println("Params: ");
            System.out.println(param);
            if (start != null && get != null && get > 0) {
                q.setFirstResult(start);
                q.setMaxResults(get);
            }
            List<StockGoodsInvSerialDTO> rs = new ArrayList<StockGoodsInvSerialDTO>();
            List<Object[]> a = q.list();
            for (Object[] o : a) {
                rs.add(convertObjectToEquipment(o));
            }
            return rs;
        } catch (Exception ex) {
            ex.printStackTrace();
            ex.printStackTrace();
            return new ArrayList<StockGoodsInvSerialDTO>();
        }
    }

    public List<StockGoodsInvSerialDTO> searchEquipmentId(String serial, Integer start, Integer get) {
        try {
            List param = new ArrayList();

            StringBuilder s = new StringBuilder(" select det.EQUIPMENT_ID, det.SPECIFICATION, " +
                    " p.SHOP_ID, s.SHOP_CODE, s.SHOP_NAME, p.POSITION_CODE, " +
                    " p.POSITION_NAME, det.MAINTANCE_PERIOD, me.MAINTENANCE_COUNT, me.LAST_MAINTAIN_DATE  " +
                    " from EQUIPMENT_DETAIL det \n" +
                    " left join POSITION p ON det.POSITION_ID = p.POSITION_ID\n" +
                    " left join SHOP s on s.SHOP_ID = p.SHOP_ID " +
                    " left join MAINTENANCE_EQUIPMENT me on det.EQUIPMENT_ID = me.EQUIPMENT_ID" +
                    " Where 1 = 1 ");

            if(serial!=null){
                s.append(" AND det.SERIAL = ? ");
                param.add(serial);
            }
            s.append(" order by s.SHOP_CODE, p.POSITION_CODE  ");
            Query q = getSession().createSQLQuery(s.toString());
            for (int i = 0; i < param.size(); i++) {
                q.setParameter(i, param.get(i));
            }
            System.out.println("Query detail: ");
            System.out.println(s);
            System.out.println("Params: ");
            System.out.println(param);
            if (start != null && get != null && get > 0) {
                q.setFirstResult(start);
                q.setMaxResults(get);
            }
            List<StockGoodsInvSerialDTO> rs = new ArrayList<StockGoodsInvSerialDTO>();
            List<Object[]> a = q.list();
            for (Object[] o : a) {
                rs.add(convertObjectEquipmentDetail(o));
            }
            return rs;
        } catch (Exception ex) {
            ex.printStackTrace();
            ex.printStackTrace();
            return new ArrayList<StockGoodsInvSerialDTO>();
        }
    }

    public List<StockGoodsInvSerialDTO> searchActionAudit(String  serial, Integer start, Integer get) {
        try {
            List param = new ArrayList();

            StringBuilder s = new StringBuilder(" select det.EQUIPMENT_ID, det.SERIAL," +
                    " a.ACTION_DATETIME, a.ACTION_TYPE from EQUIPMENT_DETAIL det\n" +
                    "    join EQUIPMENT_HISTORY h ON h.EQUIPMENT_ID = det.EQUIPMENT_ID\n" +
                    "    join ACTION_AUDIT a ON a.REFERENCE_ID = h.EQUIPMENT_HISTORY_ID\n" +
                    "    Where 1=1 ");
            if(serial!=null){
                s.append(" AND det.SERIAL = ? ");
                param.add(serial);
            }
//            and det.EQUIPMENT_ID = ? and det.SERIAL = ?
            s.append(" order by det.SERIAL, a.ACTION_DATETIME DESC  ");
            Query q = getSession().createSQLQuery(s.toString());
            for (int i = 0; i < param.size(); i++) {
                q.setParameter(i, param.get(i));
            }
            System.out.println("Query detail: ");
            System.out.println(s);
            System.out.println("Params: ");
            System.out.println(param);
            if (start != null && get != null && get > 0) {
                q.setFirstResult(start);
                q.setMaxResults(get);
            }
            List<StockGoodsInvSerialDTO> rs = new ArrayList<StockGoodsInvSerialDTO>();
            List<Object[]> a = q.list();
            for (Object[] o : a) {
                rs.add(convertObjectActionAudit(o));
            }
            return rs;
        } catch (Exception ex) {
            ex.printStackTrace();
            ex.printStackTrace();
            return new ArrayList<StockGoodsInvSerialDTO>();
        }
    }
    public List<StockGoodsInvSerialDTO> searchMaintainEquiment(String  serial,Long equipmentId, Integer start, Integer get) {
        try {
            List param = new ArrayList();

            StringBuilder s = new StringBuilder(" select mse.mse_id,\n" +
                    "det.EQUIPMENT_ID, det.SERIAL, p.POSITION_CODE, p.POSITION_NAME ,\n" +
                    "s.SHOP_CODE, s.SHOP_NAME, ms.FROM_DATE, ms.STATUS\n" +
                    "from EQUIPMENT_DETAIL det\n" +
                    " join POSITION p ON p.POSITION_ID = det.POSITION_ID\n" +
                    " join SHOP s ON p.SHOP_ID = s.SHOP_ID\n" +
                    "join MAINTENANCE_SCHEDULE_EQUIPMENT mse ON mse.EQUIPMENT_ID = det.EQUIPMENT_ID\n" +
                    "join MAINTENANCE_SCHEDULE ms ON mse.SCHEDULE_ID = ms.SCHEDULE_ID\n" +
                    "where 1=1  ");
            if(serial!=null){
                s.append(" AND det.SERIAL = ? ");
                param.add(serial);
            }
            if(equipmentId!=null){
                s.append(" AND det.EQUIPMENT_ID = ? ");
                param.add(equipmentId);
            }
//            and det.EQUIPMENT_ID = ? and det.SERIAL = ?
            s.append(" order by det.SERIAL, s.SHOP_CODE DESC  ");
            Query q = getSession().createSQLQuery(s.toString());
            for (int i = 0; i < param.size(); i++) {
                q.setParameter(i, param.get(i));
            }
            System.out.println("Query detail: ");
            System.out.println(s);
            System.out.println("Params: ");
            System.out.println(param);
            if (start != null && get != null && get > 0) {
                q.setFirstResult(start);
                q.setMaxResults(get);
            }
            List<StockGoodsInvSerialDTO> rs = new ArrayList<StockGoodsInvSerialDTO>();
            List<Object[]> a = q.list();
            for (Object[] o : a) {
                rs.add(convertObjectMantenEquiment(o));
            }
            return rs;
        } catch (Exception ex) {
            ex.printStackTrace();
            ex.printStackTrace();
            return new ArrayList<StockGoodsInvSerialDTO>();
        }
    }


    public Long searchSizeContainOutstockDatetime(Date fromOutstockDatetime, Date toOutstockDatetime, String from,
                                                  String to, StockGoodsInvSerial sgi, Long goodsGroupId) {
        try {
            List param = new ArrayList();

            StringBuilder s = new StringBuilder("SELECT  COUNT(1) FROM STOCK_GOODS_INV_SERIAL s " +
                    " JOIN EQUIPMENT_PROFILE g ON s.EQUIPMENT_PROFILE_ID =  g.PROFILE_ID " +
                    " JOIN EQUIPMENT_GROUP gg  ON g.EQUIPMENT_GROUP_ID = gg.EQUIPMENT_GROUP_ID WHERE 1=1");
            if (sgi.getEquipmentProfileId() != null && sgi.getEquipmentProfileId() > 0) {
                s.append(" And s.EQUIPMENT_PROFILE_ID = ?");
                param.add(sgi.getEquipmentProfileId());
            } else if (goodsGroupId != null && goodsGroupId > 0) {
                s.append(
                        " And gg.EQUIPMENT_GROUP_ID = ?");
                param.add(goodsGroupId);
            }
            if (from != null && !from.trim().isEmpty()) {
                s.append(" AND (( TO_CHAR(SERIAL) >= TO_CHAR(?) AND LENGTH(SERIAL) = ? ) OR LENGTH(SERIAL) > ? ) ");
                param.add(from);
                param.add(from.length());
                param.add(from.length());
            }
            if (to != null && !to.trim().isEmpty()) {
                s.append(" AND (( TO_CHAR(SERIAL) <= TO_CHAR(?) AND LENGTH(SERIAL) = ? ) OR LENGTH(SERIAL) < ? ) ");
                param.add(to);
                param.add(to.length());
                param.add(to.length());
            }
            if (sgi.getCurrentTaId() != null && sgi.getCurrentTaId() > 0) {
                s.append(" and CURRENT_TA_ID = ?");
                param.add(sgi.getCurrentTaId());
            }
            if (sgi.getShopId() != null && sgi.getShopId() > 0) {
                s.append(" and SHOP_ID = ?");
                param.add(sgi.getShopId());
            }

            // if(fromOutstockDatetime != null){
            // s.append(" and OUTSTOCK_DATETIME > ? ");
            // long t = DateTimeUtils.addDay(fromOutstockDatetime, -1).getTime();
            // java.sql.Date sqlDate = new java.sql.Date(t);
            // param.add(sqlDate);
            // }
            //
            // if(toOutstockDatetime != null){
            // s.append(" and OUTSTOCK_DATETIME < ? ");
            // long time = DateTimeUtils.addDay(toOutstockDatetime, +1).getTime();
            // java.sql.Date toDate = new java.sql.Date(time);
            // param.add(toDate);
            // }
            if (fromOutstockDatetime != null) {
                s.append(" and TO_CHAR(OUTSTOCK_DATETIME,'yyyy-MM-dd') >= ? ");
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                param.add(sf.format(fromOutstockDatetime));

            }

            if (toOutstockDatetime != null) {
                s.append(" and TO_CHAR(OUTSTOCK_DATETIME,'yyyy-MM-dd') < ? ");
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                param.add(sf.format(DateTimeUtils.addDay(toOutstockDatetime, 1)));

            }

            if (sgi.getStockStatus() != null && !sgi.getStockStatus().trim().isEmpty()
                    && !sgi.getStockStatus().trim().equals("0")) {
                if (sgi.getStockStatus().trim().contains(",")) {
                    String[] k = sgi.getStockStatus().trim().split(",");

                    s.append(" and STOCK_STATUS in (");
                    for (int i = 0; i < k.length; i++) {
                        s.append(i < k.length - 1 ? "?," : "?");
                        param.add(k[i]);
                    }
                    s.append(")");
                } else {
                    s.append(" and STOCK_STATUS = ?");
                    param.add(sgi.getStockStatus());
                }
            }

            if (sgi.getEquipmentProfileStatus() != null && sgi.getEquipmentProfileStatus() > 0) {
                s.append(" and EQUIPMENT_STATUS = ?");
                param.add(sgi.getEquipmentProfileStatus());
            }
            if (sgi.getProviderId() != null && sgi.getProviderId() > 0) {
                s.append(" and PROVIDER_ID= ?");
                param.add(sgi.getProviderId());
            }
            Query q = getSession().createSQLQuery(s.toString());
            for (int i = 0; i < param.size(); i++) {
                q.setParameter(i, param.get(i));
            }
            return Long.parseLong(q.uniqueResult().toString());
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

//    public Long searchSize(String from, String to, StockGoodsInvSerial sgi, Long goodsGroupId) {
//        try {
//            List param = new ArrayList();
//            StringBuilder s = new StringBuilder("SELECT  COUNT(1) FROM STOCK_GOODS_INV_SERIAL s " +
//                    " JOIN EQUIPMENT_PROFILE g ON s.EQUIPMENT_PROFILE_ID =  g.PROFILE_ID JOIN EQUIPMENT_GROUP gg  ON g.EQUIPMENT_GROUP_ID = gg.EQUIPMENT_GROUP_ID WHERE 1=1");
////            if (sgi.getEquipmentProfileId() != null && sgi.getEquipmentProfileId() > 0) {
////                s.append(" AND s.EQUIPMENT_PROFILE_ID = ?");
////                param.add(sgi.getEquipmentProfileId());
////            } else if (goodsGroupId != null && goodsGroupId > 0) {
////                s.append(
////                        " And gg.EQUIPMENT_GROUP_ID = ?");
////                param.add(goodsGroupId);
////            }
//            if (from != null && !from.trim().isEmpty()) {
//                s.append(" AND TO_NUMBER(SERIAL_SEARCH) >= TO_NUMBER(?) ");
//                param.add(from);
//            }
//            if (to != null && !to.trim().isEmpty()) {
//                s.append(" AND TO_NUMBER(SERIAL_SEARCH) <= TO_NUMBER(?) ");
//                param.add(to);
//            }
//            s.append(" AND SERIAL_SEARCH is not null and length(SERIAL_SEARCH) = 8 ");
//            if (sgi.getCurrentTaId() != null && sgi.getCurrentTaId() > 0) {
//                s.append(" and CURRENT_TA_ID = ?");
//                param.add(sgi.getCurrentTaId());
//            }
////            if (sgi.getShopId() != null && sgi.getShopId() > 0) {
////                s.append(" and SHOP_ID = ?");
////                param.add(sgi.getShopId());
////            }
////            if (sgi.getStockStatus() != null && !sgi.getStockStatus().trim().isEmpty()
////                    && !sgi.getStockStatus().trim().equals("0")) {
////                if (sgi.getStockStatus().trim().contains(",")) {
////                    String[] k = sgi.getStockStatus().trim().split(",");
////
////                    s.append(" and STOCK_STATUS in (");
////                    for (int i = 0; i < k.length; i++) {
////                        s.append(i < k.length - 1 ? "?," : "?");
////                        param.add(k[i]);
////                    }
////                    s.append(")");
////                } else {
////                    s.append(" and STOCK_STATUS = ?");
////                    param.add(sgi.getStockStatus());
////                }
////            }
////
////            if (sgi.getEquipmentProfileStatus() != null && sgi.getEquipmentProfileStatus() > 0) {
////                s.append(" and EQUIPMENT_STATUS = ?");
////                param.add(sgi.getEquipmentProfileStatus());
////            }
//            if (sgi.getProviderId() != null && sgi.getProviderId() > 0) {
//                s.append(" and PROVIDER_ID= ?");
//                param.add(sgi.getProviderId());
//            }
//            //		System.out.println("Query size: ");
//            //		System.out.println(s.toString());
//            //		System.out.println("Params: ");
//            //		System.out.println(param);
//            Query q = getSession().createSQLQuery(s.toString());
//            for (int i = 0; i < param.size(); i++) {
//                q.setParameter(i, param.get(i));
//            }
//            //		System.out.println(q.uniqueResult().toString());
//            return Long.parseLong(q.uniqueResult().toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return 0L;
//        }
//
//    }

    public void updateStatusByTaId(String stockStatus, Long taId, Long newTa) {
        Query q = getSession()
                .createQuery("Update StockGoodsInvNoSerial set stockStatus = ?, currentTaId = ? where currentTaId = ?");
        q.setParameter(0, stockStatus);
        q.setParameter(1, newTa != null ? newTa : -1L);
        q.setParameter(2, taId);
        q.executeUpdate();
        getSession().flush();
    }

    public List<StockGoodsInvSerial> searchByTaId(Long taId, String stockStatus, Long gId, Long gStatus) {
        try {
            Query q = getSession().createQuery(
                    "Select s from StockGoodsInvNoSerial s where stockStatus = ? and currentTaId = ? and equipmentProfileId = ? and equipmentProfileStatus = ? ORDER BY LENGTH(SERIAL), SERIAL  ");
            q.setParameter(0, stockStatus);
            q.setParameter(1, taId);
            q.setParameter(2, gId);
            q.setParameter(3, gStatus);
            return q.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<StockGoodsInvSerial>();
        }
    }

    public Long getSizeByTaId(Long taId) {
        try {
            Query q = getSession().createQuery("Select Count(1) from StockGoodsInvSerial s where  currentTaId = ? ");
            q.setParameter(0, taId);
            return (Long) q.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public Long getSizeByTaId(Long taId, String stockStatus, Long gId, Long gStatus) {
        try {
            Query q = getSession().createQuery(
                    "Select Count(1) from StockGoodsInvSerial s where stockStatus = ? and currentTaId = ? and equipmentProfileId = ? and equipmentProfileStatus = ? ORDER BY LENGTH(SERIAL), SERIAL  ");
            q.setParameter(0, stockStatus);
            q.setParameter(1, taId);
            q.setParameter(2, gId);
            q.setParameter(3, gStatus);
            return (Long) q.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public Long countGoods(Long equipmentProfileId, Long equipmentProfileStatus, Long shopId) {
        try {
            String s = "Select Count(1) from StockGoodsInvSerial s "
                    + "where  shopId = ? and equipmentProfileId = ? and equipmentProfileStatus = ?";
            Query query = session.createQuery(s);
            query.setParameter(0, shopId);
            query.setParameter(1, equipmentProfileId);
            query.setParameter(2, equipmentProfileStatus);

            return (Long) query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public Long getSizeStock(Long equipmentProfileId, Long status, Long shopId, Long stockSts) {
        try {
            StringBuilder s = new StringBuilder("Select s.quantity from StockGoodsInvNoSerial s where  shopId = ? ");
            List param = new ArrayList();
            param.add(shopId);
            if ((equipmentProfileId != null) && (equipmentProfileId != 0)) {
                s.append(" and s.equipmentProfileId = ? ");
                param.add(equipmentProfileId);
            }
            if ((status != null) && (status != 0)) {
                s.append(" and s.equipmentStatus = ? ");
                param.add(status);
            }
            if ((stockSts != null) && (stockSts != 0)) {
                s.append(" and stockStatus = ? ");
                param.add(stockSts.toString());
            }
//            System.out.println("No Serial");
//            System.out.println(s.toString());
//            System.out.println("Param");
//            System.out.println(param);
            Query query = session.createQuery(s.toString());
            for (int i = 0; i < param.size(); i++) {
                query.setParameter(i, param.get(i));
            }
            return query.uniqueResult() == null ? 0L : (Long) query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public Long getSizeStock(List<Long> listGoodsId, Long status, Long shopId, Long stockSts) {
        try {
            StringBuilder s = new StringBuilder("Select Count(1) from StockGoodsInvSerial s where  shopId = ? ");
            List param = new ArrayList();
            param.add(shopId);

            if ((listGoodsId != null) && (!listGoodsId.isEmpty())) {
                s.append(" and equipmentProfileId in ( ");
                for (int i = 0; i < listGoodsId.size(); i++) {
                    if (i != listGoodsId.size() - 1) {
                        s.append("?,");
                        param.add(listGoodsId.get(i));
                    } else {
                        s.append("?");
                        param.add(listGoodsId.get(i));
                    }
                }
                s.append(")");
            }

            if ((status != null) && (status != 0)) {
                s.append(" and equipmentProfileStatus = ? ");
                param.add(status);
            }
            if ((stockSts != null) && (stockSts != 0)) {
                s.append(" and stockStatus = ? ");
                param.add(stockSts.toString());
            }
            Query query = session.createQuery(s.toString());
            for (int i = 0; i < param.size(); i++) {
                query.setParameter(i, param.get(i));
            }
            return (Long) query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public Long getStockSize(Long goodsGroupId, Long equipmentProfileId, Long status, Long shopId, Long stockSts) {
        try {
            StringBuilder s = new StringBuilder("Select Count(1) from StockGoodsInvSerial s where  shopId = ? ");
            List param = new ArrayList();
            param.add(shopId);

            if ((goodsGroupId != null) && (goodsGroupId != 0)) {

                if ((equipmentProfileId != null) && (equipmentProfileId != 0)) {
                    s.append(" and s.equipmentProfileId = ? ");
                    param.add(equipmentProfileId);
                }

                if ((equipmentProfileId == null) || (equipmentProfileId == 0)) {
                    s.append(" and s.equipmentProfileId in (select equipmentProfileId from Goods where goodsGroupId = ?) ");
                    param.add(goodsGroupId);
                }

            } else {
                if (equipmentProfileId != null && equipmentProfileId != 0) {
                    s.append(" and s.equipmentProfileId = ? ");
                    param.add(equipmentProfileId);
                }
            }
            if ((status != null) && (status != 0)) {
                s.append(" and equipmentProfileStatus = ? ");
                param.add(status);
            }
            if ((stockSts != null) && (stockSts != 0)) {
                s.append(" and stockStatus = ? ");
                param.add(stockSts.toString());
            }
            Query query = session.createQuery(s.toString());
            for (int i = 0; i < param.size(); i++) {
                query.setParameter(i, param.get(i));
            }
            return (Long) query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public Boolean checkSerial(String serial, Long providerId) {
        Query query = session.createSQLQuery(
                "SELECT 1 from STOCK_GOODS_INV_SERIAL s where SERIAL = ? and PROVIDER_ID = ? and ROWNUM = 1");
        query.setParameter(0, serial);
        query.setParameter(1, providerId.toString());
        return query.uniqueResult() == null;
    }

    public Long checkApproveGoods(Long taId) {
        try {
            Query q = getSession().createSQLQuery(
                    "SELECT COUNT(1) FROM STOCK_TRANSACTION_SERIAL s JOIN STOCK_GOODS_INV_SERIAL t ON t.EQUIPMENT_PROFILE_ID = s.GOODS_ID AND t.EQUIPMENT_PROFILE_ID = s.GOODS_ID AND t.SERIAL = s.SERIAL WHERE s.IM_TRANSACTION_ID = ? ");
            q.setParameter(0, taId);
            return Long.parseLong(q.uniqueResult().toString());
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public void InsertByStockTransactionSerial(Long taId, Long shopId) {
        StringBuilder s = new StringBuilder(
                "Insert into StockGoodsInvSerial(shopId,equipmentProfileId,serial,equipmentProfileStatus,stockStatus,providerId,serialSearch) select ? , equipmentProfileId , serial, equipmentProfileStatus, ? , providerId, serialSearch from StockTransactionSerial ss where ss.imTransactionId =  ?");
        Query q = getSession().createQuery(s.toString());
        q.setParameter(0, shopId);
        q.setParameter(1, InventoryConstanst.StockStatus.INSTOCK);
        q.setParameter(2, taId);
        q.executeUpdate();
        getSession().flush();
    }

    public void updateByTaId(Long taId, Long shopId, String status, Long newtaId) {

        StringBuilder s = new StringBuilder("Update StockGoodsInvSerial set stockStatus = ?");
        List param = new ArrayList();
        param.add(InventoryConstanst.StockStatus.INSTOCK);
        if (shopId != null) {
            s.append(" , shopId = ?");
            param.add(shopId);
        }
        if (newtaId != null) {
            s.append(" , currentTaId = ? ");
            param.add(newtaId);
        } else {
            s.append(" , currentTaId = null ");
        }
        s.append("  where currentTaId = ?");
        param.add(taId);
        Query q = getSession().createQuery(s.toString());
        for (int i = 0; i < param.size(); i++) {
            q.setParameter(i, param.get(i));
        }
        q.executeUpdate();
        getSession().flush();
    }

    // CRM
    public Long checkBySerial(String serial, Long shopId) {
        if (serial == null || serial.trim().isEmpty() || shopId == null) {
            return 0L;
        } else {
            Query q = getSession().createQuery(
                    "Select Count(1) from StockGoodsInvSerial s where shopId = ? and equipmentProfileStatus = ? and stockStatus = ? and serial = ?");
            q.setParameter(0, shopId);
            q.setParameter(1, InventoryConstanst.GoodsStatus.NOMAL);
            q.setParameter(2, InventoryConstanst.StockStatus.INSTOCK);
            q.setParameter(3, serial);
            return (Long) q.uniqueResult();
        }
    }

    public StockGoodsInvSerial getBySerial(String serial, Long shopId) {
        if (serial == null || serial.trim().isEmpty() || shopId == null) {
            return null;
        } else {
            Query q = getSession().createQuery(
                    "Select s from StockGoodsInvSerial s where shopId = ? and equipmentProfileStatus = ? and serial = ?");
            q.setParameter(0, shopId);
            q.setParameter(1, InventoryConstanst.GoodsStatus.NOMAL);
            q.setParameter(2, serial);
            if (q.list().isEmpty()) {
                return null;
            } else {
                return (StockGoodsInvSerial) q.list().get(0);
            }
        }
    }

    public StockGoodsInvSerial getInstockBySerial(String serial, Long shopId) {
        if (serial == null || serial.trim().isEmpty() || shopId == null) {
            return null;
        } else {
            Query q = getSession().createQuery(
                    "Select s from StockGoodsInvSerial s where shopId = ? and equipmentProfileStatus = ? and serial = ? and stockStatus = ?");
            q.setParameter(0, shopId);
            q.setParameter(1, InventoryConstanst.GoodsStatus.NOMAL);
            q.setParameter(2, serial);
            q.setParameter(3, InventoryConstanst.StockStatus.INSTOCK);
            if (q.list().isEmpty()) {
                return null;
            } else {
                return (StockGoodsInvSerial) q.list().get(0);
            }
        }
    }
    public StockGoodsInvSerial getStockByProfileAndSerial(String serial, Long profileId) {
        if (serial == null || serial.trim().isEmpty() || profileId == null) {
            return null;
        } else {
            Query q = getSession().createQuery(
                    "Select s from StockGoodsInvSerial s where equipmentProfileId = ? and serial = ? ");
            q.setParameter(0, profileId);
            q.setParameter(1, serial);
            if (q.list().isEmpty()) {
                return null;
            } else {
                return (StockGoodsInvSerial) q.list().get(0);
            }
        }
    }


    public StockGoodsInvSerial  convertDTOtoEntity(StockGoodsInvSerialDTO dto){
        StockGoodsInvSerial entity = new StockGoodsInvSerial();
        if (dto==null) return entity;
        entity.setId(dto.getId());
        entity.setSerial(dto.getSerial());
        entity.setEquipmentProfileStatus(dto.getEquipmentProfileStatus());
        entity.setStockStatus(dto.getStockStatus());
        entity.setInstockDatetime(dto.getInstockDatetime());
        entity.setOutstockDatetime(dto.getOutstockDatetime());
        entity.setShopId(dto.getShopId());
        entity.setEtagNumber(dto.getStaffId());
        entity.setProviderId(dto.getProviderId());
        entity.setEquipmentProfileId(dto.getEquipmentProfileId());
        entity.setCurrentTaId(dto.getCurrentTaId());
        entity.setSerialSearch(dto.getSerialSearch());
        return entity;
    }
    public StockGoodsInvSerialDTO  convertEmtitytoDTO(StockGoodsInvSerial entity){

        StockGoodsInvSerialDTO dto = new StockGoodsInvSerialDTO();
        if (entity==null) return dto;
        dto.setSerial(entity.getSerial());
        dto.setId(entity.getId());
        dto.setEquipmentProfileStatus(entity.getEquipmentProfileStatus());
        dto.setStockStatus(entity.getStockStatus());
        dto.setInstockDatetime(entity.getInstockDatetime());
        dto.setOutstockDatetime(entity.getOutstockDatetime());
        dto.setShopId(entity.getShopId());
        dto.setProviderId(entity.getProviderId());
        dto.setEquipmentProfileId(entity.getEquipmentProfileId());
        dto.setCurrentTaId(entity.getCurrentTaId());
        dto.setSerialSearch(entity.getSerialSearch());
        dto.setStaffId(entity.getEtagNumber());
        return dto;
    }
    public List<StockGoodsInvSerialDTO>  convertEmtitystoDTOs(List<StockGoodsInvSerial> entitys){
        List<StockGoodsInvSerialDTO> dtos = new ArrayList<>();
        if (entitys==null) return dtos;
        for (StockGoodsInvSerial entity:entitys) {
            StockGoodsInvSerialDTO dto = new StockGoodsInvSerialDTO();
            dto.setId(entity.getId());
            dto.setSerial(entity.getSerial());
            dto.setEquipmentProfileStatus(entity.getEquipmentProfileStatus());
            dto.setStockStatus(entity.getStockStatus());
            dto.setInstockDatetime(entity.getInstockDatetime());
            dto.setOutstockDatetime(entity.getOutstockDatetime());
            dto.setShopId(entity.getShopId());
            dto.setProviderId(entity.getProviderId());
            dto.setEquipmentProfileId(entity.getEquipmentProfileId());
            dto.setCurrentTaId(entity.getCurrentTaId());
            dto.setSerialSearch(entity.getSerialSearch());
            dto.setStaffId(entity.getEtagNumber());
            dtos.add(dto);
        }
        return dtos;
    }

    public List<StockGoodsInvSerialDTO> getSerialStockGoods(Long shopId , Long equipmentId,
                                                            Long providerId, String stockStatus) {
        try {
            List param = new ArrayList();

            StringBuilder s =
                    new StringBuilder("SELECT sgis.ID,\n" +
                            "                    sgis.SHOP_ID, sgis.EQUIPMENT_PROFILE_ID, sgis.SERIAL,\n" +
                            "                    sgis.EQUIPMENT_STATUS, sgis.STOCK_STATUS,\n" +
                            "                    sgis.PROVIDER_ID, sgis.CURRENT_TA_ID,\n" +
                            "                    det.EQUIPMENT_ID , det.EQUIPMENT_GROUP_ID,\n" +
                            "                    det.POSITION_ID,\n" +
                            "                    det.LIFE_CYCLE, det.MAINTANCE_PERIOD,\n" +
                            "                    det.WARRANTY_COUNT, det.WARRANTY_PERIOD,\n" +
                            "                    det.CO_NUMBER,det.CQ_NUMBER,\n" +
                            "                    det.WARANTY_EXPIRED_DATE, det.SPECIFICATION,\n" +
                            "                    det.WARANTY_STATUS, det.IM_TRANSACTION_ID, stg.STOCK_GOODS_ID \n" +
                            "            FROM STOCK_GOODS_INV_SERIAL sgis\n" +
                            "            INNER JOIN STOCK_GOODS stg\n" +
                            "            ON sgis.SHOP_ID = stg.SHOP_ID AND sgis.EQUIPMENT_PROFILE_ID = stg.GOODS_ID\n" +
                            "           and stg.GOODS_STATUS = sgis.EQUIPMENT_STATUS " +
                            "            INNER JOIN EQUIPMENT_DETAIL det\n" +
                            "            ON sgis.EQUIPMENT_PROFILE_ID = det.EQUIPMENT_PROFILE_ID " +
                            "            AND  sgis.SERIAL = det.SERIAL AND det.PROVIDER_ID = sgis.PROVIDER_ID\n" +
//                            "                    --AND sgis.CURRENT_TA_ID = det.IM_TRANSACTION_ID\n" +
                            "            Where 1 = 1 ");
            if(shopId!=null){
                s.append(" AND sgis.SHOP_ID  = ? ");
                param.add(shopId);
            }
            if(equipmentId!=null){
                s.append(" AND sgis.EQUIPMENT_PROFILE_ID  = ? ");
                param.add(equipmentId);
            }
            if(providerId!=null){
                s.append(" AND sgis.PROVIDER_ID  = ? ");
                param.add(providerId);
            }
            if(stockStatus!=null && !stockStatus.isEmpty()){
                s.append(" AND sgis.STOCK_STATUS  = ? ");
                param.add(stockStatus);
            }
            s.append(" ORDER BY sgis.SERIAL  ");
            Query q = getSession().createSQLQuery(s.toString());
            for (int i = 0; i < param.size(); i++) {
                q.setParameter(i, param.get(i));
            }
            System.out.println("Query detail: ");
            System.out.println(s);
            System.out.println("Params: ");
            System.out.println(param);
            List<StockGoodsInvSerialDTO> rs = new ArrayList<StockGoodsInvSerialDTO>();
            List<Object[]> a = q.list();
            for (Object[] o : a) {
                rs.add(convertObjectSerialAndStockGoods(o));
            }
            return rs;
        } catch (Exception ex) {
            ex.printStackTrace();
            ex.printStackTrace();
            return new ArrayList<StockGoodsInvSerialDTO>();
        }
    }

    public List<StockGoodsInvSerialDTO> getSerialStockGoodsExMaintain(StockGoodsInvSerialDTO dto) {
        try {
            List param = new ArrayList();

            StringBuilder s =
                    new StringBuilder("SELECT sgis.ID,\n" +
                            "                    sgis.SHOP_ID, sgis.EQUIPMENT_PROFILE_ID, sgis.SERIAL,\n" +
                            "                    sgis.EQUIPMENT_STATUS, sgis.STOCK_STATUS,\n" +
                            "                    sgis.PROVIDER_ID, sgis.CURRENT_TA_ID,\n" +
                            "                    det.EQUIPMENT_ID , det.EQUIPMENT_GROUP_ID,\n" +
                            "                    det.POSITION_ID,\n" +
                            "                    det.LIFE_CYCLE, det.MAINTANCE_PERIOD,\n" +
                            "                    det.WARRANTY_COUNT, det.WARRANTY_PERIOD,\n" +
                            "                    det.CO_NUMBER,det.CQ_NUMBER,\n" +
                            "                    det.WARANTY_EXPIRED_DATE, det.SPECIFICATION,\n" +
                            "                    det.WARANTY_STATUS, det.IM_TRANSACTION_ID \n" +
                            "            FROM STOCK_GOODS_INV_SERIAL sgis\n" +
                            "            INNER JOIN EQUIPMENT_DETAIL det\n" +
                            "            ON sgis.EQUIPMENT_PROFILE_ID = det.EQUIPMENT_PROFILE_ID " +
                            "            AND  sgis.SERIAL = det.SERIAL AND det.PROVIDER_ID = sgis.PROVIDER_ID\n" +
//                            "                    --AND sgis.CURRENT_TA_ID = det.IM_TRANSACTION_ID\n" +
                            "            Where 1 = 1 ");
            if(dto.getShopId()!=null){
                s.append(" AND sgis.SHOP_ID  = ? ");
                param.add(dto.getShopId());
            }
            if(dto.getEquipmentProfileId()!=null){
                s.append(" AND sgis.EQUIPMENT_PROFILE_ID  = ? ");
                param.add(dto.getEquipmentProfileId());
            }
            if(dto.getProviderId()!=null){
                s.append(" AND sgis.PROVIDER_ID  = ? ");
                param.add(dto.getProviderId());
            }
            if(dto.getStockStatus()!=null && !dto.getStockStatus().isEmpty()){
                s.append(" AND sgis.STOCK_STATUS  = ? ");
                param.add(dto.getStockStatus());
            }
            if(dto.getSerial()!=null && !dto.getSerial().isEmpty()){
                s.append(" AND sgis.SERIAL  LIKE ? ");
                param.add("%" + dto.getSerial() + "%");
            }
            if(dto.getEquipmentProfileStatus()!=null){
                s.append(" AND sgis.EQUIPMENT_PROFILE_ID in(select sg.goods_id from STOCK_GOODS sg\n" +
                        "                where sg.shop_id = sgis.shop_id and sg.GOODS_ID = sgis.EQUIPMENT_PROFILE_ID  and sg.GOODS_STATUS = ? )");
                param.add(dto.getEquipmentProfileStatus());

            }
            s.append(" ORDER BY sgis.SERIAL  ");
            Query q = getSession().createSQLQuery(s.toString());
            for (int i = 0; i < param.size(); i++) {
                q.setParameter(i, param.get(i));
            }
            System.out.println("Query detail: ");
            System.out.println(s);
            System.out.println("Params: ");
            System.out.println(param);
            List<StockGoodsInvSerialDTO> rs = new ArrayList<StockGoodsInvSerialDTO>();
            List<Object[]> a = q.list();
            for (Object[] o : a) {
                rs.add(convertObjectSerialAndStockGoodsEx(o));
            }
            return rs;
        } catch (Exception ex) {
            ex.printStackTrace();
            ex.printStackTrace();
            return new ArrayList<StockGoodsInvSerialDTO>();
        }
    }

    public List<StockGoodsInvSerialDTO> getNotSerialStockGoodsEx(StockGoodsInvSerialDTO dto) {
        try {
            List param = new ArrayList();

            StringBuilder s =
                    new StringBuilder("SELECT sg.STOCK_GOODS_ID,\n" +
                            "  sg.SHOP_ID,\n" +
                            "  sg.GOODS_ID,\n" +
                            "  sg.QUANTITY,\n" +
                            "  sg.GOODS_STATUS,\n" +
                            "  sg.AVAILABLE_QUANTITY\n" +
                            "FROM STOCK_GOODS sg where  1= 1 \n");
            if(dto.getShopId()!=null){
                s.append(" AND sg.SHOP_ID  = ? ");
                param.add(dto.getShopId());
            }
            if(dto.getEquipmentProfileId()!=null){
                s.append(" and sg.goods_id in(select pr.PROFILE_ID from EQUIPMENT_PROFILE pr where  pr.PROFILE_ID = ? ) ");
                param.add(dto.getEquipmentProfileId());
                s.append(" AND sg.GOODS_ID = ?  ");
                param.add(dto.getEquipmentProfileId());
            }
            if(dto.getEquipmentProfileStatus()!=null){
                s.append(" AND sg.GOODS_STATUS = ?  ");
                param.add(dto.getEquipmentProfileStatus());

            }
            s.append(" ORDER BY sg.QUANTITY  ");
            Query q = getSession().createSQLQuery(s.toString());
            for (int i = 0; i < param.size(); i++) {
                q.setParameter(i, param.get(i));
            }
            System.out.println("Query detail: ");
            System.out.println(s);
            System.out.println("Params: ");
            System.out.println(param);
            List<StockGoodsInvSerialDTO> rs = new ArrayList<StockGoodsInvSerialDTO>();
            List<Object[]> a = q.list();
            for (Object[] o : a) {
                rs.add(convertObjectNotSerialAndStock(o));
            }
            return rs;
        } catch (Exception ex) {
            ex.printStackTrace();
            ex.printStackTrace();
            return new ArrayList<StockGoodsInvSerialDTO>();
        }
    }
    public StockGoodsInvSerialDTO convertObjectNotSerialAndStock(Object[] o) {
        StockGoodsInvSerialDTO rs = new StockGoodsInvSerialDTO();
        try {
            int i = 0;
            rs.setStockGoodsId(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setShopId(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setEquipmentProfileId(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setQuantity(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setEquipmentProfileStatus(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setAvailableQuantity(o[i] == null ? null : Long.parseLong(o[i].toString()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public StockGoodsInvSerialDTO convertObjectSerialAndStockGoodsEx(Object[] o) {
        StockGoodsInvSerialDTO rs = new StockGoodsInvSerialDTO();
        try {
            int i = 0;
            rs.setId(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setShopId(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setEquipmentProfileId(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setSerial(o[i] == null ? null : o[i].toString());
            i++;
            rs.setEquipmentProfileStatus(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setStockStatus(o[i] == null ? null : o[i].toString());
            i++;
            rs.setProviderId(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setCurrentTaId(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setEquimentDetailId(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setGroupId(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setPositionId(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setLifeCycle(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setMaintancePeriod(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setWaranCount(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setWarrantyPeriod(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setCoNuber(o[i] == null ? null : o[i].toString());
            i++;
            rs.setCqNumber(o[i] == null ? null : o[i].toString());
            i++;
            rs.setWarantyExpiredDate(o[i] == null ? null : (Date) o[i]);
            i++;
            rs.setSpecification(o[i] == null ? null : o[i].toString());
            i++;
            rs.setWarrantyStatus(o[i] == null ? null : Long.parseLong(o[i].toString()));
            i++;
            rs.setImTransaction(o[i] == null ? null : Long.parseLong(o[i].toString()));
//            i++;
//            rs.setStockGoodsId(o[i] == null ? null : Long.parseLong(o[i].toString()));
            rs.setQuantity(1L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }
}
