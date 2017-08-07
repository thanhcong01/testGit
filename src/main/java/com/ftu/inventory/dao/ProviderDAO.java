/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.dao;

import com.ftu.inventory.bo.Provider;
import com.ftu.staff.bo.Staff;
import com.ftu.admin.consumer.utils.StringUtil;
import com.ftu.hibernate.GenericDAOHibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author E5420
 */
public class ProviderDAO extends GenericDAOHibernate<Provider, Long> {

    public ProviderDAO() {
        super(Provider.class);
    }

    @Override
    public void saveOrUpdate(Provider g) {
        if (g != null) {
            getSession().merge(g);
        }
        getSession().flush();
    }

    @Override
    public Provider findById(Long id) {
        Query q = getSession().createQuery("Select p from Provider p where p.providerId = :id");
        q.setParameter("id", id);
        if (q.list().isEmpty()) {
            return null;
        } else {
            return (Provider) q.list().get(0);
        }
    }

    public List<Provider> getAllProviders(Provider p, boolean search) {

        String sql = "select s from Provider s where 1=1 ";
        HashMap<String, Object> mapParams = new HashMap<String, Object>();
        if (p != null) {
            if (!StringUtil.stringIsNullOrEmty(p.getProviderCode())) {
                if (search) {
                    sql += " and UPPER(s.providerCode) like :code ";
                    mapParams.put("code", "%" + p.getProviderCode().trim().toUpperCase() + "%");
                } else {
                    sql += " and s.providerCode = :code ";
                    mapParams.put("code", p.getProviderCode().trim());
                }

            }
            if (!StringUtil.stringIsNullOrEmty(p.getProviderName())) {
                if (search) {
                    sql += " and UPPER(s.providerName) like :name ";
                    mapParams.put("name", "%" + p.getProviderName().trim().toUpperCase() + "%");
                } else {
                    sql += " and s.providerName = :name ";
                    mapParams.put("name", p.getProviderName().trim());
                }

            }

            if (!StringUtil.stringIsNullOrEmty(p.getAddress())) {
                if (search) {
                    sql += " and UPPER(s.address) like :address ";
                    mapParams.put("address", "%" + p.getAddress().trim().toUpperCase() + "%");
                } else {
                    sql += " and s.address = :address ";
                    mapParams.put("address", p.getAddress().trim());
                }

            }

            if (!StringUtil.stringIsNullOrEmty(p.getPhone())) {
                if (search) {
                    sql += " and s.phone like :phone ";
                    mapParams.put("phone", "%" + p.getPhone().trim() + "%");
                } else {
                    sql += " and s.phone = :phone ";
                    mapParams.put("phone", p.getPhone().trim());
                }

            }

            if (!StringUtil.stringIsNullOrEmty(p.getFax())) {
                if (search) {
                    sql += " and s.fax like :fax ";
                    mapParams.put("fax", "%" + p.getFax().trim() + "%");
                } else {
                    sql += " and s.fax = :fax ";
                    mapParams.put("fax", p.getFax().trim());
                }

            }

            if (!StringUtil.stringIsNullOrEmty(p.getStatus())) {
                sql += " and s.status = :status ";
                mapParams.put("status", p.getStatus());
            }

            if (!StringUtil.stringIsNullOrEmty(p.getContractNo())) {
                if (search) {
                    sql += " and UPPER(s.contractNo) like :contract ";
                    mapParams.put("contract", "%" + p.getContractNo().toUpperCase().trim() + "%");
                } else {
                    sql += " and s.contractNo = :contract ";
                    mapParams.put("contract", p.getContractNo().trim());
                }
            }

            if (p.getContractDate() != null) {
                sql += " and s.contractDate = :date ";
                mapParams.put("date", p.getContractDate());
            }
        }

        sql += " order by s.providerCode, s.providerName asc ";

        Query q = getSession().createQuery(sql);

        for (String param : mapParams.keySet()) {
            q.setParameter(param, mapParams.get(param));
        }

        if (q.list().isEmpty()) {
            return null;
        } else {
            return q.list();
        }

    }

    public Provider checkExists(String c) {
        String sql = "select s from Provider s where UPPER(s.contractNo) = :contractNo ";
        Provider st = null;
        Query q = getSession().createQuery(sql);
        q.setParameter("contractNo", c.trim().toUpperCase());
        List<Provider> lst = q.list();
        if (!lst.isEmpty()) {
            st = lst.get(0);
        }
        return st;
    }
    public Provider findByProviderCode(String code) {
        String sql = "select s from Provider s where UPPER(s.providerCode) = :providerCode ";
        Provider st = null;
        Query q = getSession().createQuery(sql);
        q.setParameter("providerCode", code.trim().toUpperCase());
        List<Provider> lst = q.list();
        if (!lst.isEmpty()) {
            st = lst.get(0);
        }
        return st;
    }

    public List<Provider> getAllProviders() {
        Query q = getSession().getNamedQuery("Provider.findAll");
        if (q.list().isEmpty()) {
            return null;
        } else {
            return q.list();
        }

    }
    public List<Provider> getAllProviderActive() {
        Query q = getSession().createQuery("Select p from Provider p where status = 1");
        if (q.list().isEmpty()) {
            return null;
        } else {
            return q.list();
        }
    }

    public List<Provider> getProvidersByStatus(String status) {
        Query q = getSession().createQuery("Select p from Provider p where status = ? order by p.providerCode ASC , p.providerName ASC ");
        q.setParameter(0, status);
        if (q.list().isEmpty()) {
            return null;
        } else {
            return q.list();
        }
    }

    @Override
    public void delete(Provider g) {
        getSession().delete(g);
        getSession().flush();
    }

    public List<Provider> getProviderByStockSerial() {
        try{
            StringBuilder s = new StringBuilder("SELECT " +
                    "st.PROVIDER_ID, st.PROVIDER_CODE, st.PROVIDER_NAME "+
                    " FROM PROVIDER  st JOIN STOCK_TRANSACTION_SERIAL tr ON st.PROVIDER_ID = tr.PROVIDER_ID" +
                    " WHERE 1 = 1 ");
            s.append(" GROUP BY st.PROVIDER_ID, st.PROVIDER_CODE, st.PROVIDER_NAME  ORDER BY st.PROVIDER_CODE, st.PROVIDER_NAME ");
            Query q = getSession().createSQLQuery(s.toString());
            List<Provider> rs = new ArrayList<>();
            List<Object[]> a = q.list();
            for (Object[] o : a) {
                Provider st = new Provider();
                st.setProviderId(o[0] == null ? null : Long.parseLong(o[0].toString()));
                st.setProviderCode(o[1] == null ? null :o[1].toString());
                st.setProviderName(o[2] == null ? null : o[2].toString());
                rs.add(st);
            }
            return rs;
        }catch(Exception ex){
            ex.printStackTrace();
            return new ArrayList<Provider>();
        }
    }
}
