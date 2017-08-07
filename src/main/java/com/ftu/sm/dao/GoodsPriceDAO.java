/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.sm.dao;

import com.ftu.admin.consumer.utils.StringUtil;
import com.ftu.hibernate.GenericDAOHibernate;
import com.ftu.inventory.bo.EquipmentsProfile;
import com.ftu.inventory.bo.EquipmentsGroup;
import com.ftu.sm.bo.GoodsPrice;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;

/**
 *
 * @author E5420
 */
public class GoodsPriceDAO extends GenericDAOHibernate<GoodsPrice, Serializable> {

    public GoodsPriceDAO() {
        super(GoodsPrice.class);
    }

    @Override
    public void saveOrUpdate(GoodsPrice a) {
        getSession().saveOrUpdate(a);
        getSession().flush();
    }

    public void delete(GoodsPrice gp){
    	getSession().delete(gp);
    	getSession().flush();
    }
    
    
    public GoodsPrice findByGoodId(Long goodId) {
        Query query = getSession().getNamedQuery("GoodsPrice.findByGoodsId");
        query.setParameter("goodsId", goodId);
        if (query.list().isEmpty()) {
            return null;
        } else {
            return (GoodsPrice) query.list().get(0);
        }
    }
    
    public List<GoodsPrice> getAllGoods(GoodsPrice g, boolean search,int first,int pageSize) {

		String sql = "SELECT p.PRICE_ID,p.GOODS_ID,p.PRICE_TYPE,p.PRICE,p.STA_DATE,p.END_DATE,p.USERNAME,p.STATUS,p.VAT,g.GOODS_NAME,g.GOODS_GROUP_ID FROM GOODS_PRICE p INNER JOIN GOODS g"+ 
				" ON p.GOODS_ID = g.GOODS_ID WHERE 1=1 ";
		HashMap<String, Object> mapParams = new HashMap<String, Object>();
		if (g != null) {
//			System.out.println(g.getListId());
			if(g.getListId() != null){
				if(g.getGoodsId() != null){
					g.getListId().add(g.getGoodsId());
				}
				sql += " and p.GOODS_ID in :lstGId ";
				mapParams.put("lstGId", g.getListId());
			}else{
				if(g.getGoodsId() != null){
					sql += " and p.GOODS_ID = :gId ";
					mapParams.put("gId", g.getGoodsId());
				}
			}
			
			if(!StringUtil.stringIsNullOrEmty(g.getPriceType())){
				sql += " and p.PRICE_TYPE = :price ";
				mapParams.put("price", g.getPriceType().trim());
			}
			
			if(!StringUtil.stringIsNullOrEmty(g.getUsername())){
				sql += " and p.USERNAME = :uname ";
				mapParams.put("uname", g.getUsername().trim());
			}
			
			if(!StringUtil.stringIsNullOrEmty(g.getStatus())){
				sql += " and p.STATUS = :status ";
				mapParams.put("status", g.getStatus().trim());
			}
			
			if(g.getPriceId() != null){
				sql += " and p.PRICE_ID = :pId ";
				mapParams.put("pId", g.getPriceId());
			}
			
			if(g.getPrice() != null){
				/*if(search){
					sql += " and p.PRICE like :price ";
					mapParams.put("price","%"+ g.getPrice()+ "%");
				}else{
					
				}*/
				sql += " and p.PRICE = :price ";
				mapParams.put("price", g.getPrice());
				
			}
			
			if(g.getStaDate() != null){
				sql += " and p.STA_DATE = :sdate ";
				mapParams.put("sdate", g.getStaDate());
			}
			
			if(g.getEndDate() != null){
				sql += " and p.END_DATE = :edate ";
				mapParams.put("edate", g.getEndDate());
			}
			
			if(g.getVat() != null){
				if(search){
					sql += " and p.VAT like :vat ";
					mapParams.put("vat","%"+ g.getVat()+"%");
				}else{
					sql += " and p.VAT = :vat ";
					mapParams.put("vat", g.getVat());
				}
				
			}
			
		}

		sql += " order by p.PRICE_TYPE asc ";

		//Query q = getSession().createQuery(sql);
		SQLQuery q = getSession().createSQLQuery(sql);
		if (first >= 0) {
			q.setMaxResults(pageSize);
			q.setFirstResult(first);
		}
		
		for (String param : mapParams.keySet()) {
			if (mapParams.get(param) instanceof List) {
				q.setParameterList(param, (Collection) mapParams.get(param));
			} else {
				q.setParameter(param, mapParams.get(param));
			}
		}
		List<Object[]> result =  q.list();
		List<GoodsPrice> list = new ArrayList<GoodsPrice>();
		for (Object[] object : result) {
            list.add(convertRowToGoodsPrice(object));
        }
		return list;

	}
	
	private GoodsPrice convertRowToGoodsPrice(Object[] object){
		GoodsPrice goods = new GoodsPrice();
		goods.setPriceId((object[0] != null ? new Long(object[0].toString()) : null));
        goods.setGoodsId((object[1] != null ? new Long(object[1].toString()) : null));
        goods.setPriceType((object[2] != null ? object[2].toString() : null));
        goods.setPrice((object[3] != null ? new Double(object[3].toString()) : null));
        goods.setStaDate((object[4] != null ? (Date)object[4] : null));
        goods.setEndDate((object[5] != null ? (Date)object[5] : null));
        goods.setUsername((object[6] != null ? object[6].toString() : null));
        goods.setStatus((object[7] != null ? object[7].toString() : null));
        goods.setVat((object[8] != null ? new Long(object[8].toString()) : null));
        goods.setGoodsName((object[9] != null ? object[9].toString().toString() : null));
        goods.setGoodsGroupId((object[10] != null ? new Long(object[10].toString()) : null));
        return goods;
	}
}
