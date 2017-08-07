package com.ftu.inventory.bussiness;

import com.ftu.hibernate.HibernateUtil;
import com.ftu.inventory.bo.ActionAudit;
import com.ftu.inventory.bo.ActionAudit;
import com.ftu.inventory.dao.ActionAuditDAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ActionAuditService implements Serializable {

    public List<ActionAudit> getAll() {
        try {
            ActionAuditDAO dao = new ActionAuditDAO();
            return dao.findAll();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(ActionAuditService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return new ArrayList<>();
    }
    public void saveOrUpdate(ActionAudit obj) {
        try {
            ActionAuditDAO dao = new ActionAuditDAO();
            dao.saveOrUpdate(obj);
            HibernateUtil.commitCurrentSessions();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(ActionAuditService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    public void delete(ActionAudit obj) {
        try {
            ActionAuditDAO dao = new ActionAuditDAO();
            dao.delete(obj);
            HibernateUtil.commitCurrentSessions();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(ActionAuditService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
    public ActionAudit findById(Long id ){
        try {
            ActionAuditDAO dao = new ActionAuditDAO();
            return dao.findById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(ActionAuditService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return null;
    }

    public ActionAudit findByreferenceId(Long id ){
        try {
            ActionAuditDAO dao = new ActionAuditDAO();
            return dao.findByreferenceId(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(ActionAuditService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return null;
    }
}
