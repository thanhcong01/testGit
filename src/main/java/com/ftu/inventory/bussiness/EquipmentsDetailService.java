package com.ftu.inventory.bussiness;

import com.ftu.hibernate.HibernateUtil;
import com.ftu.inventory.bo.EquipmentsDetail;
import com.ftu.inventory.dao.EquipmentDetailDAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EquipmentsDetailService implements Serializable {

    public List<EquipmentsDetail> getAll() {
        try {
            EquipmentDetailDAO dao = new EquipmentDetailDAO();
            return dao.findAll();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(EquipmentsDetailService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return new ArrayList<>();
    }
    public void saveOrUpdate(EquipmentsDetail obj) {
        try {
            EquipmentDetailDAO dao = new EquipmentDetailDAO();
            dao.saveOrUpdate(obj);
            HibernateUtil.commitCurrentSessions();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(EquipmentsDetailService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    public void delete(EquipmentsDetail obj) {
        try {
            EquipmentDetailDAO dao = new EquipmentDetailDAO();
            dao.delete(obj);
            HibernateUtil.commitCurrentSessions();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(EquipmentsDetailService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
    public EquipmentsDetail findById(Long id ){
        try {
            EquipmentDetailDAO dao = new EquipmentDetailDAO();
            return dao.findById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(EquipmentsDetailService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return null;
    }
    public EquipmentsDetail findBySerial(String serial ){
        try {
            EquipmentDetailDAO dao = new EquipmentDetailDAO();
            return dao.findBySerial(serial);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(EquipmentsDetailService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return null;
    }
    public Integer countPosition(Long positionId ){
        try {
            EquipmentDetailDAO dao = new EquipmentDetailDAO();
            return dao.countPosition(positionId);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(EquipmentsDetailService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return null;
    }
    public Long getStatusBySerial(String serial ){
        try {
            EquipmentDetailDAO dao = new EquipmentDetailDAO();
            return dao.getStatusBySerial(serial);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(EquipmentsDetailService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return -1L;
    }
    public EquipmentsDetail getBySerial(String serial, Long taId){
        try {
            EquipmentDetailDAO dao = new EquipmentDetailDAO();
            return dao.getBySerial( taId, serial);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(EquipmentsDetailService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return null;
    }
    public EquipmentsDetail findBySerialAndProviderIdEquip(String serial, Long prvId , Long equipId){
        try {
            EquipmentDetailDAO dao = new EquipmentDetailDAO();
            return dao.findBySerialAndProviderIdEquip(serial, prvId, equipId);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(EquipmentsDetailService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return null;
    }
    public void updateWaranStatus(List<Long> lstEquipmentId, List<Long> lstProfileId, Long waranStatus){
        try {
            EquipmentDetailDAO dao = new EquipmentDetailDAO();
             dao.updateWaranStatus(lstEquipmentId, lstProfileId, waranStatus);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(EquipmentsDetailService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
    }
    public List<EquipmentsDetail> findByProfile(Long profileId ){
        try {
            EquipmentDetailDAO dao = new EquipmentDetailDAO();
            return dao.findByProfile(profileId);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(EquipmentsDetailService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return null;
    }
    public List<EquipmentsDetail> findByProfileAndSerial(Long profileId, String serial ){
        try {
            EquipmentDetailDAO dao = new EquipmentDetailDAO();
            return dao.findByProfileAndSerial(profileId, serial);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(EquipmentsDetailService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return null;
    }
}
