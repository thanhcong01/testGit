package com.ftu.java.business;

import com.ftu.hibernate.HibernateUtil;
import java.io.Serializable;
import java.util.List;

import com.ftu.inventory.bo.EquipmentsProfile;
import com.ftu.inventory.dao.EquipmentsProfileDAO;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GoodService implements Serializable {
	public void saveOrUpdate(EquipmentsProfile g) {
            try{
		EquipmentsProfileDAO dao = new EquipmentsProfileDAO();
		dao.saveOrUpdate(g);
                HibernateUtil.commitCurrentSessions();
            }catch(Exception ex){
                ex.printStackTrace();
                try {
                    HibernateUtil.closeCurrentSessions();
                } catch (Exception ex1) {
                    Logger.getLogger(GoodService.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
	}
	
	public void delete(EquipmentsProfile g) {
            try{
		EquipmentsProfileDAO dao = new EquipmentsProfileDAO();
		dao.delete(g);
                HibernateUtil.commitCurrentSessions();
            }catch(Exception ex){
                ex.printStackTrace();
                try {
                    HibernateUtil.closeCurrentSessions();
                } catch (Exception ex1) {
                    Logger.getLogger(GoodService.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
	}
	
	public List<EquipmentsProfile> getAllGoods(EquipmentsProfile g, boolean search,int first,int pageSize) {
            try{
		EquipmentsProfileDAO dao = new EquipmentsProfileDAO();
		return dao.getAllProfiles(g, search, first, pageSize);
            }catch(Exception ex){
                ex.printStackTrace();
                try {
                    HibernateUtil.closeCurrentSessions();
                } catch (Exception ex1) {
                    Logger.getLogger(GoodService.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            return null;
	}
	
	public EquipmentsProfile checkExists(String code) {
            try{
		EquipmentsProfileDAO dao = new EquipmentsProfileDAO();
		return dao.checkExists(code);
            }catch(Exception ex){
                ex.printStackTrace();
                try {
                    HibernateUtil.closeCurrentSessions();
                } catch (Exception ex1) {
                    Logger.getLogger(GoodService.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            return null;
	}
}
