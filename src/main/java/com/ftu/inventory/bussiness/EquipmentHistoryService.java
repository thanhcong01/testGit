package com.ftu.inventory.bussiness;

import com.ftu.hibernate.HibernateUtil;
import com.ftu.inventory.bo.EquipmentHistory;
import com.ftu.inventory.dao.EquipmentHistoryDAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EquipmentHistoryService implements Serializable {

    public List<EquipmentHistory> getAll() {
        try {
            EquipmentHistoryDAO dao = new EquipmentHistoryDAO();
            return dao.findAll();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(EquipmentHistoryService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return new ArrayList<>();
    }
    public void saveOrUpdate(EquipmentHistory obj) {
        try {
            EquipmentHistoryDAO dao = new EquipmentHistoryDAO();
            dao.saveOrUpdate(obj);
            HibernateUtil.commitCurrentSessions();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(EquipmentHistoryService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    public void delete(EquipmentHistory obj) {
        try {
            EquipmentHistoryDAO dao = new EquipmentHistoryDAO();
            dao.delete(obj);
            HibernateUtil.commitCurrentSessions();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(EquipmentHistoryService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    public List<EquipmentHistory> searchByEquipmentId(EquipmentHistory equipmentHistory, Integer start, Integer get) {
        try {
            EquipmentHistoryDAO dao = new EquipmentHistoryDAO();
            return dao.searchByEquipmentId( equipmentHistory, start, get);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(EquipmentHistoryService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return null;
    }
    public EquipmentHistory findById(Long id) {
        try {
            EquipmentHistoryDAO dao = new EquipmentHistoryDAO();
            return dao.findById( id);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(EquipmentHistoryService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return null;
    }
    public EquipmentHistory findByEquipmentId(Long id) {
        try {
            EquipmentHistoryDAO dao = new EquipmentHistoryDAO();
            return dao.findByEquipmentId( id);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(EquipmentHistoryService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return null;
    }

}
