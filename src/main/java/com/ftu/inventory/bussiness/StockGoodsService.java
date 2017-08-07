package com.ftu.inventory.bussiness;

import com.ftu.hibernate.HibernateUtil;
import com.ftu.inventory.bo.StockGoods;
import com.ftu.inventory.dao.StockGoodsDAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StockGoodsService implements Serializable {

    public List<StockGoods> getAll() {
        try {
            StockGoodsDAO dao = new StockGoodsDAO();
            return dao.findAll();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(StockGoodsService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return new ArrayList<>();
    }
    public void saveOrUpdate(StockGoods obj) {
        try {
            StockGoodsDAO dao = new StockGoodsDAO();
            dao.saveOrUpdate(obj);
            HibernateUtil.commitCurrentSessions();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(StockGoodsService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    public void delete(StockGoods obj) {
        try {
            StockGoodsDAO dao = new StockGoodsDAO();
            dao.delete(obj);
            HibernateUtil.commitCurrentSessions();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(StockGoodsService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
    public StockGoods findById(Long id ){
        try {
            StockGoodsDAO dao = new StockGoodsDAO();
            return dao.findById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(StockGoodsService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return null;
    }

    public StockGoods findByEquipmentId(Long id ){
        try {
            StockGoodsDAO dao = new StockGoodsDAO();
            return dao.findByEquipmentId(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(StockGoodsService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return null;
    }
    public StockGoods getStockGoods(Long goodsId, Long status, Long shopId) {
        try {
            StockGoodsDAO dao = new StockGoodsDAO();
            return dao.getStockGoods(goodsId, status, shopId);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(StockGoodsService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return null;
    }
}
