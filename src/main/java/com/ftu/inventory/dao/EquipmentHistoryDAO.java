/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.dao;

import com.ftu.hibernate.GenericDAOHibernate;
import com.ftu.inventory.bo.EquipmentHistory;
import com.ftu.inventory.bo.EquipmentsDetail;
import org.hibernate.Query;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author E5420
 */
public class EquipmentHistoryDAO extends GenericDAOHibernate<EquipmentHistory, Serializable> {

    public EquipmentHistoryDAO() {
        super(EquipmentHistory.class);
    }

    @Override
    public void saveOrUpdate(EquipmentHistory a) {
        getSession().saveOrUpdate(a);
        getSession().flush();
    }

    public void save(EquipmentHistory a) {
        getSession().save(a);
        getSession().flush();
    }

    public List<EquipmentHistory> searchByEquipmentId(EquipmentHistory equipmentHistory, Integer start, Integer get) {
        try{
            List param = new ArrayList();
            StringBuilder s = new StringBuilder("SELECT   his.EQUIPMENT_HISTORY_ID,\n" +
                    "  his.EQUIPMENT_ID,\n" +
                    "  his.CREATED_DATETIME,\n" +
                    "  his.STAFF_ID,\n" +
                    "  his.SHOP_ID,\n" +
                    "  his.REFERENCE_ID,\n" +
                    "  his.DOCUMENT,\n" +
                    "  ac.ACTION_AUDIT_ID,\n" +
                    "  ac.ACTION_TYPE,\n" +
                    "  ac.ACTION_DATETIME  ,\n" +
                    "  his.NOTE,\n" +
                    "  s.SHOP_NAME, s.SHOP_CODE,\n" +
                    "  p.POSITION_CODE, p.LANE_CODE, p.POSITION_NAME, det.POSITION_ID , p.SHOP_ID shopIdHis\n" +
                    "  FROM EQUIPMENT_HISTORY  his LEFT JOIN ACTION_AUDIT ac " +
                    " ON his.EQUIPMENT_HISTORY_ID = ac.REFERENCE_ID \n" +
                    "  LEFT JOIN SHOP s ON his.SHOP_ID = s.SHOP_ID\n" +
                    "  JOIN EQUIPMENT_DETAIL det on his.EQUIPMENT_ID = det.EQUIPMENT_ID\n" +
                    "  LEFT JOIN Position p ON det.POSITION_ID = p.POSITION_ID  WHERE 1=1");

            if (equipmentHistory.getEquipmentId() != null ) {
                s.append(" and his.EQUIPMENT_ID = ? ");
                param.add(equipmentHistory.getEquipmentId());
            }
            if (equipmentHistory.getShopId() != null && equipmentHistory.getShopId() > 0L) {
                if(equipmentHistory.isNotSerial()){
                    s.append(" and his.SHOP_ID = ? ");
                }else {
                    s.append(" and his.SHOP_ID IN(select b.SHOP_ID from Shop b  " +
                            "                          start with b.SHOP_ID = ? \n" +
                            "                          connect by prior b.SHOP_ID = b.SHOP_PARENT_ID )");
                }
                param.add(equipmentHistory.getShopId());
            }
            s.append(" ORDER BY his.CREATED_DATETIME DESC, his.EQUIPMENT_HISTORY_ID DESC ");
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
            List<EquipmentHistory> rs = new ArrayList<EquipmentHistory>();
            List<Object[]> a = q.list();
            for (Object[] o : a) {
                rs.add(convertObjectToSerial(o));
            }
            return rs;
        }catch(Exception ex)
        {
            return new ArrayList<EquipmentHistory>();
        }
    }
    public EquipmentHistory convertObjectToSerial(Object[] o) {
        EquipmentHistory rs = new EquipmentHistory();
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            rs.setEquipmentHistoryId(o[0] == null ? null : Long.parseLong(o[0].toString()));
            rs.setEquipmentId(o[1] == null ? null : Long.parseLong(o[1].toString()));
            rs.setCreatedDatetime(o[2] == null ? null : (Date) o[2]);
            rs.setStaffId(o[3] == null ? null : Long.parseLong(o[3].toString()));
            rs.setShopId(o[4] == null ? null : Long.parseLong(o[4].toString()));
            rs.setReferenceId(o[5] == null ? null : o[5].toString());
            rs.setDocument(o[6] == null ? null : o[6].toString());
            rs.setActionAuditId(o[7] == null ? null : Long.parseLong(o[7].toString()));
            rs.setActionType(o[8] == null ? null : Long.parseLong(o[8].toString()));
            rs.setActionDate(o[9] == null ? null : (Date) o[9]);
            rs.setNote(o[10] == null ? null : o[10].toString());
            rs.setShopName(o[11] == null ? null : o[11].toString());
            rs.setShopCode(o[12] == null ? null : o[12].toString());
            rs.setPositionCode(o[13] == null ? null : o[13].toString());
            rs.setLanCode(o[14] == null ? null : o[14].toString());
            rs.setPositionName(o[15] == null ? null : o[15].toString());
            rs.setPositionId(o[16] == null ? null : Long.parseLong(o[16].toString()));
            rs.setShopIdHis(o[17] == null ? null : Long.parseLong(o[17].toString()));
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return rs;
    }


    public Long searchSize(EquipmentHistory equipmentHistory) {
        try {
            List param = new ArrayList();
            StringBuilder s = new StringBuilder(" SELECT  COUNT(1) " +
                    "FROM EQUIPMENT_HISTORY  his LEFT JOIN ACTION_AUDIT ac " +
                    " ON his.EQUIPMENT_HISTORY_ID = ac.REFERENCE_ID \n" +
                            "  JOIN SHOP s ON his.SHOP_ID = s.SHOP_ID\n" +
                            "  JOIN EQUIPMENT_DETAIL det on his.EQUIPMENT_ID = det.EQUIPMENT_ID\n" +
                            "  LEFT JOIN Position p ON det.POSITION_ID = p.POSITION_ID  WHERE 1=1");

            if (equipmentHistory.getEquipmentId() != null && equipmentHistory.getEquipmentId() > 0) {
                s.append(" and his.EQUIPMENT_ID = ? ");
                param.add(equipmentHistory.getEquipmentId());
            }
            Query q = getSession().createSQLQuery(s.toString());
            for (int i = 0; i < param.size(); i++) {
                q.setParameter(i, param.get(i));
            }
            return Long.parseLong(q.uniqueResult().toString());
        }catch (Exception e)
        {
            e.printStackTrace();
            return 0L;
        }

    }


    public void deleteByEquipmentId(Long equipmentId) {
        Query q4 = getSession().createQuery("Delete from EquipmentHistory where equipmentId = ? ");
        q4.setParameter(0, equipmentId);
        q4.executeUpdate();
        getSession().flush();
    }


    public EquipmentHistory findById(Long id) {
        Query q = getSession().createQuery("Select p from EquipmentHistory p where p.equipmentHistoryId = :id");
        q.setParameter("id", id);
        if (q.list().isEmpty()) {
            return null;
        } else {
            return (EquipmentHistory) q.list().get(0);
        }
    }
    public EquipmentHistory findByEquipmentId(Long id) {
        Query q = getSession().createQuery("Select p from EquipmentHistory p where p.equipmentId = :id order by p.equipmentHistoryId DESC");
        q.setParameter("id", id);
        if (q.list().isEmpty()) {
            return null;
        } else {
            return (EquipmentHistory) q.list().get(0);
        }
    }
}
