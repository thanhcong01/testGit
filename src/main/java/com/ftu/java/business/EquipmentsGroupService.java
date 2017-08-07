package com.ftu.java.business;

import com.ftu.hibernate.HibernateUtil;
import java.io.Serializable;
import java.util.List;

import com.ftu.inventory.bo.EquipmentsGroup;
import com.ftu.inventory.dao.EquipmentsGroupDAO;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EquipmentsGroupService implements Serializable {
	public List<EquipmentsGroup> getAllEquipmentsGroup(EquipmentsGroup g, boolean search,int first,int pageSize) {
            try{
		EquipmentsGroupDAO dao = new EquipmentsGroupDAO();
		return dao.getAllEquipmentsGroup(g, search, first, pageSize);
            }catch(Exception ex){
                ex.printStackTrace();
                try {
                    HibernateUtil.closeCurrentSessions();
                } catch (Exception ex1) {
                    Logger.getLogger(EquipmentsGroupService.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            return null;
	}
	
	public void delete(EquipmentsGroup g) {
            try{
		EquipmentsGroupDAO dao = new EquipmentsGroupDAO();
		dao.delete(g);
                HibernateUtil.commitCurrentSessions();
            }catch(Exception ex){
                ex.printStackTrace();
                try {
                    HibernateUtil.closeCurrentSessions();
                } catch (Exception ex1) {
                    Logger.getLogger(EquipmentsGroupService.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
	}
	
	public void saveOrUpdate(EquipmentsGroup g) {
            try{
		EquipmentsGroupDAO dao = new EquipmentsGroupDAO();
		dao.saveOrUpdate(g);
                HibernateUtil.commitCurrentSessions();
            }catch(Exception ex){
                ex.printStackTrace();
                try {
                    HibernateUtil.closeCurrentSessions();
                } catch (Exception ex1) {
                    Logger.getLogger(EquipmentsGroupService.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
	}
	
	public EquipmentsGroup checkExists(String code) {
            try{
		EquipmentsGroupDAO dao = new EquipmentsGroupDAO();
		return dao.checkExists(code);
            }catch(Exception ex){
                ex.printStackTrace();
                try {
                    HibernateUtil.closeCurrentSessions();
                } catch (Exception ex1) {
                    Logger.getLogger(EquipmentsGroupService.class.getName()).log(Level.SEVERE, null, ex1);
                }
                
            }
            return null;
	}
    public Long checkGroupIsSerial(Long groupId) {
        try{
            EquipmentsGroupDAO dao = new EquipmentsGroupDAO();
            return dao.checkGroupIsSerial(groupId);
        }catch(Exception ex){
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(EquipmentsGroupService.class.getName()).log(Level.SEVERE, null, ex1);
            }

        }
        return null;
    }
}
