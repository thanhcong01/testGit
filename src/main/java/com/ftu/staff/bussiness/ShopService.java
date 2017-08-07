package com.ftu.staff.bussiness;

import com.ftu.hibernate.HibernateUtil;
import java.io.Serializable;
import java.util.List;

import com.ftu.staff.bo.Shop;
import com.ftu.staff.dao.ShopDAO;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShopService implements Serializable {
	public List<Shop> findAllShopTree(Long parentId) {
            try{
		ShopDAO dao = new ShopDAO();
		return dao.getTreeShop(parentId);
            }catch(Exception ex){
                ex.printStackTrace();
                try {
                    HibernateUtil.closeCurrentSessions();
                } catch (Exception ex1) {
                    Logger.getLogger(ShopService.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            return null;
	}
    public List<Shop> getAllShopChildrent(Long parentId) {
        try{
            ShopDAO dao = new ShopDAO();
            return dao.getAllShopChildrent(parentId);
        }catch(Exception ex){
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(ShopService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return null;
    }
    public List<Shop> getAllShopChildrentAllStatus(Long parentId) {
        try{
            ShopDAO dao = new ShopDAO();
            return dao.getAllShopChildrentAllStatus(parentId);
        }catch(Exception ex){
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(ShopService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return null;
    }
	public List<Shop> findByParentId(Long id) {
            try{
		ShopDAO dao = new ShopDAO();
		return dao.findByParentId(id);
            }catch(Exception ex){
                ex.printStackTrace();
                try {
                    HibernateUtil.closeCurrentSessions();
                } catch (Exception ex1) {
                    Logger.getLogger(ShopService.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            return null;
	}

	public void saveOrUpdate(Shop g) {
            try{
		ShopDAO dao = new ShopDAO();
		dao.saveOrUpdate(g);
                HibernateUtil.commitCurrentSessions();
            }catch(Exception ex){
                ex.printStackTrace();
                try {
                    HibernateUtil.closeCurrentSessions();
                } catch (Exception ex1) {
                    Logger.getLogger(ShopService.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            
	}
	
	public void delete(Shop g) {
            try{
		ShopDAO dao = new ShopDAO();
		dao.delete(g);
                HibernateUtil.commitCurrentSessions();
            }catch(Exception ex){
                ex.printStackTrace();
                try {
                    HibernateUtil.closeCurrentSessions();
                } catch (Exception ex1) {
                    Logger.getLogger(ShopService.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
	}
	
	public List<Shop> getAllShopParrent(){
            try{
		ShopDAO dao = new ShopDAO();
		return dao.getAllShopParrent();
            }catch(Exception ex){
                ex.printStackTrace();
                try {
                    HibernateUtil.closeCurrentSessions();
                } catch (Exception ex1) {
                    Logger.getLogger(ShopService.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            return null;
	}
    public List<Shop> getListShopByParentId(Shop s, boolean search,int first,int pageSize){
        try{
            ShopDAO dao = new ShopDAO();
            return dao.getListShopByParentId(s, search, first, pageSize, null, null);
        }catch(Exception ex){
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(ShopService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return null;
    }
    public Shop findById(Long id){
        try{
            ShopDAO dao = new ShopDAO();
            return dao.findById(id);
        }catch(Exception ex){
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(ShopService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return null;
    }

    public List<Shop> getAllShop(){
        try{
            ShopDAO dao = new ShopDAO();
            return dao.findAllShop();
        }catch(Exception ex){
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(ShopService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return null;
    }

    public boolean checkExistShopCode(String shopCode,Long shopId){
        ShopDAO shopDAO = new ShopDAO();

        return shopDAO.findByShopCode(shopCode,shopId) != null;

    }
    public boolean checkExistShopCodeAllStatus(String shopCode,Long shopId){
        ShopDAO shopDAO = new ShopDAO();

        return shopDAO.findByShopCodeAllStatus(shopCode,shopId) != null;

    }

    public List<Shop> getAllShopPosition(Long parentId) {
        try{
            ShopDAO dao = new ShopDAO();
            return dao.getAllShopPosition(parentId);
        }catch(Exception ex){
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(ShopService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return null;
    }

}
