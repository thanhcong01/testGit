package com.ftu.java.business;

import com.ftu.hibernate.HibernateUtil;
import java.io.Serializable;
import java.util.List;

import com.ftu.inventory.bo.Provider;
import com.ftu.inventory.dao.ProviderDAO;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProviderService implements Serializable {
	public List<Provider> getAllProviders() {
            try{
		ProviderDAO dao = new ProviderDAO();
		return dao.getAllProviders();
            }catch(Exception ex){
                ex.printStackTrace();
                try {
                    HibernateUtil.closeCurrentSessions();
                } catch (Exception ex1) {
                    Logger.getLogger(ProviderService.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            return null;
	}
	
	public void delete(Provider g) {
            try{
		ProviderDAO dao = new ProviderDAO();
		dao.delete(g);
                HibernateUtil.commitCurrentSessions();
            }catch(Exception ex){
                ex.printStackTrace();
                try {
                    HibernateUtil.closeCurrentSessions();
                } catch (Exception ex1) {
                    Logger.getLogger(ProviderService.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
	}
	
	public void saveOrUpdate(Provider g) {
            try{
		ProviderDAO dao = new ProviderDAO();
		dao.saveOrUpdate(g);
                HibernateUtil.commitCurrentSessions();
            }catch(Exception ex){
                ex.printStackTrace();
                try {
                    HibernateUtil.closeCurrentSessions();
                } catch (Exception ex1) {
                    Logger.getLogger(ProviderService.class.getName()).log(Level.SEVERE, null, ex1);
                }
                
            }
            
	}
	
	public Provider checkExists(String c) {
            try{
		ProviderDAO dao = new ProviderDAO();
		return dao.checkExists(c);
            }catch(Exception ex){
                ex.printStackTrace();
                try {
                    HibernateUtil.closeCurrentSessions();
                } catch (Exception ex1) {
                    Logger.getLogger(ProviderService.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            return null;
	}
    public Provider findById(Long c) {
        try{
            ProviderDAO dao = new ProviderDAO();
            return dao.findById(c);
        }catch(Exception ex){
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(ProviderService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return null;
    }

    public Provider findByProviderCode(String code) {
        try{
            ProviderDAO dao = new ProviderDAO();
            return dao.findByProviderCode(code);
        }catch(Exception ex){
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(ProviderService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return null;
    }
	
	public List<Provider> getAllProviders(Provider p,boolean search) {
            try{
		ProviderDAO dao = new ProviderDAO();
		return dao.getAllProviders(p,search);
            }catch(Exception ex){
                ex.printStackTrace();
                try {
                    HibernateUtil.closeCurrentSessions();
                } catch (Exception ex1) {
                    Logger.getLogger(ProviderService.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            return null;
	}
}
