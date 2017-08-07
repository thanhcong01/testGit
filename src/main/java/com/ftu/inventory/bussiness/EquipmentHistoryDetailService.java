package com.ftu.inventory.bussiness;

import com.ftu.hibernate.HibernateUtil;
import com.ftu.inventory.bo.EquipmentHistoryDetail;
import com.ftu.inventory.bo.EquipmentHistoryDetail;
import com.ftu.inventory.dao.EquipmentHistoryDetailDAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EquipmentHistoryDetailService implements Serializable {

    public List<EquipmentHistoryDetail> getAll() {
        try {
            EquipmentHistoryDetailDAO dao = new EquipmentHistoryDetailDAO();
            return dao.findAll();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(EquipmentHistoryDetailService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return new ArrayList<>();
    }
    public void saveOrUpdate(EquipmentHistoryDetail obj) {
        try {
            EquipmentHistoryDetailDAO dao = new EquipmentHistoryDetailDAO();
            dao.saveOrUpdate(obj);
            HibernateUtil.commitCurrentSessions();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(EquipmentHistoryDetailService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    public void saveOrUpdate(List<EquipmentHistoryDetail> objs) {
        try {
            EquipmentHistoryDetailDAO dao = new EquipmentHistoryDetailDAO();
            dao.saveOrUpdate(objs);
            HibernateUtil.commitCurrentSessions();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(EquipmentHistoryDetailService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    public void delete(EquipmentHistoryDetail obj) {
        try {
            EquipmentHistoryDetailDAO dao = new EquipmentHistoryDetailDAO();
            dao.delete(obj);
            HibernateUtil.commitCurrentSessions();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(EquipmentHistoryDetailService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    public void deleteByEquipmentHistoryId(Long equipmentHistoryId) {
        try {
            EquipmentHistoryDetailDAO dao = new EquipmentHistoryDetailDAO();
            dao.deleteByEquipmentHistoryId(equipmentHistoryId);
            HibernateUtil.commitCurrentSessions();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(EquipmentHistoryDetailService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    public EquipmentHistoryDetail findById(Long id ){
        try {
            EquipmentHistoryDetailDAO dao = new EquipmentHistoryDetailDAO();
            return dao.findById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(EquipmentHistoryDetailService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return null;
    }

    public List<EquipmentHistoryDetail> findByEquipmentHistoryId(Long id ){
        try {
            EquipmentHistoryDetailDAO dao = new EquipmentHistoryDetailDAO();
            return dao.findByEquipmentHistoryId(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(EquipmentHistoryDetailService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return null;
    }

    public String getSerialOldBySerialNew(String newValue, String feildName) {
        EquipmentHistoryDetailDAO appDAO = new EquipmentHistoryDetailDAO();
        return appDAO.getSerialOldBySerialNew(newValue, feildName);
    }

    public EquipmentHistoryDetail getDetailBySerialOld(String oldValue, String feildName) {
        EquipmentHistoryDetailDAO appDAO = new EquipmentHistoryDetailDAO();
        return appDAO.getDetailBySerialOld(oldValue, feildName);
    }
    public void deleteHisDetailSerial(String newValue, String oldValue, String feildName) {
        EquipmentHistoryDetailDAO appDAO = new EquipmentHistoryDetailDAO();
        appDAO.deleteHisDetailSerial(oldValue, feildName);
    }
}
