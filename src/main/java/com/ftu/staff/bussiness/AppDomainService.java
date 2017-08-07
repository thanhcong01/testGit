package com.ftu.staff.bussiness;

import com.ftu.hibernate.HibernateUtil;
import java.io.Serializable;
import java.util.List;

import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.dao.AppDomainDAO;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppDomainService implements Serializable {
	
	public List<ApDomain> findAllByType(String type) {
            try{
		AppDomainDAO dao = new AppDomainDAO();
		return dao.findAllByType(type);
            }catch(Exception ex){
                ex.printStackTrace();
                try {
                    HibernateUtil.closeCurrentSessions();
                } catch (Exception ex1) {
                    Logger.getLogger(AppDomainService.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            return null;
	}
    public List<ApDomain> findAllByTypeAllStatus(String type) {
        try{
            AppDomainDAO dao = new AppDomainDAO();
            return dao.findAllByTypeAllStatus(type);
        }catch(Exception ex){
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(AppDomainService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return null;
    }
    public ApDomain findByTypeAndValue(String type,String code, Long value) {
        try{
            AppDomainDAO dao = new AppDomainDAO();
            return dao.findByTypeAndValue(type, code, value);
        }catch(Exception ex){
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(AppDomainService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return null;
    }
    public ApDomain findByTypeAndValue(String type, Long value) {
        try{
            AppDomainDAO dao = new AppDomainDAO();
            return dao.findByTypeAndValue(type, value);
        }catch(Exception ex){
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(AppDomainService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return null;
    }
	
	public List<ApDomain> getAllDomains(ApDomain domain, boolean search) {
            try{
		AppDomainDAO dao = new AppDomainDAO();
		return dao.getAllDomains(domain, search);
            }catch(Exception ex){
                ex.printStackTrace();
                try {
                    HibernateUtil.closeCurrentSessions();
                } catch (Exception ex1) {
                    Logger.getLogger(AppDomainService.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            return null;
	}
	
	public void saveOrUpdate(ApDomain o) {
            try{
		AppDomainDAO dao = new AppDomainDAO();
		dao.saveOrUpdate(o);
                HibernateUtil.commitCurrentSessions();
            }catch(Exception ex){
                ex.printStackTrace();
                try {
                    HibernateUtil.closeCurrentSessions();
                } catch (Exception ex1) {
                    Logger.getLogger(AppDomainService.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
	}

	
	public void delete(ApDomain o) {
            try{
		AppDomainDAO dao = new AppDomainDAO();
		dao.delete(o);
                HibernateUtil.commitCurrentSessions();
            }catch(Exception ex){
                ex.printStackTrace();
                try {
                    HibernateUtil.closeCurrentSessions();
                } catch (Exception ex1) {
                    Logger.getLogger(AppDomainService.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
	}
	
	
}
