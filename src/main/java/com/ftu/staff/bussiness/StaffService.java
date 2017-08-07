package com.ftu.staff.bussiness;

import com.ftu.hibernate.HibernateUtil;
import java.io.Serializable;
import java.util.List;

import com.ftu.staff.bo.Staff;
import com.ftu.staff.dao.StaffDAO;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StaffService implements Serializable{
	
	public List<Staff> getAllWithParams(Staff st, boolean search,int first,int pageSize, String sortField,Boolean desc) {
            try{
		StaffDAO dao = new StaffDAO();
		return dao.getAllWithParams(st, search,first,pageSize, sortField, desc);
            }catch(Exception ex){
                ex.printStackTrace();
                try {
                    HibernateUtil.closeCurrentSessions();
                } catch (Exception ex1) {
                    Logger.getLogger(StaffService.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            return null;
	}
	
	public void saveOrUpdate(Staff g) {
            try{
		StaffDAO dao = new StaffDAO();
		dao.saveOrUpdate(g);
                HibernateUtil.commitCurrentSessions();
            }catch(Exception ex){
                ex.printStackTrace();
                try {
                    HibernateUtil.closeCurrentSessions();
                } catch (Exception ex1) {
                    Logger.getLogger(StaffService.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
	}
    public void delete(Staff g) {
        try{
            StaffDAO dao = new StaffDAO();
            dao.delete(g);
            HibernateUtil.commitCurrentSessions();
        }catch(Exception ex){
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(StaffService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
	
	public Staff checkExists(String code) {
            try{
		StaffDAO dao = new StaffDAO();
		return dao.checkExists(code);
            }catch(Exception ex){
                ex.printStackTrace();
                try {
                    HibernateUtil.closeCurrentSessions();
                } catch (Exception ex1) {
                    Logger.getLogger(StaffService.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            return null;
	}
	
	public boolean checkUsernameExist(String userName){
            try{
		StaffDAO dao = new StaffDAO();
		return dao.checkUsernameExist(userName);
            }catch(Exception ex){
                ex.printStackTrace();
                try {
                    HibernateUtil.closeCurrentSessions();
                } catch (Exception ex1) {
                    Logger.getLogger(StaffService.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            return true;
	}
	
	public Staff checkUsernameExists(String userName) {
            try{
		StaffDAO dao = new StaffDAO();
		return dao.checkUsernameExists(userName);
            }catch(Exception ex){
                ex.printStackTrace();
                try {
                    HibernateUtil.closeCurrentSessions();
                } catch (Exception ex1) {
                    Logger.getLogger(StaffService.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            return null;
	}

    public List<Staff> findAll(){
        try{
            StaffDAO dao = new StaffDAO();
            return dao.findAll();
        }catch(Exception ex){
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(StaffService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return null;
    }
}
