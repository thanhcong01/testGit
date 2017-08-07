/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.dao;

import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.bo.EquipmentsDetail;
import com.ftu.hibernate.GenericDAOHibernate;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Query;

/**
 * @author E5420
 */
public class EquipmentDetailDAO extends GenericDAOHibernate<EquipmentsDetail, Serializable> {

    public EquipmentDetailDAO() {
        super(EquipmentsDetail.class);
    }

    @Override
    public void saveOrUpdate(EquipmentsDetail a) {
        if(a.getId()!=null){
            getSession().merge(a);
        }else {
            getSession().saveOrUpdate(a);
        }
        getSession().flush();
    }
    public void saveOrUpdateNotMerge(EquipmentsDetail a) {
        if(a.getId()!=null){
            getSession().merge(a);
        }else {
            getSession().saveOrUpdate(a);
        }
        getSession().flush();
    }

//    public void saveOrUpdate(EquipmentsDetail a) {
//            getSession().saveOrUpdate(a);
//
//    }

    public void save(EquipmentsDetail a) {
        getSession().save(a);
        getSession().flush();
    }

    public void insertBysql(EquipmentsDetail a) {
        Query q = getSession().createSQLQuery(""
                + "INSERT INTO EQUIPMENT_DETAIL ( "
                + "EQUIPMENT_ID , SERIAL , IM_TRANSACTION_ID , PROVIDER_ID , EQUIPMENT_PROFILE_ID , EQUIPMENT_GROUP_ID , EQUIPMENT_STATUS , EQUIPMENT_CODE ) "
                + "VALUES ( "
                + "EQUIPMENT_DETAIL_SEQ.nextval , :serial , :taId , :prvId , :gid , :grid , :gsts , :eCode )");
        q.setParameter("serial", a.getSerial());
        q.setParameter("taId", a.getImTransactionId().toString());
        q.setParameter("prvId", a.getProviderId().toString());
        q.setParameter("gid", a.getEquipmentsProfileId().toString());
        q.setParameter("grid", a.getEquipmentsGroupId().toString());
        q.setParameter("gsts", a.getStatus().toString());
        q.setParameter("eCode", a.getEquipmentCode());
        q.executeUpdate();
    }

    public EquipmentsDetail getBySerial(Long taId, String serial) {
        Query query = getSession().createQuery("Select a from EquipmentsDetail a where imTransactionId = ? and serial = ?");
        query.setParameter(0, taId);
        query.setParameter(1, serial);
        if (query.list().isEmpty()) {
            return null;
        } else {
            return (EquipmentsDetail) query.list().get(0);
        }
    }
    public Long  getStatusBySerial(String serial) {
        Query query = getSession().createQuery("Select a from EquipmentsDetail a where serial = ?");
        query.setParameter(0, serial);
        List<EquipmentsDetail> lst = query.list();
        if (lst == null || lst.isEmpty()) {
            return null;
        } else {
            return  lst.get(0).getStatus();
        }
    }
    public List<EquipmentsDetail> getByImTransactionId(Long taId) {
        Query query = getSession().createQuery("Select a from EquipmentsDetail a where imTransactionId = ? ");
        if(taId == null) return null;
        query.setParameter(0, taId);
        if (query.list().isEmpty()) {
            return null;
        } else {
            return  query.list();
        }
    }

    public EquipmentsDetail findBySerial(String serial) {
        Query query = getSession().createQuery("Select a from EquipmentsDetail a where serial = ?");
        query.setParameter(0, serial);
        if (query.list().isEmpty()) {
            return null;
        } else {
            return (EquipmentsDetail) query.list().get(0);
        }
    }
    public Integer countPosition(Long positionId) {
        Query query = getSession().createQuery("Select a from EquipmentsDetail a where positionId = ?");
        query.setParameter(0, positionId);
        if (query.list().isEmpty()) {
            return 0;
        } else {
            return query.list().size();
        }
    }
    public List<EquipmentsDetail> findByProfile(Long equipmentsProfileId) {
        Query query = getSession().createQuery("Select a from EquipmentsDetail a where equipmentsProfileId = ?");
        query.setParameter(0, equipmentsProfileId);
        return query.list();
    }
    public EquipmentsDetail findBySerialAndProviderIdEquip(String serial, Long prvId, Long equipId) {
        Query query = getSession().createQuery("Select a from EquipmentsDetail a where serial = ? and providerId = ? and equipmentsProfileId = ?");
        query.setParameter(0, serial);
        query.setParameter(1, prvId);
        query.setParameter(2, equipId);
        if (query.list().isEmpty()) {
            return null;
        } else {
            return (EquipmentsDetail) query.list().get(0);
        }
    }
    public void updateWaranStatus(List<Long> lstEquipmentId, List<Long> lstProfileId, Long waranStatus){
        Query q = getSession().createSQLQuery(
                "UPDATE EQUIPMENT_DETAIL set WARANTY_STATUS = :WARANTY_STATUS where  1=1 AND (EQUIPMENT_ID IN :EQUIPMENT_ID" +
                        " OR EQUIPMENT_PROFILE_ID IN  :EQUIPMENT_PROFILE_ID ) ");
        if(lstEquipmentId!=null && !lstEquipmentId.isEmpty()){
            q.setParameterList("EQUIPMENT_ID", lstEquipmentId);
        }else {
            lstEquipmentId.add(-1L);
            q.setParameterList("EQUIPMENT_ID", lstEquipmentId);
        }
        if(lstProfileId!=null && !lstProfileId.isEmpty()){
            q.setParameterList("EQUIPMENT_PROFILE_ID", (Collection) lstProfileId);
        }else {
            lstProfileId.add(-1L);
            q.setParameterList("EQUIPMENT_PROFILE_ID", (Collection) lstProfileId);
        }
//        if(waranStatus!=null){
            q.setParameter("WARANTY_STATUS", waranStatus);
//        }

        q.executeUpdate();
        getSession().flush();
    }


    public void updateStatus(List<Long> lstEquipmentId, Long status){
        Query q = getSession().createSQLQuery(
                "UPDATE EQUIPMENT_DETAIL set EQUIPMENT_STATUS = :EQUIPMENT_STATUS where  1=1 AND EQUIPMENT_ID IN :EQUIPMENT_ID ");
        if(lstEquipmentId!=null && !lstEquipmentId.isEmpty()){
            q.setParameterList("EQUIPMENT_ID", lstEquipmentId);
        }else {
            lstEquipmentId.add(-1L);
            q.setParameterList("EQUIPMENT_ID", lstEquipmentId);
        }
        q.setParameter("EQUIPMENT_STATUS", status);

        q.executeUpdate();
        getSession().flush();
    }

    public void deleteByTaId(Long taId) {
        Query q4 = getSession().createQuery("Delete from EquipmentsDetail where imTransactionId = ? ");
        q4.setParameter(0, taId);
        q4.executeUpdate();
        getSession().flush();
    }

    public EquipmentsDetail convertObjectToSerial(Object[] o) {
        EquipmentsDetail rs = new EquipmentsDetail();
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            rs.setId(o[0] == null ? null : Long.parseLong(o[0].toString()));
            rs.setSerial(o[1] == null ? null : o[1].toString());
            rs.setImTransactionId(o[2] == null ? null : Long.parseLong(o[2].toString()));
            rs.setEquipmentsProfileId(o[3] == null ? null : Long.parseLong(o[3].toString()));
            rs.setEquipmentsGroupId(o[4] == null ? null : Long.parseLong(o[4].toString()));
            rs.setStatus(o[5] == null ? null : Long.parseLong(o[5].toString()));
            rs.setEquipmentSpecification(o[6] == null ? null : o[6].toString());
            rs.setPositionId(o[7] == null ? null : Long.parseLong(o[7].toString()));
            rs.setProviderId(o[8] == null ? null : Long.parseLong(o[8].toString()));
            rs.setLifeCycle(o[9] == null ? null : Long.parseLong(o[9].toString()));
            rs.setLastUsedDate(o[10] == null ? null : (Date)o[10]);
            rs.setMaintancePeriod(o[11] == null ? null : Long.parseLong(o[11].toString()));
            rs.setWarrantyCount(o[12] == null ? null : Long.parseLong(o[12].toString()));
            rs.setWarrantyPeriod(o[13] == null ? null : Long.parseLong(o[13].toString()));
            rs.setEquipmentCode(o[14] == null ? null : o[14].toString());
            rs.setCoNumber(o[15] == null ? null : o[15].toString());
            rs.setCqNumber(o[16] == null ? null : o[16].toString());
//            rs.setWarantyExpiredDate(o[17] == null ? null : new Date(((Timestamp)o[17]).getTime()));
            rs.setWarantyExpiredDate(o[17] == null ? null : (Date)o[17]);

            rs.setProfileName(o[18] == null ? null : o[18].toString());
            rs.setEquipmentsProfileCode(o[19] == null ? null : o[19].toString());
            rs.setGroupCode(o[20] == null ? null : o[20].toString());
            rs.setTransactionActionCode(o[21] == null ? null : o[21].toString());
            rs.setTransactionActionID(o[22] == null ? null : Long.parseLong(o[22].toString()));
            rs.setStockStatusId(o[23] == null ? null : Long.parseLong(o[23].toString()));
            rs.setStatus(o[24] == null ? null : Long.parseLong(o[24].toString()));
            rs.setQuantitySerial(o[25] == null ? null : Long.parseLong(o[25].toString()));
            rs.setShopId(o[26] == null ? null : Long.parseLong(o[26].toString()));
            rs.setWarantyStatus(o[27] == null ? 2L : Long.parseLong(o[27].toString()));
            rs.setWarrantyReason(o[28] == null ? null : o[28].toString());
            if(rs.getStockStatusId().toString().equals(InventoryConstanst.StockStatus.INSTOCK)){
                rs.setTransactionActionCode("");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }


    public List<EquipmentsDetail> searchNoserial(EquipmentsDetail sgi, Integer start, Integer get, String sortField,Boolean desc) {
        try {
            List param = new ArrayList();
            StringBuilder s = new StringBuilder(" SELECT EQUIPMENT_ID id,\n" +
                    "  '' serial, det.IM_TRANSACTION_ID imTransactionId,\n" +
                    "  det.EQUIPMENT_PROFILE_ID equipmentsProfileId, det.EQUIPMENT_GROUP_ID equipmentsGroupId,\n" +
                    "  det.EQUIPMENT_STATUS status, det.SPECIFICATION as equipmentSpecification,\n" +
                    "  det.POSITION_ID positionId, det.PROVIDER_ID providerId,\n" +
                    "  det.LIFE_CYCLE lifeCycle, det.LAST_USED_DATE lastUsedDate,\n" +
                    "  det.MAINTANCE_PERIOD maintancePeriod, det.WARRANTY_COUNT warrantyCount,\n" +
                    "  det.WARRANTY_PERIOD warrantyPeriod, det.EQUIPMENT_CODE equipmentCode,\n" +
                    "  det.CO_NUMBER coNumber, det.CQ_NUMBER cqNumber, det.WARANTY_EXPIRED_DATE warantyExpiredDate, \n" +
                    "  pro.PROFILE_NAME profileName, pro.PROFILE_CODE equipmentsProfileCode,\n" +
                    "  grp.EQUIPMENT_GROUP_CODE groupCode,tr.TRANSACTION_ACTION_CODE transactionActionCode, tr.TRANSACTION_ACTION_ID transactionActionID, " +
                    " sgis.STOCK_STATUS stockStatusId, sgis.EQUIPMENT_STATUS goodStatus, sgis.QUANTITY quantitySerial" +
                    " ,  sgis.SHOP_ID shopId,  det.WARANTY_STATUS,  det.WARRANTY_REASON \n" +
                    " FROM EQUIPMENT_DETAIL det LEFT JOIN EQUIPMENT_PROFILE pro ON det.EQUIPMENT_PROFILE_ID = pro.PROFILE_ID\n" +
                    " INNER JOIN STOCK_GOODS_INV_NOSERIAL sgis on det.EQUIPMENT_PROFILE_ID = sgis.EQUIPMENT_PROFILE_ID and det.PROVIDER_ID =  sgis.PROVIDER_ID "+
                    " LEFT JOIN EQUIPMENT_GROUP grp ON det.EQUIPMENT_GROUP_ID = grp.EQUIPMENT_GROUP_ID\n" +
                    " INNER JOIN TRANSACTION_ACTION tr ON sgis.CURRENT_TA_ID = tr.TRANSACTION_ACTION_ID  " +
                    " WHERE 1 = 1 ");
            if (sgi.getEquipmentsProfileId() != null && sgi.getEquipmentsProfileId() > 0) {
                s.append(" and det.EQUIPMENT_PROFILE_ID = ? ");
                param.add(sgi.getEquipmentsProfileId());
            }
            if (sgi.getEquipmentsGroupId() != null && sgi.getEquipmentsGroupId() > 0) {
                s.append(" and det.EQUIPMENT_GROUP_ID = ? ");
                param.add(sgi.getEquipmentsGroupId());
            }
            if (sgi.getSerial() != null && !sgi.getSerial().trim().isEmpty()) {
                s.append(" AND UPPER(det.SERIAL) like ? ");
                param.add("%" + sgi.getSerial().toUpperCase() + "%");
            }

            if (sgi.getShopId() != null && sgi.getShopId() > 0) {
//                s.append(" and det.position_id in( SELECT p.position_id \n" +
//                        " FROM POSITION p WHERE p.shop_id = ?)");
                s.append(" AND sgis.SHOP_ID = ? ");
                param.add(sgi.getShopId());
            }

            if (sgi.getTransactionActionCode() != null && !sgi.getTransactionActionCode().trim().isEmpty() && !sgi.getTransactionActionCode().trim().equals("Tất cả")) {
                s.append(" and tr.TRANSACTION_ACTION_CODE = ?");
                param.add(sgi.getTransactionActionCode());
            }
            if (sgi.getDateInventory() != null) {
                s.append(" and trunc(tr.CREATE_DATETIME) = TO_DATE(?,'dd-MON-yy') ");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy");
                String string = simpleDateFormat.format(sgi.getDateInventory());
                param.add(string);
            }
            if (sgi.getStaffId() != null) {
                s.append(" and tr.ASSESSMENT_STAFF_ID = ?");
                param.add(sgi.getStaffId());
            }
            if (sgi.getActionType() != null && sgi.getActionDate() != null) {
                s.append(" AND det.EQUIPMENT_ID in(select EQUIPMENT_ID from EQUIPMENT_HISTORY h join ACTION_AUDIT a on h.EQUIPMENT_HISTORY_ID = a.REFERENCE_ID where a.ACTION_TYPE = ? and a.ACTION_DATETIME = ? ) ");
                param.add(sgi.getActionType());
                param.add(sgi.getActionDate());
            }else if(sgi.getActionType()!=null){
                s.append(" AND det.EQUIPMENT_ID in(select EQUIPMENT_ID from EQUIPMENT_HISTORY h join ACTION_AUDIT a on h.EQUIPMENT_HISTORY_ID = a.REFERENCE_ID where a.ACTION_TYPE = ? ) ");
                param.add(sgi.getActionType());
            }else if(sgi.getActionDate() != null){
                s.append(" AND det.EQUIPMENT_ID in(select EQUIPMENT_ID from EQUIPMENT_HISTORY h join ACTION_AUDIT a on h.EQUIPMENT_HISTORY_ID = a.REFERENCE_ID where a.ACTION_DATETIME = ? ) ");
                param.add(sgi.getActionDate());
            }
            if(sgi.getStockStatusId()!=null && sgi.getStockStatusId().equals(-1L)){
                s.append(" AND sgis.STOCK_STATUS in(1, 8, 9) ");
            }else if(sgi.getStockStatusId()!=null ){
                s.append(" AND sgis.STOCK_STATUS  = ? ");
                param.add(sgi.getStockStatusId());
            }
            if(sortField != null){
                if(desc){
                    s.append(" order by UPPER(" + sortField + ") desc ");
                } else{
                    s.append(" order by UPPER(" + sortField + ")  asc ");
                }
            }else {
                s.append(" ORDER BY grp.EQUIPMENT_GROUP_CODE,  pro.PROFILE_CODE, det.SERIAL ");
            }

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
            List<EquipmentsDetail> rs = new ArrayList<EquipmentsDetail>();
            List<Object[]> a = q.list();
            for (Object[] o : a) {
                rs.add(convertObjectToSerial(o));
            }
            return rs;
        } catch (Exception ex) {
            return new ArrayList<EquipmentsDetail>();
        }
    }
    public Long searchSizeNoserial(EquipmentsDetail sgi) {
        try {
            List param = new ArrayList();
            StringBuilder s = new StringBuilder(" SELECT  COUNT(1) " +
                    " FROM EQUIPMENT_DETAIL det LEFT JOIN EQUIPMENT_PROFILE pro ON det.EQUIPMENT_PROFILE_ID = pro.PROFILE_ID\n" +
                    " INNER JOIN STOCK_GOODS_INV_NOSERIAL sgis on det.EQUIPMENT_PROFILE_ID = sgis.EQUIPMENT_PROFILE_ID and det.PROVIDER_ID =  sgis.PROVIDER_ID "+
                    " LEFT JOIN EQUIPMENT_GROUP grp ON det.EQUIPMENT_GROUP_ID = grp.EQUIPMENT_GROUP_ID\n" +
                    " INNER JOIN TRANSACTION_ACTION tr ON sgis.CURRENT_TA_ID = tr.TRANSACTION_ACTION_ID  " +
                    " WHERE 1 = 1 ");

            if (sgi.getEquipmentsProfileId() != null && sgi.getEquipmentsProfileId() > 0) {
                s.append(" and det.EQUIPMENT_PROFILE_ID = ? ");
                param.add(sgi.getEquipmentsProfileId());
            }
            if (sgi.getEquipmentsGroupId() != null && sgi.getEquipmentsGroupId() > 0) {
                s.append(" and det.EQUIPMENT_GROUP_ID = ? ");
                param.add(sgi.getEquipmentsGroupId());
            }
            if (sgi.getSerial() != null && !sgi.getSerial().trim().isEmpty()) {
                s.append(" AND UPPER(det.SERIAL) like ? ");
                param.add("%" + sgi.getSerial().toUpperCase() + "%");
            }

            if (sgi.getShopId() != null && sgi.getShopId() > 0) {
//                s.append(" and det.position_id in( SELECT p.position_id \n" +
//                        " FROM POSITION p WHERE p.shop_id = ?)");
//                param.add(sgi.getShopId());
                s.append(" AND sgis.SHOP_ID = ? ");
                param.add(sgi.getShopId());
            }

            if (sgi.getTransactionActionCode() != null && !sgi.getTransactionActionCode().trim().isEmpty()
                    && !sgi.getTransactionActionCode().trim().equals("Tất cả")) {
                s.append(" and tr.TRANSACTION_ACTION_CODE = ?");
                param.add(sgi.getTransactionActionCode());
            }
            if (sgi.getDateInventory() != null) {
                s.append(" and trunc(tr.CREATE_DATETIME) = TO_DATE(?,'dd-MON-yy') ");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy");
                String string = simpleDateFormat.format(sgi.getDateInventory());
                param.add(string);
            }
            if (sgi.getStaffId() != null) {
                s.append(" and tr.ASSESSMENT_STAFF_ID = ?");
                param.add(sgi.getStaffId());
            }
            if (sgi.getActionType() != null && sgi.getActionDate() != null) {
                s.append(" AND det.EQUIPMENT_ID in(select EQUIPMENT_ID from EQUIPMENT_HISTORY h join ACTION_AUDIT a on h.EQUIPMENT_HISTORY_ID = a.REFERENCE_ID where a.ACTION_TYPE = ? and a.ACTION_DATETIME = ? ) ");
                param.add(sgi.getActionType());
                param.add(sgi.getActionDate());
            }else if(sgi.getActionType()!=null){
                s.append(" AND det.EQUIPMENT_ID in(select EQUIPMENT_ID from EQUIPMENT_HISTORY h join ACTION_AUDIT a on h.EQUIPMENT_HISTORY_ID = a.REFERENCE_ID where a.ACTION_TYPE = ? ) ");
                param.add(sgi.getActionType());
            }else if(sgi.getActionDate() != null){
                s.append(" AND det.EQUIPMENT_ID in(select EQUIPMENT_ID from EQUIPMENT_HISTORY h join ACTION_AUDIT a on h.EQUIPMENT_HISTORY_ID = a.REFERENCE_ID where a.ACTION_DATETIME = ? ) ");
                param.add(sgi.getActionDate());
            }
            if(sgi.getStockStatusId()!=null && sgi.getStockStatusId().equals(-1L)){
                s.append(" AND sgis.STOCK_STATUS in(1, 8, 9) ");
            }else if(sgi.getStockStatusId()!=null ){
                s.append(" AND sgis.STOCK_STATUS  = ? ");
                param.add(sgi.getStockStatusId());
            }
            s.append(" ORDER BY grp.EQUIPMENT_GROUP_CODE,  pro.PROFILE_CODE, det.SERIAL ");
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

    public List<EquipmentsDetail> search(EquipmentsDetail sgi, Integer start, Integer get, String sortField,Boolean desc) {
        try {
            List param = new ArrayList();
            StringBuilder s = new StringBuilder(" SELECT EQUIPMENT_ID id,\n" +
                    "  det.SERIAL serial, det.IM_TRANSACTION_ID imTransactionId,\n" +
                    "  det.EQUIPMENT_PROFILE_ID equipmentsProfileId, det.EQUIPMENT_GROUP_ID equipmentsGroupId,\n" +
                    "  det.EQUIPMENT_STATUS status, det.SPECIFICATION as equipmentSpecification,\n" +
                    "  det.POSITION_ID positionId, det.PROVIDER_ID providerId,\n" +
                    "  det.LIFE_CYCLE lifeCycle, det.LAST_USED_DATE lastUsedDate,\n" +
                    "  det.MAINTANCE_PERIOD maintancePeriod, det.WARRANTY_COUNT warrantyCount,\n" +
                    "  det.WARRANTY_PERIOD warrantyPeriod, det.EQUIPMENT_CODE equipmentCode,\n" +
                    "  det.CO_NUMBER coNumber, det.CQ_NUMBER cqNumber, det.WARANTY_EXPIRED_DATE warantyExpiredDate, \n" +
                    "  pro.PROFILE_NAME profileName, pro.PROFILE_CODE equipmentsProfileCode,\n" +
                    "  grp.EQUIPMENT_GROUP_CODE groupCode,tr.TRANSACTION_ACTION_CODE transactionActionCode, tr.TRANSACTION_ACTION_ID transactionActionID, " +
                    " sgis.STOCK_STATUS stockStatusId, sgis.EQUIPMENT_STATUS goodStatus, 1 as quantitySerial" +
                    " ,  sgis.SHOP_ID shopId,  det.WARANTY_STATUS,  det.WARRANTY_REASON \n" +
                    " FROM EQUIPMENT_DETAIL det LEFT JOIN EQUIPMENT_PROFILE pro ON det.EQUIPMENT_PROFILE_ID = pro.PROFILE_ID\n" +
                    " INNER JOIN STOCK_GOODS_INV_SERIAL sgis on det.EQUIPMENT_PROFILE_ID = sgis.EQUIPMENT_PROFILE_ID and det.SERIAL = sgis.SERIAL "+
                    " LEFT JOIN EQUIPMENT_GROUP grp ON det.EQUIPMENT_GROUP_ID = grp.EQUIPMENT_GROUP_ID\n" +
                    " INNER JOIN TRANSACTION_ACTION tr ON sgis.CURRENT_TA_ID = tr.TRANSACTION_ACTION_ID  " +
                    " WHERE 1 = 1 ");

            if (sgi.getEquipmentsProfileId() != null && sgi.getEquipmentsProfileId() > 0) {
                s.append(" and det.EQUIPMENT_PROFILE_ID = ? ");
                param.add(sgi.getEquipmentsProfileId());
            }
            if (sgi.getEquipmentsGroupId() != null && sgi.getEquipmentsGroupId() > 0) {
                s.append(" and det.EQUIPMENT_GROUP_ID = ? ");
                param.add(sgi.getEquipmentsGroupId());
            }
            if (sgi.getSerial() != null && !sgi.getSerial().trim().isEmpty()) {
                s.append(" AND UPPER(det.SERIAL) like ? ");
                param.add("%" + sgi.getSerial().toUpperCase() + "%");
            }

            if (sgi.getShopId() != null && sgi.getShopId() > 0) {
//                s.append(" and det.position_id in( SELECT p.position_id \n" +
//                        " FROM POSITION p WHERE p.shop_id = ?)");
                s.append(" AND sgis.SHOP_ID = ? ");
                param.add(sgi.getShopId());
            }

            if (sgi.getTransactionActionCode() != null && !sgi.getTransactionActionCode().trim().isEmpty() && !sgi.getTransactionActionCode().trim().equals("Tất cả")) {
                s.append(" and tr.TRANSACTION_ACTION_CODE = ?");
                param.add(sgi.getTransactionActionCode());
            }
            if (sgi.getDateInventory() != null) {
                s.append(" and trunc(tr.CREATE_DATETIME) = TO_DATE(?,'dd-MON-yy') ");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy");
                String string = simpleDateFormat.format(sgi.getDateInventory());
                param.add(string);
            }
            if (sgi.getStaffId() != null) {
                s.append(" and tr.ASSESSMENT_STAFF_ID = ?");
                param.add(sgi.getStaffId());
            }
            if (sgi.getActionType() != null && sgi.getActionDate() != null) {
                s.append(" AND det.EQUIPMENT_ID in(select EQUIPMENT_ID from EQUIPMENT_HISTORY h join ACTION_AUDIT a on h.EQUIPMENT_HISTORY_ID = a.REFERENCE_ID where a.ACTION_TYPE = ? and a.ACTION_DATETIME = ? ) ");
                param.add(sgi.getActionType());
                param.add(sgi.getActionDate());
            }else if(sgi.getActionType()!=null){
                s.append(" AND det.EQUIPMENT_ID in(select EQUIPMENT_ID from EQUIPMENT_HISTORY h join ACTION_AUDIT a on h.EQUIPMENT_HISTORY_ID = a.REFERENCE_ID where a.ACTION_TYPE = ? ) ");
                param.add(sgi.getActionType());
            }else if(sgi.getActionDate() != null){
                s.append(" AND det.EQUIPMENT_ID in(select EQUIPMENT_ID from EQUIPMENT_HISTORY h join ACTION_AUDIT a on h.EQUIPMENT_HISTORY_ID = a.REFERENCE_ID where a.ACTION_DATETIME = ? ) ");
                param.add(sgi.getActionDate());
            }
            if(sgi.getStockStatusId()!=null && sgi.getStockStatusId().equals(-1L)){
                s.append(" AND sgis.STOCK_STATUS in(1, 8, 9) ");
            }else if(sgi.getStockStatusId()!=null ){
                s.append(" AND sgis.STOCK_STATUS  = ? ");
                param.add(sgi.getStockStatusId());
            }
            if(sortField != null){
                if(desc){
                    s.append(" order by UPPER(" + sortField + ") desc ");
                } else{
                    s.append(" order by UPPER(" + sortField + ")  asc ");
                }
            }else {
                s.append(" ORDER BY grp.EQUIPMENT_GROUP_CODE,  pro.PROFILE_CODE, det.SERIAL ");
            }

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
            List<EquipmentsDetail> rs = new ArrayList<EquipmentsDetail>();
            List<Object[]> a = q.list();
            for (Object[] o : a) {
                rs.add(convertObjectToSerial(o));
            }
            return rs;
        } catch (Exception ex) {
            return new ArrayList<EquipmentsDetail>();
        }
    }

    public Long searchSize(EquipmentsDetail sgi) {
        try {
            List param = new ArrayList();
            StringBuilder s = new StringBuilder(" SELECT  COUNT(1) " +
                    " FROM EQUIPMENT_DETAIL det LEFT JOIN EQUIPMENT_PROFILE pro ON det.EQUIPMENT_PROFILE_ID = pro.PROFILE_ID\n" +
                    " INNER JOIN STOCK_GOODS_INV_SERIAL sgis on det.EQUIPMENT_PROFILE_ID = sgis.EQUIPMENT_PROFILE_ID and det.SERIAL = sgis.SERIAL"+
                    " LEFT JOIN EQUIPMENT_GROUP grp ON det.EQUIPMENT_GROUP_ID = grp.EQUIPMENT_GROUP_ID\n" +
                    " INNER JOIN TRANSACTION_ACTION tr ON sgis.CURRENT_TA_ID = tr.TRANSACTION_ACTION_ID  " +
                    " WHERE 1 = 1 ");

            if (sgi.getEquipmentsProfileId() != null && sgi.getEquipmentsProfileId() > 0) {
                s.append(" and det.EQUIPMENT_PROFILE_ID = ? ");
                param.add(sgi.getEquipmentsProfileId());
            }
            if (sgi.getEquipmentsGroupId() != null && sgi.getEquipmentsGroupId() > 0) {
                s.append(" and det.EQUIPMENT_GROUP_ID = ? ");
                param.add(sgi.getEquipmentsGroupId());
            }
            if (sgi.getSerial() != null && !sgi.getSerial().trim().isEmpty()) {
                s.append(" AND UPPER(det.SERIAL) like ? ");
                param.add("%" + sgi.getSerial().toUpperCase() + "%");
            }

            if (sgi.getShopId() != null && sgi.getShopId() > 0) {
//                s.append(" and det.position_id in( SELECT p.position_id \n" +
//                        " FROM POSITION p WHERE p.shop_id = ?)");
//                param.add(sgi.getShopId());
                s.append(" AND sgis.SHOP_ID = ? ");
                param.add(sgi.getShopId());
            }

            if (sgi.getTransactionActionCode() != null && !sgi.getTransactionActionCode().trim().isEmpty()
                    && !sgi.getTransactionActionCode().trim().equals("Tất cả")) {
                s.append(" and tr.TRANSACTION_ACTION_CODE = ?");
                param.add(sgi.getTransactionActionCode());
            }
            if (sgi.getDateInventory() != null) {
                s.append(" and trunc(tr.CREATE_DATETIME) = TO_DATE(?,'dd-MON-yy') ");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy");
                String string = simpleDateFormat.format(sgi.getDateInventory());
                param.add(string);
            }
            if (sgi.getStaffId() != null) {
                s.append(" and tr.ASSESSMENT_STAFF_ID = ?");
                param.add(sgi.getStaffId());
            }
            if (sgi.getActionType() != null && sgi.getActionDate() != null) {
                s.append(" AND det.EQUIPMENT_ID in(select EQUIPMENT_ID from EQUIPMENT_HISTORY h join ACTION_AUDIT a on h.EQUIPMENT_HISTORY_ID = a.REFERENCE_ID where a.ACTION_TYPE = ? and a.ACTION_DATETIME = ? ) ");
                param.add(sgi.getActionType());
                param.add(sgi.getActionDate());
            }else if(sgi.getActionType()!=null){
                s.append(" AND det.EQUIPMENT_ID in(select EQUIPMENT_ID from EQUIPMENT_HISTORY h join ACTION_AUDIT a on h.EQUIPMENT_HISTORY_ID = a.REFERENCE_ID where a.ACTION_TYPE = ? ) ");
                param.add(sgi.getActionType());
            }else if(sgi.getActionDate() != null){
                s.append(" AND det.EQUIPMENT_ID in(select EQUIPMENT_ID from EQUIPMENT_HISTORY h join ACTION_AUDIT a on h.EQUIPMENT_HISTORY_ID = a.REFERENCE_ID where a.ACTION_DATETIME = ? ) ");
                param.add(sgi.getActionDate());
            }
            if(sgi.getStockStatusId()!=null && sgi.getStockStatusId().equals(-1L)){
                s.append(" AND sgis.STOCK_STATUS in(1, 8, 9) ");
            }else if(sgi.getStockStatusId()!=null ){
                s.append(" AND sgis.STOCK_STATUS  = ? ");
                param.add(sgi.getStockStatusId());
            }
            s.append(" ORDER BY grp.EQUIPMENT_GROUP_CODE,  pro.PROFILE_CODE, det.SERIAL ");
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
    public List<EquipmentsDetail> findByProfileAndSerial(Long equipmentsProfileId, String serial) {
        Query query = getSession().createQuery("Select a from EquipmentsDetail a where equipmentsProfileId = ? and serial = ?");
        query.setParameter(0, equipmentsProfileId);
        query.setParameter(1, serial);
        return query.list();
    }
}
