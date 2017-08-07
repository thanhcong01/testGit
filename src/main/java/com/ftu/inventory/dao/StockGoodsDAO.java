/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.dao;

import com.ftu.inventory.bo.StockGoods;
import com.ftu.constanst.InventoryConstanst;
import com.ftu.hibernate.GenericDAOHibernate;

import java.util.ArrayList;
import java.util.List;

import org.docx4j.model.datastorage.XPathEnhancerParser.main_return;
import org.hibernate.Query;

/**
 *
 * @author E5420
 */
public class StockGoodsDAO extends GenericDAOHibernate<StockGoods, Long> {

    public StockGoodsDAO() {
        super(StockGoods.class);
    }

    @Override
    public void saveOrUpdate(StockGoods g) {
        if (g != null) {
            super.saveOrUpdate(g);
        }
        getSession().flush();
    }

    @Override
    public void delete(StockGoods g) {
        super.delete(g);
        getSession().flush();
    }

    public List<StockGoods> getByShopId(Long sId) {
        Query query = getSession().createQuery("Select s from StockGoods s where shopId = ? order by goodsId ");
        query.setParameter(0, sId);
        if (query.list().isEmpty()) {
            return null;
        } else {
            return query.list();
        }
    }
    public List<StockGoods> getByShopIds(List<Long> ids) {
        Query query = getSession().createQuery("Select s from StockGoods s where shopId in ? order by goodsId ");
        query.setParameter(0, ids);
        if (query.list().isEmpty()) {
            return null;
        } else {
            return query.list();
        }
    }
    public StockGoods findByEquipmentId(Long eId) {
        Query query = getSession().createQuery("Select s from StockGoods s where goodsId = ? order by goodsId ");
        query.setParameter(0, eId);
        if (query.list().isEmpty()) {
            return null;
        } else {
            return (StockGoods)query.list().get(0);
        }
    }
   
    public List<StockGoods> searchStockGoods(Long sId, Long goodsId, Long goodsGroupId, Long stockGoodsStatus){
    	StringBuilder strBuilder = new StringBuilder("Select s from StockGoods s ");
         List params = new ArrayList<>();
         
         if(sId != null && sId != 0){
        	 strBuilder.append(" where s.shopId = ? ");
        	 params.add(sId);
         }
    	 
         if(stockGoodsStatus != null && stockGoodsStatus != 0){
        	 strBuilder.append(" and s.goodsStatus = ? ");
        	 params.add(stockGoodsStatus);
         }
         
         
         
         if(goodsGroupId != null && goodsGroupId != 0){
        	 
        	 if((goodsId != null) && (goodsId !=0)){
            	 strBuilder.append(" and s.goodsId = ? ");
            	 params.add(goodsId);
             }
        	 
        	 if((goodsGroupId != null) || (goodsGroupId != 0)){
        		 strBuilder.append(" and s.goodsId in (select profileId from EquipmentsProfile where equipmentsGroupId = ?) ");
            	 params.add(goodsGroupId);
        	 }
        	 
         }
         else
         {
        	 if((goodsId != null) && (goodsId !=0)){
            	 strBuilder.append(" and s.goodsId = ? ");
            	 params.add(goodsId);
             }
         }
         strBuilder.append(" order by goodsId");
         Query query = getSession().createQuery(strBuilder.toString());
         
         System.out.println("Query Stock: ");
         System.out.println(strBuilder.toString());
         System.out.println("Params: ");
         System.out.println(params);
         for(int i = 0; i < params.size(); i++){
        	 query.setParameter(i, params.get(i));
         }
         if (query.list().isEmpty()) {
             return null;
         } else {
             return query.list();
         }
    }

    public StockGoods getStockGoods(Long goodsId, Long status, Long shopId) {
        Query query = session.createQuery("Select s from StockGoods s where goodsId = ? and shopId = ? and goodsStatus = ? ");
        query.setParameter(0, goodsId);
        query.setParameter(1, shopId);
        query.setParameter(2, status);
        if (query.list().isEmpty()) {
            return null;
        } else {
            return (StockGoods) query.list().get(0);
        }
    }

    public List<StockGoods> getStockGoodsNotStatusError(Long goodsId, Long shopId) {
        Query query = session.createQuery("Select s from StockGoods s where goodsId = ? and shopId = ? and goodsStatus NOT IN(?) ");
        query.setParameter(0, goodsId);
        query.setParameter(1, shopId);
        query.setParameter(2, InventoryConstanst.GoodsStatus.ERROR);
//        query.setParameter(3, InventoryConstanst.GoodsStatus.ERROR);
//        query.setParameter(4, InventoryConstanst.GoodsStatus.USED);
        if (query.list().isEmpty()) {
            return null;
        } else {
            return  query.list();
        }
    }

    public void updateQuantity(Boolean desc, Long quan, Long goodsId, Long status, Long shopId) {
        Query query = null;
        Query query2 = null;
        if (!desc) {
            query = session.createQuery("Update StockGoods set quantity = quantity + ? ,availableQuantity = availableQuantity + ? where goodsId = ? and shopId = ? and goodsStatus = ?  ");
        } else {
            query = session.createQuery("Update StockGoods set quantity = quantity - ? ,availableQuantity = availableQuantity - ? where goodsId = ? and shopId = ? and goodsStatus = ? " );
            query2 =  session.createQuery(" Delete  StockGoods where quantity = 0 and availableQuantity = 0 ");
        }
        query.setParameter(0, quan);
        query.setParameter(1, quan);
        query.setParameter(2, goodsId);
        query.setParameter(3, shopId);
        query.setParameter(4, status);
        query.executeUpdate();
        if(query2!=null){
            query2.executeUpdate();
        }

    }

    public void updateQuantityDetailUpdate(Boolean desc, Long quan, Long goodsId, Long status, Long shopId) {
        Query query = null;
        Query query2 = null;
        if (!desc) {
            query = session.createQuery("Update StockGoods set quantity = quantity + ?  where goodsId = ? and shopId = ? and goodsStatus = ?  ");
        } else {
            query = session.createQuery("Update StockGoods set quantity = quantity - ?  where goodsId = ? and shopId = ? and goodsStatus = ? " );
            query2 =  session.createQuery(" Delete  StockGoods where quantity = 0 and availableQuantity = 0 ");
        }
        query.setParameter(0, quan);
        query.setParameter(1, quan);
        query.setParameter(2, goodsId);
        query.setParameter(3, shopId);
        query.setParameter(4, status);
        query.executeUpdate();
        if(query2!=null){
            query2.executeUpdate();
        }

    }

    public void updateQuantityKTV(Boolean desc, Long quan, Long goodsId, Long status, Long shopId) {
        Query query = null;
//        if (!desc) {
//            query = session.createQuery("Update StockGoods set quantity = quantity + ? ,availableQuantity = availableQuantity + ? where goodsId = ? and shopId = ? and goodsStatus = ?  ");
//        } else {
//            query = session.createQuery("Update StockGoods set availableQuantity = availableQuantity - ? where goodsId = ? and shopId = ? and goodsStatus = ?  ");
//        }
        query = session.createQuery("Update StockGoods set availableQuantity = availableQuantity - ?, quantity = quantity - ? where goodsId = ? and shopId = ? and goodsStatus = ?  ");
        query.setParameter(0, quan);
        query.setParameter(1, quan);
        query.setParameter(2, goodsId);
        query.setParameter(3, shopId);
        query.setParameter(4, status);
        query.executeUpdate();
    }

    public Boolean checkStockGoods(Long goodsId, Long status, Long shopId) {
        Query query = session.createQuery("Select count(1) from StockGoods s where goodsId = ? and shopId = ? and goodsStatus = ? ");
        query.setParameter(0, goodsId);
        query.setParameter(1, shopId);
        query.setParameter(2, status);
        return (Long)query.uniqueResult() != 0;
    }
}
