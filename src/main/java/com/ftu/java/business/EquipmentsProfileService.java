package com.ftu.java.business;

import com.ftu.hibernate.HibernateUtil;
import java.io.Serializable;
import java.util.List;

import com.ftu.inventory.bo.EquipmentsProfile;
import com.ftu.inventory.dao.EquipmentsProfileDAO;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EquipmentsProfileService implements Serializable {
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
                    Logger.getLogger(EquipmentsProfileService.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
	}
	
	public void delete(EquipmentsProfile g) throws Exception{
            try{
		EquipmentsProfileDAO dao = new EquipmentsProfileDAO();
		dao.delete(g);
                HibernateUtil.commitCurrentSessions();
            }catch(Exception ex){
                ex.printStackTrace();
                try {
                    HibernateUtil.closeCurrentSessions();
                } catch (Exception ex1) {
                    Logger.getLogger(EquipmentsProfileService.class.getName()).log(Level.SEVERE, null, ex1);
                }
                throw ex;
            }
	}
	
	public List<EquipmentsProfile> getAllProfile(EquipmentsProfile g, boolean search,int first,int pageSize) {
            try{
		EquipmentsProfileDAO dao = new EquipmentsProfileDAO();
		return dao.getAllProfiles(g, search, first, pageSize);
            }catch(Exception ex){
                ex.printStackTrace();
                try {
                    HibernateUtil.closeCurrentSessions();
                } catch (Exception ex1) {
                    Logger.getLogger(EquipmentsProfileService.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            return null;
	}


    public List<EquipmentsProfile> findByGroupId(Long groupId) {
        try{
            EquipmentsProfileDAO dao = new EquipmentsProfileDAO();
            return dao.findByGroupId(groupId);
        }catch(Exception ex){
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(EquipmentsProfileService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return null;
    }

    public EquipmentsProfile findByProfileId(Long profileId) {
        try{
            EquipmentsProfileDAO dao = new EquipmentsProfileDAO();
            return dao.findByProfileId(profileId);
        }catch(Exception ex){
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(EquipmentsProfileService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return null;
    }

	public Integer getSizeProfilesByGroup(List<Long> objs){
        try{
            EquipmentsProfileDAO dao = new EquipmentsProfileDAO();
            return dao.getSizeProfilesByGroup(objs);
        }catch(Exception ex){
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(EquipmentsProfileService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return 0;
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
                    Logger.getLogger(EquipmentsProfileService.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            return null;
	}
}
