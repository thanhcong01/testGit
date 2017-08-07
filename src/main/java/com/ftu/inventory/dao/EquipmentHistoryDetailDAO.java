/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.dao;

import com.ftu.hibernate.GenericDAOHibernate;
import com.ftu.inventory.bo.EquipmentHistoryDetail;
import com.ftu.inventory.bo.EquipmentHistoryDetail;
import org.hibernate.Query;

import java.util.List;

/**
 *
 * @author E5420
 */
public class EquipmentHistoryDetailDAO extends GenericDAOHibernate<EquipmentHistoryDetail, Long> {

    public EquipmentHistoryDetailDAO() {
        super(EquipmentHistoryDetail.class);
    }

    @Override
    public void saveOrUpdate(EquipmentHistoryDetail g) {
        if (g != null) {
            getSession().merge(g);
        }
        getSession().flush();
    }
    public void saveOrUpdate(List<EquipmentHistoryDetail> lst) {
        int count = 0;
        for (EquipmentHistoryDetail obj : lst) {
            count++;
            if (obj != null) {
                getSession().merge(obj);
            }
            if ((count % 500) == 0) {
                getSession().flush();
            }
        }
        getSession().flush();
    }

    @Override
    public EquipmentHistoryDetail findById(Long id) {
        Query q = getSession().createQuery("Select p from EquipmentHistoryDetail p where p.equipmentHistoryDetailId = :id");
        q.setParameter("id", id);
        if (q.list().isEmpty()) {
            return null;
        } else {
            return (EquipmentHistoryDetail) q.list().get(0);
        }
    }

    public String getSerialOldBySerialNew(String newValue, String feildName) {
        Query q = getSession().createQuery("Select p from EquipmentHistoryDetail p " +
                " where p.feildName = :feildName and p.newValue = :newValue");
        q.setParameter("feildName", feildName);
        q.setParameter("newValue", newValue);
        if (q.list().isEmpty()) {
            return null;
        } else {
            return ((EquipmentHistoryDetail) q.list().get(0)).getOldValue();
        }
    }

    public EquipmentHistoryDetail getDetailBySerialOld(String oldValue, String feildName) {
        Query q = getSession().createQuery("Select p from EquipmentHistoryDetail p " +
                " where p.feildName = :feildName and p.oldValue = :oldValue");
        q.setParameter("feildName", feildName);
        q.setParameter("oldValue", oldValue);
        if (q.list().isEmpty()) {
            return null;
        } else {
            return ((EquipmentHistoryDetail) q.list().get(0));
        }
    }

    public void deleteHisDetailSerial(String oldValue , String feildName){
        Query q = getSession().createQuery("Delete from EquipmentHistoryDetail p" +
                " where p.feildName = :feildName  and p.oldValue = :oldValue ") ;
//                " and p.newValue = :newValue ");
        q.setParameter("feildName", feildName);
        q.setParameter("oldValue", oldValue);
//        q.setParameter("oldValue", oldValue);
        q.executeUpdate();
    }
    public void deleteHisDetailNewSerial(String newValue , String feildName){

        Query q1 = getSession().createQuery("Delete from EquipmentHistory e " +
                " where e.equipmentHistoryId IN(select p.equipmentHistoryId from EquipmentHistoryDetail p " +
                "       where p.feildName = :feildName  and p.newValue = :newValue )") ;
        q1.setParameter("feildName", feildName);
        q1.setParameter("newValue", newValue);
        q1.executeUpdate();

        Query q2 = getSession().createQuery("Delete from EquipmentHistoryDetail p" +
                " where p.feildName = :feildName  and p.newValue = :newValue ") ;
        q2.setParameter("feildName", feildName);
        q2.setParameter("newValue", newValue);
        q2.executeUpdate();
    }
    public List<EquipmentHistoryDetail> findByEquipmentHistoryId(Long id) {
        Query q = getSession().createQuery("Select p from EquipmentHistoryDetail p where p.equipmentHistoryId = :id order by feildName ASC ");
        q.setParameter("id", id);
        List<EquipmentHistoryDetail> lst = q.list();

        return lst;
    }

    public List<EquipmentHistoryDetail> getAllEquipmentHistoryDetails() {
        Query q = getSession().getNamedQuery("EquipmentHistoryDetail.findAll");
        if (q.list().isEmpty()) {
            return null;
        } else {
            return q.list();
        }

    }

    public List<EquipmentHistoryDetail> getEquipmentHistoryDetailsByStatus(String status) {
        Query q = getSession().createQuery("Select p from EquipmentHistoryDetail p where status = ?");
        q.setParameter(0, status);
        if (q.list().isEmpty()) {
            return null;
        } else {
            return q.list();
        }
    }
    public void deleteByEquipmentHistoryId(Long equipmentHistoryId) {
        Query q = getSession().createQuery("delete from EquipmentHistoryDetail  where equipmentHistoryId = ?");
        q.setParameter(0, equipmentHistoryId);
        q.executeUpdate();
        getSession().flush();
    }

    @Override
    public void delete(EquipmentHistoryDetail g) {
        getSession().delete(g);
        getSession().flush();
    }

}
