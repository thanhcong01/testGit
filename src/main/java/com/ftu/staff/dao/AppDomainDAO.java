package com.ftu.staff.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;

import com.ftu.admin.consumer.utils.StringUtil;
import com.ftu.hibernate.GenericDAOHibernate;
import com.ftu.staff.bo.ApDomain;

public class AppDomainDAO extends GenericDAOHibernate<ApDomain, Long> implements Serializable {

    public AppDomainDAO() {
        super(ApDomain.class);
    }

    public List<ApDomain> findAllByType(String type) {
        Query q = getSession().createQuery("Select ap from ApDomain ap where UPPER(ap.type)  = ? and status = ? ORDER BY UPPER(ap.name) ASC ");
        q.setParameter(0, type.trim().toUpperCase());
        q.setParameter(1, "1");
        if (q.list().isEmpty()) {
            return new ArrayList<>();
        } else {
            return q.list();
        }

    }
    public List<ApDomain> findAllByTypeAllStatus(String type) {
        Query q = getSession().createQuery("Select ap from ApDomain ap where UPPER(ap.type)  = ? ORDER BY UPPER(ap.name) ASC ");
        q.setParameter(0, type.trim().toUpperCase());
        if (q.list().isEmpty()) {
            return new ArrayList<>();
        } else {
            return q.list();
        }

    }

    public List<ApDomain> findByTypeAndCode(String type, String code) {
        Query q = getSession().createQuery("Select ap from ApDomain ap where ap.type = ? and ap.code = ? and status = ? ORDER BY UPPER(ap.name) ASC");
        q.setParameter(0, type.trim().toUpperCase());
        q.setParameter(1, code);
        q.setParameter(2, "1");
        if (q.list().isEmpty()) {
            return null;
        } else {
            return q.list();
        }
    }

    public ApDomain findByTypeAndValue(String type, Long value) {
        Query q = getSession().createQuery("Select ap from ApDomain ap where UPPER(ap.type)  = ? and ap.value = ? and status = ? ORDER BY UPPER(ap.name) ASC");
        q.setParameter(0, type.trim().toUpperCase());
        q.setParameter(1, value);
        q.setParameter(2, "1");
        if (q.list().isEmpty()) {
            return null;
        } else {
            return (ApDomain) q.list().get(0);
        }
    }

    public ApDomain findByTypeAndValue(String type,String code, Long value) {
        Query q = getSession().createQuery("Select ap from ApDomain ap where UPPER(ap.type)  = ? and UPPER(ap.code) = ? and ap.value = ? and status = ? ");
        q.setParameter(0, type.trim().toUpperCase());
        q.setParameter(1, code.trim().toUpperCase());
        q.setParameter(2, value);
        q.setParameter(3, "1");
        if(type.isEmpty() || code.isEmpty() || value == null) return null;
        if (q.list().isEmpty()) {
            return null;
        } else {
            return (ApDomain) q.list().get(0);
        }

    }

    @Override
    public void saveOrUpdate(ApDomain o) {
        // TODO Auto-generated method stub
        if (o != null) {
            getSession().merge(o);
        }
        getSession().flush();
    }

    @Override
    public void delete(ApDomain o) {
        // TODO Auto-generated method stub
        getSession().delete(o);
        getSession().flush();
    }

    public List<ApDomain> getAllDomains(ApDomain domain, boolean search) {
        String sql = "select s from ApDomain s where 1=1 ";
        HashMap<String, Object> mapParams = new HashMap<String, Object>();
        if (domain != null) {

            if (!StringUtil.stringIsNullOrEmty(domain.getType())) {
                if (search) {
                    sql += " and UPPER(s.type) like :type ";
                    mapParams.put("type", "%" + domain.getType().toUpperCase().trim() + "%");
                } else {
                    sql += " and UPPER(s.type) = :type ";
                    mapParams.put("type", domain.getType().toUpperCase().trim());
                }
            }

            if (!StringUtil.stringIsNullOrEmty(domain.getCode())) {
                if (search) {
                    sql += " and UPPER(s.code) like :code ";
                    mapParams.put("code", "%" + domain.getCode().trim().toUpperCase() + "%");
                } else {
                    sql += " and UPPER(s.code) = :code ";
                    mapParams.put("code", domain.getCode().trim().toUpperCase());
                }
            }

            if (!StringUtil.stringIsNullOrEmty(domain.getName())) {
                if (search) {
                    sql += " and UPPER(s.name) like :name ";
                    mapParams.put("name", "%" + domain.getName().trim().toUpperCase() + "%");
                } else {
                    sql += " and UPPER(s.name) = :name ";
                    mapParams.put("name", domain.getName().trim().toUpperCase());
                }
            }

            if (!StringUtil.stringIsNullOrEmty(domain.getDescription())) {
                if (search) {
                    sql += " and UPPER(s.description) like :description ";
                    mapParams.put("description", "%" + domain.getDescription().trim().toUpperCase() + "%");
                } else {
                    sql += " and UPPER(s.description) = :description ";
                    mapParams.put("description", domain.getDescription().trim().toUpperCase());
                }
            }

            if (!StringUtil.stringIsNullOrEmty(domain.getStatus())) {
                if (search) {
                    sql += " and UPPER(s.status) like :status ";
                    mapParams.put("status", "%" + domain.getStatus().trim().toUpperCase() + "%");
                } else {
                    sql += " and UPPER(s.status) = :status ";
                    mapParams.put("status", domain.getStatus().trim().toUpperCase());
                }
            }

            if (domain.getValue() != null) {
                if (search) {
                    sql += " and (s.value like :value OR s.value = :value ) ";
                    mapParams.put("value", "%" + domain.getValue() + "%");
                    mapParams.put("value", domain.getValue());
                } else {
                    sql += " and s.value = :value ";
                    mapParams.put("value", domain.getValue());
                }
            }

            if (domain.getParentId() != null) {
            	sql += " and s.parentId = :parentId ";
                mapParams.put("parentId", domain.getParentId());
            }

        }else{
//        	sql += " and UPPER(s.status) = :status ";
//        	mapParams.put("status", "1");
        }

        sql += " order by s.type,s.code asc ";

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

}
