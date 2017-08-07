/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.dao;

import com.ftu.admin.consumer.utils.StringUtil;
import com.ftu.hibernate.GenericDAOHibernate;
import com.ftu.inventory.bo.ActionAudit;
import com.ftu.inventory.bo.ActionAudit;
import org.hibernate.Query;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author E5420
 */
public class ActionAuditDAO extends GenericDAOHibernate<ActionAudit, Long> {

    public ActionAuditDAO() {
        super(ActionAudit.class);
    }

    @Override
    public void saveOrUpdate(ActionAudit g) {
        if (g != null) {
            getSession().merge(g);
        }
        getSession().flush();
    }

    @Override
    public ActionAudit findById(Long id) {
        Query q = getSession().createQuery("Select p from ActionAudit p where p.actionAuditId = :id");
        q.setParameter("id", id);
        if (q.list().isEmpty()) {
            return null;
        } else {
            return (ActionAudit) q.list().get(0);
        }
    }

    public ActionAudit findByreferenceId(Long id) {
        Query q = getSession().createQuery("Select p from ActionAudit p where p.referenceId = :id");
        q.setParameter("id", id);
        if (q.list().isEmpty()) {
            return null;
        } else {
            return (ActionAudit) q.list().get(0);
        }
    }

    public List<ActionAudit> getAllActionAudits() {
        Query q = getSession().getNamedQuery("ActionAudit.findAll");
        if (q.list().isEmpty()) {
            return null;
        } else {
            return q.list();
        }

    }

    public List<ActionAudit> getActionAuditsByStatus(String status) {
        Query q = getSession().createQuery("Select p from ActionAudit p where status = ?");
        q.setParameter(0, status);
        if (q.list().isEmpty()) {
            return null;
        } else {
            return q.list();
        }
    }

    @Override
    public void delete(ActionAudit g) {
        getSession().delete(g);
        getSession().flush();
    }

}
