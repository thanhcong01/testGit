/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ftu.inventory.bo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DuongThanhCong
 */
@Entity
@Table(name = "ACTION_AUDIT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ActionAudit.findAll", query = "SELECT a FROM ActionAudit a"),
    @NamedQuery(name = "ActionAudit.findByActionAuditId", query = "SELECT a FROM ActionAudit a WHERE a.actionAuditId = :actionAuditId"),
    @NamedQuery(name = "ActionAudit.findByActionType", query = "SELECT a FROM ActionAudit a WHERE a.actionType = :actionType"),
    @NamedQuery(name = "ActionAudit.findByActionDatetime", query = "SELECT a FROM ActionAudit a WHERE a.actionDatetime = :actionDatetime"),
    @NamedQuery(name = "ActionAudit.findByReferenceId", query = "SELECT a FROM ActionAudit a WHERE a.referenceId = :referenceId")})
public class ActionAudit implements Serializable {
    private static final long serialVersionUID = 1L;
    @SequenceGenerator(name = "ACTION_AUDIT_SEQ", sequenceName = "ACTION_AUDIT_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ACTION_AUDIT_SEQ")
    @Basic(optional = false)
    @Column(name = "ACTION_AUDIT_ID")
    private Long actionAuditId;
    @Column(name = "ACTION_TYPE")
    private Long actionType;
    @Column(name = "ACTION_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actionDatetime;
    @Column(name = "REFERENCE_ID")
    private Long referenceId;

    public ActionAudit() {
    }

    public ActionAudit(Long actionAuditId) {
        this.actionAuditId = actionAuditId;
    }

    public Long getActionAuditId() {
        return actionAuditId;
    }

    public void setActionAuditId(Long actionAuditId) {
        this.actionAuditId = actionAuditId;
    }

    public Long getActionType() {
        return actionType;
    }

    public void setActionType(Long actionType) {
        this.actionType = actionType;
    }

    public Date getActionDatetime() {
        return actionDatetime;
    }

    public void setActionDatetime(Date actionDatetime) {
        this.actionDatetime = actionDatetime;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (actionAuditId != null ? actionAuditId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActionAudit)) {
            return false;
        }
        ActionAudit other = (ActionAudit) object;
        if ((this.actionAuditId == null && other.actionAuditId != null) || (this.actionAuditId != null && !this.actionAuditId.equals(other.actionAuditId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.ActionAudit[ actionAuditId=" + actionAuditId + " ]";
    }
    
}
