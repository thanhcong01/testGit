/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.staff.dao;

import com.ftu.staff.bo.Shop;
import com.ftu.hibernate.GenericDAOHibernate;

import java.io.Serializable;
import java.util.*;

import com.ftu.utils.StringUtil;
import org.hibernate.Query;
import org.hibernate.SQLQuery;

/**
 *
 * @author E5420
 */
public class ShopDAO extends GenericDAOHibernate<Shop, Long> implements Serializable{

    public ShopDAO() {
        super(Shop.class);
    }

    @Override
    public Shop findById(Long id) {
        Query q = getSession().createQuery("Select s from Shop s where s.shopId = ? ");
        if(id == null) return null;
        q.setParameter(0, id);
        if (q.list().isEmpty()) {
            return null;
        } else {
            return (Shop) q.list().get(0);
        }

    }

    public Shop findByShopCode(String shopCode,Long shopId) {
        StringBuilder sql = new StringBuilder("select s from Shop s where UPPER(s.shopCode) = :code ");

        if(shopId!=null){
            sql.append(" and s.shopId !=  :id");
        }
        sql.append(" and s.shopStatus = 1");
        Shop st = null;
        Query q = getSession().createQuery(sql.toString());
        q.setParameter("code", shopCode.trim().toUpperCase());
        if(shopId!=null){
            q.setParameter("id", shopId);
        }
        List<Shop> lst = q.list();
        if (!lst.isEmpty()) {
            st = lst.get(0);
        }
        return st;
    }

    public Shop findByShopCodeAllStatus(String shopCode,Long shopId) {
        StringBuilder sql = new StringBuilder("select s from Shop s where UPPER(s.shopCode) = :code ");

        if(shopId!=null){
            sql.append(" and s.shopId !=  :id");
        }
        Shop st = null;
        Query q = getSession().createQuery(sql.toString());
        q.setParameter("code", shopCode.trim().toUpperCase());
        if(shopId!=null){
            q.setParameter("id", shopId);
        }
        List<Shop> lst = q.list();
        if (!lst.isEmpty()) {
            st = lst.get(0);
        }
        return st;
    }

    public List<Shop> findAllShop() {
        Query q = getSession().createQuery("Select s from Shop s order by s.shopCode ");
        if (q.list().isEmpty()) {
            return new ArrayList<Shop>();
        } else {
            return q.list();
        }
    }

    public List<Shop> findAllShopActive() {
        Query q = getSession().createQuery("Select s from Shop s where s.shopStatus = 1 order by s.shopCode ");
        if (q.list().isEmpty()) {
            return new ArrayList<Shop>();
        } else {
            return q.list();
        }
    }

    public List<Shop> findAllShopTree(Long parentId){
        StringBuilder sql = new StringBuilder("Select s from Shop s where s.shopStatus = 1  ");
        if(parentId != null){
            sql.append(" and s.shopParentId = ? or s.shopId = ?");
        }
        sql.append(" order by s.shopCode ");
        Query q = getSession().createQuery(sql.toString());
        if(parentId != null) {
            q.setParameter(0, parentId);
            q.setParameter(1, parentId);
        }
        if (q.list().isEmpty()) {
            return new ArrayList<Shop>();
        } else {
            return getTree(q.list());
        }
    }

    public List<Shop> getTreeShop(Long parentId){
        StringBuilder sql = new StringBuilder("SELECT s.*  FROM SHOP s where s.SHOP_STATUS = 1 ");
        if(parentId != null){
            sql.append(" START WITH s.SHOP_ID = ?");
        }else{
            sql.append(" START WITH s.SHOP_PARENT_ID is null");
        }
        sql.append(" CONNECT BY PRIOR s.SHOP_ID = s.SHOP_PARENT_ID ");
        SQLQuery q = getSession().createSQLQuery(sql.toString());
        q.addEntity(Shop.class);
        if(parentId != null) {
            q.setParameter(0, parentId);
        }
        if (q.list().isEmpty()) {
            return new ArrayList<Shop>();
        } else {
            return getTree(q.list());
        }
    }
    public List<Shop> getTreeShopAll(Long parentId){
        StringBuilder sql = new StringBuilder("SELECT s.*  FROM SHOP s where 1=1 ");
        if(parentId != null){
            sql.append(" START WITH s.SHOP_ID = ?");
        }else{
            sql.append(" START WITH s.SHOP_PARENT_ID is null");
        }
        sql.append(" CONNECT BY PRIOR s.SHOP_ID = s.SHOP_PARENT_ID ");
        SQLQuery q = getSession().createSQLQuery(sql.toString());
        q.addEntity(Shop.class);
        if(parentId != null) {
            q.setParameter(0, parentId);
        }
        if (q.list().isEmpty()) {
            return new ArrayList<Shop>();
        } else {
            return getTree(q.list());
        }
    }

    public List<Shop> getAllShopChildrent(Long parentId){
        StringBuilder sql = new StringBuilder("SELECT s.*  FROM SHOP s where s.SHOP_STATUS = 1 ");
        if(parentId != null){
            sql.append(" START WITH s.SHOP_ID = ?");
        }else{
            sql.append(" START WITH s.SHOP_PARENT_ID is null");
        }
        sql.append(" CONNECT BY PRIOR s.SHOP_ID = s.SHOP_PARENT_ID ");
        SQLQuery q = getSession().createSQLQuery(sql.toString());
        q.addEntity(Shop.class);
        if(parentId != null) {
            q.setParameter(0, parentId);
        }
        if (q.list().isEmpty()) {
            return new ArrayList<Shop>();
        } else {
            return q.list();
        }
    }
    public List<Shop> getAllShopChildrentAllStatus(Long parentId){
        StringBuilder sql = new StringBuilder("SELECT s.*  FROM SHOP s where 1=1 ");
        if(parentId != null){
            sql.append(" START WITH s.SHOP_ID = ?");
        }else{
            sql.append(" START WITH s.SHOP_PARENT_ID is null");
        }
        sql.append(" CONNECT BY PRIOR s.SHOP_ID = s.SHOP_PARENT_ID ");
        SQLQuery q = getSession().createSQLQuery(sql.toString());
        q.addEntity(Shop.class);
        if(parentId != null) {
            q.setParameter(0, parentId);
        }
        if (q.list().isEmpty()) {
            return new ArrayList<Shop>();
        } else {
            return q.list();
        }
    }

    public List<Shop> getAllShopParrent(){
        Query q = getSession().createQuery("Select s from Shop s where s.shopParentId = null order by s.shopCode ");
        if (q.list().isEmpty()) {
            return new ArrayList<Shop>();
        } else {
            return getTree(q.list());
        }
    }

    private List<Shop> getTree(List<Shop> source){
        List<Shop> list = new ArrayList<Shop>();
        LinkedHashMap<Long, Shop> mapShop = new LinkedHashMap<Long, Shop>();
        for(Shop s:source){
            mapShop.put(s.getShopId(), s);
        }
        for (Shop s:source) {
            Shop parent = null;
            if (s.getShopParentId() != null) {
                parent = mapShop.get(s.getShopParentId());
                if(parent!=null) {
                    s.setParentShop(parent);
                    parent.getChildShops().add(s);
                }
            }
            list.add(s);
        }
        return list;
    }

    public Shop getAllShop(Long id) {
        Shop rs = findById(id);
        rs.setChildShops(new ArrayList<Shop>());
        List<Shop> lsChild = findByParentId(id);
        if (lsChild != null && !lsChild.isEmpty()) {
            for (Shop subs : lsChild) {
                rs.getChildShops().add(getAllShop(subs.getShopId()));
            }
        }
        return rs;
    }
    public Shop getAllShopAllStatus(Long id) {
        Shop rs = findById(id);
        rs.setChildShops(new ArrayList<Shop>());
        List<Shop> lsChild = findByParentIdAllStatus(id);
        if (lsChild != null && !lsChild.isEmpty()) {
            for (Shop subs : lsChild) {
                rs.getChildShops().add(getAllShopAllStatus(subs.getShopId()));
            }
        }
        return rs;
    }

    public Shop getAllShopStatus(Long id) {
        Shop rs = findById(id);
        rs.setChildShops(new ArrayList<Shop>());
        List<Shop> lsChild = findByParentIdBySearchDetail(id);
        if (lsChild != null && !lsChild.isEmpty()) {
            for (Shop subs : lsChild) {
                rs.getChildShops().add(getAllShop(subs.getShopId()));
            }
        }
        return rs;
    }
    



    public List<Shop> getAllParentShopById(Long id) {
        Query q;
        if (id == null) {
            q = getSession().createQuery("select s from shop s join (\r\n" +
                    "select connect_by_root shopId shopId, CONNECT_BY_ISLEAF leaf\r\n" +
                    "from shop \r\n" +
                    "connect by prior shopId = shopParentId\r\n" +
                    "start with shopParentId = 3\r\n" +
                    ") d on d.shopId = s.shopId\r\n" +
                    "where d.leaf = 0");
        } else {
            q = getSession().createQuery("select s from shop s join (\r\n" +
                    "select connect_by_root shopId shopId, CONNECT_BY_ISLEAF leaf\r\n" +
                    "from shop \r\n" +
                    "connect by prior shopId = shopParentId\r\n" +
                    "start with shopParentId = ?\r\n" +
                    ") d on d.shopId = s.shopId\r\n" +
                    "where d.leaf = 0");
            q.setParameter(0, id);
        }
        if (q.list().isEmpty()) {
            return new ArrayList<Shop>();
        } else {
            return q.list();
        }
    }

    public List<Shop> findByParentIdBySearchDetail(Long id) {
        Query q;
        if (id == null) {
            q = getSession().createQuery("Select s from Shop s where s.shopParentId is null ");
        } else {
            q = getSession().createQuery("Select s from Shop s where s.shopParentId = ? ");
            q.setParameter(0, id);
        }
        if (q.list().isEmpty()) {
            return new ArrayList<Shop>();
        } else {
            return q.list();
        }

	}

    public List<Shop> findByParentId(Long id) {
        Query q;
        if (id == null) {
            q = getSession().createQuery("Select s from Shop s where s.shopStatus = 1 and s.shopParentId is null ");
        } else {
            q = getSession().createQuery("Select s from Shop s where s.shopStatus = 1 and  s.shopParentId = ? ");
            q.setParameter(0, id);
        }
        if (q.list().isEmpty()) {
            return new ArrayList<Shop>();
        } else {
            return q.list();
        }

    }
    public List<Shop> findByParentIdAllStatus(Long id) {
        Query q;
        if (id == null) {
            q = getSession().createQuery("Select s from Shop s where  s.shopParentId is null ");
        } else {
            q = getSession().createQuery("Select s from Shop s where  s.shopParentId = ? ");
            q.setParameter(0, id);
        }
        if (q.list().isEmpty()) {
            return new ArrayList<Shop>();
        } else {
            return q.list();
        }

    }

    @Override
    public void saveOrUpdate(Shop g) {
        if (g != null) {
            super.saveOrUpdate(g);
        }
        getSession().flush();
    }

    @Override
    public void delete(Shop g) {
        getSession().delete(g);
        getSession().flush();
    }

    public List<Shop> getListShop(Long parentShopId,Long shopId) {
        Query q;
        if (parentShopId == null) {
            q = getSession().createQuery("Select s from Shop s where s.shopParentId is null  and s.shopId != ? ");
            q.setParameter(0,shopId);
        } else {
            q = getSession().createQuery("Select s from Shop s where s.shopParentId = ?  and s.shopId != ?");
            q.setParameter(0, parentShopId);
            q.setParameter(1,shopId);
        }
        if (q.list().isEmpty()) {
            return new ArrayList<Shop>();
        } else {
            return q.list();
        }

    }

    public List<Shop> getListShopByParentId(Shop s, boolean search,int first,int pageSize, String sortField, Boolean desc) {

        Map<String,Object> mapParams = new LinkedHashMap<String,Object>() ;
        StringBuilder sql = new StringBuilder("SELECT s.SHOP_ID shopId,\n" +
                "  s.SHOP_CODE shopCode,\n" +
                "  s.SHOP_PARENT_ID shopParentId,\n" +
                "  s.SHOP_NAME shopName,\n" +
                "  s.SHOP_STATUS shopStatus,\n" +
                "  s.ADDRESS address,\n" +
                "  s.SHOP_TYPE shopType,\n" +
                "  s.CONTACT_NAME contactName,\n" +
                "  s.CONTACT_TITLE contactTitle,\n" +
                "  s.TEL_NUMBER telNumber,\n" +
                "  s.FAX fax,\n" +
                "  s.EMAIL email,\n" +
                "  s.DESCRIPTION description,\n" +
                "  s.PROVINCE province,\n" +
                "  s.CODE_PATH codePath,\n" +
                "  s.LATITUDE latitude,\n" +
                "  s.LONGITUDE longitude,\n" +
                "  s.STATUS_CANCEL statusCancel\n" +
                "FROM SHOP s  ");
        //        sql.append(" where s.shopStatus = 1 ");
        if (s.getShopId() == null) {
            //            sql.append(" and s.shopParentId is null ");
            sql.append("join (select b.SHOP_ID from Shop b  \n" +
                    "                          start with b.SHOP_PARENT_ID  is null \n" +
                    "                          connect by prior b.SHOP_ID = b.SHOP_PARENT_ID) d on s.SHOP_ID = d.SHOP_ID ");
        } else {
            //            sql.append(" and s.shopParentId = :id");
            sql.append(" join (select b.SHOP_ID from Shop b  \n" +
                    "                          start with b.SHOP_ID = :id \n" +
                    "                          connect by prior b.SHOP_ID = b.SHOP_PARENT_ID) d on s.SHOP_ID = d.SHOP_ID ");
            mapParams.put("id",s.getShopId());
        }

        if(!StringUtil.isEmpty(s.getShopCode())){
            sql.append(" and UPPER(s.SHOP_CODE) like :SHOP_CODE");
            mapParams.put("SHOP_CODE","%"+ s.getShopCode().trim().toUpperCase()+"%");
        }
        if(!StringUtil.isEmpty(s.getShopName())){
            sql.append(" and UPPER(s.SHOP_NAME) like :SHOP_NAME");
            mapParams.put("SHOP_NAME", "%" + s.getShopName().trim().toUpperCase() +"%");
        }
        if(!StringUtil.isEmpty(s.getAddress())){
            sql.append(" and UPPER(s.ADDRESS) like :ADDRESS");
            mapParams.put("ADDRESS", "%" + s.getAddress().trim().toUpperCase() +"%");
        }
        if(!StringUtil.isEmpty(s.getContactName())){
            sql.append(" and UPPER(s.CONTACT_NAME) like :CONTACT_NAME");
            mapParams.put("CONTACT_NAME", "%" + s.getContactName().trim().toUpperCase() +"%");
        }
        if(!StringUtil.isEmpty(s.getTelNumber())){
            sql.append(" and UPPER(s.TEL_NUMBER) like :TEL_NUMBER");
            mapParams.put("TEL_NUMBER", "%" + s.getTelNumber().trim().toUpperCase() +"%");
        }
        if(s.getStatusCancel()!=null){
            sql.append(" and s.STATUS_CANCEL = :STATUS_CANCEL");
            mapParams.put("STATUS_CANCEL", s.getStatusCancel());
        }

        if (s.getShopStatus()!=null)
        {
            sql.append(" where s.SHOP_STATUS = :SHOP_STATUS ");
            mapParams.put("SHOP_STATUS", s.getShopStatus());
        }
        if(sortField != null){
            if(desc){
                sql.append(" order by UPPER(" + sortField + ") desc ");
            } else{
                sql.append(" order by UPPER(" + sortField + ")  asc ");
            }
        }else {
            sql.append(" ORDER BY  s.SHOP_CODE ASC");
        }

        //System.out.println(sql.toString());
        //        Query q = getSession().createQuery(sql.toString());
        Query q = getSession().createSQLQuery(sql.toString());
        for (String param : mapParams.keySet()) {
            q.setParameter(param, mapParams.get(param));
        }
        if (first >= 0) {
            q.setMaxResults(pageSize);
            q.setFirstResult(first);
        }
        List<Shop> rs = new ArrayList<Shop>();
        List<Object[]> a = q.list();
        if( a == null || a.isEmpty()) return rs;

        for (Object[] o : a) {
            rs.add(convertObjectToEquipment(o));
        }
        return rs;
        //        if (q.list().isEmpty()) {
        //            return new ArrayList<Shop>();
        //        } else {
        //            return q.list();
        //        }

    }
    public Shop convertObjectToEquipment(Object[] o) {
        Shop rs = new Shop();
        try {
            rs.setShopId(o[0] == null ? null : Long.parseLong(o[0].toString()));
            rs.setShopCode(o[1] == null ? null : o[1].toString());
            rs.setShopParentId(o[2] == null ? null : Long.parseLong(o[2].toString()));
            rs.setShopName(o[3] == null ? null : o[3].toString());
            rs.setShopStatus(o[4] == null ? null : Long.parseLong(o[4].toString()));
            rs.setAddress(o[5] == null ? null : o[5].toString());
            rs.setShopType(o[6] == null ? null : o[6].toString());
            rs.setContactName(o[7] == null ? null : o[7].toString());
            rs.setContactTitle(o[8] == null ? null : o[8].toString());
            rs.setTelNumber(o[9] == null ? null : o[9].toString());
            rs.setFax(o[10] == null ? null : o[10].toString());
            rs.setEmail(o[11] == null ? null : o[11].toString());
            rs.setDescription(o[12] == null ? null : o[12].toString());
            rs.setProvince(o[13] == null ? null : o[13].toString());
            rs.setCodePath(o[14] == null ? null : o[14].toString());
            rs.setLatitude(o[15] == null ? null : Double.parseDouble(o[15].toString()));
            rs.setLongitude(o[16] == null ? null : Double.parseDouble(o[16].toString()));
            rs.setStatusCancel(o[17] == null ? false : (o[17].toString().equals("1")?true:false));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public List<Shop> getAllShopPosition(Long parentId) {
        try{
            StringBuilder s = new StringBuilder("SELECT " +
                    "st.SHOP_ID, st.SHOP_CODE, st.SHOP_NAME "+
                    " FROM SHOP  st JOIN POSITION tr ON st.SHOP_ID = tr.SHOP_ID" +
                    " WHERE 1 = 1 ");
            if (parentId != null) {
                s.append(" AND st.shopParentId = ?");

            }
            s.append(" GROUP BY st.SHOP_ID, st.SHOP_CODE, st.SHOP_NAME  ORDER BY st.SHOP_CODE, st.SHOP_NAME ");
            Query q = getSession().createSQLQuery(s.toString());
            if (parentId != null) {
                q.setParameter(0, parentId);
            }
            List<Shop> rs = new ArrayList<>();
            List<Object[]> a = q.list();
            for (Object[] o : a) {
                Shop st = new Shop();
                st.setShopId(o[0] == null ? null : Long.parseLong(o[0].toString()));
                st.setShopCode(o[1] == null ? null :o[1].toString());
                st.setShopName(o[2] == null ? null : o[2].toString());
                rs.add(st);
            }
            return rs;
        }catch(Exception ex)
        {
            return new ArrayList<Shop>();
        }
    }

    public List<Shop> findEshopByParentId(Long id) {
        Query q;
        if (id == null) {
            q = getSession().createQuery("Select s from Shop s where s.shopStatus = 1 and s.shopType = 1 and s.shopParentId is null ");
        } else {
            q = getSession().createQuery("Select s from Shop s where s.shopStatus = 1 and s.shopType = 1 and  s.shopParentId = ? ");
            q.setParameter(0, id);
        }
        if (q.list().isEmpty()) {
            return new ArrayList<Shop>();
        } else {
            return q.list();
        }

    }

}
