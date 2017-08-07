/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ftu.inventory.bo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author E5420
 */
@Entity
@Table(name = "ATTACHS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Attachs.findAll", query = "SELECT a FROM Attachs a"),
    @NamedQuery(name = "Attachs.findByAttachId", query = "SELECT a FROM Attachs a WHERE a.attachId = :attachId"),
    @NamedQuery(name = "Attachs.findByAttachName", query = "SELECT a FROM Attachs a WHERE a.attachName = :attachName"),
    @NamedQuery(name = "Attachs.findByAttachPath", query = "SELECT a FROM Attachs a WHERE a.attachPath = :attachPath"),
    @NamedQuery(name = "Attachs.findByAttachDes", query = "SELECT a FROM Attachs a WHERE a.attachDes = :attachDes"),
    @NamedQuery(name = "Attachs.findByObjectId", query = "SELECT a FROM Attachs a WHERE a.objectId = :objectId"),
    @NamedQuery(name = "Attachs.findByAttachType", query = "SELECT a FROM Attachs a WHERE a.attachType = :attachType"),
    @NamedQuery(name = "Attachs.findByIsDeleted", query = "SELECT a FROM Attachs a WHERE a.isDeleted = :isDeleted")})
public class Attachs implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
     @SequenceGenerator(name = "ATTACHS_SEQ", sequenceName = "ATTACHS_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ATTACHS_SEQ")
    @Basic(optional = false)
    @Column(name = "ATTACH_ID")
    private Long attachId;
    @Column(name = "ATTACH_NAME")
    private String attachName;
    @Column(name = "ATTACH_PATH")
    private String attachPath;
    @Column(name = "ATTACH_DES")
    private String attachDes;
    @Column(name = "OBJECT_ID")
    private Long objectId;
    @Column(name = "ATTACH_TYPE")
    private Long attachType;
    @Column(name = "IS_DELETED")
    private Long isDeleted;

    public Attachs() {
    }

    public Attachs(Long attachId) {
        this.attachId = attachId;
    }

    public Long getAttachId() {
        return attachId;
    }

    public void setAttachId(Long attachId) {
        this.attachId = attachId;
    }

    public String getAttachName() {
        return attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }

    public String getAttachPath() {
        return attachPath;
    }

    public void setAttachPath(String attachPath) {
        this.attachPath = attachPath;
    }

    public String getAttachDes() {
        return attachDes;
    }

    public void setAttachDes(String attachDes) {
        this.attachDes = attachDes;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public Long getAttachType() {
        return attachType;
    }

    public void setAttachType(Long attachType) {
        this.attachType = attachType;
    }

    public Long getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Long isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (attachId != null ? attachId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Attachs)) {
            return false;
        }
        Attachs other = (Attachs) object;
        if ((this.attachId == null && other.attachId != null) || (this.attachId != null && !this.attachId.equals(other.attachId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "core.Hibernate.BO.Inventory.Attachs[ attachId=" + attachId + " ]";
    }
    
}
